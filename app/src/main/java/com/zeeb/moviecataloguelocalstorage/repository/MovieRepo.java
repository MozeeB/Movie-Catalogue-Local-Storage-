package com.zeeb.moviecataloguelocalstorage.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.zeeb.moviecataloguelocalstorage.data.local.movie.MovieDao;
import com.zeeb.moviecataloguelocalstorage.data.local.movie.MovieDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;
import com.zeeb.moviecataloguelocalstorage.network.ApiConfig;
import com.zeeb.moviecataloguelocalstorage.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepo {

    private static MovieRepo movieRepo;

    public static MovieRepo getInstancemovie(){
        if (movieRepo == null){
            movieRepo = new MovieRepo();

        }
        return  movieRepo;
    }

    private ApiService apiService;

    private MovieRepo(){
        apiService = ApiConfig.getInitRetrofit();
    }

    public MutableLiveData<ResponseMovie> getMovies(){
        final MutableLiveData<ResponseMovie> movieData = new MutableLiveData<>();
        apiService.getDataMovie().enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
               if ((response.body() != null ? response.body().getPage() : 0) > 0){
                   movieData.setValue(response.body());
               }
            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {
                movieData.setValue(null);
            }
        });

        return movieData;
    }

}