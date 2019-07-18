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

import com.bumptech.glide.Glide;
import com.zeeb.moviecataloguelocalstorage.BuildConfig;
import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.activity.DetailTvShowActivity;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {

    private Context context;
    private List<ResultsItemTvShow> resultsItemTvShowList;


    public TvShowAdapter(Context context, List<ResultsItemTvShow> resultsItemMovies) {
        this.context = context;
        this.resultsItemTvShowList = resultsItemMovies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,  int i) {


        viewHolder.judul.setText(resultsItemTvShowList.get(i).getName());
        viewHolder.score.setText(String.valueOf(resultsItemTvShowList.get(i).getVoteAverage()));
        Glide.with(context).load(BuildConfig.URLIMAGE + resultsItemTvShowList.get(i).getPosterPath()).into(viewHolder.img);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTvShowActivity.class);
                intent.putExtra(DetailTvShowActivity.EXTRATVSHOW, resultsItemTvShowList.get(viewHolder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultsItemTvShowList.size();
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
