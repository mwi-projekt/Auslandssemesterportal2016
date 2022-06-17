// import faqComponent from "faq-component.js";
import { createApp } from 'vue'
import {faqComponent} from "./faq-component.js";

const app = createApp({
    components: {'faq-component': faqComponent}
})
.mount('#app')