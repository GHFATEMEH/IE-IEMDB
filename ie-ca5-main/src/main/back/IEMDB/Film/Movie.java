package IEMDB.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Map;

import IEMDB.Entity.Actor;
import IEMDB.Exception.AgeLimitError;
import IEMDB.Utils.Functions;

public class Movie {
    private int id;
    private String name;
    private String summary;
    private String releaseDate;
    private String director;
    private ArrayList<String> writers;
    private ArrayList<String> genres;
    private ArrayList<Actor> casts;
    private double imdbRate;
    private int duration;
    private int ageLimit;
    private Double ratingCount;

    private ArrayList<Rate> rates;
    private ArrayList<Comment> comments;
    private int genreSimilarity;
    private double score;
    private String image;
    private String coverImage;

    public Movie(Map<String, Object> values, ArrayList<Actor> _casts){
        id = ((com.fasterxml.jackson.databind.node.IntNode) values.get("id")).intValue();
        updateInfo(values, _casts);
        rates = new ArrayList<Rate> ();
        comments = new ArrayList<Comment> ();
        ratingCount = null;
        genreSimilarity = 0;
        score = 0;
    }

    public void updateInfo(Map<String, Object> values, ArrayList<Actor> _casts){
        Functions functions = new Functions();
        name = ((com.fasterxml.jackson.databind.node.TextNode) values.get("name")).asText();
        summary = ((com.fasterxml.jackson.databind.node.TextNode) values.get("summary")).asText();
        releaseDate = ((com.fasterxml.jackson.databind.node.TextNode) values.get("releaseDate")).asText();
        director = ((com.fasterxml.jackson.databind.node.TextNode) values.get("director")).asText();
        imdbRate = ((com.fasterxml.jackson.databind.node.DoubleNode) values.get("imdbRate")).doubleValue();
        duration = ((com.fasterxml.jackson.databind.node.IntNode) values.get("duration")).intValue();
        ageLimit = ((com.fasterxml.jackson.databind.node.IntNode) values.get("ageLimit")).intValue();
        writers = functions.convertToListString((values.get("writers")));
        genres = functions.convertToListString((values.get("genres")));
        casts = _casts;
        image = ((com.fasterxml.jackson.databind.node.TextNode) values.get("image")).asText();
        coverImage = ((com.fasterxml.jackson.databind.node.TextNode) values.get("coverImage")).asText();
        addMovieToCast();
    }

    private void addMovieToCast() {
        for(Actor a : casts)
            a.addMovie(this);
    }

