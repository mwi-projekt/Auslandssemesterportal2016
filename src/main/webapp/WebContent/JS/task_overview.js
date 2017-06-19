$(document).ready(function() {
	getList();
});

function getList(){
	$
	.ajax({
		type : "GET",
		url : "getTasks",
		data : {
		},
		success : function(result) {
			output = "<table clases=\"table table-border table-hover\"><tr><td>Name</td><td>Vorname</td><td>Uni</td><td>Prüfen</td></tr>";
			instances = result.split(",");
			for (var i = 0; i < instances.length; i++){
				singleInstance = instances[i].split("|");
				output = output + "<tr><td>" + singleInstance[1] + "</td><td>" + singleInstance[2] + "</td><td>" + singleInstance[3] + "</td><td>" + 
				'<button href="http://193.196.7.215:8080/Auslandssemesterportal/WebContent/task_detail.html?instance_id=' + singleInstance[0] + '&verify=true></button>' + "</td></tr>";
				//alert("<tr><td>" + singleInstance[1] + "</td><td>" + singleInstance[2] + "</td><td>" + singleInstance[3] + "</td><td>" + singleInstance[0] + "</td></tr>");
			}
			output = output + "</table>";
			document.getElementById("resultList").innerHTML = output;
		},
		error : function(result) {
			swal("Ein Fehler ist aufgetreten","error");
		}
	});
}