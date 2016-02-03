<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Prozess</title>
	<script src="external/jquery-ui/jquery-ui.js"></script>
	<script src="external/jquery-ui/jquery-ui.min.js"></script>
	<script src="external/jquery/jquery.js"></script>
	
		<script src="//code.jquery.com/ui/1.11.2/jquery-ui.js"></script>

<style type="text/css">
	@charset "utf-8";
	/* style for the new prozess input */
	body {font-family: "Helvetica Neue",Helvetica,Arial,sans-serif; font-size: 14px !important;}
		 
	#new_prozess_in{ color: black!important; }

	/* style for the list of documents */
	#products {color: black !important; float: left !important; width: 350px !important; height: auto !important; margin-right: 20px !important; display: block !important;
				box-shadow: 0 6px 12px rgba(0, 0, 0, .5) !important; border-radius: 14px !important; z-index: 0 !important;}
	#headers {padding: 0px 5px !important; margin: 0px !important; height: 34px !important; font-size: 30px !important; background-color: #C8C8C8 !important;
		 		border: none; z-index: 0 !important;}
	#catalog { z-index: 0 !important; } 
	h2 { padding: 10px; margin: 0px !important; height: 15px; font-size: 15px !important; z-index: 0 !important;}
	#catalog_list { height: 333px !important; display: block !important; overflow: scroll !important; background-color: white !important; z-index: 0 !important;}
	
	/* style for the prozess creating list */
	#cart {  color: black !important; float: left !important; width: 350px !important; height: auto !important; margin-right: 20px !important; display: block !important;
			box-shadow: 0 6px 12px rgba(0, 0, 0, .5) !important; border-radius: 14px !important; z-index: 0 !important;}
	#cart_list { height: 354px !important; overflow: scroll !important; background-color: white !important; z-index: 0 !important;}
	#cart ol { margin: 0px !important; padding: 0px 0px 0px 0px !important; z-index: 0 !important;}
	#cart li { height: 50px; list-style-type: none; padding-left: 20px; margin-left: 25px;
    			background-image:url('images/prozess_doc_pic.png') !important; background-size: 30px 30px !important;
   				 background-repeat:no-repeat !important; background-position:left center !important; z-index: 0 !important;}
	#recycle_bin { text-align: center !important; color: red !important; font-weight: bold !important; font-size: 15px !important; z-index: 0 !important;}
	
	/* the box for informations and input */
	#out_box { width: 400px !important; height: auto !important; padding: 0px 0px 0px 0px !important; top: 120px !important; left: 150px !important;
				position: fixed !important; background-color: silver !important; z-index: 1 !important; color: black !important;}
	
	/* buttons  */
	#save_prozess, #chg_prozess_btn, #del_prozess_btn{font-size: medium !important; border: 1px solid white !important;
					font-weight: bold !important; background: #005353 !important;
					color: white !important; -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, .5) !important;
					box-shadow: 0 6px 12px rgba(0, 0, 0, .5) !important;}
					
	#save_prozess	{position: relative !important; top: 420px !important; right: 430px !important; z-index: 0 !important;}
	#save_prozess:hover, #chg_prozess_btn:hover, #del_prozess_btn:hover {	background: #D60000 !important; color: white !important;}
	
	/* select box */
	#sel_prozesses {box-shadow: none !important; border: 1px solid #000 !important; border-radius: 5px !important; font-weight: bold !important;
					font-size: medium !important; padding: 5px !important; margin-bottom: 10px !important; color: #000 !important;}
	input { width: auto !important;}

</style>

