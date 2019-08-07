package com.zeeb.moviecataloguelocalstorage.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResponseTvShow;
import com.zeeb.moviecataloguelocalstorage.repository.MovieRepo;
import com.zeeb.moviecataloguelocalstorage.repository.TvShowRepo;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<ResponseMovie> responseMovieMutableLiveData;

    public void initMovie() {
        if (responseMovieMutableLiveData != null) {
            return;
        }
        MovieRepo movieRepo = MovieRepo.getInstancemovie();
        responseMovieMutableLiveData = movieRepo.getMovies();
    }

    public LiveData<ResponseMovie> getMoviesModel() {
        return responseMovieMutableLiveData;
    }


    public void initSearchMovie(String text){
        if (responseMovieMutableLiveData != null){
            return;
        }
        MovieRepo movieRepo = MovieRepo.getInstancemovie();
        responseMovieMutableLiveData = movieRepo.searchMovie(text);
    }
    public LiveData<ResponseMovie> searchMovie(String judul){
        return responseMovieMutableLiveData;
    }

    //TVSHOW
    private MutableLiveData<ResponseTvShow> responseTvShowMutableLiveData;

    public void initTvShow() {
        if (responseTvShowMutableLiveData != null) {
            return;
        }
        TvShowRepo tvShowRepo = TvShowRepo.getInstance();
        responseTvShowMutableLiveData = tvShowRepo.getTVShow();
    }

    public LiveData<ResponseTvShow> getTvShowsModel() {
        return responseTvShowMutableLiveData;
    }
}
