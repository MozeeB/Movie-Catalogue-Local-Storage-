package com.zeeb.moviecataloguelocalstorage.data.local.movie;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie( ResultsItemMovie itemMovie);

    @Query("DELETE FROM tb_movie WHERE id = :id")
    void deleteMovie(long id);

    @Query("SELECT * FROM tb_movie WHERE id = :id")
    Cursor selectItem(long id);

    @Query("SELECT * FROM tb_movie")
    Cursor getFavoriteMovie();

    @Query("SELECT * FROM tb_movie")
    List<ResultsItemMovie> getFavoriteNoCursor();

    @Query("SELECT * FROM tb_movie WHERE id = :id")
    ResultsItemMovie selectItemNoCursor(String id);

}
