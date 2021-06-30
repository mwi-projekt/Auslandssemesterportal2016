import { $, baseUrl } from "../config";
import "../app";
const jsPDF = require("jspdf");
import Swal from "sweetalert2";
import "dropzone";
require("jquery-validation")($);
require("jquery-validation/dist/localization/messages_de.min");
import "jquery-ui-dist";
import imagePage1 from "../../images/Anmeldeformular_1.jpg";
import imagePage2 from "../../images/Anmeldeformular_2.jpg";

$.validator.setDefaults({
  errorElement: "span",
  errorPlacement: function (error, element) {
    error.addClass("invalid-feedback");
    element.closest(".form-group").append(error);
  },
  highlight: function (element, errorClass, validClass) {
    $(element).addClass("is-invalid").removeClass("is-valid");
  },
  unhighlight: function (element, errorClass, validClass) {
    $(element).removeClass("is-invalid").addClass("is-valid");
  },
});

var instanceID;
var uni;
var idList = [];
var typeList = [];

$(document).ready(function () {
  var url = new URL(window.location.href);
  instanceID = url.searchParams.get("instance_id");
  uni = url.searchParams.get("uni");

  parse();
});

function manipulateDOM() {
  $("#matrikelnummer").attr("readonly", true);

  if ($("#gdprCompliance").length === 1) {
    $("#gdprCompliance").change(function () {
      if ($("#gdprCompliance").prop("checked")) {
        $("#saveData").attr("disabled", false);
      } else {
        $("#saveData").attr("disabled", true);
      }
    });

    $("#saveData").attr("disabled", true);
  }
}

// Automatische Ortserkennung bei PLZ-Eingabe in den persönlichen Daten
$(document).on("keyup change", "#bewPLZ", function (e) {
  if ($(this).val().length > 4) {
    var ort = $("#bewOrt");
    $.getJSON(
      "https://secure.geonames.org/postalCodeLookupJSON?&country=DE&username=mwidhbw&callback=?",
      { postalcode: this.value },
      function (response) {
        if (
          response &&
          response.postalcodes.length &&
          response.postalcodes[0].placeName
        ) {
          ort.val(response.postalcodes[0].placeName);
        }
      }
    );
  } else {
    $("#bewOrt").val("");
  }
});

