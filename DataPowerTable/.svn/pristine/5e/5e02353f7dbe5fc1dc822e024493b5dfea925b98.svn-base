
<#include "dump.ftl"> 

<#macro dumpProperties node matchfields>

<div class="content">

	<div align="center">
		<a class="showmodal" href="/myModal?matchid=${match["id"]?c}&entityid=${node[ "_id"]}" data-toggle="modal"
			data-target="#myModal"><mark>click to view side by side</mark></a>
	</div>



	<@dumpEntity node matchfields/>

</div>

</#macro> 

<#macro dumpInput node matchfields>

<table class="table table-bordered table-striped">
	<#list node?keys as key> 
        <#assign value = node[key]>

	<tr>

		<#if matchfields?contains(key) >

		<td><b>${key}</b></td> 
        
        <#else>

		<td>${key}</td> 
        
        </#if>

		<td><#if matchfields?contains(key) > <b><mark> </#if>


					<#if value?is_date> ${value?datetime} <#elseif value?is_sequence> [
					<#list value as valueValue> ${valueValue}, </#list> ] 
                   <#else> 

						<#if value?is_string && value == "NULL"> 
						
                        <#elseif value?is_string> ${value}
                        
						<#elseif value?is_number> ${value}
                        
						<#else> 
                            <pre>
							<@dump value />
                            </pre> 
                        </#if>


                    </#if>


					<#if matchfields?contains(key) > </mark></b> </#if>

		</td>
	</tr>
	</#list>
</table>
</#macro> 

<#macro sidebyside input entity matchfields>

<div class="panel panel-default" id="panelGuest">
	<div class="panel-heading"><strong>Compare the input and entity</strong></div>

	<table class="table table-bordered table-striped">
		<tr>
			<th>Field Name</th>
			<th>Input Value</th>
			<th>Entity Value</th>
		</tr>
		<#list input?keys as key>
		  <#assign evalue = ""> 
          <#assign value = input[key]> 
          <#if entity[key]??> 
            <#assign evalue = entity[key]>
          </#if>
		<tr>

			<#if matchfields?contains(key) >

			<td><b>${key}</b></td> 
            <#else>

			<td>${key}</td> 
            </#if>

			<td><#if matchfields?contains(key) > <b><mark> </#if>


						<#if value?is_date> 
                           ${value?datetime} 
                        <#elseif value?is_sequence>
						   [ <#list value as valueValue> ${valueValue}, </#list> ] 
                        <#else>

						<#if value?is_string && value == "NULL"> 
                        <#elseif value?is_string> ${value}
                        <#elseif value?is_number> ${value}
                        <#else> 
                            <pre>
							<@dump value />
                            </pre> 
                        </#if>

						</#if> 
						
                 <#if matchfields?contains(key) > </mark></b> </#if>

			</td>

			<!--  output the entity values           -->

			<#if evalue??>

			  <td>
              <#if matchfields?contains(key) > <b><mark> </#if>

						<#if evalue?is_date> 
                          ${evalue?datetime} 
                        <#elseif evalue?is_sequence> 
                          [ <#list evalue as valueValue> ${valueValue},</#list> ] 
                        <#else> 
                        

						<#if evalue?is_string && value == "NULL"> 
                        <#elseif evalue?is_string> ${evalue}
                        <#elseif value?is_number> ${evalue}

                        <#else> 
                            <pre>
							<@dump evalue />
                            </pre> 
                        </#if>
                        
                        </#if> 

                  <#if matchfields?contains(key) > </mark></b> </#if>
              <#else>
                 <#assign evalue = " ">
                             ${evalue} 
              </#if>
			  </td> 

		</tr>
		</#list>
	</table>

</div>

</#macro>


