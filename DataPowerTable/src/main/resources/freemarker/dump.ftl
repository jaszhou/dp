<#-- dump.ftl
  --
  -- Generates tree representations of data model items.
  --
  -- Usage:
  -- <#import "dump.ftl" as dumper>
  --
  -- <#assign foo = something.in["your"].data[0].model />
  --
  -- <@dumper.dump foo />
  --
  -- When used within html pages you've to use <pre>-tags to get the wanted
  -- result:
  -- <pre>
  -- <@dumper.dump foo />
  -- <pre>
  -->

<#-- The black_list contains bad hash keys. Any hash key which matches a 
  -- black_list entry is prevented from being displayed.
  -->
<#assign black_list = ["class"] />


<#-- 
  -- The main macro.
  -->
  
<#macro dump data>
(root)
<#if data?is_enumerable>
<@printList data,[] />
<#elseif data?is_hash_ex>
<@printHashEx data,[] />
</#if>
</#macro>

<#macro dump_sdsf data>

 <#if data["@name"]??>
      <tr>
      <td>${data["@name"]}</td>
      <td>${data["#text"]}</td>
      </tr>
 </#if>

 <#if data["sdf"]??>
 
   <#if data["sdf"]?is_sequence>
       <#list data["sdf"] as subkey>
         <@dump_sdsf subkey />          
       </#list>
    
    </#if>
 </#if>

</#macro>



<#macro dump_dobs data>

<#if data?is_hash>

 <#if data["@Y"]??>
      <tr>
      <td>Year</td><td>${data["@Y"]}</td>
      </tr>
      <tr>
      <td>DOB</td><td>${data["#text"]}</td>
      </tr>
  </#if>

 <#if data["dob"]??>
 
    <#if data["dob"]?is_sequence>
      <#list data["dob"] as dob>

        <@dump_dobs dob />

      </#list>
    <#else>          
        <@dump_dobs data["dob"] />
 
    </#if>
 
  </#if>

<#else>

  <pre><@dump data /></pre>
  
 </#if>

</#macro>

<#macro dump_addrs data>

 <#if data["country"]??>
      <tr>
      <td>Country</td><td>${data["country"]}</td>
      </tr>
      <tr>
      <td>Country Name</td><td>${data["countryName"]}</td>
      </tr>
 </#if>

 <#if data["address"]??>
    <#if data["address"]?is_sequence>
      <#list data["address"] as address>

        <@dump_addrs address />

      </#list>
    <#else>          
        <@dump_addrs data["address"] />  
 
    </#if>
 </#if>

 </#macro>

<#macro dump_titles data>

<#if data?is_hash>
 <#if data["title"]??>
   <#if data["title"]?is_string>
      <tr>
      <td>Title</td><td>${data["title"]}</td>
      </tr>
   </#if>
<#else>
    <#if data["title"]?is_sequence>
       <#list data["title"] as subkey>
        <#if subkey?is_string>
      <tr>
      <td>Title</td><td>${subkey}</td>
      </tr>
       </#if>
       </#list>
    
    </#if>
  
 </#if>
 
 <#else>
    <#if data?is_sequence>
       <#list data as subkey>
        <#if subkey?is_string>
      <tr>
      <td>Title</td><td>${subkey}</td>
      </tr>
       </#if>
       </#list>
    
    </#if>
 
</#if>
 
</#macro>

<#macro dump_pobs data>

 <#if data["pob"]?? && data["pob"]?is_string>
      <tr>
      <td>POB</td><td>${data["pob"]}</td>
      </tr>
      
 <#else>
    <#if data["pob"]?is_sequence>
       <#list data["pob"] as subkey>
        <#if subkey?is_string>
      <tr>
      <td>POB</td><td>${subkey}</td>
      </tr>
       </#if>
       </#list>
    
    </#if>
 </#if>

 
</#macro>

<#macro dump_ids data>

<#if data?is_sequence>
     <#list data as d>

	 <#if d["@type"]??>
	      <tr>
	      <td>Type</td><td>${data["@type"]}</td>
	      </tr>
	      <tr>
	      <td>Value</td><td>${data["#text"]}</td>
	      </tr>
	 </#if>
      </#list>
 
</#if>

</#macro>

<#macro dump_answer data>

	      <tr>
	        <td>
	         <pre>${data}</pre>
 
	        </td>
	      </tr>
</#macro>

<#macro dump_comments data>

	      <tr>
	        <td>
	         ${data}
 
	        </td>
	      </tr>
</#macro>

<#-- private helper macros. it's not recommended to use these macros from 
  -- outside the macro library.
  -->

<#macro printList list has_next_array>
<#local counter=0 />
<#list list as item>
<#list has_next_array+[true] as has_next><#if !has_next>    <#else>  | </#if></#list>
<#list has_next_array as has_next><#if !has_next>    <#else>  | </#if></#list><#t>
<#t><@printItem item?if_exists,has_next_array+[item_has_next], counter />
<#local counter = counter + 1/>
</#list>
</#macro>

<#macro printHashEx hash has_next_array>
<#list hash?keys as key>
<#list has_next_array+[true] as has_next><#if !has_next>    <#else>  | </#if></#list>
<#list has_next_array as has_next><#if !has_next>    <#else>  | </#if></#list><#t>
<#t><@printItem hash[key]?if_exists,has_next_array+[key_has_next], key />
</#list>
</#macro>

<#macro printItem item has_next_array key>
<#if item?is_method>
  +- ${key} = ?? (method)
<#elseif item?is_enumerable>
  +- ${key}
  <@printList item, has_next_array /><#t>
<#elseif item?is_hash_ex && omit(key?string)><#-- omit bean-wrapped java.lang.Class objects -->
  +- ${key} (omitted)
<#elseif item?is_hash_ex>
  +- ${key}
  <@printHashEx item, has_next_array /><#t>
<#elseif item?is_number>
  +- ${key} = ${item}
<#elseif item?is_string>
  +- ${key} = "${item}"
<#elseif item?is_boolean>
  +- ${key} = ${item?string}
<#elseif item?is_date>
  +- ${key} = ${item?string("yyyy-MM-dd HH:mm:ss zzzz")}
<#elseif item?is_transform>
  +- ${key} = ?? (transform)
<#elseif item?is_macro>
  +- ${key} = ?? (macro)
<#elseif item?is_hash>
  +- ${key} = ?? (hash)
<#elseif item?is_node>
  +- ${key} = ?? (node)
</#if>
</#macro>

<#function omit key>
    <#local what = key?lower_case>
    <#list black_list as item>
        <#if what?index_of(item) gte 0>
            <#return true>
        </#if>
    </#list>
    <#return false>
</#function>

<#macro dumpEntity node matchfields>

<table class="table table-bordered table-striped">
		<#list node?keys as key> 
		 <#if node[key]??>
          <#assign value = node[key]>
         <#else>
          <#assign value = "">
         </#if>
		  <tr>

			<#if matchfields?contains(key) >

			   <td><b>${key}</b></td> 
            <#else>

			   <td>${key}</td> 
            </#if>

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

            <#else>  



			<td><#if matchfields?contains(key) > <b><mark> </#if>


						<#if value?is_date> 
                           ${value?datetime} 
                        <#elseif value?is_sequence>
						   [ <#list value as valueValue> 
                              <#if valueValue?is_hash>
	                            <pre> 
								  <@dump valueValue />
	                            </pre> 
                                 
                              <#else>
                                 ${valueValue}, 
                              </#if> 
                             </#list> ] 
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

          </#if>

		</tr>
		</#list>
	</table>
</#macro>