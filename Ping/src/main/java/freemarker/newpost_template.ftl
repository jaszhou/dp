<!doctype HTML>
<html>
<head>
<title>Create a new post</title>
</head>
<body>

	<#include "menu.ftl">

	<div class="container">
	
	<div class='well'><strong>Add Ping Alert</strong></div>

		<form action="/newpost" method="POST">
			${errors!""}
			
			<div class="form-group">
				
						<input type="text"class="form-control" placeholder="Subject"  name="subject" size="60" value="${subject!""}"></input>
						
						
			</div>
			<div class="form-group">
			
					   
			<input type="text" name="project" class="form-control" placeholder="Summary" size="60" value="${project!""}"></input>
			
			
			</div>
			
			<div class="form-group">
				
					 
			<input type="text" name="production" size="60" class="form-control" placeholder="Is Production?" value="${production!""}"></input>
			
			</div>
			
			
			<div class="form-group">
				
					
					<textarea name="body" class="form-control" placeholder="Issue"  cols="60" rows="10">${body!""}</textarea>
					
					
			</div>
			
			
			<div class="form-group">
				
			<input type="text" name="action" class="form-control" placeholder="Action/Comment" size="60" value="${action!""}"></input>
			
			
			</div>
			<div class="form-group">
				
		   
			<input type="text" name="status" size="60" class="form-control" placeholder="Status" value="${status!""}"></input>
			
			
			</div>
			<div class="form-group">
			
			<input type="text" name="tags"
				size="60" class="form-control" placeholder="Tags, Comma separated" value="${tags!""}"></input>
					
					
					
			</div>
			
			
						<button type="submit" class="btn btn-default">Post Blog</button>
	</div>

	<#include "footer.ftl">
</body>
</html>

