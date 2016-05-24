 <#if recs??>

<table class="table table-striped table-hover">
	<tr>
		<td>Profile ID</td>
		<td>Profile Name</td>
		<td>Creator</td>
		<td>Creation Date</td>
		<td>Description</td>
		<td>Edit</td>
		<td>Delete</td>
		<td>Search</td>
	</tr>
	<#list recs as rec>
	<tr>
		<td><a href="/profile?profileid=${rec["id"]}">${rec["id"]}</a></td>
		<td><a href="/profile?profileid=${rec["id"]}">${rec["name"]}</a></td>
		<td><a href="/profile?profileid=${rec["id"]}">${rec.creator}</a></td>
		<td><a href="/profile?profileid=${rec["id"]}">${rec["date"]?datetime}</a></td>
		<td><#if rec["comment"]??>${rec["comment"]}</#if></td>
		<td><a href="/profile?profileid=${rec["id"]}"><span
				class="glyphicon glyphicon-pencil"></span></a></td>
		<td><a href="/deleteprofile?profileid=${rec["id"]}" class="delete"><span class="glyphicon glyphicon-trash"></span></a></td>
		<td><a href="/searchprofile?profileid=${rec["id"]}" class="search"><span class="glyphicon glyphicon-search"></span></a></td>
	</tr>
	</#list>
</table>

</#if>

