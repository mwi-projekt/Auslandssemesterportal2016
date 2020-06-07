$(document).ready(function () {

    const inputVorname = $("#vorname");
    const inputNachname = $("#nachname");
    const inputEmail = $("#email");
    const inputStudiengang = $("#studiengang");
    const inputKurs = $("#kurs")
    const inputMatrikelnr = $("#matrikelnummer");
    const inputStandort = $("#standort");

    let currentEmail;
    let isEdit = false;

    // Erstelle die DataTable
    myTable = $('#example').DataTable({
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
            }
        },
        buttons: [
            {
                text: '<i class="fas fa-user-plus" id="create"></i> Neuer Student',
                action: function (e, node, config) {
                    $('#exampleModal').modal('show')
                    isEdit = false;
                    clearModal();
                }
            }],
        ajax: '/getUser?rolle=3',
        columns: [
            {data: 'vorname'},
            {data: 'nachname'},
            {data: 'email'},
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
                defaultContent: '<i style="cursor: pointer" id="deleteButton" class="fas fa-trash"></i>'
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
        currentEmail = myData.email;
        inputEmail.val(myData.email);
        inputStudiengang.val(myData.studiengang);
        inputKurs.val(myData.kurs);
        inputMatrikelnr.val(myData.matrikelnummer);
        inputStandort.val(myData.standort);
        isEdit = true;
    });

    // Submit-Button in Modal Dialog
    $('#myFormSubmit').click(function (e) {
        e.preventDefault();
        var vorname = inputVorname.val();
        var nachname = inputNachname.val();
        var email = inputEmail.val();
        var studiengang = inputStudiengang.val();
        var kurs = inputKurs.val();
        var matrikelnummer = inputMatrikelnr.val();
        var standort = inputStandort.val();
        var oldMail = "0";

        if (isEdit) {
            if (currentEmail != $('#email').val()) {
                oldMail = currentEmail;
            }

            Swal.fire({
                title: 'Speichere Änderungen'
            });
            Swal.showLoading();
            $.ajax({
                type: "POST",
                url: baseUrl + "/user/update",
                data: {
                    email: email,
                    oldmail: oldMail,
                    vorname: vorname,
                    nachname: nachname,
                    studgang: studiengang,
                    kurs: kurs,
                    matnr: matrikelnummer,
                    role: "3"
                },
                success: function (result) {
                    Swal.close();
                    Swal.fire('Erfolgreich geändert.', 'Die Benutzerdaten wurden aktualisiert.', 'success');
                    $('#exampleModal').modal('hide');
                    $('body').removeClass('modal-open');
                    $('.modal-backdrop').remove();
                    myTable.ajax.reload();
                    isEdit = false;
                },
                error: function (result) {
                    Swal.close();
                    Swal.fire('Fehler', 'Es ist ein Fehler beim Aktualisieren aufgetreten. Überprüfen Sie die Eingaben.', 'error');
                    isEdit = false;
                }
            });
        } else {
            Swal.fire({
                title: 'Speichere Änderungen'
            });
            Swal.showLoading();
            $.ajax({
                type: "POST",
                url: baseUrl + "/createStudent",
                data: {
                    email: email,
                    vorname: vorname,
                    nachname: nachname,
                    studgang: studiengang,
                    kurs: kurs,
                    matnr: matrikelnummer,
                    standort: standort
                },
                success: function (data) {
                    Swal.close();
                    if (data == "mailError") {

                        Swal.fire({
                            title: "Fehler!",
                            text: "Ein Account mit dieser Mail existiert bereits! Bitte benutzen Sie eine andere.",
                            icon: "error",
                            confirmButtonText: "OK"
                        });

                    } else if (data == "No account registered for this email adress") {
                        Swal.fire({
                            title: "Fehler!",
                            text: "Es ist ein Fehler beim Erstellen des Accounts aufgetreten. Versuchen Sie es später erneut.",
                            icon: "error",
                            confirmButtonText: "OK"
                        });
                    } else if (data == "registerError") {
                        Swal.fire({
                            title: "Fehler!",
                            text: "Es ist ein Fehler beim Erstellen des Accounts aufgetreten. Versuchen Sie es später erneut.",
                            icon: "error",
                            confirmButtonText: "OK"
                        });
                    } else {
                        Swal.fire({
                            title: "Account erfolgreich erstellt",
                            text: "An die Mailadresse wurde ein Link zum setzen des Passwords geschickt.",
                            icon: "success",
                            confirmButtonText: "OK"
                        });
                    }
                    $('#exampleModal').modal('hide');
                    $('body').removeClass('modal-open');
                    $('.modal-backdrop').remove();
                    myTable.ajax.reload();
                    isEdit = false;
                }
            });
        }
    });

    // Delete-Button
    $("#example").on("mousedown", "#deleteButton", function (e) {
        const myData = myTable.row($(this).parents('tr')).data();
        const martikelnummer = myData.matrikelnummer;
        Swal.fire({
            title: "Bist du sicher?",
            text: "Der User kann nicht wiederhergestellt werden!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!"
        }).then(function (result) {
            if (result.value) {
                alert("Du hast auf Löschen gedrückt");
                Swal.fire({
                    title: 'Lösche User'
                });
                Swal.showLoading();
                $.ajax({
                    type: "GET",
                    url: baseUrl + "/user/delete",
                    data: {
                        matrikelnummer: martikelnummer
                    },
                    success: function (result) {
                        Swal.close();
                        $('#userStudShow').click();
                        Swal.fire('Gelöscht!', 'Der User wurde erfolgreich gelöscht.', 'success');
                        myTable.ajax.reload();
                    },
                    error: function (result) {
                        Swal.close();
                        Swal.fire('Fehler', 'Der User konnte nicht gelöscht werden', 'error');
                    }
                });
            }
        });
    });
});

function clearModal() {
    $('#exampleModal').on('hidden.bs.modal', function (e) {
        $(this)
            .find("input,textarea,select")
            .val('')
            .end()
            .find("input[type=checkbox], input[type=radio]")
            .prop("checked", "")
            .end();
    })
}