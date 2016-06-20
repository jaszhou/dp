
// wait for the DOM to be loaded 


$(document).ready(function() {


	$('.delete').click(function(event) {
		event.preventDefault();
		var url = $(this).attr('href');
		$.ajax({
			url : url,
			data : '{}',
			success : function(data) {
				// Your Code here

				 alert(url);

				$('#profilelist').html(data);
				
				
  			    $('#status').html("<div class='well'>Profile deleted successfully!</div>");
  			    $('#status').fadeOut(5000);
  			  
			}
		})
	});

});

