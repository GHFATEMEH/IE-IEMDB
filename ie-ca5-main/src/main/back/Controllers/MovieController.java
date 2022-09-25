package Controllers;

import IEMDB.Exception.AgeLimitError;
import IEMDB.Exception.MovieAlreadyExists;
import IEMDB.Exception.MovieNotFound;
import IEMDB.Film.Comment;
import IEMDB.IEMDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/movies/*")
public class MovieController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            loadMovie(request, response);
        }
        catch (MovieNotFound movieNotFound){
            //request.setAttribute("msg", "Movie id not found!");
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/404.jsp");
            requestDispatcher.forward(request, response);
        }

    }

    private void loadMovie(HttpServletRequest request, HttpServletResponse response) throws MovieNotFound, IOException, ServletException {
        IEMDB iemdb = IEMDB.getInstance();
        int movieId = Integer.parseInt(request.getPathInfo().split("/")[1]);
        iemdb.doesMovieIdExist(movieId);
        if (iemdb.isAnybodyLoggedIn()) {
            request.setAttribute("movieInfo", iemdb.findMovieById(movieId).getMovieInfoList());
            request.setAttribute("castInfo", iemdb.findMovieById(movieId).getCastInf());
            request.setAttribute("comments", iemdb.findMovieById(movieId).getCommentsInfo());
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/movie.jsp");
            requestDispatcher.forward(request, response);
        }
        else
            response.sendRedirect("http://localhost:8080/login.jsp");
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IEMDB iemdb = IEMDB.getInstance();
        int movieId = Integer.parseInt(request.getPathInfo().split("/")[1]);
        String action = request.getParameter("action");
        if (action.equals("rate"))
            iemdb.findMovieById(movieId).addRate(Integer.parseInt(request.getParameter("rate")), iemdb.getCurrUser().getId());
        else if (action.equals("add"))
            addToWatchListProcess(iemdb, response, request, movieId);
        else if(action.equals("dislike"))
            iemdb.voteCommentURLMovies(Integer.parseInt(request.getParameter("comment_id")), -1);
        else if(action.equals("like"))
            iemdb.voteCommentURLMovies(Integer.parseInt(request.getParameter("comment_id")), 1);
        else if(action.equals("comment")){
            Comment newComment = iemdb.makeCommentInMovieURL(request.getParameter("comment"));
            iemdb.findMovieById(movieId).addComment(newComment);
        }
        response.sendRedirect("/movies/" + movieId);
    }

    private void addToWatchListProcess(IEMDB iemdb, HttpServletResponse response, HttpServletRequest request, int movieId) throws ServletException, IOException {
        try {
            iemdb.getCurrUser().addMovieToWatchList(iemdb.findMovieById(movieId));
        } catch (AgeLimitError ageLimitError) {
            request.setAttribute("msg", "Your age is not suitable for watching this movie!");
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            requestDispatcher.forward(request, response);
        } catch (MovieAlreadyExists movieAlreadyExists) {
            request.setAttribute("msg", "Your have this movie in your watchlist already!");
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/error.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
