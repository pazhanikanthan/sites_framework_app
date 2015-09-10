<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://www.oracle.com/webcenter/sites/audit" prefix="audit"%>
<html>
    <head>
        <title>Audit Report</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <script src="../js/jquery-1.11.1.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <link href="../css/bootstrap.min.css" rel="stylesheet"/>
        <link href="../css/application.css" rel="stylesheet"/>
        <link href="../css/datepicker.css" rel="stylesheet"/>
        <link rel="shortcut icon" type="image/png" href="../favicon.ico"/>
    </head>
    <body>
        <audit:getUniqueValues columnName="ASSET_TYPE" var="TYPE_LIST"/>
        <audit:getUniqueValues columnName="TARGET_ASSET_NAME" var="DEST_LIST"/>
        <form>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="type">Asset Type</label>
                        <select id="type" name="type" class="form-control">
                            <option value="">Select Asset Type</option>
                            <c:forEach items="${TYPE_LIST}" var="type" varStatus="loopIndex">
                            <option value="${type}" ${(param.type == type) ? 'SELECTED' : ''}>${type}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="name">Name</label>
                        <input type="text" class="form-control" id="name" name="name" placeholder="Name" value="${param.name}">
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="form-group">
                        <label for="type">Destination</label>
                        <select id="destination" name="destination" class="form-control">
                            <option value="">Select Destination</option>
                            <c:forEach items="${DEST_LIST}" var="destination" varStatus="loopIndex">
                            <option value="${destination}" ${(param.destination == destination) ? 'SELECTED' : ''}>${destination}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="startDate">Start Date</label>
                            <input class="form-control" id="startDate" name="startDate" size="16" type="text" data-date-format="dd/mm/yyyy" value="${param.startDate}" readonly>
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="endDate">End Date</label>
                            <input class="form-control" id="endDate" name="endDate" size="16" type="text" data-date-format="dd/mm/yyyy" value="${param.endDate}" readonly>
                            <span class="add-on"><i class="icon-th"></i></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <button type="submit" id="btnSearch" class="btn btn-success">Search</button>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <div class="clearfix">&nbsp;</div>
                </div>
            </div>
            <%@include file="report_include.jsp" %>
        </div>
        </form>
        

        <script>
            $(function(){
                $('#startDate').datepicker();
                $('#endDate').datepicker();
                $("#btnSearch").click( function() {
                    var startDateStr = $('#startDate').val();
                    var endDateStr   = $('#endDate').val();
                    if (startDateStr != null && endDateStr != null) {
                        var startDate = new Date(startDateStr);
                        var endDate   = new Date(endDateStr);
                        if (endDate < startDate){
                            alert ('End Date less than Start Date.');
                            return;
                        }
                    }
                });
            });
        </script>
    </body>
</html>