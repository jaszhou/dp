<!DOCTYPE html>

<html>
<head>
<title>User Action History</title>
</head>

<body>

	<#include "menu.ftl">
    <#include "dump.ftl"> 
  
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  
  
<#macro dumpProperties node>
			<table class="table table-striped table-hover">
				<#list node?keys as key> 
				
				<#if node[key]??>
                  <#assign value = node[key]>
                <#else>
                  <#assign value = "">
				</#if>
				<tr>
					<td>${key}</td>
				
           <#if key == 'sdfs'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_sdsf value />
                </table>
               </td> 
            
        

            <#elseif key == 'Answer Unit'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_answer value />
                </table>
               </td> 


             <#else>
					
					
					<td>
                    <#if value?is_date> ${value?datetime}
					<#elseif value?is_string> ${value}
                    <#elseif value?is_number> ${value}
                    <#else>
                            <pre>
							  <@dump value />
                            </pre> 
                      
                    </#if>
                    </td>
			
             </#if>
			
				</tr>
			
				</#list>
			</table>
			</#macro> 
			
	<div class="container">

		<#if actions??>


		<table class="table table-striped table-hover">
			<tr>
	
				<td>Date</td>
				<td>User</td>
				<td>List Name</td>
				<td>Previous Entity ID</td>
			</tr>

         
		       
			<#list actions as match>
			<tr>
				<td>${match["timestamp"]?datetime}</td>
				<td><#if match["username"]??> ${match["username"]} </#if></td>
				<td><#if match["listname"]??> ${match["listname"]} </#if></td>
				<td><#if match["before"]??> 
				
				<a href="#demo${match['before']['_id']}" data-toggle="collapse" >${match["before"]["_id"]}</a>
				
				
				</#if>
				</td>
			</tr>
  
           <tr><td colspan='4'>
             <div id="demo${match['before']['_id']}" class="collapse" >
				      <#if match["before"]?is_hash>
                         <@dumpProperties match["before"]  /> 
                       </#if>
		       </div>
			</td>
           </tr>
			</#list>

     	       
             	  
		</table>
		</#if>

	</div>
	<#include "footer.ftl">
</body>

</html>
