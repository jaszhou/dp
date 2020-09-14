<!DOCTYPE html>

<html>
<head>
<title>Edit Entity</title>
</head>

<body>

	<#include "menu.ftl">
    <script src="/js/matchview.js"></script>
	<script src="/js/jquery.form.js"></script>

	<div class="container">

		<h2>Search Entity</h2>

		<#macro listProperties node> 
		
		<#list node?keys as key> 
		
		<#assign value =
		node[key]> 
		
		<#if key != "_id" && key != "Attachment" > 
		
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
			
			<a id="showattachment" href="/attachment?entityid=${listdoc._id}&listname=${listname}" class="btn btn-default">Show Attachments</a> <a
								href="javascript:history.back()" class="btn btn-default">Back</a>
			
						
			</form>
			
	    <div id="attachment"></div>

		<form method="POST" id="myForm" action="/attachfile"
			style="margin-top: 30px;" enctype="multipart/form-data">

			<div class="panel panel-default">

				<div class="panel-heading">Upload attachment</div>
				<div class="panel-body">
			
					<div class="form-group">
						<input type="hidden" class="form-control" name="listname"
							value="${listname}"></input>
				
				    	<input type="hidden" class="form-control" name="entityid"
					    	value="${listdoc._id}"></input>
					</div>


					<div class="form-group">

						<input type="file" class="form-control"
							placeholder="Upload a file" name="upload" size="20" required>
						</input>

					</div>

					<div class="form-group">
						<label for="upload">Description:</label> <input type="text"
							class="form-control" placeholder="Description" name="description"
							size="20"> </input>
					</div>


					<button type="submit" class="btn btn-primary">Attach</button>

					<a id="selectp" href="" class="btn btn-default">Reset</a>

					<p>
					<div class="progress">
						<div class="bar"></div>
						<div class="percent">0%</div>
					</div>

					<div id="status"></div>

				</div>
		</form>			
	</div>

	<#include "footer.ftl">
</body>

</html>
