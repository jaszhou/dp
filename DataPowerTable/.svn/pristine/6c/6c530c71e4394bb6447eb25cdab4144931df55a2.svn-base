<!doctype HTML>
<html>
<head>
<title>Create a new post</title>
</head>
<body>

	<#include "menu.ftl">

	<div class="container">

		<form action="/newpost" method="POST">
			${errors!""}
			<h2>Title</h2>
			<input type="text" name="subject" size="60" value="${subject!""}"><br>

			<h2>Blog Entry</h2>

			<textarea name="body" cols="60" rows="10">${body!""}</textarea>
			<br>

			<h2>Tags</h2>
			Comma separated, please <br> <input type="text" name="tags"
				size="60" value="${tags!""}"><br> <br>

			<button type="submit" class="btn btn-default">Post Blog</button>
	</div>

	<#include "footer.ftl">
</body>
</html>

