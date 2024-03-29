import { createApp } from 'vue'
import faqComponent from "./faq-component.vue";
import footerComponent from "./footer-component.vue";
import forgot_pwComponent from "./forgot_pw-component.vue";
import change_pwComponent from "./change_pw-component.vue";

const app1 = createApp({
    components: {
        'faq-component': faqComponent,
    },
});
app1.mount('#vue-faq');

const app2 = createApp({
    components: {
        'footer-component': footerComponent,
    },
});
app2.mount('#vue-layout');

const app3 = createApp({
    components: {
        'forgot_pw-component': forgot_pwComponent,
    },
});
app3.mount('#vue-forgotPW');

const app4 = createApp({
    components: {
        'change_pw-component': change_pwComponent,
    },
});
app4.mount('#vue-changePW');