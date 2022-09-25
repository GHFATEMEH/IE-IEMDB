package ProcessInputs;

import Entity.Actor;
import Entity.User;
import Film.Comment;
import Film.Movie;
import Film.Vote;
import ProcessOutputs.OutputGenerator;
import Utils.Functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import Exception.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.JSONException;
import io.javalin.http.Context;

public final class CommandHandler {
    private ArrayList<Actor> actors;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Comment> comments;
    private int currCommentId;
    private DataValidator dataValidator;
    private OutputGenerator outputGenerator;

    public CommandHandler() {
        outputGenerator = new OutputGenerator();
    }

    public CommandHandler(ArrayList<Map<String, Object>> _actors, ArrayList<Map<String, Object>> _movies,
        ArrayList<Map<String, Object>> _users, ArrayList<Map<String, Object>> _comments) throws JSONException {
        currCommentId = 0;
        makeActors(_actors);
        makeMovies(_movies);
        makeUsers(_users);
        makeComments(_comments);
        dataValidator = new DataValidator(actors, users, movies, comments);
        outputGenerator = new OutputGenerator();
    }

    private void makeActors(ArrayList<Map<String, Object>> _actors) throws JSONException {
        actors = new ArrayList<>();
        for(Map<String, Object> a : _actors)
            actors.add(new Actor(a));
    }

    private void makeMovies(ArrayList<Map<String, Object>> _movies) throws JSONException {
        movies = new ArrayList<>();
        for(Map<String, Object> m : _movies) {
            movies.add(new Movie(m, getMovieActors(m.get("cast"))));
        }
    }

    private void makeUsers(ArrayList<Map<String, Object>> _users) {
        users = new ArrayList<>();
        for(Map<String, Object> u : _users) {
            int id = users.size() + 1;
            users.add(new User(u, id));
        }
    }

