package com.zeeb.moviecataloguelocalstorage.network;

import com.zeeb.moviecataloguelocalstorage.BuildConfig;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResponseTvShow;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("discover/movie?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<ResponseMovie> getDataMovie();

    @GET("discover/tv?api_key=" + BuildConfig.API_KEY + "&language=en-US")
    Call<ResponseTvShow> getTvShowData();
}
