
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8"> 
        <meta charset="utf-8">
        <title>Screen a batch</title>
        <meta name="generator" content="Bootply" />
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
        <meta name="description" content="Bootstrap Bootstrap wizard example using the wizard component from ExactTarget. example snippet for Bootstrap." />
        
        
        <!--[if lt IE 9]>
          <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <link rel="apple-touch-icon" href="/bootstrap/img/apple-touch-icon.png">
        <link rel="apple-touch-icon" sizes="72x72" href="/bootstrap/img/apple-touch-icon-72x72.png">
        <link rel="apple-touch-icon" sizes="114x114" href="/bootstrap/img/apple-touch-icon-114x114.png">

        <link href="//netdna.bootstrapcdn.com/font-awesome/3.2.1/css/font-awesome.min.css" type="text/css" rel="stylesheet">
        <link href="//fuelcdn.com/fuelux/2.3.1/css/fuelux.css" type="text/css" rel="stylesheet"><link href="//fuelcdn.com/fuelux/2.3/css/fuelux-responsive.css" type="text/css" rel="stylesheet">

    </head>
    
    
    <body class="fuelux" >

    <#include "menu.ftl">
    <script src="/js/batchlist.js"></script>
    
	
	<div class="container">

        
  <div class="well wizard-example">
  <div id="MyWizard" class="wizard">
    <ul class="steps">
      <li data-target="#step1" class="active"><span class="badge badge-info">1</span>Step 1 - select a batch file <span class="chevron"></span></li>
      <li data-target="#step2"><span class="badge">2</span>Step 2 - select a profile <span class="chevron"></span></li>
      <li data-target="#step3"><span class="badge">3</span>Step 3 - complete <span class="chevron"></span></li>
    </ul>
    <div class="actions">
      <button class="btn btn-mini btn-prev"> <i class="icon-arrow-left"></i>Prev</button>
      <button class="btn btn-mini btn-next" data-last="Finish">Next<i class="icon-arrow-right"></i></button>
    </div>
  </div>
  <div class="step-content">
    
    <div class="step-pane active" id="step1">
    
		<#if recs??> <br>
		<div id="message"></div>

		<div class="panel panel-default">

			<div class="panel-heading">
				<strong>Batch List</strong> 
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
					<td>Select</td>
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


  				   <td><input type="radio" class="batches" name="batchid"
					value=${rec["batchid"]?c}></td>

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

		</#if>
    
    </div>
    
    <div class="step-pane" id="step2">
        <div id="profilelist"></div>
    </div>
    <div class="step-pane" id="step3">
    
            <div id='loadingmessage' style='display: none'>
				<br>
				<div class="alert alert-success">
					<img src='images/ajax-loader.gif' /> Screening in process, please
					wait ...
				</div>
			</div>

			<br><br>
			
			 <div id="t" class="text-center" style="font-size: 36px; display: none"><!-- pretend an enclosing class has big font size -->
               00:00
             </div>
			

			<br><br>

			 <div id='goto' style='display: none'>
			    <br>
 	  	        <div class="form-group">
	             <a href="/resultlist" class="btn btn-primary">Check Result</a> 
               </div>
	         </div>
			
			
    </div>
  </div>
  <br>
  <input type="button" class="btn" id="btnWizardPrev" value="Back">
  <input type="button" class="btn btn-primary" id="btnWizardNext" value="Next">

</div>
<!-- /well -->


        
        <script type='text/javascript' src="//fuelcdn.com/fuelux/2.3.1/loader.min.js"></script>

        <!-- JavaScript jQuery code from Bootply.com editor  -->
        
        <script type='text/javascript'>
        
        $(document).ready(function() {
        
			$('#MyWizard').on('change', function(e, data) {
			  console.log('change');
			  if(data.step===3 && data.direction==='next') {
			    // return e.preventDefault();
			  }
			  if(data.step===1 && data.direction==='next') {
			    
			    var selectedVal = "";
				var selected = $("input[type='radio'][name='batchid']:checked");
				if (selected.length > 0) {
				    selectedVal = selected.val();
				}
				
				
			    //alert("select profile: "+selectedVal);

				var url = '/getprofilelist?batchid='+selectedVal;
				
				$.ajax({
					url : url,
					data : '{}',
					success : function(data) {
						// Your Code here
		
						//alert(url);
		
						$('#profilelist').hide().html(data);
						$('#profilelist').html(data).slideDown("slow");
		
					}
				})
		
			    // return e.preventDefault();
			  }
			  
			  if(data.step===2 && data.direction==='next') {
			  
			    var selectedValb = "";
				var selected = $("input[type='radio'][name='batchid']:checked");
				if (selected.length > 0) {
				    selectedValb = selected.val();
				}
			  
			    //alert("select batch: "+selectedValb);
			    

                var selectedValp = "";
				selected = $("input[type='radio'][name='profileid']:checked");
				if (selected.length > 0) {
				    selectedValp = selected.val();
				}
			  
			    //alert("select profile: "+selectedValp);

			     $('#btnWizardPrev').hide();
        		 $('#btnWizardNext').hide();
			    
			     $('#loadingmessage').show();
			     $('#t').show();
			     
			     
			     $("#t").timer({
								action: 'start',
								seconds: 0
								});
			     
			     
			   var url = '/screen?batchid='+selectedValb+'&profileid='+selectedValp;
				
				//alert(url);
				
				$.ajax({
					url : url,
					type: 'post',
					data : '{}',
					success : function(data) {
					
					      //alert(url);
        			     $('#loadingmessage').hide();
        			     
        			     $("#t").timer({action: 'pause'});
        			     
        			     $('#goto').show('slow');
        			     
        			     //location.href = "/resultlist?batchid="+selectedValb;
		
					}
				})
			     
			     
			  }

			});
			$('#MyWizard').on('changed', function(e, data) {
			  console.log('changed');
			});
			$('#MyWizard').on('finished', function(e, data) {
			  console.log('finished');
			});
			$('#btnWizardPrev').on('click', function() {
			  $('#MyWizard').wizard('previous');
			});
			$('#btnWizardNext').on('click', function() {
			  $('#MyWizard').wizard('next','foo');
			});
			$('#btnWizardStep').on('click', function() {
			  var item = $('#MyWizard').wizard('selectedItem');
			  console.log(item.step);
			});
			$('#MyWizard').on('stepclick', function(e, data) {
			  console.log('step' + data.step + ' clicked');
			  if(data.step===1) {
			    // return e.preventDefault();
			  }
			});
			
			// optionally navigate back to 2nd step
			$('#btnStep2').on('click', function(e, data) {
			  $('[data-target=#step2]').trigger("click");
			});
			
        });
        
        </script>
        <script src="/js/timer.jquery.min.js"></script>
   	</div>	<!--  container -->
        
    <#include "footer.ftl">
        
    </body>
</html>