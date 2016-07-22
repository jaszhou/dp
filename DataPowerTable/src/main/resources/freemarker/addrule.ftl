<!DOCTYPE html>

<html>
  <head>
    <title>Create New Rule</title>

    </head>

  <body>
  
  <#include "menu.ftl">

    <script src="/js/jquery.form.js"></script>
    <script src="/js/addrule.js"></script>
     
	<!-- Include the plugin's CSS and JS: -->
	<script type="text/javascript" src="/js/bootstrap-multiselect.js"></script>
	<link rel="stylesheet" href="/css/bootstrap-multiselect.css" type="text/css"/>
    
	<div class="container">
	      
		<div class="panel panel-default">
			<div class="panel-heading"><strong>Create a rule</strong></div>
			</div>
	
	<form id="myForm" method="post" action="/saverule">

  <div class="panel panel-default">
     <div class="panel-heading">Rule details</div>
      <div class="panel-body">

       <div class="form-group">
          Rule Name: 
          <input type="text"
            class="form-control"
            placeholder="Rule Name"
            name="rule" required></input>
       </div>
          
       <div class="form-group">
          Description:<input type="text"
            class="form-control"
            placeholder="Comment"
            name="comment" ></input>
       </div>
         
       <div class="form-group">
          Condition:<input type="text"
            class="form-control"
            placeholder="Filter Condition"
            name="condition" ></input>
       </div>

      <button type="submit" class="btn btn-primary">Save Rule</button>
    </div> <!-- panel body -->

  </div> <!-- panel -->
   
   <div id="selectmsg"></div>
   
    </form>
 
    </div>  <!-- container -->

  <#include "footer.ftl"></body>

</html>
