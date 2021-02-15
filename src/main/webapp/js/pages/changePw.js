import {$,baseUrl} from "../config";
import Swal from "sweetalert2";
import "bootstrap";
import "jquery-form-validator";
import "datatables.net-bs4";
import "cookieconsent";

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
				Swal.fire({
					title : "Fehler",
					text : "Kein passendes Nutzerkonto gefunden",
					icon : "error",
					confirmButtonText : "Ok"
				}).then(function(result) {
					location.href = '/';
				});
			}
			Swal.fire({
				title : "Kennwort zurückgesetzt",
				text : "Kennwort zurückgesetzt. Sie können sich jetzt mit dem neuen Kennwort anmelden",
				icon : "success",
				confirmButtonText : "Ok"
			}).then(function(result) {
				location.href = '/';
			});
		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten');
		}
	});
	} else {
		Swal.fire({
			title : "Fehler",
			text : "Die Passwörter stimmen nicht überein",
			icon : "error"});
	}
}
