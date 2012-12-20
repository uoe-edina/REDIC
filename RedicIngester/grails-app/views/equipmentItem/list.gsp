
<%@ page import="uk.ac.ed.redic.EquipmentItem"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'equipmentItem.label', default: 'Equipment')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>


	<div class="container-fluid">

		<div class="row-fluid">
			<div class="span2">
				<!--Sidebar content-->
				<ul class="nav nav-list" class="span1">
					<li class="nav-header">Equipment</li>
					<li class="active"><a href="#">Browse</a></li>
					<li><a
						href="${createLink(controller: 'equipmentItem', action: 'create')}">Add
							New</a></li>
					<li><a
						href="${createLink(controller: 'equipmentManager', action: 'index')}">Manager</a></li>
				</ul>
			</div>
			<div class="span10">
				<!--Body content-->

				<div id="list-equipmentItem" class="span10 offset1">
					<!--  <div id="list-equipmentItem" class="content scaffold-list" role="main"> -->
					<h1>
						<g:message code="default.list.label" args="[entityName]" />
					</h1>
					<g:if test="${flash.message}">
						<div class="alert alert-success" role="status">
							${flash.message}
						</div>
					</g:if>
					<table class="table table-striped table-hover">
						<thead>
							<tr>

								<g:sortableColumn property="id"
									title="${message(code: 'equipmentItem.id.label', default: 'ID')}" />

								<g:sortableColumn property="equipmentID"
									title="${message(code: 'equipmentItem.equipmentId.label', default: 'Equipment ID')}" />

								<g:sortableColumn property="name1"
									title="${message(code: 'equipmentItem.name1.label', default: 'Name1')}" />


								<g:sortableColumn property="contact1Email"
									title="${message(code: 'equipmentItem.contact1Email.label', default: 'Contact1 Email')}" />

								<g:sortableColumn property="contact1Name"
									title="${message(code: 'equipmentItem.contact1Name.label', default: 'Contact1 Name')}" />


							</tr>
						</thead>
						<tbody>
							<g:each in="${equipmentItemInstanceList}" status="i"
								var="equipmentItemInstance">
								<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

									<td><g:link action="show" id="${equipmentItemInstance.id}">
											${fieldValue(bean: equipmentItemInstance, field: "id")}
										</g:link></td>

									<td>
										${fieldValue(bean: equipmentItemInstance, field: "equipmentID")}
									</td>

									<td>
										${fieldValue(bean: equipmentItemInstance, field: "name1")}
									</td>

									<td>
										${fieldValue(bean: equipmentItemInstance, field: "contact1Email")}
									</td>

									<td>
										${fieldValue(bean: equipmentItemInstance, field: "contact1Name")}
									</td>

									<td><g:if
											test="${equipmentItemInstance.dspaceHandle != null}">
											<a href="${equipmentItemInstance.dspaceHandle}"><button
													class="btn btn-info" type="button">View in DSpace</button></a>
										</g:if> 
										<g:else>
											<g:form url="[controller: 'sword', action:'syncItem']">
												<g:hiddenField name="item"
													value="${equipmentItemInstance.id}" />
												<g:submitButton name="button" value="Send to DSpace"
													class="btn btn-success" />
											</g:form>
										</g:else></td>

									<td></td>

								</tr>
							</g:each>
						</tbody>
					</table>
					<div class="pagination">
						<g:paginate total="${equipmentItemInstanceTotal}" />
					</div>
				</div>

			</div>
		</div>
</body>
</html>
