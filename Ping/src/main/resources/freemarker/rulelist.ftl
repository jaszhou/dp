<!DOCTYPE html>

<html>
<head>
<title>Rule List</title>
<!--
      <script src="/js/rulelist.js"></script> 
    -->


</head>

<body>

	<#include "menu.ftl">

	<div class="container">

		<br>
		<div id="status"></div>

		<div class="panel panel-default">
			<div class="panel-heading">
				<strong>Rule List</strong>
			</div>

			<div class="panel-body">
				<p>Use can create rules to further reduce the false positive matches. You don't 
				need to re-run the batch, the rules can be applied to existing results.</p>
			
				<a href="/addrule" class="btn btn-primary">Add Rule</a> 
			
			</div>
			
			

			<div id="rulelist">

				<#if recs??>

				<table class="table table-striped table-hover">
					<tr>
						<td>Rule ID</td>
						<td>Rule Name</td>
						<td>Creator</td>
						<td>Creation Date</td>
						<td>Description</td>
						<td>Edit</td>
						<td>Delete</td>
					</tr>
					<#list recs as rec>
					<tr>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec["id"]}</a></td>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec["name"]}</a></td>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec.creator}</a></td>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec["date"]?datetime}</a></td>
						<td><#if rec["comment"]??>${rec["comment"]}</#if></td>
						<td><a href="/rule?ruleid=${rec["id"]}"><span
								class="glyphicon glyphicon-pencil"></span></a></td>



						<td><a href="#" data-href="/deleterule?ruleid=${rec["id"]}" id="delete" data-toggle="modal"
							data-target="#confirm-delete"><span
								class="glyphicon glyphicon-trash"></span></a></td>

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


					</tr>
					</#list>
				</table>

				</#if>




			</div>

		</div>

		<div class="alert alert-warning alert-dismissible" role="alert">
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<strong>Note:</strong> You may apply the rules to batch on the fly!
		</div>


	</div>
	<!--  container -->

	<#include "footer.ftl">
</body>

</html>
