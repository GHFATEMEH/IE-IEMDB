package ProcessInputs;

import Entity.Actor;
import Entity.User;
import Film.Comment;
import Film.Movie;
import Film.Vote;
import Utils.Functions;
import com.fasterxml.jackson.databind.node.TextNode;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import Exception.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommandHandler {
    private ArrayList<Actor> actors;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Comment> comments;
    private int currCommentId;
    private DataValidator dataValidator;

    private Actor findActorById(int id){
        for(Actor actor : actors){
            if(actor.getId() == id)
                return actor;
        }
        return null;
    }

    private Movie findMovieById(int id){
        for(Movie movie : movies){
            if(movie.getId() == id)
                return movie;
        }
        return null;
    }

    private Comment findCommentById(int id){
        for(Comment comment : comments){
            if(comment.getId() == id)
                return comment;
        }
        return null;
    }

    private User findUserByEmail(String email){
        for(User user : users){
            if(user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    private Map<String, Object> generateOutputMsg(Object data) {
        Map<String, Object> msg = new LinkedHashMap<String, Object>();
        msg.put("success", true);
        msg.put("data", data);
        return msg;
    }

    private Map<String, Object> handleAddActor(Map<String, Object> values) throws InvalidCommand {
        dataValidator.validateDate(((com.fasterxml.jackson.databind.node.TextNode)values.get("birthDate")).asText());
        Actor actor = findActorById(((com.fasterxml.jackson.databind.node.IntNode) values.get("id")).intValue());
        if(actor == null){
            Actor newActor = new Actor(values);
            actors.add(newActor);
        }
        else
            actor.updateInfo(values);
        return generateOutputMsg("actor added successfully");
    }

    private ArrayList<Actor> getMovieActors(Object _casts) {
        ArrayList<Actor> casts = new ArrayList<>();
        Functions functions = new Functions();
        ArrayList<Integer> castsInt = functions.convertToListInt(_casts);
        for(Integer id : castsInt){
            Actor newActor = findActorById(id);
            casts.add(newActor);
        }
        return casts;
    }

    private Map<String, Object> handleAddMovie(Map<String, Object> values) throws Exception {
        dataValidator.validateDate(((com.fasterxml.jackson.databind.node.TextNode)values.get("releaseDate")).asText());
        dataValidator.validateActorsIds(values.get("cast"));
        Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) values.get("id")).intValue());
        ArrayList<Actor> movieActors = getMovieActors(values.get("cast"));
        if(movie == null){
            Movie newMovie = new Movie(values, movieActors);
            movies.add(newMovie);
        }
        else
            movie.updateInfo(values, movieActors);
        return generateOutputMsg("movie added successfully");
    }

    private Map<String, Object> handleAddUser(Map<String, Object> values) throws UserAlreadyExists, InvalidCommand {
        dataValidator.validateDate(((com.fasterxml.jackson.databind.node.TextNode)values.get("birthDate")).asText());
        dataValidator.validateUserEmail(((com.fasterxml.jackson.databind.node.TextNode)values.get("email")).asText());
        User user = new User(values);
        users.add(user);
        return generateOutputMsg("user added successfully");
    }

    private Map<String, Object> handleAddComment(Map<String, Object> values) throws Exception {
        dataValidator.validateUserIds(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        dataValidator.validateMovie(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        currCommentId = currCommentId + 1;
        Comment comment = new Comment(currCommentId, values);
        Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        movie.addComment(comment);
        comments.add(comment);
        return generateOutputMsg("comment with id " + currCommentId + " added successfully");
    }

    public Map<String, Object> handleRateMovie(Map<String, Object> values) throws Exception {
        dataValidator.validateScore(((com.fasterxml.jackson.databind.node.IntNode) values.get("score")).intValue());
        dataValidator.validateUserIds(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        dataValidator.validateMovie(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        movie.addRate(values);
        return generateOutputMsg("movie rated successfully");
    }

    Map<String, Object> handleVoteComment(Map<String, Object> values) throws Exception {
        dataValidator.validateVote(((com.fasterxml.jackson.databind.node.IntNode) values.get("vote")).intValue());
        dataValidator.validateCommentId(((com.fasterxml.jackson.databind.node.IntNode) values.get("commentId")).intValue());
        dataValidator.validateUserIds(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        Comment comment = findCommentById(((com.fasterxml.jackson.databind.node.IntNode) values.get("commentId")).intValue());
        if (comment.shouldVoteUpdate(values) == false) {
            Vote newVote = new Vote(values);
            comment.addVote(newVote);
        }
        return generateOutputMsg("comment voted successfully");
    }

    Map<String, Object> handleAddToWatchList(Map<String, Object> values) throws Exception {
        dataValidator.validateMovie(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        dataValidator.validateUserIds(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        User user = findUserByEmail(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        user.addMovieToWatchList(movie);
        return generateOutputMsg("movie added to watchlist successfully");
    }

    private Map<String, Object> handleRemoveFromWatchList(Map<String, Object> values) throws Exception {
        dataValidator.validateMovie(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        dataValidator.validateUserIds(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        User user = findUserByEmail(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) values.get("movieId")).intValue());
        user.removeFromWatchList(movie);
        return generateOutputMsg("movie removed from watchlist successfully");
    }

    private Map<String, Object> handleGetMoviesList(Map<String, Object> values) throws JSONException, InvalidCommand {
        Map<String, Object> msg = new LinkedHashMap<String, Object>();
        msg.put("success", true);
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        for(Movie m : movies) {
            JSONObject newJsonObj = m.getMovieSummaryInfo();
            array.put(newJsonObj);
        }
        data.put("MoviesList", array);
        msg.put("data", data);
        return msg;
    }

    private Map<String, Object> handleGetMovieById(Map<String, Object> values) throws MovieNotFound, JSONException, InvalidCommand {
        dataValidator.validateMovie(((com.fasterxml.jackson.databind.node.IntNode) values.get("MovieId")).intValue());
        Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) values.get("MovieId")).intValue());

        Map<String, Object> msg = new LinkedHashMap<String, Object>();
        msg.put("success", true);
        JSONObject data = movie.getMovieInfo();
        msg.put("data", data);
        return msg;
    }

    Map<String, Object> handleGetMoviesByGenre(Map<String, Object> values) throws JSONException {
        String reqGenre = ((com.fasterxml.jackson.databind.node.TextNode) values.get("genre")).asText();
        Map<String, Object> msg = new LinkedHashMap<String, Object>();
        msg.put("success", true);
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        for(Movie m : movies) {
            ArrayList<String> genres = m.getGenres();
            for(String g : genres)
                if(g.equals(reqGenre)) {
                    JSONObject newJsonObj = m.getMovieSummaryInfo();
                    array.put(newJsonObj);
                }
        }
        data.put("MoviesListByGenre", array);
        msg.put("data", data);
        return msg;
    }

    //validate user added by ourselves
    private Map<String, Object> handleGetWatchList(Map<String, Object> values) throws Exception {
        dataValidator.validateUserIds(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        User user = findUserByEmail(((com.fasterxml.jackson.databind.node.TextNode)values.get("userEmail")).asText());
        ArrayList<Movie> watchList = user.getWatchList();

        Map<String, Object> msg = new LinkedHashMap<String, Object>();
        msg.put("success", true);
        JSONObject data = new JSONObject();
        JSONArray array = new JSONArray();
        for(Movie m : watchList) {
            JSONObject newJsonObj = m.getMovieSummaryInfo();
            array.put(newJsonObj);
        }
        data.put("WatchList", array);
        msg.put("data", data);

        return msg;
    }


    public CommandHandler(){
        currCommentId = 0;
        actors = new ArrayList<>();
        users = new ArrayList<>();
        movies = new ArrayList<>();
        comments = new ArrayList<>();
        dataValidator = new DataValidator(actors, users, movies, comments);
    }

    public Map<String, Object> handle(Pair<String, Map<String, Object>> commandData) throws Exception {
        String command = commandData.getKey();
        Map<String, Object> values = commandData.getValue();
        Map<String, Object> msg = new LinkedHashMap<String, Object>();
        if(command.equals("addActor"))
            msg = handleAddActor(values);
        else if(command.equals("addMovie"))
            msg = handleAddMovie(values);
        else if(command.equals("addUser"))
            msg = handleAddUser(values);
        else if(command.equals("addComment"))
            msg = handleAddComment(values);
        else if(command.equals("rateMovie"))
            msg = handleRateMovie(values);
        else if(command.equals("voteComment"))
            msg = handleVoteComment(values);
        else if(command.equals("addToWatchList"))
            msg = handleAddToWatchList(values);
        else if(command.equals("removeFromWatchList"))
            msg = handleRemoveFromWatchList(values);
        else if(command.equals("getMoviesList"))
            msg = handleGetMoviesList(values);
        else if(command.equals("getMovieById"))
            msg = handleGetMovieById(values);
        else if(command.equals("getMoviesByGenre"))
            msg = handleGetMoviesByGenre(values);
        else if(command.equals("getWatchList"))
            msg = handleGetWatchList(values);
        else
            throw new InvalidCommand("InvalidCommand");
        return msg;
    }
}
