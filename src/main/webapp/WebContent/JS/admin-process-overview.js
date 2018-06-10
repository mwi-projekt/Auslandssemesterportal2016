$(document).ready(function () {
    // redirect if user does not have the necessary role
    if (sessionStorage['rolle'] != '1') {
       swal({
           title: "Fehler!",
           text: "Sie besitzen nicht die nötigen Rechte um diese Seite zu sehen.",
           type: "error",
           confirmButtonText: "Ok"
       }, function () {
           location.href = 'index.html';
       });
    }

    // init & logout
    $('.nutzerName').text(sessionStorage['User']);
    $('#logout').on('click', function() {
		window.location.href = "logout";
    });

    $.ajax
    ({
        type: "GET",
        url: 'http://193.196.7.215:8080/camunda/api/engine/engine/default/process-definition',
        data: {
            'latestVersion': true,
            'active': true
        },
        success: function (json){
            $.each(json, function () {
                if (this.key != 'invoice') {
                    $('#processSteps tbody').append('<tr><td>' + this.name + '</td><td><a href="admin-process.html?dia=' + this.key + '" type="button" class="btn btn-primary">Bearbeiten</a></td></tr>')
                }
            });
        },
        beforeSend: function (xhr) {
            xhr.setRequestHeader ("Authorization", "Basic ZGVtbzpQYWlzMjAxNg==");
        },
    });

	$('.imgSlider').css('background-image', 'url(images/pan' + back + '.jpg)');
	loadPortalInfo();
	loop();
	// Überprüfen ob jemand eingeloggt ist
	if (isEmpty(sessionStorage['User']) === true
			|| sessionStorage['User'] === 'undefined') {
		$('.logoutFenster').hide();
	} else {
		$('.loginFenster').hide();
		$('.logFenster').hide();
		$('.loginButton').hide();
		$('.regButton').hide();
		$('.portalInfo').hide();
		$('.logoFenster').show();
		$('.logoutFenster').show();
		$('.nutzerName').text(sessionStorage['User']);
		if (sessionStorage['rolle'] === "2") {
			$('.cms').show();
			$('.nonCms').show();
			$('.Admin').hide();
			$('.Mitarbeiter').show();
			$('.portalInfo').show();
		} else if (sessionStorage['rolle'] === "1") {
			if (sessionStorage['verwaltung'] === "0"
					|| sessionStorage['verwaltung'] === undefined) {
				$('#normalBereich').hide();
				$('#adminBereich').show();
			} else if (sessionStorage['verwaltung'] === "1") {
				$('#normalBereich').show();
				$('.cms').show();
				$('#adminBereich').hide();
				$('.Admin').show();
				$('.Mitarbeiter').hide();
			} else if (sessionStorage['verwaltung'] === "2") {
				$('#adminBereich').hide();
				$('#nutzerVerwaltung').show();
				$('#normalBereich').hide();
			}
			$('.Mitarbeiter').hide();
			$('.portalInfo').show();
		} else {
			$('.cms').hide();
			$('.nonCms').show();
			$('.eingeloggt').css('display', 'inline');
		}
	}

	for (var i = 1; i < 10; i++) {
		$('.angCont' + i).hide();
		$('.c' + i + '1').show();
	}

});
