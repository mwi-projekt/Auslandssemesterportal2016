$(document).ready(function() {
	getList();
});

function getList(){
	output = "<table><tr><td>Name</td><td>Vorname</td><td>Uni</td><td>Prüfen</td></tr>";
	$
	.ajax({
		type : "GET",
		url : "getTasks",
		data : {
		},
		success : function(result) {
			instances = result.split(",");
			for (var i = 0; i < instances.length; i++){
				singleInstance = instances[i].split("|");
				output = output + "<tr><td>" + singleInstance[1] + "</td><td>" + singleInstance[2] + "</td><td>" + singleInstance[3] + "</td><td>" + singleInstance[0] + "</td></tr>";
				alert("<tr><td>" + singleInstance[1] + "</td><td>" + singleInstance[2] + "</td><td>" + singleInstance[3] + "</td><td>" + singleInstance[0] + "</td></tr>");
			}
		},
		error : function(result) {
			swal("Ein Fehler ist aufgetreten","error");
		}
	});
	output = output + "</table>";
	document.getElementById("resultList").innerHTML = output;
}