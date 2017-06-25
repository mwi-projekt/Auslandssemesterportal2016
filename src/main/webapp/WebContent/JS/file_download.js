$(document).ready(function() {
	url = new URL(window.location.href);
	instanceID = url.searchParams.get("instance_id");
	filename = url.searchParams.get("filename");
	$.ajax({
		type : "GET",
		url : "getProcessFile",
		data : {
			instance_id : instanceID,
			key : filename
		},
		success : function(result) {
			location.href=result;
		},
		error : function(result) {
			alert('Ein Fehler ist aufgetreten');
		}
	});
});

