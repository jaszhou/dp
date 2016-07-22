<!doctype HTML>
<html>
<head>
<title>Edit Alert</title>
</head>
<body>

	<#include "menu.ftl">

	<div class="container">
	
	<div class='well'><strong>Edit Ping Alert</strong></div>

    <form id="myForm" method="post" action="/editalert">

		
			${errors!""}
			<input type="hidden" name="id" value="${alert['_id']}"></input>
			
			<div class="form-group">
				
						<input type="text"class="form-control" placeholder="Subject"  name="subject" size="60" value="${alert['title']}"></input>
						
						
			</div>
			<div class="form-group">
			
					   
			<input type="text" name="project" class="form-control" placeholder="Summary" size="60" value="${alert['project']}"></input>
			
			
			</div>
			
			<div class="form-group">
				
					 
			<input type="text" name="production" size="60" class="form-control" placeholder="Is Production?" value="${alert['production']}"></input>
			
			</div>
			
			
			<div class="form-group">
				
					
					<textarea name="body" class="form-control" placeholder="Issue"  cols="60" rows="10">${alert['body']}</textarea>
					
					
			</div>
			
			
			<div class="form-group">
				
			<input type="text" name="action" class="form-control" placeholder="Action/Comment" size="60" value="${alert['action']}"></input>
			
			
			</div>
			<div class="form-group">
				
		   
			<input type="text" name="status" size="60" class="form-control" placeholder="Status" value="${alert['status']}"></input>
			
			
			</div>
	
	      
			
			
						<button type="submit" class="btn btn-default">Post Blog</button>
	</div>

	<#include "footer.ftl">
</body>
</html>

