$(document).ready(function () {
    // check if logged in
    if (sessionStorage['User']) {
        if (sessionStorage['role'] === "2") {
            $('.cms').show();
            $('.nonCms').show();
            $('.Admin').hide();
            $('.Mitarbeiter').show();
            $('.portalInfo').show();
        } else if (sessionStorage['role'] === "1") {
            if (sessionStorage['verwaltung'] === "0" ||
                sessionStorage['verwaltung'] === undefined) {
                $('#normalBereich').hide();
                $('#adminBereich').show();
            } else if (sessionStorage['verwaltung'] === "1") {
                $('#normalBereich').show();
                $('.cms').show();
                $('#adminBereich').hide();
                $('.Admin').show();
                $('.Mitarbeiter').hide();
            } else if (sessionStorage['verwaltung'] === "2") {
                $('#adminBereich').hide();
                //$('#nutzerVerwaltung').show();
                $('#normalBereich').hide();
            }
            $('.Mitarbeiter').hide();
            $('.portalInfo').show();
        } else {
            $('.cms').hide();
            $('.nonCms').show();
            $('.eingeloggt').css('display', 'inline');
        }
    }
    
    
    if (sessionStorage['role'] === "1") {
    	document.getElementById("zumPortal").href = "index.jsp";
    }
    else if (sessionStorage['role'] === "2") { 
    	document.getElementById("zumPortal").href = "task_overview.html";
    	document.getElementById("zumPortal").innerHTML = "<a style= \"color: white \">Bewerbungen Validieren</a>";
    }
    else if (sessionStorage['role'] === "3") {
    	document.getElementById("zumPortal").href = "bewerbungsportal.html";
    }
    else if (sessionStorage['role'] === "4") {
    	 document.getElementById("zumPortal").href = "task_overview_sgl.html";	
    	 document.getElementById("zumPortal").innerHTML = "<a style= \"color: white \">Bewerbungen Validieren</a>";
    }
    
    // init ui
    initSlider();
    initArrows();

    if ($.urlParam('confirm') != null && $.urlParam('confirm').trim() != '') {
        var link = $.urlParam('confirm');
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
    loadAuslandsangeboteInhalt();
    loadInfoMaterial();

    // Click Listener für die Tiles im AdminBereich
    $('.tile').on('click', function () {
        var id = $(this).attr('id');
        if (id === 'verwaltungIndex') {
            location.href = 'cms.jsp';
        } else if (id === 'verwaltungUser') {
            location.href = 'nutzer.html';
        } else if (id === 'verwaltungPortal') {
            location.href = 'choose_diagram.html';
        }
    });
});

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

// Läd die Auslandsangebote auf die Seite
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
                    + '</div><div class="col-md-4">';
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
            $('#angebotLinkUp').before(htmlText);
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

// Setzt den back to top Pfeil auf invisible wenn man sich ganz oben auf der Seite befindet
$(document).scroll(function() { 
	   if($(window).scrollTop() === 0) {
	     $(".chevronup").css('opacity', '0');
	     $(".chevronup").css('cursor', 'initial');
	   }else{
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