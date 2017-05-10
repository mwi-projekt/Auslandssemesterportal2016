

  $(document).ready(function() {
	
	
	// hier wird der Anzahl der Sternen ausgewählt
	  
	$('#rating a').hover(function(){
	    $('.star_off').mouseover(function(){
	      $(this).removeClass('star_off').addClass('star_on');
	      $(this).prevAll('.star_off').removeClass('star_off').addClass('star_on');
	      $(this).nextAll('.star_on').removeClass('star_on').addClass('star_off');
	    });
	 /* }, function() {
	    $('.star_on').removeClass('star_on').addClass('star_off');*/
	});
	    
	    
	   
	//frageID, Anzahl der Sternen, Matrikelnummer, Frage,  Antwort und Bildern von HTML speichern und  an erfahrungsberichtInhalt_db.java übergeben
	
	$('.btn').on('click', function() {
		
		var id = $(this).attr('id');
		
		matrikelnummer = $('#matrikelnummer').val();
		
	 
		//ID X1 gehört zu Button: " Erfaruhngsbericht abschlißen" und wird nur dann ausgeführt, wenn
		//dann dieser Button bei Erfahrungsbericht4.html gedrückt wird.
		/* if(id == x1){
			
		flag = 1;	
		$.
		ajax(
			 {
			type: "POST",
			url: "erfahrungsberichtInhalt_db",
			data: { 
				action: "flag_post",
				
				matrikelnummer: matrikelnummer,
				flag: flag		
			},
			success: function(result){
		       
				}})
				.done(function(data) {
					    
						alert("Ihre Antworten wurde erfolgreich gespeichert");
				})
		
				.fail(function(data) {
					alert( "error" + data );
				});
		
			
	  };
	  */
		
		
		var rating = 0;
		for (var i = 1; $('#s' + id).children().children('#stern' + i).attr('class') === 'star_on'; i++) {
			rating = i;
		}
		
		//Bei IDs: 17,18,19 und 20 werden dann statt Antwort die Bildern in DB gespeichert.
		if( id >= 17 ){
			
			bild = $('#bl' + id).val();	
			$.
			ajax(
				 {
				type: "POST",
				url: "erfahrungsberichtInhalt_db",
				data: { 
					action: "bild_post",
					frageID: id,
					matrikelnummer: matrikelnummer,
					bild: bild
					
					
				},
				success: function(result){
			       
					
					}})
					.done(function(data) {
						    
							alert("Ihre Bild war erfolgreich gespeichert.");
					})
			
					.fail(function(data) {
						alert( "error" + data );
					});
		
		}
		
		else{
			
		 frage = $('#f' + id).text();
		 antworttext = $('#t' + id).val();
		
		//alert(id + ", "+ rating +", "+ matrikelnummer+", "+ frage+ ", "+ antworttext );
		//if (rating != "0") {
		 $.
			ajax(
				 {
				type: "POST",
				url: "erfahrungsberichtInhalt_db",
				data: { 
					action: "erfahrungsbericht_post",
					frageID: id,
					matrikelnummer: matrikelnummer,
					frage: frage,
					antworttext: antworttext,
					stern: rating
					
					
					
				},
				success: function(result){
			       		
					}})
					.done(function(data) {
						    
							alert("Ihre Antwort war erfolgreich gespeichert. Antworten Sie bitte auf die naechste Frage!!!");
					})
			
					.fail(function(data) {
						alert( "error" + data );
					});
		
		}
			
			
		
	});
   
  
	
});
	
	
	
	
	
	
	
	
	
	
	
	
