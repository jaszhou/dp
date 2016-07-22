
// wait for the DOM to be loaded 
$(document).ready(function() {

	$('#delete').click(function(event) {
		event.preventDefault();
		var url = $(this).attr('href');
		$.ajax({
			url : url,
			data : '{}',
			success : function(data) {
				// Your Code here

				// alert(url);

//				$('#profilelist').hide().html(data);
//				$('#profilelist').html(data).slideDown("slow");

//				$('#message').hide(1000);
//				$('#message').hide("slow");
				
//				window.location="/batchlist";
				window.location.href = "/batchlist";
//				$('#message').html("<h2>Batch has been deleted successfully!</h2>");
				
//				windows.reload();
			}
		})
	});

});


