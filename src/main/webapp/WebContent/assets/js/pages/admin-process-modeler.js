$(window).onbeforeunload = function() {
	alert("The Window is closing!");
   return "Möchten sie wirklich verlassen?";
};

$(window).addEventListener('beforeunload', function (e) {
			return "test";
	});

$(window).onunload = function(event) { return "test" };

$(window).onbeforeunload = function(e) {
    alert("The Window is closing!");
};

$(document).ready(function () {
	
	$(window).on('beforeunload', function(){
        return 'Are you sure you want to leave?';
 });
	
    var id = $.urlParam('id').trim();
    var dia = $.urlParam('dia').trim();
    var type = $.urlParam('type').trim();
    var index = $.urlParam('index').trim();
    var json = {};

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
            swal(
                'Gespeichert!',
                'Der Prozessschritt wurde erfolgreich abgespeichert.',
                'success'
            )
        });
    });

    function openCheckboxPopup(data, cb, cbClose) {
        var success = false;
        $.sweetModal({
            title: 'Checkbox hinzufügen',
            content: checkboxForm,
            blocking: true,
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
                    $('#demo-label').text($(this).val());
                });

                $('#field-save').on('click', function () {
                    var data = {
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
            //theme: $.sweetModal.THEME_DARK
        });
    }

    function openSelectFormPopup(data, cb, cbClose) {
        var success = false;
        $.sweetModal({
            title: 'Auswahlfeld hinzufügen',
            content: selectForm,
            blocking: true,
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

                function addListItem(val) {
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
                    $('#field-req')[0].checked = data.required;
                }

                $("[switch]").bootstrapSwitch({
                    onText: 'Ja',
                    offText: 'Nein'
                });

                if (data.id) {
                    $('#field-id').val(data.id);
                }

                $('#field-label').on('change keydown', function () {
                    $('#demo-label').text($(this).val());
                });

                $('#field-val').on('keydown', function () {
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
                    var values = [];
                    $('#field-values').children().each(function () {
                        values.push($(this).find('span').text());
                    });
                    var data = {
                        data: {
                            values: values,
                            label: $('#field-label').val(),
                            id: $('#field-id').val(),
                            required: $('#field-req')[0].checked
                        },
                        content: $('#demo').html()
                    };
                    success = true;
                    cb(data);
                    $('.sweet-modal-close-link').trigger('click');
                });
            },
            //theme: $.sweetModal.THEME_DARK
        });
    }

    function openUploadPopup(data, cb, cbClose) {
        var success = false;
        $.sweetModal({
            title: 'Upload hinzufügen',
            content: uploadForm,
            blocking: true,
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
                    var data = {
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
            //theme: $.sweetModal.THEME_DARK
        });
    }

    function openTextInputPopup(data, cb, cbClose) {
        var success = false;
        $.sweetModal({
            title: 'Textfeld hinzufügen',
            content: textForm,
            blocking: true,
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
                    $('#field-req')[0].checked = data.required;
                }

                $("[switch]").bootstrapSwitch({
                    onText: 'Ja',
                    offText: 'Nein'
                });

                $('#field-label').on('change keydown', function () {
                    $('#demo-label').text($(this).val());
                });

                $('#field-save').on('click', function () {
                    var data = {
                        data: {
                            label: $('#field-label').val(),
                            type: $('#field-type').val(),
                            numchars: $('#field-numchars').val(),
                            id: $('#field-id').val(),
                            required: $('#field-req')[0].checked
                        }
                    };
                    success = true;
                    cb(data);
                    $('.sweet-modal-close-link').trigger('click');
                });
            },
            //theme: $.sweetModal.THEME_DARK
        });
    }

    var selectForm = '';
    $.get('modals/select-form.html', {}, function (data) {
        selectForm = data;
    });

    var textForm = '';
    $.get('modals/text-form.html', {}, function (data) {
        textForm = data;
    });

    var checkboxForm = '';
    $.get('modals/checkbox-form.html', {}, function (data) {
        checkboxForm = data;
    });

    var uploadForm = '';
    $.get('modals/upload-form.html', {}, function (data) {
        uploadForm = data;
    });

    function init(data) {
        $('#cardSlots').dynamicdom({

            // initalize
            data: data,

            // on change update output element
            onchange: function(output) {
                json = output;
            },

            onedit: function ($elm, type, cb, self) {
                if (type == 'form-select') {
                    openSelectFormPopup($elm.data('cdata'), function (data) {
                        self.settings.oninit($elm, data, data.data, cb, self);
                    }, function () {
                    });
                } else if (type == 'form-text') {
                    openTextInputPopup($elm.data('cdata'), function (data) {
                        self.settings.oninit($elm, data, data.data, cb, self);
                    }, function () {
                    });
                } else if (type == 'form-checkbox') {
                    openCheckboxPopup($elm.data('cdata'), function (data) {
                        data.content = '<div class="form-group"><label><input type="checkbox" /> '+ data.data.label +'</label></div>';
                        data.editor = false;
                        cb(self, $elm, data);
                    }, function () {
                    });
                } else if (type == 'form-upload') {
                    openUploadPopup($elm.data('cdata'), function (data) {
                        self.settings.oninit($elm, data, data.data, cb, self);
                    }, function () {
                    });
                }
            },

            oninit: function ($elm, outp, data, cb, self) {
                var type = $elm.data('type');
                if (type == 'form-select') {
                    var con = '';
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
            render: function($elm, type) {

                // use default options when editor = true
                var editor = true;
                var enableEdit = false;
                var deleteable = true;

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
                    // add h1 class to elemtent
                    $elm.closest('.item').addClass('h1');
                    con = 'Page Title';
                } else if (type == 'subtitle') {
                    // add h2 class to elemtent
                    $elm.closest('.item').addClass('h2');
                    con = 'Subtitle';
                }

                if (type == 'form-select') {
                    enableEdit = true;
                    con = function (elm, outp, cb, self) {
                        outp.content = 'Hallo Welt';
                        openSelectFormPopup({}, function (data) {
                            self.settings.oninit(elm, data, data.data, cb, self);
                        }, function () {
                            elm.parent().find('.actions .fa-trash').trigger('click');
                        });
                    };
                } else if (type == 'form-text') {
                    enableEdit = true;
                    con = function (elm, outp, cb, self) {
                        outp.content = 'Hallo Welt';
                        openTextInputPopup({}, function (data) {
                            self.settings.oninit(elm, data, data.data, cb, self);
                        }, function () {
                            elm.parent().find('.actions .fa-trash').trigger('click');
                        });
                    };
                } else if (type == 'form-checkbox') {
                    enableEdit = true;
                    con = function (elm, outp, cb, self) {
                        outp.content = 'Hallo Welt';
                        openCheckboxPopup({}, function (data) {
                            data.content = '<div class="checkbox"><label><input type="checkbox" /> '+ data.data.label +'</label></div>';
                            data.editor = false;
                            cb(self, elm, data);
                        }, function () {
                            elm.parent().find('.actions .fa-trash').trigger('click');
                        });
                    };
                } else if (type == 'form-upload') {
                    enableEdit = true;
                    con = function (elm, outp, cb, self) {
                        outp.content = 'Hallo Welt';
                        openUploadPopup({}, function (data) {
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
            filter: function(content, type) {
                if (type == 'title') {
                    //content = $(content).unwrap().html();
                }

                return content;
            }

        });
    }
    
});
