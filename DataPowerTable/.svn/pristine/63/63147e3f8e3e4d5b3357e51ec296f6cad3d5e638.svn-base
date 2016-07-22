<!DOCTYPE html>

<html>
<head>
<title>User Action History</title>
</head>

<body>

	<#include "menu.ftl">

	<div class="container">

		<#if actions??>


		<table class="table table-striped table-hover">
			<tr>
				<td>Match ID</td>
				<td>New Status</td>
				<td>User</td>
				<td>Date</td>
				<td>Comment</td>
			</tr>

			<#list actions as match>
			<tr>
				<td>${match["matchid"]}</td>
				<td>${match["newstatus"]}</td>
				<td><#if match["user"]??> ${match["user"]} </#if></td>
				<td>${match["updateTime"]?datetime}</td>
				<td>${match["comment"]}</td>
			</tr>

			</#list>

		</table>
		</#if>

	</div>
	<#include "footer.ftl">
</body>

</html>
