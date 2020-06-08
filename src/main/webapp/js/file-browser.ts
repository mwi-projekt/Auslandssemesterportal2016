import { $, baseUrl } from "./config";
import "ckeditor4";

$(function () {

	let filemanager = $('.filemanager'),
		fileList = filemanager.find('.data');

	// Helper function to get parameters from the query string.
	function getUrlParam(paramName: string) {
		let reParam = new RegExp('(?:[\?&]|&)' + paramName + '=([^&]+)', 'i');
		let match = window.location.search.match(reParam);

		return (match && match.length > 1) ? match[1] : null;
	}

	// Start by fetching the file data from scan.php with an AJAX request

	$.get(baseUrl + '/filelist', function (data) {

		let response = [data],
			currentPath = '';

		let folders: any[] = [],
			files: any[] = [];

		// This event listener monitors changes on the URL. We use it to
		// capture back/forward navigation in the browser.

		$(window).on('hashchange', function () {

			goto(window.location.hash);

			// We are triggering the event. This will execute
			// this function on page load, so that we show the correct folder:

		}).trigger('hashchange');


		// Hiding and showing the search box

		filemanager.find('.search').click(function () {

			let search = $(this);

			search.find('span').hide();
			search.find('input[type=search]').show().focus();

		});

		$(document).on('click', '.file-item', function () {
			if ($(this).parent().hasClass('active')) {
				$('.filemanager .data li').removeClass('active');
				$('#chooseFile').prop("disabled", true);
			} else {
				$('.filemanager .data li').removeClass('active');
				$(this).parent().addClass('active');
				$('#chooseFile').prop("disabled", false);
			}
		});

		$('#chooseFile').click(function () {
			if ($(this).prop('disabled') != true) {
				let funcNum = getUrlParam('CKEditorFuncNum');
				let fileUrl = $('.filemanager .data li.active a').attr('title');
				console.log(fileUrl);
				window.opener.CKEDITOR.tools.callFunction(funcNum, fileUrl);
				window.close();
			}
		});


		// Listening for keyboard input on the search field.
		// We are using the "input" event which detects cut and paste
		// in addition to keyboard input.

		filemanager.find('input').on('input', function (e) {

			folders = [];
			files = [];

			let value = (<any>this).value.trim();

			if (value.length) {

				filemanager.addClass('searching');

				// Update the hash on every key stroke
				window.location.hash = 'search=' + value.trim();

			}

			else {

				filemanager.removeClass('searching');
				window.location.hash = encodeURIComponent(currentPath);

			}

		}).on('keyup', function (e) {

			// Clicking 'ESC' button triggers focusout and cancels the search

			let search = $(this);

			if (e.keyCode == 27) {

				search.trigger('focusout');

			}

		}).focusout(function (e) {

			// Cancel the search

			let search = $(this);

			if (!search.val()!.toString().trim().length) {

				window.location.hash = encodeURIComponent(currentPath);
				search.hide();
				search.parent().find('span').show();

			}

		});


		// Clicking on folders

		fileList.on('click', 'li.folders', function (e) {
			e.preventDefault();

			let nextDir = $(this).find('a.folders').attr('href')?.toString()!;

			if (filemanager.hasClass('searching')) {

				filemanager.removeClass('searching');
				filemanager.find('input[type=search]').val('').hide();
				filemanager.find('span').show();
			}

			window.location.hash = encodeURIComponent(nextDir);
			currentPath = nextDir;
		});


		// Navigates to the given hash (path)

		function goto(hash: string) {

			let hashes = decodeURIComponent(hash).slice(1).split('=');

			if (hashes.length) {
				let rendered: any = '';

				// if hash has search in it

				if (hash[0] === 'search') {

					filemanager.addClass('searching');
					rendered = searchData(response, hashes[1].toLowerCase());

					if (rendered.length) {
						currentPath = hashes[0];
						render(rendered);
					}
					else {
						render(rendered);
					}

				}

				// if hash is some path

				else if (hashes[0].trim().length) {

					rendered = searchByPath(hashes[0]);

					if (rendered.length) {

						currentPath = hashes[0];
						render(rendered);

					}
					else {
						currentPath = hashes[0];
						render(rendered);
					}

				}

				// if there is no hash

				else {
					currentPath = data.path;
					render(searchByPath(data.path));
				}
			}
		}


		// Locates a file by path

		function searchByPath(dir: string) {
			let path = dir.split('/'),
				demo = response,
				flag = 0;

			for (let i = 0; i < path.length; i++) {
				for (let j = 0; j < demo.length; j++) {
					if (demo[j].name === path[i]) {
						flag = 1;
						demo = demo[j].items;
						break;
					}
				}
			}

			demo = flag ? demo : [];
			return demo;
		}


		// Recursively search through the file tree

		function searchData(data: any, searchTerms: any) {

			data.forEach(function (d: any) {
				if (d.type === 'folder') {
					searchData(d.items, searchTerms);
				}
				else if (d.type === 'file') {
					if (d.name.toLowerCase().match(searchTerms)) {
						files.push(d);
					}
				}
			});
			return { folders: folders, files: files };
		}


		// Render the HTML for the file manager

		function render(data: any) {

			let scannedFolders = [],
				scannedFiles = [];

			if (Array.isArray(data)) {

				data.forEach(function (d) {

					if (d.type === 'folder') {
						scannedFolders.push(d);
					}
					else if (d.type === 'file') {
						scannedFiles.push(d);
					}

				});

			}
			else if (typeof data === 'object') {

				scannedFolders = data.folders;
				scannedFiles = data.files;

			}


			// Empty the old result and make the new one

			fileList.empty().hide();

			if (!scannedFolders.length && !scannedFiles.length) {
				filemanager.find('.nothingfound').show();
			}
			else {
				filemanager.find('.nothingfound').hide();
			}

			if (scannedFolders.length) {

				scannedFolders.forEach(function (f: any) {

					let itemsLength = f.items.length,
						name = escapeHTML(f.name),
						icon = '<span class="icon folder"></span>';

					if (itemsLength) {
						icon = '<span class="icon folder full"></span>';
					}

					if (itemsLength == 1) {
						itemsLength += ' item';
					}
					else if (itemsLength > 1) {
						itemsLength += ' items';
					}
					else {
						itemsLength = 'Empty';
					}

					let folder = $('<li class="folders"><a href="' + f.path + '" title="' + f.path + '" class="folders">' + icon + '<span class="name">' + name + '</span> <span class="details">' + itemsLength + '</span></a></li>');
					folder.appendTo(fileList);
				});

			}

			if (scannedFiles.length) {

				scannedFiles.forEach(function (f: any) {

					let fileSize = bytesToSize(f.size),
						name = escapeHTML(f.name),
						fileType = name.split('.'),
						icon = '<span class="icon file"></span>';

					let fileTypeString = fileType[fileType.length - 1].toLowerCase();

					icon = '<span class="icon file f-' + fileTypeString + '">.' + fileTypeString + '</span>';

					let file = $('<li class="files"><a title="' + f.path + '" class="files file-item">' + icon + '<span class="name">' + name + '</span> <span class="details">' + fileSize + '</span></a></li>');
					file.appendTo(fileList);
				});

			}


			// Show the generated elements

			fileList.animate({ 'display': 'inline-block' });

		}


		// This function escapes special html characters in names

		function escapeHTML(text: string): string {
			return text.replace(/\&/g, '&amp;').replace(/\</g, '&lt;').replace(/\>/g, '&gt;');
		}


		// Convert file sizes from bytes to human readable units

		function bytesToSize(bytes: any) {
			let sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
			if (bytes == 0) return '0 Bytes';
			let i = Math.floor(Math.log(bytes) / Math.log(1024));
			return Math.round(bytes / Math.pow(1024, i)) + ' ' + sizes[i];
		}

	});
});
