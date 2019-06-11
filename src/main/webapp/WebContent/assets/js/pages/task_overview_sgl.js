


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
            output = "";
            completed = "";
            validateAAA = "";
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
                            '<button class="btn glyphicon glyphicon-list" title="Details" onclick="location.href=\'task_detail_sgl.html?instance_id=' +
                            singleInstance.id +
                            '&uni=' +
                            singleInstance.uni +
                            '&verify=true\'\"> </button>' +
                            "</td><td>" +
                            "<button class=\"btn glyphicon glyphicon-trash btn-delete\" title=\"Delete\" onclick=\"deleteProcessButtons('"+singleInstance.uni+"','"+singleInstance.matrikelnummer+"')\"></button></td></tr>";
                 
                    } else if (singleInstance.status === 'complete') {
                        completed = completed +
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
                            "</td></tr>";
                            
                    } else if (singleInstance.status === 'validate') {
                    	validateAAA = validateAAA +
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

                $(document).ready(function () {
                    $('.table').DataTable();
                });
          
            document.getElementById("resultList").innerHTML = '<h1>Zu validierende Bewerbungen</h1>' + output + '<h1>Bewerbungen beim Auslandsamt</h1>' + validateAAA + '<br><h1>Abgeschlossene Bewerbungen</h1>' + completed; 
        },
        error: function (result) {
            swal("Ein Fehler ist aufgetreten", "error");
        }
    });
  

}
function deleteProcessButtons(uni, matrikelnummer) {
	swal({
            title: "Bist du sicher?",
            text: "Der Prozess kann nicht wiederhergestellt werden! Das hier wird angezeigt :-)",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!",
            closeOnConfirm: false
        }, function () {
            $.ajax({
                type: "GET",
                url: baseUrl + "/process/delete",
                data: {
                    matrikelnummer: matrikelnummer,
                    uni: uni
                }
            }).done(function (data) {
                swal({
                	title: 'Gelöscht!',
                	text: 'Der Prozess wurde erfolgreich gelöscht.',
                	type: 'success'
                }, function() {
                	location.reload();
                });
            }).error(function (error) {
                console.error(error);
                swal('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
            })
        });
}
function initDeleteProcessButtonsTaskOverview() {
    $('.btn-delete').on('click', function () {
        var uni = $(this).attr("uni");
        var id = $(this).attr("rid");
        swal({
            title: "Bist du sicher?",
            text: "Der Prozess kann nicht wiederhergestellt werden!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!",
            closeOnConfirm: false
        }, function () {
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
                swal('Gelöscht!', 'Der Prozess wurde erfolgreich gelöscht.', 'success');
            }).error(function (error) {
                console.error(error);
                swal('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
            })
        });
    });
}

function deleteTask (taskID) {
    //$('.taskdelete').click(function () {
    	alert(taskID);

    	var self = $(this);
	
        swal({
            title: "Bist du sicher?",
            text: "Die Bewerbung kann nicht wiederhergestellt werden!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!"
        }).then((result) => {
        	alert("Du hast auf Löschen gedrückt");
        	if (result.value) {
                 alert("Auf Löschen gedrückt");
        		 swal({
                     title: 'Lösche Bewerbung'
                 });
                 swal.showLoading();
                 $.ajax({
                     type: "POST",
                     url: baseUrl + "/task/delete",
                     data: {
                         taskId: self.data(taskID)
                     },
                     success: function (result) {
                         swal.close();
                         $('#userStudShow').click();
                         swal('Gelöscht!', 'Die Bewerbung wurde erfolgreich gelöscht.', 'success');
                     },
                     error: function (result) {
                         swal.close();
                         swal('Fehler', 'Die Bewerbung konnte nicht gelöscht werden', 'error');
                     }
                 });
        	 }
        }); 

   }