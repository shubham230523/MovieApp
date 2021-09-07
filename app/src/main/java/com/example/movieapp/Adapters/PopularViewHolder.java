package com.example.movieapp.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;

public class PopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imageViewPop;
    RatingBar ratingBarPop;

    OnMovieListener onMovieListener;

    public PopularViewHolder(@NonNull View itemView , OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;
        imageViewPop  = itemView.findViewById(R.id.movie_list_item_img);
        ratingBarPop  = itemView.findViewById(R.id.rating_bar_list_item);


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
