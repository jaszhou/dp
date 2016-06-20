<!DOCTYPE html>

<html>
<head>
<title>All Event List</title>

</head>

<body>

	<#include "menu.ftl">

	<br>
	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>View Events</strong>
			</div>

			<div class="panel-body">
			</div>

			<#if recs??>
			<table class="table table-striped table-hover">
				<tr>
					<td>User Name</td>
					<td>Event</td>
					<td>Type</td>
					<td>Date</td>
				</tr>

				<#list recs as match>
				<tr>
					<td><#if match["username"]??>${match["username"]}</#if></td>
					<td><#if match["event"]??>${match["event"]}</#if></td>
					<td><#if match["type"]??>${match["type"]}</#if></td>
					<td><#if match["time"]??>${match["time"]?datetime}</#if></td>
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
