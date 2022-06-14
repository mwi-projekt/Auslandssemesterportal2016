// import faqComponent from "faq-component.js";
import {faqComponent} from "./faq-component.js";
import {footerComponent} from "./footer-component.js";

const app1 = Vue.createApp({
    components: {
        'faq-component': faqComponent
    },
});
app1.mount('#vue-faq');

const app2 = Vue.createApp({
    components: {
        'footer-component': footerComponent,
    },
});
app2.mount('#vue-layout');