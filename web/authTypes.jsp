<%-- 
    Document   : authTypes
    Created on : May 7, 2011, 2:09:19 PM
    Author     : rik
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Authorization Types</title>
    </head>
    <body>
        <form action="/AuthTypesServlet" method="post">
            <div>Authorization Code: <input type=text name=code SIZE=4><br>
                Description: <input type=text name="description" size=20><br>
            </div>
            <div><input type="submit" value="Add Authorization Type" /></div>
        </form>
    </body>
</html>
