   <br>
   <div class="panel panel-default">
     <div class="panel-heading">Case Attachments</div>
     <div class="panel-body">
   
   <#if match["Attachment"]??>

   <table class="table table-striped table-hover">
    <tr>
     <td>File</td>
     <td>Description</td>
     <td>Creator</td>
     <td>Date</td>
    </tr> 
    <#list match["Attachment"] as rec>
    <tr>

		<#assign mylist=rec["file"]?split("/")>
		
		
     
     <td><a href="/download?file=${rec['file']}&filename=${mylist[3]}">${mylist[3]}</a></td>
     <td><#if rec["desc"]?? >${rec["desc"]}</#if></td>
     <td><#if rec["creator"]?? >${rec["creator"]}</#if></td>
     <td><#if rec["lastUpdate"]?? >${rec["lastUpdate"]?datetime}</#if></td>
    </tr> 
    </#list>
  </table>
 </#if>
 </div>
</div>
<p>

