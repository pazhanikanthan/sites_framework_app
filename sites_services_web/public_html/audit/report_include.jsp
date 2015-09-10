<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.oracle.com/webcenter/sites/audit" prefix="audit"%>
    <div class="row">
        <div class="col-md-12">
        <audit:report var="auditRecords"/>
        <c:choose>
            <c:when test="${not empty auditRecords.records}">
                <div class="table-responsive">
                  <table class="table table-bordered">
                    <tr class="info">
                       <td>Export as: 
                            <c:forEach items="${auditRecords.exportOptions}" var="exportOption" varStatus="loopIndex">
                                <a class="${(auditRecords.totalRecordCount == 0) ? 'inactive' : ''}" href="${exportOption.href}"><img alt="${exportOption.text}" src="img/${exportOption.text}.png"></a> ${!loopIndex.last ? ' | ' : ''}
                            </c:forEach>
                       </td>
                       <td class="text-right">
                           Record(s) per page:
                           <c:forEach items="${auditRecords.recordsPerPage}" var="recordPerPage" varStatus="loopIndex">
                            <a class="${(auditRecords.totalRecordCount == 0) ? 'inactive' : recordPerPage.styleClass}" href="${recordPerPage.href}">${recordPerPage.text}</a> ${!loopIndex.last ? ' | ' : ''}
                           </c:forEach>
                       </td>
                    </tr>
                    <tr class="success">
                       <td>Total Record(s): ${auditRecords.totalRecordCount}</td>
                       <td class="text-right">
                            <c:if test="${not empty auditRecords.records}">
                            Go to page: 
                            <c:forEach items="${auditRecords.pages}" var="page" varStatus="loopIndex">
                                <a class="${page.styleClass}" href="${page.href}">${page.text}</a> ${!loopIndex.last ? ' | ' : ''}
                            </c:forEach>
                            </c:if>
                       </td>
                    </tr>
                  </table>
                  <table class="table table-bordered table-hover">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Type</th>
                        <th>User Id</th>
                        <th class="text-center">Operation</th>
                        <th>Destination</th>
                        <th>Timestamp</th>
                      </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty auditRecords.records}">
                            <tr>
                                <td colspan="7">No records found.</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${auditRecords.records}" var="auditRecord" varStatus="loopIndex">
                                <c:choose>
                                    <c:when test="${auditRecord.operation == 'APPROVED'}">
                                        <c:set var="operationStyleClass" value="success"/>
                                    </c:when>
                                    <c:when test="${auditRecord.operation == 'UNAPPROVED'}">
                                        <c:set var="operationStyleClass" value="danger"/>
                                    </c:when>
                                    <c:when test="${auditRecord.operation == 'UPDATE'}">
                                        <c:set var="operationStyleClass" value="warning"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="operationStyleClass" value="default"/>
                                    </c:otherwise>
                                </c:choose>
                              <tr>
                                <td scope="row">${auditRecord.id}</td>
                                <td>${auditRecord.sourceAsset.name}</td>
                                <td>${auditRecord.sourceAsset.type}</td>
                                <td>${auditRecord.userId}</td>
                                <td class="text-center"><span class="label label-${operationStyleClass}">${auditRecord.operation}</span></td>
                                <td>${auditRecord.targetAsset.id > 0 ? auditRecord.targetAsset.name : ''}</td>
                                <td>${auditRecord.timestamp}</td>
                              </tr>
                              <c:remove var="operationStyleClass"/>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>

                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger" role="alert">
                      No audit records found.
                </div>
            </c:otherwise>
        </c:choose>
        </div>
    </div>
