package com.example.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1)
public abstract class MoviesDataBase extends RoomDatabase {
    public abstract MoviesDAO moviesDao();
}
