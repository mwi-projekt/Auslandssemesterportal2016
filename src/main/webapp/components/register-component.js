
let registerComponent = {
    template: `
        <div class="modal fade" id="register" role="dialog">
          <div class="modal-dialog">
            <div class="modal-content">
              <form action="" id="regForm">
                <div class="modal-header">
                  <h4 class="modal-title">Registrierung</h4>
                  <button type="button" class="close" data-dismiss="modal">
                    &times;
                  </button>
                </div>
                <div class="modal-body mx-3">
                  <div class="student auslandsmitarbeiter">
                    <div class="md-form mb-4">
                      <label for="inVorname">Vorname</label>
                      <input
                        type="text"
                        class="inBox form-control"
                        id="inVorname"
                        required
                      />
                    </div>
                    <div class="md-form mb-4">
                      <label for="inNachname">Nachname</label>
                      <input
                        type="text"
                        class="inBox form-control"
                        id="inNachname"
                        required
                      />
                    </div>
                    <div class="md-form mb-4">
                      <label for="inMail">Email-Adresse</label>
                      <input
                        type="email"
                        placeholder="Nachname.Vorname@student.dhbw-karlsruhe.de"
                        class="inBox form-control"
                        id="inMail"
                        required
                        pattern="^[a-zA-Z0-9 -]*(\\.){0,1}[a-zA-Z0-9 -]+@(student\\.){0,1}dhbw-karlsruhe\\.de$"
                        title="Nachname.Vorname@student.dhbw-karlsruhe.de"
                      />
                    </div>
                  </div>
        
                  <div class="student">
                    <div class="md-form mb-4">
                      <label for="inMatrikel">Matrikelnummer</label>
                      <input
                        type="number"
                        class="inBox form-control"
                        id="inMatrikel"
                        required
                      />
                    </div>
                    <div class="md-form mb-4">
                      <label for="inStudiengang">Studiengang</label>
                      <select class="inBox form-control" id="inStudiengang" required>
                        <option value="Wirtschaftsinformatik">
                          Wirtschaftsinformatik
                        </option>
                        <option value="Wirtschaftsingenieurwesen">
                          Wirtschaftsingenieurwesen
                        </option>
                      </select>
                    </div>
                    <div class="md-form mb-4">
                      <label for="inKurs">Kurs</label>
                      <input
                        type="text"
                        class="inBox form-control"
                        id="inKurs"
                        required
                      />
                    </div>
                    <div class="md-form mb-4">
                      <label for="inStandort">Standort</label>
                      <select class="inBox form-control" id="inStandort" required>
                        <option value="DHBW_Karlsruhe">DHBW Karlsruhe</option>
                        <option value="DHBW_Stuttgart">DHBW Stuttgart</option>
                        <option value="DHBW_Mannheim">DHBW Mannheim</option>
                        <option value="DHBW_Mosbach">DHBW Mosbach</option>
                        <option value="DHBW_Lörrach">DHBW Lörrach</option>
                        <option value="DHBW_Ravensburg">DHBW Ravensburg</option>
                        <option value="DHBW_Heidenheim">DHBW Heidenheim</option>
                        <option value="DHBW_Villingen-Schwenningen">
                          DHBW Villingen-Schwenningen
                        </option>
                      </select>
                    </div>
        
                    <div class="md-form mb-4">
                      <label for="inPwSt1">Passwort</label>
                      <input
                        type="password"
                        class="inBox form-control"
                        id="inPwSt1"
                        required
                        pattern="(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                        title="Das Passwort muss mindestens 8 Zeichen lang sein und eine Nummer sowie einen Groß- und Kleinbuchstaben beinhalten"
                      />
                    </div>
        
                    <div class="md-form mb-4">
                      <label for="inPwSt2">Passwort wiederholen</label>
                      <input
                        type="password"
                        class="inBox form-control"
                        id="inPwSt2"
                        required
                      />
                    </div>
                  </div>
                  <p class="falsch"></p>
                </div>
                <div class="modal-footer d-flex">
                  <button
                    type="button"
                    class="btn btn-outline-secondary"
                    data-dismiss="modal"
                  >
                    Schlie&szlig;en
                  </button>
                  <button type="submit" class="btn btn-outline-primary btnReg">
                    Registrieren
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>  
    `
}
export {registerComponent};