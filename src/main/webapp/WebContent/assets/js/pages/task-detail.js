var instanceID;
var url;
var typeList;
var verify;
var idList;
var sendBew;
var processDefinition;

$(document).ready(function () {
    idList = [];
    typeList = [];
    url = new URL(window.location.href);
    instanceID = url.searchParams.get("instance_id");
    verify = url.searchParams.get("verify");
    sendBew = url.searchParams.get("send_bew");
    var uni = url.searchParams.get("uni");
    if (!(verify === "true")) {
        if (!(sendBew === "true")) {
            $('#saveChanges').hide();
        }
        $('#validate').hide();
        $('#nav2').hide();
    } else {
        $('#saveChanges').hide();
        $('#nav3').hide();
    }
    $('#Sonstige Angaben').hide();
    $('#reason').hide();
    $('#reasonl').hide();
    $('#validateBtn').prop('disabled', true);


    $.ajax({
        type: "GET",
        url: baseUrl + "/currentActivity",
        data: {
            instance_id: instanceID,
            uni: uni
        },
        success: function (result) {
            step_id = result.active;
            processDefinition = result.data;
            parse();
        }
    });
    
});

function manipulateDOM() {
	
    $("[id='Sonstige Angaben']").hide();

}

function parse() {
    var output = "";
    $.ajax({
        type: "GET",
        url: baseUrl + "/getOverview",
        data: {
            instance_id: instanceID,
            definition: processDefinition
        },

        success: function (result) {
            var steps = result.data;
            output = output +
                '<div class="panel-group" id="accordion">';
            for (var k = 0; k < steps.length; k++) {
                data = steps[k].data;
                stepName = steps[k].activity;

                if (data.search("id") != -1) {
                    var innerOutput = "";
                    var json = JSON.parse(decodeURI(data));
                    for (var i = 0; i < json.length; i++) {
                        var type = json[i]["type"];
                        // alert (type);
                        switch (type) {
                            case "form-select":
                                var req = "";
                                if (json[i]["data"]["required"] == true) {
                                    req = ' required="required"';
                                    dis = ' disabled ="disabled"';
                                }
                                innerOutput = innerOutput +
                                    '<div class="form-group"><label class="col-sm-2 control-label">' +
                                    json[i]["data"]["label"] +
                                    '</label><div class="col-sm-10"><select class="form-control" id="' +
                                    json[i]["data"]["id"] + '"' + req + dis +
                                    '>';
                                for (var j = 0; j < json[i]["data"]["values"].length; j++) {
                                    innerOutput = innerOutput + '<option>' +
                                        json[i]["data"]["values"][j] +
                                        '</option>';
                                }
                                innerOutput = innerOutput + '</select></div></div>';
                                idList.push(json[i]["data"]["id"]);
                                typeList.push("text");
                                break;
                            case "form-text":
                                var req = "";
                                if (json[i]["data"]["required"] == true) {
                                    req = ' required="required"';
                                }
                                innerOutput = innerOutput +
                                    '<div class="form-group"><label class="col-sm-2 control-label">' +
                                    json[i]["data"]["label"] +
                                    ' </label><div class="col-sm-10"><input class="form-control" type="' +
                                    json[i]["data"]["type"] +
                                    '" id="' + json[i]["data"]["id"] +
                                    '"' + req + '></div></div>';
                                idList.push(json[i]["data"]["id"]);
                                typeList.push(json[i]["data"]["type"]);
                                break;
                            case "form-checkbox":
                                innerOutput = innerOutput +
                                    '<div class="form-group"><div class="col-sm-offset-2 col-sm-10"><div class="checkbox"><label><input type="checkbox" id="' +
                                    json[i]["data"]["id"] +  '"disabled> ' + 
                                    json[i]["data"]["label"] +
                                    ' </label></div></div></div>';
                                idList.push(json[i]["data"]["id"]);
                                typeList.push("boolean");
                                break;
                            case "form-upload":
                                break;
                        }
                    }
                    
                    //console.log(idList);

                    if (innerOutput != '') {
                        if (stepName === "datenEingeben") {
                            visibleStepName = "Persönliche Daten";
                        } else if (stepName === "datenEingebenUnt") {
                            visibleStepName = "Partnerunternehmen";
                        } else if (stepName === "Task_1jq3nab") {
                            visibleStepName = "Semesteranschrift";
                        } else if (stepName === "englischNotePruefen") {
                            visibleStepName = "Notenpunkte im Abitur";
                        } else {
                            visibleStepName = "Sonstige Angaben";
                        }

                        output = output +
                            '<div class="panel panel-default" id="'+visibleStepName+'"><div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" href="#collapse' +
                            k + '">' + visibleStepName + '</a></h4></div>'; // Header
                        // des
                        // Accordions
                        output = output +
                            ' <div id="collapse' +
                            k +
                            '" class="panel-collapse collapse in"><div class="panel-body">'
                        output = output + innerOutput;
                        output = output + '</div></div></div><br>';
                    }
                }
            }

            output = output + '<div class="panel panel-default"><div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" href="#collapse-downloads">Dateien</a></h4></div>'; // Header
            output = output +
                ' <div id="collapse-downloads" class="panel-collapse collapse in"><div class="panel-body" id="downloadsBody">'
            output = output + '</div></div></div><br>';

            output = output + '</div>';
            document.getElementById("taskDetails").innerHTML = output;

            for (var k = 0; k < steps.length; k++) {
                data = steps[k].data;
                stepName = steps[k].activity;
                if (data.search("id") != -1) {
                    var json = JSON.parse(decodeURI(data));
                    for (var i = 0; i < json.length; i++) {
                        var type = json[i]["type"];
                        switch (type) {
                            case "form-upload":
                                var file = json[i];
                                getAccordionFile(file);
                                break;
                        }
                    }
                }
            }
            getData();
            manipulateDOM();
            
        },
        error: function (result) {
            alert('Ein Fehler ist aufgetreten. Aktiver Schritt konnte nicht abgerufen werden.');
        }
    });
}

