package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.movieClickListener,
        NetworkingService.NetworkingListener, DatabaseManager.DatabaseListener{

    ArrayList<Movie> movies = new ArrayList<>(0);
    ArrayList<Bitmap> posters  = new ArrayList<>(0);
    RecyclerView recyclerView;
    MoviesAdapter adapter;
    NetworkingService networkingManager;
    JsonService jsonService;
    ImageView imageView;

    DatabaseManager dbManager;
    MoviesManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.poster);
        networkingManager = ((myApp)getApplication()).getNetworkingService();
        jsonService = ((myApp)getApplication()).getJsonService();
        networkingManager.listener = this;
        recyclerView = findViewById(R.id.moviesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MoviesAdapter(this, movies);
        recyclerView.setAdapter(adapter);

        dbManager = ((myApp)getApplication()).dbManager;
        manager = ((myApp)getApplication()).manager;
        dbManager.getDb(this);
        dbManager.listener = this;
        dbManager.getAllMovies();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.options_menu, menu);

        SearchView searchView = findViewById(R.id.searchBar);
        searchView.setIconifiedByDefault(false);
                String searchFor = searchView.getQuery().toString();
        if (!searchFor.isEmpty()) {
            searchView.setQuery(searchFor, false);
            searchView.clearFocus();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {// when the user clicks enter
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 3) {
                    // search for movies
                    posters = new ArrayList<>(0);
                    networkingManager.searchForMovies(newText);
                }
                else {
                    movies = new ArrayList<>(0);
                    adapter.moviesList = movies;
                    adapter.notifyDataSetChanged();

                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void dataListener(String jsonString) {
        movies = jsonService.getMoviesFromJSON(jsonString);
        for (int i = 0; i < movies.size(); i++) {
            Log.d(movies.get(i).getTitle(), movies.get(i).getPosterURL());
            networkingManager.getImageData(movies.get(i).getPosterURL(), i);
        }
        adapter = new MoviesAdapter(this, movies);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void imageListener(Bitmap image, int index) {
        if (index < movies.size()) {
            movies.get(index).setPoster(image);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void movieClicked(Movie selectedMovie) {
        ((myApp)getApplication()).mainMovie = selectedMovie;
        Log.d(String.valueOf(((myApp)getApplication()).mainMovie.isFavorite()), "is Movie Favorite: ");
        Intent myIntent = new Intent(this, MovieInfoActivity.class);
        startActivity(myIntent);
    }

    //Setting Listeners to Menu options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.favorites: {
                Intent intent = new Intent(this,FavoritesActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.deleteAllFavorites: {
                dbManager.deleteAllMovies();
                Toast.makeText(this,"Favorites List Deleted", Toast.LENGTH_LONG).show();
                break;
            }
        }
        return true;
    }

    @Override
    public void onListReady(List<Movie> list) {
        manager.allFavorites = (ArrayList<Movie>) list;
    }

    @Override
    public void onAddDone() {
        dbManager.getAllMovies();
    }

    @Override
    public void onDeleteDone() {
    }
}