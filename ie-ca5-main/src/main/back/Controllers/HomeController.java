package Controllers;

import IEMDB.IEMDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//
//@WebServlet(name = "home", value = "/")
//public class HomeController extends HttpServlet {
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        IEMDB iemdb = IEMDB.getInstance();
//
//        if (iemdb.isAnybodyLoggedIn()) {
//            RequestDispatcher requestDispatcher = request.getRequestDispatcher("home.jsp");
//            requestDispatcher.forward(request, response);
//        }
//        else
//            response.sendRedirect("login.jsp");
//    }
//}

@RestController
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public void getAllMovies() {
        IEMDB iemdb = IEMDB.getInstance();
        iemdb.getAllMovies();

    }
}

