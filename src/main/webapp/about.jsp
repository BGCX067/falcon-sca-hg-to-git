<!DOCTYPE html>
<%-- 
    Document   : about
    Created on : Feb 14, 2012, 
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

<%
    String cbar = "1";
%>
<html>
    <head>
        <title>Calontir Marshals UI</title>
        <link rel="stylesheet" href="default.css" type="text/css" media="all" />
        <% if (cbar.equals("0")) {%>
        <link type="text/css" href="css/calonbar.css" rel="stylesheet" />
        <% } else {%>
        <link type="text/css" href="css/calonbar_01.css" rel="stylesheet" />
        <% }%>
        <link rel="SHORTCUT ICON" href="images/Marshal.ico">
        <script type="text/javascript" src="jscmpe-1.0.0.js"></script>
        <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.13.custom.css" rel="stylesheet" />	
        <script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
    </head>
    <body>
        <%
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
        %>
        <% if (cbar.equals("0")) {%>
        <%@include file="WEB-INF/jspf/calonbar.jspf" %>
        <% } else {%>
        <%@include file="WEB-INF/jspf/calonbar2.jspf" %>
        <% }%>

        <div id="about">
            <p>The Calontir Marshallate Modernization project. The focus of this project is to evolve the Calontir’s current fighter card issuance and tracking process from a pen and paper driven process to web driven process with professional hosting.</p>

            <p>This application is brought to you by:
            <ul>
                <li>His Grace Martino Michel Venneri,
                Earl Marshal of Calontir.</li>
                <li>Sir Gustav Jameson,
                Project lead and Mastermind</li>
                <li>Taiji Bataciqan-nu Ko'un Ashir</li>
                <li>Sir Duncan Bruce of Logan</li>
                <li>Sir Hans Krieger</li>
                <li>Her Ladyship Kalisa Martel</li>
                <li>His Lordship Aiden O'Seaghdma</li>
                <li>His Lordship Brendan Mac an tSaoir</li>
            </ul>
            </p>

        </div>


    </body>
</html>
