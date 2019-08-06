package com.zeeb.moviecataloguelocalstorage.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zeeb.moviecataloguelocalstorage.BuildConfig;
import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.data.local.movie.MovieDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<ResultsItemMovie> resultsItemMovies = new ArrayList<>();
    private final Context mContext;

    StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        MovieDatabase movieDatabase = MovieDatabase.getMovieDatabase(mContext);
        resultsItemMovies = movieDatabase.movieDao().getFavoriteNoCursor();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return resultsItemMovies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        try {
           Bitmap bm = Glide.with(mContext)
                       .asBitmap()
                       .load(BuildConfig.URLIMAGE + resultsItemMovies.get(position).getPosterPath())
                        .apply(new RequestOptions().fitCenter()).submit().get();

            rv.setImageViewBitmap(R.id.imageView, bm);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Bundle extras = new Bundle();
        extras.putInt(MovieBanner.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView()  {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
