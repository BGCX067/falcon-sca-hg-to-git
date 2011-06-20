<!DOCTYPE html>
<%-- 
    Document   : fighter
    Created on : May 8, 2011, 12:23:19 PM
    Author     : rik
--%>
<%@page import="org.sca.calontir.cmpe.dto.Fighter"%>
<%@page import="org.sca.calontir.cmpe.db.FighterDAO"%>
<%@page import="org.sca.calontir.cmpe.utils.MarshalUtils"%>
<%@page import="org.sca.calontir.cmpe.dto.AuthType"%>
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
        <jsp:useBean id="fighter" scope="request" class="org.sca.calontir.cmpe.dto.Fighter" /> 

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

        <form action="/FighterServlet" method="post" name="fighterInfoForm">
            <input type="hidden" name="mode" value="<%= mode%>"/>
            <% Long fighterId = fighter.getFighterId() == null ? null : fighter.getFighterId();%>
            <input type="hidden" name="fighterId" value="<%=fighterId%>"/>
            <div class="figherIdBox">
                SCA Name: <cmp:input type="text" name="scaName" id="scaName" mode="<%= mode%>" value="<%= fighter.getScaName()%>" editMode="editFighterInfo"/>
            </div>
            <div class="dataBox">
                <div class="dataHeader">Authorizations <cmp:editButton mode="<%=mode%>" target="Authorizations" form="document.fighterInfoForm" /></div>
                <div class="dataBody">
                    <cmp:auths mode="<%= mode%>" authTypes="<%=authTypes%>" authorizations="<%= fighter.getAuthorization()%>"  editMode="editAuthorizations"/>
                </div>
            </div>
            <% if (userService.isUserLoggedIn()) {%>
            <div class="dataBox" name="fighterInfoBox">
                <div class="dataHeader">Fighter Info <cmp:editButton mode="<%=mode%>" target="FighterInfo" form="document.fighterInfoForm" /></div>
                <div class="dataBody">
                    <div id="fighterInfo">
                        <table>
                            <tr>
                                <td class="label">Modern Name:</td>
                                <td class="data"><cmp:input type="text" name="modernName"
                                    mode ="<%=mode%>" value="<%= fighter.getModernName()%>" /></td>
                            </tr>
                            <tr>
                                <td class="label">Address:</td>
                                <td class="data"><cmp:address mode="<%=mode%>" addresses="<%=fighter.getAddress()%>" editMode="editFighterInfo" /></td>
                            </tr>
                            <tr>
                                <td class="label">SCA Membership:</td>
                                <td class="data"><cmp:input type="text" name="scaMemberNo" size="20" 
                                    mode="<%= mode%>" value="<%= fighter.getScaMemberNo()%>" editMode="editFighterInfo" /></td>
                            </tr>
                            <tr>
                                <% String groupName = fighter.getScaGroup() == null ? null : fighter.getScaGroup().getGroupName();%>
                                <td class="label">Group:</td>
                                <td class="data"><cmp:groupTag mode="<%=mode%>" groupName="<%= groupName%>" editMode="editFighterInfo" /></td>
                            </tr>
                            <tr>
                                <% String minorValue = MarshalUtils.isMinor(fighter) ? "true" : "false";%>
                                <td class="label">Minor:</td>
                                <td class="data"><cmp:input type="viewonly" mode="<%=mode%>" value="<%= minorValue%>" editMode="editFighterInfo"/></td>
                            </tr>
                            <tr><td class="label">DOB:</td>
                                <td class="data"><cmp:input type="text" name="dateOfBirth"  id="dateOfBirth"
                                    mode="<%=mode%>" value="<%=fighter.getDateOfBirth()%>" editMode="editFighterInfo" /></td>
                            </tr>
                            <tr><td class="label">Phone Number:</td>
                                <td class="data"><cmp:phone mode="<%=mode%>" numbers="<%=fighter.getPhone()%>" editMode="editFighterInfo" /></td>
                            </tr>
                            <tr><td class="label">Email Address:</td>
                                <td class="data"><cmp:email mode="<%=mode%>" emails="<%=fighter.getEmail()%>" editMode="editFighterInfo"  /></td>
                        </table>
                    </div>
                        <% if (userService.isUserAdmin()) {%>
                    <div id="adminInfo">
                        <table>
                            <tr>
                                <td class="label">Google ID:</td>
                                <td class="data"><cmp:input type="text" name="googleId" id="googleId" mode="<%=mode%>" value="<%=fighter.getGoogleId()%>" editMode="editFighterInfo"  /></td>
                            </tr>
                            <tr>
                                <td class="label">User Role:</td>
                                <td class="data">
                                    <cmp:rolesTag mode="<%=mode%>" userRole="<%=fighter.getRole()%>" editMode="editFighterInfo" />
                                </td>
                            </tr>
                        </table>
                    </div>

                        <%}%>
                    <div><cmp:input type="submit" value="Add Fighter" mode="<%= mode%>" /></div>
                </div>
            </div>
            <% }%>
        </form>
    </body>
</html>
