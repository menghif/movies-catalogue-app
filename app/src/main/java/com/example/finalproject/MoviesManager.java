package com.example.finalproject;

import java.util.ArrayList;

public class MoviesManager {
    ArrayList<Movie> allFavorites = new ArrayList<>(0);
    public void addMovie(Movie m){
        allFavorites.add(m);
    }
}
