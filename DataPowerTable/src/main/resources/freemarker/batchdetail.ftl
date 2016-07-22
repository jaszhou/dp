<!DOCTYPE html>

<html>
<head>
<title>Batch Detail</title>

</head>

<body>

	<#include "menu.ftl"> <#include "micro.ftl">

	<script src="/js/jquery.form.js"></script>
	<script src="/js/screen.js"></script>

	<#import "pagination.ftl" as pagination />

	<div class="container">



		<div class="panel panel-default">
			<div class="panel-heading">
				<strong>Batch ID: ${batchid}</strong> File Name: <strong>${filename}</strong> - batch
				screening step 4
			</div>
		</div>

		<form id="frmBatch" method="post" action="/screen">

 		   <div class="form-group">

			<a href="/addprofile?batchid=${batchid}" class="btn btn-primary">Generate
				Profile</a> <#if profiles??> <a id="selectp"
				href="/getprofilelist?batchid=${batchid}" class="btn btn-primary">Select
				Profile for Screening</a>
			<button id="sbutton" type="submit" class="btn btn-primary">Start
				Batch Screening</button>

			</div>

			<div id='loadingmessage' style='display: none'>
				<br>
				<div class="alert alert-success">
					<img src='images/ajax-loader.gif' /> Screening in process, please
					wait ...
				</div>
			</div>

			</#if> <br>

			<div id="profilelist"></div>


			<#if recs??>


			<table class="table table-striped table-hover">
				<tr>
					<#list recs[0]?keys as key>
					<td><b>${key}</b></td> </#list>
				</tr>
				<#list recs as rec>
				<tr><@dumpPropertiesRow rec />
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

			<div class="form-group" style="display: none;">
				<input type="text" name="batchid" value="${batchid}"></input>
			</div>


			<#-- -->


		</form>

	</div>
	<#include "footer.ftl">
</body>

</html>
