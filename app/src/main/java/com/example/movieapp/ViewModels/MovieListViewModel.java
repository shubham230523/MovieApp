package com.example.movieapp.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.Repository.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {



    private MovieRepository movieRepository;
    //constructor


    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
        return movieRepository.getPop();
    }

    public void searchMovieApi(String query , int pageNumber){
        movieRepository.searchMovieApi(query , pageNumber);
    }

    public void searchMovieApiPop(int pageNumber){
        movieRepository.searchMoviePop(pageNumber);
    }

    public void searcNextPage(){
        movieRepository.searcNextPage();
    }
}





