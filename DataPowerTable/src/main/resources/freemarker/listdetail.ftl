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
            
            <#elseif key == 'dobs'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_dobs value />
                </table>
               </td> 

            <#elseif key == 'addresses'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_addrs value />
                </table>
               </td> 

            <#elseif key == 'titles'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_titles value />
                </table>
               </td> 

            <#elseif key == 'pobs'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_pobs value />
                </table>
               </td> 

            <#elseif key == 'ids'>
               
			   <td>
			    <table class="table table-bordered table-striped">
                <@dump_ids value />
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
			
			<a href="/addentity?listname=${listname}"
				class="btn btn-default">Add Enity</a> 
			<a href="/searchentity?listname=${listname}" class="btn btn-default">Search
				Enity</a>
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
