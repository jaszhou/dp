<#macro listProperties node>

<div class="form-group">

	<#list node?keys as key> <#assign value = node[key]> <#if key
	=="input"> Input Details <@listProperties value /> <#else> <input
		type="text" class="form-control" placeholder="${key}" name="${key}"></input>

	</#if> </#list>
</div>

</#macro>

	
	   <#if listdoc??> <@listProperties listdoc.input /> </#if>



