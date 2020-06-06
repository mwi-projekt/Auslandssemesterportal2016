$(document).ready(function () {

    const inputVorname = $("#vorname");
    const inputNachname = $("#nachname");
    const inputEmail = $("#email");
    const inputStudiengang = $("#studiengang");
    const inputKurs = $("#kurs")
    const inputStandort = $("#standort");

    // Erstelle die DataTable
    myTable = $('#example').DataTable({
        dom: '<"top"f> rt <"bottom"lp>',
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
        ajax: '/getUser?rolle=4',
        columns: [
            {data: 'vorname'},
            {data: 'nachname'},
            {data: 'email'},
            {data: 'standort'},
            {data: 'studiengang'},
            {data: 'kurs'},
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

    let currentEmail;

    // Fülle die Modal-Felder
    $("#example").on("mousedown", "#edit", function (e) {
        const myData = myTable.row($(this).parents('tr')).data();
        inputVorname.val(myData.vorname);
        inputNachname.val(myData.nachname);
        currentEmail = myData.email;
        inputEmail.val(myData.email);
        inputStudiengang.val(myData.studiengang);
        inputKurs.val(myData.kurs);
        inputStandort.val(myData.standort);
    });

    // Submit-Button
    $('#myFormSubmit').click(function (e) {
        e.preventDefault();

        console.log($('#myForm').serialize());

        var vorname = inputVorname.val();
        var nachname = inputNachname.val();
        var email = inputEmail.val();
        var studiengang = inputStudiengang.val();
        var kurs = inputKurs.val();
        var standort = inputStandort.val();
        var oldMail = "0";

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
                role: "4"
            },
            success: function (result) {
                Swal.close();
                Swal.fire('Erfolgreich geändert.', 'Die Benutzerdaten wurden aktualisiert.', 'success');
                $('#exampleModal').modal('hide');
                myTable.ajax.reload();
            },
            error: function (result) {
                Swal.close();
                Swal.fire('Fehler', 'Es ist ein Fehler beim Aktualisieren aufgetreten. Überprüfen Sie die Eingaben.', 'error');
            }
        });
    });

    // Delete-Button
            $("#example").on("mousedown", "#deleteButton", function (e) {
                const myData = myTable.row($(this).parents('tr')).data();
                const email = myData.email;
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
                            type: "POST",
                            url: baseUrl + "/user/deleteSGL",
                            data: {
                                mail: email
                            },
                            success: function (result) {
                                Swal.close();
                                $('#userSGLShow').click();
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