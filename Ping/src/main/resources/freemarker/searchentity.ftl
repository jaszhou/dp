<!DOCTYPE html>

<html>
<head>
<title>Search Entity</title>
</head>

<body>

	<#include "menu.ftl">


	<div class="row">
		<div class="col-md-6 col-md-offset-3">

			<h2>Search Entity</h2>

			<#macro listProperties node> <#list node?keys as key> <#assign value
			= node[key]> <input type="text" class="form-control"
				placeholder="${key}" name="${key}"></input> </#list> </#macro>



			<form method="post" action="/searchaction" style="margin-top: 30px;">


				<div class="form-group"><@listProperties listdoc /></div>

				<div class="form-group">
					<input type="hidden" class="form-control" name="listname"
						value="${listname}"></input>
				</div>


				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-search"></span> Search
				</button>
		</div>

	</div>
	<#include "footer.ftl">
</body>

</html>
