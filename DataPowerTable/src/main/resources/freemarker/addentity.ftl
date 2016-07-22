<!DOCTYPE html>

<html>
  <head>
    <title>Add Entity</title>
    </head>

  <body>
  
  <#include "menu.ftl">
  

<div class="container">

<h2>Add Entity</h2>

<#macro listProperties node>
  
    <#list node?keys as key>
    <#assign value = node[key]>

     <#if key != "_id">

       <#if key != "Answer Unit" && key != "Content" >
    
      <label >${key}</label>
                <input type="text"
            class="form-control"
            name="${key}" 
            value=" ">
            
            </input>
     
        <#else>
        
      <label >${key}</label>
   
      <textarea name="${key}" class="form-control" cols="60" rows="10">
      
      
      </textarea>
   
   
      
        </#if>
            
     </#if>
     
    </#list>
</#macro>



<form method="post" action="/addentityaction" style="margin-top:30px;">


        <div class="form-group">

            <@listProperties listdoc />

        </div>
        
        <div class="form-group">
          <input type="hidden"
            class="form-control"
            name="listname" value="${listname}"></input>
        </div>
        
	            <input type="hidden"
            class="form-control"
            name="entityid" value="${listdoc._id}"></input>

    <button type="submit" class="btn btn-default">Save</button>
    <a href="javascript:history.back()" class="btn btn-default">Back</a>

        </div>
 
  <#include "footer.ftl"></body>

</html>
