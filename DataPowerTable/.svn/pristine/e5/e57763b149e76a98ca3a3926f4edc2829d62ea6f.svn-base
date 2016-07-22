<!DOCTYPE html>

<html>
<head>
<title>All User List</title>

</head>

<body>

	<#include "menu.ftl">

	<br>
	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Manage Users</strong>
			</div>

			<div class="panel-body">
			</div>

			<#if recs??>
			<table class="table table-striped table-hover">
				<tr>
					<td>Client ID</td>
					<td>Status</td>
					<td>Client Name</td>
					<td>Email</td>
					<td>Work Phone</td>
					<td>Mobile Phone</td>
					<td>Address</td>
					<td>Add User</td>
				</tr>

				<#list recs as match>
				<tr>
					<td><#if match["id"]??>${match["id"]}</#if></td>
					<td><#if match["status"]??>${match["status"]}</#if></td>
					<td><#if match["clientname"]??>${match["clientname"]}</#if></td>
					<td><#if match["email"]??>${match["email"]}</#if></td>
					<td><#if match["workphone"]??>${match["workphone"]}</#if></td>
					<td><#if match["mobilephone"]??>${match["mobilephone"]}</#if></td>
					<td><#if match["address"]??>${match["address"]}</#if></td>
					<td><a href="/signup?clientid=${match['id']}"><i class="fa fa-fw fa-user"></i> Add User</a></td>
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
