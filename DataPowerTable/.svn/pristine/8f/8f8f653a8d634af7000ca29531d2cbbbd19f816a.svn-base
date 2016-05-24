    <#macro dumpName node> 
        <#list node?keys as key>
         <#assign value = node[key]> 
        <#if key?lower_case?contains("name") >
          <#t>${key?trim}<#t>:<#t>${value?trim}|<#t>
        </#if> 
      </#list> 
    </#macro> 

    <#if matches??>
          <#t>ID,BatchID,RecID,Names Match,Status,LastUpdate
      <#list matches as match>
			${match["id"]?c}<#t>,${match["batchid"]}<#t>, <#t>${match["recid"]}<#t>,<#t>"<@dumpName match["input"]/>"<#t>,${match["status"]}<#t>,${match["lastUpdate"]?datetime}
	  </#list>
    </#if>

