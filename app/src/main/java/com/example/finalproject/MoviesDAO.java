package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoviesDAO {
    @Insert
    void addNewMovieToDB(Movie newMovie);

    @Delete
    void deleteMovie(Movie toDeleteMovie);

    @Query("SELECT * FROM Movie")
    List<Movie> getAll();

    @Query("DELETE FROM Movie")
    void deleteAll();
}
