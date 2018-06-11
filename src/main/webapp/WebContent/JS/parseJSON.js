var instanceID;
var uni;
var url;
var typeList;
var idList;
var json;

$(document).ready(function() {
	idList = [];
	typeList = [];
	url = new URL(window.location.href);
	instanceID = url.searchParams.get("instance_id");
	uni = url.searchParams.get("uni");

    // init & logout
    $('.nutzerName').text(sessionStorage['User']);
    $('#logout').on('click', function() {
		window.location.href = "logout";
    });

	parse();
});

function getDropzoneOptions(action, fileName) {
    return {
        acceptedFiles: 'application/pdf',
        maxFilesize: 16,
        addRemoveLinks: true,
        sending: function(file, xhr, formData){
            formData.append('action', action);
            formData.append('matrikelnummer', sessionStorage['matrikelnr']);
            formData.append('uni', sessionStorage['uni']);
        },
        accept: function(file, done){
            if(file.name != fileName) {
                swal("Fehler", "Bitte beachte die Syntax zur Benennung des Dokuments: " + fileName, "error");
                this.removeFile(file);
            }else{
                done();
            }
        },
        error: function(file, response){
            if($.type(response) === "string"){
                var message = response;
            } else {
                var message = response.message;
            }

            swal('Fehler', message, 'error');
            this.removeFile(file);
        }
    }
}

function parse(){
	var step_id = "";
	var output = "";
	$
	.ajax({
		type : "GET",
		url : "currentActivity",
		data : {
			instance_id: instanceID,
			uni: uni
		},
		success : function(result) {
			//alert('Aktiver Schritt: ' + result);
			var resultArr = result.split(';');
			step_id = resultArr[0];
			var model = resultArr[1];
			if (step_id === "datenPruefen"){
				location.href = 'task_detail.html?instance_id=' + instanceID + '&uni='+uni+'&send_bew=true';
			}
			$
			.ajax({
				type : "GET",
				url : "processmodel/get",
				data : {
					model: model,
					step : step_id
				},
				success : function(result) {
						json = JSON.parse(decodeURI(result));
						for (var i = 0; i < json.length; i++){
							var type = json[i]["type"];
							//alert (type);
							switch (type){
							case "title":
								output = output + '<h1>' + json[i]["content"]+ '</h1><br>';
								break;
							case "subtitle":
								output = output + '<h3>' + json[i]["content"]+ '</h3><br>';
								break;
							case "paragraph":
								output = output + '<p>' + json[i]["content"]+ '</p><br>';
								break;
							case "form-select":
								var req = "";
								if (json[i]["data"]["required"] == true){
									req = ' required="required"';
								}
								output = output + '<div class="form-group"><label class="col-sm-2 control-label">' + json[i]["data"]["label"] + '</label><div class="col-sm-10"><select class="form-control" id="' + json[i]["data"]["id"] +'"' + req +'>';
								for (var j = 0; j < json[i]["data"]["values"].length; j++){
									output = output + '<option>' + json[i]["data"]["values"][j] + '</option>';
									//alert ("Option hinzugefügt: " + json[i]["data"]["values"][j]);
								}
								output = output + '</select></div></div>';
								idList.push(json[i]["data"]["id"]);
								typeList.push("text");
								break;
							case "form-text":
								var req = "";
								var isPlz = "";
								if (json[i]["data"]["required"] == true){
									req = ' required="required"';
								}
								output = output + '<div class="form-group"><label class="col-sm-2 control-label">' + json[i]["data"]["label"] + ' </label><div class="col-sm-10"><input class="form-control" type="' + json[i]["data"]["type"]+ '" ';

								if (json[i]["data"]["numchars"]) {
									output += 'maxlength="'+ json[i]["data"]["numchars"] +'" ';
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
                                output = output + '<form action="upload_new" class="dropzone" id="'+json[i]["data"]["id"]+'"></form>';
								break;
						}


					}
					document.getElementById("formular").innerHTML = output;
					if (idList.length > 0){
						getData();
					}
                    for (var i = 0; i < json.length; i++) {
                        var type = json[i]["type"];
                        if (type == 'form-upload') {
                            $("#"+json[i]["data"]["id"]).dropzone(getDropzoneOptions(json[i]["data"]["id"], json[i]["data"]["filename"]));
						}
                    }
                    $.validate({
                        form : '#formular',
						lang : 'de',
                        modules : 'html5'
                    });
				},
				error : function(result) {
					alert('Ein Fehler ist aufgetreten: ' + result);
				}
			});

		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten. Aktiver Schritt konnte nicht abgerufen werden.');
		}
	});
}

function saveData(){

    var form = $('#formular');

    if (form && !form.isValid()) {
        swal('Bitte füllen sie alle Felder korrekt aus.');
        return;
    }

    for (var i = 0; i < json.length; i++) {
        var type = json[i]["type"];
        //alert (type);
        switch (type) {
            case "form-upload":
                var dropzoneForm = $('#'+json[i]["data"]["id"])[0].dropzone;
                if (!dropzoneForm.getAcceptedFiles() || dropzoneForm.getAcceptedFiles().length <= 0) {
                    swal('Bitte laden Sie zunächst eine Datei hoch');
                    return;
                }
        }
    }

	var keyString = "";
	var valString = "";
	var typeString = "";
	for (var j = 0; j < idList.length;j++){

		if ($('#'+idList[j]).attr('type') == 'checkbox') {
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
	keyString = keyString.substr(0,keyString.length-1);
	valString = valString.substr(0,valString.length-1);
	typeString = typeString.substr(0,typeString.length-1);
	//alert(keyString);
	//alert(valString);
	//alert(typeString);
	$
	.ajax({
		type : "POST",
		url : "setVariable",
		data : {
			instance_id: instanceID,
			key : keyString,
			value: valString,
			type: typeString
		},
		success : function(result) {
			location.reload();
		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten');
		}
	});
};

function getData(){
	var keyString = "";
for (var l = 0; l < idList.length; l++)	{
	keyString = keyString + idList[l]+"|";
	}
keyString = keyString.substr(0, keyString.length - 1);

$.ajax({
	type : "GET",
	url : "getVariable",
	data : {
		instance_id : instanceID,
		key : keyString
	},
	success : function(result) {
		values = result.split("|");
		for (var m = 0; m < values.length; m++){
			$('#' + idList[m]).val(values[m]);
		}
	},
	error : function(result) {
		alert('Ein Fehler ist aufgetreten');
	}
});

}
