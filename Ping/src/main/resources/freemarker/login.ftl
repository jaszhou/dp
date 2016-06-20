<!DOCTYPE html>

<html>
<head>
<title>Login</title>


</head>

<body>

	<#include "menu2.ftl">


	<div class="container">

		Need to Create an account? <a href="/signup">Signup</a>
		<p>
		<h2>Login</h2>

        <#if login_error?? ><b>${login_error}</b> </#if>

		<form method="post" style="margin-top: 30px;">

			<div class="form-group">
				<input type="text" class="form-control" placeholder="User Name"
					name="username" required></input>
			</div>
			<div class="form-group">
				<input type="password" class="form-control" placeholder="Password"
					name="password" required></input>
			</div>

			<button type="submit" class="btn btn-primary">Login</button>
		</form>


	</div>

	<#include "footer.ftl">
</body>

</html>
