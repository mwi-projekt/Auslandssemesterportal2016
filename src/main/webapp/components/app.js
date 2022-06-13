import {faqComponent} from "./faq-component.js";
import {layoutComponent} from "./layout-component.js";

const app1 = Vue.createApp({
    components: {
        'faq-component': faqComponent
    }
})
app1.mount('#vue-faq')

const app2 = Vue.createApp({
    components: {
        'layout-component': layoutComponent,
    }
})
app2.mount('#vue-layout')