<%-- 
    Document   : fighter
    Created on : May 8, 2011, 12:23:19 PM
    Author     : rik
--%>
<%@page import="org.sca.calontir.cmpe.data.Fighter"%>
<%@page import="org.sca.calontir.cmpe.db.FighterDAO"%>
<%@page import="org.sca.calontir.cmpe.utils.MarshalUtils"%>
<%@page import="org.sca.calontir.cmpe.data.AuthType"%>
<%@page import="java.util.List"%>
<%@page import="org.sca.calontir.cmpe.db.AuthTypeDAO"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/cmp.tld" prefix="cmp" %>
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
        <script type="text/javascript" src="js/jquery-ui-1.8.13.custom.min.js"></script>
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
            source: availableTags
        });
    });
        </script>

    </head>
    <body>
        <jsp:useBean id="fighter" scope="request" class="org.sca.calontir.cmpe.data.Fighter" /> 

        <% String mode = (String) request.getAttribute("mode");
            if (mode == null) {
                mode = "view";
            }
            pageContext.setAttribute("mode", mode);

            AuthTypeDAO atDao = new AuthTypeDAO();
            List<AuthType> authTypes = atDao.getAuthType();
        %>

        <%@include file="WEB-INF/jspf/userbox.jspf" %>
        <%@include file="WEB-INF/jspf/searchbox.jspf" %>

        <form action="/FighterServlet" method="post">
            <div class="figherIdBox">
                SCA Name: <cmp:input type="text" name="scaName" id="scaName" mode="<%= mode%>" value="<%= fighter.getScaName()%>"/>
            </div>
            <div class="dataBox">
                <div class="dataHeader">Authorizations</div>
                <div class="dataBody">
                    <cmp:auths mode="<%= mode%>" authTypes="<%=authTypes%>" authorizations="<%= fighter.getAuthorization()%>"/>
                </div>
            </div>
            <div class="dataBox">
                <div class="dataHeader">Fighter Info</div>
                <div class="dataBody">
                    <div>
                        Modern Name: <cmp:input type="text" name="modernName"
                        mode ="<%=mode%>" value="<%= fighter.getModernName()%>" /><br>
                        Address: <cmp:address mode="<%=mode%>" addresses="<%=fighter.getAddress()%>" /><br>

                        SCA Membership: <cmp:input type="text" name="scaMemberNo" size="20" 
                        mode="<%= mode%>" value="<%= fighter.getScaMemberNo()%>" /><br>

                        <% Long groupId = fighter.getScaGroup() == null ? null : fighter.getScaGroup().getId();%>
                        Group: <cmp:groupTag mode="<%=mode%>" groupId="<%= groupId%>"/><br>
                        <% String minorValue = MarshalUtils.isMinor(fighter) ? "true" : "false";%>
                        Minor: <cmp:input type="viewonly" mode="<%=mode%>" value="<%= minorValue%>" /><br>
                        DOB: <cmp:input type="text" name="dateOfBirth"  id="dateOfBirth"
                        mode="<%=mode%>" value="<%=fighter.getDateOfBirth()%>" /><br>
                        Phone Number: <cmp:phone mode="<%=mode%>" numbers="<%=fighter.getPhone()%>" /><br>
                        Email Address: <cmp:email mode="<%=mode%>" emails="<%=fighter.getEmail()%>" /><br>

                    </div>
                    <div><cmp:input type="submit" value="Add Fighter" mode="<%= mode%>" /></div>
                </div>

            </div>
        </form>
    </div>
</body>
</html>
