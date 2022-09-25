import ProcessInputs.CommandHandler;
import ProcessInputs.CommandParser;
import ProcessInputs.ExternalSourceDataReader;
import io.javalin.Javalin;
import org.json.JSONException;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import org.json.simple.parser.ParseException;

public class Controller {
    private CommandHandler commandHandler;
    private CommandParser commandParser;
    private ExternalSourceDataReader externalSourceDataReader;

    public Controller() throws InterruptedException, ParseException, IOException, JSONException {
        externalSourceDataReader = new ExternalSourceDataReader();
        commandParser = new CommandParser();
        Map<String, ArrayList<String>> data = externalSourceDataReader.getData();
        ArrayList<Map<String, Object>> actors = commandParser.parseStringArray(data.get("actors"));
        ArrayList<Map<String, Object>> movies = commandParser.parseStringArray(data.get("movies"));
        ArrayList<Map<String, Object>> users = commandParser.parseStringArray(data.get("users"));
        ArrayList<Map<String, Object>> comments = commandParser.parseStringArray(data.get("comments"));
        commandHandler = new CommandHandler(actors, movies, users, comments);
    }

    public void Run(){
        Javalin app = Javalin.create().start(7070);

        app.get("/movies", context -> {
            String result = commandHandler.getAllMovies();
            context.html(result);
        });

        app.get("/movies/:movie_id", context -> {
            String result = commandHandler.getMovieById(context.pathParam("movie_id"));
            context.html(result);
        });

        app.post("/movies/:movie_id", commandHandler::handleRateMovieInPage);

        app.get("/actors/:actor_id", context -> {
            String result = commandHandler.getActorById(context.pathParam("actor_id"));
            context.html(result);
        });

        app.get("/watchList/:user_id", context -> {
            String result = commandHandler.handleGetWatchList(context.pathParam("user_id"));
            context.html(result);
        });
        app.post("/watchList/:user_id", commandHandler::handleRemoveMovieFromWatchList);

        app.get("/watchList/:user_id/:movie_id", commandHandler::handleAddToWatchList);
        app.get("/rateMovie/:user_id/:movie_id/:rate", context -> {
            String result = commandHandler.handleRateMovie(context.pathParam("user_id"), context.pathParam("movie_id"), context.pathParam("rate"));
            context.html(result);
        });
        app.get("/voteComment/:user_id/:comment_id/:vote", commandHandler::handleVoteComment);
        app.get("/movies/search/:start_year/:end_year", context -> {
            String result = commandHandler.handleSearchByYear(context.pathParam("start_year"), context.pathParam("end_year"));
            context.html(result);
        });
        app.get("/movies/search/:genre", commandHandler::handleSearchByGenre);
        app.get("/*", commandHandler::handleError);
    }
}
