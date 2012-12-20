
<%@ page import="uk.ac.ed.redic.EquipmentItem" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'equipmentItem.label', default: 'EquipmentItem')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="container-fluid">
		
		<div class="row-fluid">
    		<div class="span2">
      			<!--Sidebar content-->
      			<ul class="nav nav-list" class="span1">
		  			<li class="nav-header">Equipment</li>
		  			<li><a href="${createLink(controller: 'equipmentItem', action: 'list')}">Browse</a></li>
		  			<li><a href="${createLink(controller: 'equipmentItem', action: 'create')}">Add New</a></li>
		  			<li><a href="${createLink(controller: 'equipmentManager', action: 'index')}">Manager</a></li>
		  			<li class="active"><a href="#">View Item</a></li>
				</ul>
    		</div>
    		<div class="span10">
      			<!--Body content-->
      			
      			<div id="show-equipmentItem" class="content scaffold-show" role="main">
					<h1><g:message code="default.show.label" args="[entityName]" /></h1>
					<g:if test="${flash.message}">
					<div class="message" role="status">${flash.message}</div>
					</g:if>
					
					
					<dl class="dl-horizontal">
						<g:if test="${equipmentItemInstance?.approvalStatus}">
							<dt>Approval Status</dt>
							<dd><g:formatBoolean boolean="${equipmentItemInstance?.approvalStatus}" /></dd>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.assetCost}">
						<li class="fieldcontain">
							<span id="assetCost-label" class="property-label"><g:message code="equipmentItem.assetCost.label" default="Asset Cost" /></span>
							
								<span class="property-value" aria-labelledby="assetCost-label"><g:fieldValue bean="${equipmentItemInstance}" field="assetCost"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.attachments}">
						<li class="fieldcontain">
							<span id="attachments-label" class="property-label"><g:message code="equipmentItem.attachments.label" default="Attachments" /></span>
							
								<span class="property-value" aria-labelledby="attachments-label"><g:formatBoolean boolean="${equipmentItemInstance?.attachments}" /></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.buildingID}">
						<li class="fieldcontain">
							<span id="buildingID-label" class="property-label"><g:message code="equipmentItem.buildingID.label" default="Building ID" /></span>
							
								<span class="property-value" aria-labelledby="buildingID-label"><g:fieldValue bean="${equipmentItemInstance}" field="buildingID"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.contact1Email}">
						<li class="fieldcontain">
							<span id="contact1Email-label" class="property-label"><g:message code="equipmentItem.contact1Email.label" default="Contact1 Email" /></span>
							
								<span class="property-value" aria-labelledby="contact1Email-label"><g:fieldValue bean="${equipmentItemInstance}" field="contact1Email"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.contact1Name}">
						<li class="fieldcontain">
							<span id="contact1Name-label" class="property-label"><g:message code="equipmentItem.contact1Name.label" default="Contact1 Name" /></span>
							
								<span class="property-value" aria-labelledby="contact1Name-label"><g:fieldValue bean="${equipmentItemInstance}" field="contact1Name"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.contact1Phone}">
						<li class="fieldcontain">
							<span id="contact1Phone-label" class="property-label"><g:message code="equipmentItem.contact1Phone.label" default="Contact1 Phone" /></span>
							
								<span class="property-value" aria-labelledby="contact1Phone-label"><g:fieldValue bean="${equipmentItemInstance}" field="contact1Phone"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.contact1URL}">
						<li class="fieldcontain">
							<span id="contact1URL-label" class="property-label"><g:message code="equipmentItem.contact1URL.label" default="Contact1 URL" /></span>
							
								<span class="property-value" aria-labelledby="contact1URL-label"><g:fieldValue bean="${equipmentItemInstance}" field="contact1URL"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.created}">
						<li class="fieldcontain">
							<span id="created-label" class="property-label"><g:message code="equipmentItem.created.label" default="Created" /></span>
							
								<span class="property-value" aria-labelledby="created-label"><g:formatDate date="${equipmentItemInstance?.created}" /></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.dateAdded}">
						<li class="fieldcontain">
							<span id="dateAdded-label" class="property-label"><g:message code="equipmentItem.dateAdded.label" default="Date Added" /></span>
							
								<span class="property-value" aria-labelledby="dateAdded-label"><g:formatDate date="${equipmentItemInstance?.dateAdded}" /></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.description}">
						<li class="fieldcontain">
							<span id="description-label" class="property-label"><g:message code="equipmentItem.description.label" default="Description" /></span>
							
								<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${equipmentItemInstance}" field="description"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.equipmentID}">
						<li class="fieldcontain">
							<span id="equipmentID-label" class="property-label"><g:message code="equipmentItem.equipmentID.label" default="Equipment ID" /></span>
							
								<span class="property-value" aria-labelledby="equipmentID-label"><g:fieldValue bean="${equipmentItemInstance}" field="equipmentID"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.equipmentWebPage}">
						<li class="fieldcontain">
							<span id="equipmentWebPage-label" class="property-label"><g:message code="equipmentItem.equipmentWebPage.label" default="Equipment Web Page" /></span>
							
								<span class="property-value" aria-labelledby="equipmentWebPage-label"><g:fieldValue bean="${equipmentItemInstance}" field="equipmentWebPage"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.faculty}">
						<li class="fieldcontain">
							<span id="faculty-label" class="property-label"><g:message code="equipmentItem.faculty.label" default="Faculty" /></span>
							
								<span class="property-value" aria-labelledby="faculty-label"><g:fieldValue bean="${equipmentItemInstance}" field="faculty"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.itemType}">
						<li class="fieldcontain">
							<span id="itemType-label" class="property-label"><g:message code="equipmentItem.itemType.label" default="Item Type" /></span>
							
								<span class="property-value" aria-labelledby="itemType-label"><g:fieldValue bean="${equipmentItemInstance}" field="itemType"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.lastSynced}">
						<li class="fieldcontain">
							<span id="lastSynced-label" class="property-label"><g:message code="equipmentItem.lastSynced.label" default="Last Synced" /></span>
							
								<span class="property-value" aria-labelledby="lastSynced-label"><g:formatDate date="${equipmentItemInstance?.lastSynced}" /></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.lastUpdated}">
						<li class="fieldcontain">
							<span id="lastUpdated-label" class="property-label"><g:message code="equipmentItem.lastUpdated.label" default="Last Updated" /></span>
							
								<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${equipmentItemInstance?.lastUpdated}" /></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.level}">
						<li class="fieldcontain">
							<span id="level-label" class="property-label"><g:message code="equipmentItem.level.label" default="Level" /></span>
							
								<span class="property-value" aria-labelledby="level-label"><g:fieldValue bean="${equipmentItemInstance}" field="level"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.locationID}">
						<li class="fieldcontain">
							<span id="locationID-label" class="property-label"><g:message code="equipmentItem.locationID.label" default="Location ID" /></span>
							
								<span class="property-value" aria-labelledby="locationID-label"><g:fieldValue bean="${equipmentItemInstance}" field="locationID"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.modified}">
						<li class="fieldcontain">
							<span id="modified-label" class="property-label"><g:message code="equipmentItem.modified.label" default="Modified" /></span>
							
								<span class="property-value" aria-labelledby="modified-label"><g:fieldValue bean="${equipmentItemInstance}" field="modified"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.name}">
						<li class="fieldcontain">
							<span id="name-label" class="property-label"><g:message code="equipmentItem.name.label" default="Name" /></span>
							
								<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${equipmentItemInstance}" field="name"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.name1}">
						<li class="fieldcontain">
							<span id="name1-label" class="property-label"><g:message code="equipmentItem.name1.label" default="Name1" /></span>
							
								<span class="property-value" aria-labelledby="name1-label"><g:fieldValue bean="${equipmentItemInstance}" field="name1"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.orgUnitID}">
						<li class="fieldcontain">
							<span id="orgUnitID-label" class="property-label"><g:message code="equipmentItem.orgUnitID.label" default="Org Unit ID" /></span>
							
								<span class="property-value" aria-labelledby="orgUnitID-label"><g:fieldValue bean="${equipmentItemInstance}" field="orgUnitID"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.propertyBag}">
						<li class="fieldcontain">
							<span id="propertyBag-label" class="property-label"><g:message code="equipmentItem.propertyBag.label" default="Property Bag" /></span>
							
								<span class="property-value" aria-labelledby="propertyBag-label"><g:fieldValue bean="${equipmentItemInstance}" field="propertyBag"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.publishingLevel}">
						<li class="fieldcontain">
							<span id="publishingLevel-label" class="property-label"><g:message code="equipmentItem.publishingLevel.label" default="Publishing Level" /></span>
							
								<span class="property-value" aria-labelledby="publishingLevel-label"><g:fieldValue bean="${equipmentItemInstance}" field="publishingLevel"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.relatedFacilityID}">
						<li class="fieldcontain">
							<span id="relatedFacilityID-label" class="property-label"><g:message code="equipmentItem.relatedFacilityID.label" default="Related Facility ID" /></span>
							
								<span class="property-value" aria-labelledby="relatedFacilityID-label"><g:fieldValue bean="${equipmentItemInstance}" field="relatedFacilityID"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.rowID}">
						<li class="fieldcontain">
							<span id="rowID-label" class="property-label"><g:message code="equipmentItem.rowID.label" default="Row ID" /></span>
							
								<span class="property-value" aria-labelledby="rowID-label"><g:fieldValue bean="${equipmentItemInstance}" field="rowID"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.simpleName}">
						<li class="fieldcontain">
							<span id="simpleName-label" class="property-label"><g:message code="equipmentItem.simpleName.label" default="Simple Name" /></span>
							
								<span class="property-value" aria-labelledby="simpleName-label"><g:fieldValue bean="${equipmentItemInstance}" field="simpleName"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.supplierWebPage}">
						<li class="fieldcontain">
							<span id="supplierWebPage-label" class="property-label"><g:message code="equipmentItem.supplierWebPage.label" default="Supplier Web Page" /></span>
							
								<span class="property-value" aria-labelledby="supplierWebPage-label"><g:fieldValue bean="${equipmentItemInstance}" field="supplierWebPage"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.taxonomy}">
						<li class="fieldcontain">
							<span id="taxonomy-label" class="property-label"><g:message code="equipmentItem.taxonomy.label" default="Taxonomy" /></span>
							
								<span class="property-value" aria-labelledby="taxonomy-label"><g:fieldValue bean="${equipmentItemInstance}" field="taxonomy"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.uniqueID}">
						<li class="fieldcontain">
							<span id="uniqueID-label" class="property-label"><g:message code="equipmentItem.uniqueID.label" default="Unique ID" /></span>
							
								<span class="property-value" aria-labelledby="uniqueID-label"><g:fieldValue bean="${equipmentItemInstance}" field="uniqueID"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.universityAssetCode}">
						<li class="fieldcontain">
							<span id="universityAssetCode-label" class="property-label"><g:message code="equipmentItem.universityAssetCode.label" default="University Asset Code" /></span>
							
								<span class="property-value" aria-labelledby="universityAssetCode-label"><g:fieldValue bean="${equipmentItemInstance}" field="universityAssetCode"/></span>
							
						</li>
						</g:if>
					
						<g:if test="${equipmentItemInstance?.urlPath}">
						<li class="fieldcontain">
							<span id="urlPath-label" class="property-label"><g:message code="equipmentItem.urlPath.label" default="Url Path" /></span>
							
								<span class="property-value" aria-labelledby="urlPath-label"><g:fieldValue bean="${equipmentItemInstance}" field="urlPath"/></span>
							
						</li>
						</g:if>
					
					</dl>
					<g:form>
						<fieldset class="buttons">
							<g:hiddenField name="id" value="${equipmentItemInstance?.id}" />
							<g:link class="btn btn-inverse" action="edit" id="${equipmentItemInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
							<g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						</fieldset>
					</g:form>
				</div>
      		
      		
      		
      		
      		</div>
      		
      	</div>
      		
      	</div>	
		
	</body>
</html>
