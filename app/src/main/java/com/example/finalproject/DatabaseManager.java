package com.example.finalproject;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    interface DatabaseListener {
        void onListReady (List<Movie> list);
        void onAddDone();
        void onDeleteDone();
    }

    DatabaseListener listener;
    static MoviesDataBase db;
    ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(4);
    Handler mainThread_Handler = new Handler(Looper.getMainLooper());

    private static void buildDBInstance(Context context){
        db = Room.databaseBuilder(context,
                MoviesDataBase.class, "movies_db").build();
    }

    public MoviesDataBase getDb(Context context){
        if (db == null)
            buildDBInstance(context);
        return db;
    }

    public void saveNewMovie(Movie newMovie){
        databaseExecutor.execute(() -> {
            db.moviesDao().addNewMovieToDB(newMovie);
            mainThread_Handler.post(() -> listener.onAddDone());
        });
    }

    public  void getAllMovies(){
        databaseExecutor.execute(() -> {
            List<Movie> list =  db.moviesDao().getAll();
            // go to main thread
            mainThread_Handler.post(() -> listener.onListReady(list));
        });
    }

    public  void deleteAllMovies(){
        databaseExecutor.execute(() -> {
            db.moviesDao().deleteAll();
            // go to main thread
            mainThread_Handler.post(() -> {
            });
        });
    }

    public void deleteMovie(Movie toDelete){
        databaseExecutor.execute(() -> {
            db.moviesDao().deleteMovie(toDelete);
            // go to main thread
            mainThread_Handler.post(() -> listener.onDeleteDone());
        });
    }

}
