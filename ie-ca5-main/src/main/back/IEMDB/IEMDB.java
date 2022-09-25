package IEMDB;

import IEMDB.Entity.Actor;
import IEMDB.Entity.User;
import IEMDB.Exception.ActorNotFound;
import IEMDB.Film.Comment;
import IEMDB.Film.Movie;
import IEMDB.Film.Vote;
import IEMDB.Film.WatchList;
import IEMDB.ProcessInputs.CommandParser;
import IEMDB.ProcessInputs.ExternalSourceDataReader;
import IEMDB.ProcessOutputs.OutputGenerator;
import IEMDB.Utils.Functions;
import com.fasterxml.jackson.databind.node.TextNode;
import org.json.JSONException;
import IEMDB.Exception.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class IEMDB {
    private static IEMDB instance;
    private CommandParser commandParser;
    private ExternalSourceDataReader externalSourceDataReader;
    private ArrayList<Actor> actors;
    private ArrayList<User> users;
    private List<Movie> movies;
    private ArrayList<Comment> comments;
    private int currCommentId;
    private User currUser;
    private OutputGenerator outputGenerator;

    private IEMDB() {
        externalSourceDataReader = new ExternalSourceDataReader();
        commandParser = new CommandParser();
        try {
            Map<String, ArrayList<String>> data = externalSourceDataReader.getData();
            ArrayList<Map<String, Object>> actors = commandParser.parseStringArray(data.get("actors"));
            ArrayList<Map<String, Object>> movies = commandParser.parseStringArray(data.get("movies"));
            ArrayList<Map<String, Object>> users = commandParser.parseStringArray(data.get("users"));
            ArrayList<Map<String, Object>> comments = commandParser.parseStringArray(data.get("comments"));
            outputGenerator = new OutputGenerator();
            setData(actors, movies, users, comments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currUser = null;
    }

    public static IEMDB getInstance() {
        if (instance == null)
            instance = new IEMDB();
        return instance;
    }

    private void setData(ArrayList<Map<String, Object>> _actors, ArrayList<Map<String, Object>> _movies,
                         ArrayList<Map<String, Object>> _users, ArrayList<Map<String, Object>> _comments) throws JSONException {
        currCommentId = 0;
        makeActors(_actors);
        makeMovies(_movies);
        makeUsers(_users);
        makeComments(_comments);
        outputGenerator = new OutputGenerator();
    }

    private void makeActors(ArrayList<Map<String, Object>> _actors) throws JSONException {
        actors = new ArrayList<>();
        for (Map<String, Object> a : _actors)
            actors.add(new Actor(a));
    }

    private void makeMovies(ArrayList<Map<String, Object>> _movies) throws JSONException {
        movies = new ArrayList<>();
        for (Map<String, Object> m : _movies) {
            movies.add(new Movie(m, getMovieActors(m.get("cast"))));
        }
    }

    private void makeUsers(ArrayList<Map<String, Object>> _users) {
        users = new ArrayList<>();
        for (Map<String, Object> u : _users) {
            int id = users.size() + 1;
            users.add(new User(u, id));
        }
    }

    private void makeComments(ArrayList<Map<String, Object>> _comments) {
        comments = new ArrayList<>();
        for (Map<String, Object> c : _comments) {
            currCommentId = currCommentId + 1;
            Comment new_comment = new Comment(currCommentId, c, getNickName(((TextNode) c.get("userEmail")).asText()));
            comments.add(new_comment);
            Movie movie = findMovieById(((com.fasterxml.jackson.databind.node.IntNode) c.get("movieId")).intValue());
            movie.addComment(new_comment);
        }
    }

    private String getNickName(String userEmail) {
        for (User u : users) {
            if (u.getEmail().equals(userEmail))
                return u.getNickName();
        }
        return null;
    }

    private Actor findActorById(int id) {
        for (Actor actor : actors) {
            if (actor.getId() == id)
                return actor;
        }
        return null;
    }

    public Movie findMovieById(int id) {
        for (Movie movie : movies) {
            if (movie.getId() == id)
                return movie;
        }
        return null;
    }

    public void doesMovieIdExist(int id) throws MovieNotFound {
        for (Movie movie : movies) {
            if (movie.getId() == id)
                return;
        }
        throw new MovieNotFound("MovieNotFound");
    }

    private Comment findCommentById(int id){
        for(Comment comment : comments){
            if(comment.getId() == id)
                return comment;
        }
        return null;
    }

    public User findUserByEmail(String email) {
        for (User u : users)
            if (u.getEmail().equals(email))
                return u;
        return null;
    }

    private ArrayList<Actor> getMovieActors(Object _casts) {
        ArrayList<Actor> casts = new ArrayList<>();
        Functions functions = new Functions();
        ArrayList<Integer> castsInt = functions.convertToListInt(_casts);
        for (Integer id : castsInt) {
            Actor newActor = findActorById(id);
            casts.add(newActor);
        }
        return casts;
    }

    public String getLoggedInEmail() {
        if (currUser == null)
            return null;
        else
            return currUser.getEmail();
    }

    public boolean isAnybodyLoggedIn() {
        if (currUser == null)
            return false;
        else
            return true;
    }

    public void LogUserOut() {
        currUser = null;
    }

    public boolean doesUserExist(String email) {
        if (findUserByEmail(email) != null)
            return true;
        else
            return false;
    }

    public void loginUser(String email) {
        currUser = findUserByEmail(email);
    }

    public String getAllMovies() {
        try {
            ArrayList<String> data = new ArrayList<>();
            for (Movie m : movies)
                data.add(m.getMovieInfo());
            return outputGenerator.makeMoviesInfo(data);
        } catch (Exception e) {
            return "";
        }
    }

    public void sortByImdbRate() {
        Collections.sort(movies, new Comparator<Movie>() {
            public int compare(Movie m1, Movie m2) {
                if (m1.getImdbRate() == m2.getImdbRate())
                    return 0;
                return m1.getImdbRate() > m2.getImdbRate() ? -1 : 1;
            }
        });
    }

    public void sortByDate() {
        Collections.sort(movies, new Comparator<Movie>() {
            public int compare(Movie m1, Movie m2) {
                return m2.getReleaseDate().compareTo(m1.getReleaseDate());
            }
        });
    }

    public String getSearchedMovies(String search_param) {
        try {
            ArrayList<String> data = new ArrayList<>();
            for (Movie m : movies)
                if (m.getName().equals(search_param) || m.getName().indexOf(search_param) != -1)
                    data.add(m.getMovieInfo());
            return outputGenerator.makeMoviesInfo(data);
        } catch (Exception e) {
            return "";
        }
    }

    public Actor findActorByID(int actorId) throws ActorNotFound {
        for (Actor a : actors)
            if (a.getId() == actorId)
                return a;
        throw new ActorNotFound("ActorNotFound");
    }

    public User getCurrUser(){
        return currUser;
    }

    public void calculateGenreSimilarity() {
        for (Movie movie : movies) {
            movie.resetScoreAndSimilarity();
            checkGenreSimilarity(movie);
        }
    }

    private void checkGenreSimilarity(Movie movie){
        ArrayList<String> genreMovie = addElementToArray(movie.getGenres());
        for(Movie m : currUser.getMoviesInWatchList()) {
            ArrayList<String> userMoviesInWatchListGenre = addElementToArray(m.getGenres());
            userMoviesInWatchListGenre.retainAll(genreMovie);
            movie.incrementGenreSimilarity(userMoviesInWatchListGenre.size());
        }
        movie.calculateScore();
    }

    private ArrayList<String> addElementToArray(ArrayList<String> genre) {
        ArrayList<String> temp = new ArrayList<>();
        for(String s : genre)
            temp.add(s);
        return temp;
    }

    public void sortByScore() {
        Collections.sort(movies, new Comparator<Movie>() {
            public int compare(Movie m1, Movie m2) {
                if (m1.getScore() == m2.getScore())
                    return 0;
                return m1.getScore() < m2.getScore() ? 1 : -1;
            }
        });
    }

    public ArrayList<String> getRecommendedMovies() {
        ArrayList<Movie> recommendedMovies = deleteSameMoviesWithWatchList();
        ArrayList<String> recommendedMoviesInfo = new ArrayList<>();
        double score = 0.0;
        for(Movie m : recommendedMovies){
            if(recommendedMoviesInfo.size() < 3) {
                if (m.getScore() != score) {
                    score = m.getScore();
                    recommendedMoviesInfo.add(m.getRecommendation());
                }
            }
        }
        return recommendedMoviesInfo;
    }

    private ArrayList<Movie> deleteSameMoviesWithWatchList() {
        ArrayList<Movie> recommendedMovies = new ArrayList<>();
        WatchList watchList = currUser.getWatchListField();
        for(Movie m :movies){
            if (watchList.checkExistenceOfMovie(m) == false)
                recommendedMovies.add(m);
        }
        return recommendedMovies;
    }

    public void voteCommentURLMovies(int commentId, int vote) {
        Comment comment = findCommentById(commentId);
        if (comment.shouldVoteUpdate(currUser.getId(), vote) == false) {
            Vote newVote = new Vote(currUser.getId(), vote);
            comment.addVote(newVote);
        }
    }

    public Comment makeCommentInMovieURL(String text) {
        currCommentId = currCommentId + 1;
        Map<String, Object> commentValues = new HashMap<>();
        commentValues.put("text", new TextNode(text));
        Comment new_comment = new Comment(currCommentId, commentValues, currUser.getNickName());
        comments.add(new_comment);
        return new_comment;
    }
}




