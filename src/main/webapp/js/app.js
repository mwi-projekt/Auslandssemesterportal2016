import $ from "jquery";
import {baseUrl} from "./config.js";
import Swal from "sweetalert2";

$.ajaxSetup({
    xhrFields: { withCredentials: true },
    crossDomain: true,
});

$(document).ready(function () {
    // logout
    $('#logout').on('click', logout);

    // Überprüfen ob jemand eingeloggt ist
    if (!sessionStorage['User']) {
        $('.logoutFenster').hide();
    } else {
        $('.logFenster').hide();
        $('.portalInfo').hide();
        $('.logoutFenster').show();
        $('.nutzerName').text(sessionStorage['User']);
    }
});

//Check ob Passwörter übereinstimmen
$(document).on('keyup', '#inPwSt2', function(){
    var inPwSt1 = document.getElementById("inPwSt1")
      , inPwSt2 = document.getElementById("inPwSt2");

    if(inPwSt1.value != inPwSt2.value) {
        inPwSt2.setCustomValidity("Die Passwörter stimmen nicht überein");
    } else {
        inPwSt2.setCustomValidity('');
    }
});

$(document).on('submit', '#regForm', function (e) {
    e.preventDefault();
    var telefon = $('#inTel').val();
    var mobil = $('#inMobil').val();
    var rolle = "Studierender";

    var pw1 = $('#inPwSt1').val();
    var pw2 = $('#inPwSt2').val();
    var matrikelnummer = $('#inMatrikel').val();
    var studiengang = $('#inStudiengang').val();
    var kurs = $('#inKurs').val();
    var standort = $('#inStandort').val();
    var vorname = $('#inVorname').val();
    var nachname = $('#inNachname').val();
    var email = $('#inMail').val();

    if (pw1 === pw2) {
        if (studiengang === "Studiengang*") {
            Swal.fire({
                title: "Studiengang auswählen",
                text: "Bitte einen Studiengang aus der Liste auswählen",
                icon: "error",
                confirmButtonText: "OK"
            });
        } else if (vorname != "" && nachname != "" && email != "" && matrikelnummer != "" && kurs != "" && pw1 != "") {
            $.ajax({
                type: "POST",
                url: baseUrl + "/register",
                data: {
                    rolle: rolle,
                    passwort: pw1,
                    vorname: vorname,
                    nachname: nachname,
                    email: email,
                    matrikelnummer: matrikelnummer,
                    studiengang: studiengang,
                    kurs: kurs,
                    tel: telefon,
                    mobil: mobil,
                    standort: standort,
                },
                success: function (result) {
                    if (result == "mailError") {
                        Swal.fire({
                            title: "Mailadresse belegt",
                            text: "Die eingetragene Mailadresse wird bereits von einem Account verwendet",
                            icon: "error",
                            confirmButtonText: "OK"
                        });
                    } else if (result == "matnrError") {
                        Swal.fire({
                            title: "Matrikelnummer belegt",
                            text: "Die eingetragene Matrikelnummer wird bereits von einem Account verwendet",
                            icon: "error",
                            confirmButtonText: "OK"
                        });
                    } else {
                        Swal.fire({
                            title: "Registrierung erfolgreich",
                            text: "Bitte bestätigen deine Mailadresse (" + email + "), damit du dich im Portal einloggen kannst",
                            icon: "success",
                            confirmButtonText: "OK",
                            timer: 8000
                        });
                        $('.popUpBack').hide();
                        $('.popUpFeld').fadeOut();
                        $('.modal').fadeOut();
                        $('.modal').modal('hide');
                    }

                },
                error: function (result) {
                    Swal.fire({
                        title: "Fehler!",
                        text: "Bei der Registrierung ist ein Fehler aufgetreten",
                        icon: "error",
                        confirmButtonText: "OK"
                    });
                }
            });
        }
    }
});

$(document).on('submit', '#loginForm', function (e) {
    e.preventDefault();
    var email = $('#inEmail').val();
    var pw = $('#inPasswort').val();
    $.ajax({
        type: "POST",
        url: baseUrl + "/login",
        data: {
            email: email,
            pw: pw,
        },
        success: function (data) {
            if (data.resultCode == 2) {
                Swal.fire({
                    title: "Fehler!",
                    text: "Nutzername oder Passwort falsch",
                    icon: "error",
                    confirmButtonText: "Erneut versuchen"
                });
            } else if (data.resultCode == 3) {
                Swal.fire({
                    title: "Mailadresse bestätigen",
                    text: "Dieser Nutzer ist nicht aktiviert. Bitte bestätige zuerst deine Mailadresse",
                    icon: "error",
                    confirmButtonText: "OK"
                });
            } else if (data.resultCode == 4) {
                Swal.fire({
                    title: "Serverfehler",
                    text: "Bei der Serververbindung ist ein Fehler aufgetreten. Bitte versuche es später erneut",
                    icon: "error",
                    confirmButtonText: "OK"
                });
            } else {
                sessionStorage['rolle'] = data.rolle;
                sessionStorage['matrikelnr'] = data.matrikelnummer;
                sessionStorage['studiengang'] = data.studiengang;
                sessionStorage['User'] = email;

                if (sessionStorage['rolle'] == 2) {
                    window.location.href = 'task_overview.html'
                } else if (sessionStorage['rolle'] == 3) {
                    window.location.href = 'bewerbungsportal.html';
                } else if (sessionStorage['rolle'] == 4) {
                	window.location.href = 'task_overview_sgl.html';
                } else {
                    window.location.reload();
                }
            }

        },
        error: function (data) {
            Swal.fire("Fehler");
        }
    });
});

if (!String.prototype.startsWith) {
    String.prototype.startsWith = function (searchString, position) {
        position = position || 0;
        return this.indexOf(searchString, position) === position;
    };
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

//Function to remove certain URL Parameters
function removeQueryStringParameter(key, url) {
    if (!url) url = window.location.href;

    var hashParts = url.split('#');

    var regex = new RegExp("([?&])" + key + "=.*?(&|#|$)", "i");

    if (hashParts[0].match(regex)) {
        //REMOVE KEY AND VALUE
        url = hashParts[0].replace(regex, '$1');

        //REMOVE TRAILING ? OR &
        url = url.replace(/([?&])$/, '');

        //ADD HASH
        if (typeof hashParts[1] !== 'undefined' && hashParts[1] !== null)
            url += '#' + hashParts[1];
    }

    return url;
}

function logout() {
    $.ajax({
        type: "GET",
        url: baseUrl + "/logout",
        complete: function () {
            sessionStorage.clear();
            document.location.href = '/';
        }
    });
}
