import {loginComponent} from "./login-component";
import {registerComponent} from "./register-component";
import {headerComponent} from "./header-component";

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
        'header-component': headerComponent
    }
}
export {layoutComponent};