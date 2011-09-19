<%@page import="org.sca.calontir.cmpe.dto.FighterListItem"%>
<%@page import="org.sca.calontir.cmpe.utils.MarshalUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<!DOCTYPE html>
<%-- 
    Document   : index
    Created on : Sep 3, 2011
    Author     : rik
--%>

<%@page import="org.sca.calontir.cmpe.dto.Fighter"%>
<%@page import="java.util.List"%>
<%@page import="org.sca.calontir.cmpe.db.FighterDAO"%>
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
        <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.13.custom.css" rel="stylesheet" />	
        <script type="text/javascript" src="js/jquery-1.5.1.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
        <%
            FighterDAO fighterDao = new FighterDAO();
            List<Fighter> fighters = fighterDao.getFighters();
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
        <%@include file="WEB-INF/jspf/userbox.jspf" %>
        <%@include file="WEB-INF/jspf/messagebox.jspf" %>
        <%@include file="WEB-INF/jspf/searchbox.jspf" %>

        <% List<FighterListItem> fighterList = (List<FighterListItem>) request.getAttribute("fighters");

            Security security = SecurityFactory.getSecurity();
        %>

        <div class="list">
            <div class="listRow header">
                <span class="rowElement" style="width: 20%;">SCA Name</span>
                <span class="rowElement" style="width: 25%;">Authorizations</span>
                <% if (security.isRoleOrGreater(UserRoles.DEPUTY_EARL_MARSHAL)) {%>
                <span class="rowElement" style="width: 25%;">Group </span>
                <% }%>
                <% if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {%>
                <span class="rowElement" style="width: 10%;">Minor</span>   
                <% }%>
            </div>
            <% for (FighterListItem f : fighterList) {%>
            <div class="listRow">
                <% if (f != null) {%>
                <a href="/FighterSearchServlet?mode=lookup&fid=<%=f.getFighterId()%>" >
                    <span class="rowElement" style="width: 20%;"><%= f.getScaName()%></span>
                    <span class="rowElement" style="width: 25%;"><%= f.getAuthorizations()%></span>
                    <% if (security.isRoleOrGreater(UserRoles.DEPUTY_EARL_MARSHAL)) {%>
                    <span class="rowElement" style="width: 25%;"><%= f.getGroup()%> </span>
                    <% }%>
                    <% if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {%>
                    <span class="rowElement" style="width: 10%;"><%= f.isMinor() %></span>
                    <% }%>
                </a>
                <% }%>
            </div>
            <% }%>
            <div>&nbsp;</div>

        </div>

    </body>
</html>
