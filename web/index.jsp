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
    <div class="userbox">
        <%
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
            if (user != null) {
        %>
        Logged in, <%= user.getNickname()%>
        <a href="<%= userService.createLogoutURL(request.getRequestURI()) %>" class="buttonLink">logout</a>
        <%
        } else {
        %>
        <a href="<%= userService.createLoginURL(request.getRequestURI())%>" class="buttonLink">login</a>
        <%
            }
        %>
    </div>
    <form action="/FighterSearchServlet" method="post" id="searchForm">
        <input type="hidden" name="mode" value=""/>
    <div id="searchBar">
        <input type="text" name="search" value="Search SCA name or modern name"/>
        <input type="submit" value="Search" onClick="setMode(this.form, 'search'); "/>
        <input type="submit" value="Add" onClick="setMode(this.form, 'add');" />
    </div>
    </form>
    
    <div class="dataBox">
        <div class="dataHeader"></div>
        <div class="dataBody"></div>
    </div>
         
        

</body>
</html>
