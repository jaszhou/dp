<!-- 
<nav class="navbar navbar-inverse">
-->
<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#myNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="/plmlist">WCTS</a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav">

		
		
				<li id="listmng" class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#"><span
						class="glyphicon glyphicon-th-list"></span> List Management<span
						class="caret"></span></a>
					<ul class="dropdown-menu   bullet">
						<li><a href="/newplm">Import New List</a></li>
						<li><a href="/plmlist">View List</a></li>
						<li><a href="/actionlist">Change History</a></li>
					</ul></li>

		
			

				<li id="adminlist" class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#"><span
						class="glyphicon glyphicon-edit"></span> Administration <span class="caret"></span></a>
					<ul class="dropdown-menu   bullet">
						<li><a href="/userlist">User List</a></li>
					</ul></li>

			</ul>
			<ul class="nav navbar-nav navbar-right">

				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown" id="login"><i class="fa fa-user">
							User </i> <span class="caret"></span> </a>
					<ul class="dropdown-menu   bullet">
						<li><a href="/edituser"><i class="fa fa-fw fa-user"></i> Profile</a>
						</li>
						<li><a href="/resetpwd"><i class="fa fa-key"></i> Reset Password</a>
						</li>
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


<!-- check if mobile device to include sidebar or not -->

 <div class="row">

	<#include "sidebar.ftl">



	<div class="col-sm-10" id="mainCol">
	
	<link href="/css/bootstrap.min.css" rel="stylesheet" type="text/css">
	<link href="/css/font-awesome.min.css" rel="stylesheet" type="text/css">
	<link href="/css/customize.css" rel="stylesheet" type="text/css">
	<link href="/css/dropdowns-enhancement.css" rel="stylesheet" type="text/css">
	
	<script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
	
	  ga('create', 'UA-72004708-1', 'auto');
	  ga('send', 'pageview');

  </script>

	<script src="/js/jquery.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/dropdowns-enhancement.js"></script>
	
	<script type="text/javascript">
			$(document).ready(function () {
			  $('#adminlist').hide();
			  $('#listmng').hide();
  		    });
	</script>

	<#if username?? >
	<script type="text/javascript">
	  $(document).ready(function () {
		$('#login').html(
				"<span class='glyphicon glyphicon-user'></span><#if clientname??> <strong>${clientname}</strong>|</#if> ${username}");
				
		// hide admin menu if user is not admin
		
		
	    });
				
	</script>
	<#else>
	<script type="text/javascript">
		window.location.href = "/login";
	</script>
	</#if>


	<#if roles?? && roles?seq_contains("Administrator")>
		<script type="text/javascript">
				$(document).ready(function () {
				  $('#adminlist').show();
				  $('#listmng').show();
	  		    });
		</script>
	
	</#if>
	
