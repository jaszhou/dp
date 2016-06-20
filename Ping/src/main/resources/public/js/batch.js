// prepare the form when the DOM is ready 
$(document).ready(function() { 
    // bind form using ajaxForm 
//    $('#myForm').ajaxForm( { beforeSubmit: validate } ); 
    
    $('.progress').hide();
    
    sendFile();
    
});

function sendFile() {
	
    // handle file upload
    var bar = $('.bar');
    var percent = $('.percent');
    var status = $('#status');

    
    
//    bar.hide();
//    percent.hide();
//    status.hide();
    
    $('#myForm').ajaxForm({
    	
    	beforeSubmit: validate,
    
        beforeSend: function() {
            status.empty();
            
            var percentVal = '0%';
            bar.width(percentVal)
            percent.html(percentVal);
            
            $('.progress').slideDown();
            $('.bar').slideDown();
            $('#status').slideDown();
            
        },
        uploadProgress: function(event, position, total, percentComplete) {
            var percentVal = percentComplete + '%';
            bar.width(percentVal)
            percent.html(percentVal);

//            bar.show();
//            percent.show();

        },
        success: function() {
            var percentVal = '100%';
            bar.width(percentVal)
            percent.html(percentVal);
        },
    	complete: function(xhr) {
    		status.html("File uploaded successfully!");
    	}
    }); 

	
}
function validate(formData, jqForm, options) { 
    // jqForm is a jQuery object which wraps the form DOM element 
    // 
    // To validate, we can access the DOM elements directly and return true 
    // only if the values of both the username and password fields evaluate 
    // to true 
 
    var form = jqForm[0]; 
   
    if (!form.upload.value) { 
        alert('Please select a file to upload!'); 
        return false; 
    } 
    
    
}

