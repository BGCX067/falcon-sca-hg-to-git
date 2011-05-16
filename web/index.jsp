<%-- 
    Document   : index
    Created on : Feb 28, 2011, 9:13:51 PM
    Author     : rik
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<html>
    <head>
        <title>Calontir Marshals UI</title>
        <style type="text/css" media="all">@import "default.css";</style>
        <link rel="SHORTCUT ICON" href="images/Marshal.ico">
        <script type="text/javascript" src="jscmpe-1.0.0.js"></script>
    </head>
    <body>
    <%@include file="WEB-INF/jspf/userbox.jspf" %>
    <%@include file="WEB-INF/jspf/searchbox.jspf" %>
    
 
         
        

</body>
</html>
