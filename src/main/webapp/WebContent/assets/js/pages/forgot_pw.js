$(document).ready(function() {
	$(document).on('click', '#resetPassword', function() {
		resetpw();
	})
});

function resetpw() {
	var mail = $('#mail').val();
	$.ajax({
		type : "POST",
		url : baseUrl + "/resetPassword",
		data : {
			email: mail
		},
		success : function(result) {
			swal({
				title : "Kennwort zurückgesetzt",
				text : "Falls diese Mailadresse und bekannt ist, erhalten Sie eine Mail mit einem Link zum zurücksetzen des Passworts",
				type : "success",
				confirmButtonText : "Ok"
			}, function() {
				location.href = 'index.jsp';
			});
		},
		error : function(result) {
			swal('Diese Mailadresse ist uns nicht bekannt');
		}
	});
}