//Automatische Download des ausgefüllten Anmeldeformulars
$(document).on("click", '[href="#downloadAnmeldeformular"]', function (e) {
  e.preventDefault();
  console.log("Download");

  $.ajax({
    type: "GET",
    url: baseUrl + "/getVariables",
    data: {
      instance_id: instanceID,
    },
    success: function (result) {
      console.log("neueVersionOnline");
      console.log("result");
      console.log(result);

      const image1 = new Image();
      image1.src = imagePage1;
      const image2 = new Image();
      image2.src = imagePage2;
      var doc = new jsPDF();
      doc.addImage(image1, "JPEG", 0, 0, 210, 297);

      //Hochschule aus URL
      var url_string = window.location.href;
      var url = new URL(url_string);
      var uni = url.searchParams.get("uni");
      //Bulgarien
      if (uni == "American University in Bulgaria (Bulgarien)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
        //Schottland
      } else if (uni == "Abertay University of Dundee (Schottland)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
        //San Marcos
      } else if (uni == "California State University San Marcos (USA)") {
        var absatz = uni.indexOf("San");
        var stringUni =
          uni.slice(0, absatz - 1) + "\n" + uni.slice(absatz - 1, uni.length);
        //Channel Islands
      } else if (uni == "California State University Channel Islands (USA)") {
        var absatz = uni.indexOf("Channel");
        var stringUni =
          uni.slice(0, absatz - 1) + "\n" + uni.slice(absatz - 1, uni.length);
        //Costa Rica
      } else if (uni == "Costa Rica Institute of Technology (Costa Rica)") {
        var absatz = uni.indexOf("Technology");
        var stringUni =
          uni.slice(0, absatz - 1) + "\n" + uni.slice(absatz - 1, uni.length);
        //Suedarfika
      } else if (uni == "Durban University of Technology (Suedafrika)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
        //Finnland
      } else if (
        uni == "South-Eastern Finland University of Applied Sciences (Finnland)"
      ) {
        var absatz1 = uni.indexOf("of");
        var absatz2 = uni.indexOf("(");
        var stringUni =
          uni.slice(0, absatz1 - 1) +
          "\n" +
          uni.slice(absatz1 - 1, absatz2 - 1) +
          "\n" +
          uni.slice(absatz2 - 1, uni.length);
        //Polen
      } else if (uni == "Technischen Universität Lodz (Polen)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
      } else {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
      }

      //Hochschule aus URL
      var url_string = window.location.href;
      var url = new URL(url_string);
      var uni = url.searchParams.get("uni");
      //Bulgarien
      if (uni == "American University in Bulgaria (Bulgarien)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
        //Schottland
      } else if (uni == "Abertay University of Dundee (Schottland)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
        //San Marcos
      } else if (uni == "California State University San Marcos (USA)") {
        var absatz = uni.indexOf("San");
        var stringUni =
          uni.slice(0, absatz - 1) + "\n" + uni.slice(absatz - 1, uni.length);
        //Channel Islands
      } else if (uni == "California State University Channel Islands (USA)") {
        var absatz = uni.indexOf("Channel");
        var stringUni =
          uni.slice(0, absatz - 1) + "\n" + uni.slice(absatz - 1, uni.length);
        //Costa Rica
      } else if (uni == "Costa Rica Institute of Technology (Costa Rica)") {
        var absatz = uni.indexOf("Technology");
        var stringUni =
          uni.slice(0, absatz - 1) + "\n" + uni.slice(absatz - 1, uni.length);
        //Suedarfika
      } else if (uni == "Durban University of Technology (Suedafrika)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
        //Finnland
      } else if (
        uni == "South-Eastern Finland University of Applied Sciences (Finnland)"
      ) {
        var absatz1 = uni.indexOf("of");
        var absatz2 = uni.indexOf("(");
        var stringUni =
          uni.slice(0, absatz1 - 1) +
          "\n" +
          uni.slice(absatz1 - 1, absatz2 - 1) +
          "\n" +
          uni.slice(absatz2 - 1, uni.length);
        //Polen
      } else if (uni == "Technischen Universität Lodz (Polen)") {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
      } else {
        var klammer = uni.indexOf("(");
        var stringUni =
          uni.slice(0, klammer - 1) + "\n" + uni.slice(klammer, uni.length);
      }

      var nationalität = result.bewLand;
      var zeitraum = result.zeitraum;
      var semester = result.bewSemester;
      var vorname = result.bewVorname;
      var strasse = result.bewStrasse;
      var name = result.bewNachname;
      var studiengang = result.bewStudiengang;
      var kurs = result.bewKurs;
      var mail = result.bewEmail;
      var uni1 = result.uni1;
      var uni2 = result.uni2;
      var uni3 = result.uni3;
      var bewGeburtsdatum = result.bewGeburtsdatum;
      var erasmus = result.bewErasmus;

      var SGL = result.bewSGL;
      var learningAgreement = result.bewLA;
      doc.setFontSize(11);
      doc.setTextColor(92, 76, 76);
      doc.text(20, 66, name);
      doc.text(107, 66, vorname);
      doc.text(20, 79, bewGeburtsdatum);
      doc.text(107, 79, nationalität);
      doc.text(20, 91, strasse);
      doc.text(107, 91, result.bewPLZ + ", " + result.bewOrt);
      doc.text(20, 104, mail);
      doc.text(107, 104, result.bewTelefon);
      doc.text(20, 116, kurs);
      doc.text(107, 116, semester);
      doc.text(20, 128, studiengang);
      doc.text(107, 128, zeitraum);

      if (erasmus == "Nein") {
        doc.text(174, 200, "X");
      } else if (erasmus == "Ja") {
        doc.text(152, 200, "X");
      }
      // Abstimmung Semesterzeit
      doc.text(152, 206, "X");

      if (SGL == "Nein") {
        doc.text(174, 212, "X");
      } else if (SGL == "Ja") {
        doc.text(152, 212, "X");
      }

      if (learningAgreement == "Nein") {
        doc.text(174, 218, "X");
      } else if (learningAgreement == "Ja") {
        doc.text(152, 218, "X");
      }

      var uni1kurz = uni1.split("(");
      doc.text(36, 153, uni1kurz[0]);
      var land1 = uni1.split("(")[1].trim().replace(")", "");
      doc.text(147, 153, land1);

      try {
        if (uni2 != "" || uni2 != null) {
          var uni2kurz = uni2.split("(");
          doc.text(36, 168, uni2kurz[0]);
          var land2 = uni2.split("(")[1].trim().replace(")", "");
          doc.text(147, 168, land2);
        }
        if (uni3 != "" || uni3 != null) {
          var uni3kurz = uni3.split("(");
          doc.text(36, 183, uni3kurz[0]);
          var land3 = uni3.split("(")[1].trim().replace(")", "");
          doc.text(147, 183, land3);
        }
      } catch (e) {
        console.log(e);
      }

      //__ Bringt das Datum in ein schönes Format
      var today = new Date();
      var dd = String(today.getDate()).padStart(2, "0");
      var mm = String(today.getMonth() + 1).padStart(2, "0"); //January is 0!
      var yyyy = today.getFullYear();
      today = dd + "." + mm + "." + yyyy;
      //_______________________________________________

      doc.addPage();
      doc.addImage(image2, "JPEG", 0, 0, 210, 297);
      doc.text(19, 29, result.bewOrt);
      doc.text(74, 29, today);
      doc.save("Anmeldeformular.pdf");
    },
    error: function (result) {
      alert("Ein Fehler ist aufgetreten");
    },
  });
});

