<%@page import="IEMDB.IEMDB"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Movie</title>
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
    <ul> <% out.println(request.getAttribute("movieInfo"));%> </ul>
    <h3>Cast</h3>
    <table>
          <tr>
           <th>name</th>
           <th>age</th>
           <th>Link</th>
           <th></th>
           <th></th>
          </tr>
           <% out.println(request.getAttribute("castInfo")); %>
    </table>
    <br>
        <form action="" method="POST">
          <label>Rate(between 1 and 10):</label>
          <input type="number" name="rate" min="1" max="10">
          <button type="submit" name="action" value="rate">rate</button>
        </form>
    <br>
    <br>
    <form action="" method="POST">
        <button type="submit" name="action" value="add">Add to WatchList</button>
    </form>
    <br/>
    <table>
      <tr>
       <th>nickname</th>
       <th>comment</th>
        <th></th>
        <th></th>
      </tr>
       <% out.println(request.getAttribute("comments")); %>
    </table>
    <br><br>
    <form action="" method="POST">
        <label>Your Comment:</label>
        <input type="text" name="comment" value="">
        <button type="submit" name="action" value="comment">Add Comment</button>
    </form>
</body>
</html>
