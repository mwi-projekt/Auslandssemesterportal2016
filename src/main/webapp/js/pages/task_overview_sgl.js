import {$,baseUrl} from "../config";
import "../app";
import 'datatables.net';
import 'datatables.net-bs4';
import 'datatables.net-buttons';
import Swal from "sweetalert2";
import "jquery-ui-dist/jquery-ui";
import "bootstrap";

$(document).ready(function () {
    getList();
    $(document).on('click', '.btn-delete', function() {
        var uni = $(this).data('uni');
        var matrikelnummer = $(this).data('matrikelnummer');
        deleteProcessButtons(uni, matrikelnummer);
    });
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
            var edit = ""; 		    //zur Bearbeitung zurückgesendet
            var completed = "";		//angenommene Bewerbungen
            var abgelehnt = "";		//abgelehnte Bewerbungen
            if (!result || result.data.length == 0) {
                // substring bilden nicht möglich bei leerem String
            } else {
                var instances = result.data;

                for (var i = 0; i < instances.length; i++) {
                    var singleInstance = instances[i];
                    if (singleInstance.status === 'validateSGL') {
                        output = output +
                            "<tr><td>" +
                            singleInstance.name +
                            "</td><td>" +
                            singleInstance.vname +
                            "</td><td>" +
                            singleInstance.kurs +
                            "</td><td>" +
                            singleInstance.prioritaet +
                            "</td><td>" +
                            singleInstance.aktuelleUni +
                            "</td><td>" +
                            singleInstance.uni +
                            "</td><td>" +
                            '<button class="btn fas fa-list" title="Details" onclick="location.href=\'task_detail_sgl.html?instance_id=' + singleInstance.id + '&uni=' + singleInstance.uni + '&verify=true\'\"> </button>' +
                            "</td><td>" +
                            "<button class=\"btn fas fa-trash btn-delete\" title=\"Delete\" data-uni=\"" + singleInstance.uni + "\" data-matrikelnummer=\"" + singleInstance.matrikelnummer + "\"></button></td></tr>";

                    } else if (singleInstance.status === 'edit') {
                        edit = edit +
                            "<tr>" +
                            "<td>" + singleInstance.name + "</td>" +
                            "<td>" + singleInstance.vname + "</td>" +
                            "<td>" + singleInstance.kurs + "</td>" +
                            "<td>" + singleInstance.prioritaet + "</td>" +
                            "<td>" + singleInstance.aktuelleUni + "</td>" +
                            "<td>" + singleInstance.uni + "</td>" +
                            "<td>" +
                            '<button class="btn fas fa-list" title="Details" onclick="location.href=\'task_detail_sgl.html?instance_id=' + singleInstance.id + '&uni=' + singleInstance.uni + '&read=true\'\"> </button>' +
                            "</td>" +
                            "<td>" + '<button class="btn fas fa-redo" disabled></button' + "</td>" +
                            "<td>" +
                            "<button class=\"btn fas fa-trash btn-delete\" title=\"Delete\" data-uni=\"" + singleInstance.uni + "\" data-matrikelnummer=\"" + singleInstance.matrikelnummer + "\"></button></td></tr>";
                    } else if (singleInstance.status === 'complete' || singleInstance.status === 'abgeschlossen'|| singleInstance.status === 'goOutUebergeben') {
                        completed = completed +
                            "<tr><td>" +
                            singleInstance.name +
                            "</td><td>" +
                            singleInstance.vname +
                            "</td><td>" +
                            singleInstance.aktuelleUni +
                            "</td><td>" +
                            singleInstance.prioritaet +
                            "</td><td>" +
                            singleInstance.kurs +
                            "</td><td>" +
                            singleInstance.uni +
                            "</td><td>" +
                            '<button class="btn fas fa-list" title="Details" onclick="location.href=\'task_detail_sgl.html?instance_id=' + singleInstance.id + '&uni=' + singleInstance.uni + '&read=true\'\"> </button>' +
                            "</td><td>" +
                            "<button class=\"btn fas fa-trash btn-delete\" title=\"Delete\" data-uni=\"" + singleInstance.uni + "\" data-matrikelnummer=\"" + singleInstance.matrikelnummer + "\"></button></td></tr>";
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
                            "</td><td>" +
                            '<button class="btn fas fa-list" title="Details" onclick="location.href=\'task_detail_sgl.html?instance_id=' + singleInstance.id + '&uni=' + singleInstance.uni + '&read=true\'\"> </button>' +
                            "</td></tr>";
                    }
                }
            }

            if (output === "") {
                output = "<p>Aktuell gibt es keine Bewerbungen, die überprüft werden müssen</p>";
            } else {
                output = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Kurs</th><th>Priorität</th><th>Heimatuniversität</th><th>Partneruniversität</th><th>Prüfen</th><th>Löschen</th></tr></thead><tbody>' +
                    output + "</tbody></table>";
            }
            if (edit === "") {
                edit = "<p>Aktuell gibt es keine Bewerbungen, die zur erneuten Überprüfung durch den Studenten zurückgesendet wurden</p>";
            } else {
                edit = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Kurs</th><th>Priorität</th><th>Heimatuniversität</th><th>Partneruniversität</th><th>Details</th><th>Erinnerung senden</th><th>Löschen</th></tr></thead><tbody>' +
                    edit + "</tbody></table>";
            }
            if (completed === "") {
                completed = "<p>Es gibt noch keine abgeschlossenen Bewerbungen</p>";
            } else {
                completed = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Heimatuniversität</th><th>Priorität</th><th>Kurs</th><th>Partneruniversität</th><th>Details</th><th>Löschen</th></tr></thead><tbody>' +
                    completed + "</tbody></table>";
                completed += '<a style="width: 200px;text-align: center;margin: 20px 0;" href="'+ baseUrl +'/sglExport" target="_blank" class="page-link">Exportieren als zip</a>';
            }

            if (abgelehnt === "") {
                abgelehnt = "<p>Bisher wurden keine Bewerbungen abgelehnt</p>";
            } else {
                abgelehnt = '<table id="task" class="table table-striped table-bordered"><thead><tr><th>Name</th><th>Vorname</th><th>Heimatuniversität</th><th>Kurs</th><th>Partneruniversität</th><th>Details</th></tr></thead><tbody>' +
                    abgelehnt + "</tbody></table>";
            }

            $(document).ready(function () {
                $('.table').DataTable({
                    dom: '<"top"fB> rt <"bottom"lp>',
                    language: {
                        processing: "Bitte warten ...",
                        search: "Suchen ",
                        lengthMenu: "_MENU_ Einträge anzeigen",
                        info: "_START_ bis _END_ von _TOTAL_ Einträgen",
                        infoEmpty: "Keine Daten vorhanden",
                        infoFiltered: "(gefiltert von _MAX_ Einträgen)",
                        infoPostFix: "",
                        loadingRecords: "Wird geladen ...",
                        zeroRecords: "Es sind keine Einträge vorhanden.",
                        emptyTable: "Die Tabelle ist leer",
                        paginate: {
                            first: "Erste",
                            previous: "Zurück",
                            next: "Nächste",
                            last: "Letzte"
                        },
                        aria: {
                            sortAscending: ": aktivieren, um Spalte aufsteigend zu sortieren",
                            sortDescending: ": aktivieren, um Spalte absteigend zu sortieren"
                        },
                    },
                    buttons: []
                });
            });

            document.getElementById("resultList").innerHTML = '<br><h4>Zu validierende Bewerbungen</h4>' + output + '<br><h4>Zur Bearbeitung geschickte Bewerbungen</h4>' + edit + '<br><h4>Angenommene Bewerbungen</h4>' + completed + '<br><h4>Abgelehnte Bewerbungen</h4>' + abgelehnt;
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
    }).then(function (result) {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: baseUrl + "/process/delete",
                data: {
                    matrikelnummer: matrikelnummer,
                    uni: uni
                },
                success: function (data) {
                    Swal.fire({
                        title: 'Gelöscht!',
                        text: 'Der Prozess wurde erfolgreich gelöscht.',
                        icon: 'success'
                    }).then(function () {
                        location.reload();
                    });
                },
                error: function (error) {
                    console.error(error);
                    Swal.fire('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
                }
            });
        }
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
        }).then(function (result) {
            if (result.value) {
                //var id = $('.btn-delete').closest('tr').data('rid');
                var matrikelnummer = sessionStorage['matrikelnr'];

                $.ajax({
                    type: "GET",
                    url: baseUrl + "/process/delete",
                    data: {
                        matrikelnummer: matrikelnummer,
                        uni: uni
                    },
                    success: function (data) {
                        $('#tableBewProzess tr[data-rid=' + id + ']').remove();
                        Swal.fire('Gelöscht!', 'Der Prozess wurde erfolgreich gelöscht.', 'success');
                    },
                    error: function (error) {
                        console.error(error);
                        Swal.fire('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
                    }
                });
            }
        });
    });
}

function deleteTask(taskID) {
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
    }).then(function (result) {
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
