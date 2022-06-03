import {loginComponent} from "./login-component";
import {registerComponent} from "./register-component";
import {headerComponent} from "./header-component";
import {footerComponent} from "./footer-component";

let layoutComponent = {
    template: `
        <div>
            <register-component></register-component>
            <login-component></login-component>
            <header-component></header-component>
        </div>
    `,
    components: {
        'login-component': loginComponent,
        'register-component': registerComponent,
        'header-component': headerComponent,
        'footer-component': footerComponent
    }
}
export {layoutComponent};