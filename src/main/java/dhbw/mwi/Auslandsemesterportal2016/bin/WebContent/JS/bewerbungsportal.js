var main = function() {
	sessionStorage['nichtBeworben'] = false;
	$('#progressbar').progressbar({
		value: 20
	});
	getStudiengaenge();
	// Überprüfen, was der User für einen Status hat
	if (isEmpty(sessionStorage['User']) === true){
		location.replace("index.html");
	} else {
		$('.loginFenster').hide();
		$('.logoutFenster').show();
		$('.nutzerName').text(sessionStorage['User']);
		if (sessionStorage['rolle'] === "2") {
			$('#in1').hide();
			$('#in5').show();
			$('.nonCms').hide();
			$('#in7').children('.inhaltBox').load('create_prozess.jsp');
			$('#in8').children('.inhaltBox').load('server_control.jsp');
		} else {
			$('.cms').hide();
			$('.nonCms').show();
			$('.inhalt').hide();
			$('.iFenster').show();
			$('.iFenster1').hide();
			$('#in1').show();
			$('#prozent').text($('#progressbar').attr('aria-valuenow') + '%');
		}
	}
	
	$('.navEl').on('click', function(event) {
		$('.bewSub').hide();
		$('.navEl').removeClass('current');
		var titel = $(this).text();
		$('.navVer').children('h2').hide();
		$(this).addClass('current');
		$('.inhalt').hide();
		$('.' + titel).show();
		if (titel === "Bewerben") {
			$('.iFenster').hide();
			$('.popUpBack').html('<img style="position: fixed; top: 50%; margin-top: -10%; width: 20%; left: 50%; margin-left: -10%" src="images/loading.gif" />');
			$('.popUpBack').show();
			setTimeout(closeLoading, 1500);
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "get_prozessStatus",
					matrikelnummer: sessionStorage['matrikelnr'],
				}, success: function(result) {
					sessionStorage['beworbeneUnis'] = '';
					var even = "odd";
					var tabelle = '';
					var status = 0;
					var zaehler = 1;
					var auslesen = result.split(';');
					for (var i = 0; i < auslesen.length; i++) {
						auslesen[i] = auslesen[i].trim();
						//Das muss noch dynamisch gemacht werden
						if (auslesen[(2 * zaehler)] === "0") {
							status = 0;
						} else if (auslesen[(3 * zaehler)] === "0") {
							status = 20;
						} else if (auslesen[(4 * zaehler)] === "0") {
							status = 40;
						} else if (auslesen[(5 * zaehler)] === "0") {
							status = 60;
						} else if (auslesen[(6 * zaehler)] === "0") {
							status = 80;
						}
						if (auslesen[(2 * zaehler)] === "1" || auslesen[(3 * zaehler)] === "1" || auslesen[(4 * zaehler)] === "1" || auslesen[(5 * zaehler)] === "1" || auslesen[(6 * zaehler)] === "1") {
							//status = status + 10;
						}
						if(i === (7 * zaehler)) {
							if (zaehler === 1) {
								tabelle = tabelle + '<tr class="' + even + '" id="row' + (zaehler) + '"><td>' + sessionStorage["studiengang"] + '</td><td id="uni' + zaehler + '">' + auslesen[(0)]  + '</td><td>' + auslesen[1] + '</td><td id="status' + zaehler + '">' + status + '%</td><td class="btn" id="btnProzessFortfahren' + zaehler + '">Fortsetzen</td>';
							} else {
								tabelle = tabelle + '<tr class="' + even + '" id="row' + (zaehler) + '"><td>' + sessionStorage["studiengang"] + '</td><td id="uni' + zaehler + '">' + auslesen[((7 * zaehler) - 7)]  + '</td><td>' + auslesen[((7 * zaehler) - 6)] + '</td><td id="status' + zaehler + '">' + status + '%</td><td class="btn" id="btnProzessFortfahren' + zaehler + '">Fortsetzen</td>';
							}
							sessionStorage['beworbeneUnis'] = sessionStorage['beworbeneUnis'] + auslesen[((7 * zaehler) - 7)] + ';';
							zaehler++; //Muss am Ende dieses if-Statements stehen!!!!
						}
						if (even === "odd") {
							even = "even";
						} else {
							even === "odd";
						}
					}
					if (isEmpty(tabelle) === true) {
						$('#tableBewProzess').hide();
						$('#nichtBeworben').show();
					} else {
						$('#tableBewProzess').show();
						$('#nichtBeworben').hide();
					}
					$('#tableBewProzessBody').html(tabelle);
					for (var i = 1; i <= zaehler; i++) {
						document.getElementById('btnProzessFortfahren' + i).addEventListener('click', function(event) {
							var id = event.target.id.replace('btnProzessFortfahren', '');
							$('.popUpBack').html('<img style="position: fixed; top: 50%; margin-top: -10%; width: 20%; left: 50%; margin-left: -10%" src="images/loading.gif" />');
							$('.popUpBack').show();
							setTimeout(closeLoading, 1000);
							//Ermittlung des Fortschritts für die weiteren Bewerbungsschritte
							var uni = $('#uni' + id).text();
							schritt1(uni);

						}); //Bitte nichts hinter diese Klammer schreiben, durch den AJAX request kommt es hin und wieder zu Fehlern, die den weiteren Prozess aber nicht beeinflussen
					} 
				}, error: function(result) {
					
				}
			});
			$('#bewProzess').show();
		} else if (titel === "Bewerber") {
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "get_bewerber",
				}, success: function(result) {
					var tabelle = '';
					var auslesen = result.split(';');
					zaehler = 1;
					for (var j = 0; j < auslesen.length; j++) {
						if (j === zaehler * 7) {
							zaehler++;
						}
					}
					zaehler = zaehler - 1;
					count = zaehler - 1;
					var even = 'odd';
					loadBewerber(auslesen, zaehler, tabelle, count, even);
					
					
				}, error: function(result) {
					
				}
			});
		}

		var id = $(this).attr('id');
		id = id.substring(3, 4);
		$('#in' + id).show();
	});
	//Click-Listener für neuen Bewerbungsprozess starten Button
	$('#newBewProzess').on('click', function() {
		var popUpHtml = '<b id="popClose"><img src="images/Button Delete.png" id="smallImg"></b><br><p>Bitte wähle die Uni aus.</p><select class="inBox" id="selectUni">';
		$.ajax({
			type: "POST",
			url: "login_db",
			data: {
				action: "get_Unis",
				studiengang: sessionStorage['studiengang'],
			}, success: function(result) {
				var auslesen = result.split(';');
				//var splitUnis = sessionStorage['beworbeneUnis'].split(';');
				var splitUni = [];
				for (var j = 0; isEmpty($('#uni' + (j + 1)).text()) != true; j++) {
					splitUni[j] = $('#uni' + (j + 1)).text();
				}
				var pruefen = 'nein';
				var index = '';
				var hilfsindex = '';
				if (isEmpty(splitUni) != true) {
					for (var i = 0; i < auslesen.length - 1; i++) {
						for (var k = 0; k < splitUni.length; k++) {
							auslesen[i] = auslesen[i].trim();
							// Diese Abfrage verhindert, dass Unis für die der Student sich beworben hat in der Auswahl angezeigt werden
							if (splitUni[k] === auslesen[i]) {
								pruefen = "ja";
							} else {
								hilfsindex = hilfsindex + ';' + auslesen[i];
							}	
							if (pruefen === "ja") {
								hilfsindex = '';
							} else {
								index = hilfsindex;
							}
						}
						if (isEmpty(index) != true) {
							alert(index);
							var ausgelesen = index.split(';');
							for (var m = 1; m < ausgelesen.length; m++) {
								popUpHtml = popUpHtml + '<option>' + ausgelesen[m] + '</option>';
							}
							
						} else {
						}
						
						
					}
					if (auslesen.length - 1 > splitUni.length) {
						popUpHtml = popUpHtml + '<option>' + auslesen[splitUni.length] + '</option>';
					}
				} else {
					for (var l = 0; l < auslesen.length - 1; l++) {
						auslesen[l] = auslesen[l].trim();
						popUpHtml = popUpHtml + '<option>' + auslesen[l] + '</option>';
					}
				}
				popUpHtml = popUpHtml + '</select><label class="btn" id="newBewProzessWahl" style="margin-left: 10px;">Bestätigen</label>';
				if (popUpHtml.match('<option>') != '<option>') {
					popUpHtml = '<b id="popClose"><img src="images/Button Delete.png" id="smallImg"></b><br><p>Sie haben sich bereits für alle verfügbaren Auslandsuniversitäten für ihren Studiengang beworben.</p>';
				}
				$('.popUpFeld').html(popUpHtml);
				$('.popUpFeld').show();
				$('.popUpBack').show();
				document.getElementById('popClose').addEventListener('click', function(event) {
					$('.popUpBack').hide();
					$('.popUpFeld').hide();
				});
				document.getElementById('newBewProzessWahl').addEventListener('click', function(event) {
					$('.popUpFeld').hide();
					$('.popUpBack').hide();
					$('.iFenster').hide();
					$('.iF1').show();
					$('#bewProzess').hide();
					var datum = new Date;
					if (datum.getMonth() < 10 && datum.getDate() < 10) {
						var heute = (datum.getYear() + 1900) + '-0' + datum.getMonth() + '-0' + datum.getDate();
					} else if (datum.getMonth() < 10) {
						var heute = (datum.getYear() + 1900) + '-0' + datum.getMonth() + '-' + datum.getDate();
					} else if (datum.getDate() < 10) {
						var heute = (datum.getYear() + 1900) + '-' + datum.getMonth() + '-0' + datum.getDate();
					}
					$.ajax({
						type: "POST",
						url: "login_db",
						data: {
							action: "post_prozessStart",
							matrikelnummer: sessionStorage['matrikelnr'],
							uni: $('#selectUni').val(),
							datum: heute,
							schritt1: "0",
							schritt2: "0",
							schritt3: "0",
							schritt4: "0",
							schritt5: "0",
						}, success: function(result) {
							var uni = $('#selectUni').val();
							schritt1(uni);
						}, error: function(result) {
							
						}
					});
					
				});
			}, error: function(result) {
				
			}
		});
		
				
	});

	//Click-Listener für neuen Erfahrungbericht starten Button
	$('#newErfahrungsbericht').on('click', function() {
		$('.iFenster').hide();
		$('.iF2').show();
		$('#bewProzess').hide();
	});
	$('.nav').children('li').on('click', function() {
		$('.nav').children('li').removeClass('active');
		var id = $(this).attr('id').substring(2, 3);
		$('.iF2').attr('src', 'erfahrungsbericht' + id + '.html');
		$(this).addClass('active');
	});
	// Anzeigen der TipBox
	$('.btn').on('mouseover', function() {
		var id = $(this).attr('id');
		if (id === "btnBewSave") {
			$('#textbox').text("Wenn du noch nicht alle Felder ausgefüllt hast, kannst du so deine Daten zwischenspeichern, um später weiterzumachen.");
		} else if (id === "btnBewWeiter") {
			$('#textbox').text("Alle Felder ausgefüllt und überprüft? Super, dann geht's hier weiter.");
		}
		$('#textbox').show();
	});
	$('.btn').on('mouseout', function() {
		$('#textbox').hide();
	});
	$('.btn').on('click', function() {
		var id = $(this).attr('id');
		if (id === "btnBewWeiter") {
			// Hier muss noch rein, wie die Daten verschickt werden
			$('.iF1').attr('src', 'einschreibungsprozess.html');
		} else if (id === "btnBewSave") {
			//Hier muss rein, wie die eingegebenen Daten in die Datenbank gespeichert werden
		}
	});
	// Wenn der auf logout geklickt wird.
	$('#logout').on('click', function() {
		sessionStorage.clear();
		//Herausfinden auf welcher Seite ich mich gerade befinde
		var title = $(document).find("title").text();
		if (title === "DHBW Auslandsinfo") {
			
		} else /*if (title === "DHBW Auslandsportal" || title === "DHBW Auslandsportalprozess")*/ {
			location.replace("index.html");
		}
		$('#inName').val('');
		$('.logoutFenster').hide();
		$('.loginFenster').show();
		/*for (var i = 1; i <= 10; i++) {
			$('#'+ i + '5').addClass("hidden");
		}*/
	});
	//Wenn während des Bewerbungsprozess auf Zurück zur Übersicht geklickt wird
	$('.backToBewView').on('click', function() {
		$('.iFenster').hide();
		$('.iF1').hide();
		$('.iF2').hide();
		$('#bewProzess').show();
	});
	//Auswahlmöglichkeit im Bewerbungsportal zur Sortierung der Angebote
	$('#selStudiengang').on('click', function(event) {
		if ($('#selStudiengang').val() != "Alle Angebote") {
			$('.angebote').hide();
			$('.' + $('#selStudiengang').val()).show();
		} else {
			$('.angebote').show();
		}
	});
	//Blabla
	$('#closeBewerber').on('click', function(event) {
		$('.popUpBewerber').hide();
		$('.popUpBewerber').children().children('b').html('');
	});
	//Bewerbungsfomularbuttons
	$('.btnBewFormular').on('click', function(event) {
		var id = event.target.id.substring(14, 15);
		$('.popUpBack').html('<img style="position: fixed; top: 50%; margin-top: -10%; width: 20%; left: 50%; margin-left: -10%" src="images/loading.gif" />');
		$('.popUpBack').show();
		setTimeout(closeLoading, 1000);
		if (id === '1') {
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "update_User",
					matrikelnummer: sessionStorage['matrikelnr'],
					vorname: $('#bewVorname').val(),
					nachname: $('#bewNachname').val(),
					email: $('#bewEmail').val(),
					telefon: $('#bewTelefon').val(),
					mobil: $('#bewMobil').val(),
					studiengang: $('#bewStudiengang').val(),
					kurs: $('#bewKurs').val(),
				}, success: function(result) {
					if (sessionStorage['noteOkay'] === 'true') {
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "get_Adresse",
								matrikelnummer: sessionStorage['matrikelnr'],
								phase: "Praxis",
							}, success: function(result) {
								var auslesen = result.split(';');
								$('#bewStrasse').val(auslesen[0].trim());
								$('#bewHausnummer').val(auslesen[1].trim());
								$('#bewPlz').val(auslesen[2].trim());
								$('#bewStadt').val(auslesen[3].trim());
								if (auslesen[5].trim() === "Deutschland") {
									$('#bewBundesland').val(auslesen[4].trim());
									$('#bewBundesland').show();
								}
								$('#bewLand').val(auslesen[5].trim());
								if (isEmpty($('#bewStrasse').val()) != true) {
									sessionStorage['adresseOkay'] = true;
								} else {
									sessionStorage['adresseOkay'] = false;
								}
							}, error: function(result) {
								
							}
						});
					} else {
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "insert_EnglischAbi",
								matrikelnummer: sessionStorage['matrikelnr'],
								abinote: $('#bewEnglischAbi').val(),
							}, success: function(result) {
									$.ajax({
										type: "POST",
										url: "login_db",
										data: {
											action: "get_Adresse",
											matrikelnummer: sessionStorage['matrikelnr'],
											phase: "Praxis",
										}, success: function(result) {
											var auslesen = result.split(';');
											$('#bewStrasse').val(auslesen[0].trim());
											$('#bewHausnummer').val(auslesen[1].trim());
											$('#bewPlz').val(auslesen[2].trim());
											$('#bewStadt').val(auslesen[3].trim());
											if (auslesen[5].trim() === "Deutschland") {
												$('#bewBundesland').val(auslesen[4].trim());
												$('#bewBundesland').show();
											}
											
											$('#bewLand').val(auslesen[5].trim());
											if (isEmpty($('#bewStrasse').val()) != true) {
												sessionStorage['adresseOkay'] = true;
											} else {
												sessionStorage['adresseOkay'] = false;
											}
										}, erorr: function(result) {
											
										}
									});
							}, error: function(result) {
								alert(result.message);
							}
						});
					}
					
					
				}, error: function(result) {
					alert(result.message);
				}
			});
			$('.dat').hide();
			$('#bewFormular2').show();
		} else if (id === '2') {
			if (isEmpty($('#bewLand').val()) === true || isEmpty($('#bewBundesland').val()) === true || isEmpty($('#bewStrasse').val()) === true || isEmpty($('#bewHausnummer').val()) === true || isEmpty($('#bewPlz').val()) === true || isEmpty($('#bewStadt').val()) === true) {
				alert("Bitte fülle alle Felder aus.");
			}
			if (sessionStorage['adresseOkay'] === 'true') {
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "update_Adresse",
						matrikelnummer: sessionStorage['matrikelnr'],
						phase: "Praxis",
						land: $('#bewLand').val(),
						bundesland: $('#bewBundesland').val(),
						strasse: $('#bewStrasse').val(),
						hausnummer: $('#bewHausnummer').val(),
						plz: $('#bewPlz').val(),
						stadt: $('#bewStadt').val(),
					}, success: function(result) {
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "get_Adresse",
								matrikelnummer: sessionStorage['matrikelnr'],
								phase: "Theorie",
							}, success: function(result) {
								var auslesen = result.split(';');
								$('#bewThStrasse').val(auslesen[0].trim());
								$('#bewThHausnummer').val(auslesen[1].trim());
								$('#bewThPlz').val(auslesen[2].trim());
								$('#bewThStadt').val(auslesen[3].trim());
								if (auslesen[5].trim() === "Deutschland") {
									$('#bewThBundesland').val(auslesen[4].trim());
									$('#bewBundesland').show();
								}
								
								$('#bewThLand').val(auslesen[5].trim());
								if (isEmpty($('#bewThStrasse').val()) != true) {
									sessionStorage['adresseThOkay'] = true;
								} else {
									sessionStorage['adresseThOkay'] = false;
								}
							}, error: function(result) {
								
							}
						});
					}, error: function(result) {
						
					}
					
			});
			} else {
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "insert_Adresse",
						matrikelnummer: sessionStorage['matrikelnr'],
						phase: "Praxis",
						land: $('#bewLand').val(),
						bundesland: $('#bewBundesland').val(),
						strasse: $('#bewStrasse').val(),
						hausnummer: $('#bewHausnummer').val(),
						plz: $('#bewPlz').val(),
						stadt: $('#bewStadt').val(),
					}, success: function(result) {
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "get_Adresse",
								matrikelnummer: sessionStorage['matrikelnr'],
								phase: "Theorie",
							}, success: function(result) {
								var auslesen = result.split(';');
								$('#bewThStrasse').val(auslesen[0].trim());
								$('#bewThHausnummer').val(auslesen[1].trim());
								$('#bewThPlz').val(auslesen[2].trim());
								$('#bewThStadt').val(auslesen[3].trim());
								if (auslesen[5].trim() === "Deutschland") {
									$('#bewThBundesland').val(auslesen[4].trim());
									$('#bewBundesland').show();
								}
								
								$('#bewThLand').val(auslesen[5].trim());
								if (isEmpty($('#bewThStrasse').val()) != true) {
									sessionStorage['adresseThOkay'] = true;
								} else {
									sessionStorage['adresseThOkay'] = false;
								}
							}, error: function(result) {
								
							}
						});
					}, error: function(result) {
						
					}
				});
			}
			$('.dat').hide();
			$('#bewFormular3').show();
		} else if (id === '3') {
			// Hier fehlt noch die Abfrage zum Updaten oder Inserten
			if (sessionStorage['adressThOkay'] === false) {
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "insert_Adresse",
						matrikelnummer: sessionStorage['matrikelnr'],
						phase: "Theorie",
						land: $('#bewThLand').val(),
						bundesland: $('#bewThBundesland').val(),
						strasse: $('#bewThStrasse').val(),
						hausnummer: $('#bewThHausnummer').val(),
						plz: $('#bewThPlz').val(),
						stadt: $('#bewThStadt').val(),
					}, success: function(result) {
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "get_Partnerunternehmen",
								matrikelnummer: sessionStorage['matrikelnr'],
							}, success: function(result) {
								var auslesen = result.split(';');
								$('#bewPartnerName').val(auslesen[0].trim());
								$('#bewPartnerAnsprech').val(auslesen[1].trim());
								$('#bewPartnerEmail').val(auslesen[2].trim());
								$('#bewPartnerStrasse').val(auslesen[3].trim());
								$('#bewPartnerHausnummer').val(auslesen[4].trim());
								$('#bewPartnerPlz').val(auslesen[5].trim());
								$('#bewPartnerStadt').val(auslesen[6].trim());
								if (isEmpty($('#bewPartnerStrasse').val()) != true) {
									sessionStorage['partnerUnternehmenOkay'] = true;
								} else {
									sessionStorage['partnerUnternehmenOkay'] = false;
								}
							}, error: function(result) {
								
							}
						});
					}, error: function(result) {
						
					}
				});
			} else {
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "update_Adresse",
						matrikelnummer: sessionStorage['matrikelnr'],
						phase: "Theorie",
						land: $('#bewThLand').val(),
						bundesland: $('#bewThBundesland').val(),
						strasse: $('#bewThStrasse').val(),
						hausnummer: $('#bewThHausnummer').val(),
						plz: $('#bewThPlz').val(),
						stadt: $('#bewThStadt').val(),
					}, success: function(result) {
						$.ajax({
							type: "POST",
							url: "login_db",
							data: {
								action: "get_Partnerunternehmen",
								matrikelnummer: sessionStorage['matrikelnr'],
							}, success: function(result) {
								var auslesen = result.split(';');
								$('#bewPartnerName').val(auslesen[0].trim());
								$('#bewPartnerAnsprech').val(auslesen[1].trim());
								$('#bewPartnerEmail').val(auslesen[2].trim());
								$('#bewPartnerStrasse').val(auslesen[3].trim());
								$('#bewPartnerHausnummer').val(auslesen[4].trim());
								$('#bewPartnerPlz').val(auslesen[5].trim());
								$('#bewPartnerStadt').val(auslesen[6].trim());
							}, error: function(result) {
								
							}
						});
					}, error: function(result) {
						
					}
				});
			}
			$('.dat').hide();
			$('#bewFormular4').show();
		} else if (id === '4') {
			if (sessionStorage['partnerUnternhmenOkay'] === false) {
			// Abfrage zum Insert oder Updaten
				if (isEmpty($('#bewPartnerName').val()) === true || isEmpty($('#bewPartnerAnsprech').val()) === true || isEmpty($('#bewPartnerEmail').val()) === true || isEmpty($('#bewPartnerStrasse').val()) === true || isEmpty($('#bewPartnerHausnummer').val()) === true || isEmpty($('#bewPartnerPlz').val()) === true || isEmpty($('#bewPartnerStadt').val()) === true) {
					alert("Bitte fülle alle Felder aus.");
				} else {
					$.ajax({
						type: "POST",
						url: "login_db",
						data: {
							action: "insert_Partnerunternehmen",
							matrikelnummer: sessionStorage['matrikelnr'],
							firma: $('#bewPartnerName').val(),
							ansprechpartner: $('#bewPartnerAnsprech').val(),
							email: $('#bewPartnerEmail').val(),
							strasse: $('#bewPartnerStrasse').val(),
							hausnummer: $('#bewPartnerHausnummer').val(),
							plz: $('#bewPartnerPlz').val(),
							stadt: $('#bewPartnerStadt').val(),
						}, success: function(result) {
							
						}, error: function(result) {
							
						}
					});
					$('.dat').hide();
					$('.erfolgreich').html('<p>Du hat alle Daten benötigten Daten eingetragen. Frau Dreischer wird sich bei dir melden!</p>');
					$('.erfolgreich').show();
					$('.erfolgreich').fadeOut(7000);
				}
			} else {
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "update_Partnerunternehmen",
						matrikelnummer: sessionStorage['matrikelnr'],
						firma: $('#bewPartnerName').val(),
						ansprechpartner: $('#bewPartnerAnsprech').val(),
						email: $('#bewPartnerEmail').val(),
						strasse: $('#bewPartnerStrasse').val(),
						hausnummer: $('#bewPartnerHausnummer').val(),
						plz: $('#bewPartnerPlz').val(),
						stadt: $('#bewPartnerStadt').val(),
					}, success: function(result) {
						
					}, error: function(result) {
						
					}
				});
				$('.dat').hide();
				$('.erfolgreich').html('<p>Du hat alle Daten benötigten Daten eingetragen. Frau Dreischer wird sich bei dir melden!</p>');
				$('.erfolgreich').show();
				$('.erfolgreich').fadeOut(7000);
				var name = $('#bewVorname').val() + ' ' + $('bewNachname').val();
				var uni = $('#aktuelleUni').html();
				var matrikelnummer = sessionStorage['matrikelnr'];
				$.ajax({
					type: "POST",
					url: "login_db",
					data: {
						action: "sendmail",
						name: name,
						uni: uni,
						matrikelnummer: matrikelnummer,
					}, success: function(result) {
						
					}, error: function(result) {
						
					}
				});
			}
		}
	});
	//Theorieadresse ist die gleiche wie die PRaxisadresse
	$('#btnSameAdress').on('click', function(event) {
		if (sessionStorage['adressThOkay'] === 'true') {
			
		} else {
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "insert_Adresse",
					matrikelnummer: sessionStorage['matrikelnr'],
					phase: "Theorie",
					land: $('#bewLand').val(),
					bundesland: $('#bewBundesland').val(),
					strasse: $('#bewStrasse').val(),
					hausnummer: $('#bewHausnummer').val(),
					plz: $('#bewPlz').val(),
					stadt: $('#bewStadt').val(),
				}, success: function(result) {
					$('#bewThLand').val($('#bewLand').val());
					$('#bewThBundesland').val($('#bewBundesland').val());
					$('#bewThStrasse').val($('#bewStrasse').val());
					$('#bewThHausnummer').val($('#bewHausnummer').val());
					$('#bewThPlz').val($('#bewPlz').val());
					$('#bewThStadt').val($('#bewStadt').val());
				}, error: function(result) {
					
				}
			});
		}
	});
	//Bewerbungsformular zurück buttons
	$('.btnBewBack').on('click', function(event) {
		var id = event.target.id.substring(10, 11);
		if (id === '2') {
			$('.dat').hide();
			$('#bewFormular1').show();
		} else if (id === '3') {
			$('.dat').hide();
			$('#bewFormular2').show();
		} else if (id === '4') {
			$('.dat').hide();
			$('#bewFormular3').show();
		}
	});
};

