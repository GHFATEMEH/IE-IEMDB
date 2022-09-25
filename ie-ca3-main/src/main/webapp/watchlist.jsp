<%@page import="IEMDB.IEMDB"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WatchList</title>
    <style>
      li, td, th {
        padding: 5px;
      }
    </style>
</head>

<%
String loggedInUser = IEMDB.getInstance().getLoggedInEmail();
%>

<body>
    <a href="/">Home</a>
     <p id="email">email: <%=loggedInUser%></p>
     </ul>
     <% out.println(request.getAttribute("userInfo"));%>
     </ul>
    <h2>Watch List</h2>
     <table>
             <tr>
                 <th>Movie</th>
                 <th>releaseDate</th>
                 <th>director</th>
                 <th>genres</th>
                 <th>imdb Rate</th>
                 <th>rating</th>
                 <th>duration</th>
                 <th></th>
                 <th></th>
             </tr>
            <% out.println(request.getAttribute("movieInfo")); %>
     </table>
     <h2>Recommendation List</h2>
      <table>
      <tr>
          <th>Movie</th>
          <th>imdb Rate</th>
          <th></th>
      </tr>
            <% out.println(request.getAttribute("recommend")); %>
      </table>
</body>
</html>