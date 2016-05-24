<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#myNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#">CloudScreening</a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav">

				<li class="active"><a href="/welcome"><span
						class="glyphicon glyphicon-home"></span></a></li>

				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">Workspace<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a id="HyperlinkAdvancedSearchTop" href="/inbox/open">Inbox</a></li>
						<li><a id="HyperlinkAdvancedSearchTop" href="/actionlist">User
								Actions</a></li>
						<li><a id="HyperlinkAdvancedSearchTop" href="/searchmatch">Match
								Review Search</a></li>

					</ul></li>

				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">PLM Search<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a id="HyperlinkAdvancedSearchTop" href="/profilelist">Interactive
								Lookup</a></li>
						<li><a id="HyperlinkStatisticalQueryTop" href="/batch">New
								Batch</a></li>
						<li><a id="HyperlinkStatisticalQueryTop" href="/batchlist">Batch
								List</a></li>
						<li><a id="HyperlinkStatisticalQueryTop" href="/profilelist">Profile
								List</a></li>
						<li><a id="HyperlinkStatisticalQueryTop" href="/resultlist">Result
								List</a></li>

					</ul></li>

				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">List Management<span
						class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a id="HyperlinkAdvancedSearchTop" href="/newplm">Import
								New List</a></li>
						<li><a id="HyperlinkStatisticalQueryTop" href="/plmlist">View
								List</a></li>
					</ul></li>


				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#">Blogs<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a id="HyperlinkAbrUpdateTop" href="/newpost">Post
								Blog</a></li>
						<li><a id="HyperlinkAbrUpdateTop" href="/">Blog List</a></li>
					</ul></li>

			</ul>
			<ul class="nav navbar-nav navbar-right">

				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" id="login"><i class="fa fa-user">
							User </i> <span class="caret"></span> </a>
					<ul class="dropdown-menu">
						<li><a href="#"><i class="fa fa-fw fa-user"></i> Profile</a>
						</li>
						<li><a href="#"><i class="fa fa-fw fa-envelope"></i>
								Inbox</a></li>
						<li><a href="#"><i class="fa fa-fw fa-gear"></i> Settings</a>
						</li>
						<li class="divider"></li>
						<li><a href="/logout"><i class="fa fa-fw fa-power-off"></i>
								Log Out</a></li>

					</ul></li>
			</ul>
		</div>
	</div>
</nav>


<!-- container-all -->
<div class="container-all">

	<div class="container">

		<link rel="stylesheet" href="/css/bootstrap.min.css">
		<script src="/js/jquery.min.js"></script>
		<script src="/js/bootstrap.min.js"></script>
		<link href="/css/font-awesome.min.css" rel="stylesheet"
			type="text/css">


		<#if username?? >
		<script type="text/javascript">
			$('#login').text("${username}");
		</script>
		<#else>



		<script type="text/javascript">
			window.location.href = "/login";
		</script>
		</#if>
	</div>