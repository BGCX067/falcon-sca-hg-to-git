<%-- 
    Document   : scaGroup
    Created on : May 7, 2011, 4:20:59 PM
    Author     : rik
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SCA Group</title>
    </head>
    <body>
        <form action="/ScaGroupServlet" method="post">
            <div>Group Name: <input type=text name=groupName SIZE=4><br>
                Location: <input type=text name="location" size=20><br>
            </div>
            <div><input type="submit" value="Add Group" /></div>
        </form>
    </body>
</html>
