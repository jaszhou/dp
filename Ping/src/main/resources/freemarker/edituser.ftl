<!DOCTYPE html>

<html>
<head>
<title>Edit User</title>
</head>

<body>

	<#include "menu.ftl">

	<script src="/js/jquery.form.js"></script>
    <script src="/js/edituser.js"></script>

	<div class="container">

		<div id="message"></div>

		<div class="panel panel-default">

			<div class="panel-heading">
				Edit User Details 
			</div>

			<div class="panel-body">
			
		<form id="myForm" method="post" action="/saveuser">

			<#if username_error?? ><b>${username_error}</b> </#if>

			<div class="form-group">
				<input type="text" class="form-control" placeholder="User Name"
					name="username" value="${username}" disable></input>
			</div>

			<div class="form-group">
				<select name="status" required>
					<option value="Active" selected>Active</option>
					<option value="Inactive">Inactive</option>
				</select>
			</div>

			<div class="form-group">
				<input type="text" class="form-control" placeholder="First Name"
					name="firstname" value="${user['firstname']}" required></input>
			</div>

			<div class="form-group">
				<input type="text" class="form-control" placeholder="Last Name"
					name="lastname" value="${user['lastname']}" required></input>
			</div>


			<div class="form-group">
				<input type="text" class="form-control"
					placeholder="Email(optional)" name="email" <#if user['email']?? > value="${user['email']}" </#if>  >  </input>

			</div>

			<div class="form-group">
				<input type="text" class="form-control"
					placeholder="Work Phone(optional)" name="workphone" <#if user['workphone']?? > value="${user['workphone']}" </#if> ></input>

			</div>

			<div class="form-group">
				<input type="text" class="form-control"
					placeholder="Mobile Phone(optional)" name="mobilephone" <#if user['mobilephone']?? > value="${user['mobilephone']}" </#if> ></input>

			</div>

			<div class="form-group">
				<input type="text" class="form-control" placeholder="Department"
					name="department" <#if user['department']?? > value="${user['department']}" </#if> ></input>

			</div>


            <#assign x = user['role']>


			<div class="form-group">
				<label for="role">Roles:</label> <br> 
				
				<#if x?seq_contains("Administrator")> 
				  <input type="checkbox"
					name="role1" value="Administrator"  checked > Administrator
					
				</#if>

					<br>
				
				<input type="checkbox" name="role2" value="User" <#if x?seq_contains("User")> checked </#if> >
				Application User

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
