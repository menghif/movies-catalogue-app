package com.example.finalproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonService {

    public ArrayList<Movie> getMoviesFromJSON(String json)  {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");

            for (int i = 0 ; i < jsonArray.length();i++){
                JSONObject movieObject = jsonArray.getJSONObject(i);
                String title = movieObject.getString("Title");
                String posterURL = movieObject.getString("Poster");
                String imdbID = movieObject.getString("imdbID");
                movies.add(new Movie(imdbID, title, posterURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public Movie getMovieFromJSON(String json)  {
        Movie movie = new Movie();
        try {
            JSONObject movieObject = new JSONObject(json);
            String title = movieObject.getString("Title");
            String posterURL = movieObject.getString("Poster");
            String imdbID = movieObject.getString("imdbID");
            String director = movieObject.getString("Director");
            String releaseDate = movieObject.getString("Released");
            String plot = movieObject.getString("Plot");
            movie = new Movie(imdbID, title, posterURL);
            movie.setDirector(director);
            movie.setReleaseDate(releaseDate);
            movie.setPlot(plot);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

}
