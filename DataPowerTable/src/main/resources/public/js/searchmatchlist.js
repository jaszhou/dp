// prepare the form when the DOM is ready 

$(function() {

	$('#selectall').click(function(event) {
		event.preventDefault();
		
		  var checkBoxes = $(".check");
	        checkBoxes.prop("checked", !checkBoxes.prop("checked"));
	        
	       
		 //$('.check').prop('checked',true);
	});
	
	
	
	  $('.btn-ok').click(function(){
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

			        $(':checkbox:checked').each(function(i){
				          //val[i] = $(this).val();
				          
				        	$(this).closest("tr").hide();
				          
				        });

			        $(".modal").modal("hide");
			        
	  			    $('#status').html("<div class='well'>Match deleted successfully!</div>");
	  			    $('#status').fadeOut(5000);


				}
			})
	  });

	  $('#bulkupdate').click(function(){
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

			        $(':checkbox:checked').each(function(i){
				          //val[i] = $(this).val();
				          
//			        	    alert($(this));
//			        	    
//			        	    alert($('#newstatus').val());
//			        	    
			        	    $(this).parent().parent().find('.matchstatus').html($('#newstatus').val());
			        	    
//			        	    $(this).closest("tr").hide();
									        	    
				          
				        });

	  			    $('#status').html("<div class='well'>Match updated successfully!</div>");
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
