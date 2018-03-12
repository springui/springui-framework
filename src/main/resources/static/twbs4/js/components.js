$(document).ready(function() {

    $('.btn[data-color]').each(function() {
        var button = $(this);
        var color = button.data('color');
        button.addClass(color == 'DANGER' ? 'btn-danger' : (
                        color == 'SUCCESS' ? 'btn-success' : (
                        color == 'SECONDARY' ? 'btn-secondary' : 'btn-primary')));
    });

    $('.btn[data-size]').each(function() {
        var button = $(this);
        var size = button.data('size');
        button.addClass(size == 'SMALL' ? 'btn-sm' : (
                        size == 'LARGE' ? 'btn-lg' : ''));
    });

    $('.progress-bar[data-color]').each(function() {
        var button = $(this);
        var color = button.data('color');
        button.addClass(color == 'DANGER' ? 'bg-danger' : (
                        color == 'SUCCESS' ? 'bg-success' : (
                        color == 'SECONDARY' ? 'bg-secondary' : 'bg-primary')));
    });

    $('[data-description]').each(function() {
        var component = $(this);
        var description = component.data('description');
        var tagName = component.prop("tagName").toLowerCase();
        switch (tagName) {
        default:
            component.attr('title', description);
        }
    });
});