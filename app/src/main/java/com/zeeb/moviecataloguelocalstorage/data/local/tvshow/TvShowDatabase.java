package com.zeeb.moviecataloguelocalstorage.data.local.tvshow;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;

@Database(entities = ResultsItemTvShow.class, version = 1)
public abstract class TvShowDatabase extends RoomDatabase {

    public abstract TvShowDao tvShowDao();
    private static TvShowDatabase tvShowDatabase;

    public static TvShowDatabase getTvShowDatabase(Context context){
        synchronized (TvShowDatabase.class){
            if (tvShowDatabase == null){
                tvShowDatabase = Room.databaseBuilder(context, TvShowDatabase.class, "db_tvshow").allowMainThreadQueries().build();
            }
        }return tvShowDatabase;
    }

}
