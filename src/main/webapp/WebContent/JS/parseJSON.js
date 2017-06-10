/* window.onload = function() {
	//Lese JSON-Datei ein
	var jsonString = '[{"type": "title","content": "Page Title"},{"type": "paragraph","content": "Bla"}]';
	var json = JSON.parse(jsonString);
	for (var i = 0;i < json.length; i++){
		alert (json[i][type]);
	}
}; */

function parse(){
	var step_id = document.getElementById("step_id").value;
	var output = "";
	$
	.ajax({
		type : "GET",
		url : "processmodel/get",
		data : {
			model: 'studentBewerben',
			step : step_id
		},
		success : function(result) {
			//TODO: required-Attribut korrekt übergeben
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
						output = output + '<form><label>' + json[i]["data"]["label"] + ' <select id="' + step_id + i +'"' + req +'>';
						for (var j = 0; j < json[i]["data"]["values"].length; j++){
							output = output + '<option>' + json[i]["data"]["values"][j] + '</option>';
							//alert ("Option hinzugefügt: " + json[i]["data"]["values"][j]);
						}
						output = output + '</select></label></form><br>';
						break;
					case "form-text":
						var req = "";
						if (json[i]["data"]["required"] == true){
							req = ' required="required"';
						}
						output = output + '<label>' + json[i]["data"]["label"] + ' </label><input type="' + json[i]["data"]["type"]+ '" id="' + step_id + i +'"' + req + '>';
					}
				
				//alert (output);
				document.getElementById("results").innerHTML = output;
			}
			
		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten: '. result);
		}
	});
}