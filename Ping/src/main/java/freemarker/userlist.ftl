<!DOCTYPE html>

<html>
<head>
<title>All User List</title>

</head>

<body>

	<#include "menu.ftl">

	<br>
	<div class="container">

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Manage Users</strong>
			</div>

			<div class="panel-body">
			</div>

			<#if recs??>
			<table class="table table-striped table-hover">
				<tr>
					<td>User ID</td>
					<td>Status</td>
					<td>First Name</td>
					<td>Last Name</td>
					<td>Client Name</td>
					<td>Department</td>
					<td>Work Phone</td>
					<td>Mobile Phone</td>
					<td>Role</td>
					<td>Activate/Deactivate</td>
					<td>Reset Password</td>
				</tr>

				<#list recs as match>
				<tr>
					<td><a href="/updateuser?userid=${match['_id']}">${match["_id"]}</td>
					<td>${match["status"]}</td>
					<td><#if match["firstname"]??>${match["firstname"]}</#if></td>
					<td><#if match["lastname"]??>${match["lastname"]}</#if></td>
					<td><#if match["clientname"]??>${match["clientname"]}</#if></td>
					<td><#if match["department"]??>${match["department"]}</#if></td>
					<td><#if match["workphone"]??>${match["workphone"]}</#if></td>
					<td><#if match["mobilephone"]??>${match["mobilephone"]}</#if></td>
					<td>
                      <#if match["role"]??>
                      [
                       <#list match["role"] as role>
                        ${role},
                       </#list>
                      ]
                      </#if></td>
					<td>
                      <#if match["status"] == "Inactive">
                      
                       <a class="selectp" href="/adminactivate?username=${match['_id']}" class="btn btn-default">Activate</a>
                      
                      <#else>
                      
                       <a class="selectp" href="/admindeactivate?username=${match['_id']}" class="btn btn-default">Deactivate</a>
                       
                      </#if>
                    </td>
                    <td><a href="/pwdoverride?userid=${match['_id']}">Reset</a></td>
				</tr>

				</#list>

			</table>
			</#if>

           <div id="message"></div>

		</div>
		<!-- panel -->

	</div>
	<!--  container -->

	<#include "footer.ftl">
	
  <script>
	$(function() {
	$('.selectp').click(function(event) {
		event.preventDefault();
		var url = $(this).attr('href');
		$.ajax({
			url : url,
			data : '{}',
			type: 'POST',
			success : function(data) {
			
			   //alert("activate user");

				// Your Code here
				$('#message').hide(1000);
				$('#message').hide("slow");
				$('#message').html("<h2>User has been activated successfully!</h2>");
				
				window.location.href = "/userlist";
			}
		})
	});

	 });
	</script>
	
</body>

</html>