// Automatische Ortserkennung bei PLZ-Eingabe in Unternehmensdaten
$(document).on("keyup change", "#untPLZ", function (e) {
  if ($(this).val().length > 4) {
    var ort = $("#untOrt");
    $.getJSON(
      "https://secure.geonames.org/postalCodeLookupJSON?&country=DE&username=mwidhbw&callback=?",
      { postalcode: this.value },
      function (response) {
        if (
          response &&
          response.postalcodes.length &&
          response.postalcodes[0].placeName
        ) {
          ort.val(response.postalcodes[0].placeName);
        }
      }
    );
  } else {
    $("#untOrt").val("");
  }
});

var json;

function parse() {
  $.ajax({
    type: "GET",
    url: baseUrl + "/currentActivity",
    data: {
      instance_id: instanceID,
      uni: uni,
    },
    success: function (result) {
      var output = "";
      var fileID = "";
      var fileName = "";
      var step_id = result.active;
      var model = result.data;
      if (step_id === "datenPruefen") {
        location.href =
          "task_detail.html?instance_id=" +
          instanceID +
          "&uni=" +
          uni +
          "&send_bew=true";
      }
      $.ajax({
        type: "GET",
        url: baseUrl + "/processmodel/get",
        data: {
          model: model,
          step: step_id,
        },
        success: function (result) {
          // generate html output
          json = JSON.parse(decodeURI(result));
          for (var i = 0; i < json.length; i++) {
            var type = json[i]["type"];
            switch (type) {
              case "title":
                output = output + "<h1>" + json[i]["content"] + "</h1><br>";
                break;
              case "subtitle":
                output = output + "<h3>" + json[i]["content"] + "</h3><br>";
                break;
              case "paragraph":
                output = output + "<p>" + json[i]["content"] + "</p><br>";
                break;
              case "form-select":
                var req = "";
                if (json[i]["data"]["required"] == true) {
                  req = ' required="required"';
                }
                output =
                  output +
                  '<div class="form-group"><label class="col-sm-2 control-label">' +
                  json[i]["data"]["label"] +
                  '</label><div class="col-sm-10"><select class="form-control" name="' +
                  json[i]["data"]["id"] +
                  '" id="' +
                  json[i]["data"]["id"] +
                  '"' +
                  req +
                  ">";
                for (var j = 0; j < json[i]["data"]["values"].length; j++) {
                  output =
                    output +
                    "<option>" +
                    json[i]["data"]["values"][j] +
                    "</option>";
                }
                output = output + "</select></div></div>";
                idList.push(json[i]["data"]["id"]);
                typeList.push("text");
                break;
              case "form-text":
                var req = "";
                if (json[i]["data"]["required"] == true) {
                  req = ' required="required"';
                }
                output =
                  output +
                  '<div class="form-group"><label class="col-sm-2 control-label">' +
                  json[i]["data"]["label"] +
                  ' </label><div class="col-sm-10"><input class="form-control" type="' +
                  json[i]["data"]["type"] +
                  '" ';

                if (json[i]["data"]["numchars"]) {
                  output += 'maxlength="' + json[i]["data"]["numchars"] + '" ';
                }
                output +=
                  'name="' +
                  json[i]["data"]["id"] +
                  '" id="' +
                  json[i]["data"]["id"] +
                  '"' +
                  req +
                  "></div></div>";

                idList.push(json[i]["data"]["id"]);
                typeList.push(json[i]["data"]["type"]);
                break;
              case "form-checkbox":
                var req = "";
                if (json[i]["data"]["required"] == true) {
                  req = ' required="required"';
                }
                output =
                  output +
                  '<div class="form-group"><div class="col-sm-offset-2 col-sm-10"><div class="checkbox"><label><input name="' +
                  json[i]["data"]["id"] +
                  '" type="checkbox" id="' +
                  json[i]["data"]["id"] +
                  '"' +
                  req +
                  ">" +
                  json[i]["data"]["label"] +
                  " </label></div></div></div>";
                idList.push(json[i]["data"]["id"]);
                typeList.push("boolean");
                break;
              case "form-upload":
                fileID = json[i]["data"]["id"];
                fileName = json[i]["data"]["filename"];
                output =
                  output +
                  '<div class="dropzone" id="' +
                  json[i]["data"]["id"] +
                  '"></div>';
                break;
            }
          }

          //Doesn't allow to write numbers in input fields with type='text
          $(function () {
            $("#bewVorname").keydown(function (e) {
              if (e.ctrlKey || e.altKey) {
                e.preventDefault();
              } else {
                var key = e.keyCode;
                if (
                  !(
                    key == 8 ||
                    key == 32 ||
                    key == 46 ||
                    (key >= 35 && key <= 40) ||
                    (key >= 65 && key <= 90)
                  )
                ) {
                  e.preventDefault();
                }
              }
            });

            $("#bewTelefon").keypress(function (e) {
              try {
                // telefonnummer validation
                if (
                  (this.value.includes("/") && e.keyCode == 47) ||
                  (this.value.includes(" ") && e.keyCode == 32) ||
                  (this.value.includes(" ") && e.keyCode == 47) ||
                  (this.value.includes("/") && e.keyCode == 32) ||
                  !((e.keyCode <= 57 && e.keyCode >= 46) || e.keyCode == 32)
                ) {
                  e.preventDefault();
                }
              } catch (error) {}
            });

            $("#bewGeburtsdatum").keydown(function (e) {
              try {
                if (
                  !(
                    e.keyCode == 37 ||
                    e.keyCode == 39 ||
                    e.keyCode == 8 ||
                    e.keyCode == 9
                  )
                ) {
                  var code = e.keyCode;
                  var leng = this.value.length;
                  var allowedCharacters = [
                    46, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 96, 97, 98, 99,
                    100, 101, 102, 103, 104, 105, 190,
                  ];
                  var isValidInput = false;
                  for (var i = allowedCharacters.length - 1; i >= 0; i--) {
                    if (allowedCharacters[i] == code) {
                      isValidInput = true;
                    }
                  }

                  if (
                    isValidInput ===
                      false /* Can only input 1,2,3,4,5,6,7,8,9 or . */ ||
                    (code == 190 &&
                      (leng < 2 || leng > 5 || leng == 3 || leng == 4)) ||
                    ((leng == 2 || leng == 5) &&
                      code !== 190) /* only can hit a . for 3rd pos. */ ||
                    leng == 10
                  ) {
                    /* only want 10 characters "12.45.7890" */
                    e.preventDefault();
                    return;
                  }
                }
              } catch (error) {}
            });

            $("#bewNachname").keydown(function (e) {
              if (e.ctrlKey || e.altKey) {
                e.preventDefault();
              } else {
                var key = e.keyCode;
                if (
                  !(
                    key == 8 ||
                    key == 32 ||
                    key == 46 ||
                    (key >= 35 && key <= 40) ||
                    (key >= 65 && key <= 90)
                  )
                ) {
                  e.preventDefault();
                }
              }
            });
            $("#bewOrt").keydown(function (e) {
              if (e.ctrlKey || e.altKey) {
                e.preventDefault();
              } else {
                var key = e.keyCode;
                if (
                  !(
                    key == 8 ||
                    key == 32 ||
                    key == 46 ||
                    (key >= 35 && key <= 40) ||
                    (key >= 65 && key <= 90)
                  )
                ) {
                  e.preventDefault();
                }
              }
            });

            $("#untOrt").keydown(function (e) {
              if (e.ctrlKey || e.altKey) {
                e.preventDefault();
              } else {
                var key = e.keyCode;
                if (
                  !(
                    key == 8 ||
                    key == 32 ||
                    key == 46 ||
                    (key >= 35 && key <= 40) ||
                    (key >= 65 && key <= 90)
                  )
                ) {
                  e.preventDefault();
                }
              }
            });
          });

          // set content
          document.getElementById("formular").innerHTML = output;
          if (idList.length > 0) {
            getData();
            manipulateDOM();
          }

          var $dropzone;
          // init upload
          for (var i = 0; i < json.length; i++) {
            var type = json[i]["type"];
            if (type == "form-upload") {
              console.log(json[i]["data"]["id"]);
              console.log(json[i]["data"]["filename"]);
              $dropzone = $("#" + json[i]["data"]["id"]).dropzone(
                getDropzoneOptions(
                  json[i]["data"]["id"],
                  json[i]["data"]["filename"]
                )
              );
            }
          }

          /*
           * Wenn eine Bewerbung vom SGL an den Student zurückgeschickt (zur Überarbeitung), werden mit dieser Funktionen neu hochgeladen (ein Student muss die Dokumente nicht erneut hochladen)
           */
          $.ajax({
            type: "GET",
            url: baseUrl + "/getProcessFile",
            data: {
              instance_id: instanceID,
              key: fileID,
            },
            success: function (result) {
              var byteNumbers = new Array(result.length);
              for (var i = 0; i < result.length; i++) {
                byteNumbers[i] = result.charCodeAt(i);
              }
              var byteArray = new Uint8Array(byteNumbers);
              var blob = new Blob([byteArray], { type: "application/pdf" });
              var file = new File([blob], fileName, {
                type: "application/pdf",
              });
              $dropzone[0].dropzone.addFile(file);
            },
          });

          $("#formular")
            .submit(function (e) {
              e.preventDefault();
            })
            .validate({
              debug: true,
            });
        },
        error: function (result) {
          alert("Ein Fehler ist aufgetreten: " + result);
        },
      });
    },
    error: function (result) {
      alert(
        "Ein Fehler ist aufgetreten. Aktiver Schritt konnte nicht abgerufen werden."
      );
    },
  });
}

