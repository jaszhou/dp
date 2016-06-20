// prepare the form when the DOM is ready 

$(function() {

	$('#myForm').ajaxForm({

		// beforeSubmit: validate,
		success : function() {
			
			var msg = '<div class="well"><p class="text-success">User updated successfully!</p></div>';

			$('#message').html(msg).slideDown("slow");
		}
	});

    
	
});




