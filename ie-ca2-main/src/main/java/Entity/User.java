package Entity;

import Film.Movie;
import Film.WatchList;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Map;
import Exception.*;

public class User extends Entity{
    private String email;
    private String password;
    private String nickname;
    private int id;
    WatchList watchList;

    public User(Map<String, Object> values, int _id){
        id = _id;
        email = ((com.fasterxml.jackson.databind.node.TextNode) values.get("email")).asText();
        password = ((com.fasterxml.jackson.databind.node.TextNode) values.get("password")).asText();
        nickname = ((com.fasterxml.jackson.databind.node.TextNode) values.get("nickname")).asText();
        name = ((com.fasterxml.jackson.databind.node.TextNode) values.get("name")).asText();
        birthDate = ((com.fasterxml.jackson.databind.node.TextNode) values.get("birthDate")).asText();
        watchList = new WatchList();
    }

    public String getEmail() {
        return email;
    }

    private int calculateAge(LocalDate birthDate){
        LocalDate curDate = LocalDate.now();
        if ((birthDate != null) && (curDate != null))
            return Period.between(birthDate, curDate).getYears();
        else
            return 0;
    }

    private void checkAgeRange(int ageLimit) throws AgeLimitError {
        LocalDate birthDateUser = LocalDate.parse(birthDate);
        if( calculateAge(birthDateUser) < ageLimit)
            throw new AgeLimitError("AgeLimitError");
    }

    private void checkExistenceOfMovie(Movie movie) throws MovieAlreadyExists {
        if(watchList.checkExistenceOfMovie(movie))
            throw new MovieAlreadyExists("MovieAlreadyExists");
    }

    public void addMovieToWatchList(Movie movie) throws AgeLimitError {
        checkAgeRange(movie.getAgeLimit());
//        checkExistenceOfMovie(movie);
        watchList.addMovie(movie);
    }

    private void checkExistenceOfMovieToRemove(Movie movie) throws MovieNotFound {
        if(!watchList.checkExistenceOfMovie(movie))
            throw new MovieNotFound("MovieNotFound");
    }

    public void removeFromWatchList(Movie movie) throws Exception {
        checkExistenceOfMovieToRemove(movie);
        watchList.removeFromList(movie);
    }

    public ArrayList<String> getWatchList() {
        return watchList.getMoviesList();
    }

    public int getId() {
        return id;
    }

    public String getUserInfo() {
        return "        <li id=\"name\">name: " + name + "</li>\n" +
                "        <li id=\"nickname\">nickname: @" + nickname + "</li>\n";
    }

    public String getNickName() {
        return nickname;
    }
}
