import $ from "jquery";

window.CKEDITOR_BASEPATH = '/node_modules/ckeditor4/';

//jQuerySetup
$.ajaxSetup({
    xhrFields: { withCredentials: true },
    crossDomain: true,
});

export const baseUrl = "http://localhost";
export {$};
