package com.example.movieapp.response;

import com.example.movieapp.Models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// This is for getting one movie

public class MovieResponse {

    @SerializedName("results")
    @Expose
    private MovieModel movie;

    public MovieModel getMovie(){
        return movie;
    }

    @Override
    public String toString() {
        return "ParticularSearchResponse{" +
                "movie=" + movie +
                '}';
    }
}
