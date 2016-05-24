<!DOCTYPE html>
<html>
<head>
<title>Cloud Screening Blog</title>
</head>
<body>

	<#include "menu.ftl">

	<div class="container">

		<h1>Company Blog</h1>

		<#list myposts as post>
		<h2>
			<a href="/post/${post["permalink"]}">${post["title"]}</a>
		</h2>
		Posted ${post["date"]?datetime} <i>By ${post["author"]}</i><br>
		Comments: <#if post["comments"]??> <#assign numComments =
		post["comments"]?size> <#else> <#assign numComments = 0> </#if> <a
			href="/post/${post["permalink"]}">${numComments}</a>
		<hr>
		${post["body"]!""}
		<p>
		<p>
			<em>Filed Under</em>: <#if post["tags"]??> <#list post["tags"] as
			tag> ${tag} </#list> </#if>
		<p></#list>
	</div>

	<#include "footer.ftl">
</body>
</html>

