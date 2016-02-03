var imgCount = 1;
var back = 1;
var angeboteNavBar = '<div class="navBar"><p class="navel current" id="l1">Allgemeine Infos</p><p class="navel" id="l2">FAQs</p><p class="navel" id="l3">Erfahrungsbericht</p><p class="navel" id="l4">Bilder</p><p class="navel" id="l5">Bewerben</p></div>';
var main = function() {
	$('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
	loadPortalInfo();
	loop();
	// Überprüfen ob jemand eingeloggt ist
	if (isEmpty(sessionStorage['User']) === true  || sessionStorage['User'] === 'undefined'){
	} else {
		$('.loginFenster').hide();
		$('.logoutFenster').show();
		$('.nutzerName').text(sessionStorage['User']);
		if (sessionStorage['rolle'] === "2") {
			$('.cms').show();
			$('.nonCms').show();
		} else if (sessionStorage['rolle'] === "1") {
			if (sessionStorage['verwaltung'] === "0" || sessionStorage['verwaltung'] === undefined) {
				$('#normalBereich').hide();
				$('#adminBereich').show();
			} else if (sessionStorage['verwaltung'] === "1") {
				$('#normalBereich').show();
				$('.cms').show();
				$('#adminBereich').hide();
				$('.Admin').show();
			} else if (sessionStorage['verwaltung'] === "2") {
				$('#adminBereich').hide();
				$('#nutzerVerwaltung').show();
				$('#normalBereich').hide();
			}
			
		} else {
			$('.cms').hide();
			$('.nonCms').show();
			$('.eingeloggt').css('display', 'inline');
		} 
	}
	
	for (var i = 1; i < 10; i++) {
		$('.angCont' + i).hide();
		$('.c' + i + '1').show();
	}
	//Registrierenlink wurde angeklickt, PopUpFeld erscheint
	$('.linkReg').on('click', function() {
		$('.popUpBack').show();
		$('.popUpFeld').show();
	});
	//Schließen Kreuz im PopUpFeld wurde geklickt, PopUp wird geschlossen
	$('#close').on('click', function() {
		$('.popUpBack').hide();
		$('.popUpFeld').hide();
	});
	//Anzeigen der Auslandsangebote des Studienganges
	$('.studiengang').on('click', function() {
		$('.uni').hide();
		$(this).parent().children('.uni').toggle();
	});
	//Anzeigen des Login Fensters
	$('.loginShow').on('click', function() {
		$('.loginFenster').css('margin-top', '-5px');
		$('.popUpFeld').hide();
		$('.popUpBack').hide();
	});
	$('#loginClose').on('click', function() {
		$('#falschLogin').hide();
		$('#inName').css('color', 'black');
		$('#inPasswort').css('color', 'black');
		$('#inName').val('');
		$('#inPasswort').val('');
		$('.loginFenster').css('margin-top', '-190px');
	});
	$('.navel').on('click', function() {
		var id = $(this).attr('id');
		var idB = id.substring(0, 1);
		var idE = id.substring(1, 2);
		for (var i = 1; i <= 5; i++) {
			$('#' + idB + i).removeClass('current');
		}
		$('.angCont' + idB).hide();
		
		$(this).addClass('current');
		$('.c' + id).show();
	});
	//Auswahl der Rolle im RegistrierenPopUp
	$('.auswahl').on('click', function() {
		if ($('.rolleWahl').val() === "Studierender") {
			$('.auslandsmitarbeiter').hide();
			$('.student').show();
			$('#askAdmin').hide();
		} else if ($('.rolleWahl').val() === "Unternehmen") {
			$('.auslandsmitarbeiter').hide();
			$('.student').hide();
			$('#askAdmin').hide();
			alert('hier fehlt noch was!');
			$('#askAdmin').hide();
		} else if ($('.rolleWahl').val() === "Auslandsmitarbeiter") {
			$('.auslandsmitarbeiter').hide();
			$('.student').hide();
			$('#askAdmin').show();
		}
	});
	//Click-Listener für Registrieren-Button
	$('.btnReg').on('click', function() {
		var telefon, mobil, matrikelnummer, studiengang, kurs, pw1, pw2, standort;
		telefon = '';
		mobil = '';
		matrikelnummer = '';
		studiengang = '';
		kurs = '';
		pw1 = '';
		pw2 = '';
		standort = '';
		var rolle = $('.rolleWahl').val();
		if (rolle === "Studierender") {
			pw1 = $('#inPwSt1').val();
			pw2 = $('#inPwSt2').val();
			matrikelnummer = $('#inMatrikel').val();
			studiengang = $('#inStudiengang').val();
			kurs = $('#inKurs').val();
			standort = $('#inStandort').val();
		} else if (rolle === "Auslandsmitarbeiter") {
			pw1 = $('#inPwAu1').val();
			pw2 = $('#inPwAu2').val();
			alert($('#inTel').val());
			telefon = $('#inTel').val();
			mobil = $('#inMobil').val();
		}
		if (pw1 != pw2) {
			$('.falsch').html("Die Passwörter stimmen nicht überein.");
			$('.falsch').show();
		} else {
			var vorname = $('#inVorname').val();
			var nachname = $('#inNachname').val();
			var email = $('#inMail').val();
			if (rolle === 'Auslandsmitarbeiter' && email.match('dhbw-karlsruhe.de') !=  'dhbw-karlsruhe.de') {
				$('.falsch').html("Bitte wählen sie nur Auslandsmitarbeiter, wenn sie einer sind.");
			} else if (studiengang === "Studiengang*") {
				$('.falsch').html("Bitte wählen Deinen Studiengang aus.");
			} else {
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "post_register",
						rolle: rolle,
						passwort: pw1,
						vorname: vorname,
						nachname: nachname,
						email: email,
						matrikelnummer: matrikelnummer,
						studiengang: studiengang,
						kurs: kurs,
						tel: telefon,
						mobil: mobil,
						standort: standort,
					},
					success: function(result) {
						$('.erfolgreich').html('Registrierung erfolgreich. <br> Bitte logge dich ein um fortzufahren. <br> Dein Nutzername lautet ' + nachname + '.' + vorname);
						$('.erfolgreich').show();
						$('.erfolgreich').fadeOut(10000);
						$('.popUpBack').hide();
						$('.popUpFeld').fadeOut();
					}, 
					error: function(result) {
						
					}
				});
			}
		}
		
	});
	//Click-Listener für Login-Button
	$('#btnLogin').on('click', function() {
		var user = $('#inName').val();
		var pw = $('#inPasswort').val();
		var name = user.split('.');
		$.ajax({
			type: "POST",
			url: "login_db",
			data: {
				action: "post_login",
				vorname: name[1],
				nachname: name[0],
				pw: pw,
			},
			success: function(data) {
				var auslesen = data.split(';');
				sessionStorage['rolle'] = auslesen[0];
				sessionStorage['matrikelnr'] = auslesen[1].trim();
				sessionStorage['studiengang'] = auslesen[2].trim();
				sessionStorage['User'] = name[1];
				if (sessionStorage['rolle'] === '2') {
					$('.cms').show();
					$('.nonCms').show();
					$('.loginFenster').hide();
					$('.logoutFenster').show();
					$('.weg').css('display', 'inline');
					$('.nutzerName').html(name[1]);
				} else if (sessionStorage['rolle'] === '3') {
					location.replace('bewerbungsportal.html');
					$('.nutzerName').html(name[1]);
					$('.cms').hide();
					$('.nonCms').show();
					$('.weg').css('display', 'inline');
				} else if (sessionStorage['rolle'] === '1') {
					$('#normalBereich').hide();
					$('#adminBereich').show();
					$('.nutzerName').html(name[1]);
					$('.loginFenster').hide();
					$('.logoutFenster').show();
				} else if (sessionStorage['rolle'] === "") {
					$('#inName').css('color', 'red');
					$('#inPasswort').css('color', 'red');
					$('#falschLogin').show();
				}
			}, error: function(data) {
				alert("Fehler");
			}
		});			
	});
	$('#logout').on('click', function() {
		sessionStorage.clear();
		//Herausfinden auf welcher Seite ich mich gerade befinde
		var title = $(document).find("title").text();
		if (title === "DHBW Auslandsinfo") {
			
		} else if (title === "DHBW Auslandsportal") {
			location.replace("index.html");
		}
		sessionStorage.clear();
		location.replace("index.html");
		
		/*for (var i = 1; i <= 10; i++) {
			$('#'+ i + '5').addClass("hidden");
		}*/
	});
	$('.arrow').on('click', function() {
		var id = $(this).attr('id');
		if (id === "arrLeft") {
			id = $('.zeig').attr('id');
			id = id.substring(2, 3);
			if (id === '1') {
				for (var i = 1; $('#kb' + i).text() != ""; i++) {
					id = i;
				}
			} else {
				id = id - 1;
			}
		} else if (id === "arrRight") {
			id = $('.zeig').attr('id');
			id = id.substring(2, 3);
			var count = 0;
			for (var i = 1; $('#kb' + i).text() != ""; i++) {
				count = i;
			}
			id = parseInt(id);
			if (id === count) {
				id = 1;
			} else {
				id = id + 1;
			}
		}
		$('.kurzbericht').removeClass('zeig');
		$('#kb' + id).addClass('zeig');
	});
