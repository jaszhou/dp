// prepare the form when the DOM is ready 
$(document).ready(function() {
	
	var options = {
			
	    beforeSubmit: validate,
		success : showResponse
	// post-submit callback
	};

	// bind 'myForm' and provide a simple callback function
	$('#myForm').ajaxForm(options);

	
});


function validate(formData, jqForm, options) { 
    // jqForm is a jQuery object which wraps the form DOM element 
    // 
    // To validate, we can access the DOM elements directly and return true 
    // only if the values of both the username and password fields evaluate 
    // to true 
 
    var form = jqForm[0]; 

    if (!form.profile.value) { 
        //alert('Please input profile name!'); 
        return false; 
    } 

    if (form.mlist.value == 'null') { 
//        alert('Please select a list!'); 
        
    	//$('#profilelist').html(data).slideDown("slow");
    	
    	//
        $('#selectmsg').html("<div class='well'>Please select a list!</div>").slideDown("slow");
        
        
        return false; 
    } 

    
}

//post-submit callback
function showResponse(responseText, statusText, xhr, $form) {


	location.href = "/profilelist";

}


