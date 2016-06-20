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

	<#include "menu.ftl">

	<script src="/js/jquery.form.js"></script>
	<script src="/js/newplm.js"></script>

	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Import a new list file</strong>
			</div>

			<div class="panel-body">
			<span class="glyphicon icon-bell"></span>
				<p> <span class="glyphicon glyphicon-bell"></span> Format requirement:
				<ul>
					<li>The list file should be in <mark>.csv</mark> format
					</li>

					<li>The first line of the batch file contains the field names
					</li>

					<li>The field names must not contain character '.'</li>

				</ul>
				</p>






				<form method="POST" id="myForm" action="/createplm"
					style="margin-top: 30px;" enctype="multipart/form-data">


					<div class="form-group">

						<input type="text" class="form-control" placeholder="List Name"
							name="listname" required> </input> <input type="text"
							class="form-control" placeholder="Description" name="description">
						</input>
					</div>

					<div class="form-group">
						<input type="file" class="form-control"
							placeholder="Upload a file" name="upload" size="50" accept=".csv">
						</input>

					</div>

					<button type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-upload"></span> Create New
						List</button>

					<a id="selectp" href="/plmlist" class="btn btn-default">View
						Lists</a>

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
