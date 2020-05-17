import $ from "jquery";
window.$ = window.jQuery = $;
import _,{baseUrl} from "./config";
import "jquery-ui-dist/jquery-ui";
import CKEDITOR from "ckeditor4";

/*
 DynamicDom
 description: build content with editable components
 author: Andre Steudel <kontakt@andre-steudel.de>
 */
(function ( $, window, document, undefined ) {

    "use strict";

    var pluginName = "dynamicdom",
        defaults = {
            template: '<div class="item ui-state-disabled"><div class="content">Drag it here</div><div class="actions"><i class="fa fa-pencil hidden"></i><i class="fa fa-trash"></i><i class="fa fa-arrows"></i></div><div class="hover"></div></div>',
            sidebar: '#sidebar .square',
            onchange: null,
            onedit: null,
            oninit: null,
            render: null,
            filter: null,
            data: null
        };

    function Plugin ( element, options ) {
        this.element = element;
        this.settings = $.extend( {}, defaults, options );
        this._defaults = defaults;
        this._name = pluginName;
        this.limits = [];
        this.init();
    }

    $.extend(Plugin.prototype, {
        init: function () {
            var self = this,
                $el = $(this.element);

            // enable dragging of sidebar cards
            $(this.settings.sidebar).each(function() {
                self.limits[$(this).data('type')] = {limit: $(this).data('limit'), num: 0};
                $(this).draggable( {
                    cursor: 'move',
                    revert: true,
                    opacity: 0.75,
                    helper: 'clone'
                } )
            });

            if (this.settings.data != null) {
                this.import(this.settings.data);
            }

            // add a placeholder for dropping
            this.addPlaceholder().appendTo($el);

            // init sorting
            $el.sortable({
                items: ".item:not(.ui-state-disabled)",
                handle: ".actions .fa-arrows",
            });

            // get output
            $el.on( "sortupdate", function() {
                self.update();
            });

            // disable auto creation of inline editors
            CKEDITOR.disableAutoInline = true;

        },
        addPlaceholder: function() {
            var self = this;

            return $(this.settings.template).droppable( {
                accept: this.settings.sidebar,
                hoverClass: 'hovered',
                drop: function( event, ui ) {
                    self.handleCardDrop( this, event, ui);
                }
            } );
        },
        handleCardDrop: function(that, event, ui, content, init) {

            var outerThis = this;
            var self = $(that);
            var $el = $(this.element);

            // check if ui is object, if not function is called form external
            // with last parameter as type
            if (typeof ui === 'object') {
                var type = ui.draggable.data('type');

                if (this.limitReached(type)) {
                    console.error('no more nodes of type: '+ type + ' are allowed!');
                    return;
                }

                // if dropped on placeholder, than change content of it and append new one
                if (!self.hasClass('active')) {
                    this.addPlaceholder().appendTo($el);
                } else {
                    // if dropped on existing node, prepend before node
                    self = this.addPlaceholder();
                    $(that).before(self);
                }

            } else {
                var type = ui;
            }

            this.limits[type].num++;

            this.setCardContent(self.find('.content'), type, content, init);

            // change node to active, enable sorting
            self.addClass('active').removeClass('ui-state-disabled');

            // set action listeners
            self.find('.actions .fa-trash').click(function() {
                var item = $(this).closest('.item');
                var type = item.find('.content').data('type');
                item.remove();
                outerThis.update();
                outerThis.limits[type].num--;
                $(outerThis.settings.sidebar + '[data-type='+type+']').draggable( 'option', 'disabled', false );
            });

            self.find('.actions .fa-pencil').click(function() {
                var item = $(this).closest('.item').find('.content');
                var type = item.data('type');

                if (typeof outerThis.settings.onedit == 'function') {
                    outerThis.settings.onedit.call(outerThis, item, type, outerThis.updateCardContent, outerThis);
                }

            });

            // disable and reenable revert option, to hide animation
            if (typeof ui === 'object') {
                ui.draggable.draggable( 'option', 'revert', false );
                setTimeout(function() {
                    ui.draggable.draggable( 'option', 'revert', true );
                    if (outerThis.limitReached(type)) {
                        ui.draggable.draggable( 'option', 'disabled', true );
                    }
                }, 100);
            } else if (outerThis.limitReached(type)) {
                $(outerThis.settings.sidebar + '[data-type='+type+']').draggable( 'option', 'disabled', true );
            }

            this.update();

        },
        limitReached: function(type) {
            return this.limits[type].limit != null && this.limits[type].limit <= this.limits[type].num;
        },
        update: function() {
            if (typeof this.settings.onchange == 'function') {
                this.settings.onchange.call(this, this.generateOutput());
            }
        },
        generateOutput: function() {
            var outp = [];
            var $el = $(this.element);
            var self = this;

            $el.find('.item').each(function() {
                if (!$(this).hasClass('ui-state-disabled')) {
                    var elm = $(this).find('.content');
                    var content = self.trim(elm.html());
                    var type = elm.data('type');
                    var data = elm.data('cdata');

                    if (typeof self.settings.filter == 'function') {
                        content = self.settings.filter.apply(null, [content, type]);
                    }

                    outp.push({
                        type: type,
                        data: data,
                        content: content
                    });
                }
            });

            return outp;
        },
        setCardContent: function(elm, type, content, init) {
            var self = this;

            // set type of node
            elm.data('type', type);

            var outp = {editor: false, content: 'undefined', edit: false};

            if (typeof this.settings.render == 'function') {
                outp = this.settings.render.apply(null, [elm, type]);
            }

            if (outp.edit) {
                elm.parent().find('.actions .fa-pencil').removeClass('hidden');
            }

            if (!outp.deleteable) {
                elm.parent().find('.actions .fa-trash').addClass('hidden');
            }

            // set content
            if (content != null) {
                outp.content = content;
            }

            if (init === true) {
                if (typeof self.settings.oninit === "function") {
                    self.settings.oninit(elm, outp, elm.data('cdata'), this.updateCardContent, this);
                } else {
                    this.updateCardContent(this, elm, outp);
                }
            } else {
                if (typeof outp.content === "function") {
                    outp.content(elm, outp, this.updateCardContent, this);
                } else {
                    this.updateCardContent(this, elm, outp);
                }
            }
        },
        updateCardContent: function (self, elm, outp) {
            elm.html(outp.content);

            elm.data('cdata', outp.data);

            // enable ckeditor
            if (outp.editor !== false) {
                elm.attr('contenteditable', true);

                if (typeof outp.editor === 'object') {
                    CKEDITOR.inline(elm[0], outp.editor).on('change', function() {
                        self.update();
                    });
                } else {
                    CKEDITOR.inline(elm[0]).on('change', function() {
                        self.update();
                    });
                }
            }

            self.update();
        },
        import: function(arr) {
            var $el = $(this.element);
            var self = this;

            $.each(arr, function() {
                var tmp = self.addPlaceholder();
                $el.append(tmp);
                tmp.find('.content').data('cdata', this.data);
                self.handleCardDrop(tmp, null, this.type, this.content, true);
            });
        },
        nl2br: function (str) {
            return (str + '').replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1<br />$2');
        },
        trim: function(str) {
            return this.nl2br($.trim(str.replace(/<br\s*[\/]?>/gi, "\n")))
        }
    });

    $.fn[ pluginName ] = function ( options ) {
        return this.each(function() {
            if ( !$.data( this, "plugin_" + pluginName ) ) {
                $.data( this, "plugin_" + pluginName, new Plugin( this, options ) );
            }
        });
    };

})( jQuery, window, document );