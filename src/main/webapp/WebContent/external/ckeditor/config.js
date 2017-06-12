/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
    //config.language = 'fr';
    config.toolbarGroups = [
        { name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
        { name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
        { name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
        { name: 'forms', groups: [ 'forms' ] },
        { name: 'styles', groups: [ 'styles' ] },
        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
        { name: 'colors', groups: [ 'colors' ] },
        { name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
        { name: 'insert', groups: [ 'insert' ] },
        { name: 'links', groups: [ 'links' ] },
        { name: 'tools', groups: [ 'tools' ] },
        { name: 'others', groups: [ 'others' ] },
        { name: 'about', groups: [ 'about' ] }
    ];

    config.extraPlugins = 'uploadimage,entities';

    config.entities_latin = true;
    config.entities = true;

    config.filebrowserBrowseUrl = 'modals/file-browser.html';
    config.filebrowserUploadUrl = 'model/upload';

    config.removeButtons = 'NewPage,Preview,Print,Save,Templates,Source,SelectAll,Scayt,BidiRtl,BidiLtr,Language,Table,PageBreak,SpecialChar,Iframe,Anchor,Styles,BGColor,ShowBlocks,Subscript,Superscript,Strike,Outdent,Indent,Cut,Copy,PasteText,Find,Replace,HorizontalRule,CreateDiv,Font,Maximize';
};