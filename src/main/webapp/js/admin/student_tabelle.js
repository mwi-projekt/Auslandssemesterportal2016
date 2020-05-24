$(document).ready(function () {

    const inputVorname = $("#vorname");
    const inputNachname = $("#nachname");
    const inputEmail = $("#email");
    const inputTel = $("#telefonnummer");
    const inputMobil = $("#mobilnummer");
    const inputStudiengang = $("#studiengang");
    const inputKurs = $("#kurs")
    const inputMartikelnr = $("#martikelnummer");
    const inputStandort = $("#standort");

    // Erstelle die DataTable
    myTable = $('#example').DataTable({
        dom: '<"top"f> rt <"bottom"lp>',
        language: {
            processing:     "Bitte warten ...",
            search:         "Suchen ",
            lengthMenu:     "_MENU_ Einträge anzeigen",
            info:           "_START_ bis _END_ von _TOTAL_ Einträgen",
            infoEmpty:      "Keine Daten vorhanden",
            infoFiltered:   "(gefiltert von _MAX_ Einträgen)",
            infoPostFix:    "",
            loadingRecords: "Wird geladen ...",
            zeroRecords:    "Es sind keine Einträge vorhanden.",
            emptyTable:     "Die Tabelle ist leer",
            paginate: {
                first:      "Erste",
                previous:   "Zurück",
                next:       "Nächste",
                last:       "Letzte"
            },
            aria: {
                sortAscending:  ": aktivieren, um Spalte aufsteigend zu sortieren",
                sortDescending: ": aktivieren, um Spalte absteigend zu sortieren"
            }
        },
        ajax: '/getUser?rolle=3',
        columns: [
            {data: 'vorname'},
            {data: 'nachname'},
            {data: 'email'},
            {data: 'tel'},
            {data: 'mobil'},
            {data: 'studiengang'},
            {data: 'kurs'},
            {data: 'matrikelnummer'},
            {data: 'standort'},
            {
                data: null,
                className: "center",
                defaultContent: '<i style="cursor: pointer" id="edit" class="fas fa-edit" data-toggle="modal" data-target="#exampleModal"></i>'
            },
            {
                data: null,
                className: "center",
                defaultContent: '<i style="cursor: pointer" id="delete" class="fas fa-trash"></i>'
            }
        ],
        select: 'single',
        responsive: true
    });

    // Fülle die Modal-Felder
    $("#example").on("mousedown", "#edit", function (e) {
        const myData = myTable.row($(this).parents('tr')).data();
        inputVorname.val(myData.vorname);
        inputNachname.val(myData.nachname);
        inputEmail.val(myData.email);
        inputTel.val(myData.tel);
        inputMobil.val(myData.mobil);
        inputStudiengang.val(myData.studiengang);
        inputKurs.val(myData.kurs);
        inputMartikelnr.val(myData.matrikelnummer);
        inputStandort.val(myData.standort);
    });

    // Submit-Button
    $('#myFormSubmit').click(function (e) {
        e.preventDefault();

        console.log($('#myForm').serialize());

        var vorname = inputVorname.val();
        var nachname = inputNachname.val();
        var email = inputEmail.val();
        var telefonnummer = inputTel.val();
        var mobilnummer = inputMobil.val();
        var studiengang = inputStudiengang.val();
        var kurs = inputKurs.val();
        var martikelnummer = inputMartikelnr.val();
        var standort = inputStandort.val();
        var oldMail = "0";

        if ($('#email').attr('data-value') != $('#email').val()) {
            oldMail = $('#email').attr('data-value');
        }

        Swal.fire({
            title: 'Speichere Änderungen'
        });
        Swal.showLoading();
        $.ajax({
            type: "POST",
            url: baseUrl + "/user/update",
            // url: baseUrl + "/test",
            data: {
                email: email,
                oldmail: oldMail,
                vorname: vorname,
                nachname: nachname,
                tel: telefonnummer,
                mobil: mobilnummer,
                studgang: studiengang,
                kurs: kurs,
                matnr: martikelnummer,
                role: "3"
            },
            success: function (result) {
                Swal.close();
                Swal.fire('Erfolgreich geändert.', 'Die Benutzerdaten wurden aktualisiert.', 'success');
                $('#exampleModal').modal('hide');
            },
            error: function (result) {
                Swal.close();
                Swal.fire('Fehler', 'Es ist ein Fehler beim Aktualisieren aufgetreten. Überprüfen Sie die Eingaben.', 'error');
                console.log(JSON.stringify(result));
            }
        });
    });
});