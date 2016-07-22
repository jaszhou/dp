<!DOCTYPE html>

<html>
<head>
<title>Inbox</title>
</head>

<body>

	<#include "menu.ftl"> 
    <#include "micro.ftl"> 
    <#import "pagination.ftl" as pagination /> 
    
    <script src="/js/inbox.js"></script>
    <script src="/js/json.js" type="text/javascript"></script>
    
    
    <#if matches??> 


    <#if permalink=="open">
	<ul class="nav nav-tabs">
		<li class="active"><a href="/inbox/open">Open <span class="badge">${matches?size}</span></a></li>
		<li><a href="/inbox/fp">False Positive</a></li>
		<li><a href="/inbox/pbr">Pass by Rule</a></li>
		<li><a href="/inbox/fi">Further Investigation</a></li>
		<li><a href="/inbox/tm">True Match</a></li>
	</ul>
	</#if> 
   <#if permalink=="tm">
	<ul class="nav nav-tabs">
		<li><a href="/inbox/open">Open</a></li>
		<li><a href="/inbox/fp">False Positive </a></li>
		<li><a href="/inbox/pbr">Pass by Rule</a></li>
		<li><a href="/inbox/fi">Further Investigation</a></li>
		<li class="active"><a href="/inbox/tm">True Match <span class="badge">${matches?size}</span></a></li>
	</ul>
	</#if> 
    <#if permalink=="fp">
	<ul class="nav nav-tabs">
		<li><a href="/inbox/open">Open</a></li>
		<li class="active"><a href="/inbox/fp">False Positive <span class="badge">${matches?size}</span></a></li>
		<li><a href="/inbox/pbr">Pass by Rule</a></li>
		<li><a href="/inbox/fi">Further Investigation</a></li>
		<li><a href="/inbox/tm">True Match</a></li>
	</ul>
	</#if> 
    <#if permalink=="pbr">
	<ul class="nav nav-tabs">
		<li><a href="/inbox/open">Open</a></li>
		<li><a href="/inbox/fp">False Positive</a></li>
		<li class="active"><a href="/inbox/pbr">Pass by Rule <span class="badge">${matches?size}</span></a></li>
		<li><a href="/inbox/fi">Further Investigation</a></li>
		<li><a href="/inbox/tm">True Match</a></li>
	</ul>
	</#if> 
    <#if permalink=="fi">
	<ul class="nav nav-tabs">
		<li><a href="/inbox/open">Open</a></li>
		<li><a href="/inbox/fp">False Positive</a></li>
        <li><a href="/inbox/pbr"></a></li>
		<li class="active"><a href="/inbox/fi">Further Investigation <span class="badge">${matches?size}</span></a></li>
		<li><a href="/inbox/tm">True Match</a></li>
	</ul>
	</#if>


	<table class="table table-striped table-hover">
		<tr>
			<td>Match ID</td>
			<td>Batch ID</td>
			<td>Record ID</td>
			<td>Record Sample</td>
			<td>Status</td>
			<td>Last Update Time</td>
		</tr>

		<#list matches as match>
		<tr>
			<td><a href="/matchview?matchid=${match["id"]?c}">${match["id"]?c}</a></td>
			<td>${match["batchid"]} 
               <a href="/searchmatch?batchid=${match["batchid"]}"><span class="glyphicon glyphicon-search"></span></a>
            </td>
			<td><#if match["recid"]??>${match["recid"]}</#if></td>

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
		<li></li>
		<li>    <a  id="export" href="/inboxexportcsv?status=${permalink}" class="btn btn-default pull-right"><span class="glyphicon glyphicon-cloud-download"></span> Export to .CSV file</a> </li>
		<li>    <a  href="/inboxexport?status=${permalink}" class="btn btn-default pull-right"><span class="glyphicon glyphicon-cloud-download"></span> Export to .JSON file</a></li>
		
	</ul>
	<@pagination.counter /> 
       
   </#if> 
  </div>
<#include "footer.ftl">
</body>

</html>