$(document).on("click", "#saveData", function () {
  var form = $("#formular");

  if (form && !form.valid()) {
    Swal.fire({
      title: "Bitte fülle alle Felder korrekt aus.",
      icon: "warning",
      confirmButtonColor: "#28a745",
    });
    return;
  }

  for (var i = 0; i < json.length; i++) {
    var type = json[i]["type"];
    switch (type) {
      case "form-upload":
        var dropzoneForm = $("#" + json[i]["data"]["id"])[0].dropzone;
        if (
          !dropzoneForm.getAcceptedFiles() ||
          dropzoneForm.getAcceptedFiles().length <= 0
        ) {
          Swal.fire({
            title: "Bitte lade zunächst eine Datei hoch.",
            icon: "warning",
            confirmButtonColor: "#28a745",
          });
          return;
        }
    }
  }

  var keyString = "";
  var valString = "";
  var typeString = "";
  console.log(idList);
  for (var j = 0; j < idList.length; j++) {
    if ($("#" + idList[j]).attr("type") == "checkbox") {
      var checkedString = document.getElementById(idList[j]).checked
        ? "true"
        : "false";
      keyString = keyString + idList[j] + "|";
      valString = valString + checkedString + "|";
      typeString = typeString + typeList[j] + "|";
    } else {
      keyString = keyString + idList[j] + "|";
      valString = valString + document.getElementById(idList[j]).value + "|";
      typeString = typeString + typeList[j] + "|";
    }
  }
  keyString = keyString.substr(0, keyString.length - 1);
  valString = valString.substr(0, valString.length - 1);
  typeString = typeString.substr(0, typeString.length - 1);
  console.log(keyString);
  console.log(valString);
  $.ajax({
    type: "POST",
    url: baseUrl + "/setVariable",
    data: {
      instance_id: instanceID,
      key: keyString,
      value: valString,
      type: typeString,
    },
    success: function (result) {
      location.reload();
    },
    error: function (result) {
      alert("Ein Fehler ist aufgetreten");
    },
  });
});

