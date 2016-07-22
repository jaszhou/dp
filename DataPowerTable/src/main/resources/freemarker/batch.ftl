<!DOCTYPE html>

<html>
<head>
<title>Upload A Batch File</title>

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
	<script src="/js/batch.js"></script>

	<br>
	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Upload a batch file</strong> - batch screening step 1
			</div>

			<div class="panel-body">
				<p><span class="glyphicon glyphicon-bell"></span> Format requirement:</p>

				<ul>
					<li>
						<p>The batch file should be in .csv format</p>
					</li>

					<li>
						<p>The first line of the batch file contains the field names</p>
					</li>

					<li>
						<p>The field names must not contain character '.'</p>
					</li>

				</ul>



				<form id="myForm" method="POST" action="/upload"
					style="margin-top: 30px;" enctype="multipart/form-data">

					<div class="form-group">
						<input type="file" class="form-control"
							placeholder="Upload a file" name="upload" size="50" required></input>
					</div>

					<button type="submit" value="Upload" class="btn btn-primary"><i class="fa fa-cloud-upload"></i> Upload</button>

				</form>


				<p>
				<div class="progress">
					<div class="bar"></div>
					<div class="percent">0%</div>
				</div>

				<div id="status"></div>


			</div>
			<!-- panel-body -->

		</div>
		<!-- panel -->

	</div>
	<!-- container -->
	</div>
	<!-- mainCol -->

	<div class="col-md-1" id="rightCol"></div>
	<!-- rightCol -->

	</div>
	<!-- row -->


	<#include "footer.ftl">
</body>

</html>