</head>
<body>
	<input type="hidden" id="prozessNo" value="0">
	
	<div id="out_box"></div>
	
	<fieldset id="new_prozess_in">
		<label for="prozess_name">Name des Prozesses :</label>
		
		<!-- 
		<input type="text" id="prozess_name">
		<input type="button" id="new_prozess_btn" value="Erstellen" onclick="new_prozess();">
		 -->
		
		<select id="sel_prozesses" name="top5" size="1">
			<!-- automatic in "fill_doc" -->
    	</select>
		<input type="button" id="del_prozess_btn" value="Löschen" onclick="del_prozess();">
		<input type="button" id="chg_prozess_btn" value="Bearbeiten" onclick="chg_prozess();">
	</fieldset>

	<div id="new_prozess">
	
		<div id="products">
			<h1 id="headers" class="ui-widget-header">Documente</h1>
			<div id="catalog">
				<h2>Verfügbare Dateien</h2>
				<div id="catalog_list">
					<ul id="liste_files">
						<!-- automatical filled up with script (<li id="">DOC</li>) -->
					</ul>
					<form id="file_upload" enctype="multipart/form-data">
						<input type="file" id="sel_doc_file" name="sel_doc_file" onchange="file_change();">
						<input type="button" value="Upload" id="add_doc_file" onclick="add_file();">
					</form>
				</div>

			</div>
		</div>
		<div id="cart">
			<h1 id="headers" class="ui-widget-header">Prozess</h1>
			<div id="cart_list" class="ui-widget-content">         
				<div id="recycle_bin">Löschen</div>
				<ol>
					<li class="placeholder">Add your Doc's here</li>
				</ol>
			</div>
		</div>
		<input type="button" value="Speichern" id="save_prozess" onclick="save_prozess();">
	</div>
</body>

