<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>POST Audit Sample</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <script src="../js/jquery-1.11.1.min.js"></script>
        <script src="../js/bootstrap.min.js"></script>
        <link href="../css/bootstrap.min.css" rel="stylesheet"/>
        <link href="../css/application.css" rel="stylesheet"/>
        
        <script>
            var auditRecord = {  
                "userId" : 'John',
                "operation" : 'ADD',
                "sourceAsset" : {
                    "id" : '<%=request.getParameter ("id")%>',
                    "type" : '<%=request.getParameter ("type")%>'
                }
                <%
                    if (request.getParameter ("tid") != null) {
                %>
                , "targetAsset" : {
                    "id" : '<%=request.getParameter ("tid")%>'
                }
                <%
                    }
                %>
            };
        
            //Call jQuery ajax
            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: '${pageContext.request.contextPath}/services/rest/audit/create',
                data: JSON.stringify(auditRecord),
                dataType: "json",
                success: function (msg) {
                    alert('Success');
                },
                error: function (xhr, ajaxOptions, thrownError) {
                  alert(xhr.status);
                  alert(thrownError);
                }
            });
        
        </script>
    </head>
    <body></body>
</html>