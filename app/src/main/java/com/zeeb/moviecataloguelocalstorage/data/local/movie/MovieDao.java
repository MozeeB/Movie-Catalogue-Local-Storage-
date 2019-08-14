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


    //withcursor
    @Query("SELECT * FROM tb_movie WHERE id = :id")
    Cursor selectItem(long id);

    @Query("SELECT * FROM tb_movie")
    Cursor getFavoriteMovie();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(ResultsItemMovie movie);

    @Query("DELETE FROM tb_movie WHERE  id = :id")
    int deleteById(long id);


    //noCUrsor
    @Query("SELECT * FROM tb_movie")
    List<ResultsItemMovie> getFavoriteNoCursor();

    @Query("SELECT * FROM tb_movie WHERE id = :id")
    ResultsItemMovie selectItemNoCursor(String id);

}
