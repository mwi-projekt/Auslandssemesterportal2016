import $ from "jquery";

//jQuerySetup
$.ajaxSetup({
    xhrFields: { withCredentials: true },
    crossDomain: true,
});
window.$ = window.jQuery = $;

export const baseUrl = "http://localhost:81";
export {$};
