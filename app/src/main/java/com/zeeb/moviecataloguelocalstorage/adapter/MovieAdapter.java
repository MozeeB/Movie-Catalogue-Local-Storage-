package com.zeeb.moviecataloguelocalstorage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeeb.moviecataloguelocalstorage.BuildConfig;
import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.activity.DetailMovieActivity;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private List<ResultsItemMovie> resultsItemMovies;


    public MovieAdapter(Context context, List<ResultsItemMovie> resultsItemMovies) {
        this.context = context;
        this.resultsItemMovies = resultsItemMovies;
    }

    public void setResultsItemMovies(List<ResultsItemMovie> resultsItemMovies) {
        this.resultsItemMovies = resultsItemMovies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.judul.setText(resultsItemMovies.get(i).getOriginalTitle());
        viewHolder.score.setText(String.valueOf(resultsItemMovies.get(i).getVoteAverage()));
        Glide.with(context).load(BuildConfig.URLIMAGE + resultsItemMovies.get(i).getPosterPath()).into(viewHolder.img);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRAMOVIE, resultsItemMovies.get(viewHolder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultsItemMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView judul, score;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            judul = itemView.findViewById(R.id.tvJudulMoview);
            score = itemView.findViewById(R.id.tvScoreMovie);
            img = itemView.findViewById(R.id.imgMoview);
        }
    }

}
