if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(searchString, position) {
        position = position || 0;
        return this.indexOf(searchString, position) === position;
    };
}

var main = function() {
    sessionStorage['nichtBeworben'] = false;
    sessionStorage['SchrittAktuell'] = 0;
    $('#progressbar').progressbar({
	value : 20
    });
    getStudiengaenge();
    // Überprüfen, was der User für einen Status hat
    if (isEmpty(sessionStorage['User']) === true) {
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

    $('.navEl')
	    .on( 'click', function(event) {
			$('.bewSub').hide();
			$('.navEl').removeClass('current');
			var titel = $(this).text();
			$('.navVer').children('h2').hide();
			$(this).addClass('current');
			$('.inhalt').hide();
			$('.' + titel).show();
			if (titel === "Bewerben") {
			    $('.iFenster').hide();
			//Lade aktive Prozesse
			    $
			    .ajax({
				type : "GET",
				url : "getUserInstances",
				data : {
				    matnr : sessionStorage['matrikelnr']
				},
				success : function(result) {
					tabelle = '<table class="table table-bordered table-hover"><thead><tr><th>Universität</th><th>Status</th><th colspan="2">Aktionen</th></tr></thead>';
					result = result.trim();
					if (result === ""){
						$('#tableBewProzess').html('<h2>Keine Bewerbungen vorhanden</h2>');
					}
					else {
					row = result.split("\n");

					for (var i = 0; i < row.length; i++){
						instance_info = row[i].split('|');
						//instance_info[0] = instanceID, [1] = uni, [2] = stepCounter
						tabelle = tabelle + '<tr data-rid="' + (i+1) + '"><td>' + instance_info[1] + '</td><td>' + instance_info[2] + '</td>';
						//Anlegen der Buttons
						if ((instance_info[2] === "Abgeschlossen")||(instance_info[2] === "Auf Rückmeldung warten")){
							//Übersicht
							tabelle = tabelle + '<td><span class="glyphicon glyphicon-list" title="Übersicht" onclick="location.href=\'task_detail.html?instance_id=' + instance_info[0] + '\'"></span></td>';
						} else if (instance_info[2] === "Daten prüfen"){
								//Übersicht
								tabelle = tabelle + '<td><span class="glyphicon glyphicon-list" title="Übersicht" onclick="location.href=\'task_detail.html?instance_id=' + instance_info[0] + '&send_bew=true\'"></span><td>';
								//Prozess löschen
								tabelle = tabelle + '<span uni="' + instance_info[1] + '" class="glyphicon glyphicon-trash btn-delete" title="Löschen" rid="' + (i+1) + '"></span>';
						} else {
							//Fortsetzen
							tabelle = tabelle + '<td><span class="glyphicon glyphicon-arrow-right" title="fortsetzen" onclick="location.href=\'bewerben.html?instance_id=' + instance_info[0] + '&uni='+instance_info[1]+'\'"></span></td>';
							//Prozess löschen
							tabelle = tabelle + '<span uni="' + instance_info[1] + '" class="glyphicon glyphicon-trash btn-delete" title="Löschen" rid="' + (i+1) + '"></span>';
						}
						tabelle = tabelle + '</tr>'
					}
					tabelle = tabelle + '</table>';
					$('#tableBewProzess').html(tabelle);

					$('.btn-delete').on('click', function() {
						var uni = $(this).attr("uni");
						var id = $(this).attr("rid");
				    	swal({
				    		  title: "Bist du sicher?",
				    		  text: "Der Prozess kann nicht wiederhergestellt werden!",
				    		  type: "warning",
				    		  showCancelButton: true,
				    		  confirmButtonColor: "#DD6B55",
				    		  confirmButtonText: "Löschen!",
				    		  closeOnConfirm: false
				    		},
				    	function(){

				    		//var id = $('.btn-delete').closest('tr').data('rid');
				    		var matrikelnummer = sessionStorage['matrikelnr'];

				    		$.ajax({
				    			type : "GET",
				    			url : "process/delete",
				    			data : {
				    				matrikelnummer : matrikelnummer,
				    				uni: uni
				    			}
				    		}).done(function(data) {
				    			$('#tableBewProzess tr[data-rid='+ id +']').remove();
				    			swal('Gelöscht!', 'Der Prozess wurde erfolgreich gelöscht.', 'success');
				    			sessionStorage['beworbeneUnis'].split(';');
				    		}).error(function (error) {
				    			console.error(error);
				    			swal('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
				    		})
				    	});
					});
					}

				},
				error: function(result){
					swal("Fehler","Beim abrufen der laufenden Prozesse ist ein fehler aufgetreten","error");
				}
				});
			    /*$('.popUpBack')
				    .html(
					    '<img style="position: fixed; top: 50%; margin-top: -10%; width: 20%; left: 50%; margin-left: -10%" src="images/loading.gif" />');
			    $('.popUpBack').show();*/
			    /*setTimeout(closeLoading, 1500);
			    $
				    .ajax({
					type : "POST",
					url : "login_db",
					data : {
					    action : "get_prozessStatus",
					    matrikelnummer : sessionStorage['matrikelnr']
					},
					success : function(result) {
						sessionStorage['beworbeneUnis'] = '';
					    var even = "odd";
					    var tabelle = '';
					    var schritt_gesamt = 0;
					    var schritt_aktuell = 0;
					    var status = '';
					    var zaehler = 1;
					    var auslesen = result.split(';');

					    for (var i = 0; i < auslesen.length; i++) {
						auslesen[i] = auslesen[i]
							.trim();

					    schritt_aktuell = Number(auslesen[((2 * zaehler) + ((zaehler-1) * 2))]);
					    schritt_gesamt = Number(auslesen[((3 * zaehler) + (zaehler-1))]);

					    status = schritt_aktuell + ' von ' + schritt_gesamt + ' Schritte';

						/*if (schritt_aktuell > schritt_gesamt) {
							status = status + "FEHLER" + result;
						}
						if (schritt_aktuell == schritt_gesamt) {
							status = "abgeschlossen";
						}

						if (i === (4 * zaehler)) {
							if (status === "abgeschlossen") {
							tabelle = tabelle
								+ '<tr class="'
								+ even
								+ '" id="row'
								+ (zaehler)
                                + '" data-rid="'+zaehler+'"><td>'
								+ sessionStorage["studiengang"]
								+ '</td><td id="uni'
								+ zaehler
								+ '">'
								+ auslesen[((4 * zaehler) - 4)]
								+ '</td><td>'
								+ auslesen[((4 * zaehler) - 3)]
								+ '</td><td id="status'
								+ zaehler
								+ '">'
								+ status
								+ '</td><td class="btn" id="btnZusammenfassung'
								+ zaehler
								+ '">Zusammenfassung</td><td class="btn btnProcessDelete">Löschen</td></tr>';
						    } else {
							tabelle = tabelle
								+ '<tr class="'
								+ even
								+ '" id="row'
								+ (zaehler)
								+ '" data-rid="'+zaehler+'"><td>'
								+ sessionStorage["studiengang"]
								+ '</td><td id="uni'
								+ zaehler
								+ '">'
								+ auslesen[((4 * zaehler) - 4)]
								+ '</td><td>'
								+ auslesen[((4 * zaehler) - 3)]
								+ '</td><td id="status'
								+ zaehler
								+ '">'
								+ status
								+ '</td><td class="btn" id="btnProzessFortfahren'
								+ zaehler
								+ '">Fortsetzen</td><td class="btn btnProcessDelete">Löschen</td></tr>';
						    }
						    sessionStorage['beworbeneUnis'] = sessionStorage['beworbeneUnis']
							    + auslesen[((4 * zaehler) - 4)]
							    + ';';
						    zaehler++; // Muss am Ende
						    // dieses
						    // if-Statements
						    // stehen!!!!
						}
						if (even === "odd") {
						    even = "even";
						} else {
						    even === "odd";
						}
					    }
					    // ANZEIGE NICHT BEWORBEN
					    if (isEmpty(tabelle) === true) {
							$('#tableBewProzess').hide();
							$('#nichtBeworben').show();
					    } else {
							$('#tableBewProzess').show();
							$('#nichtBeworben').hide();
					    }
					    // GENERIERUNG TABELLE
					    $('#tableBewProzessBody').html(
						    tabelle);
					    // *** ENDE TABELLE

					    for (var i = 1; i < zaehler; i++) {
							$('#btnProzessFortfahren'+ i).on('click',
								function(event) {
								    var id = event.target.id
									    .replace('btnProzessFortfahren','');
								    /*$('.popUpBack').html('<img style="position: fixed; top: 50%; margin-top: -10%; width: 20%; left: 50%; margin-left: -10%" src="images/loading.gif" />');
								    $('.popUpBack').show();
								    setTimeout(closeLoading,1000);

								    // Ermittlung des Fortschritts für die weiteren Bewerbungsschritte
								    var uni = $('#uni'+ id).text();
                                    sessionStorage['uni'] = uni;
								    /*$('.iFenster').hide();
								    $('.iF1').show();
								    $('.dat').hide();
								    sessionStorage['uni'] = uni;
								    $('#bewProzess').hide();
								    $('#aktuelleUni').html(uni);
								    SchrittReq(uni);
								    askNextStep(uni);
								    $
								    .ajax({
									type : "GET",
									url : "getInstance",
									data : {
									    matnr : sessionStorage['matrikelnr'],
									    uni : uni
									},
									success : function(
										result) {
									    location.replace("http://193.196.7.215:8080/Auslandssemesterportal/WebContent/bewerben.html?instance_id="+result);
									},
									error : function(
										result) {
									}
								    });


								});

							$('#btnZusammenfassung'+ i).on('click',
									function(event) {
									var id = event.target.id
								    	.replace('btnZusammenfassung','');
									var uni = $('#uni'+ id).text();
								    /*$('.iFenster').hide();
								    $('.iF1').show();
								    $('.dat').hide();
									$('#bewProzess').hide();
									$('#aktuelleUni').html(uni);
									getDataAllPruef();
									$('#bewFormular10').show();
									sessionStorage['uni'] = uni;
									$
								    .ajax({
									type : "GET",
									url : "getInstance",
									data : {
									    matnr : sessionStorage['matrikelnr'],
									    uni : sessionStorage['uni']
									},
									success : function(
										result) {
									    location.replace("http://193.196.7.215:8080/Auslandssemesterportal/WebContent/task_detail.html?instance_id="+result);
									},
									error : function(
										result) {
									}
								    });
								});
					    }

					    $('.btnProcessDelete').on('click', function() {
					    	swal({
					    		  title: "Bist du sicher?",
					    		  text: "Der Prozess kann nicht wiederhergestellt werden!",
					    		  type: "warning",
					    		  showCancelButton: true,
					    		  confirmButtonColor: "#DD6B55",
					    		  confirmButtonText: "Löschen!",
					    		  closeOnConfirm: false
					    		},
					    	function(){

					    		var id = $('.btnProcessDelete').closest('tr').data('rid');
					    		var uni = $('#uni' + id).text();
					    		var matrikelnummer = sessionStorage['matrikelnr'];

					    		$.ajax({
					    			type : "GET",
					    			url : "process/delete",
					    			data : {
					    				matrikelnummer : matrikelnummer,
					    				uni: uni
					    			}
					    		}).done(function(data) {
					    			$('#tableBewProzessBody tr[data-rid='+ id +']').remove();
					    			swal('Gelöscht!', 'Der Prozess wurde erfolgreich gelöscht.', 'success');
					    			sessionStorage['beworbeneUnis'].split(';');
					    		}).error(function (error) {
					    			console.error(error);
					    			swal('Fehler', 'Der Prozess konnte nicht gelöscht werden', 'error');
					    		})
					    	});
						});
					},
					error : function(result) {

					}
				    }); */
			    //$('#bewProzess').show();
			} else if (titel === "Bewerber") {
			    $.ajax({
				type : "POST",
				url : "login_db",
				data : {
				    action : "get_bewerber",
				},
				success : function(result) {
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
				    loadBewerber(auslesen, zaehler, tabelle,
					    count, even);

				},
				error : function(result) {

				}
			    });
			}

			var id = $(this).attr('id');
			id = id.substring(3, 4);
			$('#in' + id).show();
		    });
    // Click-Listener f�r neuen Bewerbungsprozess starten Button
    $('#newBewProzess')
	    .on(
		    'click',
		    function() {
			var popUpHtml = '<div class="form-horizontal"><div class="form-group"><div class="col-md-12"><select class="inBox" id="selectUni">';
			$
				.ajax({
				    type : "POST",
				    url : "login_db",
				    data : {
					action : "get_Unis",
					studiengang : sessionStorage['studiengang'],
				    },
				    success : function(result) {
					var auslesen = result.split(';');
					// var splitUnis =
					// sessionStorage['beworbeneUnis'].split(';');
					var splitUni = [];
					for (var j = 0; isEmpty($(
						'#uni' + (j + 1)).text()) != true; j++) {
					    splitUni[j] = $('#uni' + (j + 1))
						    .text();
					}
					var pruefen = 'nein';
					var index = '';
					var hilfsindex = '';
					if (isEmpty(splitUni) != true) {
					    for (var i = 0; i < auslesen.length - 1; i++) {
						for (var k = 0; k < splitUni.length; k++) {
						    auslesen[i] = auslesen[i]
							    .trim();
						    // Diese Abfrage verhindert,
						    // dass Unis f�r die der
						    // Student sich beworben hat
						    // in der Auswahl angezeigt
						    // werden
						    if (splitUni[k] === auslesen[i]) {
							pruefen = "ja";
						    } else {
							hilfsindex = hilfsindex
								+ ';'
								+ auslesen[i];
						    }
						    if (pruefen === "ja") {
							hilfsindex = '';
						    } else {
							index = hilfsindex;
						    }
						}
						if (isEmpty(index) != true) {
						    var ausgelesen = index
							    .split(';');
						    for (var m = 1; m < ausgelesen.length; m++) {
							popUpHtml = popUpHtml
								+ '<option>'
								+ ausgelesen[m]
								+ '</option>';
						    }

						} else {
						}

					    }
					    if (auslesen.length - 1 > splitUni.length) {
						popUpHtml = popUpHtml
							+ '<option>'
							+ auslesen[splitUni.length]
							+ '</option>';
					    }
					} else {
					    for (var l = 0; l < auslesen.length - 1; l++) {
						auslesen[l] = auslesen[l]
							.trim();
						popUpHtml = popUpHtml
							+ '<option>'
							+ auslesen[l]
							+ '</option>';
					    }
					}
					popUpHtml = popUpHtml
						+ '</select></div></div><div class="form-group"><div class="col-md-12"><button id="newBewProzessWahl" class="btn btn-success">Bestätigen</button></div></div></div>';

					if (popUpHtml.match('<option>') != '<option>') {
					    popUpHtml = '<b id="popClose"><img src="images/Button Delete.png" id="smallImg"></b><br><p>Sie haben sich bereits für alle verfügbaren Auslandsuniversitäten für ihren Studiengang beworben.</p>';
					}
                        $.sweetModal({
                            title: 'Bitte wähle die Uni aus',
                            content: popUpHtml,
							width: '500px',
                            onOpen: function () {
                            	$('#newBewProzessWahl').on('click', function () {
                                    sessionStorage['uni'] = $('#selectUni').val();
                                    $.ajax({
										type : "GET",
										url : "getInstance",
										data : {
											//NEUE DB-EINTRAG
											matnr : sessionStorage['matrikelnr'],
											uni : $(
												'#selectUni')
												.val(),
										},
										success : function(
											result) {
											/*var uni = $(
											 '#selectUni')
											 .val();
											 zaehlupdate(0);
											 askNextStep(uni); */
											location.replace("http://193.196.7.215:8080/Auslandssemesterportal/WebContent/bewerben.html?instance_id="+result);
										},
										error : function(
											result) {
										}
									});
                                });
                            }
                        });
				    },
				    error : function(result) {

				    }
				});

		    });

    // Click-Listener für neuen Erfahrungbericht starten Button
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


    // Wenn der User auf logout geklickt wird.
    $('#logout').on('click', function() {
		window.location.href = "logout"; 
    });
    // Wenn während des Bewerbungsprozess auf "Zurück" zur Übersicht geklickt
    // wird
    $('.backToBewView').on('click', function() {
	$('.iFenster').hide();
	$('.iF1').hide();
	$('.iF2').hide();
	$('#bewProzess').show();
    });
    // Auswahlmöglichkeit im Bewerbungsportal zur Sortierung der Angebote
    $('#selStudiengang').on('click', function(event) {
	if ($('#selStudiengang').val() != "Alle Angebote") {
	    $('.angebote').hide();
	    $('.' + $('#selStudiengang').val()).show();
	} else {
	    $('.angebote').show();
	}
    });
    $('#btnNotePruef').on('click', function(event) {
	var note = $('#bewEnglischAbi').val();
	if (note >= 11) {
	    $.ajax({
		type : "POST",
		url : "login_db",
		data : {
		    action : "note_ueberelf",
		    note : note,
		},
		success : function(result) {

		},
		error : function(result) {

		}
	    });
	} else {
	    $.ajax({
		type : "POST",
		url : "login_db",
		data : {
		    action : "note_unterelf",
		    note : note,
		},
		success : function(result) {
		},
		error : function(result) {

		}
	    });
	}
    });


    if (window.location.hash && window.location.hash.startsWith('#nav')) {
        $(window.location.hash).trigger('click');
    }

};

$(document).ready(main);

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function getStudiengaenge() {
    $.ajax({
	type : "POST",
	url : "login_db",
	data : {
	    action : "get_Studiengaenge",
	},
	success : function(result) {
	    var options = '<option>Alle Angebote</option>';
	    var auslesen = result.split(";");
	    for (var i = 0; i < auslesen.length - 1; i++) {
		options = options + '<option>' + auslesen[i] + '</option>';
	    }
	    $('#selStudiengang').html(options);
	    getAngebotsDaten();

	},
	error : function(result) {

	}
    });
}

function getAngebotsDaten() {
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
		    if (result == ''){
		    	$('.keininhalt').show();
		    };
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
				    + '<div class="inhaltBox angebote row '
				    + auslesen[(6 * zaehler) - 6]
				    + '" id="angebot'
				    + zaehler
				    + '" style="margin-right: 5px;"><div class="col-md-12">'
				    + '<h2 class="uniName">'
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
				    + '1"><div class="pull-left" style="width: 50%">'
				    + auslesen[(6 * zaehler) - 4]
				    + '</div><div class="pull-right">';
			    if (isEmpty(auslesen[(6 * zaehler) - 1].trim()) != true) {
				htmlText = htmlText
					+ '<iframe width="400" height="200" style="display: block" src="'
					+ auslesen[(6 * zaehler) - 1]
					+ '" frameborder="0" scrolling="no" marginheight="0" marginwidth="0"></iframe>';
			    } else {
				htmlText = htmlText
					+ '<p>Keine Kartendaten gefunden.</p>';
			    }
			    htmlText = htmlText + '</div></div>'
				    + '<div class="contentAng" id="c' + zaehler
				    + '2">' + auslesen[(6 * zaehler) - 3]
				    + '</div>'
				    + '<div class="contentAng" id="c' + zaehler
				    + '3">' + auslesen[(6 * zaehler) - 2]
				    + '</div>'
				    + '<div class="contentAng" id="c' + zaehler
				    + '4">Keine Bilder vorhanden</div>'
				    + '</div>' + '</div>';
			    zaehler++;
			}
		    }
		    $('#selStudiengang').after(htmlText);
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
		},
		error : function(result) {
		}
	    });
}

function closeLoading() {
    $('.popUpBack').html('');
    $('.popUpBack').hide();
}

var acc = document.getElementsByClassName("accordion");
var i;

for (i = 0; i < acc.length; i++) {
    acc[i].onclick = function(){
        /* Toggle between adding and removing the "active" class,
        to highlight the button that controls the panel */
        this.classList.toggle("active");

        /* Toggle between hiding and showing the active panel */
        var panel = this.nextElementSibling;
        if (panel.style.display === "block") {
            panel.style.display = "none";
        } else {
            panel.style.display = "block";

        }
    }
}
