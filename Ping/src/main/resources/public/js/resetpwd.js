// prepare the form when the DOM is ready 

$(function() {

	$('#message').hide();
	
	$('#myForm').ajaxForm({

		// beforeSubmit: validate,
		success : function(data) {
			
			//var msg = '<div class="well"><p class="text-success">Password updated successfully!</p></div>';

			$('#message').html(data).slideDown("slow");
		}
	});

    
	
});




