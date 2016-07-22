<!DOCTYPE html>

<html>
<head>
<title>Reset Password</title>
</head>

<body>

	<#include "menu.ftl">

	<script src="/js/jquery.form.js"></script>
    <script src="/js/resetpwd.js"></script>

	<div class="container">

		<div id="message" class="well" >			
            
		</div>

         

		<div class="panel panel-default">

			<div class="panel-heading">
				Reset Password
			</div>

			<div class="panel-body">
			
		<form id="myForm" method="post" action="/resetpwdforce">


			<div class="form-group">
				<input class="form-control" placeholder="New Password" type="password"
					name="newpassword" required></input>
					
				<input type='hidden' name='userid' value="${user['_id']}">
				
			</div>

			<div class="form-group">
				<input class="form-control"
					placeholder="Verify Password" type="password" name="verify"
					required></input>
			</div>

			<button type="submit" class="btn btn-primary">Save Changes</button>
            <a href="javascript:history.back()" class="btn btn-default">Cancel</a>


		</form>
			</div>  <!-- body -->
		</div>  <!-- panel -->

	</div>

	<#include "footer.ftl">
</body>

</html>
