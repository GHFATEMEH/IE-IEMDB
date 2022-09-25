package Controllers;

import IEMDB.IEMDB;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        IEMDB iemdb = IEMDB.getInstance();
        iemdb.LogUserOut();
        response.sendRedirect("login.jsp");
    }
}

