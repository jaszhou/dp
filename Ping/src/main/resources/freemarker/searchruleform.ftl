

        <script src="/js/searchrulelist.js"></script>
		<div class="panel panel-default">
			<div class="panel-heading">
				<strong>Apply rules to result ID ${resultid}</strong>
			</div>

			<div id="rulelist">

				<#if recs??>
             <form id="myForm" method="post" action="/applyrule">
				<table class="table table-striped table-hover">
					<tr>
						<td>Rule ID</td>
						<td>Rule Name</td>
						<td>Creator</td>
						<td>Creation Date</td>
						<td>Description</td>
						<td>Select</td>
					</tr>
					<#list recs as rec>
					<tr>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec["id"]}</a></td>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec["name"]}</a></td>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec.creator}</a></td>
						<td><a href="/rule?ruleid=${rec["id"]}">${rec["date"]?datetime}</a></td>
						<td><#if rec["comment"]??>${rec["comment"]}</#if></td>
						<td>
						
						<input class="check" type="checkbox" name="sel" value="${rec["id"]}" />
						</td>

					</tr>
					</#list>
				</table>

                       <input type="hidden" name="resultid" value="${resultid}"></input>
                       
                       
                       <a id="apply" href="/applyrule" class="btn btn-primary">Apply Rules</a>
                       
                  </form>
				</#if>
		</div>

         <div id="status"></div>
	