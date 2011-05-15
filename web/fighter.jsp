<%-- 
    Document   : fighter
    Created on : May 8, 2011, 12:23:19 PM
    Author     : rik
--%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/cmp.tld" prefix="cmp" %>
<%@page import="org.sca.calontir.cmpe.data.Fighter"%>
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
        %>

        <%@include file="WEB-INF/jspf/userbox.jspf" %>
        <%@include file="WEB-INF/jspf/searchbox.jspf" %>

        <div class="figherIdBox">
            SCA Name: <cmp:input name="scaName" mode="<%= mode%>" value="<%= fighter.getScaName()%>"/>
        </div>
        <c:choose>
            <c:when test="${mode eq 'add'}">
                <div class="dataBox">
                    <form action="/FighterServlet" method="post">
                        <div class="dataHeader">Fighter Info</div>
                        <div class="dataBody">
                            <div>SCA Name: <input type=text name=scaName SIZE=60><br>
                                Membership Number: <input type=text name="scaMemberNo" size=20><br>
                            </div>
                            <div><input type="submit" value="Add Fighter" /></div>
                        </div>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="dataBox">
                    <div class="dataHeader">Fighter Info</div>
                    <div class="dataBody">
                        <div>SCA Name: <%= fighter.getScaName()%><br>
                            Membership Number: <%= fighter.getScaMemberNo()%><br>
                        </div>

                    </div>
                </c:otherwise>
            </c:choose>

        </div>
    </body>
</html>
