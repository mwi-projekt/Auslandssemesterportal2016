import { createApp } from 'vue'
import {faqComponent} from "./faq-component.js";
import {footerComponent} from "./footer-component.js";

const app1 = createApp({
    components: {
        'faq-component': faqComponent
    },
});
app1.mount('#vue-faq');

const app2 = createApp({
    components: {
        'footer-component': footerComponent,
    },
});
app2.mount('#vue-layout');