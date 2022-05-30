// import faqComponent from "faq-component.js";
import {faqComponent} from "./faq-component.js";

const app = Vue.createApp({
    components: {'faq-component': faqComponent}
})
.mount('#app')