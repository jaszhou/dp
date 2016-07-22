<!DOCTYPE html>

<html>
<head>
<title>Edit Entity</title>
</head>

<body>

	<#include "menu.ftl">


	<div class="container">

		<h2>Search Entity</h2>

		<#macro listProperties node> 
		
		<#list node?keys as key> 
		
		<#assign value =
		node[key]> 
		
		<#if key != "_id"> 
		
		<label>${key}</label> 
		
		
		<#if key != "Answer Unit" && key != "Comments" && key != "Content">
		
		
		
		<input
			type="text" class="form-control" name="${key}"
			value="
            
            
      <#if value?is_date>
        ${value?datetime}
      <#elseif value?is_sequence>
        [
        <#list value as valueValue>
          ${valueValue},
        </#list>
        ]
      <#else>
         <#if value?is_string && value == "NULL">
		<#else> ${value} </#if> 
		</#if> "> 
		
		</input>
		
		<#else> 
		
		    <textarea name="${key}" class="form-control" cols="60" rows="10">
             
             <#lt>${value}
 
            </textarea>
   
   
		</#if> 
		
		</#if> 
		
		
		
		</#list> 
		
		</#macro>



		<form method="post" action="/saveentity" style="margin-top: 30px;">


			<div class="form-group"><@listProperties listdoc /></div>

			<div class="form-group">
				<input type="hidden" class="form-control" name="listname"
					value="${listname}"></input>
			</div>

			<input type="hidden" class="form-control" name="entityid"
				value="${listdoc._id}"></input>

			<button type="submit" class="btn btn-default">Save</button>
			<a href="/list?name=${listname}" class="btn btn-default">Back</a>
			
			  <a href="/editentity?_id=${listdoc._id}&listname=${listname}"
						class="btn btn-default">Edit</a> 
						
	</div>

	<#include "footer.ftl">
</body>

</html>
