$(document).ready(function() {
	url = new URL(window.location.href);
	uuid_var = url.searchParams.get("uuid");
	$(document).on('click', '#savePassword', function() {
		setpw();
	})
});

function setpw() {
	var pw_var = $('#pw').val();
	var pw_repeat = $('#pw_repeat').val();
	if (pw_var === pw_repeat){
	$.ajax({
		type : "POST",
		url : baseUrl + "/updatePassword",
		data : {
			uuid: uuid_var,
			password: pw_var
		},
		success : function(result) {
			if (result == '0'){
				swal({
					title : "Fehler",
					text : "Kein passendes Nutzerkonto gefunden",
					type : "error",
					confirmButtonText : "Ok"
				}, function() {
					location.href = 'index.html';
				});	
			}
			swal({
				title : "Kennwort zurückgesetzt",
				text : "Kennwort zurückgesetzt. Sie können sich jetzt mit dem neuen Kennwort anmelden",
				type : "success",
				confirmButtonText : "Ok"
			}, function() {
				location.href = 'index.html';
			});
		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten');
		}
	});
	} else {
		swal({
			title : "Fehler",
			text : "Die Passwörter stimmen nicht überein",
			type : "error"});
	}
}