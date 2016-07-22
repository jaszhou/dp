// prepare the form when the DOM is ready 
$(document).ready(function() {
	
	
	var options = {
		beforeSerialize:init,	
	    beforeSubmit: showRequest,
		success : showResponse
	// post-submit callback
	};

	// bind 'myForm' and provide a simple callback function
	$('#pForm').ajaxForm(options);
	
	// bind submit handler to form
//	$('#pForm').on('submit', function(e) {
//	    e.preventDefault(); // prevent native submit
//	    var values = $("#example-enableCollapsibleOptGroups").val();
//	    
//	    $(this).ajaxSubmit({
//	          // You can change the url option to desired target
//	          url: $form.attr('action'),
//	  	      data: {'mlist': values },
//	          success: function(responseText, statusText, xhr, $form) {
//	              // Process the response returned by the server ...
//	              // console.log(responseText);
//	          }
//	    })
//	});
	
});


//pre-submit callback 
function showRequest(formData, jqForm, options) { 
    // formData is an array; here we use $.param to convert it to a string to display it 
    // but the form plugin does this for you automatically when it submits the data 
    var queryString = $.param(formData); 
 
    // jqForm is a jQuery object encapsulating the form element.  To access the 
    // DOM element for the form do this: 
    // var formElement = jqForm[0]; 
 
    
//	alert('About to submit: \n\n' + queryString); 
 
    // here we could return false to prevent the form from being submitted; 
    // returning anything other than false will allow the form submit to continue 
    return true; 
} 


function init(jqForm, options) { 

	var values = $("#example-enableCollapsibleOptGroups").val();

    var input = $("<input>")
    .attr("type", "hidden")
    .attr("name", "mylist").val(values);
   

	$('#pForm').append($(input));

 
    // here we could return false to prevent the form from being submitted; 
    // returning anything other than false will allow the form submit to continue 
    return true; 
} 


function validate(formData, jqForm, options) { 
    // jqForm is a jQuery object which wraps the form DOM element 
    // 
    // To validate, we can access the DOM elements directly and return true 
    // only if the values of both the username and password fields evaluate 
    // to true 
 
    var form = jqForm[0]; 
    
    var values = $("#example-enableCollapsibleOptGroups").val();
    
    //data: { 'mlist': values },
    
//    alert(values);
    
    
}

//post-submit callback
function showResponse(responseText, statusText, xhr, $form) {


	location.href = "/profilelist";

}