    private void makeComments(ArrayList<Map<String, Object>> _comments) {
        comments = new ArrayList<>();
        for(Map<String, Object> c : _comments) {
            currCommentId = currCommentId + 1;
            Comment new_comment = new Comment(currCommentId, c, getNickName(((com.fasterxml.jackson.databind.node.TextNode) c.get("userEmail")).asText()));
            comments.add(new_comment);
            Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) c.get("movieId")).intValue());
            movie.addComment(new_comment);
        }
    }

    private String getNickName(String userEmail) {
        for(User u : users) {
            if(u.getEmail().equals(userEmail))
                return u.getNickName();
        }
        return null;
    }

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

    public String getAllMovies() throws IOException {
        try {
            ArrayList<String> data = new ArrayList<>();
            for(Movie m : movies)
                data.add(m.getMovieInfo());
            return outputGenerator.generateMoviesOutput(data);
        } catch (Exception e) {
           return outputGenerator.generate403ErrorOutput();
        }
    }

    public String getMovieById(String movie_id) throws IOException {
        try {
            dataValidator.validateMovie(Integer.parseInt(movie_id));
            Movie movie = findMovieById(Integer.parseInt(movie_id));
            return outputGenerator.generateGetMovieOutput(movie.getMovieInfoList(), movie.getCommentsInfo());
        } catch (MovieNotFound m) {
            return outputGenerator.generate404ErrorOutput();
        } catch (Exception e) {
            return outputGenerator.generate403ErrorOutput();
        }
    }

    public void handleRateMovieInPage(Context context) throws IOException {
        if(context.formParam("quantity") != null)
            rateMovieURLMovies(context);
        else if(context.formParam("user_id_watchList") != null)
            addToWatchListURLMovies(context);
        else if(context.formParam("comment_id") != null)
            voteCommentURLMovies(context);
    }

    public void rateMovieURLMovies(Context context) throws IOException {
        try{
            dataValidator.validateUserById(Integer.parseInt(context.formParam("user_id")));
            Movie movie = findMovieById(Integer.parseInt(context.pathParam("movie_id")));
            movie.addRate(Integer.parseInt(context.formParam("quantity")), Integer.parseInt(context.formParam("user_id")));
            context.html(outputGenerator.generate200Output());
        } catch (UserNotFound u) {
            context.html(outputGenerator.generate404ErrorOutput());
        } catch (Exception e) {
            context.html(outputGenerator.generate403ErrorOutput());
        }
    }

    public void addToWatchListURLMovies(Context context) throws IOException {
        try {
            dataValidator.validateUserById(Integer.parseInt(context.formParam("user_id_watchList")));
            User user = findUserById(Integer.parseInt(context.formParam("user_id_watchList")));
            Movie movie = findMovieById(Integer.parseInt(context.pathParam("movie_id")));
            user.addMovieToWatchList(movie);
            context.html(outputGenerator.generate200Output());
        } catch (UserNotFound u) {
            context.html(outputGenerator.generate404ErrorOutput());
        } catch (AgeLimitError a) {
            context.html(outputGenerator.generate403ErrorOutput());
        } catch (Exception e) {
            context.html(outputGenerator.generate403ErrorOutput());
        }
    }

    public void voteCommentURLMovies(Context context) throws IOException {
        try {
            dataValidator.validateUserById(Integer.parseInt(context.formParam("user_id")));
            Comment comment = findCommentById(Integer.parseInt(context.formParam("comment_id")));
            if (comment.shouldVoteUpdate(Integer.parseInt(context.formParam("user_id")), Integer.parseInt(context.formParam("vote"))) == false) {
                Vote newVote = new Vote(Integer.parseInt(context.formParam("user_id")), Integer.parseInt(context.formParam("vote")));
                comment.addVote(newVote);
            }
            context.html(outputGenerator.generate200Output());
        } catch (UserNotFound u) {
            context.html(outputGenerator.generate404ErrorOutput());
        } catch (Exception e) {
            context.html(outputGenerator.generate403ErrorOutput());
        }
    }

    public String getActorById(String actor_id) throws IOException {
        try {
            dataValidator.validateActor(Integer.parseInt(actor_id));
            Actor actor = findActorById(Integer.parseInt(actor_id));
            return outputGenerator.generateGetActorByIdOutput(actor.getActorInfo(), actor.getMoviesInfo());
        } catch (ActorNotFound a) {
           return outputGenerator.generate404ErrorOutput();
        } catch (Exception e) {
            return outputGenerator.generate403ErrorOutput();
        }
    }

    public String handleGetWatchList(String user_id) throws IOException {
        try {
            dataValidator.validateUserById(Integer.parseInt(user_id));
            User user = findUserById(Integer.parseInt(user_id));
            return outputGenerator.generateGetWatchListOutput(user.getUserInfo(), user.getWatchList());
        } catch (UserNotFound userNotFound) {
           return outputGenerator.generate404ErrorOutput();
        } catch (Exception e) {
            return outputGenerator.generate403ErrorOutput();
        }
    }

    public void handleRemoveMovieFromWatchList(Context context) throws Exception {
        User user = findUserById(Integer.parseInt(context.pathParam("user_id")));
        Movie movie = findMovieById(Integer.parseInt(context.formParam("movie_id")));
        user.removeFromWatchList(movie);
        context.html(outputGenerator.generate200Output());
    }

    private User findUserById(int user_id) {
        for(User u : users)
            if(u.getId() == user_id)
                return u;
        return null;
    }

    public void handleAddToWatchList(Context context) throws IOException {
        try {
            dataValidator.validateMovie(Integer.parseInt(context.pathParam("movie_id")));
            dataValidator.validateUserById(Integer.parseInt(context.pathParam("user_id")));
            User user = findUserById(Integer.parseInt(context.pathParam("user_id")));
            Movie movie = findMovieById(Integer.parseInt(context.pathParam("movie_id")));
            user.addMovieToWatchList(movie);
            context.html(outputGenerator.generate200Output());
        } catch (MovieNotFound m) {
            context.html(outputGenerator.generate404ErrorOutput());
        } catch (UserNotFound u) {
            context.html(outputGenerator.generate404ErrorOutput());
        } catch (AgeLimitError a) {
            context.html(outputGenerator.generate403ErrorOutput());
        } catch (Exception e) {
            context.html(outputGenerator.generate403ErrorOutput());
        }
    }

    public String handleRateMovie(String user_id, String movie_id, String rate) throws IOException {
        try {
            dataValidator.validateMovie(Integer.parseInt(movie_id));
            dataValidator.validateUserById(Integer.parseInt(user_id));
            dataValidator.validateScore(Integer.parseInt(rate));
            Movie movie = findMovieById(Integer.parseInt(movie_id));
            movie.addRate(Integer.parseInt(rate), Integer.parseInt(user_id));
            return outputGenerator.generate200Output();
        } catch (MovieNotFound m) {
            return outputGenerator.generate404ErrorOutput();
        } catch (UserNotFound u) {
            return outputGenerator.generate404ErrorOutput();
        } catch (InvalidRateScore i) {
            return outputGenerator.generate403ErrorOutput();
        } catch (Exception e) {
            return outputGenerator.generate403ErrorOutput();
        }
    }

    public void handleVoteComment(Context context) throws IOException {
        try {
            dataValidator.validateUserById(Integer.parseInt(context.pathParam("user_id")));
            dataValidator.validateCommentId(Integer.parseInt(context.pathParam("comment_id")));
            dataValidator.validateVote(Integer.parseInt(context.pathParam("vote")));
            Comment comment = findCommentById(Integer.parseInt(context.pathParam("comment_id")));
            if (comment.shouldVoteUpdate(Integer.parseInt(context.pathParam("user_id")), Integer.parseInt(context.pathParam("vote"))) == false) {
                Vote newVote = new Vote(Integer.parseInt(context.pathParam("user_id")), Integer.parseInt(context.pathParam("vote")));
                comment.addVote(newVote);
            }
            context.html(outputGenerator.generate200Output());
        }  catch (CommentNotFound c) {
            context.html(outputGenerator.generate404ErrorOutput());
        } catch (UserNotFound u) {
            context.html(outputGenerator.generate404ErrorOutput());
        } catch (InvalidVoteValue i) {
            context.html(outputGenerator.generate403ErrorOutput());
        } catch (Exception e) {
            context.html(outputGenerator.generate403ErrorOutput());
        }
    }

    public void handleSearchByGenre(Context context) throws IOException {
        try {
            ArrayList<String> data = new ArrayList<>();
            for(Movie m : movies)
                if(m.isThisGenre(context.pathParam("genre")))
                    data.add(m.getMovieInfo());
            context.html(outputGenerator.generateMoviesOutput(data));
        } catch (Exception e) {
            context.html(outputGenerator.generate403ErrorOutput());
        }
    }

    public String handleSearchByYear(String start_year, String end_year) throws IOException {
        try {
            ArrayList<String> data = new ArrayList<>();
            for(Movie m : movies)
                if(m.isBetweenYears(Integer.parseInt(start_year), Integer.parseInt(end_year)))
                    data.add(m.getMovieInfo());
            return outputGenerator.generateMoviesOutput(data);
        } catch (Exception e) {
           return outputGenerator.generate403ErrorOutput();
        }
    }

    public void handleError(Context context) throws IOException {
        context.html(outputGenerator.generate403ErrorOutput());
    }
}
