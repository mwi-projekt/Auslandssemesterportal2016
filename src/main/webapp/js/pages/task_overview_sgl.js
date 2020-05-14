import $ from "jquery";
import _,{baseUrl} from "../config";
import Swal from "sweetalert2";
import "bootstrap";
import "jquery-form-validator";
import "jquery-ui-dist";
import "datatables.net-bs4";

$(document).ready(function () {
    getList();
});

function getList() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/getSGLTasks",
        data: {
            //'definition' : 'studentBewerben'
        },
        success: function (result) {
            var output = ""; 		//zu validierende Bewerbungen
            var completed = "";		//angenommene Bewerbungen
            var validateAAA = "";	//Bewerbungen, die vom Auslansdamt bearbeitet werden müssen
            var abgelehnt = "";		//abgelehnte Bewerbungen
            if (!result || result.data.length == 0) {
                // substring bilden nicht möglich bei leerem String
            } else {
                var instances = result.data;

                for (var i = 0; i < instances.length; i++) {
                    singleInstance = instances[i];
                    if (singleInstance.status === 'validateSGL') {
                    	output = output +
                            "<tr><td>" +
                            singleInstance.name +
                            "</td><td>" +
                            singleInstance.vname +
                            "</td><td>" +
                            singleInstance.kurs +
                            "</td><td>" +
                            singleInstance.aktuelleUni +
                            "</td><td>" +
                            singleInstance.uni +
                            "</td><td>" +
                            '<button class="btn fas fa-list" title="Details" onclick="location.href=\'task_detail_sgl.html?instance_id=' +
                            singleInstance.id +
                            '&uni=' +
                            singleInstance.uni +
                            '&verify=true\'\"> </button>' +
                            "</td><td>" +
                            "<button class=\"btn fas fa-trash btn-delete\" title=\"Delete\" onclick=\"deleteProcessButtons('"+singleInstance.uni+"','"+singleInstance.matrikelnummer+"')\"></button></td></tr>";
                 
                    } else if (singleInstance.status === 'complete') {
                        completed = completed +
                            "<tr><td>" +
                            singleInstance.name +
                            "</td><td>" +
                            singleInstance.vname +
                            "</td><td>" +
                            singleInstance.aktuelleUni +
                            "</td><td>" +
                            singleInstance.kurs +
                            "</td><td>" +
                            singleInstance.uni +
                            "</td></tr>";
                    } else if (singleInstance.status === 'abgelehnt') {
                        abgelehnt = abgelehnt +
                            "<tr><td>" +
                            singleInstance.name +
                            "</td><td>" +
                            singleInstance.vname +
                            "</td><td>" +
                            singleInstance.aktuelleUni +
                            "</td><td>" +
                            singleInstance.kurs +
                            "</td><td>" +
                            singleInstance.uni +
                            "</td></tr>";
                                    
                    } else if (singleInstance.status === 'validate') {
                    	validateAAA = validateAAA +
                        "<tr><td>" +
                        singleInstance.name +
                        "</td><td>" +
                        singleInstance.vname +
                        "</td><td>" +
                        singleInstance.aktuelleUni +
                        "</td><td>" +
                        singleInstance.kurs +
                        "</td><td>" +
                        singleInstance.uni +
                        "</td></tr>";
                    }
                   
                }
            }
               
                if (output === "") {
                    output = "<h2>Aktuell gibt es keine Bewerbungen, die überprüft werden müssen</h2>";
                } else {
                    output = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Kurs</th><th>Heimatuniversität</th><th>Partneruniversität</th><th>Prüfen</th><th>Löschen</th></tr></thead><tbody>' +
                        output + "</tbody></table>";
                }
                if (completed === "") {
                    completed = "<h2>Es gibt noch keine abgeschlossenen Bewerbungen</h2>";
                } else {
                    completed = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Heimatuniversität</th><th>Kurs</th><th>Partneruniversität</th></tr></thead><tbody>' +
                        completed + "</tbody></table>";
                }
                
                if (validateAAA === "") {
                	validateAAA = "<h2>Aktuell gibt es keine Bewerbungen, die von einem AAA überprüft werden müssen</h2>";
                } else {
                	validateAAA = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Heimatuniversität</th><th>Kurs</th><th>Partneruniversität</th></tr></thead><tbody>' +
                       	validateAAA + "</tbody></table>";
                }      
                if (abgelehnt === "") {
                	abgelehnt = "<h2>Bisher wurden keine Bewerbungen abgelehnt</h2>";
                } else {
                	abgelehnt = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Heimatuniversität</th><th>Kurs</th><th>Partneruniversität</th></tr></thead><tbody>' +
                   		abgelehnt + "</tbody></table>";
                }

                $(document).ready(function () {
                    $('.table').DataTable();
                });
          
            document.getElementById("resultList").innerHTML = '<h1>Zu validierende Bewerbungen</h1>' + output + '<h1>Bewerbungen beim Auslandsamt</h1>' + validateAAA + '<br><h1>Angenommene Bewerbungen</h1>' + completed + '<br><h1>Abgelehnte Bewerbungen</h1>' + abgelehnt; 
        },
        error: function (result) {
            Swal.fire("Ein Fehler ist aufgetreten", "error");
        }
    });
  

}
function deleteProcessButtons(uni, matrikelnummer) {
	Swal.fire({
            title: "Bist du sicher?",
            text: "Der Prozess kann nicht wiederhergestellt werden! Das hier wird angezeigt :-)",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!"
        }).then(function () {
            $.ajax({
                type: "GET",
                url: baseUrl + "/process/delete",
                data: {
                    matrikelnummer: matrikelnummer,
                    uni: uni
                }
            }).done(function (data) {
                Swal.fire({
                	title: 'Gelöscht!',
                	text: 'Der Prozess wurde erfolgreich gelöscht.',
                    icon: 'success'
                }).then(function() {
                	location.reload();
                });
            }).error(function (error) {
                console.error(error);
                Swal.fire('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
            })
        });
}
function initDeleteProcessButtonsTaskOverview() {
    $('.btn-delete').on('click', function () {
        var uni = $(this).attr("uni");
        var id = $(this).attr("rid");
        Swal.fire({
            title: "Bist du sicher?",
            text: "Der Prozess kann nicht wiederhergestellt werden!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!"
        }).then(function () {
            //var id = $('.btn-delete').closest('tr').data('rid');
            var matrikelnummer = sessionStorage['matrikelnr'];

            $.ajax({
                type: "GET",
                url: baseUrl + "/process/delete",
                data: {
                    matrikelnummer: matrikelnummer,
                    uni: uni
                }
            }).done(function (data) {
                $('#tableBewProzess tr[data-rid=' + id + ']').remove();
                Swal.fire('Gelöscht!', 'Der Prozess wurde erfolgreich gelöscht.', 'success');
            }).error(function (error) {
                console.error(error);
                Swal.fire('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
            })
        });
    });
}

function deleteTask (taskID) {
    //$('.taskdelete').click(function () {
    	alert(taskID);

    	var self = $(this);
	
        Swal.fire({
            title: "Bist du sicher?",
            text: "Die Bewerbung kann nicht wiederhergestellt werden!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!"
        }).then(function(result) {
        	alert("Du hast auf Löschen gedrückt");
        	if (result.value) {
                 alert("Auf Löschen gedrückt");
        		 Swal.fire({
                     title: 'Lösche Bewerbung'
                 });
                 Swal.showLoading();
                 $.ajax({
                     type: "POST",
                     url: baseUrl + "/task/delete",
                     data: {
                         taskId: self.data(taskID)
                     },
                     success: function (result) {
                         Swal.close();
                         $('#userStudShow').click();
                         Swal.fire('Gelöscht!', 'Die Bewerbung wurde erfolgreich gelöscht.', 'success');
                     },
                     error: function (result) {
                         Swal.close();
                         Swal.fire('Fehler', 'Die Bewerbung konnte nicht gelöscht werden', 'error');
                     }
                 });
        	 }
        }); 

   }