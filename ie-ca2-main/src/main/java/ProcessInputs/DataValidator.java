package ProcessInputs;

import Entity.Actor;
import Entity.User;
import Film.Comment;
import Film.Movie;
import Utils.Functions;
import Exception.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

public class DataValidator {
    private ArrayList<Actor> actors;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Comment> comments;

    private Actor findActorById(int id){
        for(Actor actor : actors){
            if(actor.getId() == id)
                return actor;
        }
        return null;
    }

    public DataValidator(ArrayList<Actor> _actors, ArrayList<User> _users, ArrayList<Movie> _movies, ArrayList<Comment> _comments) {
        actors = _actors;
        users = _users;
        movies = _movies;
        comments = _comments;
    }

    public void validateMovie(int movieId) throws MovieNotFound {
        for(Movie movie : movies){
            if(movie.getId() == movieId)
                return;
        }
        throw new MovieNotFound("MovieNotFound");
    }

    public void validateScore(int score) throws InvalidRateScore {
        if(score > 10 || score < 1)
            throw new InvalidRateScore("InvalidRateScore");
    }

    public void validateVote(int vote) throws InvalidVoteValue {
        if(vote != 1 && vote != 0 && vote != -1)
            throw new InvalidVoteValue("InvalidVoteValue");
    }

    public void validateCommentId(int id) throws CommentNotFound {
        for(Comment c : comments){
            if(c.getId() == id)
                return;
        }
        throw new CommentNotFound("CommentNotFound");
    }

    public void validateActor(int actorId) throws ActorNotFound {
        if(findActorById(actorId) == null)
            throw new ActorNotFound("ActorNotFound");
    }

    public void validateUserById(int user_id) throws UserNotFound {
        for(User user: users){
            if(user.getId() == user_id)
                return;
        }
        throw new UserNotFound("UserNotFound");
    }
}


