$(document)
		.ready(
				function() {
					if (sessionStorage['rolle'] === '3') {
						swal(
								{
									title : "Fehler!",
									text : "Sie besitzen nicht die nötigen Rechte um diese Seite zu sehen.",
									type : "error",
									confirmButtonText : "Ok"
								}, function() {
									location.href = 'index.html';
								});
					}

					// init & logout
					$('.nutzerName').text(sessionStorage['User']);
					$('#logout').on('click', function() {
						window.location.href = "logout";
					});

					getList();
				});

function getList() {
	$
			.ajax({
				type : "GET",
				url : "GetAdminTasksServlet",
				data : {
					'definition' : 'studentBewerben'
				},
				success : function(result) {
					output = "";
					completed = "";
					if (result === "") {
						// substring bilden nicht möglich bei leerem String
					} else {
						result = result.substring(0, result.length - 1);
						instances = result.split(";");

						for (var i = 0; i < instances.length; i++) {
							singleInstance = instances[i].split("|");
							if (singleInstance[4] === 'validate') {
								output = output
										+ "<tr><td>"
										+ singleInstance[1]
										+ "</td><td>"
										+ singleInstance[2]
										+ "</td><td>"
										+ singleInstance[3]
										+ "</td><td>"
										+ '<button class="btn btn-default" onclick="location.href=\'http://193.196.7.215:8080/Auslandssemesterportal/WebContent/task_detail.html?instance_id='
										+ singleInstance[0]
										+ '&verify=true\'">Details</button>'
										+ "</td></tr>"
							} else if (singleInstance[4] === 'complete') {
								completed = completed
										+ "<tr><td>"
										+ singleInstance[1]
										+ "</td><td>"
										+ singleInstance[2]
										+ "</td><td>"
										+ singleInstance[3]
										+ "</td><td>"
										+ '<button class="btn btn-default" onclick="location.href=\'http://193.196.7.215:8080/Auslandssemesterportal/WebContent/task_detail.html?instance_id='
										+ singleInstance[0]
										+ '\'">Details</button>'
										+ "</td></tr>"
							}
						}
						if (output === ""){
							output = "<h2>Aktuell gibt es keine Bewerbungen, die überprüft werden müssen</h2>";
						} else {
							output = '<table class="table table-hover table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Uni</th><th>Prüfen</th></tr></thead><tbody>' +
							output + "</tbody></table>";
						}
						if (completed === ""){
							completed = "<h2>Es gibt noch keine abgeschlossenen Bewerbungen</h2>";
						} else {
							completed = '<table class="table table-hover table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Uni</th><th>Prüfen</th></tr></thead><tbody>' +
							completed + "</tbody></table>";
						}


					}
					document.getElementById("resultList").innerHTML = '<h1>Zu validierende Bewerbungen</h1>' + output + '<br><h1>Abgeschlossene Bewerbungen</h1>' + completed;
							+ output;
				},
				error : function(result) {
					swal("Ein Fehler ist aufgetreten", "error");
				}
			});
}
