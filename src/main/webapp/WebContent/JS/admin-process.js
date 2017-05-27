$(document).ready(function () {
    // redirect if user does not have the necessary role
    if (sessionStorage['rolle'] != '1') {
       swal({
           title: "Fehler!",
           text: "Sie besitzen nicht die nötigen Rechte um diese Seite zu sehen.",
           type: "error",
           confirmButtonText: "Ok"
       }, function () {
           location.href = 'indexMockup.html';
       });
    }


});