<%-- 
    Document   : fighter
    Created on : May 8, 2011, 12:23:19 PM
    Author     : rik
--%>
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
                SCA Name: <cmp:input type="text" name="scaName" mode="<%= mode%>" value="<%= fighter.getScaName()%>"/>
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
                        Address: <%--- cmp:input type="text" name="address"
                                            mode="<%=mode%>" value="<%=fighter.getAddress()%>" / --%><br>
                        
                        SCA Membership: <cmp:input type="text" name="scaMemberNo" size="20" 
                        mode="<%= mode%>" value="<%= fighter.getScaMemberNo()%>" /><br>
                        
                        Group: <br>
                        <% String minorValue = MarshalUtils.isMinor(fighter) ? "true" : "false"; %>
                        Minor: <cmp:input type="viewonly" mode="<%=mode%>" value="<%= minorValue %>" /><br>
                        DOB: <%-- cmp:input type="text" name="dateOfBirth" 
                                        mode="<%=mode%>" value="<%=fighter.getDateOfBirth() %>" / --%><br>
                        Phone Number: <br>
                        Email Address: <%-- cmp:input type="text" name="emailAddress" 
                                        mode="<%=mode%>" value="<%=fighter.getEmail() %>" / --%><br>

                    </div>
                    <div><cmp:input type="submit" value="Add Fighter" mode="<%= mode%>" /></div>
                </div>

            </div>
        </form>
    </div>
</body>
</html>
