<form id="pform" method="post" action="/updateprofile"
	style="margin-top: 30px;">

	<#if cols??>

	<div class="form-group">
		Name:<input type="text" class="form-control"
			placeholder="Profile Name" name="profile" value="${profile.name}"></input>
	</div>

	<div class="form-group">
		<input type="hidden" name="profileid" value="${profile.id}"></input>
	</div>


	<div class="form-group">
		Comment:<input type="text" class="form-control" placeholder="Comment"
			name="comment"<#if
		profile.comment??>value="${profile.comment}"></#if></input>
	</div>

	<div class="form-group">
		Match Threshold:<input type="text" class="form-control"
			placeholder="Match Threshold (0-100 default is 50)" name="thres"<#if
		profile.threshold??>value="${profile.threshold}"></#if></input>
	</div>

	<div class="form-group">
		Fuzzy Threshold:<input type="text" class="form-control"
			placeholder="Fuzzy Threshold (0-1 default is 0.8)" name="fuzzy"<#if
		profile.fuzzy??>value="${profile.fuzzy}"></#if></input>
	</div>

	<#if lists??>

	<div class="form-group">
		<select name="list"> <#list lists as l>
			<option value="${l.id}"<#if
				profile.listid?? && l.id?c==profile.listid > selected="${l.name}"
				</#if> > <b>${l.name}</b><#if
				l.description??>-${l.description}</#if>
			</option> </#list> <#if profile.listid??> <#else>
			<option value="null" selected="Please select a list!"><b>Please
					select a list!</b>
			</option> </#if>


		</select>
	</div>

	</#if>

	<table class="table-striped">
		<tr>
			<td>Field</td>
			<td>Unique</td>
			<td>Screening</td>
			<td>Weight</td>
		</tr>

		<#list fld as f>
		<tr>
			<td><b>${f.name}</b></td>

			<td><input type="checkbox" name="${f.name}_unique"<#if
				f.uvalue??> value="${f.uvalue}" checked </#if> ></td>


			<td><input type="checkbox" name="${f.name}_screen"<#if
				f.svalue??> value="${f.svalue}" checked </#if> ></td>

			<td><input type="text" name="${f.name}_weight" size="5"<#if
				f.wvalue??> value="${f.wvalue}" </#if> ></td>


		</tr>
		</#list>
	</table>
	</#if>

	<button type="submit" class="btn btn-default">Update Profile</button>
	<a href="javascript:history.back()" class="btn btn-default">Back</a>
</form>
