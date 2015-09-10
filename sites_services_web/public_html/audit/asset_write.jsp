<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252" 
import="oracle.webcenter.sites.framework.model.AuditRecord,
        oracle.webcenter.sites.framework.model.Asset,
        oracle.webcenter.sites.framework.context.APIContextAware"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
    <head>
        <title>Asset Write</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <script src="../js/jquery-1.11.1.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <link href="../css/bootstrap.min.css" rel="stylesheet"/>
        <link href="../css/application.css" rel="stylesheet"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                <%
                    for (int i = 0; i < 300; i++) {
                        AuditRecord auditRecord = new AuditRecord ();
                        if ( (i % 2) == 0) {
                            auditRecord.setOperation("UNAPPROVED");
                        }
                        else {
                            auditRecord.setOperation("APPROVED");
                        }
                        auditRecord.setUserId("admin");
                        Asset sourceAsset = new Asset (Long.parseLong (request.getParameter("assetId")), "Page");
                        sourceAsset.setName("Paz");
                        auditRecord.setSourceAsset(sourceAsset);
                        Asset targetAsset = new Asset (1343224039228L, "PubTarget");
                        targetAsset.setName("Delivery");
                        auditRecord.setSourceAsset(sourceAsset);
                        APIContextAware.getAuditService().create(auditRecord);
                        Thread.sleep (1000);
                    }
                %>
                </div>
            </div>
        </div>
    </body>
</html>