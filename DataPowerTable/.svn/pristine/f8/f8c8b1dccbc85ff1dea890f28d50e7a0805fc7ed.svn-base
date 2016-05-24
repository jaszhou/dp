// prepare the form when the DOM is ready 

$(function() {

	$('#export').click(function(event) {
		event.preventDefault();
		var url = $(this).attr('href');

//		alert(url);

		
		$.ajax({
			url : url,
			data : '{}',
			type : 'get',
			success : function(data) {

//				 alert(url);
//				 alert(data);

				var uri = "data:text/csv;charset=utf-8," + escape(data);
				
				var downloadLink = document.createElement("a");
				downloadLink.href = uri;
				downloadLink.download = "data.csv";

				document.body.appendChild(downloadLink);
				downloadLink.click();
				document.body.removeChild(downloadLink);
				 
				// window.open( );
			}
		})
	});

});

function DownloadJSON2CSV(objArray)
{
    var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;

    var str = '';

    for (var i = 0; i < array.length; i++) {
        var line = '';

        for (var index in array[i]) {
            line += array[i][index] + ',';
        }

        // Here is an example where you would wrap the values in double quotes
        // for (var index in array[i]) {
        //    line += '"' + array[i][index] + '",';
        // }

        line.slice(0,line.Length-1); 

        str += line + '\r\n';
    }
    window.open( "data:text/csv;charset=utf-8," + escape(str))
}

