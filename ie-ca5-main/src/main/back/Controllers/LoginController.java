package Controllers;

import IEMDB.IEMDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {

     public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
         String email = request.getParameter("email");
         IEMDB iemdb = IEMDB.getInstance();
         if (iemdb.doesUserExist(email)) {
             iemdb.loginUser(email);
             response.sendRedirect("/");
         }
         else {
             request.setAttribute("msg", "User Not Found!");
             RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/error.jsp");
             requestDispatcher.forward(request, response);
//                 HttpSession session = request.getSession(false);
//                 session.setAttribute("msg", "User Not Found!");
//                 response.sendRedirect("/error.jsp");
         }
     }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
        requestDispatcher.forward(request, response);
    }
}
