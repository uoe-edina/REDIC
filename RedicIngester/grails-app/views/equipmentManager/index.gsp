<!doctype html>
<html>
<head>
<meta name="layout" content="main" />
<title>Equipment Manager</title>


<g:javascript library="jquery" />


</head>
<body>


	<g:javascript>
			function showProcessing(){
				$().button('loading')
			}
			
			function hideProcessing(){
				$().button('reset')
			}
		</g:javascript>


	<div class="container-fluid">

		<div class="row-fluid">
			<div class="span2">
				<!--Sidebar content-->
				<ul class="nav nav-list" class="span1">
					<li class="nav-header">Equipment</li>
					<li><a
						href="${createLink(controller: 'equipmentItem', action: 'list')}">Browse</a></li>
					<li><a
						href="${createLink(controller: 'equipmentItem', action: 'create')}">Add
							New</a></li>
					<li class="active"><a href="#">Manager</a></li>
				</ul>
			</div>
			<div class="span10">
				<!--Body content-->
				
				<h1>Equipment Manager</h1>
				<p>This is the Equipment Manager interface, from here you can
					upload equipment data in the form of a tab separated value file and
					then sync this with DSpace.</p>

				<g:if test="${flash.message}">
					<div class="alert alert-success">
						${flash.message}
					</div>
				</g:if>

				<%--				<div id="success" class="alert alert-success"></div>--%>

				<div>
					<g:form url="[controller: 'equipmentItem', action:'list']">
						<legend>View Equipment Data</legend>
						<ul>
							<p>This action will browse the equipment data currently
								stored</p>
							<g:submitButton name="button" value="Browse Equipment"
								class="btn btn-success" />
						</ul>
					</g:form>

					<g:form url="[controller: 'equipmentManager', action:'deleteAll']">
						<legend>Delete Equipment Data</legend>
						<ul>
							<p>This action will delete all equipment data</p>
							<g:submitButton name="button" value="Delete All"
								class="btn btn-danger" />
						</ul>
					</g:form>




					<%--				<g:formRemote action="uploadEquipmentData" --%>
					<%--				              name="myUpload"--%>
					<%--				              update="success"--%>
					<%--				              url="[controller: 'equipmentManager', action:'uploadEquipmentData']"--%>
					<%--				              onLoading="showProcessing()"--%>
					<%--              			      onComplete="hideProcessing()"> --%>

					<g:uploadForm action="uploadEquipmentData" name="myUpload"
						url="[controller: 'equipmentManager', action:'uploadEquipmentData']">
						<div>
							<legend>Upload Equipment Data</legend>
							<g:select name="metadata" from='${['Southampton data','Cerif data', 'EPSRC data', 'Bath data']}'
								value="${metadata}"
								noSelection="['':'-Select metadata type-']" />
							<g:select name="type" from='${['tsv','xml'] }' value="${type}"
								noSelection="['':'-Select type of file-']" />
							
							<g:javascript>
		    				$(function(){
		    				
		    					var $type = $('#type');
		    						    					
		    					$type.attr('disabled','disabled');
		    					
		    					$('#metadata').on('change', function(){
	
									if(!this.value) {
										$type.attr('disabled','disabled');
									} else {
										
										switch(this.value){ 
											case 'Southampton data':
												$type.find('option').removeAttr('disabled');
												break;
											case 'Cerif data':
												$type.find('option[value="tsv"]').attr('disabled','disabled');
												break;
											case 'EPSRC data':
												$type.find('option').removeAttr('disabled');
												break;
											case 'Bath data':
												$type.find('option').removeAttr('disabled');
												break;
											case 'Edinburgh Equipment data':
												$type.find('option[value="tsv"]').attr('disabled','disabled');
												break;
										}
										
										$type.removeAttr('disabled');
									}
									
									$type[0].selectedIndex = 0;
									
		    					});	    					
		    				});
		    				</g:javascript>
						</div>
						<div>
							<input type="file" name="file" />
							<g:submitButton name="button" value="Upload"
								class="btn btn-primary" data-loading-text="Processing..." />
							</div>
						</g:uploadForm>
						
						<%-- <g:uploadForm url="[controller: 'cerif', action:'syncCerifXML']"> 
						<input type="file" name="cerifFile" /> 
						<g:submitButton name="button1" value="Send to DSpace"
							class="btn btn-success" />
					</g:uploadForm> --%>

				
		
				</div>
			</div>
		</div>
</body>
</html>
