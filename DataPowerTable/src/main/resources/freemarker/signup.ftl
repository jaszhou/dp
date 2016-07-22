<!DOCTYPE html>

<html>
<head>
<title>Sign Up</title>

</head>

<body>

	<#include "menu2.ftl">
	<script src="/js/jquery.form.js"></script>
    <script src="/js/signup.js"></script>
	

	<div class="container">

	   <div class="panel panel-default">

			<div class="panel-heading">
				<strong>Signup</strong> 
			</div>

		
		<div class="panel-body">
		
		<div id="status"></div>


		<form id="clientForm" method="post" style="margin-top: 30px;">

			<#if username_error?? ><b>${username_error}</b> </#if>

			<div class="form-group">
				User Name: <input type="text" class="form-control" placeholder="User Name"
					name="username" required></input>
					
			    <input type='hidden' name='status' value='Inactive'></input>
			    
			</div>
			<div class="form-group">
				Password: <input class="form-control" placeholder="Password" type="password"
					name="password" required></input>
			</div>

			<div class="form-group">
				Confirm Password: <input type="password" class="form-control"
					placeholder="Verify Password" type="password" name="verify"
					required></input>
			</div>


			<div class="form-group">
				First Name: <input type="text" class="form-control" placeholder="First Name"
					name="firstname" required></input>
			</div>

			<div class="form-group">
				Last Name: <input type="text" class="form-control" placeholder="Last Name"
					name="lastname" required></input>
			</div>


			<div class="form-group">
				Email(will be used to active new account): <input type="text" class="form-control"
					placeholder="Email(required)" name="email" required></input>

			</div>

			<div class="form-group">
				Work Phone(optional): <input type="text" class="form-control"
					placeholder="Work Phone(optional)" name="workphone"></input>

			</div>

			<div class="form-group">
				Mobile Phone(optional): <input type="text" class="form-control"
					placeholder="Mobile Phone(optional)" name="mobilephone"></input>

			</div>


          <#if client?? > 

			<div class="form-group">
				Client: <input type="text" class="form-control" placeholder="Client:${client['clientname']}"
					name="client" value="${client['clientname']}" > </input>

			</div>
         </#if>

			<div class="form-group">
				Department(optional): <input type="text" class="form-control" placeholder="Department"
					name="department"></input>

			</div>


			<div class="form-group">
				<label for="role">Roles:</label> <br> 
								
				<input type="checkbox" name="role2" value="User" checked>
				Application User

			</div>


			<button type="submit" class="btn btn-primary">Signup</button>



		</form>

	  </div> <!-- panel body -->
     </div> <!-- panel -->

	</div>

	<#include "footer.ftl">
</body>

</html>
