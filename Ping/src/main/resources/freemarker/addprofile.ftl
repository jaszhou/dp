<!DOCTYPE html>

<html>
  <head>
    <title>Create New Profile</title>

    <style>
        .tr {   line-height: 10px;   min-height: 10px;   height: 10px;}
    </style>
 
    </head>

  <body>
  
  <#include "menu.ftl">

    <script src="/js/jquery.form.js"></script>
    <script src="/js/addprofile.js"></script>
     
	<!-- Include the plugin's CSS and JS: -->
	<script type="text/javascript" src="/js/bootstrap-multiselect.js"></script>
	<link rel="stylesheet" href="/css/bootstrap-multiselect.css" type="text/css"/>
    
<div class="container">
      
<div class="panel panel-default">
<div class="panel-heading"><strong>Create a profile</strong> - batch screening step 3 </div>
</div>

<form id="myForm" method="post" action="/saveprofile">

  <div class="panel panel-default">
     <div class="panel-heading">Profile details</div>
         <div class="panel-body">

  <#if recs??>

     
       <div class="form-group">
          Profile Name: 
          <input type="text"
            class="form-control"
            placeholder="${filename} Profile"
            name="profile" value="${filename} Profile" required></input>
       </div>
          
       <div class="form-group">
          Description:<input type="text"
            class="form-control"
            placeholder="Comment"
            name="comment" ></input>
       </div>
       
       
         
       <div class="form-group form-group-sm">
          <div class="col-xs-2">  
          Match Threshold (0-100 default is 90):<input type="text"
            class="form-control input-sm"
            placeholder="Match Threshold (0-100 default is 90)"
            name="thres" value="90"></input>
          </div>
         
         <div class="col-xs-2">
          Fuzzy Threshold (0-1 default is 0.9):<input type="text"
            class="form-control input-sm"
            placeholder="Fuzzy Threshold (0-1 default is 0.9)"
            name="fuzzy" value="0.9"></input>
         </div>
        
         <div class="col-xs-2">
        
			<#if lists??>

			<div class="form-group">
			Screen List:

			<!-- Build your select: -->
			<select id="example-enableCollapsibleOptGroups"  name="mlist" multiple="multiple">
	                  <#list lists as l>
						<option value="${l.id?c}" >
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

        </div>
   </div>
 </div>

 <br>      
 <div class="panel panel-default">
     <div class="panel-heading">Search details</div>


			<table class="table table-striped table-hover">

				<tr>
					<td>Field</td>
					<td>Unique</td>
					<td>Screening</td>
					<td>Category</td>
					<td>Weight</td>
				</tr>

				<#list cols as col>
				<tr>
					<td><b>${col}</b></td>

					<td><input type="checkbox" name="${col}_unique"></td>


					<td><input type="checkbox" name="${col}_screen"></td>

					<td>
					 <select name="${col}_category">

						<#list cats as c>	
							<option name="${col}_category" value="${c['category']}" <#if "Default"==c['category'] > selected="selected" </#if> >${c['category']}</option>
		                </#list>					 
					 
					 </select>

					</td>

					<td><input type="text" name="${col}_weight" size="5"></td>


				</tr>
				</#list>
			</table>
      
       <div class="form-group">
          <input type="hidden" name="batchid" value="${batchid}"></input>
        </div>


   <button type="submit" class="btn btn-primary">Save Profile</button>

  </div>
   
   <div id="selectmsg"></div>
   
    </form>
 
 </#if>
    
    </div>

  <#include "footer.ftl"></body>

</html>
