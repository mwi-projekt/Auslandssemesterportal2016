import {$} from "../js/config";

let testComponent = {
    template: `
        <h1>{{message}}</h1>
    `,
    data () {
        return {
            message: "Test"
            }
    },
}
export {testComponent};