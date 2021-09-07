package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movieapp.Models.MovieModel;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView img;
    private TextView overview , title , release_date;
    private RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        img = findViewById(R.id.imageView_details);
        overview = findViewById(R.id.overview_details);
        title = findViewById(R.id.movie_title_details);
        ratingBar = findViewById(R.id.ratingBar_details);
        release_date = findViewById(R.id.Movie_details_releaseDate);

        GetDataFromIntent();

    }

    private void GetDataFromIntent() {
        if(getIntent().hasExtra("movie")){
            MovieModel movieModel= getIntent().getParcelableExtra("movie");
            title.setText(movieModel.getTitle());
            overview.setText(movieModel.getOverview());
            ratingBar.setRating(movieModel.getVote_average()/2);
            release_date.setText(movieModel.getRelease_date());

            Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movieModel.getPoster_path()).into(img);

        }
    }
}