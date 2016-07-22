// prepare the form when the DOM is ready 

$(function() {

//	$('#selectall').click(function(event) {
//		event.preventDefault();
//		
//		  var checkBoxes = $(".check");
//	        checkBoxes.prop("checked", !checkBoxes.prop("checked"));
//	        
//	       
//		 //$('.check').prop('checked',true);
//	});
	
	
	
	  $('#apply').click(function(){
			event.preventDefault();
	        var val = [];
	        
			var url = $(this).attr('href');

	        //alert($('form').serialize());
	        
	        $.ajax({
				url : url,
				type: 'post',
				data : $('form').serialize(),
				success : function(data) {
					// Your Code here

					//alert(data);

			        
	  			    $('#status').html("<div class='well'>Rules applied successfully!</div>");
	  			    $('#status').fadeOut(5000);


				}
			})
	  });

	  

});


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
