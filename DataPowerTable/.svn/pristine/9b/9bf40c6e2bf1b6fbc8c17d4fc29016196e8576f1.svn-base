<!DOCTYPE html>

<html>
<head>
<title>Search List</title>
</head>

<body>

	<#include "menu.ftl"> 
    <#include "micro.ftl"> 
    <#import "pagination.ftl" as pagination /> 
    <script src="/js/searchmatchlist.js"></script>
    

	<div class="container">
	<#if matches??>
	 <div class="panel panel-default">
	  <div class="panel-heading"><strong>Search Result</strong> - found number of matches: ${matches?size} </div>
	 	<div class="panel-body">
	

  	 <form method="post" action="/deletematch">

		<table class="table table-striped table-hover">
			<tr>
				<td>Select</td>
				<td>Match ID</td>
				<td>Batch ID</td>
				<td>Record ID</td>
				<td>Input Name</td>
				<td>Status</td>
				<td>Last Update Time</td>
			</tr>

			<#list matches as match>
			<tr>
				<td><input class="check" type="checkbox" name="sel" value="${match['id']?c}" /></td>
			
				<td><a href="/matchview?matchid=${match["id"]?c}">${match["id"]?c}</a></td>
				<td>${match["batchid"]}</td>
				<td>${match["recid"]}</td>

				<td><a href="/matchview?matchid=${match["id"]?c}"> <@dumpName
						match["input"] /> </a></td>

				<td > <span class="matchstatus">${match["status"]}</span></td>
				<td>${match["lastUpdate"]?datetime}</td>
			</tr>

			</#list>

		</table>

  		   <div class="form-group">
				<label for="status">Change Status to:</label> 
				<select id="newstatus" name="status">
					<option value="open">Open</option>
					<option value="False Positive">False Positive</option>
					<option value="Further Investigation">Further Investigation</option>
					<option value="True Match">True Match</option>
				</select>

			</div>
		   
		   				<div class="modal fade" id="confirm-delete" tabindex="-1"
							role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content">
									<div class="modal-header">Delete Confirmation</div>
									<div class="modal-body">Are you sure to delete this item?
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">Cancel</button>
										<a class="btn btn-danger btn-ok">Delete</a>
									</div>
								</div>
							</div>
						</div>

						<script>
							$('#confirm-delete').on(
									'show.bs.modal',
									function(e) {
										$(this).find('.btn-ok')
												.attr(
														'href',
														$(e.relatedTarget)
																.data('href'));
									});
						</script>
						
		
		   
		   <a id="selectall" href="" class="btn btn-default">Select All</a>
		   <a id="bulkupdate" href="/bulkupdate" class="btn btn-default">Bulk Update</a>
		   

		   <a id="delete" href="#" data-href="/deletematch" class="btn btn-default" data-toggle="modal" data-target="#confirm-delete"><span class="glyphicon glyphicon-trash"></span> Delete Selected</a>
                  
            </button>
		
          </form>
          
          
 
   	        </div> <!-- panel body -->

    	</div> <!-- panel -->

          <div id="status"></div>

			<ul class="pagination">
				<li><@pagination.first /></li>
				<li><@pagination.previous /></li>
				<li><@pagination.numbers /></li>
				<li><@pagination.next /></li>
				<li><@pagination.last /></li>
			</ul>
			<@pagination.counter />

       </#if> 
   
  	    </div>  <!--- container -->

  <#include "footer.ftl">

 </body>
</html>
