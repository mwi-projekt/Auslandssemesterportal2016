

//post a new Dokument (Abizeugnis) in the DB for the user
function new_dokument_Abizeugnis() {
	var matrikelnummer = document.getElementById("matrikelnummer").value;
	// .getElementById '= id des html Elements
	var name_dok1 = document.getElementById("name_dok1").value;
	
	var dokument = document.getElementById("dokument").value;
	var checkboxAutoHochgeladen = document.getElementById("checkboxAutoHochgeladen_01").checked ? 1 : 0;
	var checkboxAuslandsamtDokumenteKorrekt = document.getElementById("checkboxAuslandsamtDokumenteKorrekt_01").checked ? 1 : 0;
	var checkboxDokumentePostalischVersendet = document.getElementById("checkboxDokumentePostalischVersendet_01").checked ? 1 : 0;
	var checkboxDokumentePostalischKorrekt = document.getElementById("checkboxDokumentePostalischKorrekt_01").checked ? 1 : 0;
	if ("" != dokument) {
		$
				.ajax(
						{
							type : "POST",
							url : "prozessInhalt_db",
							data : {
								action : "post_dokument",
								matrikelnummer : matrikelnummer,
								name_dok : name_dok1,
								dokument : dokument,
								checkboxAutoHochgeladen : checkboxAutoHochgeladen,
								checkboxAuslandsamtDokumenteKorrekt : checkboxAuslandsamtDokumenteKorrekt,
								checkboxDokumentePostalischVersendet : checkboxDokumentePostalischVersendet,
								checkboxDokumentePostalischKorrekt : checkboxDokumentePostalischKorrekt
							}
						})	.done(function(data) {
								alert("Dokument hochgeladen mit der ID = "+ data + " Dokument ausgewählt: " + name_dok1);
							})
//							.done(function() {
//								alert( "success" );
//							})
							.fail(function() {
								alert( "error" );
							});

	} else {
		alert("Bitte Dokument hinzufügen!");
	}
}

//*************************************************************************************************************************
//post a new Dokument (Motivationsschreiben) in the DB for the user
function new_dokument_Motivationsschreiben() {
	var matrikelnummer = document.getElementById("matrikelnummer").value;
	// .getElementById '= id des html Elements
	var name_dok2 = document.getElementById("name_dok2").value;
	alert("Dokument ausgewählt: " + name_dok2);
	var dokument = document.getElementById("dokument").value;
	var checkboxAutoHochgeladen = document.getElementById("checkboxAutoHochgeladen_02").checked ? 1 : 0;
	var checkboxAuslandsamtDokumenteKorrekt = document.getElementById("checkboxAuslandsamtDokumenteKorrekt_02").checked ? 1 : 0;
	var checkboxDokumentePostalischVersendet = document.getElementById("checkboxDokumentePostalischVersendet_02").checked ? 1 : 0;
	var checkboxDokumentePostalischKorrekt = document.getElementById("checkboxDokumentePostalischKorrekt_02").checked ? 1 : 0;
	if ("" != dokument) {
		$
				.ajax(
						{
							type : "POST",
							url : "prozessInhalt_db",
							data : {
								action : "post_dokument",
								matrikelnummer : matrikelnummer,
								name_dok : name_dok2,
								dokument : dokument,
								checkboxAutoHochgeladen : checkboxAutoHochgeladen,
								checkboxAuslandsamtDokumenteKorrekt : checkboxAuslandsamtDokumenteKorrekt,
								checkboxDokumentePostalischVersendet : checkboxDokumentePostalischVersendet,
								checkboxDokumentePostalischKorrekt : checkboxDokumentePostalischKorrekt
							}
						})	.done(function(data) {
								alert("Dokument hochgeladen mit der ID = "+ data);
							})
							.done(function() {
								alert( "success" );
							})
							.fail(function() {
								alert( "error" );
							});

	} else {
		alert("Bitte Dokument hinzufügen!");
	}
}

