import $ from "jquery";
import "bootstrap";
import "jquery-form-validator";
import "jquery-ui-dist";
import "datatables.net-bs4";
import "cookieconsent";
import _,{baseUrl} from "../config";

var expandFAQ = function() {
	$('.show').css('cursor', 'pointer');
	$('.weg').hide();
	$('.show').on('click', function() {
		$('.weg').hide();
		$(this).children('.weg').show();
	});
};
$(document).ready(expandFAQ);
