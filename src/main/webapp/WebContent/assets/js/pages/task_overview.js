$(document).ready(function () {
    getList();
});

function getList() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/getTasks",
        data: {
            //'definition' : 'studentBewerben'
        },
        success: function (result) {
            output = "";
            completed = "";
            if (!result || result.data.length == 0) {
                // substring bilden nicht möglich bei leerem String
            } else {
                var instances = result.data;

                for (var i = 0; i < instances.length; i++) {
                    singleInstance = instances[i];
                    if (singleInstance.status === 'validate') {
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
                            '<button class="btn glyphicon glyphicon-list" title="Details" onclick="location.href=\'task_detail.html?instance_id=' +
                            singleInstance.id +
                            '&uni=' +
                            singleInstance.uni +
                            '&verify=true\'"> </button>' +
                            "</td><td>" +
                            '<button class="btn glyphicon glyphicon-trash btn-delete" title="Delete" onclick="location.href=\'task_detail.html?instance_id=' +
                            singleInstance.id +
                            '&uni=' +
                            singleInstance.uni +
                            '&verify=true\'"> </button>' +
                            "</td></tr>"
                    } else if (singleInstance[6] === 'complete') {
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
                            "</td></tr>"
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

                $(document).ready(function () {
                    $('.table').DataTable();
                });

            }
            document.getElementById("resultList").innerHTML = '<h1>Zu validierende Bewerbungen</h1>' + output + '<br><h1>Abgeschlossene Bewerbungen</h1>' + completed; +
            output;
        },
        error: function (result) {
            swal("Ein Fehler ist aufgetreten", "error");
        }
    });
}