package IEMDB.Film;

import java.util.ArrayList;

public class WatchList {
    private ArrayList<Movie> movies;

    public WatchList(){
        movies = new ArrayList<Movie>();
    }
    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public boolean checkExistenceOfMovie(Movie movie) {
        if(movies.contains(movie))
            return true;
        return false;
    }

    public void removeFromList(Movie movie) {
        movies.remove(movie);
    }

    public ArrayList<String> getMoviesList() {
        ArrayList<String> data = new ArrayList<>();
        for(Movie m : movies) {
            data.add(m.getMovieInfoForWatchList());
        }
        return data;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

}
