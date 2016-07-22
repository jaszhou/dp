<!DOCTYPE html>

<html>
<head>
<title>Screening Result List</title>
</head>

<body>

	<#include "menu.ftl"> 
    <#include "micro.ftl"> <#import "pagination.ftl" as pagination />
    <script src="/js/resultlist.js"></script>


	<div class="container">
		<#if recs??>
		<table class="table table-striped table-hover">
			<tr>
				<td>Result ID</td>
				<td>Batch ID</td>
				<td>Profile ID</td>
				<td>Matches</td>
				<td>Rule</td>
				<td>User</td>
				<td>Date</td>
				<td>Delete</td>
			</tr>
			<#list recs as rec>
			<tr>
				<td><a href="/result?resultid=${rec["resultid"]}">${rec["resultid"]}</a></td>
				<td><a href="/batchdetail?batchid=${rec['batchid']}"> ${rec["batchid"]} </a></td>
				
				<td><a href="/profile?profileid=${rec["profileid"]}">${rec["profileid"]}</a></td>
				<td><a href="/result?resultid=${rec["resultid"]}">
                   <#if rec["match"]??>
                     ${rec["match"]?size}
                   <#else>
                     <img src='images/ajax-loader.gif' />
                   </#if>
                </a></td>
				
				<td>
				
				<a href="#" data-href="/searchruleform?resultid=${rec["resultid"]}" class="rule" data-toggle="collapse"
						data-target="#demo"><span class="glyphicon glyphicon-cog"></span></a>
							
                  
			    </td>
			    
			    
				
				<td><#if rec["user"]?? >${rec["user"]} </#if></td>
				<td>${rec["date"]?datetime}</td>
				
				<td><a href="#" data-href="/deleteresult?resultid=${rec["resultid"]}" id="delete" data-toggle="modal"
						data-target="#confirm-delete"><span
							class="glyphicon glyphicon-trash"></span></a></td>

					<div class="modal fade" id="confirm-delete" tabindex="-1"
						role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header"><span class="glyphicon glyphicon-warning-sign"></span> <strong>Delete Confirmation </strong></div>
								<div class="modal-body">Are you sure to delete this item? <br>
								The linked match results
								will also be removed!
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
									$(this).find('.btn-ok').attr('href',
											$(e.relatedTarget).data('href'));
								});
					</script>
					
					 <div id="demo" class="collapse">
					<div id="txtHint">
					 </div>  <!-- txtHint  -->
	       </div>  <!-- demo  -->
					
				
			</tr>

			

			</#list>

		</table>

		<ul class="pagination">
			<li><@pagination.first /></li>
			<li><@pagination.previous /></li>
			<li><@pagination.numbers /></li>
			<li><@pagination.next /></li>
			<li><@pagination.last /></li>
		</ul>
		<@pagination.counter /> </#if>


	</div>

	<#include "footer.ftl">
</body>

</html>