$(document).ready(main);

function isEmpty(str) {
	return (!str || 0 === str.length);
}

function schritt1 (uni) {
	$.ajax({
		type: "POST",
		url: "login_db",
		data: {
			action: "get_userDaten",
			matrikelnr: sessionStorage['matrikelnr'],
		}, success: function(result) {
			$('.iFenster').hide();
			$('.iF1').show();
			$('.dat').hide();
			$('#bewFormular1').show();
			$('#bewProzess').hide();
			$('#aktuelleUni').html(uni);
			var auslesen = result.split(';');
			for (var i = 0; i < auslesen.length - 1; i++) {
				auslesen[i] = auslesen[i].trim();
			}
			fillBewForm(auslesen[1], auslesen[0], auslesen[2], auslesen[3], auslesen[4], auslesen[5], auslesen[6], auslesen[7]);
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "get_Note",
					matrikelnummer: sessionStorage['matrikelnr'],
				}, success: function(result) {
					auslesen = result.split(';');
					if (isEmpty(auslesen[0]) != true) {
						sessionStorage['noteOkay'] = true;
					} else {
						sessionStorage['noteOkay'] = false;
					}
					$('#bewEnglischAbi').val(auslesen[0]);
				}, error: function(result) {
					
				}
			});
		}, error: function(result) {
			
		}
	});
}

