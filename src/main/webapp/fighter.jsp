<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.sca.calontir.cmpe.common.UserRoles"%>
<%@page import="org.sca.calontir.cmpe.user.SecurityFactory"%>
<%@page import="org.sca.calontir.cmpe.user.Security"%>
<!DOCTYPE html>
<%-- 
    Document   : fighter
    Created on : May 8, 2011, 12:23:19 PM
    Author     : rik
--%>
<%@page import="org.sca.calontir.cmpe.dto.FighterListItem"%>
<%@page import="org.sca.calontir.cmpe.common.FighterStatus"%>
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
        <script type="text/javascript" src="js/jquery-ui-1.8.14.custom.min.js"></script>
        <%
            FighterDAO fighterDao = new FighterDAO();
            List<FighterListItem> fighters = fighterDao.getFighterListItems();
        %>
        <script type="text/javascript">
            $(function(){
                // Datepicker
                $('#dateOfBirth').datepicker({
                    changeMonth: true,
			changeYear: true
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
                
                    $( "#dialog-confirm" ).dialog({
                        resizable: false,
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        modal: false,
                        buttons: {
                            "Delete Fighter": function() {
                                var form = document.getElementById("fighterInfoForm");
                                form.mode.value = "deleteFighter";
                                form.submit();
                                //$( this ).dialog( "close" );
                            },
                            Cancel: function() {
                                $( this ).dialog( "close" );
                            }
                        }
                    });
                });
        </script>

    </head>
    <body>
        <div id="dialog-confirm" title="Delete this fighter?">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>This fighter will be permanently and cannot be undone . Are you sure?</p>
        </div>

        <jsp:useBean id="fighter" scope="request" class="org.sca.calontir.cmpe.dto.Fighter" /> 

        <% String mode = (String) request.getAttribute("mode");
            if (mode == null) {
                mode = "view";
            }
            pageContext.setAttribute("mode", mode);

            AuthTypeDAO atDao = new AuthTypeDAO();
            List<AuthType> authTypes = atDao.getAuthType();

            Security security = SecurityFactory.getSecurity();
        %>

        <%@include file="WEB-INF/jspf/userbox.jspf" %>
        <%@include file="WEB-INF/jspf/messagebox.jspf" %>
        <%@include file="WEB-INF/jspf/searchbox.jspf" %>

        <form action="/FighterServlet" method="post" name="fighterInfoForm" id="fighterInfoForm">
            <input type="hidden" name="mode" value="<%= mode%>"/>
            <% Long fighterId = fighter.getFighterId() == null ? null : fighter.getFighterId();%>
            <input type="hidden" name="fighterId" value="<%=fighterId%>"/>
            <div class="figherIdBox">
                SCA Name: <cmp:input type="text" name="scaName" id="scaName" mode="<%= mode%>" value="<%= fighter.getScaName()%>" editMode="editFighterInfo"/>
                <% if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL) && fighter.getFighterId() != null && fighter.getFighterId() > 0) {%>
                <cmp:deleteFighterButton mode="<%= mode%>" />
                <% } %>
                <cmp:printButton mode="<%= mode%>" fighterId="<%= fighterId %>" />
            </div>
            <div class="dataBox">
                <div class="dataHeader">Authorizations <cmp:editButton mode="<%= mode%>" target="Authorizations" form="document.fighterInfoForm" fighterId="<%= fighterId %>" /></div>
                <div class="dataBody">
                    <cmp:auths mode="<%= mode%>" authTypes="<%=authTypes%>" authorizations="<%= fighter.getAuthorization()%>"  editMode="editAuthorizations"/>
                </div>
            </div>
            <% if (userService.isUserLoggedIn() && (userService.isUserAdmin() || security.canEditFighter(fighterId))) {%>
            <div class="dataBox" name="fighterInfoBox">
                <div class="dataHeader">Fighter Info 
                    <cmp:editButton mode="<%=mode%>" target="FighterInfo" form="document.fighterInfoForm" fighterId="<%= fighterId %>" />
                </div>
                <div class="dataBody">
                    <% if (security.canEditFighter(fighterId)) {%>
                    <div id="fighterInfo">
                        <table class="wide-table">
                            <tr>
                                <td class="label">Modern Name:</td>
                                <td class="data"><cmp:input type="text" name="modernName"
                                    mode ="<%=mode%>" value="<%= fighter.getModernName()%>"  editMode="editFighterInfo" /></td>
                                <% if (security.isRoleOrGreater(UserRoles.USER)) {%>
                                <td class="rightCol">
                                <cmp:fighterStatusTag mode="<%= mode%>" status="<%= fighter.getStatus()%>" editMode="editFighterInfo" /> 
                                </td>
                                <% } %>
                            </tr>
                            <tr>
                                <td class="label">Address:</td>
                                <td class="data"><cmp:address mode="<%=mode%>" addresses="<%=fighter.getAddress()%>" editMode="editFighterInfo" /></td>
                                <td class="rightCol" style="vertical-align: top;">
                                    <cmp:treatyTag mode="<%= mode %>" treaty="<%= fighter.getTreaty() %>" editMode="editFighterInfo" /></td>
                                </td>
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
                    <%}%>
                    <% if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL) || userService.isUserAdmin()) {%>
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
                    <% }%>
                    <% if (mode.equalsIgnoreCase("Add")) {%>
                    <div><cmp:input type="submit" value="Add Fighter" mode="<%= mode%>" /></div>
                    <%}%>
                </div>
            </div>
            <% if (security.isRoleOrGreater(UserRoles.CARD_MARSHAL)) {%>
            <div class="dataBoxShort" name="fighterInfoBox">
                <div class="dataHeader">Notes</div>
                <div class="dataBody">
                    <% String note = fighter.getNote() == null ? null : fighter.getNote().getBody(); %>
                    <cmp:textAreaTag name="notes" mode="<%= mode%>" editMode="editFighterInfo" 
                        value="<%= note %>" />
                </div>
            </div>
            <%}%>
            <% }%>
        </form>
    </body>
</html>
