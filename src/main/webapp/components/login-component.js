
let loginComponent = {
    template: `
        <div id="login" class="modal fade" role="dialog">
          <div class="modal-dialog">
            <div class="modal-content">
              <form id="loginForm" action="">
                <div class="modal-header">
                  <h4 class="modal-title">Login</h4>
                  <button type="button" class="close" data-dismiss="modal">
                    &times;
                  </button>
                </div>
                <div class="modal-body mx-3">
                  <div class="md-form mb-4">
                    <label data-error="wrong" data-success="right" for="inEmail"
                      >Deine Email-Adresse</label
                    >
                    <input
                      type="email"
                      placeholder="E-Mail Adresse"
                      id="inEmail"
                      class="inBox form-control"
                      required
                      autofocus
                    />
                  </div>
                  <div class="md-form mb-5">
                    <label data-error="wrong" data-success="right" for="inPasswort"
                      >Dein Passwort</label
                    >
                    <input
                      type="password"
                      placeholder="Passwort"
                      id="inPasswort"
                      class="inBox form-control"
                      required
                    />
                    <p
                      id="falschLogin"
                      style="color: red; margin-bottom: 0px; display: none"
                    >
                      Falsche Eingabe. Bitte versuchen sie es erneut.
                    </p>
                    <div style="margin-top: 5px; float: right">
                      <a href="forgot_pw.html">Passwort vergessen?</a>
                    </div>
                  </div>
                </div>
                <div class="modal-footer d-flex">
                  <button
                    type="button"
                    class="btn btn-outline-secondary"
                    data-dismiss="modal"
                  >
                    Schlie&szlig;en
                  </button>
                  <button
                    type="submit"
                    class="btn btn-outline-primary"
                    form="loginForm"
                    id="btnLogin"
                  >
                    Login
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
    `,
    data () {
        return {
            }
    },
    methods: {

    },
    created: function () {

    },
    mounted: function () {

    }
}
export {loginComponent};