function getStudiengaenge() {
	$.ajax({
		type: "POST",
		url: "login_db",
		data: {
			action: "get_Studiengaenge",
		}, success: function(result) {
			var options = '<option>Alle Angebote</option>';
			var auslesen = result.split(";");
			for (var i = 0; i < auslesen.length - 1; i++) {
				options = options + '<option>' + auslesen[i] + '</option>';
			}
			$('#selStudiengang').html(options);
			getAngebotsDaten();
		}, error: function(result) {
			
		}
	});
}

function getAngebotsDaten() {
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
					htmlText = htmlText + '<div class="inhaltBox angebote row ' + auslesen[(6 * zaehler) - 6] + '" id="angebot' + zaehler + '" style="margin-right: 5px;"><div class="col-md-12">'
					+ '<h2 class="uniName">' + auslesen[(6 * zaehler) - 5] + '</h2>'
					+ '<div class="navBarAng">'
					+'<div class="navelAng active" id="n' + zaehler + '1">Infos</div>'
					+'<div class="navelAng" id="n' + zaehler + '2">FAQs</div>'
					+'<div class="navelAng" id="n' + zaehler + '3">Erfahrungsbericht</div>'
					+'<div class="navelAng" id="n' + zaehler + '4">Bilder</div>'
					+'</div>'
					+'<div class="contentAng active" id="c' + zaehler + '1"><div class="pull-left" style="width: 50%">' + auslesen[(6 * zaehler) - 4] + '</div><div class="pull-right">';
					if (isEmpty(auslesen[(6 * zaehler) - 1].trim()) != true) {
						htmlText = htmlText + '<iframe width="400" height="200" style="display: block" src="' + auslesen[(6 * zaehler) - 1] + '" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>';
					} else {
						htmlText = htmlText + '<p>Keine Kartendaten gefunden.</p>';
					}
					htmlText = htmlText + '</div></div>'
					+'<div class="contentAng" id="c' + zaehler + '2">' + auslesen[(6 * zaehler) - 3] + '</div>'
					+'<div class="contentAng" id="c' + zaehler + '3">' + auslesen[(6 * zaehler) - 2] + '</div>'
					+'<div class="contentAng" id="c' + zaehler + '4">Keine Bilder vorhanden</div>'
					+'</div>'
					+'</div>';
					zaehler++;
				}
			}
			$('#selStudiengang').after(htmlText);
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
		}, error: function(result) {
		}
	});
}

