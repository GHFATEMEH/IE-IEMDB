package Controllers;

import IEMDB.Entity.Actor;
import IEMDB.Exception.*;
import IEMDB.IEMDB;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/actors/*")
public class ActorController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            loadActor(request, response);
        }catch (ActorNotFound a) {
            //request.setAttribute("msg", "Actor Not Found!");
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/404.jsp");
            requestDispatcher.forward(request, response);
        }

    }

    private void loadActor(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ActorNotFound {
        IEMDB iemdb = IEMDB.getInstance();
        if (iemdb.isAnybodyLoggedIn()) {
            int actorId = Integer.parseInt(request.getPathInfo().split("/")[1]);
            Actor actor = iemdb.findActorByID(actorId);
            request.setAttribute("actor_info", actor.getActorInfo());
            request.setAttribute("movies_info", actor.getMoviesInfo());
            RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher("/actor.jsp");
            requestDispatcher.forward(request, response);
        }
        else
            response.sendRedirect("http://localhost:8080/login.jsp");
    }
}
