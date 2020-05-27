import {$,baseUrl} from "../config";
import Swal from "sweetalert2";
import "bootstrap";
require("jquery-validation")($);
require("jquery-validation/dist/localization/messages_de.min");

var uuid_var;
var url;

$.validator.addMethod("pattern", function(value, element, params){
	console.log(params);
	let pattern = new RegExp(params);
	return this.optional(element) || pattern.test(value);
}, "Ungültige Zeichenfolge.");

$(document).ready(function() {
	url = new URL(window.location.href);
	uuid_var = url.searchParams.get("uuid");

	$("#formular").validate({
		debug: true
	});

	$(document).on('click', '#savePassword', function() {
		setpw();
	})
});

function setpw() {
	var pw_var = $('#pw').val();
	var pw_repeat = $('#pw_repeat').val();
	if (pw_var === pw_repeat){
		var form = $('#formular');

		if (form && !form.valid()) {
			Swal.fire('Bitte füllen sie alle Felder korrekt aus.');
			return;
		}

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
					location.href = 'index.html';
				});	
			}
			Swal.fire({
				title : "Kennwort zurückgesetzt",
				text : "Kennwort zurückgesetzt. Sie können sich jetzt mit dem neuen Kennwort anmelden",
				icon : "success",
				confirmButtonText : "Ok"
			}).then(function(result) {
				location.href = 'index.html';
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