<!DOCTYPE html>

<html>
<head>
<title>Look Up ABN</title>
<link rel="stylesheet" href="/css/style.css">
<link rel="stylesheet" href="/css/bootstrap.min.css">

</head>

<body>

	<#include "menu.ftl">

	<div class="row">
		<div class="col-md-6 col-md-offset-3">

			Need to Create an account? <a href="/signup">Signup</a>
			<p>
			<h2>Input ABN number or business name</h2>
			<form id="abn" method="post" style="margin-top: 30px;">

				<div class="form-group">
					<input type="text" class="form-control" placeholder="ABN Number"
						name="abn_num"></input>
				</div>
				<div class="form-group">
					<input type="text" class="form-control" placeholder="Business Name"
						name="bus_name"></input>
				</div>

				<button type="submit" class="btn btn-default">Search</button>

			</form>


			<p><#if abns??> <#list abns as abn>
			<h2>
				ABN Number:<a href="/lookup?abn_num=${abn['number']}"
					onclick="post('/lookup', {abn_num:${abn['number'] }},'post');">${abn["number"]}</a>
			</h2>

			ABN Name - ${abn["name"]} <br> <a
				href="/lookup?acn_num=${abn['acn']}"> ACN - ${abn["acn"]} </a><br>
			Adress - ${abn["addr"]} <br>
			<hr>
			<p></#list> </#if> <#if acns??> <#list acns as acn>
			<h2>
				ACN Number:<a href="/lookup?acn_num=${acn['ACN']?c}">${acn["ACN"]?c}</a>
			</h2>

			Company Name - ${acn["Company Name"]} <br> Type - ${acn["Type"]}
			<br> Class - ${acn["Class"]} <br> Status - ${acn["Status"]}
			<br> Date of Registration - ${acn["Date of Registration"]} <br>
			Previous State of Registration - ${acn["Previous State of
			Registration"]} <br> State Registration Number - ${acn["State
			Registration Number"]} <br> Modified since last report -
			${acn["Modified since last report"]} <br> Current Name Indicator
			- ${acn["Current Name Indicator"]} <br> ABN - ${acn["ABN"]} <br>
			<hr>
			<p></#list> </#if>
			<p>
				<a
					href="http://data.gov.au/dataset/7b8656f9-606d-4337-af29-66b89b2eeefb/resource/9430b6b2-b28c-4b1a-a58a-a88b54912853/download/asicregisterdownloadhelpfile.pdf"
					target="_blank">Dictionary</a>
		</div>
	</div>

	<#include "footer.ftl">
</body>

</html>
