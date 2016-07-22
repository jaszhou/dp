<!DOCTYPE html>

<html>
<head>
<title>Create A New Client</title>
</head>

<body>

	<#include "menu.ftl">
    <script src="/js/jquery.form.js"></script>
    <script src="/js/addclient.js"></script>

	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Create a new client</strong> 
			</div>

		
		<div class="panel-body">
		
		<div id="status"></div>

		<form id="clientForm" method="post" style="margin-top: 30px;">

			<div class="form-group">
				<input type="text" class="form-control" placeholder="Client Name"
					name="clientname" required></input>
			</div>

			<div class="form-group">
				<select name="status" required>
					<option value="Active" selected>Active</option>
					<option value="Inactive">Inactive</option>
				</select>
			</div>

			<div class="form-group">
				<input type="text" class="form-control"
					placeholder="Email(optional)" name="email"></input>

			</div>

			<div class="form-group">
				<input type="text" class="form-control"
					placeholder="Work Phone(optional)" name="workphone"></input>

			</div>

			<div class="form-group">
				<input type="text" class="form-control"
					placeholder="Mobile Phone(optional)" name="mobilephone"></input>

			</div>

			<div class="form-group">
				<input type="text" class="form-control" placeholder="Address(optional)"
					name="address"></input>

			</div>


			<button type="submit" class="btn btn-default">Signup</button>

		</form>

	  </div> <!-- panel body -->
     </div> <!-- panel -->

	</div> <!-- container -->

	<#include "footer.ftl">
	
</body>

</html>
