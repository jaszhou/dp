// $('#loadingmessage').hide();

// wait for the DOM to be loaded
$(document).ready(function() {

	// showjtable;

	$('#sbutton').hide();

	var options = {
		beforeSubmit : showRequest, // pre-submit callback
		success : showResponse
	// post-submit callback
	};

	// bind 'myForm' and provide a simple callback function
	$('#frmBatch').ajaxForm(options);

	// show profile list when click the link button

	$('#selectp').click(function(event) {
		event.preventDefault();
		var url = $(this).attr('href');

		// alert(url);

		$.ajax({
			url : url,
			data : '{}',
			success : function(data) {
				// Your Code here

				// alert(url);

				$('#profilelist').hide().html(data);
				$('#profilelist').html(data).slideDown("slow");

			}
		})
	});

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

	// alert('About to submit: \n\n' + queryString);

	// alert(document.getElementById('batchid').value);

	// alert($("#screenbid").val());

	$('#loadingmessage').show();

	// here we could return false to prevent the form from being submitted;
	// returning anything other than false will allow the form submit to
	// continue

	return true;
}

// post-submit callback
function showResponse(responseText, statusText, xhr, $form) {

	$('#loadingmessage').hide();
	// return true;

	// alert($('#batchid').val());

	// $('#txt_name').val();

	// var form = jqForm[0];
	// if (!form.listname.value) {
	// alert('Please enter a list name!');
	// return false;
	// }

	// location.href = "/result?batchid="+$('#batchid').value();
	location.href = "/resultlist";

}
