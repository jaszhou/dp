<!DOCTYPE html>

<html>
<head>
<title>Interactive Look Up</title>
</head>

<body>

	<#include "menu.ftl">
       	<script src="/js/jquery.form.js"></script>
        <script src="/js/interactive.js"></script>



	<div class="container">

		<h2>
			Profile ${profile.name} - <small> created by
				${profile.creator} </small>
		</h2>

		<form id="pform" method="post" action="/interactiveact"">

			<#if cols??>

			<button class="btn" data-toggle="collapse" data-target="#demo">Show
				Detail</button>


			<div id="demo" class="collapse">
				<div class="form-group">
					Name: <input type="text" class="form-control"
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
					<input type="hidden" name="batchid"<#if profile.batchid??>
					value="${profile.batchid}" </#if> ></input>
				</div>


				<div class="form-group">
					Comment:<input type="text" class="form-control"
						placeholder="Comment" name="comment"<#if
					profile.comment??>value="${profile.comment}"</#if>></input>
				</div>

				<div class="form-group">
					Match Threshold:<input type="text" class="form-control"
						placeholder="Match Threshold (0-100 default is 50)" name="thres"<#if
					profile.threshold??>value="${profile.threshold}"</#if>></input>
				</div>

				<div class="form-group">
					Fuzzy Threshold:<input type="text" class="form-control"
						placeholder="Fuzzy Threshold (0-1 default is 0.8)" name="fuzzy"<#if
					profile.fuzzy??>value="${profile.fuzzy}"</#if>></input>
				</div>

				<#if lists??>

				<div class="form-group">
					Screen List:<select name="list"> <#list lists as l>
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
			</div>

			</#if>


			<table class="table table-striped table-hover">

				<tr>
					<td>Field</td>
					<td>Unique</td>
					<td>Screening</td>
					<td>Weight</td>
					<td>Search Value</td>
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

					<td><input type="text" name="${f.name}_value" size="30"<#if
						f.svalue??> required </#if> ></td>

				</tr>
				</#list>
			</table>
			</#if>

			<button type="submit" class="btn btn-primary">
				<span class="glyphicon glyphicon-search"></span> Search
			</button>
			<a href="javascript:history.back()" class="btn btn-default">Back</a>
		</form>

		    <div id='loadingmessage' style='display: none'>
				<br>
				<div class="alert alert-success">
					<img src='images/ajax-loader.gif' /> Screening in process, please
					wait ...
				</div>
			</div>

			<br><br>
			
			 <div id="t" class="text-center" style="font-size: 36px; display: none"><!-- pretend an enclosing class has big font size -->
               00:00
             </div>
			

			<br><br>

			 <div id='goto' style='display: none'>
			    <br>
 	  	        <div class="form-group">
	             <a href="/resultlist" class="btn btn-primary">Check Result</a> 
               </div>
	         </div>
			

        <script type='text/javascript' src="//fuelcdn.com/fuelux/2.3.1/loader.min.js"></script>
        <script src="/js/timer.jquery.min.js"></script>		

	</div>


	<#include "footer.ftl">
</body>

</html>
