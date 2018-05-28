var expandFAQ = function() {
	$('.show').css('cursor', 'pointer');
	$('.weg').hide();
	$('.show').on('click', function() {
		$('.weg').hide();
		$(this).children('.weg').show();
	});
};
jQuery(document).ready(expandFAQ);
