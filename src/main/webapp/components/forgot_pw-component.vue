<template>
  <!-- Mittlerer Teil -->
  <div class="row aufgaben">
    <div class="col-md-2"></div>
    <div class="col-md-8">
      <div class="nav2">
        <div class="navEl nonCms current" id="nav2"><a style="color:white"
                                                       href="index">Passwort-Wiederherstellung</a></div>
      </div>
      <div>
        <div style="width:100%" id="formular" class="form-horizontal">
          <label for="mail">Mailadresse</label>
          <input type="text" class="form-control" id="mail">
        </div>
        <div style="display:flex; flex-direction: column; text-align:center ">
          <div id="nextStep">
            <button @click="resetpw" class="btn btn-success navMargin ">Passwort zur&uuml;cksetzen</button>
          </div>
        </div>
      </div>
      <div class="iFenster iF1 col-xs-6" id="bewFormular"
           style="width: 100%; height: 100%;">
        <div style="width: 100%; margin-top: 0px;" class="container">
          <form role="form" id="frmContact">
            <div class="row">
              <div class="col-xs-12">
                <div class="pull-left"></div>
              </div>
            </div>
          </form>
        </div>
        <form class="row dat" id="bewFormular0"></form>
        <div id="einschreibungsProzess"></div>
      </div>
    </div>
  </div>
</template>

<script>
import {$, baseUrl} from "../js/config";
import Swal from "sweetalert2";

export default {
  name: "forgot_pw-component",
  methods: {
    resetpw: function () {
      var mail = $('#mail').val();
      $.ajax({
        type: "POST",
        url: baseUrl + "/resetPassword",
        data: {
          email: mail
        },
        success: function (result) {
          Swal.fire({
            title: "Kennwort zurückgesetzt",
            text: "Falls diese Mailadresse uns bekannt ist, erhalten Sie eine Mail mit einem Link zum zurücksetzen des Passworts",
            icon: "success",
            confirmButtonText: "Ok"
          }).then(function (result) {
            location.href = '/';
          });
        },
        error: function (result) {
          Swal.fire('Diese Mailadresse ist uns nicht bekannt');
        }
      });
    }
  }
}
</script>

<style scoped>

</style>