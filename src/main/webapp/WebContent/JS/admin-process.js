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
                        var y = 0;
                        while (found != null) {
                            if (found.$type == 'bpmn:UserTask') {
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
                                break;
                            }
                            found = found.outgoing[0].targetRef;

                            if (found.lanes[0].name != "Student") {
                                found = null;
                                break;
                            }

                            console.log(found)
                            if (y++ >= 100) break;
                        }
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

});