function getAccordionFile(file) {
    $.ajax({
        type: "HEAD",
        url: baseUrl + "/getProcessFile",
        data: {
            instance_id: instanceID,
            key: file["data"]["id"]
        },
        success: function (result) {
            $('#downloadsBody').append('<a href="' + baseUrl + '/getProcessFile?instance_id=' + instanceID + '&key=' +
                file["data"]["id"] + '" target="blank">' + file["data"]["filename"] + '</a><br />');
        }
    });
}

function getData() {
    var keyString = "";
    for (var l = 0; l < idList.length; l++) {
        keyString = keyString + idList[l] + "|";
    }
    keyString = keyString.substr(0, keyString.length - 1);

    $.ajax({
        type: "GET",
        url: baseUrl + "/getVariables",
        data: {
            instance_id: instanceID,
            key: keyString
        },
        success: function (result) {
            $.each(result, function (key, value) {
                $('#' + key).val(value);
                if(key === 'muttersprache' || key === 'semesteradresseAnders'){
                	$('#' + key).prop("checked", value);
//                	variableEnglishAndSemesteranschrift(key,value);
                }
                if (!(sendBew === "true")) {
                    $("#" + key).prop('readonly', true);
                }
            });
        },
        error: function (result) {
            alert('Ein Fehler ist aufgetreten');
        }
    });
}

function variableEnglishAndSemesteranschrift(key, value){
	if(key === 'muttersprache' && value === true){
		document.getElementById("Englischnote im Abitur in Punkten").remove();
	}else if(key === 'semesteradresseAnders' && value === false){
		document.getElementById("Semesteranschrift").remove();
	}
	
}

