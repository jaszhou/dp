<!DOCTYPE html>

<html>
<head>
<title>Search Match Result</title>
</head>

<body>

	<#include "menu.ftl">
    <script src="/js/searchmatch.js"></script>
	<br>
	<div class="container">

		<div class="panel panel-default">
			<div class="panel-heading">
				<strong>Search Match</strong>
			</div>
			
			<div class="panel-body">


				<#macro listProperties node> 
                  <#list node?keys as key> 
                    <#assign value= node[key]> 
                    <#if key =="input"> 
                     Input Details 
                     <@listProperties value /> 
                   <#else>

						<div class="form-group">
							<input type="text" class="form-control" placeholder="${key}"
								name="${key}"></input>
						</div>
				  </#if> 
                 </#list> 
              </#macro>



					<#if recs??>

                <form method="post" action="/searchmatchaction" style="margin-top: 30px;">
					<div class="form-group">
						<label for="batchid">Batch ID*</label> 
						<select id="selectbatch" name="batchid" >
							
							<option value="0" >Choose a batch</option> 
							
                            <#list recs as rec>
							 <option value="${rec["batchid"]?c}"
                                 <#if batchid??>
                                   <#if batchid == rec["batchid"]?c >
                                      selected
                                   </#if>
                                 </#if>
							 >
                             ${rec["batchid"]?c}-${rec["filename"]}
                             </option>
							</#list>

						</select>
					</div>
					

					</#if>

                  <div class="form-group">
                   <div class="btn btn-default" data-toggle="collapse" data-target="#demo"><span class="glyphicon glyphicon-zoom-in"></span> Show Profile- search by specific fields</div>
                  </div>
                  
	               <div id="demo" class="collapse">

					<div id="txtHint">

	                   <#if listdoc??> <@listProperties listdoc.input /> </#if>

					</div>  <!-- txtHint  -->
					
			    </div>  <!-- demo  -->
			    
			      		  <div class="form-group">


								<label for="status">Status:</label> <select
									name="status">
									<option value="open">Open</option>
									<option value="False Positive">False Positive</option>
									<option value="Further Investigation">Further
										Investigation</option>
									<option value="True Match">True Match</option>
								</select>

							</div>


			    
			    	<button type="submit" class="btn btn-primary">
		                  <span class="glyphicon glyphicon-search"></span> Search
	                </button>
              </form>
			    
					
			  </div>  <!-- panel body  -->
			
		  </div>   <!-- panel  -->
		
	</div> <!-- container  -->
	
	<#include "footer.ftl">
</body>

</html>
