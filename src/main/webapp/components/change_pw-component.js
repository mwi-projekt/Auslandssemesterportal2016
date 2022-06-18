import {$, baseUrl} from "../js/config";
import Swal from "sweetalert2";

let change_pwComponent = {
    template: `
      <module href="./layout.html">
          <div class="row aufgaben">
            <div class="col-md-2"></div>
            <div class="col-md-8">
              <div class="nav2">
                <div class="navEl nonCms current" id="nav2"><a style="color:white" href="/">Index</a></div>
              </div>
              <div id="formular" class="form-horizontal">
                <label for="pw">Passwort</label>
                <input type="password" class="form-control" id="pw">
                <label for="pw_repeat">Wiederholen</label>
                <input type="password" class="form-control" id="pw_repeat">
              </div>
              <div id="nextStep">
                <button @click="setpw" class="btn btn-success navMargin">Speichern</button>
              </div>
              <div class="iFenster iF1 col-xs-6" id="bewFormular"
                   style="width: 100%; height: 100%;">
                <div style="width: 100%; margin-top: 0px;" class="container">
                  <form role="form" id="frmContact">
                    <div class="row">
                      <div class="col-xs-12">
                        <div class="pull-left">
                        </div>
                      </div>
                    </div>
                  </form>
                </div>
                <form class="row dat" id="bewFormular0">
                </form>
                <div id="einschreibungsProzess"></div>
              </div>
            </div>
        </div>
      </module>
    `,
    methods: {
        setpw: function () {
            var pw_var = $('#pw').val();
            var pw_repeat = $('#pw_repeat').val();
            if (pw_var === pw_repeat) {
                $.ajax({
                    type: "POST",
                    url: baseUrl + "/updatePassword",
                    data: {
                        uuid: uuid_var,
                        password: pw_var
                    },
                    success: function (result) {
                        if (result == '0') {
                            Swal.fire({
                                title: "Fehler",
                                text: "Kein passendes Nutzerkonto gefunden",
                                icon: "error",
                                confirmButtonText: "Ok"
                            }).then(function (result) {
                                location.href = '/';
                            });
                        }
                        Swal.fire({
                            title: "Kennwort zurückgesetzt",
                            text: "Kennwort zurückgesetzt. Sie können sich jetzt mit dem neuen Kennwort anmelden",
                            icon: "success",
                            confirmButtonText: "Ok"
                        }).then(function (result) {
                            location.href = '/';
                        });
                    },
                    error: function (result) {
                        alert('Ein Fehler ist aufgetreten');
                    }
                });
            } else {
                Swal.fire({
                    title: "Fehler",
                    text: "Die Passwörter stimmen nicht überein",
                    icon: "error"
                });
            }
        }
    }
}
export {change_pwComponent};