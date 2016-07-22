<!DOCTYPE html>

<html>
<head>
<title>Ping Alert Report</title>


</head>

<body>

	<#include "menu.ftl">

<style>
table, th, td {
    border: 1px solid black;
}

th, td {
    padding: 5px;
    vertical-align:middle;
}
</style>


	<br>
	<div class="container">

		<#assign aDateTime = .now>
		<#assign aDate = aDateTime?date>
		<#assign aTime = aDateTime?time>
		
	

        <div class='well'><strong>Ping Alert Report - ${aDate} </strong></div>
			
			<h2>Production:</h2>

			<#if recs??>
			<table>
				<tr>
					<th>Instance</th>
					<th>Issue</th>
					<th>Action</th>
					<th>Date</th>
					
				</tr>

				<#list recs as match>
				<tr>
					
				
					<td><#if match["title"]??>${match["title"]}</#if></td>
					<td><#if match["project"]??><p class="bg-warning">${match["project"]}</p></#if></td>
				
					<td><#if match["action"]??><p class="bg-primary">${match["action"]}</p></#if></td>
					
			<td><#if match["date"]??>${match["date"]?date}</#if></td>
					
				</tr>

				</#list>

			</table>
			</#if>

			<h2>Non-Production:</h2>

			<#if non_recs??>
			<table border='1'>
				<tr>
					<th>Instance</th>
					<th>Issue</th>
					<th>Action</th>
					<th>Date</th>
					
				</tr>

				<#list non_recs as match>
				<tr>
					
				
					<td><#if match["title"]??>${match["title"]}</#if></td>
					<td><#if match["project"]??><p class="bg-warning">${match["project"]}</p></#if></td>
				
					<td><#if match["action"]??><p class="bg-primary">${match["action"]}</p></#if></td>
					
			<td><#if match["date"]??>${match["date"]?date}</#if></td>
					
				</tr>

				</#list>

			</table>
			</#if>

     <br>

	</div>
	<!--  container -->

	<#include "footer.ftl">
	
  
	
</body>

</html>
