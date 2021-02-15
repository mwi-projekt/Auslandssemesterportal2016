import {$,baseUrl} from "../config";
import Swal from "sweetalert2";
import "bootstrap";
import "jquery-form-validator";
import "dropzone";

$(document).ready(function () {
    loadAuslandsangebote();
    loadAuslandsangeboteInhalt();

    $('.navEl').on('click', function (event) {
        $('.navEl').removeClass('current');
        $(this).addClass('current');
        $('.inhalt').hide();
        var id = $(this).attr('id');
        id = id.substring(3, 4);
        $('#in' + id).show();

        // Bewerben
        if (id == 2) {
            initBewerben();
        }
    });

    if (window.location.hash && window.location.hash.startsWith('#nav')) {
        $(window.location.hash).trigger('click');
    } else {
        $('.navEl:first-child').trigger('click');
    }

    // Click-Listener für neuen Bewerbungsprozess starten Button
    $('#newBewProzess').on('click', function () {
        $.ajax({
            type: "GET",
            url: baseUrl + "/unis",
            data: {
                studiengang: sessionStorage['studiengang'],
            },
            success: function (result) {
                // schon beworbene Unis filtern
                var splitUni = [];
                $('#tableBewProzess tr[data-rid] td:first-child').each(function() {
                    splitUni.push($(this).text());
                });

                if (result.data.length == 0) {
                    popUpHtml = '<b id="popClose"><img src="images/Button Delete.png" id="smallImg"></b><br><p>Sie haben sich bereits für alle verfügbaren Auslandsuniversitäten für ihren Studiengang beworben.</p>';
                } else {
                    var popUpHtml = '<div class="form-horizontal"><div class="form-group"><div class="col-md-12"><select class="inBox" id="selectUni">';
                    for (var l = 0; l < result.data.length; l++) {
                        // filtern von schon beworbenen unis
                        if (splitUni.indexOf(result.data[l].uniTitel) != -1) continue;
                        popUpHtml = popUpHtml + '<option>' + result.data[l].uniTitel + '</option>';
                    }

                    popUpHtml = popUpHtml + '</select></div></div>';

                    popUpHtml += '<div class="form-group"><div class="col-md-12"><select class="inBox" id="selectZeit">';
                    //Zeitraum muss hier noch automatisch generiert werden
                    popUpHtml = popUpHtml + '<option> Zeitraum auswählen </option><option> Sommersemester 2021 </option>';

                    popUpHtml = popUpHtml + '</select></div></div>';

                    popUpHtml += '<div class="form-group"><div class="col-md-12"><select class="inBox" id="selectPrio">';

                    popUpHtml += '<option value="0"> Priorität auswählen </option>';

                    for (var i = 1; i <= 5; i++) {
                        if (prioArr.indexOf(""+i) < 0) {
                            popUpHtml += '<option>' + i + '</option>';
                        }
                    }

                    popUpHtml = popUpHtml + '</select></div></div>';

                    popUpHtml += '<div class="form-group"><div class="col-md-12"><button id="newBewProzessWahl" class="btn btn-success">Bestätigen</button></div></div></div>';
                }


                Swal.fire({
                    title: 'Bitte wähle die Uni und den Zeitraum aus',
                    html: popUpHtml,
                    confirmButtonText: 'Abbrechen',
                    onOpen: function () {
                        $('#newBewProzessWahl').on('click', function () {
                            sessionStorage['uni'] = $('#selectUni').val();
                            sessionStorage['zeitraum'] = $('#selectZeit').val();
                            $.ajax({
                                type: "GET",
                                url: baseUrl + "/getInstance",
                                data: {
                                    matnr: sessionStorage['matrikelnr'],
                                    uni: $('#selectUni').val(),
                                    zeitraum: $('#selectZeit').val(),
                                    prio: $('#selectPrio').val(),
                                },
                                success: function (result) {
                                    location.href = 'bewerben.html?instance_id=' + result.instanceId + '&uni=' + result.uni + '&zeitraum=' + result.zeitraum;
                                }
                            });
                        });
                    }

                });
            }
        });
    });
});

