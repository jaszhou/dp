$(document).ready(function() {

	var options = {
		beforeSubmit : showRequest, // pre-submit callback
		success : showResponse
	// post-submit callback
	};

	// bind 'myForm' and provide a simple callback function
	$('#pform').ajaxForm(options);

});

// pre-submit callback
function showRequest(formData, jqForm, options) {
	// formData is an array; here we use $.param to convert it to a string to
	// display it
	// but the form plugin does this for you automatically when it submits the
	// data
	var queryString = $.param(formData);

	// jqForm is a jQuery object encapsulating the form element. To access the
	// DOM element for the form do this:
	// var formElement = jqForm[0];
	
	//alert(queryString);

	$('#loadingmessage').show();

	$('#t').show();

	$("#t").timer({
		action : 'start',
		seconds : 0
	});

	// here we could return false to prevent the form from being submitted;
	// returning anything other than false will allow the form submit to
	// continue

	return true;
}

// post-submit callback
function showResponse(responseText, statusText, xhr, $form) {

	$('#loadingmessage').hide();
	// return true;

	$("#t").timer({
		action : 'pause'
	});

	$('#goto').show('slow');

	// location.href = "/result?batchid="+$('#batchid').value();
	location.href = "/resultlist";

}
