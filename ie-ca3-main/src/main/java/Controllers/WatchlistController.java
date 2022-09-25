package Controllers;


import IEMDB.Entity.Actor;
import IEMDB.Entity.User;
import IEMDB.Film.Movie;
import IEMDB.IEMDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/watchlist")
public class WatchlistController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDB iemdb = IEMDB.getInstance();
        if (iemdb.isAnybodyLoggedIn()) {
            User user = iemdb.getCurrUser();
            request.setAttribute("movieInfo" , user.getWatchList());
            request.setAttribute("userInfo", user.getUserInfo());
            request.setAttribute("recommend", getRecommendedList(iemdb));
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/watchlist.jsp");
            requestDispatcher.forward(request, response);
        } else
            response.sendRedirect("http://localhost:8080/login.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDB iemdb = IEMDB.getInstance();
        User user = iemdb.getCurrUser();
        int movieID = Integer.parseInt(request.getParameter("movie_id"));
        Movie movie = iemdb.findMovieById(movieID);
        try {
            user.removeFromWatchList(movie);
            response.sendRedirect("/watchlist");
        } catch (Exception e) { }
    }

    private ArrayList<String> getRecommendedList(IEMDB iemdb) {
        iemdb.calculateGenreSimilarity();
        iemdb.sortByScore();
        return iemdb.getRecommendedMovies();
    }
}