// Lädt die Daten zu den Auslandsangeboten aus der Datenbank
function loadAuslandsangebote() {
    var angeboteInhalt = '<option>Alle Angebote</option>';
    $.ajax({
        type: "GET",
        url: baseUrl + "/auslandsAngebote",
        success: function (result) {
            for (var i = 0; i < result.data.length; i++) {
                angeboteInhalt = angeboteInhalt + '<option>' + result.data[i].studiengang + '</option>';
            }
            $('#selStudiengang').html(angeboteInhalt);
        }
    });
    // Auswahlmöglichkeit auf der Startseite zur Sortierung der Angebote
    $('#selStudiengang').on('change', function (event) {
        if ($(this).val() != "Alle Angebote") {
            $('.angebote').hide();
            $('.' + $(this).val()).show();
        } else {
            $('.angebote').show();
        }
    });
}

// Lädt die Auslandsangebote auf die Seite
function loadAuslandsangeboteInhalt() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/auslandsAngebotsInhalte",
        success: function (data) {
            var result = data.data;
            var htmlText = '';
            for (var i = 0; i < result.length; i++) {
                if (!result[i].erfahrungsbericht) {
                    result[i].erfahrungsbericht = "Keine Erfahrungsberichte vorhanden.";
                }

                htmlText = htmlText +
                    '<div class="nonCms angebote ' +
                    result[i].studiengang +
                    '" id="angebot' + i +
                    '" style="margin-right: 5px;"><div class="col-md-12">' +
                    '<h3 class="uniName">' +
                    result[i].uniTitel +
                    '</h2>' +
                    '<div class="navBarAng">' +
                    '<div class="navelAng active" id="n' +
                    i +
                    '1">Infos</div>' +
                    '<div class="navelAng" id="n' +
                    i +
                    '2">FAQs</div>' +
                    '<div class="navelAng" id="n' +
                    i +
                    '3">Erfahrungsbericht</div>' +
                    '<div class="navelAng" id="n' +
                    i +
                    '4">Bilder</div>' +
                    '</div>' +
                    '<div class="contentAng active" id="c' +
                    i +
                    '1"><div class="row"><div class="col-md-7">' +
                    result[i].allgemeineInfos + '<br/>' + '<br/>'
                    + 'Mögliche Studiengänge für diese Hochschule: ' + result[i].studiengang
                    + '</div><div class="col-md-5">';
                if (result[i].maps) {
                    htmlText = htmlText +
                        '<iframe width="400" height="200" src="' +
                        result[i].maps +
                        '" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>';
                } else {
                    htmlText = htmlText +
                        '<p>Keine Kartendaten gefunden.</p>';
                }
                htmlText = htmlText + '</div></div></div>' +
                    '<div class="contentAng" id="c' + i +
                    '2">' +
                    result[i].faq +
                    '</div>' +
                    '<div class="contentAng" id="c' + i +
                    '3">' +
                    result[i].erfahrungsbericht +
                    '</div>' +
                    '<div class="contentAng" id="c' + i +
                    '4">Keine Bilder vorhanden.</div>' +
                    '</div>' + '</div>';

            }
            $('#angebote-wrapper').html(htmlText);
            for (var i = 0; i < result.length; i++) {
                for (var j = 1; j <= 4; j++) {
                    document
                        .getElementById('n' + i + j)
                        .addEventListener(
                            'click',
                            function (event) {
                                var id = $(this).parent()
                                    .parent().parent()
                                    .attr('id');
                                $('#' + id).children()
                                    .children().children(
                                        '.navelAng')
                                    .removeClass('active');
                                $(this).addClass('active');
                                $('#' + id)
                                    .children()
                                    .children('.contentAng')
                                    .removeClass('active');
                                id = $(this).attr('id')
                                    .replace('n', '');
                                $('#c' + id).addClass('active');
                            });
                }
            }
        }
    });
}

const prioArr = [];

