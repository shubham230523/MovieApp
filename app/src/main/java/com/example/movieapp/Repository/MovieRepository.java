package com.example.movieapp.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.requests.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;
    private static MovieApiClient movieApiClient;
    private String mQuery;
    private int mPageNumber;


    public static MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
        movieApiClient = new MovieApiClient().getInstance();
    }
    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
        return movieApiClient.getMoviesPop();
    }

    public void searchMovieApi(String query , int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMovieApi(query , pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMovieApiPop(pageNumber);
    }

    public void searcNextPage(){
        searchMovieApi(mQuery , mPageNumber+1);
    }
}
