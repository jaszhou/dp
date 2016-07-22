// prepare the form when the DOM is ready 

$(function() {

	// on page load
	
	$('.rule').click(function(event) {
		//event.preventDefault();
//		var url = '/searchruleform';
		var url = $(this).attr('data-href');
//		alert(url);

		$.ajax({
			cache: false,
			url : url,
//			data : 'resultid='+this.value,
			success : function(data) {

				//alert(url);
				//alert(data);
				
				
				$('#txtHint').html(data);
				  
			}
		})
	});
	
});


