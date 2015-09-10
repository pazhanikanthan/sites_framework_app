<!DOCTYPE html>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
        <title>Oracle WebCenter Sites Services API Documentation</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <script src="js/jquery-1.11.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <link href="css/bootstrap.min.css" rel="stylesheet"/>
        <link href="css/application.css" rel="stylesheet"/>
        <link rel="shortcut icon" type="image/png" href="favicon.ico"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div id="content">
                        <blockquote>
                            <p style="text-transform: uppercase;">
                                <span id="apiName">Oracle WebCenter Sites Services Application</span>
                            </p>
                            <small>
                                <span id="apiDescription">API REST services</span>
                            </small>
                        </blockquote>
                         
                        <c:set var="apis"
                               value="<%=oracle.webcenter.sites.framework.factories.APIFactory.getInstance ().getApis()%>"/>
                        <div id="accordion" class="panel-group">
                            <c:forEach var="api" items="${apis}" varStatus="loopIndex">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <span class="label pull-right ${api.method}">${api.method}</span>
                                            <a data-parent="#accordion" data-toggle="collapse" rel="method" href="#content${loopIndex.index}" class="collapsed">${api.path}</a>
                                        </h4>
                                    </div>
                                </div>
                                <div role="tabpanel" class="panel-collapse collapse"
                                     id="content${loopIndex.index}" style="height: 0px;">
                                    <div class="panel-body">
                                        <table style="table-layout: fixed;"
                                               class="table table-condensed table-bordered">
                                            <tbody>
                                                <tr>
                                                    <th style="width:18%;">Path</th>
                                                    <td>
                                                        <code><a href="${pageContext.request.contextPath}/rest${api.path}" target="_blank">${pageContext.request.contextPath}/rest${api.path}</a></code>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>Produces</th>
                                                    <td>
                                                        <code>${api.produces}</code>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <th>Response status code</th>
                                                    <td>
                                                        <code>${api.responseStatus}</code>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>