function loadBewerber(auslesen, zaehler, tabelle, count, even) {
	if (count != -1) {
		var status = 0;
		if (auslesen[(2 * (zaehler - count))] === "0") {
			status = 0;
		} else if (auslesen[(3 * (zaehler - count))] === "0") {
			status = 20;
		} else if (auslesen[(4 * (zaehler - count))] === "0") {
			status = 40;
		} else if (auslesen[(5 * (zaehler - count))] === "0") {
			status = 60;
		} else if (auslesen[(6 * (zaehler - count))] === "0") {
			status = 80;
		}
		var btn = '';
		if (status === 0 || status % 20 === 0) {
			btn = "Ausgefüllte Daten";
		} else {
			btn = "Eingaben überprüfen";
		}
		if ((zaehler - count) === 1) {
			l = 0;
		} else {
			l = ((zaehler - count) * 8) - 8;
		}
		$.ajax({
			type: "POST",
			url: "login_db",
			data: {
				action: "get_userDaten",
				matrikelnr: auslesen[l].trim(),
			}, success: function(result) {
				var ausgelesen = result.split(';');
				if ((zaehler - count) === 1) {
					tabelle = tabelle + '<tr class="' + even + '" id="row' + (zaehler - count) + '"><td>' + ausgelesen[1].trim() + '</td><td>' + ausgelesen[0].trim()  + '</td><td id="email' + (zaehler - count) + '">' + ausgelesen[2].trim() + '</td><td id="tableMatrikelnummer">' + auslesen[0] + '</td><td>' + ausgelesen[4].trim() + '</td><td>' + auslesen[1] + '<td id="status' + (zaehler - count) + '">' + status + '%</td><td class="btn" id="btnBewerberBearbeiten' + (zaehler - count) + '">' + btn + '</td>';
				} else {
					tabelle = tabelle + '<tr class="' + even + '" id="row' + (zaehler - count) + '"><td>' + ausgelesen[1].trim() + '</td><td>' + ausgelesen[0].trim()  + '</td><td id="email' + (zaehler - count) + '">' + ausgelesen[2].trim() + '</td><td id="tableMatrikelnummer">' + auslesen[((zaehler - count) * 8) - 8] + '</td><td>' + ausgelesen[4].trim() + '</td><td>' + auslesen[((zaehler - count) * 8) - 7] + '<td id="status' + (zaehler - count) + '">' + status + '%</td><td class="btn" id="btnBewerberBearbeiten' + (zaehler - count) + '">' + btn + '</td>';
				}		
				if (even === "odd") {
					even = "even"; 
				} else {
					even = "odd";
				}
				if (count != 0) {
					count = count - 1;
					loadBewerber(auslesen, zaehler, tabelle, count, even);
				} else {
					addListener(zaehler);
				}
				$('#tableBewerberBody').html(tabelle);
			}, error: function(result) {
				
			}
		});
			
	}

}

