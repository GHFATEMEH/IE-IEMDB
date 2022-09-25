package IEMDB.Entity;

import IEMDB.Exception.AgeLimitError;
import IEMDB.Film.*;


import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Map;

public class Actor extends Entity {
    private int id;
    private String nationality;
    private ArrayList<Movie> movies;
    private String image;

    public Actor(Map<String, Object> values){
        movies = new ArrayList<>();
        id = ((com.fasterxml.jackson.databind.node.IntNode) values.get("id")).intValue();
        updateInfo(values);
    }

    public void updateInfo(Map<String, Object> values){
        name = ((com.fasterxml.jackson.databind.node.TextNode) values.get("name")).asText();
        birthDate = ((com.fasterxml.jackson.databind.node.TextNode) values.get("birthDate")).asText();
        nationality = ((com.fasterxml.jackson.databind.node.TextNode) values.get("nationality")).asText();
        image = ((com.fasterxml.jackson.databind.node.TextNode) values.get("image")).asText();
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

    public String getActorInfoForMovie() {
        String ageCalculated = getAgeCalculated();
        return  "<tr>" +
                "            <th>" + name + "</th>\n" +
                "            <th>" + ageCalculated + "</th> \n" +
                "            <td><a href=\"/actors/" + id + "\">Link</a></td>\n" +
                "</tr>";
    }

    private String getAgeCalculated() {
        String[] parts = birthDate.split(" ");
        int currYear = LocalDate.now().getYear;
        return currYear - Integer.parseInt(parts[2]);
    }
}
