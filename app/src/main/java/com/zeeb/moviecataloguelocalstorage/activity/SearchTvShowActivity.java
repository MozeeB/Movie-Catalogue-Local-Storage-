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
import com.zeeb.moviecataloguelocalstorage.adapter.TvShowAdapter;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResponseTvShow;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;
import com.zeeb.moviecataloguelocalstorage.network.ApiConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTvShowActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.svMovie)
    SearchView svMovie;

    @BindView(R.id.rvMovie)
    RecyclerView rvMovie;
    @BindView(R.id.progressBarMovi)
    ProgressBar progressBarMovi;


    private TvShowAdapter tvShowAdapter;
    List<ResultsItemTvShow> resultsItemTvShowArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_movie);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);


        svMovie.setQueryHint(getString(R.string.search_tv_show));
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
        ApiConfig.getInitRetrofit().searchTvShow(newText).enqueue(new Callback<ResponseTvShow>() {
            @Override
            public void onResponse(Call<ResponseTvShow> call, Response<ResponseTvShow> response) {
                showLoading(false);
                rvMovie.setVisibility(View.VISIBLE);
                if ((response.body() != null ? response.body().getResults() : null) != null){
                    ResponseTvShow responseTvShow = response.body();
                    resultsItemTvShowArrayList = responseTvShow.getResults();
                    tvShowAdapter = new TvShowAdapter(SearchTvShowActivity.this, resultsItemTvShowArrayList);
                    rvMovie.setAdapter(tvShowAdapter);

                }else {
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseTvShow> call, Throwable t) {
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
        if (tvShowAdapter == null) {
            tvShowAdapter = new TvShowAdapter(this, resultsItemTvShowArrayList);
            rvMovie.setLayoutManager(new LinearLayoutManager(this));
            rvMovie.setAdapter(tvShowAdapter);
            rvMovie.setItemAnimator(new DefaultItemAnimator());
            rvMovie.setNestedScrollingEnabled(true);
        } else {
            tvShowAdapter.notifyDataSetChanged();
        }
    }
}
