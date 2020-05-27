import {$} from "../config";
import "bootstrap";
import "jquery-ui-dist";

var expandFAQ = function() {
	$('.show').css('cursor', 'pointer');
	$('.weg').hide();
	$('.show').on('click', function() {
		$('.weg').hide();
		$(this).children('.weg').show();
	});
};
$(document).ready(expandFAQ);
