// prepare the form when the DOM is ready 

$(function() {

	// on page load
	
	$('#selectbatch').on('change',function() {
		//event.preventDefault();
		var url = '/searchmatchform';

//		alert(url);

		$.ajax({
			cache: false,
			url : url,
			data : 'batchid='+this.value,
			success : function(data) {

				//alert(url);
				//alert(data);
				
				
				$('#txtHint').html(data);
				  
			}
		})
	});
	
});