    public int getId(){
        return id;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    private Rate findRateByUserId(int UserId) {
        for(Rate r : rates) {
            if(r.getUserId() == UserId)
                return r;
        }
        return null;
    }

    private void updateRate() {
        double sum = 0;
        for(Rate r : rates)
            sum = sum + r.getScore();
        ratingCount = sum / rates.size();
    }

    public void addRate(int rate, int user_id) {
        Rate prevRate = findRateByUserId(user_id);

        if(prevRate == null) {
            Rate newRate = new Rate(user_id, rate);
            rates.add(newRate);
        }
        else
            prevRate.setNewScore(rate);

        updateRate();
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    private ArrayList<String> getCastNames() {
        ArrayList<String> castsName = new ArrayList<>();
        for(Actor a : casts)
            castsName.add(a.getName());
        return castsName;
    }

    public String getMovieInfo() {
        return "            <td>" + name + "</td>\n" +
                "            <td>" + summary +"</td>\n" +
                "            <td>" + releaseDate + "</td>\n" +
                "            <td>" + director + "</td>\n" +
                "            <td>" + String.join(", ", writers) + "</td>\n" +
                "            <td>" + String.join(", ", genres) + "</td>\n" +
                "            <td>" + String.join(", ", getCastNames()) + "</td>\n" +
                "            <td>" + imdbRate + "</td>\n" +
                "            <td>" + ratingCount + "</td>\n" +
                "            <td>" + duration + "</td>\n" +
                "            <td>" + ageLimit + "</td>\n" +
                "            <td><a href=\"/movies/" + id + "\">Link</a></td>\n";
    }

    public String getMovieInfoForActor() {
        return  "<tr>" +
                "           <td>" + name + "</td>\n" +
                "            <td>" + imdbRate + "</td> \n" +
                "            <td>" + ratingCount + "</td> \n" +
                "            <td><a href=\"/movies/" + id + "\">Link</a></td>" +
                "</tr>";
    }

    public String getMovieInfoForWatchList() {
        return "<tr>" +
                "            <th>" + name + "</th>\n" +
                "            <th>" + releaseDate + "</th> \n" +
                "            <th>" + director + "</th> \n" +
                "            <th>" + String.join(", ", genres) + "</th> \n" +
                "            <th>" + imdbRate + "</th> \n" +
                "            <th>" + ratingCount + "</th> \n" +
                "            <th>" + duration + "</th> \n" +
                "            <td><a href=\"/movies/" + id + "\">Link</a></td>\n" +
                "            <td>\n" +
                "                <form action=\"\" method=\"POST\" >\n" +
                "                    <input id=\"form_movie_id\" type=\"hidden\" name=\"movie_id\" value=\"" + id + "\">\n" +
                "                    <button type=\"submit\">Remove</button>\n" +
                "                </form>\n" +
                "            </td>\n" +
                "</tr>";
    }

    public String getRecommendation() {
        return  "<tr>" +
                "            <th>" + name + "</th>\n" +
                "            <th>" + imdbRate + "</th> \n" +
                "            <td><a href=\"/movies/" + id + "\">Link</a></td>\n" +
                "</tr>";
    }

//    public boolean isThisGenre(String genre) {
//        for(String g : genres)
//            if(g.equals(genre))
//                return true;
//        return false;
//    }

//    public boolean isBetweenYears(int start_year, int end_year) {
//        int year = LocalDate.parse(releaseDate, DateTimeFormatter.ofPattern("uuuu-M-d").withResolverStyle(ResolverStyle.STRICT)).getYear();
//        if(start_year <= year && year <= end_year)
//            return true;
//        return false;
//    }
//
    public String getMovieInfoList() {
        return "     <li id=\"name\">name: " + name + "</li>\n" +
                "      <li id=\"summary\">summary: " + summary + "</li>\n" +
                "      <li id=\"releaseDate\">releaseDate: " + releaseDate + "</li>\n" +
                "      <li id=\"director\">director: " + director + "</li>\n" +
                "      <li id=\"writers\">writers: " + String.join(", ", writers) + "</li>\n" +
                "      <li id=\"genres\">genres: " + String.join(", ", genres) + "</li>\n" +
                "      <li id=\"cast\">cast: " + String.join(", ", getCastNames()) + "</li>\n" +
                "      <li id=\"imdbRate\">imdb Rate: " + imdbRate + "</li>\n" +
                "      <li id=\"rating\">rating: " + ratingCount + "</li>\n" +
                "      <li id=\"duration\">duration: " + duration + " minutes</li>\n" +
                "      <li id=\"ageLimit\">ageLimit: " + ageLimit + "</li>\n";
    }

    public String getCommentsInfo() {
        String data = "";
        for(Comment c : comments) {
            data += c.getCommentInfo();
        }
        return data;
    }

    public double getImdbRate() {
        return imdbRate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getGenres() {return genres;}

    public void calculateScore() {
        if (ratingCount == null)
            ratingCount = 0.0;

        score = 3 * genreSimilarity + imdbRate + ratingCount.doubleValue();
    }

    public void incrementGenreSimilarity(int number) {
        genreSimilarity = genreSimilarity + number;
    }

    public void resetScoreAndSimilarity() {
        score = 0;
        genreSimilarity = 0;
    }

    public double getScore() {return score;}

    public ArrayList<String> getCastInf(){
        ArrayList<String> castInfo = new ArrayList<>();
        for(Actor a : casts) {
            castInfo.add(a.getActorInfoForMovie());
        }
        return castInfo;
    }
}