<script type="text/javascript">
var heute = new Date().toISOString().slice(0,10);
var jetzt = new Date().toISOString().slice(11,19);
var send_data;
send_sort = new Array();
send_terms = new Array();
$('#out_box').hide();
$('#new_prozess').hide();
$('#add_doc_file').hide();
//
fill_doc();
	//
	//
	function file_change(){
    	$('#add_doc_file').show();
	}
	//*******************************************
	//create a new prozess in the DB for the use
	function new_prozess(){
		name_proz = document.getElementById("prozess_name").value;
		if ("" != name_proz ){
			send_data = name_proz;
			$('#new_prozess').show();		
			$('#new_prozess_in').hide();
		}
		else alert("Bitte Namen eingeben !");
		
	}
	//*******************************************
	//fills up the documents list and make the elements dragNdrop
	function fill_doc(){
		$.ajax({
			type: "GET",
			url: "prozess_db",
			data: { action:"get_prozesses"
				  }
		})
		.done(function( data ) {	
			var part = data.split(';');						//col=2 (id;name)
			for ( var i = 0; i < part.length - 1 ; i=i+2 ){
				var new_op = document.createElement("option");
				new_op.setAttribute("value", part[0+i].trim() );
				new_op.innerHTML = part[1+i].trim();
				document.getElementById("sel_prozesses").appendChild(new_op);
			}	
			//
				$.ajax({
					type: "GET",
					url: "prozess_db",
					data: { action:"get_files"
						  }
				})
				.done(function( data ) {	
					var part = data.split(';');						//col=3 (id, name, comment)
					for ( var i = 0; i < part.length - 1 ; i=i+3 ){
						var new_li = document.createElement("li");
						new_li.setAttribute("id", "docID_" + part[0+i].trim() );
						new_li.innerHTML = part[1+i];
						document.getElementById("liste_files").appendChild(new_li);
					}
					dragNdrop();
		  		});
	  	});
	}
	//*******************************************
	//makes all draggable and droppable and also the recycelbin
	function dragNdrop(){
		//declars the draggable elements !!!
		$( "#catalog" ).accordion();
		$( "#catalog li" ).draggable({
			appendTo: "body",
			helper: "clone"
		});

		//declars the droppable elemnts inc. as sortable list !!!	
		$( "#cart ol" ).droppable({
			activeClass: "ui-state-default",
			hoverClass: "ui-state-hover",
			accept: ":not(.ui-sortable-helper)",
			drop: function( event, ui ) {
				$( this ).find( ".placeholder" ).remove();
				var id = ui.draggable.attr("id");
				//you can use one element for one time
				if ( -1 == (send_sort.join(" ").indexOf(id)) ){
					$( "<li id='"+id+"'></li>" ).html( ui.draggable.text() + " " +
							"<br><input type='button' id='btn_doc' value='Bedingung' onclick='btn_bed("+id+");'>" +
							"<input type='button' id='btn_doc' value='Download' onclick='btn_down("+id+");'>").appendTo( this );
						send_sort.push(id);
						send_terms.push( id.slice(6) );			
						send_terms.push( heute );
						send_terms.push( jetzt );
						send_terms.push( "false" );
						send_terms.push( "false" );
				}
				else{
					alert("Element im Prozess enthalten !");	
				}
			}
		}).sortable({
			items: "li:not(.placeholder)",
			tolerance: "intersect",
			connectWith: "#recycle_bin",
			sort: function() {
				// gets added unintentionally by droppable interacting with sortable
				// using connectWithSortable fixes this, but doesn't allow you to customize active/hoverClass options
				$( this ).removeClass( "ui-state-default" );
			},
			remove: function( event, ui ) {
				ui.item.remove();
				//
				//remove terms with no sorted doc's
				var org_length = send_terms.length;
				for (var i=0; i < org_length; i=i+5){
					
					for (var ii=0; ii<send_sort.length; ii++){
						var zw = "" + send_sort[ii];
							zw = zw.split("docID_");
						if (""+send_terms[i] == zw[1] ){
							for (var j=0; j<5;j++){
								send_terms.push(send_terms[j+i]);//insert at the end
							}
						}
					}
				}
				for (var j=0; j<org_length ;j++){
					send_terms.shift();//remove all without sort no.
				}
			//				
			},
			update: function( event, ui) {
				var sortedIDs = $(this).sortable( "toArray" );
				sort_list(sortedIDs);
			}
		});

		$("#recycle_bin").droppable({
			activeClass: "ui-state-default",
			hoverClass: "ui-state-hover",
			accept: "li:not(.placeholder)",
			drop: function( event, ui ) {
				
			}
		}).sortable({
			items: "li:not(.placeholder)",
			tolerance: "intersect",
			receive: function(event, ui) {
				ui.item.remove();
			}	
		});
	}		

	//*******************************************	
	//declares all the stuff for the buttons in the prozess section
	function btn_bed( id ){
		sid = id.id.split("docID_");
		out_box_bed(sid[1]);			
	}					

	function btn_down( id ){
		//only the DOC_id in the DB
		sid = id.id.split("docID_");
		open("./download_db?sid="+sid[1]);
	}
	
	//*******************************************	
	//declares all the stuff for the dialog box
	function out_box_bed(sid){
		document.getElementById("out_box").innerHTML = ""+
			"<fieldset>"+
				"<label for='date_ende'>Datum/Zeit Abgabe</label>" +
				"<input type='date' name='date_abgabe' id='date_ende' value='0000-00-00'>" +
				"<input type='time' name='time_abgabe' id='time_ende' value='00:00'><br>" +
				"<label for='validate_online'>Onlineabgabe bestätigen</label>" +
				"<input type='checkbox' name='validate_online' id='in_online' value='true'><br>" +
				"<label for='validate_post'>Posteingang bestätigen</label>" +
				"<input type='checkbox' name='validate_post' id='in_post' value='true'><br>" +
				"<input type='hidden' value="+ sid +" id='hidden_sid'>"+
				"<input type='hidden' value='true' id='hidden_new'>"+
				"<input type='button' value='Ändern' onclick='out_box_bed_close();'>" +
				"<input type='button' value='Abbrechen' onclick='out_box_bed_abbr();'>" +
			"</fieldset>";
		//prüfen ob ein term vorhanden ist... 
		for ( var x = 0; x < send_terms.length; x++ ){
			if ( send_terms[x] == sid ){
				document.getElementById("date_ende").value = send_terms[x+1];
				document.getElementById("time_ende").value = send_terms[x+2];
				document.getElementById("in_online").checked = ( send_terms[x+3].trim() == "true");
				document.getElementById("in_post").checked = ( send_terms[x+4].trim() == "true" );
				document.getElementById("hidden_new").value = "false";
			}
		}
		$( "#out_box" ).slideDown( "slow" );
	}
	
	function out_box_bed_close(){
			sid = document.getElementById("hidden_sid").value;
			prozess = document.getElementById("prozessNo").value;
			DEnd = document.getElementById("date_ende").value;
			TEnd = document.getElementById("time_ende").value;
			onlineB = (document.getElementById("in_online").checked == true);
			postB = (document.getElementById("in_post").checked == true);
			newORold = document.getElementById("hidden_new").value;
			//sortno = "1";
		if ( "false" != newORold ){
			send_terms.push(sid);
			send_terms.push(DEnd);
			send_terms.push(TEnd);
			send_terms.push(onlineB);
			send_terms.push(postB);
		}
		else{
			for ( var x = 0; x < send_terms.length; x++ ){
				if ( send_terms[x] == sid ){
					send_terms[x+1] = DEnd;
					send_terms[x+2] = TEnd;
					send_terms[x+3] = onlineB;
					send_terms[x+4] = postB;
				}
			}
		}		
		$( "#out_box" ).slideUp( "slow" );
	}
	
	function out_box_bed_abbr(){
		$( "#out_box" ).slideUp( "slow" );
		document.getElementById("out_box").innerHTML = "";	
	}
	
	function sort_list(sortedIDs){
		send_sort = sortedIDs;
	}
	
	function save_prozess(){
		$.ajax({		
			url: 'prozess_db',
			type: 'POST',
			data: { action: "sort_list",
					send_data : send_data,
					send_terms : ""+send_terms.join(";"),
					send_sort : ""+send_sort.join(";")
				},
			success: function(data) {
				alert("OK");
			},
			error: function(data) {
				alert("Fehler bei Prozess sicherung !");
			}
		});					
		
	}
	//*******************************************	
	//declares all the stuff for add doc,form, vord	
		function get_file(sid){
		$.ajax({		
			url: 'download_db',
			type: 'POST',
			data: {sid: sid},
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			success: function(data) {
			
					data_obj = window.URL.createObjectURL(data);
					document.getElementById('out_box').innerHTML = "<iframe src='"
						+ data_obj + "' scrolling='yes' width='400px'></iframe>" +
						"<input type='button' value='Close' id='out_file_close' onclick='out_file_close();'>";
					$( "#out_box" ).slideDown( "slow" );
			},
			error: function(data) {
				alert("Datei nicht gefunden !");
			}			
		});
	}

	function add_file(){
		//grab all data from the form
		var fileData = document.getElementById("sel_doc_file").files[0];
		var formData = new FormData();
		formData.append("name", fileData.name );
		formData.append("comment", "No Comment");//here we have to add a comment text line
		formData.append("file", fileData );
		$.ajax({		
			url: 'upload_db',
			type: 'POST',
			data: formData,
			contentType: false,
			processData: false,
			success: function(data) {
				alert(data);
				fill_doc();
			},
			error: function(data) {
				alert("Upload ist fehlgeschlagen");
			}			
		});
	}
	
	function del_prozess(){
		//grab all data from the form
		var IDprozess = document.getElementById("sel_prozesses").value;
		$.ajax({		
			url: 'prozess_db',
			type: 'POST',
			data: {action: "del_prozess",
				   id: IDprozess },
			success: function(data) {
				fill_doc();
			},
			error: function(data) {
				alert("Prozess nicht gefunden !");
			}			
		});		
	}
	
	function chg_prozess(){
		var IDprozess = document.getElementById("sel_prozesses").value;
		var sel_index = document.getElementById("sel_prozesses").selectedIndex;
		var sel_option = document.getElementById("sel_prozesses").options;  
		send_data = sel_option[sel_index].text;
		
		$.ajax({		
			url: 'prozess_db',
			type: 'GET',
			data: {action: "get_full_prozess",
				   id: IDprozess },
			success: function(data) {
				//ein Satz = 9 einträge... eintrag 5 u. 6 sind DateFormat
				// Dateformat => HH:mm
				temp_data = data.split(";");
	
				if ( 9 <= temp_data.length ) $("#cart ol").find( ".placeholder" ).remove();
										
				for ( var x = 0; x < temp_data.length - 1 ; x = x + 9){

					temp_data[x + 4] = "" + temp_data[x + 4]; 
					temp_data[x + 5] = "" + temp_data[x + 5]; 
					
					id = "docID_" + temp_data[x + 3].trim();
					sid = temp_data[x + 3].trim();				
					$( "<li id='"+id+"'></li>" ).html( temp_data[x + 8] + " " +
						"<br><input type='button' value='Bedingung' onclick='btn_bed("+id+");'>" +
						"<input type='button' value='Download' onclick='btn_down("+id+");'>").appendTo( "#cart ol" );
					send_sort.push(id);
					send_terms.push( sid );			
					send_terms.push( temp_data[x + 4].trim() );
					send_terms.push( temp_data[x + 5].trim() );
					send_terms.push( temp_data[x + 6].trim() );
					send_terms.push( temp_data[x + 7].trim() );
				}
				
				$('#new_prozess_in').hide();
				$('#new_prozess').show();				

			},
			error: function(data) {
				alert("Prozess nicht gefunden !");
			}			
		});
	
	}
</script>
</html>

