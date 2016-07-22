<!DOCTYPE html>

<html>
<head>
<title>Screen Result</title>
</head>

<body>

	<#include "menu.ftl"> <#include "micro.ftl"> <#import "pagination.ftl"
	as pagination />

	<div class="container">

		<#if matches??>


		<table class="table table-striped table-hover">
			<tr>
				<td>Batch ID</td>
				<td>Match ID</td>
				<td>Record ID</td>
				<td>Input Name</td>
				<td>Status</td>
				<td>Last Update</td>
			</tr>

			<#list matches as match>
			<tr>
				<td>${match["batchid"]}</td>
				<td><a href="/matchview?matchid=${match["id"]?c}">${match["id"]?c}</a></td>
				<td>${match["recid"]}</td>
				<td><a href="/matchview?matchid=${match["id"]?c}"> <@dumpName
						match["input"] /> </a></td>

				<td>${match["status"]}</td>
				<td>${match["lastUpdate"]?datetime}</td>
			</tr>

			</#list>

		</table>

		<ul class="pagination">
			<li><@pagination.first /></li>
			<li><@pagination.previous /></li>
			<li><@pagination.numbers /></li>
			<li><@pagination.next /></li>
			<li><@pagination.last /></li>
		</ul>
		<@pagination.counter /> </#if>

	</div>
	<#include "footer.ftl">
</body>

</html>
