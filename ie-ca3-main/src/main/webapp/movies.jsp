<%@page import="IEMDB.IEMDB"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Movies</title>
    <style>
      li, td, th {
        padding: 5px;
      }
    </style>
</head>

<%
String loggedInUser = IEMDB.getInstance().getLoggedInEmail();
%>

<%
String movies_info = IEMDB.getInstance().getAllMovies();
%>

<body>
    <a href="/">Home</a>
    <p id="email">email: <%=loggedInUser%></p>
    <br><br>
    <form action="/movies" method="POST">
        <label>Search:</label>
        <input type="text" name="search" value="">
        <button type="submit" name="action" value="search">Search</button>
        <button type="submit" name="action" value="clear">Clear Search</button>
    </form>
    <br><br>
    <form action="/movies" method="POST">
        <label>Sort By:</label>
        <button type="submit" name="action" value="sort_by_imdb">imdb Rate</button>
        <button type="submit" name="action" value="sort_by_date">releaseDate</button>
    </form>
    <br>
    <table>
        <tr>
            <th>name</th>
            <th>summary</th>
            <th>releaseDate</th>
            <th>director</th>
            <th>writers</th>
            <th>genres</th>
            <th>cast</th>
            <th>imdb Rate</th>
            <th>rating</th>
            <th>duration</th>
            <th>ageLimit</th>
            <th>Links</th>
        </tr>

        <%if(request.getAttribute("movies_info") != null) {
            out.println(request.getAttribute("movies_info"));
        }
        else {
            out.println(movies_info);
        }%>
    </table>
</body>
</html>