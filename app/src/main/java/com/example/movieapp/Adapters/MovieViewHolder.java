package com.example.movieapp.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title , duration , release_date ;
    ImageView imageView;
    RatingBar ratingBar;

    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView , OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;
        title  = itemView.findViewById(R.id.movie_title_details);
        release_date  = itemView.findViewById(R.id.movie_category);
        duration  = itemView.findViewById(R.id.movie_duration);
        imageView  = itemView.findViewById(R.id.CardViewImageView);
        ratingBar  = itemView.findViewById(R.id.rating_bar);


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
