<!DOCTYPE html>

<html>
<head>
<title>All Lists</title>

</head>

<body>

	<#include "menu.ftl">

	<br>
	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Manage Lists</strong>
			</div>

			<div class="panel-body">
				<p>
					You may <a href="/newplm"><i class="fa fa-cloud-upload"></i> upload</a> a .csv file to create a new
					list.
				</p>
			</div>

			<#if recs??>
			<table class="table table-striped table-hover">
				<tr>
					<td>List ID</td>
					<td>List Name</td>
					<td>Creator</td>
					<td>Date</td>
					<td>Description</td>
					<td>Number of Entities</td>
					<td>Search</td>
				</tr>

				<#list recs as match>
				<tr>
					<td>${match["id"]?c}</td>
					
					<td><a href="/dashboard?name=${match["name"]}">${match["name"]}</a></td>
					
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
