var instanceID;
var uni;
var idList = [];
var typeList = [];


$(document).ready(function () {
    var url = new URL(window.location.href);
    instanceID = url.searchParams.get("instance_id");
    uni = url.searchParams.get("uni");
 
    parse();
        
  //automatische Ergänzung des Ortes anhand der PLZ
    $(document).bind('#bewPLZ','keyup change', function (e) {
        if ($(this).val().length > 4) {
            var ort = $('#bewOrt');
            $.getJSON('https://secure.geonames.org/postalCodeLookupJSON?&country=DE&username=mwidhbw&callback=?', { postalcode: this.value }, function (response) {
                if (response && response.postalcodes.length && response.postalcodes[0].placeName) {
                    ort.val(response.postalcodes[0].placeName);
                }
            })
        } else {
            $('#bewOrt').val('');
        }
    });
});

/*if(document.readyState === 'ready') {
	console.log("readyState = Complete");
	showGDPRComplianceDialogue();
}*/

/*$( document ).ready(function() {
    console.log( "ready!" );
}); */

/*$( window ).on( "load", function() { console.log("Log"); })*/

window.onload = function() {
	  console.log("Log");
	};

function showGDPRComplianceDialogue() {
	swal({
		title: "DSGVO zustimmen",
		text: "Yo Bro, wir verkaufen deine Daten an Facebook.\n Ist das in Ordnung für dich?",
		type: "warning",
		showCancelButton: true,
        cancelButtonText: "DSGVO ablehnen",
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "DSGVO akzeptieren",
        closeOnConfirm: false,
        closeOnCancel: false
	}, function (inputValue) {
		if(inputValue===true){
			swal('Vielen Dank!', 'Sie haben ihre Zustimmung zur Datenverarbeitung erteilt', 'success');
			saveData();
		} else {
			window.location.href = 'bewerbungsportal.html'
		}
	})
}
	
/*	function(inputValue){
  //Use the "Strict Equality Comparison" to accept the user's input "false" as string)
  if (inputValue===false) {
    swal("Well done!");
    console.log("Do here everything you want");
  } else {
    swal("Oh no...","press CANCEL please!");
    console.log("The user says: ", inputValue);
  }
	
	window.location.href = 'task_overview.html'
	
		buttons: {
			cancel: "DSGVO ablehnen",
			confirm: "DSGVO zustimmen"
		}
	})
	
	    $('.btn-delete').on('click', function () {
        var uni = $(this).attr("uni");
        var id = $(this).attr("rid");
        swal({
            title: "Bist du sicher?",
            text: "Der Prozess kann nicht wiederhergestellt werden!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!",
            closeOnConfirm: false
        }, function () {
        	swal('Vielen Dank!', 'Sie haben ihre Zustimmung zur Datenverarbeitung erteilt', 'success');
        }    
            
            swal('Vielen Dank!', 'Sie haben ihre Zustimmung zur Datenverarbeitung erteilt', 'success');
        
        swal({
        	title: "Vielen Dank!"
        	text: "Sie haben ihre Zustimmung zur Datenverarbeitung erteilt",
        
        })
        }
        
        swal(
	
	.then( val => {
		if(val) {
			swal({
				title: "Vielen Dank!",
				text: "Sie haben ihre Zustimmung zur Datenverarbeitung erteilt",
				icon: "success"
			}).then(saveData());
		}
	})
} */

