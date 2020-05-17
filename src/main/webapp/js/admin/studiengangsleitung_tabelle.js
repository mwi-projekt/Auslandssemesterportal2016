var rolle = 0;
var typ = '';

$.ajax({
    type: "GET",
    url: baseUrl + "/getUser",
    data: {
        rolle: rolle,
    },
    success: function (result) {
        var auslesen = result.data;
        if (rolle === 4) {
                    var tabelle =
                    '<h2>Registrierte ' + typ + '</h2>' +
                    '<table id="userTable" class="table table-striped table-bordered">' +
                    '<thead>' +
                    '<tr class="titleRow">' +
                    '<th>Vorname</th>' +
                    '<th>Nachname</th>' +
                    '<th>Email</th>' +
                    '<th>DHBW Standort</th>' +
                    '<th>Studiengang</th>' +
                    '<th>Kurs</th>' +
                    '<th></th>' +
                    '<th></th>' +
                    '</tr></thead>';

                    for (var i = 0; i < auslesen.length; i++) {
                        var row = auslesen[i];
                        tabelle = tabelle +
                            '<tr id="row' + i +
                            '"><td class="vorname">' +
                            row.vorname +
                            '</td><td class="nachname">' +
                            row.nachname +
                            '</td><td class="email">' +
                            row.email +
                            '</td><td>' +
                            row.standort +
                            '<td class="studgang">' +
                            row.studiengang +
                            '</td><td class="kurs">' +
                            row.kurs +
                            '</td><td><span class="btn fas fa-edit useredit-button" id="edit' +
                            i +
                            '" title="Bearbeiten" data-toggle="modal" href="#userEdit"> </span></td><td><span class="btn fas fa-trash deleteSGL-button" data-mail="' + row.email + '" id="delete' +
                            i +
                            '" title="Löschen"></span></td></tr>';
                    }
                }
           }
});