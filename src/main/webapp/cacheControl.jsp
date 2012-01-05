
<!DOCTYPE html>
<%-- 
    Document   : CacheControl
    Created on : Dec 11, 2011
    Author     : rik
--%>

<%@page import="org.sca.calontir.cmpe.dto.FighterListItem"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.sca.calontir.cmpe.db.FighterDAO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
    <head>
        <title>Calontir Marshals UI</title>
        <link rel="stylesheet" href="default.css" type="text/css" media="all" />
        <link rel="SHORTCUT ICON" href="images/Marshal.ico">
        <script type="text/javascript" src="jscmpe-1.0.0.js"></script>
        <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.13.custom.css" rel="stylesheet" />	
        <script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/userbox.jspf" %>
        <%@include file="WEB-INF/jspf/messagebox.jspf" %>

        <div class="dataBox">
            <% if (userService.isUserLoggedIn() && userService.isUserAdmin()) {%>
            <a href="/AdminServlet?clearcache=AuthType" class="buttonLink" title="Clear Cache for AuthType">AuthType</a>
            <% } else {%>
            Must be user admin.
            <% }%>
        </div>
    </body>
</html>
