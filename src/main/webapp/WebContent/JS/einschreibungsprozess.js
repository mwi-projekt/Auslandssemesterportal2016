var main = function() {
	
	

	 $(".k1").show();
     $(".k2").show();
     $(".k3").show();
     $(".k4").show();
     $(".k5").hide();
     $(".k6").hide();
     
     $(".choose").click(function() {	
    	 
    	 $(".k1").hide();
         $(".k2").hide();
         $(".k3").hide();
         $(".k4").hide();
         $(".k5").hide();
         $(".k6").hide();
        
    	
		var auswahl = $(".aus").val();
		var s= "s";
		var k= "k";
		var d= "d";
		
		
			if(auswahl==s){
			
				$(".k1").show();
				$(".k2").show();
				
			}
			else if(auswahl==k){
				
				$(".k1").show();
				$(".k2").show();
				$(".k3").show();
				$(".k4").show();
				
				
			}
           else if(auswahl==d){
				
				$(".k1").show();
				$(".k2").show();
				$(".k5").show();
				$(".k6").show();
				
				
			}
           
   
   
   });
   
};

$(document).ready(main);