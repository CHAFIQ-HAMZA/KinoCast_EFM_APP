package com.example.kinocastefm_app.recycle_views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kinocastefm_app.R;
import com.example.kinocastefm_app.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    Context context;
    List<Movie> movies;
    private final MoviesRecycleViewInterf moviesRecycleViewInterf;
    public MovieAdapter(Context context, List<Movie> movies, MoviesRecycleViewInterf moviesRecycleViewInterf) {
        this.context = context;
        this.movies = movies;
        this.moviesRecycleViewInterf = moviesRecycleViewInterf;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_view,parent,false), moviesRecycleViewInterf);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Glide.with(context).load(movies.get(position).poster).into(holder.movieImage);
        holder.movieName.setText(movies.get(position).title);
        holder.movieDuration.setText(" "+movies.get(position).duraton);
        holder.movieType.setText(movies.get(position).genre);
        holder.movieRating.setText(" "+movies.get(position).rating);
        holder.movieReleased.setText(" "+movies.get(position).released);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