// warnung bzgl. Datenverlust bei Nutzung des Zurück-Buttons im Browser

/*$(window).addEventListener("beforeunload", function (event) {
  debugger;
  event.alert("hilfe");
  Swal.fire({
    icon: 'warning',
    title: 'Deine Dateien werden nicht gespeichert!',
    text: 'Zur Zeit können wir leider keine Daten zwischenspeichern, aber wir arbeiten daran. Danke für Dein Verständnis!',
    reverseButtons: true,
    showCancelButton: true,
  });
});*/

function getData() {
  var keyString = "";
  for (var l = 0; l < idList.length; l++) {
    keyString = keyString + idList[l] + "|";
  }
  keyString = keyString.substr(0, keyString.length - 1);

  $.ajax({
    type: "GET",
    url: baseUrl + "/getVariables",
    data: {
      instance_id: instanceID,
    },
    success: function (result) {
      $.each(result, function (key, value) {
        $("#" + key).val(value);
      });
    },
    error: function (result) {
      alert("Ein Fehler ist aufgetreten");
    },
  });
}

function getDropzoneOptions(action, fileName) {
  return {
    url: baseUrl + "/upload",
    acceptedFiles: "application/pdf",
    maxFilesize: 16,
    method: "post",
    addRemoveLinks: true,
    withCredentials: true,
    uploadMultiple: false,

    sending: function (file, xhr, formData) {
      formData.append("action", action);
      formData.append("instance", instanceID);
    },
    accept: function (file, done) {
      done();
    },
    error: function (file, response) {
      if ($.type(response) === "string") {
        var message = response;
      } else {
        var message = response.message;
      }

      Swal.fire({
        title: "Fehler",
        text: message,
        icon: "error",
        confirmButtonColor: "#28a745",
      });
      this.removeFile(file);
    },
  };
}