function saveChanges() {
    var keyString = "";
    var valString = "";
    var typeString = "";
    for (var j = 0; j < idList.length; j++) {
        if ($('#' + idList[j]).attr('type') == 'checkbox') {
            var checkedString = (document.getElementById(idList[j]).checked) ? 'true' :
                'false';
            keyString = keyString + idList[j] + "|";
            valString = valString + checkedString + "|";
            typeString = typeString + typeList[j] + "|";
        } else {
            keyString = keyString + idList[j] + "|";
            valString = valString + document.getElementById(idList[j]).value + "|";
            typeString = typeString + typeList[j] + "|";
        }
    }
    keyString = keyString.substr(0, keyString.length - 1);
    valString = valString.substr(0, valString.length - 1);
    typeString = typeString.substr(0, typeString.length - 1);
    swal({
            title: "Bewerbung absenden",
            text: "Wenn Du die Bewerbung abschickst, kannst Du keine Änderungen mehr vornehmen. Fortfahren?",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Bewerbung absenden",
            cancelButtonText: "Abbrechen",
            closeOnConfirm: false
        },
        function () {
            $.ajax({
                type: "POST",
                url: baseUrl + "/setVariable",
                data: {
                    instance_id: instanceID,
                    key: keyString,
                    value: valString,
                    type: typeString
                },
                success: function (result) {
                    swal({
                        title: "Bewerbung eingereicht",
                        text: "Deine Bewerbung wurde eingereicht. Du erhältst möglichst Zeitnah eine Rückmeldung per Email",
                        type: "success",
                        confirmButtonText: "Ok"
                    }, function () {
                        location.href = 'bewerbungsportal.html';
                    });
                },
                error: function (result) {
                    sweetAlert("Fehler", "Ein Fehler ist aufgetreten", "error");
                }
            });
        });
}

function validateBew() {
    validateString = $('#validierungErfolgreich').val();
    grund = $('#reason').text();
    resultString = "";
    if (validateString === "true") {
        resultString = "bestätigen"
    } else if (validateString === "false"){
        resultString = "ablehnen"
    } else if (validateString === "edit"){
    	resultString = "zur Bearbeitung freigeben"
    }
    if (grund.indexOf("Platzhalter") < 0 ||
        grund.indexOf("--") < 0) {
        swal({
            title: "Platzhalter",
            text: "Mögliche Platzhalter im Email Text gefunden.",
            type: warning,
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Ignorieren",
            cancelButtonText: "Abbrechen"
        });
    } else {
        console.log('In der Nachricht wurden keine Platzhalter gefunden');
    }

    swal({
        title: "Bewerbung " + resultString,
        text: "Sind Sie sicher? Diese Aktion kann nicht rückgängig gemacht werden.",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "Bewerbung " + resultString,
        cancelButtonText: "Abbrechen",
        closeOnConfirm: false
    }, function () {
        $.ajax({
            type: "POST",
            url: baseUrl + "/setVariable",
            data: {
                instance_id: instanceID,
                key: 'validierungErfolgreich|mailText',
                value: validateString + '|' + grund,
                type: 'boolean|text'  //bei einem Fehler ersteres evtl. wieder zu boolean umändern. 
            },
            success: function (result) {
                swal({
                    title: "Bewerbung " + resultString,
                    text: "Gespeichert",
                    type: "success",
                    confirmButtonText: "Ok"
                }, function () {
                    location.href = 'task_overview.html';
                });
            },
            error: function (result) {
                alert('Ein Fehler ist aufgetreten');
            }
        });
    });

}

function change(obj) {
    var selectBox = obj;
    var selected = selectBox.options[selectBox.selectedIndex].value;

    if (selected === '') {
        $('#reason').hide();
        $('#reasonl').hide();
        $('#platzhalterInfo').hide();
        $('#validateBtn').prop('disabled', true);
    } else {
        $.ajax({
            type: "GET",
            url: baseUrl + "/getMailText",
            data: {
                instance_id: instanceID,
                validate: selected
            },
            success: function (result) {
                $('#reason').text(result);
                $('#validateBtn').prop('disabled', false);
                $('#reason').show();
                $('#reasonl').show();
                $('#platzhalterInfo').show();
            },
            error: function (result) {
                alert('Fehler beim Abrufen des Mailtextes');
            }
        });
    }
}