<#macro dumpProperties node>

<table>
	<#list node?keys as key> <#assign value = node[key]>
	<tr>
		<td>${key}</td>
		<td><#if value?is_date> ${value?datetime} <#elseif
			value?is_sequence> [ <#list value as valueValue> ${valueValue},
			</#list> ] <#else> <#if value?is_string && value == "NULL"> <#else>
			${value} </#if> </#if></td>
	</tr>
	</#list>
</table>
</#macro> 

<#macro dumpPropertiesRow node> <#list node?keys as key>
<#assign value = node[key]>
<td><#if value?is_date> ${value?datetime} <#elseif
	value?is_sequence> [ <#list value as valueValue> ${valueValue},
	</#list> ] <#else> <#if value?is_string && value == "NULL"> <#else>
	${value} </#if> </#if></td>
</#list> 
</#macro> 

<#macro dumpName node> 
  <#list node?keys as key>
   <#assign value = node[key]> 
   <#if key?lower_case?contains("name") >

		<i>${key}</i>
		:
		<b>${value}</b>  
   </#if> 
  </#list> 
</#macro> 

<#macro dumpJtName node> <#list node?keys as
key> <#assign value = node[key]> ${value}: {key:false, list:false},

</#list> </#macro>
