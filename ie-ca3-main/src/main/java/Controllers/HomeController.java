package Controllers;

import IEMDB.IEMDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "home", value = "/")
public class HomeController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDB iemdb = IEMDB.getInstance();

        if (iemdb.isAnybodyLoggedIn()) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("home.jsp");
            requestDispatcher.forward(request, response);
        }
        else
            response.sendRedirect("login.jsp");
    }
}