var imgCount = 1;
var back = 1;
var angeboteNavBar = '<div class="navBar"><p class="navel current" id="l1">Allgemeine Infos</p><p class="navel" id="l2">FAQs</p><p class="navel" id="l3">Erfahrungsbericht</p><p class="navel" id="l4">Bilder</p><p class="navel" id="l5">Bewerben</p></div>';
var main = function() {
	if($.urlParam('logout').trim()){
		sessionStorage.clear();
		window.location.href = removeQueryStringParameter('logout');
	}

	$('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
	loadPortalInfo();
	loop();
	// Überprüfen ob jemand eingeloggt ist
	if (isEmpty(sessionStorage['User']) === true
			|| sessionStorage['User'] === 'undefined') {
		$('.logoutFenster').hide();
	} else {
		$('.loginFenster').hide();
		$('.logFenster').hide();
		$('.loginButton').hide();
		$('.regButton').hide();
		$('.portalInfo').hide();
		$('.logoFenster').show();
		$('.logoutFenster').show();
		$('.nutzerName').text(sessionStorage['User']);
		if (sessionStorage['rolle'] === "2") {
			$('.cms').show();
			$('.nonCms').show();
			$('.Admin').hide();
			$('.Mitarbeiter').show();
			$('.portalInfo').show();
		} else if (sessionStorage['rolle'] === "1") {
			if (sessionStorage['verwaltung'] === "0"
					|| sessionStorage['verwaltung'] === undefined) {
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
				$('#nutzerVerwaltung').show();
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

	for (var i = 1; i < 10; i++) {
		$('.angCont' + i).hide();
		$('.c' + i + '1').show();
	}
	// Registrierenlink wurde angeklickt, PopUpFeld erscheint
	$('.linkReg').on('click', function() {
		$('.popUpBack').show();
		$('.popUpFeld').show();
	});
	// Schließen Kreuz im PopUpFeld wurde geklickt, PopUp wird geschlossen
	$('#close').on('click', function() {
		$('.popUpBack').hide();
		$('.popUpFeld').hide();
	});
	// Anzeigen der Auslandsangebote des Studienganges
	$('.studiengang').on('click', function() {
		$('.uni').hide();
		$(this).parent().children('.uni').toggle();
	});
	// Anzeigen des Login Fensters
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
	// Auswahl der Rolle im RegistrierenPopUp
	$('.auswahl').on('click', function() {
		if ($('.rolleWahl').val() === "Studierender") {
			$('.auslandsmitarbeiter').hide();
			$('.student').show();
			$('#askAdmin').hide();
		} else if ($('.rolleWahl').val() === "Auslandsmitarbeiter") {
			$('.auslandsmitarbeiter').hide();
			$('.student').hide();
			$('#askAdmin').show();
		}
	});
	// Click-Listener fuer Registrieren-Button
	$('#regForm').submit(function() {

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
							swal($('#inTel').val());
							telefon = $('#inTel').val();
							mobil = $('#inMobil').val();
						}
						if (pw1 === pw2) {
							var vorname = $('#inVorname').val();
							var nachname = $('#inNachname').val();
							var email = $('#inMail').val();
							/*if (rolle === 'Auslandsmitarbeiter'
									&& email.match('dhbw-karlsruhe.de') != 'dhbw-karlsruhe.de') {
								$('.falsch')
										.html(
												unescape("Bitte wählen Sie nur Auslandsmitarbeiter, wenn Sie einer sind.")); */


							if (studiengang === "Studiengang*") {
								/*$('.falsch')
										.html(
												unescape("Bitte wähle deinen Studiengang aus."));*/
								swal({
									  title: "Studiengang auswählen",
									  text: "Bitte einen Studiengang aus der Liste auswählen",
									  type: "error",
									  confirmButtonText: "OK"
									});
							} else if (vorname != "" && nachname != "" && email != "" && matrikelnummer != "" && kurs != "" && pw1 != "") {
								$
										.ajax({
											type : "POST",
											url : "register",
											data : {
												rolle : rolle,
												passwort : pw1,
												vorname : vorname,
												nachname : nachname,
												email : email,
												matrikelnummer : matrikelnummer,
												studiengang : studiengang,
												kurs : kurs,
												tel : telefon,
												mobil : mobil,
												standort : standort,
											},
											success : function(result) {
												if (result == "mailError"){
													swal({
														  title: "Mailadresse belegt",
														  text: "Die eingetragene Mailadresse wird bereits von einem Account verwendet",
														  type: "error",
														  confirmButtonText: "OK"
														});
												} else if (result == "matnrError"){
													swal({
														  title: "Matrikelnummer belegt",
														  text: "Die eingetragene Matrikelnummer wird bereits von einem Account verwendet",
														  type: "error",
														  confirmButtonText: "OK"
														});
												} else {
												/*$('.erfolgreich')
														.html(
																'Registrierung erfolgreich. <br> Bitte logge dich ein um fortzufahren. <br> Dein Nutzername lautet '
																		+ email);
												$('.erfolgreich').show();
												$('.erfolgreich')
														.fadeOut(10000);*/
													swal({
														  title: "Registrierung erfolgreich",
														  text: "Bitte bestätigen deine Mailadresse (" + email + "), damit du dich im Portal einloggen kannst",
														  type: "success",
														  confirmButtonText: "OK",
														  timer: 8000
														});
												$('.popUpBack').hide();
												$('.popUpFeld').fadeOut();

												//$('#register').fadeOut();
												//$('#register').hide();
												$('.modal').fadeOut();
												$('.modal').modal('hide');
												}

											},
											error : function(result) {
												swal({
													  title: "Fehler!",
													  text: "Bei der Registrierung ist ein Fehler aufgetreten",
													  type: "error",
													  confirmButtonText: "OK"
													});
											}
										});
							}
						}
	}

	);

	//Check ob Passwörter übereinstimmen
	$('#inPwSt2').keyup(function(){
		var inPwSt1 = document.getElementById("inPwSt1")
		  , inPwSt2 = document.getElementById("inPwSt2");

		if(inPwSt1.value != inPwSt2.value) {
			inPwSt2.setCustomValidity("Die Passwörter stimmen nicht überein");
		} else {
			inPwSt2.setCustomValidity('');
		}

	});

	//Kein Reload der Modal-Boxen
	$("form").submit(function () {
		return false;
	});

	// Click-Listener fuer Login-Button
	$('#btnLogin').on(
			'click',
			function() {
				var email = $('#inEmail').val();
				var pw = $('#inPasswort').val();
				$.ajax({
					type : "POST",
					url : "login",
					data : {
						email : email,
						pw : pw,
					},
					success : function(data) {
						var auslesen = data.split(';');
						if (auslesen[0] == "2"){
							//Nutzername oder Passwort falsch"
							swal({
								  title: "Fehler!",
								  text: "Nutzername oder Passwort falsch",
								  type: "error",
								  confirmButtonText: "Erneut versuchen"
								});
						} else if (auslesen[0] == "3"){
							swal({
								  title: "Mailadresse bestätigen",
								  text: "Dieser Nutzer ist nicht aktiviert. Bitte bestätige zuerst deine Mailadresse",
								  type: "error",
								  confirmButtonText: "OK"
								});
						} else if (auslesen[0] == "4"){
							swal({
								  title: "Serverfehler",
								  text: "Bei der Serververbindung ist ein Fehler aufgetreten. Bitte versuche es später erneut",
								  type: "error",
								  confirmButtonText: "OK"
								});
						} else {
						sessionStorage['rolle'] = auslesen[3].trim();
						sessionStorage['matrikelnr'] = auslesen[2].trim();
						sessionStorage['studiengang'] = auslesen[1].trim();
						sessionStorage['User'] = email;

						if (sessionStorage['rolle'] === '2') {
							/*$('.cms').show();
							$('.nonCms').show();
							$('.logFenster').hide();
							$('.logoutFenster').show();
							$('.weg').css('display', 'inline');
							$('.nutzerName').html(email);
							location.reload();*/
							window.location.href = 'task_overview.html'
						} else if (sessionStorage['rolle'] === '3') {
							location.href = 'index.html';
							$('.nutzerName').html(email);
							$('.cms').hide();
							$('.nonCms').show();
							$('.weg').css('display', 'inline');
							$('.logFenster').hide();
							$('.logoutFenster').show();
							window.location.reload();

						} else if (sessionStorage['rolle'] === '1') {
							$('#normalBereich').hide();
							$('#adminBereich').show();
							$('.nutzerName').html(email);
							$('.logFenster').hide();
							$('.logoutFenster').show();
							window.location.reload();

						} else if (sessionStorage['rolle'] === "") {
							$('#inName').css('color', 'red');
							$('#inPasswort').css('color', 'red');
							$('#falschLogin').show();
							window.location.reload();
						}
						$('.logoFenster').show();
						}

					},
					error : function(data) {
						swal("Fehler");
					}
				});
			});
	$('#logout').on('click', function() {
		window.location.href = "logout";

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
	// -------------------------------------------------------- CMS
	// ------------------------------------------------------------------------------
	// Click-Listener für CMS bearbeiten Buttons
	$('.cmsBtn')
			.on(
					'click',
					function() {
						if ($(this).text() === "Bearbeiten"
								|| $(this).text() === "Abbrechen") {
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
														'<input class="inBox" id="inli'
																+ i
																+ '" value="'
																+ $('#li' + i)
																		.text()
																+ '" /><img class="small" id="delPoLi'
																+ i
																+ '" src="images/Button Delete.png" />');
										$('#delPoLi' + i).click(function(event){
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
								swal('Dieser Bereich kann noch nicht bearbeitet werden.');
								/*
								 * if ($(this).text() === "Bearbeiten") {
								 * $('.kurzbericht').show();
								 * $('.erfahrungsBerichte').children().children().children('.nonCms').hide();
								 * $('.erfahrungsBerichte').children().children().children().children('.nonCms').hide();
								 * for (var i = 1; $('#kb' + i).html() !=
								 * undefined; i++) { $('#kb' +
								 * i).children().children('textarea').text($('#kb' +
								 * i).children().children('.middle').text().trim()); }
								 * $('.cmsBerichte').show();
								 * $(this).text('Abbrechen'); } else if
								 * ($(this).text() === "Abbrechen"){
								 * $('#erfahrungsBerichte').children().children().children('.nonCms').show();
								 * $('.erfahrungsBerichte').children().children().children().children('.nonCms').show();
								 * $('.cmsBerichte').hide();
								 * $('.kurzbericht').hide(); $('.zeig').show();
								 * $(this).text('Bearbeiten'); }
								 */
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
										htmlText = htmlText
												+ '<input class="inBox" id="inInfoLi'
												+ i
												+ '" value="'
												+ $('#infoli' + i)
														.children('a').text()
												+ '" /><img class="small" id="delInLi'
												+ i
												+ '" src="images/Button Delete.png" /><br>'
												+ '<b style="margin-right: 5px">Link</b><input class="inBox infoLink" id="inInfoLink'
												+ i
												+ '" value="'
												+ $('#infoli' + i)
														.children('a').attr(
																'href')
												+ '"><img class="small" id="delLink'
												+ i
												+ '" src="images/Button Delete.png" />';
									}
									$('.inputsInfo').html(htmlText);
									for (var i = 1; $('#infoli' + i).children(
											'a').text() != ""; i++) {
										document
												.getElementById('delInLi' + i)
												.addEventListener(
														'click',
														function(event) {
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
														function(event) {
															var id = $(this)
																	.attr('id');
															id = id.substring(
																	7, 8);
															$(
																	'#inInfoLink'
																			+ id)
																	.val('');
														});
									}
									for (var i = 1; $('#infoli' + i).children(
											'a').text() != ""; i++) {
										document.getElementById('inInfoLi' + i)
												.addEventListener('click',
														function(event) {
														});
									}
									for (var i = 1; $('#infoli' + i).children(
											'a').text() != ""; i++) {
										document.getElementById(
												'inInfoLink' + i)
												.addEventListener('click',
														function(event) {
														});
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
											type : "POST",
											url : "login_db",
											data : {
												action : "post_portalInfo",
												titel : titel,
												listelement1 : listelement1,
												listelement2 : listelement2,
												listelement3 : listelement3,
												listelement4 : listelement4,
												listelement5 : listelement5,
												listelement6 : listelement6,
												listelement7 : listelement7,
											},
											success : function(result) {
												$('.erfolgreich')
														.html(
																"Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
												$('.erfolgreich').show();
												$('.erfolgreich').fadeOut(7000);
											},
											error : function(result) {
												swal('Fehler');
											}
										});
								break;
							case ('cmsAngebote'):
								if ($(this).parent().attr('id') === "angebotNeu") {
									if ($('#inStudiengangNeuAngebot').val() != "Studiengang wählen*"
											&& isEmpty($('#inNeuAngebot').val()) != true
											&& isEmpty($('#inNeuAngebotInfo')
													.val()) != true
											&& isEmpty($('#inMaps1').val()) != true
											&& isEmpty($('#inMaps2').val()) != true
											&& isEmpty($('#inMaps3').val()) != true) {
										var faqs = '';
										for (var i = 1; isEmpty($(
												'#inNeuFaqFr' + i).val()) != true; i++) {
											faqs = faqs
													+ '<b>'
													+ $('#inNeuFaqFr' + i)
															.val()
													+ '</b><br>'
													+ $('#inNeuFaqAn' + i)
															.val() + '<br>';
										}
										var erfahrungsbericht = $(
												'#inNeuAngebotErfahrungsbericht')
												.val()
												+ '<br><i>'
												+ $('#inNeuAngebotErfAutor')
														.val() + '</i>';
										var maps = 'https://www.google.com/maps/embed/v1/place?key=AIzaSyA76FO67-tqO0XWItx9KtXDgcta9mYHjrM&q='
												+ $('#inMaps1').val()
												+ ','
												+ $('#inMaps2').val().replace(
														' ', '')
												+ '+'
												+ $('#inMaps3').val().replace(
														' ', '');
										$
												.ajax({
													type : "POST",
													url : "login_db",
													data : {
														action : "post_newAuslandsangebot",
														studiengang : $(
																'#inStudiengangNeuAngebot')
																.val(),
														uniTitel : $(
																'#inNeuAngebot')
																.val(),
														allgemeineInfos : $(
																'#inNeuAngebotInfo')
																.val(),
														faq : faqs,
														erfahrungsbericht : erfahrungsbericht,
														maps : maps,
													},
													success : function(event) {
														$('.erfolgreich')
																.html(
																		"Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
														$('.erfolgreich')
																.show();
														$('.erfolgreich')
																.fadeOut(7000);
													},
													error : function(event) {

													}
												});
									} else {
										swal({
											  title: "Fehlende Angaben",
											  text: "Bitte tragen Sie in alle mit * gekennzeichneten Feldern etwas ein",
											  type: "error",
											  confirmButtonText: "OK"
											});

									}
								} else if ($(this).parent().attr('id') === 'angebotEdit') {
									if (isEmpty($('#EditAngebot').text().trim()) != true
											&& isEmpty($('#inEditAngebotInfo')
													.val()) != true
											&& isEmpty($('#inEditMaps1').val()) != true
											&& isEmpty($('#inEditMaps2').val()) != true
											&& isEmpty($('#inEditMaps3').val()) != true) {
										var faqs = '';
										for (var i = 1; isEmpty($(
												'#inEditFaqFr' + i).val()) != true; i++) {
											faqs = faqs
													+ '<b>'
													+ $('#inEditFaqFr' + i)
															.val()
													+ '</b><br>'
													+ $('#inEditFaqAn' + i)
															.val() + '<br>';
										}
										var erfahrungsbericht = $(
												'#inEditAngebotErfahrungsbericht')
												.val()
												+ '<br><i>'
												+ $('#inEditAngebotErfAutor')
														.val() + '</i>';
										var maps = 'https://www.google.com/maps/embed/v1/place?key=AIzaSyA76FO67-tqO0XWItx9KtXDgcta9mYHjrM&q='
												+ $('#inEditMaps1').val()
												+ ','
												+ $('#inEditMaps2').val()
														.replace(' ', '')
												+ '+'
												+ $('#inEditMaps3').val()
														.replace(' ', '');
										$
												.ajax({
													type : "POST",
													url : "login_db",
													data : {
														action : "post_editAuslandsangebot",
														studiengang : $(
																'#inStudiengangEditAngebot')
																.val(),
														uniTitel : $(
																'#EditAngebot')
																.text().trim(),
														allgemeineInfos : $(
																'#inEditAngebotInfo')
																.val(),
														faq : faqs,
														erfahrungsbericht : erfahrungsbericht,
														maps : maps,
													},
													success : function(event) {
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
										swal({
											  title: "Fehlende Angaben",
											  text: "Bitte tragen Sie in alle mit * gekennzeichneten Feldern etwas ein",
											  type: "error",
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
											type : "POST",
											url : "login_db",
											data : {
												action : "post_infoMaterial",
												titel : $('#inInfoTitel').val(),
												listelement1 : $('#inInfoLi1')
														.val(),
												link1 : $('#inInfoLink1').val(),
												listelement2 : $('#inInfoLi2')
														.val(),
												link2 : $('#inInfoLink2').val(),
												listelement3 : $('#inInfoLi3')
														.val(),
												link3 : $('#inInfoLink3').val(),
												listelement4 : $('#inInfoLi4')
														.val(),
												link4 : $('#inInfoLink4').val(),
												listelement5 : $('#inInfoLi5')
														.val(),
												link5 : $('#inInfoLink5').val(),
												listelement6 : $('#inInfoLi6')
														.val(),
												link6 : $('#inInfoLink6').val(),
												listelement7 : $('#inInfoLi7')
														.val(),
												link7 : $('#inInfoLink7').val(),
											},
											success : function(event) {
												$('.erfolgreich')
														.html(
																"Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
												$('.erfolgreich').show();
												$('.erfolgreich').fadeOut(7000);
											},
											error : function(event) {

											}
										});
								break;
							}
						}
					});

	function delPoLi(event){

			var id = event.target.id;
			id = id.substring(
					7, 8);
			var el = $('#inli' + id);
			if(el.val() == "" || el.val() == undefined){
				el.remove();
				$('#delPoLi' + id).remove();

			}
			else{
				el.val('');
			}


	}
	// Click Listener für die Bild ändern Buttons
	$('.upload').on('click', function() {
		var id = $(this).attr('id');
		id = id.trim();
		loop1(id);
	});
	// Click-Listener für neues Infoselement in Box PortalInfo
	$('#cmsBtnInfoNeu').on(
			'click',
			function() {
				var count = $('.inputsPortal .inBox').length;
				if(count > 6){
					swal('Warnung', 'Zum aktuellen Zeitpunkt können nicht mehr als 7 Infoelemente hinzugefügt werden', 'warning');
				}else{
					$('.inputsPortal').append('<input class="inBox" id="inli' + (count + 1)
									+ '" value="" /><img class="small" id="delPoLi'
									+ (count + 1)
									+ '" src="images/Button Delete.png" />');
									$('#delPoLi' + (count+1)).click(function(event){
										delPoLi(event);
									});
				}
			});
	// Click-Listener für neuen Erfahrungsbericht in Box Erfahrungsbericht
	$('#cmsBtnBerichtNeu')
			.on(
					'click',
					function() {
						var count = 0;
						for (var i = 1; $('#kb' + i).html() !== undefined; i++) {
							count = i;
						}
						$('.berichte')
								.append(
										'<div class="row kurzbericht" id="kb'
												+ (count + 1)
												+ '"><div class="col-md-6"><img src="images/noimg.jpg" id="imgWorld" /><label class="btn cmsBtn cmsBerichte btnBild" id="btnberichtBild'
												+ (count + 1)
												+ '" style="float: right">Bild ändern<input class="upload" type="file" id="berichtBild'
												+ (count + 1)
												+ '" /></label></div><div class="col-md-6 cmsBerichte"><textarea class="inBox" style="width: 100%; height: 250px"></textarea></div>');
						$('.kurzbericht').show();
						$('.cmsBerichte').show();
					});
	// Click-Listener für neuen Link in Box InfoMaterial
	$('#cmsBtnLinkNeu')
			.on(
					'click',
					function() {
						var count = 0;
						for (var j = 1; isEmpty($('#infoli' + j).children('a')
								.text()) != true; j++) {
							count = j;
						}
						$('.inputsInfo')
								.html(
										$('.inputsInfo').html()
												+ '<input class="inBox" id="inInfoLi'
												+ (count + 1)
												+ '" value="" /><img class="small" id="delInLi'
												+ (count + 1)
												+ '" src="images/Button Delete.png" /><br><b style="margin-right: 5px">Link</b><input class="inBox infoLink" id="inInfoLink'
												+ (count + 1)
												+ '" value=""><img class="small" id="delLink'
												+ (count + 1)
												+ '" src="images/Button Delete.png" />');
						$('#cmsBtnLinkNeu').hide();
					});
	// Click-Listener für alle Images mit der class small
	$('.small').on('click', function() {
		var id = $(this).attr('id');
		if (id.substring(0, 7) === "delInLi") {
			id = id.substring(7, 8);
			$('#inInfoLi' + id).val('');
		} else if (id.substring(0, 7) === "delLink") {
			id = id.substring(7, 8);
			$('#inInfoLink' + id).val('');
		}
	});
	// Click Listener für die Tiles im AdminBereich
	$('.tile').on('click', function() {
		var id = $(this).attr('id');
		if (id === 'verwaltungIndex') {
			$('#normalBereich').show();
			$('.cms').show();
			$('#adminBereich').hide();
			$('.Admin').show();
			$('.Mitarbeiter').hide();
			sessionStorage['verwaltung'] = 1;
		} else if (id === 'verwaltungUser') {
			$('#adminBereich').hide();
			$('#nutzerVerwaltung').show();
                        
                        var AAACreateModal = $('#AAACreate');
                        
                        if(AAACreateModal.length == 0){
                           $('#login').after('<div id="AAACreate" class="modal fade" role="dialog"> <div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> <button type="button" class="close" data-dismiss="modal">×</button> <h4 class="modal-title">Neuen Auslandsmitarbeiter anlegen</h4> </div><form id="AAACreateForm" action=""> <div class="modal-body"> <div class="form-group"> <div class="row"> <div class="col-md-6"> <input type="text" placeholder="Vorname*" class="inBox form-control" id="AAAVorname" required=""> </div><div class="col-md-6"> <input type="text" placeholder="Nachname*" class="inBox form-control" id="AAANachname" required=""> </div></div></div><input type="email" placeholder="E-Mail Adresse*" class="inBox form-control" id="AAAMail" required="" title="E-Mail"><br><input type="number" placeholder="Mitarbeiternummer*" class="inBox form-control" id="AAAID" required="" title="Mitarbeiternummer"> <br><select class="inBox form-control" id="AAAStandort" required=""> <option value="">DHBW Standort wählen*</option> <option value="DHBW_Karlsruhe">DHBW Karlsruhe</option> <option value="DHBW_Stuttgart">DHBW Stuttgart</option> <option value="DHBW_Mannheim">DHBW Mannheim</option> <option value="DHBW_Mosbach">DHBW Mosbach</option> <option value="DHBW_Lörrach">DHBW Lörrach</option> <option value="DHBW_Ravensburg">DHBW Ravensburg</option> <option value="DHBW_Heidenheim">DHBW Heidenheim</option> <option value="DHBW_Villingen-Schwenningen">DHBW Villingen-Schwenningen</option> </select> <br></div><div class="modal-footer"> <input form="AAACreateForm" type="submit" class="btn" id="btnAAACreate" value="Anlegen" style="width: auto;"> </div></form> </div></div></div>');
                           AAACreateModal = $('#AAACreate');
                           $('#userNeu').attr("data-toggle", "modal").attr("href", "#AAACreate"); 
                           $('#AAACreateForm').submit(function( event ){
                                var email = $('#AAAMail').val();
                                var vname = $('#AAAVorname').val();
                                var nname = $('#AAANachname').val();
                                var standort = $('#AAAStandort').val();
                                var aaaid = $('#AAAID').val();
                                $.ajax({
                                    type : "POST",
                                    url : "createAAA",
                                    data : {
                                            email : email,
                                            vorname: vname,
                                            nachname: nname,
                                            standort: standort,
                                            aaaid: aaaid
                                    },
                                    success : function(data) {
                                            if (data == "mailError"){
                                                swal({
                                                    title: "Fehler!",
                                                    text: "Ein Account mit dieser Mail existiert bereits! Bitte benutzen Sie eine andere.",
                                                    type: "error",
                                                    confirmButtonText: "OK"
                                                });
                                            
                                            } 
                                            else if (data == "No account registered for this email adress"){
                                                swal({
                                                    title: "Fehler!",
                                                    text: "Es ist ein Fehler beim Erstellen des Accounts aufgetreten. Versuchen Sie es später erneut.",
                                                    type: "error",
                                                    confirmButtonText: "OK"
                                                });
                                            }
                                            else if (data == "registerError"){
                                                swal({
                                                    title: "Fehler!",
                                                    text: "Es ist ein Fehler beim Erstellen des Accounts aufgetreten. Versuchen Sie es später erneut.",
                                                    type: "error",
                                                    confirmButtonText: "OK"
                                                });
                                            }
                                            else {
                                                swal({
                                                    title: "Account erfolgreich erstellt",
                                                    text: "An die Mailadresse wurde ein Link zum setzen des Passwords geschickt.",
                                                    type: "success",
                                                    confirmButtonText: "OK"
                                                });
                                                
                                            }
                                        }
                                  
                                    });
                                          
                                event.preventDefault();
                            });

                         }
                        
                        
                        
                        
			sessionStorage['verwaltung'] = 2;
		} else if (id === 'verwaltungPortal') {
			location.href = 'choose-diagram.html';
		}
	});
	// Click-Listener für Link zurück zum Admin-Hauptmenü
	$('.backAdmin').on('click', function() {
		$('#nutzerVerwaltung').hide();
		$('#adminBereich').show();
		$('#normalBereich').hide();
		$('.Admin').hide();
		sessionStorage['verwaltung'] = 0;
	});
	// Click-Listener für Userverwaltung User anzeigen lassen
	$('.btnUser')
			.on(
					'click',
					function() {
						var id = $(this).attr('id');
						if (id === 'userNeu') {
							/*$('.popUpBack').show();
							$('.popUpFeld').show();
							$('.rolleWahl').val('Auslandsmitarbeiter').hide();
							$('.auswahl').hide();*/
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
							$
									.ajax({
										type : "POST",
										url : "login_db",
										data : {
											action : "get_User",
											rolle : rolle,
										},
										success : function(result) {
											var auslesen = result.split(';');
											var count = 0;
											var even = 'odd';
											if (rolle === 2) {
												var tabelle = '<h2>Registrierte '
														+ typ
														+ '</h2><table id="userTabelle" <thead><tr class="titleRow"><td>Vorname</td><td>Nachname</td><td>Email</td><td>Telefonnummer</td><td>Mobilfunknummer</td><td></td></tr></thead>';
												for (var i = 0; i < (auslesen.length - 1); i = i + 9) {
													auslesen[i] = auslesen[i]
															.trim();
													count++;
													tabelle = tabelle
															+ '<tr class="'
															+ even
															+ '" id="row'
															+ count
															+ '"><td class="vorname">'
															+ auslesen[i + 1]
															+ '</td><td class="nachname">'
															+ auslesen[i]
															+ '</td><td class="email">'
															+ auslesen[i + 2]
															+ '</td><td class=telnummer">'
															+ auslesen[i + 3]
															+ '</td><td class="mobil">'
															+ auslesen[i + 4]
															+ '</td><td class="btn" id="edit'
															+ count
															+ '">Bearbeiten</td></tr>';
													if (even === 'even') {
														even = 'odd';
													} else {
														even = 'even';
													}
												}
											} else if (rolle === 3) {
												var tabelle = '<h2>Registrierte '
														+ typ
														+ '</h2><table id="userTabelle" <thead><tr class="titleRow"><td>Vorname</td><td>Nachname</td><td>Email</td><td>DHBW Standort</td><td>Studiengang</td><td>Kurs</td><td>Matrikelnummer</td><td></td><td></td></tr></thead>';
												for (var i = 0; i < (auslesen.length - 1); i = i + 9) {
													auslesen[i] = auslesen[i]
															.trim();
													for (var j = 0; j < 8; j++) {
														if (auslesen[i + j] === "undefined"
																|| auslesen[i
																		+ j] === "null") {
															auslesen[i + j] = '';
														}
													}
													count++;
													tabelle = tabelle
															+ '<tr class="'
															+ even
															+ '" id="row'
															+ count
															+ '"><td class="vorname">'
															+ auslesen[i + 1]
															+ '</td><td class="nachname">'
															+ auslesen[i]
															+ '</td><td class="email">'
															+ auslesen[i + 2]
															+ '</td><td>'
															+ auslesen[i + 8]
															+ '<td class="studgang">'
															+ auslesen[i + 5]
															+ '</td><td class="kurs">'
															+ auslesen[i + 6]
															+ '</td><td class="matrikelnr">'
															+ auslesen[i + 7]
															+ '</td><td class="btn" id="edit'
															+ count
															+ '">Bearbeiten</td><td class="glyphicon glyphicon-trash delete-button" data-matrikel="'+ auslesen[i + 7].trim() +'" id="delete'
                            + count
                            + '">Löschen</td></tr>';
													if (even === 'even') {
														even = 'odd';
													} else {
														even = 'even';
													}
												}
											}
											tabelle = tabelle + '</table>';
											$('#userTabelle').html(tabelle);
											for (var i = 1; isEmpty($(
													'#edit' + i).text()) !== true; i++) {

												$(document).on('click', '.delete-button', function () {

                          var self = $(this);

                          swal({
                              title: "Bist du sicher?",
                              text: "Der User kann nicht wiederhergestellt werden!",
                              type: "warning",
                              showCancelButton: true,
                              confirmButtonColor: "#DD6B55",
                              confirmButtonText: "Löschen!",
                              closeOnConfirm: false
                            },
                            function(){
                              $.ajax({
                                type : "GET",
                                url : "user/delete",
                                data : {
                                  matrikelnummer: self.data('matrikel')
                                },
                                success : function(result) {
                                  self.closest('tr').remove();
                                  swal('Gelöscht!', 'Der User wurde erfolgreich gelöscht.', 'success');
                                },
                                error : function(result) {
                                  swal('Fehler', 'Der User konnte nicht gelöscht werden', 'error');
                                }
                              });
                            });
                        });

												document
														.getElementById(
																'edit' + i)
														.addEventListener(
																'click',
																function(event) {
																	var id = $(
																			this)
																			.attr(
																					'id');
																	var laenge = id.length;
																	if (laenge === 5) {
																		id = id
																				.substring(
																						4,
																						5);
																	} else {
																		id = id
																				.substring(
																						4,
																						6);
																	}
																	$(
																			'#inEditVorname')
																			.val(
																					$(
																							'#row'
																									+ id)
																							.children(
																									'.vorname')
																							.text());
																	$(
																			'#inEditNachname')
																			.val(
																					$(
																							'#row'
																									+ id)
																							.children(
																									'.nachname')
																							.text());
																	$(
																			'#inEditEmail')
																			.val(
																					$(
																							'#row'
																									+ id)
																							.children(
																									'.email')
																							.text());
																	if (rolle === 2) {
																		$(
																				'#inEditTel')
																				.val(
																						$(
																								'#row'
																										+ id)
																								.children(
																										'.telnummer')
																								.text());
																		$(
																				'#inEditMobil')
																				.val(
																						$(
																								'#row'
																										+ id)
																								.children(
																										'.mobil')
																								.text());
																		$(
																				'#inEditTel')
																				.show();
																		$(
																				'#inEditMobil')
																				.show();
																		$(
																				'#inEditStudgang')
																				.hide();
																		$(
																				'#inEditKurs')
																				.hide();
																		$(
																				'#inEditMatnr')
																				.hide();
																	}
																	$(
																			'.nutzerBearbeiten')
																			.show();
																});
											}
										},
										error : function(result) {
											swal({
												  title: "Serverfehler",
												  text: "Die Serververbindung wurde unterbrochen. Bitte laden Sie die Seite erneut",
												  type: "error",
												  confirmButtonText: "OK"
												});
										}
									});
						}
					});
	// Click-Listener f�r Schlie�enbild im Bearbeiten PopUp
	$('#closeBearb').on('click', function() {
		$('.nutzerBearbeiten').hide();
		$('.popUpBack').hide();
	});
	// Click-Listener f�r �nderungen speichern Button im Nutzer Bearbeiten PopUp
	$('#btnUserEditSave').on('click', function() {
		swal("Hier passiert noch nichts.");
	});
	// Auswahlm�glichkeit auf der Startseite zur Sortierung der Angebote
	$('#selStudiengang').on('click', function(event) {
		if ($('#selStudiengang').val() != "Alle Angebote") {
			$('.angebote').hide();
			$('.' + $('#selStudiengang').val()).show();
		} else {
			$('.angebote').show();
		}
	});
	// Click-Listener f�r CMS Buttons im Bereich Auslandsangebote
	$('.btnAngebote')
			.on(
					'click',
					function(event) {
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
													function(event) {
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
								options = options
										+ '<option>'
										+ $('#angebot' + i).children()
												.children('.uniName').text()
										+ '</option>';
							}
							$('#selEditAngebot').html(options);
							for (var i = 1; i <= 1; i++) {
								for (var j = 1; j <= 4; j++) {
									document
											.getElementById('nEdit' + i + j)
											.addEventListener(
													'click',
													function(event) {
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
	$('#btnSaveNewStudiengang')
			.on(
					'click',
					function(event) {
						if (isEmpty($('#inNewStudiengang').val()) != true) {
							$
									.ajax({
										type : "POST",
										url : "login_db",
										data : {
											action : "post_newStudiengang",
											studiengang : $('#inNewStudiengang')
													.val(),
										},
										success : function(result) {
											$('.erfolgreich')
													.html(
															"Ihre Änderungen wurden erfolgreich in die Datenbank gespeichert. <br> Bitte laden sie die Seite erneut.");
											$('.erfolgreich').show();
											$('.erfolgreich').fadeOut(7000);
										},
										error : function(result) {

										}
									});
						} else {
							swal("Sie dürfen das Feld nicht leer lassen.");
						}
					});
	// Click-Listener für ein Angebot bearbeiten
	$('#selEditAngebot')
			.on(
					'click',
					function(event) {
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
};

$(document).ready(main);

function loop() {
	setTimeout(function() {
		if (back === 1) {
			$('.imgSlider').fadeTo(
					'slow',
					0,
					function() {
						$('.imgSlider').css('background-image',
								'url(images/pan' + back + '.jpg)');
					}).fadeTo('800', 0.9);
			back = 2;
			loop();
		} else if (back === 2) {
			$('.imgSlider').fadeTo(
					'slow',
					0,
					function() {
						$('.imgSlider').css('background-image',
								'url(images/pan' + back + '.jpg)');
					}).fadeTo('800', 0.9);
			back = 3;
			loop();
		} else {
			$('.imgSlider').fadeTo(
					'slow',
					0,
					function() {
						$('.imgSlider').css('background-image',
								'url(images/pan' + back + '.jpg)');
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
			text = text.replace('ändern', 'Gewählt');
			$('#btn' + id).html(text);
			$('#btn' + id).css('background', 'rgb(48, 192, 192)');
		}

	}, 1000);
}

function isEmpty(str) {
	return (!str || 0 === str.length);
}
$.urlParam = function(name) {
	var results = new RegExp('[\?&]' + name + '=([^&#]*)')
			.exec(window.location.href);

	if (results == null) {
		return " ";
	} else {
		return results[1] || 0;
	}

};
// Laden der Daten der PortalInfo Box
function loadPortalInfo() {
	var link = "";
	if ($.urlParam('confirm') != null && $.urlParam('confirm').trim() != '') {
		link = $.urlParam('confirm');
		swal({
			  title: "Nutzeraccount bestätigt",
			  text: "Ihre Mailadresse wurde bestätigt. Sie können sich jetzt einloggen",
			  type: "success",
			  confirmButtonText: "OK"
			});
	}
	$
			.ajax({
				type : "POST",
				url : "login_db",
				data : {
					action : "get_portalInfo",
					confirm : link
				},
				success : function(result) {
					var auslesen = result.split(';');
					for (var i = 0; i < auslesen.length; i++) {
						auslesen[i] = auslesen[i].trim();
						if (i === 0) {
							$('#portalInfo').children().children('.titel')
									.text(auslesen[i]);
						} else if (auslesen[i].match('Werb') === 'Werb') {
							$('#portalInfo').children().children('.red').text(
									auslesen[i]);
						} else if (auslesen[i] != "" && auslesen[i] != "null") {
							$('#portalInfo').children().children('.funktionen')
									.append(
											'<li id="li' + i + '">'
													+ auslesen[i] + ' </li>');
						}
					}
					loadAuslandsangebote();
				},
				error : function(result) {
					swal({
						  title: "Serverfehler",
						  text: "Die Serververbindung wurde unterbrochen. Bitte laden Sie die Seite erneut",
						  type: "error",
						  confirmButtonText: "OK"
						});
				}
			});
}

// L�dt die Daten zu den Auslandsangeboten aus der Datenbank
function loadAuslandsangebote() {
	var angeboteInhalt = '<option>Alle Angebote</option>';
	$.ajax({
		type : "POST",
		url : "login_db",
		data : {
			action : "get_Auslandsangebote",
		},
		success : function(result) {
			var auslesen = result.split(';');
			for (var i = 0; i < auslesen.length - 1; i++) {
				auslesen[i] = auslesen[i].trim();
				angeboteInhalt = angeboteInhalt + '<option>' + auslesen[i]
						+ '</option>';
			}
			$('#selStudiengang').html(angeboteInhalt);
			loadAuslandsangeboteInhalt();
		},
		error : function(result) {

		}

	});
}

// L�d die Auslandsangebote auf die Seite
function loadAuslandsangeboteInhalt() {
	$
			.ajax({
				type : "POST",
				url : "login_db",
				data : {
					action : "get_angeboteDaten",
				},
				success : function(result) {
					var zaehler = 1;
					var htmlText = '';
					var auslesen = result.split(';');
					for (var i = 0; i < auslesen.length; i++) {
						auslesen[i] = auslesen[i].trim();
						if (i = (6 * zaehler)) {
							if (isEmpty(auslesen[(6 * zaehler) - 1]) === true) {
								auslesen[(6 * zaehler) - 1] = 'Keine Bilder vorhanden.';
							}
							if (isEmpty(auslesen[(6 * zaehler) - 2]) === true
									|| auslesen[(6 * zaehler) - 2] === 'Erfahrungsbericht') {
								auslesen[(6 * zaehler) - 2] = "Keine Erfahrungsberichte vorhanden.";
							}

							htmlText = htmlText
									+ '<div class="nonCms angebote '
									+ auslesen[(6 * zaehler) - 6]
									+ '" id="angebot'
									+ zaehler
									+ '" style="margin-right: 5px;"><div class="col-md-12">'
									+ '<h3 class="uniName">'
									+ auslesen[(6 * zaehler) - 5]
									+ '</h2>'
									+ '<div class="navBarAng">'
									+ '<div class="navelAng active" id="n'
									+ zaehler
									+ '1">Infos</div>'
									+ '<div class="navelAng" id="n'
									+ zaehler
									+ '2">FAQs</div>'
									+ '<div class="navelAng" id="n'
									+ zaehler
									+ '3">Erfahrungsbericht</div>'
									+ '<div class="navelAng" id="n'
									+ zaehler
									+ '4">Bilder</div>'
									+ '</div>'
									+ '<div class="contentAng active" id="c'
									+ zaehler
									+ '1"><div class="row"><div class="col-md-7">'
									+ auslesen[(6 * zaehler) - 4]
									+ '</div><div class="col-md-4">';
							if (isEmpty(auslesen[(6 * zaehler) - 1].trim()) != true) {
								htmlText = htmlText
										+ '<iframe width="400" height="200" src="'
										+ auslesen[(6 * zaehler) - 1]
										+ '" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>';
							} else {
								htmlText = htmlText
										+ '<p>Keine Kartendaten gefunden.</p>';
							}
							htmlText = htmlText + '</div></div></div>'
									+ '<div class="contentAng" id="c' + zaehler
									+ '2">'
									+ auslesen[(6 * zaehler) - 3].trim()
									+ '</div>'
									+ '<div class="contentAng" id="c' + zaehler
									+ '3">'
									+ auslesen[(6 * zaehler) - 2].trim()
									+ '</div>'
									+ '<div class="contentAng" id="c' + zaehler
									+ '4">Keine Bilder vorhanden.</div>'
									+ '</div>' + '</div>';
							zaehler++;
						}

					}
					$('#angebotLinkUp').before(htmlText);
					for (var i = 1; i < zaehler; i++) {
						for (var j = 1; j <= 4; j++) {
							document
									.getElementById('n' + i + j)
									.addEventListener(
											'click',
											function(event) {
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
					loadInfoMaterial();
				},
				error : function(result) {

				}
			});
}
//Function to remove certain URL Parameters
function removeQueryStringParameter(key, url) {
    if (!url) url = window.location.href;

    var hashParts = url.split('#');

    var regex = new RegExp("([?&])" + key + "=.*?(&|#|$)", "i");

    if (hashParts[0].match(regex)) {
        //REMOVE KEY AND VALUE
        url = hashParts[0].replace(regex, '$1');

        //REMOVE TRAILING ? OR &
        url = url.replace(/([?&])$/, '');

        //ADD HASH
        if (typeof hashParts[1] !== 'undefined' && hashParts[1] !== null)
            url += '#' + hashParts[1];
    }

    return url;
}

// L�d die Daten f�r die Infomaterialien auf die Seite
function loadInfoMaterial() {
	$.ajax({
		type : "POST",
		url : "login_db",
		data : {
			action : "get_infoMaterial",
		},
		success : function(result) {
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
							$('#infoli' + zaehler).children('a').text(
									auslesen[i]);
						} else {
							$('#infoli' + zaehler).children('a').attr('href',
									auslesen[i]);
							zaehler++;
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
		},
		error : function(result) {

		}
	});
}