function parse() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/currentActivity",
        data: {
            instance_id: instanceID,
            uni: uni
        },
        success: function (result) {
            var output = "";
            var step_id = result.active;
            var model = result.data;
            if (step_id === "datenPruefen") {
                location.href = 'task_detail.html?instance_id=' + instanceID + '&uni=' + uni + '&send_bew=true';
            }
            $.ajax({
                type: "GET",
                url: baseUrl + "/processmodel/get",
                data: {
                    model: model,
                    step: step_id
                },
                success: function (result) {
                    // generate html output
                    json = JSON.parse(decodeURI(result));
                    for (var i = 0; i < json.length; i++) {
                        var type = json[i]["type"];
                        switch (type) {
                            case "title":
                                output = output + '<h1>' + json[i]["content"] + '</h1><br>';
                                break;
                            case "subtitle":
                                output = output + '<h3>' + json[i]["content"] + '</h3><br>';
                                break;
                            case "paragraph":
                                output = output + '<p>' + json[i]["content"] + '</p><br>';
                                break;
                            case "form-select":
                                var req = "";
                                if (json[i]["data"]["required"] == true) {
                                    req = ' required="required"';
                                }
                                output = output + '<div class="form-group"><label class="col-sm-2 control-label">' + json[i]["data"]["label"] + '</label><div class="col-sm-10"><select class="form-control" id="' + json[i]["data"]["id"] + '"' + req + '>';
                                for (var j = 0; j < json[i]["data"]["values"].length; j++) {
                                    output = output + '<option>' + json[i]["data"]["values"][j] + '</option>';
                                }
                                output = output + '</select></div></div>';
                                idList.push(json[i]["data"]["id"]);
                                typeList.push("text");
                                break;
                            case "form-text":
                                var req = "";
                                if (json[i]["data"]["required"] == true) {
                                    req = ' required="required"';
                                }
                                output = output + '<div class="form-group"><label class="col-sm-2 control-label">' + json[i]["data"]["label"] + ' </label><div class="col-sm-10"><input class="form-control" type="' + json[i]["data"]["type"] + '" ';

                                if (json[i]["data"]["numchars"]) {
                                    output += 'maxlength="' + json[i]["data"]["numchars"] + '" ';
                                }
                                output += 'id="' + json[i]["data"]["id"] + '"' + req + '></div></div>';

                                idList.push(json[i]["data"]["id"]);
                                typeList.push(json[i]["data"]["type"]);
                                break;
                            case "form-checkbox":
                                output = output + '<div class="form-group"><div class="col-sm-offset-2 col-sm-10"><div class="checkbox"><label><input type="checkbox" id="' + json[i]["data"]["id"] + '"> ' + json[i]["data"]["label"] + ' </label></div></div></div>';
                                idList.push(json[i]["data"]["id"]);
                                typeList.push("boolean");
                                break;
                            case "form-upload":
                                output = output + '<form action="' + baseUrl + '/upload" class="dropzone" id="' + json[i]["data"]["id"] + '"></form>';
                                break;
                        }
                    }

                   
                    // set content
                    document.getElementById("formular").innerHTML = output;
                    if (idList.length > 0) {
                        getData();
                    }
                    // init upload
                    for (var i = 0; i < json.length; i++) {
                        var type = json[i]["type"];
                        if (type == 'form-upload') {
                            $("#" + json[i]["data"]["id"]).dropzone(getDropzoneOptions(json[i]["data"]["id"], json[i]["data"]["filename"]));
                        }
                    }
                    // init validation
                    $.validate({
                        form: '#formular',
                        lang: 'de',
                        modules: 'html5'
                    });
                },
                error: function (result) {
                    alert('Ein Fehler ist aufgetreten: ' + result);
                }
            });

        },
        error: function (result) {
            alert('Ein Fehler ist aufgetreten. Aktiver Schritt konnte nicht abgerufen werden.');
        }
    });
}

function saveData() {
    var form = $('#formular');

    if (form && !form.isValid()) {
        swal('Bitte füllen sie alle Felder korrekt aus.');
        return;
    }

    for (var i = 0; i < json.length; i++) {
        var type = json[i]["type"];
        switch (type) {
            case "form-upload":
                var dropzoneForm = $('#' + json[i]["data"]["id"])[0].dropzone;
                if (!dropzoneForm.getAcceptedFiles() || dropzoneForm.getAcceptedFiles().length <= 0) {
                    swal('Bitte laden Sie zunächst eine Datei hoch');
                    return;
                }
        }
    }

    var keyString = "";
    var valString = "";
    var typeString = "";
    for (var j = 0; j < idList.length; j++) {

        if ($('#' + idList[j]).attr('type') == 'checkbox') {
            var checkedString = (document.getElementById(idList[j]).checked) ? 'true' : 'false';
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
            location.reload();
        },
        error: function (result) {
            alert('Ein Fehler ist aufgetreten');
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
            $.each(result, function(key, value) {
                $('#' + key).val(value);
            });
        },
        error: function (result) {
            alert('Ein Fehler ist aufgetreten');
        }
    });

}

function getDropzoneOptions(action, fileName) {
    return {
        acceptedFiles: 'application/pdf',
        maxFilesize: 16,
        addRemoveLinks: true,
        sending: function (file, xhr, formData) {
            formData.append('action', action);
            formData.append('instance', instanceID);
        },
        accept: function (file, done) {
            if (file.name != fileName) {
                swal("Fehler", "Bitte beachte die Syntax zur Benennung des Dokuments: " + fileName, "error");
                this.removeFile(file);
            } else {
                done();
            }
        },
        error: function (file, response) {
            if ($.type(response) === "string") {
                var message = response;
            } else {
                var message = response.message;
            }

            swal('Fehler', message, 'error');
            this.removeFile(file);
        }
    }
}
