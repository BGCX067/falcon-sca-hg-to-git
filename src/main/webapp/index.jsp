<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>Calontir Marshals UI</title>
        <script language="javascript" src="fighter/fighter.nocache.js"></script>
        <link rel="stylesheet" href="css/cmp_001.css" type="text/css" media="all" />
        <link type="text/css" href="css/calonbar2.css" rel="stylesheet" />
        <link rel="SHORTCUT ICON" href="images/Marshal.ico"/>
    </head>
    <body>
<<<<<<< local
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
                            width="620" height="820" frameborder="0" marginheight="0" marginwidth="0">Loading...</iframe>
                </div>
            </div>
=======
        <div id="Loading-Message">
            <span>Loading, please wait...</span>
>>>>>>> other
        </div>
    </body>
</html>
