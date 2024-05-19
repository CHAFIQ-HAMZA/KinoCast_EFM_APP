package com.example.kinocastefm_app.recycle_views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinocastefm_app.R;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    ImageView movieImage;
    TextView movieName;
    TextView movieYear;
    TextView movieDuration;
    TextView movieType;
    TextView movieRating;
    TextView movieReleased;

    public MovieViewHolder(@NonNull View itemView, MoviesRecycleViewInterf moviesRecycleViewInterf) {
        super(itemView);
        this.movieImage=itemView.findViewById(R.id.movieImage);
        this.movieName=itemView.findViewById(R.id.movieName);
        this.movieDuration=itemView.findViewById(R.id.movieDuration);
        this.movieType=itemView.findViewById(R.id.movieType);
        this.movieRating=itemView.findViewById(R.id.movieRating);
        this.movieReleased=itemView.findViewById(R.id.movieReleased);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moviesRecycleViewInterf!=null){
                    int pos=getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION){
                        moviesRecycleViewInterf.onMovieClick(pos);
                    }
                }
            }
        });
    }
}
