package com.zeeb.moviecataloguelocalstorage.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.zeeb.moviecataloguelocalstorage.data.local.movie.MovieDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.ViewHolder> {

    private Context context;
    private List<ResultsItemMovie> resultsItemMovies;

    public FavMovieAdapter(Context context, List<ResultsItemMovie> resultsItemMovies) {
        this.context = context;
        this.resultsItemMovies = resultsItemMovies;
    }

    @NonNull
    @Override
    public FavMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_fav, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FavMovieAdapter.ViewHolder viewHolder, int i) {
        viewHolder.judul.setText(resultsItemMovies.get(i).getOriginalTitle());
        viewHolder.score.setText(String.valueOf(resultsItemMovies.get(i).getVoteAverage()));
        Glide.with(context).load(BuildConfig.URLIMAGE + resultsItemMovies.get(i).getPosterPath()).into(viewHolder.img);
        long id = resultsItemMovies.get(i).getId();

        viewHolder.itemView.setTag(id);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                intent.putExtra(DetailMovieActivity.EXTRAMOVIE, resultsItemMovies.get(viewHolder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialog;
                dialog = new AlertDialog.Builder(context);
                dialog.setCancelable(true);
                dialog.setIcon(R.drawable.ic_delete_black_24dp);
                dialog.setTitle(R.string.questionFav);
                dialog.setPositiveButton(R.string.ya, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MovieDatabase movieDatabase = MovieDatabase.getMovieDatabase(context);
                        movieDatabase.movieDao().deleteMovie(resultsItemMovies.get(viewHolder.getAdapterPosition()).getId());
                        Toasty.success(context, R.string.successDel, Toast.LENGTH_SHORT).show();
                        resultsItemMovies.remove(resultsItemMovies.get(viewHolder.getAdapterPosition()));
                        notifyItemRemoved(viewHolder.getAdapterPosition());
                    }
                });
                dialog.setNegativeButton(R.string.tidak, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
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

            judul = itemView.findViewById(R.id.tvFavJudul);
            score = itemView.findViewById(R.id.tvFavScore);
            img = itemView.findViewById(R.id.img_fav);
        }
    }
}
