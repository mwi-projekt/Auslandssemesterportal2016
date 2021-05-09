import {$} from "../config";
import "../app";

$(document).ready(function () {
    // Click Listener für die Tiles im AdminBereich
    $('.admintile').on('click', function () {
        let id = $(this).attr('id');
        if (id === 'bewerbungsprozess') {
            location.href = 'prozess.html';
        }
        if (id === 'student') {
            location.href = 'verwaltung_student.html';
        }
        if (id === 'auslandsamt') {
            location.href = 'verwaltung_auslandsamt.html';
        }
        if (id === 'studiengangsleitung') {
            location.href = 'verwaltung_studiengangsleitung.html';
        }
    });
});
