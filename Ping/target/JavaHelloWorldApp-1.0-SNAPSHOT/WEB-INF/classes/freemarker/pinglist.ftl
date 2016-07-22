<!DOCTYPE html>

<html>
<head>
<title>All Alert List</title>

</head>

<body>

	<#include "menu.ftl">

	<br>
	<div class="container">

		<#assign aDateTime = .now>
		<#assign aDate = aDateTime?date>
		<#assign aTime = aDateTime?time>
		
	

        <div class='well'><strong>Ping Alert List - ${aDate} </strong></div>
			
			

			<#if recs??>
			<table class="table table-striped table-hover">
				<tr>
					<td>Title</td>
					<td>Edit</td>
					<td>Project</td>
					<td>Production/Non-production</td>
					<td>Issue</td>
					<td>Investigation</td>
					<td>Status</td>
					<td>Date</td>
					
					
				</tr>

				<#list recs as match>
				<tr>
					
				
					<td><#if match["title"]??>${match["title"]}</#if></td>
						<td><a href="/alert?id=${match["_id"]}"><span
								class="glyphicon glyphicon-pencil"></span></a></td>
				
					<td><#if match["project"]??>${match["project"]}</#if></td>
					<td><#if match["production"]??>${match["production"]}</#if></td>
					<td><#if match["body"]??>${match["body"]}</#if></td>
					<td><#if match["action"]??>${match["action"]}</#if></td>
					<td><#if match["status"]??>${match["status"]}</#if></td>
					<td>${match["date"]?date}</td>
					
					
			
				</tr>

				</#list>

			</table>
			</#if>


	</div>
	<!--  container -->

	<#include "footer.ftl">
	
  
	
</body>

</html>
