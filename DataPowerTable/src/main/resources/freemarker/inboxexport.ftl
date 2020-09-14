    <#macro dumpName node> 
        <#list node?keys as key>
         <#assign value = node[key]> 
        <#if key?lower_case?contains("name") >
          <#t>${key?trim}<#t>:<#t>${value?trim}|<#t>
        </#if> 
      </#list> 
    </#macro> 
    <#if matches??>
       <#t><@compress single_line=true>
		<#list matches[0]?keys as key> 
			<#t>${key}<#t>,
		</#list>			
   	</@compress>		
     <#list matches as match>
	   <#t><@compress single_line=true>
	   	<#list match?keys as key> 
				<#if match[key]??>
                  <#assign value = match[key]>
                <#else>
                  <#assign value = "">
				</#if>
				<#t>"${value}"<#t>,
		</#list>	
		</@compress>		
      
     </#list>
    </#if>

