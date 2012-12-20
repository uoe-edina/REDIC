<%@ page import="uk.ac.ed.redic.EquipmentItem" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'equipmentItem.label', default: 'EquipmentItem')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
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
		  			<li class="active"><a href="#">Edit Item</a></li>
				</ul>
    		</div>
    		<div class="span10">
      			<!--Body content-->
      			
				<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
				<g:if test="${flash.message}">
				<div class="alert alert-error">${flash.message}</div>
				</g:if>
				<g:hasErrors bean="${equipmentItemInstance}">
				<div class="alert alert-error">
				<ul class="errors" role="alert">
					<g:eachError bean="${equipmentItemInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
				</div>
				</g:hasErrors>
				<g:form method="post" >
					<g:hiddenField name="id" value="${equipmentItemInstance?.id}" />
					<g:hiddenField name="version" value="${equipmentItemInstance?.version}" />
					<fieldset class="form">
						<g:render template="form"/>
					</fieldset>
					<fieldset class="buttons">
						<g:actionSubmit class="btn btn-success" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
						<g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</fieldset>
				</g:form>
		
      		</div>
      	</div>
		</div>
		
		
		
		
	</body>
</html>
