<!DOCTYPE html>

<html>
<head>
<title>File Uploaded</title>
<link rel="stylesheet" href="/css/style.css">
<link rel="stylesheet" href="/css/bootstrap.min.css">

</head>

<body>

	<#include "menu.ftl">



	<p>
	<div class="row">
		<div class="col-md-6 col-md-offset-3">

			<h2>Profile ${profileid} - created by ${profile.creator}</h2>

			<script type="text/javascript"
				src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
			<script src="http://malsup.github.com/jquery.form.js"></script>

			<script>
				// wait for the DOM to be loaded 
				$(document).ready(function() {
					// bind 'myForm' and provide a simple callback function 
					$('#pform').ajaxForm(function() {
						$('#pform').reload();
					});
				});
			</script>



			<span id="frmProfile">

				<form id="pform" method="post" action="/updateprofile"
					style="margin-top: 30px;">

					<#if cols??>

					<div class="form-group">
						Name:<input type="text" class="form-control"
							placeholder="Profile Name" name="profile" value="${profile.name}"></input>
					</div>

					<div class="form-group">
						Last Update:<input type="text" class="form-control"
							placeholder="Last Update" name="profile" disabled
							value="${profile.date?datetime}"></input>
					</div>

					<div class="form-group">
						<input type="hidden" name="profileid" value="${profile.id}"></input>
					</div>


					<div class="form-group">
						Comment:<input type="text" class="form-control"
							placeholder="Comment" name="comment"<#if
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
								profile.listid?? && l.id?c==profile.listid >
								selected="${l.name}" </#if> > <b>${l.name}</b><#if
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

					<button type="submit" class="btn btn-default">Update
						Profile</button>
					<a href="javascript:history.back()" class="btn btn-default">Back</a>
				</form>

			</span>

		</div>
	</div>

	<#include "footer.ftl">
</body>

</html>
