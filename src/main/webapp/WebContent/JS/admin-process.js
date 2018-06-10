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

    $.urlParam = function(name) {
        var results = new RegExp('[\?&]' + name + '=([^&#]*)')
            .exec(window.location.href);

        if (results == null) {
            return " ";
        } else {
            return results[1] || 0;
        }

    };

    var dia = $.urlParam('dia').trim();
    
    // init & logout
    $('.nutzerName').text(sessionStorage['User']);
    $('#logout').on('click', function() {
		window.location.href = "logout";
    });

    var BpmnJS = window.BpmnJS;

    var viewer = new BpmnJS({
        container: '#diagram'
    });

    var possibleIds = [];
    var filled = [];

    function showDiagram(diagramXML) {

        viewer.importXML(diagramXML, function() {

            var overlays = viewer.get('overlays'),
                eventBus = viewer.get('eventBus'),
                canvas = viewer.get('canvas'),
                elementRegistry = viewer.get('elementRegistry');

            console.log(elementRegistry._elements);

            $.each(elementRegistry._elements, function () {
                var bo = this.element.businessObject;
                if (bo.$type == "bpmn:Lane" && bo.name == 'Student') {

                    console.log(bo);

                    var i = 0;
                    var found;
                    $.each(bo.flowNodeRef, function () {
                        if (this.$type == "bpmn:StartEvent") {
                            found = this;
                        }
                    });
                    if (found) {
                    	checkModelEntries(found, i, canvas);
                    }
                }
            });

            viewer.get('canvas').zoom('fit-viewport');

            eventBus.on('element.click', function(e) {
                var elm = e.element;

                if (elm.type == "bpmn:UserTask" && $.inArray(elm.id, possibleIds) > -1) {
                    if ($.inArray(elm.id, filled) > -1) {
                        editEntry(elm.id);
                    } else {
                        createEntry(elm.id);
                    }
                }
            });


        });
    }
    
    function checkModelEntries(found, i, canvas) {
		if (found.$type == 'bpmn:UserTask') {
			if ($.inArray(found.id, possibleIds) !== -1) return;
            i++;
            possibleIds.push(found.id);
            if ($.inArray(found.id, filled) > -1) {
                canvas.addMarker(found.id, 'user-task');
                $('#processSteps tbody').append('<tr><td>'+found.name+'</td><td><button data-index="'+i+'" data-mid="'+found.id+'" type="button" class="btn btn-primary">Bearbeiten</button></td></tr>')
            } else {
                canvas.addMarker(found.id, 'user-task-new');
                $('#processSteps tbody').append('<tr><td>'+found.name+'</td><td><button data-index="'+i+'" data-mid="'+found.id+'" type="button" class="btn btn-success">Erstellen</button></td></tr>')
            }
        }
        if (found.outgoing.length == 0) {
            found = null;
            return;
        }
        
        for (var j = 0; j < found.outgoing.length; j++) {
            found = found.outgoing[j].targetRef;

            if (found.lanes[0].name != "Student") {
                found = null;
                return;
            }
            checkModelEntries(found, i, canvas);
        }
	}

    function createEntry(id) {
        var index = $('button[data-mid='+id+']').data('index');
        $.sweetModal({
            title: 'Prozesschritt hinzufügen',
            content: '<div class="row">' +
            '<div class="col-lg-12">' +
            '<p>Bitte wählen Sie eine Vorlage für die Modellierung des Prozessschrittes aus:</p>' +
            '<a href="#" id="newFormBtn" class="btn btn-sq-lg btn-primary btn-red">' +
            '<i class="fa fa-edit fa-4x"></i><br/>' +
            'Formular<br />' +
            'Vorlage' +
            '</a>' +
            '<a href="#" id="newDownloadBtn" class="btn btn-sq-lg btn-primary btn-red">' +
            '<i class="fa fa-download fa-4x"></i><br/>' +
            'Download<br />' +
            'Vorlage' +
            '</a>' +
            '<a href="#" id="newUploadBtn" class="btn btn-sq-lg btn-primary btn-red">' +
            '<i class="fa fa-cloud-upload fa-4x"></i><br/>' +
            'Upload<br />' +
            'Vorlage' +
            '</a>' +
            '</div>' +
            '</div>',
            blocking: true,
            width: '600px',
            onOpen: function () {
                $('#newFormBtn').click(function () {
                    location.href = 'admin-process-modeler.html?id=' + id + '&type=form'+"&index="+index+'&dia='+dia;
                });
                $('#newDownloadBtn').click(function () {
                    location.href = 'admin-process-modeler.html?id=' + id + '&type=download'+"&index="+index+'&dia='+dia;
                });
                $('#newUploadBtn').click(function () {
                    location.href = 'admin-process-modeler.html?id=' + id + '&type=upload'+"&index="+index+'&dia='+dia;
                });
            },
            //theme: $.sweetModal.THEME_DARK
        });
    }

    function editEntry(id) {
        var index = $('button[data-mid='+id+']').data('index');
        location.href = 'admin-process-modeler.html?id=' + id+"&index="+index+'&dia='+dia;
    }

// load + show diagram
    $('#dianame').text(dia);
    $.get('bpmn/get?model='+dia, function (diagramXML) {
        $.get( "processmodel/list", {
            model: dia
        }, function( data ) {
            var arr = data.split(';');
            if (arr.length > 1) {
                for (var i = 0; i < arr.length-1; i++) {
                    filled.push(arr[i]);
                }
            }
            showDiagram(diagramXML);
        });
    });

    $('#processSteps').on('click', '.btn-success', function () {
        createEntry($(this).data('mid'));
    });

    $('#processSteps').on('click', '.btn-primary', function () {
        editEntry($(this).data('mid'));
    });
    
	if($.urlParam('logout').trim()){
		sessionStorage.clear();
		window.location.href = removeQueryStringParameter('logout');
	}

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


