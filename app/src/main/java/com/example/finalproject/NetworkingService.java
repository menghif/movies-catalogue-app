package com.example.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkingService {

    // URL to get list of movies by title
    String movieTitleURL = "https://www.omdbapi.com/?s=";
    // URL to get single movie by ID
    String movieIdURL = "https://www.omdbapi.com/?i=";
    String APIkey = "30391803";
    String APIkeyURL = "&apikey=" + APIkey;

    public static ExecutorService networkExecutorService = Executors.newFixedThreadPool(4);
    public static Handler networkingHandler = new Handler(Looper.getMainLooper());

    interface NetworkingListener{
        void dataListener(String jsonString);
        void imageListener(Bitmap image, int index);
    }

    public NetworkingListener listener;

    public void searchForMovies(String movieChars){
        String urlString = movieTitleURL + movieChars + APIkeyURL;
        connect(urlString);
    }

    public void searchForMovie(String movieID){
        String urlString = movieIdURL + movieID + APIkeyURL;
        connect(urlString);
    }

    public void getImageData(String iconUrl, int index){
        networkExecutorService.execute(() -> {
            try {
                URL url = new URL(iconUrl);
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)url.getContent()) ;
                networkingHandler.post(() -> listener.imageListener(bitmap, index));

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public void connect(String url){
        networkExecutorService.execute(() -> {
            HttpURLConnection httpURLConnection = null;
            // code here will run in background thread
            try {
                StringBuilder jsonString = new StringBuilder();
                URL urlObject = new URL(url);
                httpURLConnection= (HttpURLConnection)urlObject.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type","application/json");

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int inputStreamData;
                while ( (inputStreamData = reader.read()) != -1){
                    char current = (char)inputStreamData;
                    jsonString.append(current);
                }

                final String finalJsonString = jsonString.toString();
                networkingHandler.post(() -> listener.dataListener(finalJsonString));

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert httpURLConnection != null;
                httpURLConnection.disconnect();
            }
        });
    }

}
