package com.zeeb.moviecataloguelocalstorage.data.local.movie;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(ResultsItemMovie resultsItemMovie);

    @Delete
    void deleteMovie(ResultsItemMovie resultsItemMovie);

    @Query("SELECT * FROM tb_movie WHERE id = :id")
    ResultsItemMovie selectItem(String id);

    @Query("SELECT * FROM tb_movie")
    List<ResultsItemMovie> getFavoriteMovie();

}
