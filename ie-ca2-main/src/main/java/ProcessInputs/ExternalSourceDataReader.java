package ProcessInputs;

import org.json.simple.parser.ParseException;

import java.io.IOException;;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;


import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExternalSourceDataReader {

    public Map<String, ArrayList<String>> getData() throws IOException, InterruptedException, ParseException {
        Map<String, ArrayList<String>> data = new HashMap<>();
        data.put("movies", getDataFromService("http://138.197.181.131:5000/api/movies"));
        data.put("actors", getDataFromService("http://138.197.181.131:5000/api/actors"));
        data.put("users", getDataFromService("http://138.197.181.131:5000/api/users"));
        data.put("comments", getDataFromService("http://138.197.181.131:5000/api/comments"));
        return data;
    }

    private String getDataFromURL(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private ArrayList<String> getDataFromService(String url) throws IOException, InterruptedException {
        String jsonData = getDataFromURL(url);
        ArrayList<String> movies = new ArrayList<>();
        jsonData = jsonData.substring(1, jsonData.length() - 1);
        String[] movies_list = jsonData.split("}, ");
        for (String e : movies_list) {
            e = e + "}";
            movies.add(e);
        }
        return movies;
    }
}

