import {$} from "../config";
import Swal from "sweetalert2";
import "bootstrap";
import "jquery-form-validator";
import "datatables.net-bs4";
import "cookieconsent";

$(document).ready(function () {
	$('#adminBereich').hide();
    $('#normalBereich').show();
    $('.cms').show();
    $('.Admin').show();
    $('.cmsBtn').on('click', function () {
        if ($(this).text() === "Bearbeiten" ||
            $(this).text() === "Abbrechen") {
            var id = $(this).parent().parent().attr('id');
            switch (id) {
                case 'portalInfo':
                    if ($(this).text() === "Bearbeiten") {
                        $('#portalInfo').children().children(
                            '.nonCms').hide();
                        $('.cmsPortal').show();
                        $('#portalInfo').children('.col-md-6')
                            .children('.titel').hide();
                        var titel = $('#portalInfo').children(
                                '.col-md-6').children('.titel')
                            .text();
                        $('#inPortalTitel').val(titel);
                        $('.inputsPortal').empty();
                        for (var i = 1; $('#li' + i).text() != ""; i++) {
                            $('.inputsPortal')
                                .append(
                                    '<input class="inBox" id="inli' +
                                    i +
                                    '" value="' +
                                    $('#li' + i)
                                    .text() +
                                    '" /><img class="small" id="delPoLi' +
                                    i +
                                    '" src="images/Button Delete.png" />');
                            $('#delPoLi' + i).click(function (event) {
                                delPoLi(event);
                            });
                        }
                        $(this).text('Abbrechen');
                    } else if ($(this).text() === "Abbrechen") {
                        $('#portalInfo').children().children(
                            '.nonCms').show();
                        $('.cmsPortal').hide();
                        $('#portalInfo').children('.col-md-6')
                            .children('.titel').show();
                        $(this).text('Bearbeiten');
                    }
                    break;
                case 'auslandsAngebote':
                    if ($(this).text() === "Bearbeiten") {
                        $('.cmsAngebote').show();
                        $('#auslandsAngebote').children('.nonCms')
                            .hide();
                        $(this).text('Abbrechen');
                    } else if ($(this).text() === "Abbrechen") {
                        $('.cmsAngebote').hide();
                        $('#auslandsAngebote').children('.nonCms')
                            .show();
                        $('#cmsAddStudgang').hide();
                        $('#cmsAddAngebot').hide();
                        $('#cmsEditAngebot').hide();
                        $(this).text('Bearbeiten');
                    }
                    break;
                case 'erfahrungsBerichte':
                    //Swal.fire('Dieser Bereich kann noch nicht bearbeitet werden.');

                    if ($(this).text() === "Bearbeiten") {
                        $('.kurzbericht').show();
                        $('.erfahrungsBerichte').children().children().children('.nonCms').hide();
                        $('.erfahrungsBerichte').children().children().children().children('.nonCms').hide();

                        for (var i = 1; $('#kb' + i).html() != undefined; i++) {
                            $('#kb' + i).children().children('textarea').text($('#kb').children().children('.middle').text().trim());
                        }
                        $('.cmsBerichte').show();
                        $(this).text('Abbrechen');
                    } else if ($(this).text() === "Abbrechen") {
                        $('#erfahrungsBerichte').children().children().children('.nonCms').show();
                        $('.erfahrungsBerichte').children().children().children().children('.nonCms').show();
                        $('.cmsBerichte').hide();
                        $('.kurzbericht').hide();
                        $('.zeig').show();
                        $(this).text('Bearbeiten');
                    }
                    break;

                case 'infoMaterial':
                    if ($(this).text() === "Bearbeiten") {
                        $('#infoMaterial').children().children(
                            '.nonCms').hide();
                        $('.cmsInfo').show();
                        var titel = $('#infoMaterial').children(
                                '.col-md-6').children('.titel')
                            .text();
                        $('#inInfoTitel').val(titel);
                        var htmlText = '';
                        for (var i = 1; $('#infoli' + i).children(
                                'a').text() != ""; i++) {
                            $('#inInfoLi' + i).val(
                                $('#infoli' + i).children('a')
                                .text());
                            $('#inInfoLink' + i).val(
                                $('#infoli' + i).children('a')
                                .attr('href'));
                            htmlText = htmlText +
                                '<input class="inBox" id="inInfoLi' +
                                i +
                                '" value="' +
                                $('#infoli' + i)
                                .children('a').text() +
                                '" /><img class="small" id="delInLi' +
                                i +
                                '" src="images/Button Delete.png" /><br>' +
                                '<b style="margin-right: 5px">Link</b><input class="inBox infoLink" id="inInfoLink' +
                                i +
                                '" value="' +
                                $('#infoli' + i)
                                .children('a').attr(
                                    'href') +
                                '"><img class="small" id="delLink' +
                                i +
                                '" src="images/Button Delete.png" />';
                        }
                        $('.inputsInfo').html(htmlText);
                        for (var i = 1; $('#infoli' + i).children(
                                'a').text() != ""; i++) {
                            document
                                .getElementById('delInLi' + i)
                                .addEventListener(
                                    'click',
                                    function (event) {
                                        var id = $(this)
                                            .attr('id');
                                        id = id.substring(
                                            7, 8);
                                        $('#inInfoLi' + id)
                                            .val('');
                                    });
                        }
                        for (var i = 1; $('#infoli' + i).children(
                                'a').text() != ""; i++) {
                            document
                                .getElementById('delLink' + i)
                                .addEventListener(
                                    'click',
                                    function (event) {
                                        var id = $(this)
                                            .attr('id');
                                        id = id.substring(
                                            7, 8);
                                        $(
                                                '#inInfoLink' +
                                                id)
                                            .val('');
                                    });
                        }
                        for (var i = 1; $('#infoli' + i).children(
                                'a').text() != ""; i++) {
                            document.getElementById('inInfoLi' + i)
                                .addEventListener('click',
                                    function (event) {});
                        }
                        for (var i = 1; $('#infoli' + i).children(
                                'a').text() != ""; i++) {
                            document.getElementById(
                                    'inInfoLink' + i)
                                .addEventListener('click',
                                    function (event) {});
                        }
                        $(this).text('Abbrechen');
                    } else if ($(this).text() === "Abbrechen") {
                        $('#infoMaterial').children().children(
                            '.nonCms').show();
                        $('.cmsInfo').hide();
                        $(this).text('Bearbeiten');
                    }
                    break;
            }
        } else if ($(this).text() === "&aumlnderungen speichern") {
            var klasse = $(this).attr('class');
            klasse = klasse.replace('btn', "");
            klasse = klasse.replace('cmsBtn', "");
            klasse = klasse.trim();
            switch (klasse) {
                case ('cmsPortal'):
                    var titel, listelement1, listelement2, listelement3, listelement4, listelement5, listelement6, listelement7;
                    titel = $('#inPortalTitel').val();
                    listelement1 = $('#inli1').val();
                    listelement2 = $('#inli2').val();
                    listelement3 = $('#inli3').val();
                    listelement4 = $('#inli4').val();
                    listelement5 = $('#inli5').val();
                    listelement6 = $('#inli6').val();
                    listelement7 = $('#inli7').val();
                    if (listelement1 === undefined) {
                        listelement1 = "";
                    } else if (listelement2 === undefined) {
                        listelement2 = "";
                    } else if (listelement3 === undefined) {
                        listelement3 = "";
                    } else if (listelement4 === undefined) {
                        listelement4 = "";
                    } else if (listelement5 === undefined) {
                        listelement5 = "";
                    } else if (listelement6 === undefined) {
                        listelement6 = "";
                        if (listelement7 === undefined) {
                            listelement7 = "";
                        }
                    } else if (listelement7 === undefined) {
                        listelement7 === "";
                    }
                    $
                        .ajax({
                            type: "POST",
                            url: "login_db",
                            data: {
                                action: "post_portalInfo",
                                titel: titel,
                                listelement1: listelement1,
                                listelement2: listelement2,
                                listelement3: listelement3,
                                listelement4: listelement4,
                                listelement5: listelement5,
                                listelement6: listelement6,
                                listelement7: listelement7,
                            },
                            success: function (result) {
                                $('.erfolgreich')
                                    .html(
                                        "Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
                                $('.erfolgreich').show();
                                $('.erfolgreich').fadeOut(7000);
                            },
                            error: function (result) {
                                Swal.fire('Fehler');
                            }
                        });
                    break;
                case ('cmsAngebote'):
                    if ($(this).parent().attr('id') === "angebotNeu") {
                        if ($('#inStudiengangNeuAngebot').val() != "Studiengang wählen*" &&
                            isEmpty($('#inNeuAngebot').val()) != true &&
                            isEmpty($('#inNeuAngebotInfo')
                                .val()) != true &&
                            isEmpty($('#inMaps1').val()) != true &&
                            isEmpty($('#inMaps2').val()) != true &&
                            isEmpty($('#inMaps3').val()) != true) {
                            var faqs = '';
                            for (var i = 1; isEmpty($(
                                    '#inNeuFaqFr' + i).val()) != true; i++) {
                                faqs = faqs +
                                    '<b>' +
                                    $('#inNeuFaqFr' + i)
                                    .val() +
                                    '</b><br>' +
                                    $('#inNeuFaqAn' + i)
                                    .val() + '<br>';
                            }
                            var erfahrungsbericht = $(
                                    '#inNeuAngebotErfahrungsbericht')
                                .val() +
                                '<br><i>' +
                                $('#inNeuAngebotErfAutor')
                                .val() + '</i>';
                            var maps = 'https://www.google.com/maps/embed/v1/place?key=AIzaSyA76FO67-tqO0XWItx9KtXDgcta9mYHjrM&q=' +
                                $('#inMaps1').val() +
                                ',' +
                                $('#inMaps2').val().replace(
                                    ' ', '') +
                                '+' +
                                $('#inMaps3').val().replace(
                                    ' ', '');
                            $
                                .ajax({
                                    type: "POST",
                                    url: "login_db",
                                    data: {
                                        action: "post_newAuslandsangebot",
                                        studiengang: $(
                                                '#inStudiengangNeuAngebot')
                                            .val(),
                                        uniTitel: $(
                                                '#inNeuAngebot')
                                            .val(),
                                        allgemeineInfos: $(
                                                '#inNeuAngebotInfo')
                                            .val(),
                                        faq: faqs,
                                        erfahrungsbericht: erfahrungsbericht,
                                        maps: maps,
                                    },
                                    success: function (event) {
                                        $('.erfolgreich')
                                            .html(
                                                "Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
                                        $('.erfolgreich')
                                            .show();
                                        $('.erfolgreich')
                                            .fadeOut(7000);
                                    },
                                    error: function (event) {

                                    }
                                });
                        } else {
                            Swal.fire({
                                title: "Fehlende Angaben",
                                text: "Bitte tragen Sie in alle mit * gekennzeichneten Feldern etwas ein",
                                icon: "error",
                                confirmButtonText: "OK"
                            });

                        }
                    } else if ($(this).parent().attr('id') === 'angebotEdit') {
                        if (isEmpty($('#EditAngebot').text().trim()) != true &&
                            isEmpty($('#inEditAngebotInfo')
                                .val()) != true &&
                            isEmpty($('#inEditMaps1').val()) != true &&
                            isEmpty($('#inEditMaps2').val()) != true &&
                            isEmpty($('#inEditMaps3').val()) != true) {
                            var faqs = '';
                            for (var i = 1; isEmpty($(
                                    '#inEditFaqFr' + i).val()) != true; i++) {
                                faqs = faqs +
                                    '<b>' +
                                    $('#inEditFaqFr' + i)
                                    .val() +
                                    '</b><br>' +
                                    $('#inEditFaqAn' + i)
                                    .val() + '<br>';
                            }
                            var erfahrungsbericht = $(
                                    '#inEditAngebotErfahrungsbericht')
                                .val() +
                                '<br><i>' +
                                $('#inEditAngebotErfAutor')
                                .val() + '</i>';
                            var maps = 'https://www.google.com/maps/embed/v1/place?key=AIzaSyA76FO67-tqO0XWItx9KtXDgcta9mYHjrM&q=' +
                                $('#inEditMaps1').val() +
                                ',' +
                                $('#inEditMaps2').val()
                                .replace(' ', '') +
                                '+' +
                                $('#inEditMaps3').val()
                                .replace(' ', '');
                            $
                                .ajax({
                                    type: "POST",
                                    url: "login_db",
                                    data: {
                                        action: "post_editAuslandsangebot",
                                        studiengang: $(
                                                '#inStudiengangEditAngebot')
                                            .val(),
                                        uniTitel: $(
                                                '#EditAngebot')
                                            .text().trim(),
                                        allgemeineInfos: $(
                                                '#inEditAngebotInfo')
                                            .val(),
                                        faq: faqs,
                                        erfahrungsbericht: erfahrungsbericht,
                                        maps: maps,
                                    },
                                    success: function (event) {
                                        $('.erfolgreich')
                                            .html(
                                                "Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
                                        $('.erfolgreich')
                                            .show();
                                        $('.erfolgreich')
                                            .fadeOut(7000);
                                    }
                                });
                        } else {
                            Swal.fire({
                                title: "Fehlende Angaben",
                                text: "Bitte tragen Sie in alle mit * gekennzeichneten Feldern etwas ein",
                                icon: "error",
                                confirmButtonText: "OK"
                            });
                        }
                    }
                    break;
                case ('cmsInfo'):
                    for (var i = 1; i <= 7; i++) {
                        if (isEmpty($('#inInfoLi' + i).val()) === true) {
                            $('#inInfoLi' + i).val('null');
                        }
                        if (isEmpty($('#inInfoLink' + i).val()) === true) {
                            $('#inInfoLink' + i).val('null');
                        }
                    }
                    $
                        .ajax({
                            type: "POST",
                            url: "login_db",
                            data: {
                                action: "post_infoMaterial",
                                titel: $('#inInfoTitel').val(),
                                listelement1: $('#inInfoLi1')
                                    .val(),
                                link1: $('#inInfoLink1').val(),
                                listelement2: $('#inInfoLi2')
                                    .val(),
                                link2: $('#inInfoLink2').val(),
                                listelement3: $('#inInfoLi3')
                                    .val(),
                                link3: $('#inInfoLink3').val(),
                                listelement4: $('#inInfoLi4')
                                    .val(),
                                link4: $('#inInfoLink4').val(),
                                listelement5: $('#inInfoLi5')
                                    .val(),
                                link5: $('#inInfoLink5').val(),
                                listelement6: $('#inInfoLi6')
                                    .val(),
                                link6: $('#inInfoLink6').val(),
                                listelement7: $('#inInfoLi7')
                                    .val(),
                                link7: $('#inInfoLink7').val(),
                            },
                            success: function (event) {
                                $('.erfolgreich').html("Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
                                $('.erfolgreich').show();
                                $('.erfolgreich').fadeOut(7000);
                            }
                        });
                    break;
            }
        }
    });

    function delPoLi(event) {
        var id = event.target.id;
        id = id.substring(
            7, 8);
        var el = $('#inli' + id);
        if (el.val() == "" || el.val() == undefined) {
            el.remove();
            $('#delPoLi' + id).remove();

        } else {
            el.val('');
        }
    }
    // Click Listener für die Bild ändern Buttons
    $('.upload').on('click', function () {
        var id = $(this).attr('id');
        id = id.trim();
        loop1(id);
    });
    // Click-Listener für neues Infoselement in Box PortalInfo
    $('#cmsBtnInfoNeu').on('click', function () {
        var count = $('.inputsPortal .inBox').length;
        if (count > 6) {
            Swal.fire('Warnung', 'Zum aktuellen Zeitpunkt können nicht mehr als 7 Infoelemente hinzugefügt werden', 'warning');
        } else {
            $('.inputsPortal').append('<input class="inBox" id="inli' + (count + 1) +
                '" value="" /><img class="small" id="delPoLi' +
                (count + 1) +
                '" src="images/Button Delete.png" />');
            $('#delPoLi' + (count + 1)).click(function (event) {
                delPoLi(event);
            });
        }
    });
    // Click-Listener für neuen Erfahrungsbericht in Box Erfahrungsbericht
    $('#cmsBtnBerichtNeu').on('click', function () {
        var count = 0;
        for (var i = 1; $('#kb' + i).html() !== undefined; i++) {
            count = i;
        }
        $('.berichte')
            .append(
                '<div class="row kurzbericht" id="kb' +
                (count + 1) +
                '"><div class="col-md-6"><img src="images/noimg.jpg" id="imgWorld" /><label class="btn cmsBtn cmsBerichte btnBild" id="btnberichtBild' +
                (count + 1) +
                '" style="float: right">Bild ändern<input class="upload" type="file" id="berichtBild' +
                (count + 1) +
                '" /></label></div><div class="col-md-6 cmsBerichte"><textarea class="inBox" style="width: 100%; height: 250px"></textarea></div>');
        $('.kurzbericht').show();
        $('.cmsBerichte').show();
    });
    // Click-Listener für neuen Link in Box InfoMaterial
    $('#cmsBtnLinkNeu').on('click', function () {
        var count = 0;
        for (var j = 1; isEmpty($('#infoli' + j).children('a')
                .text()) != true; j++) {
            count = j;
        }
        $('.inputsInfo')
            .html(
                $('.inputsInfo').html() +
                '<input class="inBox" id="inInfoLi' +
                (count + 1) +
                '" value="" /><img class="small" id="delInLi' +
                (count + 1) +
                '" src="images/Button Delete.png" /><br><b style="margin-right: 5px">Link</b><input class="inBox infoLink" id="inInfoLink' +
                (count + 1) +
                '" value=""><img class="small" id="delLink' +
                (count + 1) +
                '" src="images/Button Delete.png" />');
        $('#cmsBtnLinkNeu').hide();
    });
    // Click-Listener f�r CMS Buttons im Bereich Auslandsangebote
    $('.btnAngebote').on('click', function (event) {
        if ($(this).attr('id') === "btnAddStudgang") {
            $('#cmsAddStudgang').show();
            $('#cmsAddAngebot').hide();
            $('#cmsEditAngebot').hide();
        } else if ($(this).attr('id') === "btnAddAngebot") {
            $('#cmsAddStudgang').hide();
            $('#cmsAddAngebot').show();
            $('#cmsEditAngebot').hide();
            $('#inStudiengangNeuAngebot').html(
                $('#selStudiengang').html().replace(
                    "Alle Angebote",
                    "Studiengang wählen*"));
            for (var i = 1; i <= 1; i++) {
                for (var j = 1; j <= 4; j++) {
                    document
                        .getElementById('nNeu' + i + j)
                        .addEventListener(
                            'click',
                            function (event) {
                                var id = $(this)
                                    .parent()
                                    .parent()
                                    .parent().attr(
                                        'id');
                                $('#' + id)
                                    .children()
                                    .children()
                                    .children(
                                        '.navelAng')
                                    .removeClass(
                                        'active');
                                $(this).addClass(
                                    'active');
                                $('#' + id)
                                    .children()
                                    .children(
                                        '.contentAng')
                                    .removeClass(
                                        'active');
                                id = $(this).attr('id')
                                    .replace(
                                        'nNeu',
                                        '');
                                $('#cNeu' + id)
                                    .addClass(
                                        'active');
                            });
                }
            }
        } else if ($(this).attr('id') === "btnEditAngebot") {
            $('#cmsAddStudgang').hide();
            $('#cmsAddAngebot').hide();
            $('#cmsEditAngebot').show();
            var options = '<option>Angebot wählen</option>';
            for (var i = 1; isEmpty($('#angebot' + i)
                    .children().children('.uniName').text()) != true; i++) {
                options = options +
                    '<option>' +
                    $('#angebot' + i).children()
                    .children('.uniName').text() +
                    '</option>';
            }
            $('#selEditAngebot').html(options);
            for (var i = 1; i <= 1; i++) {
                for (var j = 1; j <= 4; j++) {
                    document
                        .getElementById('nEdit' + i + j)
                        .addEventListener(
                            'click',
                            function (event) {
                                var id = $(this)
                                    .parent()
                                    .parent()
                                    .parent().attr(
                                        'id');
                                $('#' + id)
                                    .children()
                                    .children()
                                    .children(
                                        '.navelAng')
                                    .removeClass(
                                        'active');
                                $(this).addClass(
                                    'active');
                                $('#' + id)
                                    .children()
                                    .children(
                                        '.contentAng')
                                    .removeClass(
                                        'active');
                                id = $(this)
                                    .attr('id')
                                    .replace(
                                        'nEdit',
                                        '');
                                $('#cEdit' + id)
                                    .addClass(
                                        'active');
                            });
                }
            }
        }
    });
    // Click Listener f�r neuen Studiengang anlegen
    $('#btnSaveNewStudiengang').on('click', function (event) {
        if (isEmpty($('#inNewStudiengang').val()) != true) {
            $
                .ajax({
                    type: "POST",
                    url: "login_db",
                    data: {
                        action: "post_newStudiengang",
                        studiengang: $('#inNewStudiengang')
                            .val(),
                    },
                    success: function (result) {
                        $('.erfolgreich')
                            .html(
                                "Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
                        $('.erfolgreich').show();
                        $('.erfolgreich').fadeOut(7000);
                    },
                    error: function (result) {

                    }
                });
        } else {
            Swal.fire("Sie dürfen das Feld nicht leer lassen.");
        }
    });
    // Click-Listener für ein Angebot bearbeiten
    $('#selEditAngebot').on('click', function (event) {
        for (var i = 1; isEmpty($('#angebot' + i)) != true; i++) {
            if ($('#angebot' + i).children().children(
                    '.uniName').text().trim() === $(this).val()) {
                $('#EditAngebot').text(
                    $('#angebot' + i).children().children(
                        '.uniName').text());
                $('#inEditAngebotInfo').val(
                    $('#angebot' + i).children().children(
                        '#c' + i + '1').children(
                        '.pull-left').text().trim());
                if (isEmpty($('#angebot' + i).children()
                        .children('#c' + i + '1').children(
                            '.pull-right').children().attr(
                            'src')) != true) {
                    var maps = $('#angebot' + i).children()
                        .children('#c' + i + '1').children(
                            '.pull-right').children()
                        .attr('src').split('q=');
                    maps = maps[1].split(',');
                    var help = maps[1].split('+');
                    maps[1] = help[0];
                    maps[2] = help[1];
                    $('#inEditMaps1').val(maps[0]);
                    $('#inEditMaps2').val(maps[1]);
                    $('#inEditMaps3').val(maps[2]);
                } else {
                    $('#inEditMaps1').val('');
                    $('#inEditMaps2').val('');
                    $('#inEditMaps3').val('');
                }
                // Erfahrungsbericht in input Felder eintragen
                if (isEmpty($('#angebot' + i).children()
                        .children('#c' + i + '3').html()) != true) {
                    var erfahrungsbericht = $('#angebot' + i)
                        .children()
                        .children('#c' + i + '3').html()
                        .split('<br>');
                    $('#inEditAngebotErfahrungsbericht').val(
                        erfahrungsbericht[0]);
                    erfahrungsbericht[1] = erfahrungsbericht[1]
                        .replace('<i>', '');
                    erfahrungsbericht[1] = erfahrungsbericht[1]
                        .replace('</i>', '');
                    $('#inEditAngebotErfAutor').val(
                        erfahrungsbericht[1]);
                    erfahrungsbericht = [];
                } else {
                    $('#inEditAngebotErfahrungsbericht')
                        .val('');
                    $('#inEditAngebotErfAutor').val('');
                }
                // FAQs in InputFelder �bernehmen
                if (isEmpty($('#angebot' + i).children()
                        .children('#c' + i + '2').html()) != true) {
                    var faqs = $('#angebot' + i).children()
                        .children('#c' + i + '2').html()
                        .replace(/<b>/g, ';');
                    faqs = faqs.replace(/<br>/g, '');
                    faqs = faqs.replace(/</g, '');
                    faqs = faqs.replace(/b>/g, ';');
                    faqs = faqs.trim();
                    faqs = faqs.split(';');
                    var hilfe = [];
                    var k = 0;
                    var frage = 1;
                    for (var j = 0; j < faqs.length; j++) {
                        if (isEmpty(faqs[j].trim()) != true) {
                            hilfe[k] = faqs[j].trim();
                            if ((k + 1) % 2 === 1) {
                                $('#inEditFaqFr' + frage).val(
                                    hilfe[k].replace('?/',
                                        '?'));
                            } else {
                                $('#inEditFaqAn' + frage).val(
                                    hilfe[k]);
                                frage++;
                            }
                            k++;
                        }
                        if (faqs.length < 4) {
                            for (var m = 2; m <= 4; m++) {
                                $('#inEditFaqFr' + m).val('');
                                $('#inEditFaqAn' + m).val('');
                            }
                        } else if (faqs.length < 6) {
                            for (var m = 3; m <= 4; m++) {
                                $('#inEditFaqFr' + m).val('');
                                $('#inEditFaqAn' + m).val('');
                            }
                        } else if (faqs.length < 8) {
                            for (var m = 4; m <= 4; m++) {
                                $('#inEditFaqFr' + m).val('');
                                $('#inEditFaqAn' + m).val('');
                            }
                        }
                    }
                } else {
                    for (var l = 1; l <= 4; l++) {
                        $('#inEditFaqFr' + l).val('');
                        $('#inEditFaqAn' + l).val('');
                    }
                }
                $('#angebotEdit').show();
            }
        }
    });
});
