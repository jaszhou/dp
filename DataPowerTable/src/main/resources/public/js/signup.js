// prepare the form when the DOM is ready 
$(document).ready(function() {
	
	var options = {
			
	    beforeSubmit: validate,
		success : showResponse
	// post-submit callback
	};

	// bind 'myForm' and provide a simple callback function
	$('#clientForm').ajaxForm(options);

	
});


function validate(formData, jqForm, options) { 
    // jqForm is a jQuery object which wraps the form DOM element 
    // 
    // To validate, we can access the DOM elements directly and return true 
    // only if the values of both the username and password fields evaluate 
    // to true 
 
    var form = jqForm[0]; 

}

//post-submit callback
function showResponse(responseText, statusText, xhr, $form) {


//	location.href = "/profilelist";
	
	$('#clientForm').hide();
	
    $('#status').html("<div class='well'>User has been created successfully! </div>");
//    $('#status').fadeOut(5000);

}


