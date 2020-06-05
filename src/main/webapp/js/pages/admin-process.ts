import {$,baseUrl} from "../config";
import {urlParams} from "../app";
import Swal from "sweetalert2";
// @ts-ignore
import BpmnNavigatedViewer from "bpmn-js/dist/bpmn-navigated-viewer.production.min";
import "jquery-ui-dist/jquery-ui";

$(document).ready(function () {
    let dia : string = urlParams.get('dia')?.trim()!;

    let viewer = new BpmnNavigatedViewer({
        container: $('#diagram')
    });

    let possibleIds : any = [];
    let filled : any = [];

    function showDiagram(diagramXML : any) {

        viewer.importXML(diagramXML, function() {

            let eventBus = viewer.get('eventBus'),
                canvas = viewer.get('canvas'),
                elementRegistry = viewer.get('elementRegistry');

            $.each(elementRegistry._elements, function () {
                let bo = this.element.businessObject;
                if (bo.$type == "bpmn:Lane" && bo.name == 'Student') {

                    let i : number = 0;
                    let found : any;
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

            eventBus.on('element.click', function(e : any) {
                let elm = e.element;

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
    
    function checkModelEntries(found: any, i: number, canvas: any) {
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
        
        for (let j = 0; j < found.outgoing.length; j++) {
            found = found.outgoing[j].targetRef;

            if (found.lanes[0].name != "Student") {
                found = null;
                return;
            }
            checkModelEntries(found, i, canvas);
        }
	}

    function createEntry(id : any) {
        let index = $('button[data-mid='+id+']').data('index');
        Swal.fire({
            title: 'Prozesschritt hinzufügen',
            html: '<div class="row">' +
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
            allowOutsideClick: false,
            allowEscapeKey: false,
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
        });
    }

    function editEntry(id : any) {
        let index = $('button[data-mid='+id+']').data('index');
        location.href = 'admin-process-modeler.html?id=' + id+"&index="+index+'&dia='+dia;
    }

// load + show diagram
    $('#dianame').text(dia);
    $.get(baseUrl + '/bpmn/get?model='+dia, function (diagramXML) {
        $.get( baseUrl + "/processmodel/list", {
            model: dia
        }, function( data ) {
            let arr = data.split(';');
            if (arr.length > 1) {
                for (let i = 0; i < arr.length-1; i++) {
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