// -------------------------------------------------------- CMS ------------------------------------------------------------------------------
	//Click-Listener für CMS bearbeiten Buttons
	$('.cmsBtn').on('click', function() {
		if ($(this).text() === "Bearbeiten" || $(this).text() === "Abbrechen") {
			var id = $(this).parent().parent().attr('id');
			switch(id) {
			case 'portalInfo': 
				if ($(this).text() === "Bearbeiten") {
					$('#portalInfo').children().children('.nonCms').hide();
					$('.cmsPortal').show();
					$('#portalInfo').children('.col-md-6').children('.titel').hide();
					var titel = $('#portalInfo').children('.col-md-6').children('.titel').text();
					$('#inPortalTitel').val(titel);
					for (var i = 1; $('#li' + i).text() != ""; i++) {
						$('.inputsPortal').append('<input class="inBox" id="inli' + i +'" value="' + $('#li' + i).text() + '" /><img class="small" id="delPoLi' + i + '" src="images/Button Delete.png" />');
						document.getElementById('delPoLi' + i).addEventListener('click', function(event) {
							id = event.target.id;
							id = id.substring(7, 8);
							$('#inli' + id).val('');
						});
					}
					$(this).text('Abbrechen');
				} else if ($(this).text() === "Abbrechen"){
					$('#portalInfo').children().children('.nonCms').show();
					$('.cmsPortal').hide();
					$('#portalInfo').children('.col-md-6').children('.titel').show();
					$(this).text('Bearbeiten');
				} 
				break;
			case 'auslandsAngebote':
				if ($(this).text() === "Bearbeiten") {
					$('.cmsAngebote').show();
					$('#auslandsAngebote').children('.nonCms').hide();
					$(this).text('Abbrechen');
				} else if ($(this).text() === "Abbrechen"){
					$('.cmsAngebote').hide();
					$('#auslandsAngebote').children('.nonCms').show();
					$('#cmsAddStudgang').hide();
					$('#cmsAddAngebot').hide();
					$('#cmsEditAngebot').hide();
					$(this).text('Bearbeiten');
				}
				break;
			case 'erfahrungsBerichte':
				alert('Dieser Bereich kann noch nicht bearbeitet werden.');
				/*if ($(this).text() === "Bearbeiten") {
					$('.kurzbericht').show();
					$('.erfahrungsBerichte').children().children().children('.nonCms').hide();
					$('.erfahrungsBerichte').children().children().children().children('.nonCms').hide();
					for (var i = 1; $('#kb' + i).html() != undefined; i++) {
						$('#kb' + i).children().children('textarea').text($('#kb' + i).children().children('.middle').text().trim());
					}
					$('.cmsBerichte').show();
					$(this).text('Abbrechen');
				} else if ($(this).text() === "Abbrechen"){
					$('#erfahrungsBerichte').children().children().children('.nonCms').show();
					$('.erfahrungsBerichte').children().children().children().children('.nonCms').show();
					$('.cmsBerichte').hide();
					$('.kurzbericht').hide();
					$('.zeig').show();
					$(this).text('Bearbeiten');
				} */
				break;
			case 'infoMaterial':
				if ($(this).text() === "Bearbeiten") {
					$('#infoMaterial').children().children('.nonCms').hide();
					$('.cmsInfo').show();
					var titel = $('#infoMaterial').children('.col-md-6').children('.titel').text();
					$('#inInfoTitel').val(titel);
					var htmlText = '';
					for (var i = 1; $('#infoli' + i).children('a').text() != ""; i++) {
						$('#inInfoLi' + i).val($('#infoli' + i).children('a').text());
						$('#inInfoLink' + i).val($('#infoli' + i).children('a').attr('href'));
						htmlText = htmlText + '<input class="inBox" id="inInfoLi' + i + '" value="' + $('#infoli' + i).children('a').text() + '" /><img class="small" id="delInLi' + i + '" src="images/Button Delete.png" /><br>'
  						+'<b style="margin-right: 5px">Link</b><input class="inBox infoLink" id="inInfoLink' + i + '" value="' + $('#infoli' + i).children('a').attr('href') + '"><img class="small" id="delLink' + i + '" src="images/Button Delete.png" />';
					}
					$('.inputsInfo').html(htmlText);
					for (var i = 1; $('#infoli' + i).children('a').text() != ""; i++) {
						document.getElementById('delInLi' + i).addEventListener('click', function(event) {
							var id = $(this).attr('id');
							id = id.substring (7, 8);
							$('#inInfoLi' + id).val('');
						});
					}
					for (var i = 1; $('#infoli' + i).children('a').text() != ""; i++) {
						document.getElementById('delLink' + i).addEventListener('click', function(event) {
							var id = $(this).attr('id');
							id = id.substring(7 ,8);
							$('#inInfoLink' + id).val('');
						});
					}
					for (var i = 1; $('#infoli' + i).children('a').text() != ""; i++) {
						document.getElementById('inInfoLi' + i).addEventListener('click', function(event) {
						});
					}
					for (var i = 1; $('#infoli' + i).children('a').text() != ""; i++) {
						document.getElementById('inInfoLink' + i).addEventListener('click', function(event) {
						});
					}
					$(this).text('Abbrechen');
				} else if ($(this).text() === "Abbrechen") {
					$('#infoMaterial').children().children('.nonCms').show();
					$('.cmsInfo').hide();
					$(this).text('Bearbeiten');
				}
				break;
			}
		} else if ($(this).text() === "Änderungen speichern") {
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
				$.ajax ({
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
					}, success: function(result) {
						$('.erfolgreich').html("Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
						$('.erfolgreich').show();
						$('.erfolgreich').fadeOut(7000);
					}, error: function(result) {
						alert('Fehler');
					}
				});
				break;
			case ('cmsAngebote'):
				if ($(this).parent().attr('id') === "angebotNeu") {
					if ($('#inStudiengangNeuAngebot').val() != "Studiengang wählen*" && isEmpty($('#inNeuAngebot').val()) != true && isEmpty($('#inNeuAngebotInfo').val()) != true && isEmpty($('#inMaps1').val()) != true && isEmpty($('#inMaps2').val()) != true && isEmpty($('#inMaps3').val()) != true) {
						var faqs = '';
						for (var i = 1; isEmpty($('#inNeuFaqFr' + i).val()) != true; i++) {
							faqs = faqs + '<b>' + $('#inNeuFaqFr' + i).val() + '</b><br>' + $('#inNeuFaqAn' + i).val() + '<br>';
						}
						var erfahrungsbericht = $('#inNeuAngebotErfahrungsbericht').val() + '<br><i>' + $('#inNeuAngebotErfAutor').val() + '</i>';
						var maps = 'https://www.google.com/maps/embed/v1/place?key=AIzaSyA76FO67-tqO0XWItx9KtXDgcta9mYHjrM&q=' + $('#inMaps1').val() + ',' + $('#inMaps2').val().replace(' ', '') + '+' + $('#inMaps3').val().replace(' ', '');
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "post_newAuslandsangebot",
								studiengang: $('#inStudiengangNeuAngebot').val(),
								uniTitel: $('#inNeuAngebot').val(),
								allgemeineInfos: $('#inNeuAngebotInfo').val(),
								faq: faqs,
								erfahrungsbericht: erfahrungsbericht,
								maps: maps,
							}, success: function(event) {
								$('.erfolgreich').html("Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
								$('.erfolgreich').show();
								$('.erfolgreich').fadeOut(7000);
							}, error: function(event) {
								
							}
						});
					} else {
						alert("Bitte tragen sie in alle mit * gekennzeichneten Feldern etwas ein.");
					}
				} else if ($(this).parent().attr('id') === 'angebotEdit') {
					if (isEmpty($('#EditAngebot').text().trim()) != true && isEmpty($('#inEditAngebotInfo').val()) != true && isEmpty($('#inEditMaps1').val()) != true && isEmpty($('#inEditMaps2').val()) != true && isEmpty($('#inEditMaps3').val()) != true) {
						var faqs = '';
						for (var i = 1; isEmpty($('#inEditFaqFr' + i).val()) != true; i++) {
							faqs = faqs + '<b>' + $('#inEditFaqFr' + i).val() + '</b><br>' + $('#inEditFaqAn' + i).val() + '<br>';
						}
						var erfahrungsbericht = $('#inEditAngebotErfahrungsbericht').val() + '<br><i>' + $('#inEditAngebotErfAutor').val() + '</i>';
						var maps = 'https://www.google.com/maps/embed/v1/place?key=AIzaSyA76FO67-tqO0XWItx9KtXDgcta9mYHjrM&q=' + $('#inEditMaps1').val() + ',' + $('#inEditMaps2').val().replace(' ', '') + '+' + $('#inEditMaps3').val().replace(' ', '');
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "post_editAuslandsangebot",
								studiengang: $('#inStudiengangEditAngebot').val(),
								uniTitel: $('#EditAngebot').text().trim(),
								allgemeineInfos: $('#inEditAngebotInfo').val(),
								faq: faqs,
								erfahrungsbericht: erfahrungsbericht,
								maps: maps,
							}, success: function(event) {
								$('.erfolgreich').html("Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
								$('.erfolgreich').show();
								$('.erfolgreich').fadeOut(7000);
							}
						});
					} else {
						alert("Bitte tragen sie in alle mit * gekennzeichneten Feldern etwas ein.");
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
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "post_infoMaterial",
						titel: $('#inInfoTitel').val(),
						listelement1: $('#inInfoLi1').val(),
						link1: $('#inInfoLink1').val(),
						listelement2: $('#inInfoLi2').val(),
						link2: $('#inInfoLink2').val(),
						listelement3: $('#inInfoLi3').val(),
						link3: $('#inInfoLink3').val(),
						listelement4: $('#inInfoLi4').val(),
						link4: $('#inInfoLink4').val(),
						listelement5: $('#inInfoLi5').val(),
						link5: $('#inInfoLink5').val(),
						listelement6: $('#inInfoLi6').val(),
						link6: $('#inInfoLink6').val(),
						listelement7: $('#inInfoLi7').val(),
						link7: $('#inInfoLink7').val(),
					}, success: function(event) {
						$('.erfolgreich').html("Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
						$('.erfolgreich').show();
						$('.erfolgreich').fadeOut(7000);
					}, error: function(event) {
						
					}
				});
				break;
			}
		}
	});
	//Click Listener für die Bild ändern Buttons
	$('.upload').on('click', function() {
		var id = $(this).attr('id');
		id = id.trim();
		loop1(id);
	});
	//Click-Listener für neues Infoselement in Box PortalInfo
	$('#cmsBtnInfoNeu').on('click', function() {
		var count = 0;
		for (var i = 1; $('#li' + i).text() != ""; i++) {
			count = i;
		}
		$('.inputsPortal').html($('.inputsPortal').html() + '<input class="inBox" id="inli' + (count+1) + '" value="" /><img class="small" id="delLi' + (count+1) + '" src="images/Button Delete.png" />');
		for (var i = 1; $('#li' + i).text() != ""; i++) {
			$('#inli' + i).val($('#li' + i).text());
		}
	});
	//Click-Listener für neuen Erfahrungsbericht in Box Erfahrungsbericht
	$('#cmsBtnBerichtNeu').on('click', function() {
		var count = 0;
		for (var i= 1; $('#kb' + i).html() !== undefined; i++) {
			count = i;
		}
		$('.berichte').append('<div class="row kurzbericht" id="kb' + (count+1) + '"><div class="col-md-6"><img src="images/noimg.jpg" id="imgWorld" /><label class="btn cmsBtn cmsBerichte btnBild" id="btnberichtBild' + (count+1) + '" style="float: right">Bild Ändern<input class="upload" type="file" id="berichtBild' + (count+1) + '" /></label></div><div class="col-md-6 cmsBerichte"><textarea class="inBox" style="width: 100%; height: 250px"></textarea></div>');
		$('.kurzbericht').show();
		$('.cmsBerichte').show();
	});
	//Click-Listener für neuen Link in Box InfoMaterial
	$('#cmsBtnLinkNeu').on('click', function() {
		var count = 0;
		for (var j = 1; isEmpty($('#infoli' + j).children('a').text()) != true; j++) {
			count = j;
		}
		$('.inputsInfo').html($('.inputsInfo').html() + '<input class="inBox" id="inInfoLi' + (count+1) + '" value="" /><img class="small" id="delInLi' + (count+1) + '" src="images/Button Delete.png" /><br><b style="margin-right: 5px">Link</b><input class="inBox infoLink" id="inInfoLink' + (count+1) +'" value=""><img class="small" id="delLink' + (count+1) +'" src="images/Button Delete.png" />');
		$('#cmsBtnLinkNeu').hide();
	});
	//Click-Listener für alle Images mit der class small
	$('.small').on('click', function() {
		var id = $(this).attr('id');
		if (id.substring(0, 7) === "delInLi") {
			id = id.substring (7, 8);
			$('#inInfoLi' + id).val('');
		} else if (id.substring(0, 7) === "delLink") {
			id = id.substring(7 ,8);
			$('#inInfoLink' + id).val('');
		}
	});
	//Click Listener für die Tiles im AdminBereich
	$('.tile').on('click', function() {
		var id = $(this).attr('id');
		if (id === 'verwaltungIndex') {
			$('#normalBereich').show();
			$('.cms').show();
			$('#adminBereich').hide();
			$('.Admin').show();
			sessionStorage['verwaltung'] = 1;
		} else if (id === 'verwaltungUser') {
			$('#adminBereich').hide();
			$('#nutzerVerwaltung').show();
			sessionStorage['verwaltung'] = 2;
		} else if (id === 'verwaltungPortal') {
			sessionStorage['verwaltung'] === 3;
		}
	});
	//Click-Listener für Link zurück zum Admin-Hauptmenü
	$('.backAdmin').on('click', function() {
		$('#nutzerVerwaltung').hide();
		$('#adminBereich').show();
		$('#normalBereich').hide();
		$('.Admin').hide();
		sessionStorage['verwaltung'] = 0;
	});
	//Click-Listener für Userverwaltung User anzeigen lassen
	$('.btnUser').on('click', function() {
		var id = $(this).attr('id');
		if (id === 'userNeu') {
			$('.popUpBack').show();
			$('.popUpFeld').show();
			$('.rolleWahl').val('Auslandsmitarbeiter').hide();
			$('.auswahl').hide();
			$('.auslandsmitarbeiter').show();
			
		} else {
			var rolle = 0;
			var typ = '';
			if (id === 'userStudShow') {
				rolle = 3;
				typ = "Studierende";
			} else if (id === 'userMaShow') {
				rolle = 2;
				typ = "Auslandsmitarbeiter";
			}
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "get_User",
					rolle: rolle,
				}, success: function(result) {
					var auslesen = result.split(';');
					var count = 0;
					var even = 'odd';
					if (rolle === 2) {
						var tabelle = '<h2>Registrierte ' + typ + '</h2><table id="userTabelle" <thead><tr class="titleRow"><td>Vorname</td><td>Nachname</td><td>Email</td><td>Telefonnummer</td><td>Mobilfunknummer</td><td></td></tr></thead>';
						for (var i = 0; i < (auslesen.length - 1); i = i + 9) {
							auslesen[i] = auslesen[i].trim();
							count++;
							tabelle = tabelle + '<tr class="' + even + '" id="row' + count + '"><td class="vorname">' + auslesen[i + 1] + '</td><td class="nachname">' + auslesen[i] + '</td><td class="email">' + auslesen[i + 2] + '</td><td class=telnummer">' + auslesen[i + 3] + '</td><td class="mobil">' + auslesen[i + 4] + '</td><td class="btn" id="edit' + count + '">Bearbeiten</td></tr>';
							if (even === 'even') {
								even = 'odd';
							} else {
								even = 'even';
							}
						}
					} else if (rolle === 3) {
						var tabelle = '<h2>Registrierte ' + typ + '</h2><table id="userTabelle" <thead><tr class="titleRow"><td>Vorname</td><td>Nachname</td><td>Email</td><td>DHBW Standort</td><td>Studiengang</td><td>Kurs</td><td>Matrikelnummer</td><td></td></tr></thead>';
						for (var i = 0; i < (auslesen.length - 1); i = i + 9) {
							auslesen[i] = auslesen[i].trim();
							for (var j = 0; j < 8; j++) {
								if (auslesen[i + j] === "undefined" || auslesen[i + j] === "null") {
									auslesen[i + j] = '';
								}
							}
							count++;
							tabelle = tabelle + '<tr class="' + even + '" id="row' + count + '"><td class="vorname">' + auslesen[i + 1] + '</td><td class="nachname">' + auslesen[i] + '</td><td class="email">' + auslesen[i + 2] + '</td><td>' + auslesen[i + 8] + '<td class="studgang">' + auslesen[i + 5] + '</td><td class="kurs">' + auslesen[i + 6] + '</td><td class="matrikelnr">' + auslesen[i + 7] + '</td><td class="btn" id="edit' + count + '">Bearbeiten</td></tr>';
							if (even === 'even') {
								even = 'odd';
							} else {
								even = 'even';
							}
						}
					}
					tabelle = tabelle + '</table>';
					$('#userTabelle').html(tabelle);
					for (var i = 1; isEmpty($('#edit' + i).text()) !== true; i++) {
						document.getElementById('edit' + i).addEventListener('click', function(event) {
							var id = $(this).attr('id');
							var laenge = id.length;
							if (laenge === 5) {
								id = id.substring(4, 5);
							} else {
								id = id.substring(4, 6);
							}
							$('#inEditVorname').val($('#row' + id).children('.vorname').text());
							$('#inEditNachname').val($('#row' + id).children('.nachname').text());
							$('#inEditEmail').val($('#row' + id).children('.email').text());
							if (rolle === 2) {
								$('#inEditTel').val($('#row' + id).children('.telnummer').text());
								$('#inEditMobil').val($('#row' + id).children('.mobil').text());
								$('#inEditTel').show();
								$('#inEditMobil').show();
								$('#inEditStudgang').hide();
								$('#inEditKurs').hide();
								$('#inEditMatnr').hide();
							}
							$('.nutzerBearbeiten').show();
						});
					}
				}, error: function(result) {
					alert("Die Verbindung zur DB wurde unterbrochen. Bitte laden Sie die Seite erneut.");
				}
			});
		}
	});
	//Click-Listener für Schließenbild im Bearbeiten PopUp
	$('#closeBearb').on('click', function() {
		$('.nutzerBearbeiten').hide();
		$('.popUpBack').hide();
	});
	//Click-Listener für Änderungen speichern Button im Nutzer Bearbeiten PopUp
	$('#btnUserEditSave').on('click', function() {
		alert("Hier passiert noch nichts.");
	});
	//Auswahlmöglichkeit auf der Startseite zur Sortierung der Angebote
	$('#selStudiengang').on('click', function(event) {
		if ($('#selStudiengang').val() != "Alle Angebote") {
			$('.angebote').hide();
			$('.' + $('#selStudiengang').val()).show();
		} else {
			$('.angebote').show();
		}
	});
	//Click-Listener für CMS Buttons im Bereich Auslandsangebote
	$('.btnAngebote').on('click', function(event) {
		if ($(this).attr('id') === "btnAddStudgang") {
			$('#cmsAddStudgang').show();
			$('#cmsAddAngebot').hide();
			$('#cmsEditAngebot').hide();
		} else if ($(this).attr('id') === "btnAddAngebot") {
			$('#cmsAddStudgang').hide();
			$('#cmsAddAngebot').show();
			$('#cmsEditAngebot').hide();
			$('#inStudiengangNeuAngebot').html($('#selStudiengang').html().replace("Alle Angebote", "Studiengang wählen*"));
			for (var i = 1; i <= 1; i++) {
				for (var j = 1; j <= 4; j++) {
					document.getElementById('nNeu' + i + j).addEventListener('click', function(event) {
						var id = $(this).parent().parent().parent().attr('id');
						$('#' + id).children().children().children('.navelAng').removeClass('active');
						$(this).addClass('active');
						$('#' + id).children().children('.contentAng').removeClass('active');
						id = $(this).attr('id').replace('nNeu', '');
						$('#cNeu' + id).addClass('active');
					});
				}
			}
		} else if ($(this).attr('id') === "btnEditAngebot") {
			$('#cmsAddStudgang').hide();
			$('#cmsAddAngebot').hide();
			$('#cmsEditAngebot').show();
			var options = '<option>Angebot wählen</option>';
			for (var i = 1; isEmpty($('#angebot' + i).children().children('.uniName').text()) != true; i++) {
				options = options + '<option>' + $('#angebot' + i).children().children('.uniName').text() + '</option>';
			}
			$('#selEditAngebot').html(options);
			for (var i = 1; i <= 1; i++) {
				for (var j = 1; j <= 4; j++) {
					document.getElementById('nEdit' + i + j).addEventListener('click', function(event) {
						var id = $(this).parent().parent().parent().attr('id');
						$('#' + id).children().children().children('.navelAng').removeClass('active');
						$(this).addClass('active');
						$('#' + id).children().children('.contentAng').removeClass('active');
						id = $(this).attr('id').replace('nEdit', '');
						$('#cEdit' + id).addClass('active');
					});
				}
			}
		}
	});
	//Click Listener für neuen Studiengang anlegen
	$('#btnSaveNewStudiengang').on('click', function(event) {
		if (isEmpty($('#inNewStudiengang').val()) != true) {
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "post_newStudiengang",
					studiengang: $('#inNewStudiengang').val(),
				}, success: function(result) {
					$('.erfolgreich').html("Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
					$('.erfolgreich').show();
					$('.erfolgreich').fadeOut(7000);
				}, error: function(result) {
					
				}
			});
		} else {
			alert("Sie dürfen das Feld nicht leer lassen.");
		}
	});
	//Click-Listener für ein Angebot bearbeiten
	$('#selEditAngebot').on('click', function(event) {
		for (var i = 1; isEmpty($('#angebot' + i)) != true; i++) {
			if ($('#angebot' + i).children().children('.uniName').text().trim() === $(this).val()) {
				$('#EditAngebot').text($('#angebot' + i).children().children('.uniName').text());
				$('#inEditAngebotInfo').val($('#angebot' + i).children().children('#c' + i + '1').children('.pull-left').text().trim());
				if (isEmpty($('#angebot' + i).children().children('#c' + i +  '1').children('.pull-right').children().attr('src')) != true) {
					var maps = $('#angebot' + i).children().children('#c' + i +  '1').children('.pull-right').children().attr('src').split('q=');
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
				//Erfahrungsbericht in input Felder eintragen
				if (isEmpty($('#angebot' + i).children().children('#c' + i +  '3').html()) != true) {
					var erfahrungsbericht = $('#angebot' + i).children().children('#c' + i +  '3').html().split('<br>');
					$('#inEditAngebotErfahrungsbericht').val(erfahrungsbericht[0]);
					erfahrungsbericht[1] = erfahrungsbericht[1].replace('<i>', '');
					erfahrungsbericht[1] = erfahrungsbericht[1].replace('</i>', '');
					$('#inEditAngebotErfAutor').val(erfahrungsbericht[1]);
					erfahrungsbericht = [];
				} else {
					$('#inEditAngebotErfahrungsbericht').val('');
					$('#inEditAngebotErfAutor').val('');
				}
				//FAQs in InputFelder übernehmen
				if (isEmpty($('#angebot' + i).children().children('#c' + i +  '2').html()) != true) {
					var faqs = $('#angebot' + i).children().children('#c' + i +  '2').html().replace(/<b>/g, ';');
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
							if ((k + 1) % 2 === 1){
								$('#inEditFaqFr' + frage).val(hilfe[k].replace('?/', '?'));
							} else {
								$('#inEditFaqAn' + frage).val(hilfe[k]);
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
};

$(document).ready(main);

function loop() {
    setTimeout(function() {
    	if(back === 1) {
    		$('.imgSlider').fadeTo('slow', 0, function() {
    			$('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
    		}).fadeTo('800', 0.9);	
    		back = 2;
    		loop();
    	} else if (back === 2){
    		$('.imgSlider').fadeTo('slow', 0, function() {
    			$('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
    		}).fadeTo('800', 0.9);	
    		back = 3;
    		loop();
    	}  else {
    		$('.imgSlider').fadeTo('slow', 0, function() {
    			$('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
    		}).fadeTo('800', 0.9);	
    		back = 1;
    		loop();
    	}
    }, 6000);
}

function loop1(id) {
	setTimeout(function() {
		if ($('#' + id).val() === "") {
			loop1(id);
		} else {
			var text = $('#btn' + id).html();
			text = text.replace('Ändern', 'Gewählt');
			$('#btn' + id).html(text);
			$('#btn' + id).css('background', 'rgb(48, 192, 192)');
		}
		
	}, 1000);
}

function isEmpty(str) {
	return (!str || 0 === str.length);
}

//Laden der Daten der PortalInfo Box
function loadPortalInfo() {
	$.ajax({
		type: "POST",
		url: "login_db",
		data: {
			action: "get_portalInfo",
		}, success: function(result) {
			var auslesen = result.split(';');
			for (var i = 0; i < auslesen.length; i++) {
				auslesen[i] = auslesen[i].trim();
				if (i === 0) {
					$('#portalInfo').children().children('.titel').text(auslesen[i]);
				} else if (auslesen[i].match('Werb') === 'Werb') {
					$('#portalInfo').children().children('.red').text(auslesen[i]);
				} else if (auslesen[i] != "" && auslesen[i] != "null") {
					$('#portalInfo').children().children('.funktionen').append('<li id="li' + i + '">' + auslesen[i] + ' </li>');
				}
			}
			loadAuslandsangebote();
		}, error: function(result) {
			alert("Die Verbindung zur DB wurde unterbrochen. Bitte laden Sie die Seite erneut.");
		}
	});
}
//Lädt die Daten zu den Auslandsangeboten aus der Datenbank
function loadAuslandsangebote() {
	var angeboteInhalt = '<option>Alle Angebote</option>';
	$.ajax({
		type: "POST",
		url: "login_db",
		data: {
			action: "get_Auslandsangebote",
		}, success: function(result) {
				var auslesen = result.split(';');
				for (var i = 0; i < auslesen.length - 1; i++) {
					auslesen[i] = auslesen[i].trim();
					angeboteInhalt = angeboteInhalt + '<option>' + auslesen[i] + '</option>';
				}
				$('#selStudiengang').html(angeboteInhalt);
				loadAuslandsangeboteInhalt();
		}, error: function(result) {
				
		}
			
	});
}

//Läd die Auslandsangebote auf die Seite
function loadAuslandsangeboteInhalt() {
	$.ajax({
		type: "POST",
		url: "login_db",
		data: {
			action: "get_angeboteDaten",
		}, success: function(result) {
			var zaehler = 1;
			var htmlText = '';
			var auslesen = result.split(';');
			for (var i = 0; i < auslesen.length; i++) {
				auslesen[i] = auslesen[i].trim();
				if (i = (6 * zaehler)) {
					if (isEmpty(auslesen[(6 * zaehler) - 1]) === true) {
						auslesen[(6 * zaehler) - 1] = 'Keine Bilder vorhanden.';
					}
					if (isEmpty(auslesen[(6 * zaehler) - 2]) === true || auslesen[(6 * zaehler) - 2] === 'Erfahrungsbericht') {
						auslesen[(6 * zaehler) - 2] = "Keine Erfahrungsberichte vorhanden.";
					}
					htmlText = htmlText + '<div class="nonCms angebote ' + auslesen[(6 * zaehler) - 6] + '" id="angebot' + zaehler + '" style="margin-right: 5px;"><div class="col-md-12">'
					+ '<h3 class="uniName">' + auslesen[(6 * zaehler) - 5] + '</h2>'
					+ '<div class="navBarAng">'
					+'<div class="navelAng active" id="n' + zaehler + '1">Infos</div>'
					+'<div class="navelAng" id="n' + zaehler + '2">FAQs</div>'
					+'<div class="navelAng" id="n' + zaehler + '3">Erfahrungsbericht</div>'
					+'<div class="navelAng" id="n' + zaehler + '4">Bilder</div>'
					+'</div>'
					+'<div class="contentAng active" id="c' + zaehler + '1"><div class="pull-left" style="width: 60%">' + auslesen[(6 * zaehler) - 4] + '</div><div class="pull-right">';
					if (isEmpty(auslesen[(6 * zaehler) - 1].trim()) != true) {
						htmlText = htmlText + '<iframe width="400" height="200" src="' + auslesen[(6 * zaehler) - 1] + '" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>';
					} else {
						htmlText = htmlText + '<p>Keine Kartendaten gefunden.</p>';
					}
					htmlText = htmlText + '</div></div>'
					+'<div class="contentAng" id="c' + zaehler + '2">' + auslesen[(6 * zaehler) - 3].trim() + '</div>'
					+'<div class="contentAng" id="c' + zaehler + '3">' + auslesen[(6 * zaehler) - 2].trim() + '</div>'
					+'<div class="contentAng" id="c' + zaehler + '4">Keine Bilder vorhanden.</div>'
					+'</div>'
					+'</div>';
					zaehler++;
				}
				
			}
			$('#angebotLinkUp').before(htmlText);
			for (var i = 1; i < zaehler; i++) {
				for (var j = 1; j <= 4; j++) {
					document.getElementById('n' + i + j).addEventListener('click', function(event) {
						var id = $(this).parent().parent().parent().attr('id');
						$('#' + id).children().children().children('.navelAng').removeClass('active');
						$(this).addClass('active');
						$('#' + id).children().children('.contentAng').removeClass('active');
						id = $(this).attr('id').replace('n', '');
						$('#c' + id).addClass('active');
					});
				}
			}
			loadInfoMaterial();
		}, error: function(result) {
			
		}
	});
}
//Läd die Daten für die Infomaterialien auf die Seite
function loadInfoMaterial() {
	$.ajax({
		type: "POST",
		url: "login_db",
		data: {
			action: "get_infoMaterial",
		}, success: function(result) {
			var auslesen = result.split(';');
			var zaehler = 1;
			var runterzaehlen = 0;
			var help = 1;
			for (var i = 0; i < auslesen.length; i++) {
				auslesen[i] = auslesen[i].trim();
				if (auslesen[i] != "null") {
					if (i === 0) {
						$('#infoMaterialTitel').text(auslesen[i]);
					} else {
						if (i % 2 === 1) {
							$('#infoli' + zaehler).children('a').text(auslesen[i]);
						} else {
							$('#infoli' +zaehler).children('a').attr('href', auslesen[i]);
							zaehler ++;
						}
					}
				} else {
					$('#infoli' + (7 - runterzaehlen)).hide();
					if (help === 2) {
						help = 1;
						runterzaehlen++;
					} else {
						help++;
					}
				}
				
			}
		}, error: function(result) {
			
		}
	});
}

