<%@ page import="uk.ac.ed.redic.EquipmentItem" %>



<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'approvalStatus', 'error')} ">
	<label for="approvalStatus">
		<g:message code="equipmentItem.approvalStatus.label" default="Approval Status" />
		
	</label>
	<g:checkBox name="approvalStatus" value="${equipmentItemInstance?.approvalStatus}" />
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'assetCost', 'error')} required">
	<label for="assetCost">
		<g:message code="equipmentItem.assetCost.label" default="Asset Cost" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="assetCost" required="" value="${fieldValue(bean: equipmentItemInstance, field: 'assetCost')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'attachments', 'error')} ">
	<label for="attachments">
		<g:message code="equipmentItem.attachments.label" default="Attachments" />
		
	</label>
	<g:checkBox name="attachments" value="${equipmentItemInstance?.attachments}" />
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'buildingID', 'error')} ">
	<label for="buildingID">
		<g:message code="equipmentItem.buildingID.label" default="Building ID" />
		
	</label>
	<g:textField name="buildingID" value="${equipmentItemInstance?.buildingID}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'contact1Email', 'error')} ">
	<label for="contact1Email">
		<g:message code="equipmentItem.contact1Email.label" default="Contact1 Email" />
		
	</label>
	<g:textField name="contact1Email" value="${equipmentItemInstance?.contact1Email}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'contact1Name', 'error')} ">
	<label for="contact1Name">
		<g:message code="equipmentItem.contact1Name.label" default="Contact1 Name" />
		
	</label>
	<g:textField name="contact1Name" value="${equipmentItemInstance?.contact1Name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'contact1Phone', 'error')} ">
	<label for="contact1Phone">
		<g:message code="equipmentItem.contact1Phone.label" default="Contact1 Phone" />
		
	</label>
	<g:textField name="contact1Phone" value="${equipmentItemInstance?.contact1Phone}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'contact1URL', 'error')} ">
	<label for="contact1URL">
		<g:message code="equipmentItem.contact1URL.label" default="Contact1 URL" />
		
	</label>
	<g:textField name="contact1URL" value="${equipmentItemInstance?.contact1URL}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'created', 'error')} required">
	<label for="created">
		<g:message code="equipmentItem.created.label" default="Created" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="created" precision="day"  value="${equipmentItemInstance?.created}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'dateAdded', 'error')} required">
	<label for="dateAdded">
		<g:message code="equipmentItem.dateAdded.label" default="Date Added" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="dateAdded" precision="day"  value="${equipmentItemInstance?.dateAdded}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'description', 'error')} ">
	<label for="description">
		<g:message code="equipmentItem.description.label" default="Description" />
		
	</label>
	<g:textField name="description" value="${equipmentItemInstance?.description}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'equipmentID', 'error')} required">
	<label for="equipmentID">
		<g:message code="equipmentItem.equipmentID.label" default="Equipment ID" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="number" name="equipmentID" required="" value="${fieldValue(bean: equipmentItemInstance, field: 'equipmentID')}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'equipmentWebPage', 'error')} ">
	<label for="equipmentWebPage">
		<g:message code="equipmentItem.equipmentWebPage.label" default="Equipment Web Page" />
		
	</label>
	<g:textField name="equipmentWebPage" value="${equipmentItemInstance?.equipmentWebPage}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'faculty', 'error')} ">
	<label for="faculty">
		<g:message code="equipmentItem.faculty.label" default="Faculty" />
		
	</label>
	<g:textField name="faculty" value="${equipmentItemInstance?.faculty}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'itemType', 'error')} ">
	<label for="itemType">
		<g:message code="equipmentItem.itemType.label" default="Item Type" />
		
	</label>
	<g:textField name="itemType" value="${equipmentItemInstance?.itemType}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'lastSynced', 'error')} required">
	<label for="lastSynced">
		<g:message code="equipmentItem.lastSynced.label" default="Last Synced" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="lastSynced" precision="day"  value="${equipmentItemInstance?.lastSynced}"  />
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'level', 'error')} ">
	<label for="level">
		<g:message code="equipmentItem.level.label" default="Level" />
		
	</label>
	<g:textField name="level" value="${equipmentItemInstance?.level}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'locationID', 'error')} ">
	<label for="locationID">
		<g:message code="equipmentItem.locationID.label" default="Location ID" />
		
	</label>
	<g:textField name="locationID" value="${equipmentItemInstance?.locationID}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'modified', 'error')} ">
	<label for="modified">
		<g:message code="equipmentItem.modified.label" default="Modified" />
		
	</label>
	<g:textField name="modified" value="${equipmentItemInstance?.modified}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="equipmentItem.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${equipmentItemInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'name1', 'error')} ">
	<label for="name1">
		<g:message code="equipmentItem.name1.label" default="Name1" />
		
	</label>
	<g:textField name="name1" value="${equipmentItemInstance?.name1}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'orgUnitID', 'error')} ">
	<label for="orgUnitID">
		<g:message code="equipmentItem.orgUnitID.label" default="Org Unit ID" />
		
	</label>
	<g:textField name="orgUnitID" value="${equipmentItemInstance?.orgUnitID}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'propertyBag', 'error')} ">
	<label for="propertyBag">
		<g:message code="equipmentItem.propertyBag.label" default="Property Bag" />
		
	</label>
	<g:textField name="propertyBag" value="${equipmentItemInstance?.propertyBag}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'publishingLevel', 'error')} ">
	<label for="publishingLevel">
		<g:message code="equipmentItem.publishingLevel.label" default="Publishing Level" />
		
	</label>
	<g:textField name="publishingLevel" value="${equipmentItemInstance?.publishingLevel}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'relatedFacilityID', 'error')} ">
	<label for="relatedFacilityID">
		<g:message code="equipmentItem.relatedFacilityID.label" default="Related Facility ID" />
		
	</label>
	<g:textField name="relatedFacilityID" value="${equipmentItemInstance?.relatedFacilityID}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'rowID', 'error')} ">
	<label for="rowID">
		<g:message code="equipmentItem.rowID.label" default="Row ID" />
		
	</label>
	<g:textField name="rowID" value="${equipmentItemInstance?.rowID}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'simpleName', 'error')} ">
	<label for="simpleName">
		<g:message code="equipmentItem.simpleName.label" default="Simple Name" />
		
	</label>
	<g:textField name="simpleName" value="${equipmentItemInstance?.simpleName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'supplierWebPage', 'error')} ">
	<label for="supplierWebPage">
		<g:message code="equipmentItem.supplierWebPage.label" default="Supplier Web Page" />
		
	</label>
	<g:textField name="supplierWebPage" value="${equipmentItemInstance?.supplierWebPage}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'taxonomy', 'error')} ">
	<label for="taxonomy">
		<g:message code="equipmentItem.taxonomy.label" default="Taxonomy" />
		
	</label>
	<g:textField name="taxonomy" value="${equipmentItemInstance?.taxonomy}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'uniqueID', 'error')} ">
	<label for="uniqueID">
		<g:message code="equipmentItem.uniqueID.label" default="Unique ID" />
		
	</label>
	<g:textField name="uniqueID" value="${equipmentItemInstance?.uniqueID}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'universityAssetCode', 'error')} ">
	<label for="universityAssetCode">
		<g:message code="equipmentItem.universityAssetCode.label" default="University Asset Code" />
		
	</label>
	<g:textField name="universityAssetCode" value="${equipmentItemInstance?.universityAssetCode}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: equipmentItemInstance, field: 'urlPath', 'error')} ">
	<label for="urlPath">
		<g:message code="equipmentItem.urlPath.label" default="Url Path" />
		
	</label>
	<g:textField name="urlPath" value="${equipmentItemInstance?.urlPath}"/>
</div>

