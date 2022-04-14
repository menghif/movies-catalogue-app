package com.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements MoviesAdapter.movieClickListener,
        DatabaseManager.DatabaseListener, NetworkingService.NetworkingListener {

    RecyclerView favoritesList;
    MoviesAdapter adapter;
    DatabaseManager dbManager;
    ArrayList<Movie> favoriteMovies = new ArrayList<>();
    NetworkingService networkingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        favoritesList = findViewById(R.id.favoritesList);
        MoviesManager manager = ((myApp)getApplication()).manager;
        dbManager = ((myApp)getApplication()).dbManager;
        dbManager.listener = this;
        adapter = new MoviesAdapter(this, manager.allFavorites);
        favoritesList.setLayoutManager(new LinearLayoutManager(this));
        favoritesList.setAdapter(adapter);

        networkingManager = ((myApp)getApplication()).getNetworkingService();
        networkingManager.listener = this;
        dbManager.getAllMovies();

        for (int i = 0; i < favoriteMovies.size(); i++) {
            networkingManager.getImageData(favoriteMovies.get(i).getPosterURL(), i);
            Log.d(favoriteMovies.get(i).getTitle(), "onListReady: ");
        }
    }

    @Override
    public void movieClicked(Movie selectedMovie) {
        ((myApp)getApplication()).mainMovie = selectedMovie;
        Intent myIntent = new Intent(this, MovieInfoActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onListReady(List<Movie> list) {
        favoriteMovies = new ArrayList<>(list);
        adapter.moviesList = favoriteMovies;
        for (int i = 0; i < favoriteMovies.size(); i++) {
            networkingManager.getImageData(favoriteMovies.get(i).getPosterURL(), i);
            Log.d(favoriteMovies.get(i).getTitle(), "onListReady: ");
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddDone() {
    }

    @Override
    public void onDeleteDone() {
    }

    @Override
    public void dataListener(String jsonString) {

    }

    @Override
    public void imageListener(Bitmap image, int index) {
        if (index < favoriteMovies.size()) {
            favoriteMovies.get(index).setPoster(image);
            adapter.notifyDataSetChanged();
        }
    }
}
