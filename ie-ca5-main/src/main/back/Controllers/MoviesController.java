package Controllers;

import IEMDB.IEMDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/movies")
public class MoviesController extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String search_param = request.getParameter("search");
        IEMDB iemdb = IEMDB.getInstance();
        iemdb.sortByImdbRate();
        if(action.equals("sort_by_date"))
            iemdb.sortByDate();
        request.setAttribute("movies_info", iemdb.getAllMovies());
        if(action.equals("search"))
            request.setAttribute("movies_info", iemdb.getSearchedMovies(search_param));
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/movies.jsp");
        requestDispatcher.forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDB iemdb = IEMDB.getInstance();
        if (iemdb.isAnybodyLoggedIn()) {
            iemdb.sortByImdbRate();
//        request.setAttribute("movies_info", iemdb.getAllMovies());
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/movies.jsp");
            requestDispatcher.forward(request, response);
        }
        else
            response.sendRedirect("http://localhost:8080/login.jsp");
    }
}
