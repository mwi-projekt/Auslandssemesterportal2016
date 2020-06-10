import {$,baseUrl} from "../config";
import "../app";
import Swal from "sweetalert2";
import "bootstrap";
// @ts-ignore
require("jquery-validation")($);
// @ts-ignore
require("jquery-validation/dist/localization/messages_de.min");

let uuid_var : any;
let url;

// @ts-ignore
$.validator.addMethod("pattern", function(value, element, params){
	console.log(params);
	let pattern = new RegExp(params);
	// @ts-ignore
	return this.optional(element) || pattern.test(value);
}, "Ungültige Zeichenfolge.");

$(document).ready(function() {
	url = new URL(window.location.href);
	uuid_var = url.searchParams.get("uuid");

	// @ts-ignore
	$("#formular").validate({
		debug: true
	});

	$(document).on('click', '#savePassword', function() {
		setpw();
	})
});

function setpw() {
	let pw_var = $('#pw').val();
	let pw_repeat = $('#pw_repeat').val();
	if (pw_var === pw_repeat){
		let form = $('#formular');

		// @ts-ignore
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