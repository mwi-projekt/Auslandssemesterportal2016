import {$} from "../config";
import "../app";
import "bootstrap";
import "jquery-ui-dist";

let expandFAQ = function() {
	$('.show').css('cursor', 'pointer');
	$('.weg').hide();
	$('.show').on('click', function() {
		$('.weg').hide();
		$(this).children('.weg').show();
	});
};
$(document).ready(expandFAQ);
