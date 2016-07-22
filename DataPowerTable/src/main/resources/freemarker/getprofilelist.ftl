<br>
<script>
	$(document).on('change', 'input:radio[name="profileid"]', function(event) {
		//alert("click fired");
		$('#sbutton').show("slow");
	});
</script>

<div class="panel panel-default">
	<div class="panel-heading">Select a screening profile</div>
	<div class="panel-body">

		<#if profiles??>

		<table class="table table-striped table-hover">
			<tr>
				<td>Profile ID</td>
				<td>Profile Name</td>
				<td>Select</td>
				<td>Creator</td>
				<td>Creation Date</td>
				<td>Description</td>
			</tr>
			<#list profiles as profile>
			<tr>
				<td><a href="/profile?profileid=${profile["id"]}">${profile["id"]}</a></td>
				<td><a href="/profile?profileid=${profile["id"]}">${profile["name"]}</a></td>

				<td><input type="radio" class="profiles" name="profileid"
					value=${profile["id"]}></td>

				<td><a href="/profile?profileid=${profile["id"]}">${profile.creator}</a></td>
				<td><a href="/profile?profileid=${profile["id"]}">${profile["date"]?datetime}</a></td>
				<td><#if profile["comment"]??>${profile["comment"]}</#if></td>
			</tr>
			</#list>
		</table>
		</#if>
	</div>
</div>
