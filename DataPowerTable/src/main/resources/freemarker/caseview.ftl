<!DOCTYPE html>

<html>
<head>
<title>Case Review</title>
<link rel="stylesheet" href="/css/style.css">
<link rel="stylesheet" href="/css/bootstrap.min.css">

</head>

<body>

	<#include "menu.ftl"> <#if matches??>
	<table border="1" style="font-size: 16px; width: 80%;">
		<tr>
			<td>Input Detail</td>
			<td>Entity Detail</td>
		</tr>

		<#list matches as match>
		<tr>
			<td>Record ID:<a href="/caseview?${match["recid"]}">${match["recid"]}
					<br></td>



			<td>Entity ID:${match["entity"].id?c} <br> Entity Name:<b>${match["entity"].NAME}</b>
				<br> Entity DOB: ${match["entity"].UNFORMATTED_DOB} <br>
				Entity Address: ${match["entity"].ADDRESS1} <br> Home Phone:
				${match["entity"]["Home Phone"]}<br> Mobile Phone:
				${match["entity"]["Mobile Phone"]} <br> Listing Reason:
				${match["entity"]["Listing Reason"]} <br>
			</td>
		</tr>

		</#list>

	</table>
	</#if> <#include "footer.ftl">
</body>

</html>
