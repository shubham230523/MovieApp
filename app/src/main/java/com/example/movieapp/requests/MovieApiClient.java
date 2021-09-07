package com.example.movieapp.requests;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecutors;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.response.MovieSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    //LiveData

    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    private MutableLiveData<List<MovieModel>> mMoviesPop;
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;

    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    public static MovieApiClient getInstance(){
        if(instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    public MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPop(){
        return mMoviesPop;
    }


    public void searchMovieApi(String query , int pageNumber){

        if(retrieveMoviesRunnable!=null){
            retrieveMoviesRunnable = null;
        }
        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query , pageNumber);
        final Future myHandler = AppExecutors.getInstance().getmNetworkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().getmNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancelling the retrofit calls
                myHandler.cancel(true);
            }
        } , 3000 ,  TimeUnit.MILLISECONDS);
    }

    public void searchMovieApiPop(int pageNumber){

        if(retrieveMoviesRunnablePop!=null){
            retrieveMoviesRunnablePop = null;
        }
        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);
        final Future myHandler2 = AppExecutors.getInstance().getmNetworkIO().submit(retrieveMoviesRunnablePop);

        AppExecutors.getInstance().getmNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancelling the retrofit calls
                myHandler2.cancel(true);
            }
        } , 3000 ,  TimeUnit.MILLISECONDS);
    }


    //Retriving data from restapi by runnable class

    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequests;

            public RetrieveMoviesRunnable(String query, int pageNumber) {
                this.query = query;
                this.pageNumber = pageNumber;
                this.cancelRequests = false;
            }

            @Override
            public void run() {


                try{
                    Response response = getMovies(query , pageNumber).execute();
                    if(cancelRequests){
                        return;
                    }
                    if(response.code() == 200){
                        List<MovieModel> list= new ArrayList<>(((MovieSearchResponse)response.body()).getMovieModelList());
                        if(pageNumber == 1){
                            //Sending data to live data
                            //postvalue : used for background threads
                            //setValue : not for background threads
                            mMovies.postValue(list);
                        }
                        else {
                            List<MovieModel> currentMovies = mMovies.getValue();
                            currentMovies.addAll(list);
                            mMovies.postValue(currentMovies);
                        }
                    }
                    else {
                        String error = response.errorBody().string();
                        Log.v("Tag" , "Error" + error);
                        mMovies.postValue(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mMovies.postValue(null);
                }

            }
            private Call<MovieSearchResponse> getMovies(String query , int pageNumber) {
                return Servicey.getMovieApi().searchMovie(
                        Credentials.API_KEY,
                        query,
                        String.valueOf(pageNumber)
                );
            }
            private void cancelRequests(){
                Log.v("Tag" , "Cancelling Search Requests");
                cancelRequests = true;
            }
        }
    private class RetrieveMoviesRunnablePop implements Runnable{

        private int pageNumber;
        boolean cancelRequests;

        public RetrieveMoviesRunnablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            this.cancelRequests = false;
        }

        @Override
        public void run() {


            try{
                Response response2 = getPop(pageNumber).execute();
                if(cancelRequests){
                    return;
                }
                if(response2.code() == 200){
                    List<MovieModel> list= new ArrayList<>(((MovieSearchResponse)response2.body()).getMovieModelList());
                    if(pageNumber == 1){
                        //Sending data to live data
                        //postvalue : used for background threads
                        //setValue : not for background threads
                        mMoviesPop.postValue(list);
                    }
                    else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                }
                else {
                    String error = response2.errorBody().string();
                    mMoviesPop.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop .postValue(null);
            }

        }
        private Call<MovieSearchResponse> getPop(int pageNumber) {
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void cancelRequests(){
            cancelRequests = true;
        }
    }
    }
