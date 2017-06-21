var instanceID;
var url;
var typeList;
var verify;
var idList;
$(document).ready(function() {
	idList = [];
	typeList = [];
	url = new URL(window.location.href);
	instanceID = url.searchParams.get("instance_id");
	verify = url.searchParams.get("verify");
	parse();
});

function parse() {
	var step_id = "";
	var output = "";
	$
			.ajax({
				type : "GET",
				url : "getOverview",
				data : {
					instance_id : instanceID,
					definition : 'studentBewerben'
				},
				success : function(result) {
					output = output + '<div class="panel-group" id="accordion">';
					result = result.substring(0, result.length - 1);
					steps = result.split(";");
					for (var k = 0; k < steps.length; k++){
						collapsible = steps[k].split("|");
						stepName = collapsible[0]; //Name des aktiven Prozessschrittes
						output = output + '<div class="panel panel-default"><div class="panel-heading"><h4 class="panel-title"><a data-toggle="collapse" data-parent="#accordion" href="#collapse' +
						k + '">' + stepName + '</a></h4></div>'; //Header des Accordions
						output = output + ' <div id="collapse' + k + '" class="panel-collapse collapse in"><div class="panel-body">'

						var json = JSON.parse(decodeURI(collapsible[1]));
						for (var i = 0; i < json.length; i++) {
							var type = json[i]["type"];
							// alert (type);
							switch (type) {
							case "form-select":
								var req = "";
								if (json[i]["data"]["required"] == true) {
									req = ' required="required"';
								}
								output = output
										+ '<div class="form-group"><label class="col-sm-2 control-label">'
										+ json[i]["data"]["label"]
										+ '</label><div class="col-sm-10"><select class="form-control" id="'
										+ json[i]["data"]["id"] + '"' + req + '>';
								for (var j = 0; j < json[i]["data"]["values"].length; j++) {
									output = output + '<option>'
											+ json[i]["data"]["values"][j]
											+ '</option>';
									// alert ("Option hinzugefügt: " +
									// json[i]["data"]["values"][j]);
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
								output = output
										+ '<div class="form-group"><label class="col-sm-2 control-label">'
										+ json[i]["data"]["label"]
										+ ' </label><div class="col-sm-10"><input class="form-control" type="'
										+ json[i]["data"]["type"] + '" id="'
										+ json[i]["data"]["id"] + '"' + req
										+ '></div></div>';
								idList.push(json[i]["data"]["id"]);
								typeList.push(json[i]["data"]["type"]);
								break;
							case "form-checkbox":
								output = output
										+ '<div class="form-group"><div class="col-sm-offset-2 col-sm-10"><div class="checkbox"><label><input type="checkbox" id="'
										+ json[i]["data"]["id"] + '"> '
										+ json[i]["data"]["label"]
										+ ' </label></div></div></div>';
								idList.push(json[i]["data"]["id"]);
								typeList.push("boolean");
								break;
							case "form-upload":
								output = output + 'Download link for ' + json[i]["data"]["filename"];
								break;
							}
					}
					output = output + '</div></div></div><br>';

					}
					output = output + '</div>';
					document.getElementById("taskDetails").innerHTML = output;
					for (var m = 0; m < k; m++){
					$('#collapse' + m).collapse("hide");
					}
					getData();

				},
				error : function(result) {
					alert('Ein Fehler ist aufgetreten. Aktiver Schritt konnte nicht abgerufen werden.');
				}
			});
}


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
			//alert("Setze Element mit ID " + idList[m] + "auf Wert " + values[m]);
			$("#" + idList[m]).val(values[m]);
		}
	},
	error : function(result) {
		alert('Ein Fehler ist aufgetreten');
	}
});
	
}



function saveData() {
	var keyString = "";
	var valString = "";
	var typeString = "";
	for (var j = 0; j < idList.length; j++) {

		if ($('#' + idList[j]).attr('type') == 'checkbox') {
			var checkedString = (document.getElementById(idList[j]).checked) ? 'true'
					: 'false';
			keyString = keyString + idList[j] + "|";
			valString = valString + checkedString + "|";
			typeString = typeString + typeList[j] + "|";
		} else {
			keyString = keyString + idList[j] + "|";
			valString = valString + document.getElementById(idList[j]).value
					+ "|";
			typeString = typeString + typeList[j] + "|";
		}
	}
	keyString = keyString.substr(0, keyString.length - 1);
	valString = valString.substr(0, valString.length - 1);
	typeString = typeString.substr(0, typeString.length - 1);
	// alert(keyString);
	// alert(valString);
	// alert(typeString);
	$.ajax({
		type : "POST",
		url : "setVariable",
		data : {
			instance_id : instanceID,
			key : keyString,
			value : valString,
			type : typeString
		},
		success : function(result) {
			location.reload();
		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten');
		}
	});
};