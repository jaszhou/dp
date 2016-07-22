<!DOCTYPE html>

<html>
<head>
<title>Match View</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<style>
.progress {
	position: relative;
	width: 400px;
	border: 1px solid #ddd;
	padding: 1px;
	border-radius: 3px;
}

.bar {
	background-color: #B4F5B4;
	width: 0%;
	height: 20px;
	border-radius: 3px;
}

.percent {
	position: absolute;
	display: inline-block;
	top: 3px;
	left: 48%;
}
</style>
</head>

<body>


	<#include "menu.ftl"> 
    <#include "matchviewmicro.ftl">

	<script src="/js/matchview.js"></script>
	<script src="/js/jquery.form.js"></script>

	<div class="container">

		<#include "matchmodalframe.ftl"> <#if match??>

		<div class="row">
			<div class="col-md-12">
				<div class="well">
					Match ID: <b>${match["id"]?c}</b> Record ID: <b>${match["recid"]}
					</b> Status:<b> ${match["status"]} </b> Number of Entities Matched:<b>
						${match["entity"]?size} </b> <#if match["threshold"]??> Match Score
					Threshold: <b> ${match["threshold"]} </b> </#if>
				</div>
			</div>
		</div>

		<form method="post" action="/updatematch">
			<div class="row">

				<div class="col-md-6">

					<div class="panel panel-default">
						<div class="panel-heading">Input Detail</div>
						<@dumpInput match["input"] keys />
					</div>
				</div>


				<div class="col-md-6">

					<#list match["entity"] as entity>
					<div id="entity" class="panel panel-default">
						<div class="panel-heading">
							Entity Detail <b>Score: ${entity["score"]}%</b> Entity ID:
							<strong>
							
							 <#if entity["@id"]??>
                                ${entity["@id"]}
                             <#else>
                                ${entity["_id"]}
                             </#if>
                             
                            </strong> <span class="glyphicon glyphicon-collapse-down"></span>
						</div>
						<@dumpProperties entity entity["match fields"]/>
					</div>

					</#list>
				</div>
			</div>


			<div class="row">
				<div class="col-md-12">

					<div class="panel panel-default">
						<div class="panel-heading">Review Match</div>
						<div class="panel-body">

							<div class="form-group">


								<label for="status">Update Status To:</label> <select
									name="status">
									<option value="False Positive">False Positive</option>
									<option value="Further Investigation">Further
										Investigation</option>
									<option value="True Match">True Match</option>
								</select>

							</div>

							<div class="form-group">

								<textarea placeholder="put some comment here" name="comment"
									cols="100" rows="5"><#if match["comment"]??>${match["comment"]}</#if></textarea>
							</div>

							<div class="form-group">
								<input type="hidden" name="matchid" value="${match["id"]?c}"></input>
							</div>


							<button type="submit" class="btn btn-primary">Update
								Status</button>

							<a id="showattachment" href="/attachment?matchid=${match["id"]?c}" class="btn btn-default">Show Attachments</a> <a
								href="javascript:history.back()" class="btn btn-default">Back</a>

						</div>
					</div>
		</form>

		<div id="attachment"></div>

		<form method="POST" id="myForm" action="/attachfile"
			style="margin-top: 30px;" enctype="multipart/form-data">

			<div class="panel panel-default">

				<div class="panel-heading">Upload attachment</div>
				<div class="panel-body">
					<div class="form-group">
						<input type="hidden" class="form-control" name="matchid"
							value="${match["id"]?c}"> </input>
					</div>


					<div class="form-group">

						<input type="file" class="form-control"
							placeholder="Upload a file" name="upload" size="20" required>
						</input>

					</div>

					<div class="form-group">
						<label for="upload">Description:</label> <input type="text"
							class="form-control" placeholder="Description" name="description"
							size="20"> </input>
					</div>


					<button type="submit" class="btn btn-primary">Attach</button>

					<a id="selectp" href="" class="btn btn-default">Reset</a>


					<p>
					<div class="progress">
						<div class="bar"></div>
						<div class="percent">0%</div>
					</div>

					<div id="status"></div>

				</div>
		</form>


		</#if>


		<p>
	</div>

	</div>
	</div>

	<#include "footer.ftl">
	
	<script>
	$(function() {
	   //alert($('#entity .panel-heading').html());
	   //$('#entity .panel-heading').first().click();
	 });
	</script>
</body>

</html>
