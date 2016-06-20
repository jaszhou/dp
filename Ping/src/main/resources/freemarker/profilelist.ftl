<!DOCTYPE html>

<html>
<head>
<title>Profile List</title>
<!--
      <script src="/js/profilelist.js"></script> 
    -->


</head>

<body>

	<#include "menu.ftl">

	<div class="container">

		<br>
		<div id="status"></div>

		<div class="panel panel-default">
			<div class="panel-heading">
				<strong>Profile List</strong>
			</div>

			<div class="panel-body">
				<p>These are profiles which were generated from batch files. The
					profile is required for both batch screening and interactive
					lookup.</p>
			</div>

			<div id="profilelist">

				<#if recs??>

				<table class="table table-striped table-hover">
					<tr>
						<td>Profile ID</td>
						<td>Profile Name</td>
						<td>Creator</td>
						<td>Creation Date</td>
						<td>Description</td>
						<td>Edit</td>
						<td>Delete</td>
						<td>Search</td>
					</tr>
					<#list recs as rec>
					<tr>
						<td><a href="/profile?profileid=${rec["id"]}">${rec["id"]}</a></td>
						<td><a href="/profile?profileid=${rec["id"]}">${rec["name"]}</a></td>
						<td><a href="/profile?profileid=${rec["id"]}">${rec.creator}</a></td>
						<td><a href="/profile?profileid=${rec["id"]}">${rec["date"]?datetime}</a></td>
						<td><#if rec["comment"]??>${rec["comment"]}</#if></td>
						<td><a href="/profile?profileid=${rec["id"]}"><span
								class="glyphicon glyphicon-pencil"></span></a></td>



						<td><a href="#" data-href="/deleteprofile?profileid=${rec["id"]}" id="delete" data-toggle="modal"
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


						<td><a href="/searchprofile?profileid=${rec["id"]}" class="search"><span
								class="glyphicon glyphicon-search"></span></a></td>
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
			<strong>Note!</strong> Please click the search icon on a profile to
			perform Interactive Lookup!
		</div>


	</div>
	<!--  container -->

	<#include "footer.ftl">
</body>

</html>
