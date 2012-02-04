
<!DOCTYPE html>
<%-- 
    Document   : index
    Created on : Feb 28, 2011, 9:13:51 PM
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
        <link rel="stylesheet" href="css/calonbar.css" type="text/css" media="all" />
        <link rel="SHORTCUT ICON" href="images/Marshal.ico">
        <script type="text/javascript" src="jscmpe-1.0.0.js"></script>
        <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.13.custom.css" rel="stylesheet" />	
        <script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
        <%
            FighterDAO fighterDao = new FighterDAO();
            List<FighterListItem> fighters = fighterDao.getFighterListItems();
        %>
        <script type="text/javascript">
            $(function(){
                // Datepicker
                $('#dateOfBirth').datepicker({
                    inline: true
                });
                var availableTags = [];
            <% for (int i = 0; i < fighters.size(); ++i) {%>
                    availableTags[<%= i%>] = "<%= fighters.get(i).getScaName()%>";
            <% }%>
                    $( "#search" ).autocomplete({
                        source: availableTags,
                        mustMatch:true,
                        autoFill:true,           
                        focus: function(event, ui) { this.value = ui.item.value }     
                    });
                });
        </script>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/calonbar.jspf" %>
        <%@include file="WEB-INF/jspf/messagebox.jspf" %>
        <%@include file="WEB-INF/jspf/searchbox.jspf" %>





    </body>
</html>
