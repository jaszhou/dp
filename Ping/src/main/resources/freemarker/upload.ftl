<!DOCTYPE html>

<html>
<head>
<title>File Upload</title>

</head>

<body>

	<#include "menu.ftl">

	<h2>Batch ${batchid} - ${filename}</h2>

	<p><#if recs??>
	<table>
		<tr>
			<#list cols as col>
			<td><b>${col}</b></td> </#list>
		</tr>
		<#list recs as rec>
		<tr>
			<#list cols as col>
			<td>${rec[col]}</td> </#list>
		</tr>
		</#list>
	</table>
	</#if>

	<form method="post" action="/screen" style="margin-top: 30px;">

		<div class="form-group">
			<input type="hidden" name="batchid" value="${batchid}"></input>
		</div>

		<button type="submit" class="btn btn-default">Start Batch
			Screening</button>

	</form>


	<#include "footer.ftl">
</body>

</html>
