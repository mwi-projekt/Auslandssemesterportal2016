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
				url : "getTasks",
				data : {
					//'definition' : 'studentBewerben'
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
										+ '&uni='
										+ singleInstance[3]
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
										+ '&uni='
										+ singleInstance[3]
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
function sortTable(n) {
  var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
  table = document.getElementById("testtable");
  switching = true;
  // Set the sorting direction to ascending:
  dir = "asc"; 
  /* Make a loop that will continue until
  no switching has been done: */
  while (switching) {
    // Start by saying: no switching is done:
    switching = false;
    rows = table.getElementsByTagName("TR");
    /* Loop through all table rows (except the
    first, which contains table headers): */
    for (i = 1; i < (rows.length - 1); i++) {
      // Start by saying there should be no switching:
      shouldSwitch = false;
      /* Get the two elements you want to compare,
      one from current row and one from the next: */
      x = rows[i].getElementsByTagName("TD")[n];
      y = rows[i + 1].getElementsByTagName("TD")[n];
      /* Check if the two rows should switch place,
      based on the direction, asc or desc: */
      if (dir == "asc") {
        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
          // If so, mark as a switch and break the loop:
          shouldSwitch = true;
          break;
        }
      } else if (dir == "desc") {
        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
          // If so, mark as a switch and break the loop:
          shouldSwitch = true;
          break;
        }
      }
    }
    if (shouldSwitch) {
      /* If a switch has been marked, make the switch
      and mark that a switch has been done: */
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
      // Each time a switch is done, increase this count by 1:
      switchcount ++; 
    } else {
      /* If no switching has been done AND the direction is "asc",
      set the direction to "desc" and run the while loop again. */
      if (switchcount == 0 && dir == "asc") {
        dir = "desc";
        switching = true;
      }
    }
  }
}