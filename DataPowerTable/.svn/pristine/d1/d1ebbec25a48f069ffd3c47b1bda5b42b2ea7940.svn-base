<!DOCTYPE html>

<html>
<head>
<title>Batch List</title>
</head>

<body>

	<#include "menu.ftl">

	<div class="container">
		<#if recs??> <br>
		<div id="message"></div>

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Batch List</strong> - batch screening step 2 
			</div>

			<div class="panel-body">
				<p>
					<span class="glyphicon glyphicon-bell"></span> You may <a href="/batch">upload</a> a .csv file to create a new
					batch for screening. You can also use a batch file to generate a
					screening profile.
				</p>
			</div>

			<table class="table table-striped table-hover">
				<tr>
					<td>Batch ID</td>
					<td>File Name</td>
					<td><a href="#" data-toggle="popover" 
						trigger="hover" title="Tips: a profile is required to start a screening process. "
						data-content="A profile is required to start a screening process.">Generate
							Profile <span class="glyphicon glyphicon-info-sign"></span>
					</a></td>
					
					<td>Number of Records</td>
					<td>Upload Date</td>
					<td>creator</td>
					<td>Delete</td>
				</tr>
				<#list recs as rec>
				<tr>
					<td>${rec["batchid"]?c}</td>
					<td><#if rec["filename"] == "Interactive" > ${rec["filename"]}
						<#else> <a href="/batchdetail?batchid=${rec["batchid"]?c}&filename=${rec["filename"]}"> ${rec["filename"]} </a>
						</#if>
					</td>

					<td><#if rec["filename"] != "Interactive" > <a
						href="/addprofile?batchid=${rec["batchid"]?c}"> <span
							class="glyphicon glyphicon-pencil"></span>

					</a> </#if>
					</td>

							
					<td><#if rec["size"]??>${rec["size"]}</#if></td>
					<td>${rec["date"]?datetime}</td>
					<td><#if rec["creator"]?? >${rec["creator"]}</#if></td>

					<td><a href="#" data-href="/deletebatch?batchid=${rec["batchid"]?c}" id="delete" data-toggle="modal"
						data-target="#confirm-delete"><span
							class="glyphicon glyphicon-trash"></span></a></td>

					<div class="modal fade" id="confirm-delete" tabindex="-1"
						role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header"><span class="glyphicon glyphicon-warning-sign"></span> <strong>Delete Confirmation </strong></div>
								<div class="modal-body">Are you sure to delete this item? <br>
								  Note the linked assets will also be removed in these datasets:
								  result,profile,match.
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

				</tr>
				</#list>
			</table>

		</div>
		<!-- panel -->
		
		<#if msg??>
		<div class="alert alert-warning alert-dismissible" role="alert">
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<strong>Status:</strong> 
             <#if msg=="">
              <strong>Success</strong><br>
             <#else>
              ${msg}
             </#if>
		</div>
        </#if>  <!-- if any message during upload -->
		

		</#if>

	</div>
	<!--  container -->

	<#include "footer.ftl">

</body>

</html>
