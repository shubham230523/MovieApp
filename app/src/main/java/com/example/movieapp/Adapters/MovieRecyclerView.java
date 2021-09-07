package com.example.movieapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.Models.MovieModel;
import com.example.movieapp.R;
import com.example.movieapp.Utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;
    private static final int DISPLAY_POP = 1;
    private static final int DISPLAY_SEARCH = 2;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = null;
        if(Credentials.POPULAR){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_layout , parent , false);
            return new PopularViewHolder(view , onMovieListener);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item , parent , false);
            return new MovieViewHolder( view , onMovieListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if(getItemViewType(position) != DISPLAY_SEARCH){
            ((PopularViewHolder)holder).ratingBarPop.setRating(mMovies.get(position).getVote_average()/2);
            Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500" +
                    mMovies.get(position).getPoster_path()).into(((PopularViewHolder) holder).imageViewPop);


        }
        else {

            ((MovieViewHolder)holder).duration.setText(mMovies.get(position).getVote_average()+"");
            ((MovieViewHolder)holder).release_date.setText(mMovies.get(position).getRelease_date());


            ((MovieViewHolder)holder).ratingBar.setRating((mMovies.get(position).getVote_average())/2);


            Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500" +
                    mMovies.get(position).getPoster_path()).into(((MovieViewHolder) holder).imageView);
        }

    }

    @Override
    public int getItemCount() {
        if(mMovies!=null){
            return mMovies.size();
        }
        return 0;
    }

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    public MovieModel getSelectedMovie(int position){
        if(mMovies!=null){
            if(mMovies.size()>0){
                return mMovies.get(position);
            }
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if(Credentials.POPULAR){
            return DISPLAY_POP;
        }
        else return DISPLAY_SEARCH;

    }
}
