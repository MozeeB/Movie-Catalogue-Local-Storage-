package com.zeeb.moviecataloguelocalstorage.data.local.tvshow;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;

import java.util.List;

@Dao
public interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertTvShow(ResultsItemTvShow resultsItemTvShow);

    @Delete
    void deleteTvShow(ResultsItemTvShow resultsItemTvShow);

    @Query("SELECT * FROM tb_tvshow ORDER BY name ASC")
    List<ResultsItemTvShow> getFavoriteTvShow();

    @Query("SELECT * FROM tb_tvshow WHERE id = :id")
    ResultsItemTvShow selectItem(String id);
}
