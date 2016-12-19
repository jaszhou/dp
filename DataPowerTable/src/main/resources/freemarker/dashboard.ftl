<!DOCTYPE html>

<html>
<head>
<title>Dash Board</title>

</head>

<body>

	<#include "menu.ftl">

	<br>
	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Search for instance</strong>
			</div>

			<div class="panel-body">
				<form method="post" action="/searchaction" style="margin-top: 30px;">


				<div class="form-group">

						<input type="text" class="form-control" placeholder="Instance #"
							name="Instance #" required> </input> 
			    </div>

				<div class="form-group">
					<input type="hidden" class="form-control" name="listname"
						value="${listname}"></input>
				</div>


				<button type="submit" class="btn btn-primary">
					<span class="glyphicon glyphicon-search"></span> Search Instance
				</button>
				
				</form>
		    </div>
			</div>

			<#if recs??>
			<table class="table table-striped table-hover">
				<tr>
					<td>Instance#</td>
					<td>Client Name</td>
					<td>Creator</td>
					<td>Date</td>
					<td>Description</td>
					<td>Number of Entities</td>
					<td>Search</td>
				</tr>

				<#list recs as match>
				<tr>
					<td>${match["id"]?c}</td>
					<td><a href="/list?id=${match["id"]}&name=${match["name"]}">${match["name"]}</a></td>
					<td><#if match["creator"]??>${match["creator"]}</#if></td>
					<td>${match["date"]?datetime}</td>
					<td><#if match["description"]??>${match["description"]}</#if></td>
					<td>${match["size"]?c}</td>
					<td><a href="/searchentity?listid=${match["id"]}&listname=${match["name"]}" ><span
							class="glyphicon glyphicon-search"></span></a></td>
				</tr>

				</#list>

			</table>
			</#if>

		</div>
		<!-- panel -->

	</div>
	<!--  container -->

	<#include "footer.ftl">
</body>

</html>
