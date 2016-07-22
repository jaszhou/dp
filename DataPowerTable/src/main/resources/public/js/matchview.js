// prepare the form when the DOM is ready 

$(function() {

	// $('#attachment').load('/attachment?matchid='+match["id"]);

	// alert('/attachment?matchid='+match["id"]);

    $(".content").hide();
    
    
    $(".panel-heading").click(function(){
    	         
    	$(this).next(".content").slideToggle(500);
        $(this).find('.glyphicon').toggleClass("glyphicon-collapse-up","glyphicon-collapse-down");
    	      
    });
    
	//Edit SL: more universal
	$(document).on('hidden.bs.modal', function (e) {
	    $(e.target).removeData('bs.modal');
	});
	
	$('.showmodal').click(function(event) {
		event.preventDefault();
		var url = $(this).attr('href');

		//alert(url);

		$.ajax({
			cache: false,
			url : url,
			data : '{}',
			success : function(data) {

				//alert(url);
				//alert(data);

//				$(this).next('.edit-content').html(data);
				$("#myModal").modal("show");
				$('#mycontent').replaceWith(data).slideDown('slow');
				  
			}
		})
	});
	
	$('#myForm').hide();
	
	$('.progress').hide();

	
	getAttachment();


	sendFile();

	
});


function getAttachment() {

	$('#showattachment').click(function(event) {
		event.preventDefault();

		 
		// alert($(this).text());

		if ($(this).text() == "Hide Attachments") {

			// alert("hide");
			$('#attachment').fadeToggle();
			
			$('#showattachment').text("Show Attachments");
			$('#myForm').hide("slow");

		} else {
			var url = $(this).attr('href');

			// alert(url);

			$.ajax({
				url : url,
				data : '{}',
				success : function(data) {
					// Your Code here

					// alert(url);

					$('#attachment').html(data).slideDown("slow");

					$('#showattachment').text("Hide Attachments");

					$('#myForm').show("slow");

				}
			});

			// getAttachment;

		}
	});
	


}



function sendFile() {

	// handle file upload
	var bar = $('.bar');
	var percent = $('.percent');
	var status = $('#status');

	// bar.hide();
	// percent.hide();
	// status.hide();

	$('#myForm').ajaxForm({

		// beforeSubmit: validate,
		
		

		beforeSend : function() {
			status.empty();

			var percentVal = '0%';
			bar.width(percentVal)
			percent.html(percentVal);

			$('.progress').slideDown();
			$('.bar').slideDown();
			$('#status').slideDown();

		},
		uploadProgress : function(event, position, total, percentComplete) {
			var percentVal = percentComplete + '%';
			bar.width(percentVal)
			percent.html(percentVal);

			// bar.show();
			// percent.show();

		},
		success : function() {
			var percentVal = '100%';
			bar.width(percentVal)
			percent.html(percentVal);

		},
		complete : function(xhr) {
			
			
			status.html("<div class='well'>File uploaded successfully!</div>");

			
			//$('#myForm').hide("slow");
			
			//$('#dinamic_link').click();
			//$('#link').trigger('click');
			
			$('#showattachment').text("Show Attachments");

			$('#showattachment').trigger('click');
			

		}
	});

}
