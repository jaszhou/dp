<!DOCTYPE html>

<html>
<head>
<title>Look Up PLM</title>
<link rel="stylesheet" href="/css/style.css">
<link rel="stylesheet" href="/css/bootstrap.min.css">

</head>

<body>

	<#include "menu.ftl">

	<div class="container">

		<p>
		<h2>Input person's name or phone number</h2>
		<form id="abn" method="post" style="margin-top: 30px;">

			<div class="form-group">
				<input type="text" class="form-control" placeholder="Full Name"
					name="name"></input>
			</div>
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Home Phone"
					name="home_phone"></input>
			</div>
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Mobile Phone"
					name="mobile_phone"></input>
			</div>

			<div class="form-group">
				<input type="text" class="form-control" placeholder="Address"
					name="address"></input>
			</div>

			<button type="submit" class="btn btn-default">Search</button>

		</form>


		<p><#if plms?? > <#list plms as plm>
		<h2>${plm["NAME"]}</h2>

		ID - ${plm["id"]?c} <br> Address - ${plm["ADDRESS1"]} <br>
		Home Phone - ${plm["Home Phone"]} <br> Mobile Phone -
		${plm["Mobile Phone"]} <br> Listing Reason - ${plm["Listing
		Reason"]} <br>
		<hr>
		<p></#list> </#if>
	</div>

	<#include "footer.ftl">
</body>

</html>
