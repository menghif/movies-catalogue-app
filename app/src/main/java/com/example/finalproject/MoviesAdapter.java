package com.example.finalproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.TasksViewHolder> {

    interface movieClickListener {
        void movieClicked(Movie selectedMovie);
    }
    private final Context mCtx;
    public List<Movie> moviesList;
    movieClickListener listener;

    public MoviesAdapter(Context mCtx, List<Movie> moviesList) {
        this.mCtx = mCtx;
        this.moviesList = moviesList;
        listener = (movieClickListener)mCtx;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_movies, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Movie t = moviesList.get(position);
        holder.movieTextView.setText(t.getTitle());
        holder.posterImageView.setImageBitmap((t.getPoster()));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView movieTextView;
        ImageView posterImageView;

        public TasksViewHolder(View itemView) {
            super(itemView);

            movieTextView = itemView.findViewById(R.id.movie);
            posterImageView = itemView.findViewById(R.id.poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = moviesList.get(getAdapterPosition());
            listener.movieClicked(movie);
        }
    }


}
