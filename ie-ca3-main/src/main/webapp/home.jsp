<%@page import="IEMDB.IEMDB"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>

<%
String loggedInUser = IEMDB.getInstance().getLoggedInEmail();
%>

<body>
    <ul>
        <li id="email">email: <%=loggedInUser%></li>
        <li>
            <a href="/movies">Movies</a>
        </li>
        <li>
            <a href="/watchlist">Watch List</a>
        </li>
        <li>
            <a href="/logout">Log Out</a>
        </li>
    </ul>
</body>
</html>