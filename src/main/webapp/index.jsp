
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

<%
    String cookieName = "calonbar";
    Cookie cookies[] = request.getCookies();
    Cookie calonbarCookie = null;
    if (cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(cookieName)) {
                calonbarCookie = cookies[i];
                break;
            }
        }
    }
    String cbar = (calonbarCookie == null ? "0" : calonbarCookie.getValue());
    if (cbar == null) {
        cbar = "0";
    }
    cbar = "1";
%>
<html>
    <head>
        <title>Calontir Marshals UI</title>
        <link rel="stylesheet" href="default.css" type="text/css" media="all" />
        <% if (cbar.equals("0")) {%>
        <link type="text/css" href="css/calonbar.css" rel="stylesheet" />
        <% } else {%>
        <link type="text/css" href="css/calonbar2.css" rel="stylesheet" />
        <% }%>
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
            <% for (int i = 0; i < fighters.size(); ++i) {
                    String scaName = fighters.get(i).getScaName();
                    scaName = scaName.replace("\"", "\\\"");
            %>
                    availableTags[<%= i%>] = "<%=scaName%>";
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
        <%
            UserService userService = UserServiceFactory.getUserService();
            User user = userService.getCurrentUser();
        %>
        <% if (cbar.equals("0")) {%>
        <%@include file="WEB-INF/jspf/calonbar.jspf" %>
        <% } else {%>
        <%@include file="WEB-INF/jspf/calonbar2.jspf" %>
        <% }%>
        <%@include file="WEB-INF/jspf/messagebox.jspf" %>
        <%@include file="WEB-INF/jspf/searchbox.jspf" %>

        <div class="dataBox">
            <div class="dataBody">
                <p>Not registered? Sign up now!</p>

                <p>Registering with the Calontir Marshalallate system allows you 
                    review your own authorizations, update your contact information, 
                    and print your own fighter card at home.</p>

                <div style="text-align: center;">
                    <iframe src="https://docs.google.com/spreadsheet/embeddedform?formkey=dGNDV2NYdGUtZk1aZXN6MURkaWlFNlE6MQ" 
                            width="760" height="853" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>
                </div>
            </div>
        </div>



    </body>
</html>
