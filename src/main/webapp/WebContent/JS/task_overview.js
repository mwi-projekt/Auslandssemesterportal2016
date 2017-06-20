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
			result = result.substring(0, result.length - 1);
			output = '<table class="table table-hover table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Uni</th><th>Prüfen</th></tr></thead><tbody>';
			instances = result.split(";");
			for (var i = 0; i < instances.length; i++){
				singleInstance = instances[i].split("|");
				output = output + "<tr><td>" + singleInstance[1] + "</td><td>" + singleInstance[2] + "</td><td>" + singleInstance[3] + "</td><td>" + 
				'<button class="btn btn-default" onclick="location.href=\'http://193.196.7.215:8080/Auslandssemesterportal/WebContent/task_detail.html?instance_id=' + singleInstance[0] + '&verify=true\'">Details</button>' + "</td></tr>"
				//alert("<tr><td>" + singleInstance[1] + "</td><td>" + singleInstance[2] + "</td><td>" + singleInstance[3] + "</td><td>" + singleInstance[0] + "</td></tr>");
			}
			output = output + "</tbody></table>";
			document.getElementById("resultList").innerHTML = output;
		},
		error : function(result) {
			swal("Ein Fehler ist aufgetreten","error");
		}
	});
}