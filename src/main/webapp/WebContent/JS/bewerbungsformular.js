 
var main = function() {
	//Laden der Nutzerdaten aus der Datenbank zu dem eingeloggten Nutzer
	
	
	$(".sprachnachweis").hide();
	
	$("#btnNotePruef").click(function() {	
		var pru = $(".pr").val();
			if(pru >= 0 && pru <= 15){
				if (pru < 11) {
					$(".sprachnachweis").show();
				} else {
					$('.sprachnachweis').hide();
					$('.erfolgreich').html('<p>Du hast die Mindestanforderungen mit deiner Abiturnote erfüllt.</p>');
					$('.erfolgreich').show();
					$('.erfolgreich').fadeOut(10000);
					$('.popUpBack').hide();
					$('.popUpFeld').fadeOut();
				}
			
			} else {
				alert("Bitte Note in Punkten eintragen!");
			}
		
		});


	


   $('#tooltip').hover(function(){
        $("#tooltip").tooltip();

   });
   
  

   $("#frmContact").submit(function(){
	   
	   var formControl = true; 
	
	   var email = $(".email").val();
	   
	    function validateEmail(email) { 
		     var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		     return re.test(email);
	  } 
   
	  if(validateEmail(email) == false) {
		  formControl = false;
		  $(".email").addClass("warning");
		  $(".email").val("Ihre Emailadresse bitte noch mal, korrekt eingeben");
		
	  }else{  $(".email").removeClass("warning"); }
      	  	  
        return false;
	       
   });

};



$(document).ready(main);

function fillBewForm(vorname, nachname, email, studiengang, kurs, standort, telefon, mobil) {
	$('#bewVorname').val(vorname);
	$('#bewNachname').val(nachname);
	$('#bewEmail').val(email);
	$('#bewStudiengang').val(studiengang);
	$('#bewKurs').val(kurs);
	$('#bewStandort').val(standort);
	$('#bewTelefon').val(telefon);
	$('#bewMobil').val(mobil);
}