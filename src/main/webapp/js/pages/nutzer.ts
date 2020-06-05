import {$,baseUrl} from "../config";
import "../app";
// @ts-ignore
let dt = require( 'datatables.net' )(window, $);
import "datatables.net-bs4";
import Swal from "sweetalert2";
import "bootstrap";
import "jquery-ui-dist/jquery-ui";


$(document).ready(function () {
    // Click-Listener für Userverwaltung User anzeigen lassen
    $('.btnUser').on('click', function () {
        let id = $(this).attr('id');

        let rolle = 0;
        let typ = '';
        if (id === 'userStudShow') {
            rolle = 3;
            typ = "Studierende";
        } else if (id === 'userMaShow') {
            rolle = 2;
            typ = "Auslandsmitarbeiter";
        } else if (id == 'userSGLShow'){
        	rolle = 4;
        	typ = "Studiengangsleiter";
        }
        $.ajax({
            type: "GET", 
            url: baseUrl + "/getUser",
            data: {
                rolle: rolle,
            },
            success: function (result) {
                let auslesen = result.data;
                let tabelle : string = "";
                if (rolle === 2) {
                    tabelle = '<h2>Registrierte ' + typ +
                        '</h2><table id="userTable" class="table table-striped table-bordered"> <thead><tr class="titleRow"><th>Vorname</th><th>Nachname</th><th>Email</th><th>Telefonnummer</th><th>Mobilfunknummer</th><th></th><th></th></tr></thead>';
                    for (let i = 0; i < auslesen.length; i++) {
                        let row = auslesen[i];

                        tabelle = tabelle +
                            '<tr id="row' + i +
                            '"><td class="vorname">' +
                            row.vorname +
                            '</td><td class="nachname">' +
                            row.nachname +
                            '</td><td class="email">' +
                            row.email +
                            '</td><td class="telnummer">' +
                            row.tel +
                            '</td><td class="mobil">' +
                            row.mobil +
                            '</td><td><span class="btn fas fa-edit useredit-button" id="edit' + i +
                            '" title="Bearbeiten" data-toggle="modal" href="#userEdit"> </span></td>' +
                            '<td><span class="btn fas fa-trash deleteAAA-button" data-mail="' +
                            row.email +
                            '" id="delete' + i +
                            '" title="Löschen"></span></td></tr>';
                    }
                } else if (rolle === 3) {
                    tabelle = '<h2>Registrierte ' +
                        typ +
                        '</h2><table id="userTable" class="table table-striped table-bordered"><thead><tr class="titleRow"><th>Vorname</th><th>Nachname</th><th>Email</th><th>DHBW Standort</th><th>Studiengang</th><th>Kurs</th><th>Matrikelnummer</th><th></th><th></th></tr></thead>';

                    for (let i = 0; i < auslesen.length; i++) {
                        let row = auslesen[i];
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
                            '</td><td class="matrikelnr">' +
                            row.matrikelnummer +
                            '</td><td><span class="btn fas fa-edit useredit-button" id="edit' +
                            i +
                            '" title="Bearbeiten" data-toggle="modal" href="#userEdit"> </span></td><td><span class="btn fas fa-trash delete-button" data-matrikel="' + row.matrikelnummer + '" id="delete' +
                            i +
                            '" title="Löschen"></span></td></tr>';
                    }
                }
                else if (rolle === 4) {
                    tabelle = '<h2>Registrierte ' +
                        typ +
                        '</h2><table id="userTable" class="table table-striped table-bordered"><thead><tr class="titleRow"><th>Vorname</th><th>Nachname</th><th>Email</th><th>DHBW Standort</th><th>Studiengang</th><th>Kurs</th><th></th><th></th></tr></thead>';

                    for (let i = 0; i < auslesen.length; i++) {
                        let row = auslesen[i];
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
                tabelle = tabelle + '</table>';
                $('#userTabelle').html(tabelle)
                let nonSortable = (rolle == 3 ? [7, 8] : [5, 6]);

                let setClickListeners = function () {
                    $('.delete-button').click(function () {
                        let self = $(this);

                        Swal.fire({
                            title: "Bist du sicher?",
                            text: "Der User kann nicht wiederhergestellt werden!",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "Löschen!",
                            cancelButtonText: "Abbrechen"
                        }).then(function(result) {
                            if (result.value) {
                            	Swal.fire({
                                    title: 'Lösche User'
                                });
                                Swal.showLoading();
                                $.ajax({
                                    type: "GET",
                                    url: baseUrl + "/user/delete",
                                    data: {
                                        matrikelnummer: self.data('matrikel')
                                    },
                                    success: function (result) {
                                        Swal.close();
                                        $('#userStudShow').click();
                                        Swal.fire('Gelöscht!', 'Der User wurde erfolgreich gelöscht.', 'success');
                                    },
                                    error: function (result) {
                                        Swal.close();
                                        Swal.fire('Fehler', 'Der User konnte nicht gelöscht werden', 'error');
                                    }
                                });
                            } else{
                                Swal.fire({
                                    title: "Abgebrochen",
                                    icon: "info",
                                    confirmButtonText: "Ok"
                                });
                            }
                        });

                    });
                    
                    $('.deleteSGL-button').click(function () {

                        let self = $(this);

                        Swal.fire({
                            title: "Bist du sicher?",
                            text: "Der User kann nicht wiederhergestellt werden!",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "Löschen!"
                        }).then(function(result) {
                            if (result.value) {
                                Swal.fire({
                                    title: 'Lösche User'
                                });
                                Swal.showLoading();
                                $.ajax({
                                    type: "POST",
                                    url: baseUrl + "/user/deleteSGL",
                                    data: {
                                    	mail: self.data('mail')
                                    },
                                    success: function (result) {
                                        Swal.close();
                                        $('#userSGLShow').click();
                                        Swal.fire('Gelöscht!', 'Der User wurde erfolgreich gelöscht.', 'success');
                                    },
                                    error: function (result) {
                                        Swal.close();
                                        Swal.fire('Fehler', 'Der User konnte nicht gelöscht werden', 'error');
                                    }
                                });
                            }
                        });

                    });                    

                    $('.deleteAAA-button').click(function () {

                        let self = $(this);

                        Swal.fire({
                            title: "Bist du sicher?",
                            text: "Der User kann nicht wiederhergestellt werden!",
                            icon: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "Löschen!"
                        }).then(function(result) {
                            if (result.value) {
                                Swal.fire({
                                    title: 'Lösche User'
                                });
                                Swal.showLoading();
                                $.ajax({
                                    type: "POST",
                                    url: baseUrl + "/user/deleteAAA",
                                    data: {
                                        mail: self.data('mail')
                                    },
                                    success: function (result) {
                                        Swal.close();
                                        $('#userMaShow').click();
                                        Swal.fire('Gelöscht!', 'Der User wurde erfolgreich gelöscht.', 'success');
                                    },
                                    error: function (result) {
                                        Swal.close();
                                        Swal.fire('Fehler', 'Der User konnte nicht gelöscht werden', 'error');
                                    }
                                });
                            }
                        });

                    });

                    $('.useredit-button').click(function () {
                        let id = $(this).attr('id')!;
                        let laenge = id.length;
                        if (laenge === 5) {
                            id = id.substring(4, 5);
                        } else {
                            id = id.substring(4, 6);
                        }
                        $('#inEditVorname').val($('#row' + id).children('.vorname').text().trim());
                        //$('#inEditVorname').attr('data-value', $('#inEditVorname').val()?);
                        $('#inEditNachname').val($('#row' + id).children('.nachname').text().trim());
                        //$('#inEditNachname').attr('data-value', $('#inEditNachname').val());
                        $('#inEditEmail').val($('#row' + id).children('.email').text().trim());
                        //$('#inEditEmail').attr('data-value', $('#inEditEmail').val());
                        $('#inEditEmail').attr('data-role', rolle);
                        $('#inEditTel').val($('#row' + id).children('.telnummer').text().trim());
                        //$('#inEditTel').attr('data-value', $('#inEditTel').val());
                        $('#inEditMobil').val($('#row' + id).children('.mobil').text().trim());
                        //$('#inEditMobil').attr('data-value', $('#inEditMobil').val());
                        $('#inEditStudgang').val($('#row' + id).children('.studgang').text().trim());
                        //$('#inEditStudgang').attr('data-value', $('#inEditStudgang').val());
                        $('#inEditKurs').val($('#row' + id).children('.kurs').text().trim());
                        //$('#inEditKurs').attr('data-value', $('#inEditKurs').val());
                        $('#inEditMatnr').val($('#row' + id).children('.matrikelnr').text().trim());
                        //$('#inEditMatnr').attr('data-value', $('#inEditMatnr').val());
                        if (rolle === 2) {
                            $('#inEditTel').show();
                            $('#inEditMobil').show();
                            $('#inEditStudgang').hide();
                            $('#inEditKurs').hide();
                            $('#inEditMatnr').hide();
                        } else {
                            $('#inEditTel').hide();
                            $('#inEditMobil').hide();
                            $('#inEditStudgang').show();
                            $('#inEditKurs').show();
                            $('#inEditMatnr').show();
                        }
                        $('.nutzerBearbeiten').show();
                    });
                };

                $('#userTable').DataTable({
                    "columnDefs": [{
                        "orderable": false,
                        "targets": nonSortable
                    }],
                    "drawCallback": function (settings) {
                        setClickListeners();
                    }
                });
            },
            error: function (result) {
                Swal.fire({
                    title: "Serverfehler",
                    text: "Die Serververbindung wurde unterbrochen. Bitte laden Sie die Seite erneut",
                    icon: "error",
                    confirmButtonText: "OK"
                });
            }
        });
    });

    $('#AAACreateForm').submit(function (event) {
        let email = $('#AAAMail').val();
        let vname = $('#AAAVorname').val();
        let nname = $('#AAANachname').val();
        let phone = $('#AAAPhone').val();
        let mobil = $('#AAAMobile').val();
        Swal.fire({
            title: 'Speichere Änderungen'
        });
        Swal.showLoading();
        $.ajax({
            type: "POST",
            url: baseUrl + "/createAAA",
            data: {
                email: email,
                vorname: vname,
                nachname: nname,
                phone: phone,
                mobil: mobil
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
                $('#AAACreate').modal('hide');
                $('#userMaShow').click();
            }

        });

        event.preventDefault();
    });
    
    $('#SGLCreateForm').submit(function (event) {
        let email = $('#SGLMail').val();
        let vname = $('#SGLVorname').val();
        let nname = $('#SGLNachname').val();
        let studiengang = $('#SGLStudiengang').val();
        let phone = $('#SGLPhone').val();
        let mobil = $('#SGLMobile').val();
        Swal.fire({
            title: 'Speichere Änderungen'
        });
        Swal.showLoading();
        $.ajax({
            type: "POST",
            url: baseUrl + "/createSGL",
            data: {
                email: email,
                vorname: vname,
                nachname: nname,
                studiengang: studiengang,
                phone: phone,
                mobil: mobil
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
                $('#SGLCreate').modal('hide');
                $('#userSGLShow').click();
            }

        });

        event.preventDefault();
    });
    

    
    $('#btnUserEditSave').click(function () {
		let dataMail = $('#inEditEmail').val();
		let dataOldMail = "0";
		let dataRole = $('#inEditEmail').attr('data-role');
		let dataNewVorname = $('#inEditVorname').val();
		let dataNewNachaname = $('#inEditNachname').val();
		let dataNewTel = $('#inEditTel').val();
		let dataNewMobil = $('#inEditMobil').val();
		let dataNewStudgang = $('#inEditStudgang').val();
		let dataNewKurs = $('#inEditKurs').val();
		let dataNewMatnr = $('#inEditMatnr').val();
		if ($('#inEditEmail').attr('data-value') != $('#inEditEmail').val()) {
			dataOldMail = $('#inEditEmail').attr('data-value')!;
		}
		Swal.fire({
			title: 'Speichere Änderungen'
		});
		Swal.showLoading();
		if (dataRole == "2") {
			$.ajax({
				type: "POST",
				url: baseUrl + "/user/update",
				data: {
					email: dataMail,
					oldmail: dataOldMail,
					vorname: dataNewVorname,
					nachname: dataNewNachaname,
					tel: dataNewTel,
					mobil: dataNewMobil,
					role: "2"
				},
				success: function (result) {
					Swal.close();
					Swal.fire('Erfolgreich geändert.', 'Die Mitarbeiterdaten wurden aktualisiert.', 'success');
					$('#userEdit .close').click();

					$('#userMaShow').click();


				},
				error: function (result) {
					Swal.close();
					Swal.fire('Fehler', 'Es ist ein Fehler beim Aktualisieren aufgetreten. Überprüfen Sie die Eingaben.', 'error');
				}
			});
		} else {
			$.ajax({
				type: "POST",
				url: baseUrl + "/user/update",
				data: {
					email: dataMail,
					oldmail: dataOldMail,
					vorname: dataNewVorname,
					nachname: dataNewNachaname,
					studgang: dataNewStudgang,
					kurs: dataNewKurs,
					matnr: dataNewMatnr,
					role: "3"
				},
				success: function (result) {
					Swal.close();
					Swal.fire('Erfolgreich geändert.', 'Die Benutzerdaten wurden aktualisiert.', 'success');
					$('#userEdit .close').click();
					$('#userStudShow').click();


				},
				error: function (result) {
					Swal.close();
					Swal.fire('Fehler', 'Es ist ein Fehler beim Aktualisieren aufgetreten. Überprüfen Sie die Eingaben.', 'error');
				}
			});
		}

	});
});