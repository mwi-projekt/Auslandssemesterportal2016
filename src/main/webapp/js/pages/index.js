import {$,baseUrl} from "../config";
import {urlParams} from "../app";
import Swal from "sweetalert2";

$(document).ready(function () {
    // check role and redirect
    if (sessionStorage['User']) {
        if (sessionStorage['rolle'] == 2) {
            window.location.href = 'task_overview.html'
        } else if (sessionStorage['rolle'] == 4) {
            window.location.href = 'task_overview_sgl.html';
        } else if (sessionStorage['rolle'] == 1) {
            window.location.href = 'cms.html';
        }
    }

    // init ui
    initSlider();
    initArrows();

    if (urlParams.get('confirm') != null && urlParams.get('confirm').trim() != '') {
        var link = urlParams.get('confirm');
        $.ajax({
            type: "GET",
            url: baseUrl + "/confirm?code=" + link,
            success: function (result) {
                Swal.fire({
                    title: "Nutzeraccount bestätigt",
                    text: "Ihre Mailadresse wurde bestätigt. Sie können sich jetzt einloggen",
                    icon: "success",
                    confirmButtonText: "OK"
                });
            }
        });

    }
    loadPortalInfo();
    loadAuslandsangebote();
    loadInfoMaterial();
    clearModalOnDismiss();
});

// Zurücksetzen der Modals, wenn diese geschlossen werden
function clearModalOnDismiss() {
    $('#login').on('hidden.bs.modal', function () {
        $(this).find('form').trigger('reset');
    })

    $('#register').on('hidden.bs.modal', function () {
        $(this).find('form').trigger('reset');
    })
}

// Laden der Daten der PortalInfo Box
function loadPortalInfo() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/portalInfo",
        success: function (data) {
            var result = data.data[0];
            if (result.titel) {
                $('#portalInfo').children().children('.titel').text(result.titel);
            }
            if (result.werbesatz) {
                $('#portalInfo').children().children('.red').text(result.werbesatz);
            }
            for (var i = 1; i <= 7; i++) {
                if (result['listelement' + i] && result['listelement' + i] != 'null') {
                    $('#portalInfo').children().children('.funktionen')
                        .append('<li id="li' + i + '">' + result['listelement' + i] + ' </li>');
                }
            }
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
}

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

// Setzt den back to top Pfeil auf invisible wenn man sich ganz oben auf der Seite befindet
$(document).scroll(function () {
    if ($(window).scrollTop() === 0) {
        $(".chevronup").css('opacity', '0');
        $(".chevronup").css('cursor', 'initial');
    } else {
        $(".chevronup").css('opacity', '1');
        $(".chevronup").css('cursor', 'pointer');
    }
});

// Läd die Daten für die Infomaterialien auf die Seite
function loadInfoMaterial() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/infoMaterial",
        success: function (data) {
            var result = data.data;
            $('#infoMaterialTitel').text(result.titel);
            for (var i = 1; i <= 7; i++) {
                if (result['listelement' + i]) {
                    $('#infoli' + i).children('a').text(
                        result['listelement' + i]);
                    $('#infoli' + i).children('a').attr('href',
                        result['link' + i]);
                } else {
                    $('#infoli' + i).hide();
                }
            }
        }
    });
}

function initSlider() {
    var back = 1;
    $('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
    setInterval(function () {
        back++;
        if (back == 4) back = 1;
        $('.imgSlider').fadeTo('slow', 0, function () {
            $('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
        }).fadeTo('800', 0.9);
    }, 6000);
}

function initArrows() {
    $('.arrow').on('click', function () {
        var dir = $(this).attr('id');
        var id = parseInt($('.zeig').attr('id').substring(2, 3));
        if (dir === "arrLeft") {
            id--;
            if (id == 0) {
                id = $('.kurzbericht').length;
            }
        } else if (dir === "arrRight") {
            id++;
            if ($('#kb' + id).length == 0) {
                id = 1;
            }
        }
        $('.kurzbericht').removeClass('zeig');
        $('#kb' + id).addClass('zeig');
    });
}
