<!DOCTYPE html>

<html>
<head>
<title>Profile Detail</title>


</head>

<body>

	<#include "menu.ftl">

    <script src="/js/jquery.form.js"></script>
    <script src="/js/profiledetail.js"></script>

	<!-- Include the plugin's CSS and JS: -->
	<script type="text/javascript" src="/js/bootstrap-multiselect.js"></script>
	<link rel="stylesheet" href="/css/bootstrap-multiselect.css" type="text/css"/>


	<div class="container">

		<h2>
		    <span class="glyphicon glyphicon-cog"></span>
			Profile ${profileid} - <small> created by ${profile.creator}
			</small>
		</h2>


		<form id="pForm" method="post" action="/updateprofile"
			style="margin-top: 30px;">

			<#if cols??>

			<div class="form-group">
				Name:<input type="text" class="form-control"
					placeholder="Profile Name" name="profile" value="${profile.name}"></input>
			</div>

			<div class="form-group">
				Last Update: ${profile.date?datetime}
			</div>

			<div class="form-group">
				<input type="hidden" name="profileid" value="${profile.id}"></input>
			</div>

			<div class="form-group">
				<input type="hidden" name="batchid"<#if profile.batchid??>
				value="${profile.batchid}" </#if> ></input>
			</div>


			<div class="form-group">
				Comment:<input type="text" class="form-control"
					placeholder="Comment" name="comment"<#if
				profile.comment??>value="${profile.comment}"</#if>></input>
			</div>

			<div class="form-group">
				Match Threshold (0-100 default is 90):<input type="text" class="form-control"
					placeholder="Match Threshold (0-100 default is 90)" name="thres"<#if
				profile.threshold??>value="${profile.threshold}"</#if>></input>
			</div>

			<div class="form-group">
				Fuzzy Threshold (0-1 default is 0.9):<input type="text" class="form-control"
					placeholder="Fuzzy Threshold (0-1 default is 0.9)" name="fuzzy"<#if
				profile.fuzzy??>value="${profile.fuzzy}"</#if>></input>
			</div>

			<#if lists??>

			<div class="form-group">
			Screen List:

			<!-- Build your select: -->
			<select id="example-enableCollapsibleOptGroups"  name="mlist" multiple="multiple">
	                  <#list lists as l>
						<option value="${l.id?c}"
	                      <#if profile.mlist?? && profile.mlist?seq_contains(l.id?c) > selected="${l.name}" </#if> > 
							<b>${l.name}</b>
	                      <#if l.description??>-${l.description}</#if>
						</option> 
	                   </#list> 
        
			</select>
			</div>
			
			<!-- Initialize the plugin: -->
  		    <script type="text/javascript">
			    $(document).ready(function() {
			        $('#example-enableCollapsibleOptGroups').multiselect({
			             enableCollapsibleOptGroups: false
			        });
			    });
			</script>

			</#if>
            <br>

			<table class="table table-striped table-hover">

				<tr>
					<td>Field</td>
					<td>Unique</td>
					<td>Screening</td>
					<td>Category</td>
					<td>Weight</td>
				</tr>

				<#list fld as f>
				<tr>
					<td><b>${f.name}</b></td>

					<td><input type="checkbox" name="${f.name}_unique"<#if
						f.uvalue??> value="${f.uvalue}" checked </#if> ></td>


					<td><input type="checkbox" name="${f.name}_screen"<#if
						f.svalue??> value="${f.svalue}" checked </#if> ></td>

					<td>
					 <select name="${f.name}_category">

						<#list cats as c>	
							<option name="${f.name}_category" value="${c['category']}" 
		                      <#if f.cvalue?? && f.cvalue==c['category'] > selected="selected" </#if> 
		                     >${c['category']}</option>
		                </#list>					 
					 
					 </select>

					</td>

					<td><input type="text" name="${f.name}_weight" size="5"<#if
						f.wvalue??> value="${f.wvalue}" </#if> ></td>


				</tr>
				</#list>
			</table>
			</#if>

			<button type="submit" class="btn btn-primary">Update Profile</button>
			<a href="javascript:history.back()" class="btn btn-default">Back</a>
		</form>



	</div>
	<!--  container -->


	<#include "footer.ftl">
</body>

</html>
