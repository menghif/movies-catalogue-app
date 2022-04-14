package com.example.finalproject;

import android.app.Application;

public class myApp extends Application {

    private final NetworkingService networkingService = new NetworkingService();
    private final JsonService jsonService = new JsonService();
    DatabaseManager dbManager = new DatabaseManager();
    MoviesManager manager = new MoviesManager();
    Movie mainMovie = new Movie();

    public NetworkingService getNetworkingService() {
        return networkingService;
    }

    public JsonService getJsonService() {
        return jsonService;
    }

}
