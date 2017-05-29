window.onload = function() {
	//Lese JSON-Datei ein
	var jsonString = '{[{"type": "title","content": "Page Title"},{"type": "paragraph","content": "Bla"}]}';
	var json = JSON.parse(jsonString);
	for (var i = 0;i < json.length; i++){
		alert (json[i][type]);
	}
};