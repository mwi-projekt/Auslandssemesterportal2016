$(document).ready(function () {
    // redirect if user does not have the necessary role
    if (sessionStorage['rolle'] != '1') {
       swal({
           title: "Fehler!",
           text: "Sie besitzen nicht die nötigen Rechte um diese Seite zu sehen.",
           type: "error",
           confirmButtonText: "Ok"
       }, function () {
           location.href = 'index.html';
       });
    }

    // init & logout
    $('.nutzerName').text(sessionStorage['User']);
    $('#logout').on('click', function() {
		window.location.href = "logout";
    });

    $.ajax
    ({
        type: "GET",
        url: 'http://193.196.7.215:8080/camunda/api/engine/engine/default/process-definition',
        data: {
            'latestVersion': true,
            'active': true
        },
        success: function (json){
            $.each(json, function () {
                if (this.key != 'invoice') {
                    $('#processSteps tbody').append('<tr><td>' + this.name + '</td><td><a href="admin-process.html?dia=' + this.key + '" type="button" class="btn btn-primary">Bearbeiten</a></td></tr>')
                }
            });
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader ("Authorization", "Basic ZGVtbzpQYWlzMjAxNg==");
        },
    });


});
