package Film;

import java.util.ArrayList;
import java.util.Map;

import Entity.Actor;
import Utils.Functions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public Movie(Map<String, Object> values, ArrayList<Actor> _casts){
        id = ((com.fasterxml.jackson.databind.node.IntNode) values.get("id")).intValue();
        updateInfo(values, _casts);
        rates = new ArrayList<Rate> ();
        comments = new ArrayList<Comment> ();
        ratingCount = null;
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
    }

    public int getId(){
        return id;
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    private Rate findRateByEmail(String email) {
        for(Rate r : rates) {
            if(r.getEmail().equals(email))
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

    public void addRate(Map<String, Object> values) {
        String userEmail = ((com.fasterxml.jackson.databind.node.TextNode) values.get("userEmail")).asText();
        int score = ((com.fasterxml.jackson.databind.node.IntNode) values.get("score")).intValue();
        Rate prevRate = findRateByEmail(userEmail);

        if(prevRate == null) {
            Rate newRate = new Rate(userEmail, score);
            rates.add(newRate);
        }
        else
            prevRate.setNewScore(score);

        updateRate();
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    private JSONArray makeJsonArray(ArrayList<String> arr) {
        JSONArray jsonArray = new JSONArray();
        for(String e : arr)
            jsonArray.put(e);
        return jsonArray;
    }

    public JSONObject getMovieSummaryInfo() throws JSONException {
        Functions functions = new Functions();
        JSONObject info = functions.createJSONObjectWithOrder();
        info.put("movieId", id);
        info.put("name", name);
        info.put("director", director);
        info.put("genres", makeJsonArray(genres));
        if(ratingCount == null)
            info.put("rating", JSONObject.NULL);
        else
            info.put("rating", ratingCount);
        return info;
    }

    private JSONArray makeCommentJsonArray() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(Comment c : comments) {
            JSONObject newJsonObj = c.getCommentInfo();
            jsonArray.put(newJsonObj);
        }
        return jsonArray;
    }

    private JSONArray makeCastJsonArray() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for(Actor a : casts) {
            JSONObject newJsonObj = a.getActorInfo();
            jsonArray.put(newJsonObj);
        }
        return jsonArray;
    }

    public JSONObject getMovieInfo() throws JSONException {
        Functions functions = new Functions();
        JSONObject info = functions.createJSONObjectWithOrder();
        info.put("movieId", id);
        info.put("name", name);
        info.put("summary", summary);
        info.put("releaseDate", releaseDate);
        info.put("director", director);
        info.put("writers", makeJsonArray(writers));
        info.put("genres", makeJsonArray(genres));
        info.put("cast", makeCastJsonArray());
        if(ratingCount == null)
            info.put("rating", JSONObject.NULL);
        else
            info.put("rating", ratingCount);
        info.put("duration", duration);
        info.put("ageLimit", ageLimit);
        info.put("comments", makeCommentJsonArray());
        return info;
    }
}
