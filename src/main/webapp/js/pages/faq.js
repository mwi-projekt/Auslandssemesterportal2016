import $ from "jquery";

var expandFAQ = function() {
	$('.show').css('cursor', 'pointer');
	$('.weg').hide();
	$('.show').on('click', function() {
		$('.weg').hide();
		$(this).children('.weg').show();
	});
};
$(document).ready(expandFAQ);
