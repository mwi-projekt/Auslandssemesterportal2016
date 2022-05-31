
let headerComponent = {
    template: `
        <div class="header">
          <div class="row" style="margin-left: 0; margin-right: 0">
            <header class="header-container">
              <a href="/">
                <div class="dhbwLogo-container">
                  <img id="dhbwLogo" src="images/DHBW-Logo.svg" alt="DHBW Logo" />
                </div>
              </a>
              <div class="container-buttons">
                <div>
                  <div class="logFenster">
                    <button
                      type="button"
                      class="btn btn-danger"
                      data-toggle="modal"
                      data-target="#login"
                    >
                      Login
                    </button>
                    <button
                      type="button"
                      class="btn btn-light"
                      data-toggle="modal"
                      data-target="#register"
                    >
                      Registrieren
                    </button>
                  </div>
                  <div class="logoutFenster">
                    Eingeloggt als <b class="btn btn-light nutzerName"></b>
                    <button
                      type="button"
                      class="btn btn-danger navMargin"
                      id="logout"
                      data-target="#logout"
                    >
                      Logout
                    </button>
                  </div>
                </div>
              </div>
            </header>
          </div>
        </div>
    `
}
export {headerComponent};