import {$,baseUrl} from "../config";
import {urlParams} from "../app";
import Swal from "sweetalert2";
// @ts-ignore
import Viewer from "bpmn-js";
import "jquery-ui-dist";
import "bootstrap-switch";
import "../jquery.dynamicdom";
// @ts-ignore
import selectform from "../../modals/select-form.html";
// @ts-ignore
import textform from "../../modals/text-form.html";
// @ts-ignore
import checkboxform from "../../modals/checkbox-form.html";
// @ts-ignore
import uploadform from "../../modals/upload-form.html";

let siteHasUnsavedChanges = false;

window.onbeforeunload = function(e : any) {
	if(siteHasUnsavedChanges) {
	return 'Sie sind dabei die Seite zu verlassen.\n Möchten Sie wirklich fortfahren?';
	} else {
		return null;
	}
};

$(document).ready(function () {

        let id = urlParams.get('id');
        let dia = urlParams.get('dia');
        let type = urlParams.get('type');
        let index = urlParams.get('index');
    let json = {};

    $.get(baseUrl + '/processmodel/get', {
        model: dia,
        step: id
    }, function (data) {
        init(JSON.parse(decodeURI(data)));
    }).fail(function () {
        if (type == 'upload') {
            init([
                {
                    content: "Upload",
                    type: "title"
                }
            ]);

        } else if (type == 'download') {
            init([
                {
                    content: "Download",
                    type: "title"
                }
            ]);

        } else {
            init([
                {
                    content: "Page Title",
                    type: "title"
                }, {
                    content: "Bla",
                    type: "paragraph"
                }
            ]);
        }
    });


    $('#uebersicht').click(function() {
    	location.href="admin-process.html?dia="+dia;
    })

    $('#save').click(function () {
        $.post(baseUrl + '/processmodel/save', {
            model: dia,
            step: id,
            stepnumber: index,
            json: encodeURI(JSON.stringify(json))
        }, function (data) {
            Swal.fire(
                'Gespeichert!',
                'Der Prozessschritt wurde erfolgreich abgespeichert.',
                'success'
            )
        });
        
        siteHasUnsavedChanges = false;
    });

    function openCheckboxPopup(data : any, cb : any, cbClose : any) {
    	siteHasUnsavedChanges = true;
        let success = false;
        Swal.fire({
            title: 'Checkbox hinzufügen',
            html: checkboxForm,
            allowOutsideClick: false,
            allowEscapeKey: false,
            onClose: function () {
                if (!success) cbClose();
            },
            onOpen: function () {

                if (data.label) {
                    $('#field-label').val(data.label);
                    $('#demo-label').text(data.label);
                }

                if (data.id) {
                    $('#field-id').val(data.id);
                }


                $('#field-label').on('change keydown', function () {
                    $('#demo-label').text($(this).val()!.toString);
                });

                $('#field-save').on('click', function () {
                    let data = {
                        data: {
                            label: $('#field-label').val(),
                            id: $('#field-id').val(),
                        }
                    };
                    success = true;
                    cb(data);
                    $('.sweet-modal-close-link').trigger('click');
                });
            },
        });
    }

    function openSelectFormPopup(data : any, cb : any, cbClose : any) {
    	siteHasUnsavedChanges = true;
        let success = false;
        Swal.fire({
            title: 'Auswahlfeld hinzufügen',
            html: selectForm,
            allowOutsideClick: false,
            allowEscapeKey: false,
            onClose: function () {
                if (!success) cbClose();
            },
            onOpen: function () {
                function updateDemoSelect() {
                    $('#demo').empty();
                    $('#field-values').children().each(function () {
                        $('#demo').append('<option>'+$(this).find('span').text()+'</option>');
                    });
                }

                function addListItem(val : any) {
                    $('#field-values').append('<li class="list-group-item"><span>'+val+'</span><i class="fa fa-close"></i></li>');
                }

                if (data.values) {
                    $.each(data.values, function () {
                        addListItem(this);
                    });
                    updateDemoSelect();
                }

                if (data.label) {
                    $('#field-label').val(data.label);
                    $('#demo-label').text(data.label);
                }

                if (data.required) {
                    $('#field-req ')[0].setAttribute("checked", data.required);
                }

                $("[switch]").bootstrapSwitch({
                    onText: 'Ja',
                    offText: 'Nein'
                });

                if (data.id) {
                    $('#field-id').val(data.id);
                }

                $('#field-label').on('change keydown', function () {
                    $('#demo-label').text($(this).val()!.toString);
                });

                $('#field-val').on('keydown', function (event) {
                    if(event.keyCode == 13){
                        $('#add-btn').click();
                    }
                });

                $('#add-btn').on('click', function () {
                    if ($('#field-val').val() != '') {
                        addListItem($('#field-val').val());
                        $('#field-val').val('');
                        updateDemoSelect();
                    }
                });

                $('#field-values').on('click', '.fa-close', function () {
                    $(this).closest('li').remove();
                    updateDemoSelect();
                });

                $('#field-save').on('click', function () {
                    let values : any = [];
                    $('#field-values').children().each(function () {
                        values.push($(this).find('span').text());
                    });
                    let data = {
                        data: {
                            values: values,
                            label: $('#field-label').val(),
                            id: $('#field-id').val(),
                            required: $('#field-req')[0].getAttribute("checked")
                        },
                        content: $('#demo').html()
                    };
                    success = true;
                    cb(data);
                    $('.sweet-modal-close-link').trigger('click');
                });
            },
        });
    }

    function openUploadPopup(data : any, cb : any, cbClose : any) {
    	siteHasUnsavedChanges = true;
        let success = false;
        Swal.fire({
            title: 'Upload hinzufügen',
            html: uploadForm,
            allowOutsideClick: false,
            allowEscapeKey: false,
            onClose: function () {
                if (!success) cbClose();
            },
            onOpen: function () {

                if (data.filename) {
                    $('#field-filename').val(data.filename);
                }

                if (data.id) {
                    $('#field-id').val(data.id);
                }

                $('#field-save').on('click', function () {
                    let data = {
                        data: {
                            filename: $('#field-filename').val(),
                            id: $('#field-id').val()
                        },
                        content: $('#demo').html()
                    };
                    success = true;
                    cb(data);
                    $('.sweet-modal-close-link').trigger('click');
                });
            },
        });
    }

    function openTextInputPopup(data : any, cb : any, cbClose : any) {
    	siteHasUnsavedChanges = true;
        let success = false;
        Swal.fire({
            title: 'Textfeld hinzufügen',
            html: textForm,
            allowOutsideClick: false,
            allowEscapeKey: false,
            onClose: function () {
                if (!success) cbClose();
            },
            onOpen: function () {

                if (data.id) {
                    $('#field-id').val(data.id);
                }

                if (data.numchars) {
                	$('#field-numchars').val(data.numchars);
                }

                if (data.label) {
                    $('#field-label').val(data.label);
                    $('#demo-label').text(data.label);
                }

                if (data.type) {
                    $('#field-type').val(data.type);
                }

                if (data.required) {
                    $('#field-req')[0].setAttribute("checked",data.required);
                }

                $("[switch]").bootstrapSwitch({
                    onText: 'Ja',
                    offText: 'Nein'
                });

                $('#field-label').on('change keydown', function () {
                    $('#demo-label').text($(this).val()!.toString);
                });

                $('#field-save').on('click', function () {
                    let data = {
                        data: {
                            label: $('#field-label').val(),
                            type: $('#field-type').val(),
                            numchars: $('#field-numchars').val(),
                            id: $('#field-id').val(),
                            required: $('#field-req')[0].getAttribute("checked")
                        }
                    };
                    success = true;
                    cb(data);
                    $('.sweet-modal-close-link').trigger('click');
                });
            },
        });
    }

    let selectForm = selectform;

    let textForm = textform;

    let checkboxForm = checkboxform;

    let uploadForm = uploadform;


    function init(data : any) {
        // @ts-ignore
        $('#cardSlots').dynamicdom({

            // initalize
            data: data,

            // on change update output element
            onchange: function(output : any) {
                json = output;
//                siteHasUnsavedChanges = true;
            },

            onedit: function ($elm : any, type: any, cb: any, self: any) {
                if (type == 'form-select') {
                    openSelectFormPopup($elm.data('cdata'), function (data: any) {
                        self.settings.oninit($elm, data, data.data, cb, self);
                    }, function () {
                    });
                } else if (type == 'form-text') {
                    openTextInputPopup($elm.data('cdata'), function (data: any) {
                        self.settings.oninit($elm, data, data.data, cb, self);
                    }, function () {
                    });
                } else if (type == 'form-checkbox') {
                    openCheckboxPopup($elm.data('cdata'), function (data: any) {
                        data.content = '<div class="form-group"><label><input type="checkbox" /> '+ data.data.label +'</label></div>';
                        data.editor = false;
                        cb(self, $elm, data);
                    }, function () {
                    });
                } else if (type == 'form-upload') {
                    openUploadPopup($elm.data('cdata'), function (data: any) {
                        self.settings.oninit($elm, data, data.data, cb, self);
                    }, function () {
                    });
                }
            },

            oninit: function ($elm: any, outp: any, data: any, cb: any, self: any) {
                let type = $elm.data('type');
                if (type == 'form-select') {
                    let con = '';
                    $.each(data.values, function () {
                        con += '<option>'+this+'</option>';
                    });
                    outp.content = '<div class="form-group"><label>'+ data.label +'</label><br />' +
                        '<select class="form-control">'+ con +'</select></div>';
                    outp.editor = false;
                    cb(self, $elm, outp);
                } else if (type == 'form-text') {
                    outp.content = '<div class="form-group"><label>'+ data.label +'</label><br />' +
                        '<input class="form-control" /></div>';
                    outp.editor = false;
                    cb(self, $elm, outp);
                } else if (type == 'form-upload') {
                    outp.content = '<div class="form-group"><i class="fa fa-upload"></i></div>';
                    outp.editor = false;
                    cb(self, $elm, outp);
                }  else {
                    cb(self, $elm, outp);
                }
            },

            // set suitable content for type
            render: function($elm: any, type: any) {

                // use default options when editor = true
                let editor : any = true;
                let enableEdit = false;
                let deleteable = true;
                let con;
                if (type == 'title' || type == 'subtitle') {
                    editor = {};
                    editor.toolbar = [
                        { name: 'clipboard', items: [ 'Paste', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
                        { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', '-', 'RemoveFormat' ] },
                        { name: 'colors', items: [ 'TextColor' ] },
                    ];
                    editor.enterMode = 2;
                } else if (type == 'paragraph') {
                    con = '<p>Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.</p>';
                }

                if (type == 'title') {
                    // add h1 class to element
                    $elm.closest('.item').addClass('h1');
                    con = 'Page Title';
                } else if (type == 'subtitle') {
                    // add h2 class to element
                    $elm.closest('.item').addClass('h2');
                    con = 'Subtitle';
                }

                if (type == 'form-select') {
                    enableEdit = true;
                    con = function (elm:any, outp:any, cb:any, self:any) {
                        outp.content = 'Hallo Welt';
                        openSelectFormPopup({}, function (data:any) {
                            self.settings.oninit(elm, data, data.data, cb, self);
                        }, function () {
                            elm.parent().find('.actions .fa-trash').trigger('click');
                        });
                    };
                } else if (type == 'form-text') {
                    enableEdit = true;
                    con = function (elm:any, outp:any, cb:any, self:any) {
                        outp.content = 'Hallo Welt';
                        openTextInputPopup({}, function (data:any) {
                            self.settings.oninit(elm, data, data.data, cb, self);
                        }, function () {
                            elm.parent().find('.actions .fa-trash').trigger('click');
                        });
                    };
                } else if (type == 'form-checkbox') {
                    enableEdit = true;
                    con = function (elm:any, outp:any, cb:any, self:any) {
                        outp.content = 'Hallo Welt';
                        openCheckboxPopup({}, function (data:any) {
                            data.content = '<div class="checkbox"><label><input type="checkbox" /> '+ data.data.label +'</label></div>';
                            data.editor = false;
                            cb(self, elm, data);
                        }, function () {
                            elm.parent().find('.actions .fa-trash').trigger('click');
                        });
                    };
                } else if (type == 'form-upload') {
                    enableEdit = true;
                    con = function (elm:any, outp:any, cb:any, self:any) {
                        outp.content = 'Hallo Welt';
                        openUploadPopup({}, function (data:any) {
                            self.settings.oninit(elm, data, data.data, cb, self);
                        }, function () {
                            elm.parent().find('.actions .fa-trash').trigger('click');
                        });
                    };
                }

                return {
                    edit: enableEdit,
                    editor: editor,
                    content: con,
                    deleteable: deleteable
                };
            },

            // filter output
            filter: function(content:any, type:any) {
                if (type == 'title') {
                    //content = $(content).unwrap().html();
                }

                return content;
            }

        });
    }
    
});
