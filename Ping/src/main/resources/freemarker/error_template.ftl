<!doctype HTML>
<html>
<head>
<title>Internal Error</title>
</head>
<body>
	<#include "menu2.ftl">

    <div class="container">
    
    		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>System Error</strong>
			</div>

			<div class="panel-body">
				<p>
    
		<div class="alert alert-warning alert-dismissible" role="alert">
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<strong>Note!</strong> 
			
             ${error}		
		</div>
		
		   <#if msg??>
             ${msg}
           </#if>
		</p>
  	  </div>
     </div>

	</div>




<#include "footer.ftl">
</body>
</html>