//*************************************************************************************************************************
//post a new Dokument (Notenauszug Dualis) in the DB for the user
function new_dokument_NotenauszugDualis() {
	var matrikelnummer = document.getElementById("matrikelnummer").value;
	// .getElementById '= id des html Elements
	var name_dok3 = document.getElementById("name_dok3").value;
	alert("Dokument ausgewählt: " + name_dok3);
	var dokument = document.getElementById("dokument").value;
	var checkboxAutoHochgeladen = document.getElementById("checkboxAutoHochgeladen_03").checked ? 1 : 0;
	var checkboxAuslandsamtDokumenteKorrekt = document.getElementById("checkboxAuslandsamtDokumenteKorrekt_03").checked ? 1 : 0;
	var checkboxDokumentePostalischVersendet = document.getElementById("checkboxDokumentePostalischVersendet_03").checked ? 1 : 0;
	var checkboxDokumentePostalischKorrekt = document.getElementById("checkboxDokumentePostalischKorrekt_03").checked ? 1 : 0;
	if ("" != dokument) {
		$
				.ajax(
						{
							type : "POST",
							url : "prozessInhalt_db",
							data : {
								action : "post_dokument",
								matrikelnummer : matrikelnummer,
								name_dok : name_dok3,
								dokument : dokument,
								checkboxAutoHochgeladen : checkboxAutoHochgeladen,
								checkboxAuslandsamtDokumenteKorrekt : checkboxAuslandsamtDokumenteKorrekt,
								checkboxDokumentePostalischVersendet : checkboxDokumentePostalischVersendet,
								checkboxDokumentePostalischKorrekt : checkboxDokumentePostalischKorrekt
							}
						})	.done(function(data) {
								alert("Dokument hochgeladen mit der ID = "+ data);
							})
							.done(function() {
								alert( "success" );
							})
							.fail(function() {
								alert( "error" );
							});

	} else {
		alert("Bitte Dokument hinzufügen!");
	}
}

//*************************************************************************************************************************
//post a new Dokument (Erklärung) in the DB for the user
function new_dokument_Erklaerung() {
	var matrikelnummer = document.getElementById("matrikelnummer").value;
	// .getElementById '= id des html Elements
	var name_dok4 = document.getElementById("name_dok4").value;
	alert("Dokument ausgewählt: " + name_dok4);
	var dokument = document.getElementById("dokument").value;
	var checkboxAutoHochgeladen = document.getElementById("checkboxAutoHochgeladen_04").checked ? 1 : 0;
	var checkboxAuslandsamtDokumenteKorrekt = document.getElementById("checkboxAuslandsamtDokumenteKorrekt_04").checked ? 1 : 0;
	var checkboxDokumentePostalischVersendet = document.getElementById("checkboxDokumentePostalischVersendet_04").checked ? 1 : 0;
	var checkboxDokumentePostalischKorrekt = document.getElementById("checkboxDokumentePostalischKorrekt_04").checked ? 1 : 0;
	if ("" != dokument) {
		$
				.ajax(
						{
							type : "POST",
							url : "prozessInhalt_db",
							data : {
								action : "post_dokument",
								matrikelnummer : matrikelnummer,
								name_dok : name_dok4,
								dokument : dokument,
								checkboxAutoHochgeladen : checkboxAutoHochgeladen,
								checkboxAuslandsamtDokumenteKorrekt : checkboxAuslandsamtDokumenteKorrekt,
								checkboxDokumentePostalischVersendet : checkboxDokumentePostalischVersendet,
								checkboxDokumentePostalischKorrekt : checkboxDokumentePostalischKorrekt
							}
						})	.done(function(data) {
								alert("Dokument hochgeladen mit der ID = "+ data);
							})
							.done(function() {
								alert( "success" );
							})
							.fail(function() {
								alert( "error" );
							});

	} else {
		alert("Bitte Dokument hinzufügen!");
	}
}