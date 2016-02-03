

//post a new Bewerbung in the DB for the user
function new_Bewerbung() {
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