function initBewerben() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/getUserInstances",
        data: {
            matnr: sessionStorage['matrikelnr']
        },
        success: function (result) {
            var tabelle = '<table class="table table-bordered table-hover"><thead><tr><th>Universität</th><th>Status</th><th>Priorität</th><th colspan="3">Aktionen</th></tr></thead>';
            if (result.data.length == 0) {
                $('#tableBewProzess').html('<h2>Keine Bewerbungen vorhanden</h2>');
            } else {
                for (var i = 0; i < result.data.length; i++) {
                   	var instance_info = result.data[i];
                    tabelle = tabelle + '<tr data-rid="' + (i + 1) + '"><td>' + instance_info.uni + '</td><td>' + instance_info.stepCounter + '</td>';
                    tabelle += '<td>'+instance_info.prioritaet+'</td>';
                    prioArr.push(instance_info.prioritaet);
                    //Anlegen der Buttons
                    if ((instance_info.stepCounter === "Abgeschlossen") || (instance_info.stepCounter === "Auf Rückmeldung warten") || (instance_info.stepCounter === "Bewerbung wurde abgelehnt") ) {
                        //Übersicht
                        tabelle = tabelle + '<td align="center"><span class="btn fas fa-list" title="Übersicht" onclick="location.href=\'task_detail.html?instance_id=' + instance_info.instanceID + '&uni=' + instance_info.uni + '\'"></span></td>';
                    } else if (instance_info.stepCounter === "Daten prüfen") {
                        //Übersicht
                        tabelle = tabelle + '<td align="center"><span class="btn fas fa-list" title="Übersicht" onclick="location.href=\'task_detail.html?instance_id=' + instance_info.instanceID + '&uni=' + instance_info.uni + '&send_bew=true\'"></span></td>';
                        //Prozess löschen
						tabelle = tabelle + '<td align="center"><span uni="' + instance_info.uni + '" class="btn fas fa-trash btn-delete" title="Löschen" rid="' + (i + 1) + '"></span></td>';
						//Prozess bearbeiten
						tabelle = tabelle + '<td align="center"><span instance="' + instance_info.instanceID + '" class="btn btn-edit fas fa-edit" title="Bearbeiten"></span></td>';
                    } else {
                        //Fortsetzen
                        tabelle = tabelle + '<td align="center"><span class="btn fas fa-arrow-right" title="fortsetzen" onclick="location.href=\'bewerben.html?instance_id=' + instance_info.instanceID + '&uni=' + instance_info.uni + '\'"></span></td>';
                        //Prozess löschen
						tabelle = tabelle + '<td align="center"><span uni="' + instance_info.uni + '" class="btn fas fa-trash btn-delete" title="Löschen" rid="' + (i + 1) + '"></span></td>';
						//Prozess bearbeiten
						tabelle = tabelle + '<td align="center"><span instance="' + instance_info.instanceID + '" class="btn btn-edit fas fa-edit" title="Bearbeiten"></span></td>';
                    }
                    tabelle = tabelle + '</tr>'
                }
                tabelle = tabelle + '</table>';
                $('#tableBewProzess').html(tabelle);
                initDeleteProcessButtons();
                initEditProcessButtons();
            }
        },
        error: function (result) {
            Swal.fire("Fehler", "Beim Abrufen der laufenden Prozesse ist ein Fehler aufgetreten", "error");
        }
    });
}

function initEditProcessButtons() {
    $('.btn-edit').on('click', function () {
		var instance = $(this).attr("instance");
		var popUpHtml = '<div class="form-group"><div class="col-md-12"><select class="inBox" id="selectEditPrio">';
		popUpHtml += '<option value="0"> Priorität auswählen </option>';

		for (var i = 1; i <= 5; i++) {
			popUpHtml += '<option>' + i + '</option>';
		}

		popUpHtml = popUpHtml + '</select></div></div>';
		Swal.fire({
			title: 'Bitte wählen Sie eine neue Priorität aus',
			html: popUpHtml,
		}).then(function (result) {
			$.ajax({
                type: "GET",
                url: baseUrl + "/changePriority",
                data: {
                    instance: instance,
                    prio: $('#selectEditPrio').val(),
                }
            }).done(function (data) {
                location.reload();
            });
		});
	});
}


function initDeleteProcessButtons() {
    $('.btn-delete').on('click', function () {
        var uni = $(this).attr("uni");
        var id = $(this).attr("rid");
        Swal.fire({
            title: "Bist du sicher?",
           	text: "Der Prozess kann nicht wiederhergestellt werden!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Löschen!",
        }).then(function (result) {
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
