$(document).ready(function() {
	var idList = [];
	var url = new URL(window.location.href);
	var instanceID = url.searchParams.get("instance_id");
	parse();
});

function parse(){
	var step_id = "";
	var output = "";
	$
	.ajax({
		type : "GET",
		url : "currentActivity",
		data : {
			instance_id: instanceID
		},
		success : function(result) {
			//alert('Aktiver Schritt: ' + result);
			step_id = result;
			
			$
			.ajax({
				type : "GET",
				url : "processmodel/get",
				data : {
					model: 'studentBewerben',
					step : step_id
				},
				success : function(result) {
						var json = JSON.parse(result);
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
								output = output + '<form><label>' + json[i]["data"]["label"] + ' <select id="' + json[i]["data"]["id"] +'"' + req +'>';
								for (var j = 0; j < json[i]["data"]["values"].length; j++){
									output = output + '<option>' + json[i]["data"]["values"][j] + '</option>';
									//alert ("Option hinzugefügt: " + json[i]["data"]["values"][j]);
								}
								output = output + '</select></label></form><br>';
								idList.push(step_id + i);
								break;
							case "form-text":
								var req = "";
								if (json[i]["data"]["required"] == true){
									req = ' required="required"';
								}
								output = output + '<label>' + json[i]["data"]["label"] + ' </label><input type="' + json[i]["data"]["type"]+ '" id="' + json[i]["data"]["id"] + '"' + req + '>';
								idList.push(step_id + i);
							}
						

					}
						document.getElementById("results").innerHTML = output;
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
	var keyString = "";
	var valString = "";
	for (var j = 0; j < idList.length;j++){
		keyString = keyString + idList[j] + "|";
		valString = valString + document.getElementById(idList[j]).value + "|";
	}
	keyString = keyString.substr(0,keyString.length-1);
	valString = valString.substr(0,valString.length-1);
		
	$
	.ajax({
		type : "POST",
		url : "setVariable",
		data : {
			instance_id: instanceID,
			key : keyString,
			value: valString
		},
		success : function(result) {
			location.reload();
		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten');
		}
	}); 
};