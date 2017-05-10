<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
	<script src="external/jquery-ui/jquery-ui.js"></script>
	<script src="external/jquery-ui/jquery-ui.min.js"></script>
	<script src="external/jquery/jquery.js"></script>
	
		<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

<style type="text/css">
	@charset "utf-8";
	body {color: black; }
	
	#serv_status_out {}
</style>

</head>
<body>
	<fieldset id="server_control">
		
		<input type="button" id="pserv_start" value="Server Start" onclick="psrv_start();">
		<input type="button" id="pserv_stop" value="Server Stop" onclick="psrv_stop();">
		<div id="serv_status_out"></div>
		
	</fieldset>
</body>
	<script type="text/javascript">
	$('#pserv_start').hide();
	$('#pserv_stop').hide();
	//
	//
	$.ajax({
	type: "GET",
	url: "prozess_db",
	data: { action:"get_srvStatus"
		  }
	})
	.done(function( data ) {	
		var part = data.split(';');	
		if (part[0] == "up" ){
			$('#pserv_start').hide();
			$('#pserv_stop').show();
			document.getElementById("serv_status_out").innerHTML = "SERVER is UP";
		}
		if (part[0] == "down" ){
			$('#pserv_stop').hide();
			$('#pserv_start').show();
			document.getElementById("serv_status_out").innerHTML = "SERVER is DOWN";
		}
	});	
	//
	//
	function psrv_start(){
		$.ajax({
			type: "POST",
			url: "prozess_running",
			data: { action: "start_srv"
				  }
		})
	  	.success(function(data) {
			//alert("erfolgreich");
			location.reload();
	  	})
	  	.fail(function(data){
			alert("fehler");
	  	});
	}
	
	function psrv_stop(){
		$.ajax({
			type: "POST",
			url: "prozess_running",
			data: { action: "stop_srv"
				  }
			})
	  	.success(function(data) {
			//alert("erfolgreich");
			location.reload();
	  	})
	 	.fail(function(data){
			alert("fehler");
	  	});
	}
	</script>
</html>