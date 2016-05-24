<!DOCTYPE html>

<html>
<head>
<title>Import a New List</title>

<style>
.progress {
	position: relative;
	width: 400px;
	border: 1px solid #ddd;
	padding: 1px;
	border-radius: 3px;
}

.bar {
	background-color: #B4F5B4;
	width: 0%;
	height: 20px;
	border-radius: 3px;
}

.percent {
	position: absolute;
	display: inline-block;
	top: 3px;
	left: 48%;
}
</style>

</head>

<body>

	<#include "menu2.ftl">

	<script src="/js/jquery.form.js"></script>
	<script src="/js/newplm.js"></script>

	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Deploy new jar</strong>
			</div>

			<div class="panel-body">
			<span class="glyphicon icon-bell"></span>
				
				<form method="POST" id="myForm" action="/deployjar"
					style="margin-top: 30px;" enctype="multipart/form-data">


					<div class="form-group">

						<input type="text" class="form-control" placeholder="List Name"
							name="listname" value="Jar"> </input> 
						</input>
					</div>

					<div class="form-group">
						<input type="file" class="form-control"
							placeholder="Upload a file" name="upload" size="50" >
						</input>

					</div>

					<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-upload"></span> Deploy </button>

				</form>

				<p>
				<div class="progress">
					<div class="bar"></div>
					<div class="percent">0%</div>
				</div>

				<div id="status"></div>

			</div>
		</div>
	</div>

	<#include "footer.ftl">
</body>

</html>
