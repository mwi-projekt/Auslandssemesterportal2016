 $(document).ready(function() {
	
	

	$('.btn-primary').on('click', function() {
		
		var id = $(this).attr('id');
		
		matrikelnummer = $('#matrikelnummer').val();
		
	  if(id == x2){
				
		$.
		ajax(
			 {
			type: "POST",
			url: "erfahrungsberichtInhalt_db",
			data: { 
				action: "erfahrungsbericht_get",
				
				matrikelnummer: matrikelnummer,
				flag: flag		
			},
			success: function(result){
		       
				}})
				.done(function(data) {
					    
						alert("");
				})
		
				.fail(function(data) {
					alert( "error" + data );
				});
		
			
	  }
	  else{}	
			
			
			
		
		
				
		
	});
   
  
 
	
});
	
	