$(function () {
    var includes = $('[data-include]');
    jQuery.each(includes, function () {
        var file = '/../../partials/' + $(this).data('include') + '.html';
        $(this).load(file);
    });
});