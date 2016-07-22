<!DOCTYPE html>

<html>
<head>
<title>List Detail</title>
</head>

<body>

<#include "menu.ftl">
<#include "dump.ftl"> 
<#import "pagination.ftl" as pagination />



	<div class="container">


		<h2>${listname}</h2>



		<form method="post" action="/addlistrec">

			<#macro dumpProperties node>
			<table class="table table-striped table-hover">
				<#list node?keys as key> 
				
				<#if key !='_id'>
				
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

           <#elseif key == 'Comments'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_comments value />
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
			
			      </#if>
				</#list>
			</table>
			</#macro> 
			
			<a href="/addentity?listname=${listname}"
				class="btn btn-default">Add Enity</a> 
			<a href="/searchentity?listname=${listname}" class="btn btn-default">Search
				Enity</a>
			<a  id="export" href="/inboxexportcsv?name=${listname}" class="btn btn-default pull-right"><span class="glyphicon glyphicon-cloud-download"></span> Export to .CSV file</a>
				
				
			</td> 

           <#if recs??>



			<table class="table table-striped table-hover">

				<#list recs as rec>
				<tr>
					<td>
                       <#if rec?is_hash>
                         <@dumpProperties rec /> 
                       </#if>

                       <a
						href="/editentity?_id=${rec._id}&listname=${listname}"
						class="btn btn-default">Edit</a> <a
						href="/deleteentity?_id=${rec._id}&listname=${listname}"
						class="btn btn-default">Delete</a>

					</td>

				</tr>
				</#list>
			</table>

			<ul class="pagination">
				<li><@pagination.first /></li>
				<li><@pagination.previous /></li>
				<li><@pagination.numbers /></li>
				<li><@pagination.next /></li>
				<li><@pagination.last /></li>
			</ul>

			</#if>




		</form>

	</div>

	<#include "footer.ftl">
</body>

</html>
