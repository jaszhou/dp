<!doctype HTML>
<html
<head>
<title>${post["title"]}</title>
</head>
<body>

	<#include "menu2.ftl">



	<div class="container">

		<h2>${post["title"]}</h2>
		Posted ${post["date"]?datetime}<i> By ${post["author"]}</i><br>


		${post["body"]}
		<p>
			<em>Filed Under</em>: <#if post["tags"]??> <#list post["tags"] as
			tag> ${tag} </#list> </#if>
		<p>Comments:
		<ul>
			<#if post["comments"]??> <#assign numComments =
			post["comments"]?size> <#else> <#assign numComments = 0> </#if> <#if
			(numComments > 0)> <#list 0 .. (numComments -1) as i> Author:
			${post["comments"][i]["author"]}
			<br>
			<br> ${post["comments"][i]["body"]}
			<br>
			<hr>
			</#list> </#if>
			<h3>Add a comment</h3>

			<form action="/newcomment" method="POST">
				<input type="hidden" name="permalink" , value="${post["permalink"]}">
				${errors!""}<br> <b>Name</b> (required)<br> <input
					type="text" name="commentName" size="60" value="${comment["name"]}"><br>
				<b>Email</b> (optional)<br> <input type="text"
					name="commentEmail" size="60" value="${comment["email"]}"><br>
				<b>Comment</b><br>
				<textarea name="commentBody" cols="60" rows="10">${comment["body"]}</textarea>
				<br>

				<button type="submit" class="btn btn-default">Add Comment</button>

			</form>
		</ul>

	</div>
	<#include "footer.ftl">
</body>
</html>


