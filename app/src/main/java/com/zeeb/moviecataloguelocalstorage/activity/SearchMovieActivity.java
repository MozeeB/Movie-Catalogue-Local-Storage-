package com.zeeb.moviecataloguelocalstorage.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.adapter.MovieAdapter;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;
import com.zeeb.moviecataloguelocalstorage.network.ApiConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.svMovie)
    SearchView svMovie;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.rvMovie)
    RecyclerView rvMovie;
    @BindView(R.id.progressBarMovi)
    ProgressBar progressBarMovi;

    private MovieAdapter movieAdapter;

    List<ResultsItemMovie> resultsItemMovieArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);
        ButterKnife.bind(this);

        svMovie.setQueryHint(getString(R.string.search_movie));
        svMovie.setOnQueryTextListener(this);
        svMovie.setIconified(false);
        svMovie.clearFocus();

        setupRecyclerView();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        rvMovie.setVisibility(View.GONE);
        showLoading(true);

        ApiConfig.getInitRetrofit().searchMovie(newText).enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                showLoading(false);
                rvMovie.setVisibility(View.VISIBLE);
                if ((response.body() != null ? response.body().getResults() : null) != null) {
                    ResponseMovie responseMovie = response.body();
                    resultsItemMovieArrayList = responseMovie.getResults();
                    movieAdapter = new MovieAdapter(SearchMovieActivity.this, resultsItemMovieArrayList);
                    rvMovie.setAdapter(movieAdapter);
                } else {
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {
                showLoading(false);
            }
        });
        return true;
    }


    private void showLoading(Boolean state) {
        if (state) {
            progressBarMovi.setVisibility(View.VISIBLE);
        } else {
            progressBarMovi.setVisibility(View.GONE);
        }
    }


    private void setupRecyclerView() {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(this, resultsItemMovieArrayList);
            rvMovie.setLayoutManager(new LinearLayoutManager(this));
            rvMovie.setAdapter(movieAdapter);
            rvMovie.setItemAnimator(new DefaultItemAnimator());
            rvMovie.setNestedScrollingEnabled(true);
        } else {
            movieAdapter.notifyDataSetChanged();
        }
    }
}
