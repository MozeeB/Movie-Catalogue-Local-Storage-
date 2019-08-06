package com.zeeb.moviecataloguelocalstorage.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.adapter.TvShowAdapter;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResponseTvShow;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;
import com.zeeb.moviecataloguelocalstorage.network.ApiConfig;
import com.zeeb.moviecataloguelocalstorage.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment implements SearchView.OnQueryTextListener {


    @BindView(R.id.rvTvShow)
    RecyclerView rvTvShow;
    Unbinder unbinder;
    @BindView(R.id.svTvShow)
    SearchView svTvShow;
    private TvShowAdapter tvShowAdapter;
    List<ResultsItemTvShow> resultsItemTvShowArrayList = new ArrayList<>();

    MovieViewModel movieViewModel;
    @BindView(R.id.progressBarTvSho)
    ProgressBar progressBarTvSho;

    public static final String TVSHOW = "extra_tvshow";


    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        svTvShow.setQueryHint("Cari Tv Show");
        svTvShow.setOnQueryTextListener(this);
        svTvShow.setIconified(false);
        showLoading(true);

        getTvShow();

        setupRecyclerView();


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        rvTvShow.setVisibility(View.GONE);
        showLoading(true);
        ApiConfig.getInitRetrofit().searchTvShow(newText).enqueue(new Callback<ResponseTvShow>() {
            @Override
            public void onResponse(Call<ResponseTvShow> call, Response<ResponseTvShow> response) {
                showLoading(false);
                rvTvShow.setVisibility(View.VISIBLE);
                if ((response.body() != null ? response.body().getResults() : null) != null){
                    ResponseTvShow responseTvShow = response.body();
                    resultsItemTvShowArrayList = responseTvShow.getResults();
                    tvShowAdapter = new TvShowAdapter(getActivity(), resultsItemTvShowArrayList);
                    rvTvShow.setAdapter(tvShowAdapter);

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

    public void getTvShow() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.initTvShow();
        movieViewModel.getTvShowsModel().observe(this, new Observer<ResponseTvShow>() {
            @Override
            public void onChanged(@Nullable ResponseTvShow responseTvShow) {
                if ((responseTvShow != null ? responseTvShow.getResults() : null) == null) {
                    Toast.makeText(getActivity(), R.string.nodatafound, Toast.LENGTH_SHORT).show();
                    showLoading(false);
                } else {
                    List<ResultsItemTvShow> resultsItemTvShows = responseTvShow.getResults();
                    resultsItemTvShowArrayList.addAll(resultsItemTvShows);
                    tvShowAdapter.notifyDataSetChanged();
                    showLoading(false);
                }

            }
        });
    }

    private void setupRecyclerView() {
        if (tvShowAdapter == null) {
            tvShowAdapter = new TvShowAdapter(getActivity(), resultsItemTvShowArrayList);
            rvTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvTvShow.setAdapter(tvShowAdapter);
            rvTvShow.setItemAnimator(new DefaultItemAnimator());
            rvTvShow.setNestedScrollingEnabled(true);
        } else {
            tvShowAdapter.notifyDataSetChanged();
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarTvSho.setVisibility(View.VISIBLE);
        } else {
            progressBarTvSho.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getTvShow();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
