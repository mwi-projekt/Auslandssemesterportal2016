$(document).ready(function () {
    // redirect if user does not have the necessary role
    if (sessionStorage['rolle'] != '1') {
       swal({
           title: "Fehler!",
           text: "Sie besitzen nicht die nötigen Rechte um diese Seite zu sehen.",
           type: "error",
           confirmButtonText: "Ok"
       }, function () {
           location.href = 'indexMockup.html';
       });
    }

    // init & logout
    $('.nutzerName').text(sessionStorage['User']);
    $('#logout').on('click', function() {
        sessionStorage.clear();
        location.replace("index.html");
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

            $.each(elementRegistry._elements, function () {
                var bo = this.element.businessObject;
                if (bo.$type == "bpmn:Lane" && bo.name == 'Student') {
                    $.each(bo.flowNodeRef, function () {
                        if (this.$type == "bpmn:UserTask") {
                            possibleIds.push(this.id);
                            if ($.inArray(this.id, filled) > -1) {
                                canvas.addMarker(this.id, 'user-task');
                                $('#processSteps tbody').append('<tr><td>'+this.name+'</td><td><button data-mid="'+this.id+'" type="button" class="btn btn-primary">Bearbeiten</button></td></tr>')
                            } else {
                                canvas.addMarker(this.id, 'user-task-new');
                                $('#processSteps tbody').append('<tr><td>'+this.name+'</td><td><button data-mid="'+this.id+'" type="button" class="btn btn-success">Erstellen</button></td></tr>')
                            }
                        }
                    })
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

    function createEntry(id) {
        $.sweetModal({
            title: 'Prozesschritt hinzufügen',
            content: '<div class="row">' +
            '<div class="col-lg-12">' +
            '<p>Bitte wählen Sie eine Vorlage für die Modellierung des Prozessschrittes aus:</p>' +
            '<a href="#" id="newFormBtn" class="btn btn-sq-lg btn-primary">' +
            '<i class="fa fa-edit fa-4x"></i><br/>' +
            'Formular<br />' +
            'Vorlage' +
            '</a>' +
            '<a href="#" id="newDownloadBtn" class="btn btn-sq-lg btn-primary">' +
            '<i class="fa fa-download fa-4x"></i><br/>' +
            'Download<br />' +
            'Vorlage' +
            '</a>' +
            '<a href="#" id="newUploadBtn" class="btn btn-sq-lg btn-primary">' +
            '<i class="fa fa-cloud-upload fa-4x"></i><br/>' +
            'Upload<br />' +
            'Vorlage' +
            '</a>' +
            '</div>' +
            '</div>',
            blocking: true,
            onOpen: function () {
                $('#newFormBtn').click(function () {
                    location.href = 'admin-process-modeler.html?id=' + id + '&type=form';
                });
                $('#newDownloadBtn').click(function () {
                    location.href = 'admin-process-modeler.html?id=' + id + '&type=download';
                });
                $('#newUploadBtn').click(function () {
                    location.href = 'admin-process-modeler.html?id=' + id + '&type=upload';
                });
            },
            //theme: $.sweetModal.THEME_DARK
        });
    }

    function editEntry(id) {
        location.href = 'admin-process-modeler.html?id=' + id;
    }

// load + show diagram
    $.get('../Prozess_Student_bewerben.bpmn', function (diagramXML) {
        $.get( "processmodel/list", {
            model: 'studentBewerben'
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


});