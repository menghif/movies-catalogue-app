package com.example.finalproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MovieInfoActivity  extends AppCompatActivity implements
        NetworkingService.NetworkingListener, DatabaseManager.DatabaseListener, View.OnClickListener{
    TextView movieTitle;
    TextView director;
    TextView releaseDate;
    TextView plot;

    ImageView moviePoster;
    NetworkingService networkingManager;
    JsonService jsonService;

    DatabaseManager dbManager;
    Movie movieObj;

    Button addToFavorites;
    Button removeFromFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        dbManager = ((myApp)getApplication()).dbManager;
        dbManager.listener = this;

        jsonService = ((myApp)getApplication()).getJsonService();
        networkingManager = ((myApp)getApplication()).getNetworkingService();
        networkingManager.listener = this;

        movieObj = ((myApp)getApplication()).mainMovie;
        movieTitle = findViewById(R.id.movieTitle);
        movieTitle.setText(movieObj.getTitle());
        director = findViewById(R.id.director);
        releaseDate = findViewById(R.id.releaseDate);
        plot = findViewById(R.id.plot);

        networkingManager.searchForMovie(movieObj.getImdbID());

        networkingManager.getImageData(movieObj.getPosterURL(), 0);

        addToFavorites = findViewById(R.id.addToFavorites);
        addToFavorites.setOnClickListener(this);

        removeFromFavorites = findViewById(R.id.removeFromFavorites);
        removeFromFavorites.setOnClickListener(this);

        if (movieObj.isFavorite()) {
            addToFavorites.setVisibility(View.GONE);
            removeFromFavorites.setVisibility(View.VISIBLE);
        } else {
            addToFavorites.setVisibility(View.VISIBLE);
            removeFromFavorites.setVisibility(View.GONE);
        }

    }

    @Override
    public void dataListener(String jsonString) {
        Movie movie = jsonService.getMovieFromJSON(jsonString);
        director.setText(movie.getDirector());
        releaseDate.setText(movie.getReleaseDate());
        plot.setText(movie.getPlot());
    }

    @Override
    public void imageListener(Bitmap image, int index) {
        moviePoster = findViewById(R.id.posterImage);
        moviePoster.setImageBitmap(image);
    }

    @Override
    public void onListReady(List<Movie> list) {
    }

    @Override
    public void onAddDone() {
    }

    @Override
    public void onDeleteDone() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addToFavorites:
                movieObj.setFavorite(true);
                dbManager.saveNewMovie(movieObj);
                dbManager.getAllMovies();
                Log.d(movieObj.getTitle(), "movieClicked: ");
                addToFavorites.setVisibility(View.GONE);
                removeFromFavorites.setVisibility(View.VISIBLE);
                break;

            case R.id.removeFromFavorites:
                movieObj.setFavorite(false);
                dbManager.deleteMovie(movieObj);
                dbManager.getAllMovies();
                Log.d(movieObj.getTitle(), "movieClicked: ");
                removeFromFavorites.setVisibility(View.GONE);
                addToFavorites.setVisibility(View.VISIBLE);
                break;
        }
    }
}
