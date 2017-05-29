/* window.onload = function() {
	//Lese JSON-Datei ein
	var jsonString = '[{"type": "title","content": "Page Title"},{"type": "paragraph","content": "Bla"}]';
	var json = JSON.parse(jsonString);
	for (var i = 0;i < json.length; i++){
		alert (json[i][type]);
	}
}; */

function parse(){
	var jsonString = document.getElementById("json").value;
	var json = JSON.parse(jsonString);
	var output = "";
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
		}
	}
	//alert (output);
	document.getElementById("results").innerHTML = output;
}