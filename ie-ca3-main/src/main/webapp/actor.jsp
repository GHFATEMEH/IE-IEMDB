<%@page import="IEMDB.IEMDB"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Actor</title>
    <style>
        li {
        	padding: 5px
        }
        table{
            width: 40%;
            text-align: center;
        }
    </style>
</head>

<%
String loggedInUser = IEMDB.getInstance().getLoggedInEmail();
%>

<body>
    <a href="/">Home</a>
     <p id="email">email: <%=loggedInUser%></p>
    <ul>
        <%
            out.println(request.getAttribute("actor_info"));
        %>
    </ul>
    <table>
        <tr>
            <th>Movie</th>
            <th>imdb Rate</th>
            <th>rating</th>
            <th>page</th>
        </tr>
        <%
            if (request.getAttribute("movies_info") != null)
                out.println(request.getAttribute("movies_info"));
        %>
    </table>
</body>
</html>