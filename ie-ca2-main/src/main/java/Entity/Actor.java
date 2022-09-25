package Entity;

import Film.Movie;

import java.util.ArrayList;
import java.util.Map;

public class Actor extends Entity{
    private int id;
    private String nationality;
    private ArrayList<Movie> movies;

    public Actor(Map<String, Object> values){
        movies = new ArrayList<>();
        id = ((com.fasterxml.jackson.databind.node.IntNode) values.get("id")).intValue();
        updateInfo(values);
    }

    public void updateInfo(Map<String, Object> values){
        name = ((com.fasterxml.jackson.databind.node.TextNode) values.get("name")).asText();
        birthDate = ((com.fasterxml.jackson.databind.node.TextNode) values.get("birthDate")).asText();
        nationality = ((com.fasterxml.jackson.databind.node.TextNode) values.get("nationality")).asText();
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void addMovie(Movie movie) {
        if(movies.contains(movie) == false)
            movies.add(movie);
    }

    public String getActorInfo() {
        return "<li id=\"name\">name:  " + name +"</li>\n" +
                "        <li id=\"birthDate\">birthDate: " + birthDate + "</li>\n" +
                "        <li id=\"nationality\">nationality: " + nationality + "</li>\n" +
                "        <li id=\"tma\">Total movies acted in: " + movies.size() + "</li>";
    }

    public ArrayList<String> getMoviesInfo() {
        ArrayList<String> data = new ArrayList<>();
        for(Movie m : movies)
            data.add(m.getMovieInfoForActor());
        return data;
    }
}
