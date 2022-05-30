import {faqComponent} from "./faq-component.js";
import {loginComponent} from "./login-component.js";

const app = Vue.createApp({
    components: {
        'faq-component': faqComponent,
        'login-component': loginComponent
    }
})
.mount('#app')