function addListener(zaehler) {
	for (var i = 1; i <= zaehler; i++) {
		document.getElementById('btnBewerberBearbeiten' + i).addEventListener('click', function(event) {
			var id = event.target.id.replace('btnBewerberBearbeiten', '');
			var matrikelnummer = $('#row' + id).children('#tableMatrikelnummer').text().trim();
			$('.popUpBack').html('<img style="position: fixed; top: 50%; margin-top: -10%; width: 20%; left: 50%; margin-left: -10%" src="images/loading.gif" />');
			$('.popUpBack').show();
			$.ajax({
				type: "POST",
				url: "login_db",
				data: {
					action: "get_userDaten",
					matrikelnr: matrikelnummer,
				}, success: function(result) {
					var auslesen = result.split(';');
					$('#formVorname').html(auslesen[1]);
					$('#formNachname').html(auslesen[0]);
					$('#formEmail').html(auslesen[2]);
					$('#formTel').html(auslesen[6]);
					$('#formMobil').html(auslesen[7]);
					$('#formStudiengang').html(auslesen[3]);
					$('#formKurs').html(auslesen[4]);
					$('#formMatrikelnummer').html(matrikelnummer);
					$('#formStandort').html(auslesen[5]);
					$.ajax({
						type: "POST",
						url: "login_db",
						data: {
							action: "get_Note",
							matrikelnummer: matrikelnummer,
						}, success: function(result) {
							auslesen = [];
							auslesen = result.split(';');
							$('#formEnglisch').html(auslesen[0]);
							$.ajax({
								type: "POST",
								url: "login_db",
								data: {
									action: "get_Adresse",
									matrikelnummer: matrikelnummer,
									phase: "Praxis",
								}, success: function(result) {
									auslesen = [];
									auslesen = result.split(';');
									$('#formPrStraße').html(auslesen[0] + ' ' + auslesen[1]);
									$('#formPrPlz').html(auslesen[2]);
									$('#formPrStadt').html(auslesen[3]);
									$('#formPrBundesland').html(auslesen[4]);
									$('#formPrLand').html(auslesen[5]);
									$.ajax({
										type: "POST",
										url: "login_db",
										data: {
											action: "get_Adresse",
											matrikelnummer: matrikelnummer,
											phase: "Theorie",
										}, success: function(result) {
											auslesen = [];
											auslesen = result.split(';');
											$('#formThStraße').html(auslesen[0] + ' ' + auslesen[1]);
											$('#formThPlz').html(auslesen[2]);
											$('#formThStadt').html(auslesen[3]);
											$('#formThBundesland').html(auslesen[4]);
											$('#formThLand').html(auslesen[5]);
											$.ajax({
												type: "POST",
												url: "login_db",
												data: {
													action: "get_Partnerunternehmen",
													matrikelnummer: matrikelnummer,
												}, success: function(result) {
													auslesen = [];
													auslesen = result.split(';');
													$('#formFirmenname').html(auslesen[0]);
													$('#formAnsprechpartner').html(auslesen[1]);
													$('#formEmailAnsprech').html(auslesen[2]);
													$('#formParStraße').html(auslesen[3] + auslesen[4]);
													$('#formParPlz').html(auslesen[5]);
													$('#formParStadt').html(auslesen[6]);
													$('.popUpBack').hide();
													$('.popUpBewerber').show();
												}, error: function(result) {
													
												}
											});
										}, error: function(result) {
											
										}
									});
								}, error: function(result) {
									
								}
							});
							
						}, error: function(result) {
							
						}
					});
					
					
				}, error: function(result) {
					
				}
			});
			
			
		});
	}
}

function closeLoading() {
	$('.popUpBack').html('');
	$('.popUpBack').hide();
}
