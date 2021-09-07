package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.movieapp.Adapters.MovieRecyclerView;
import com.example.movieapp.Adapters.OnMovieListener;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.Utils.Credentials;
import com.example.movieapp.Utils.MovieApi;
import com.example.movieapp.ViewModels.MovieListViewModel;
import com.example.movieapp.requests.Servicey;
import com.example.movieapp.response.MovieResponse;
import com.example.movieapp.response.MovieSearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMovieListener {


    private RecyclerView recyclerView;
    private MovieRecyclerView adapter;
    boolean isPopular = true;

    //View Model
    private MovieListViewModel movieListViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    movieListViewModel.searcNextPage();
                }
            }
        });
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpSearchView();

        ConfigureRecyclerView();
        //Calling the observer
        ObserveAnyChange();
        ObservePopularMovies();

        movieListViewModel.searchMovieApiPop(1);

    }

    private void ObservePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change

                if(movieModels!=null){
                    for(MovieModel movieModel : movieModels){
                        Log.d("Tag" , "title of the movie " + movieModel.getTitle());
                        adapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }

    private void setUpSearchView() {
        final SearchView searchView = findViewById(R.id.Search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Credentials.POPULAR = false;
                movieListViewModel.searchMovieApi(
                        query,
                        1
                );
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopular = false;
            }
        });
    }

    // Adding the observer for listening to any data change

    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change

                if(movieModels!=null){
                    for(MovieModel movieModel : movieModels){
                        adapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }

    private void getRetrofitResponse() {
        MovieApi movieApi = Servicey.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
                Credentials.API_KEY,
                "Jack Reacher",
                "1"
        );
         responseCall.enqueue(new Callback<MovieSearchResponse>() {
             @Override
             public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                 if(response.code() == 200){
                     Log.v("Tag" , "response " + response.body().toString());
                     List<MovieModel> movies = new ArrayList<>(response.body().getMovieModelList());
                     for(MovieModel m : movies){
                         Log.v("Tag" , "The title " + m.getTitle());
                     }
                 }
                 else {
                     try {
                         Log.v("Tag" , "Error" + response.errorBody().string());
                     } catch (IOException e) {
                         e.printStackTrace();
                     }

                 }
             }

             @Override
             public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

             }
         });
    }

    private void GetRetrofitResponseAccordingToId(){
        MovieApi movieApi = Servicey.getMovieApi();
        Call<MovieModel> responeCall = movieApi.getMovie(
                550,
                Credentials.API_KEY
        );
         responeCall.enqueue(new Callback<MovieModel>() {
             @Override
             public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                 if(response.code() == 200){
                     MovieModel  movie = response.body();
                     Log.v("Tag" ,"The response " + movie.getTitle());

                 }
                 else {
                     try{
                         Log.v("Tag" , "Error" + response.errorBody().string());
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
             }

             @Override
             public void onFailure(Call<MovieModel> call, Throwable t) {

             }
         });
    }


    private void ConfigureRecyclerView(){
        adapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
    }

    @Override
    public void onMovieClick(int position) {

        Intent intent = new Intent(this , MovieDetailActivity.class);
        intent.putExtra("movie"  ,adapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }
}