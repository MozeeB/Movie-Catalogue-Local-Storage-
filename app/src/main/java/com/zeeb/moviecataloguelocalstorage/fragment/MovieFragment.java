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
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.adapter.MovieAdapter;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;
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
public class MovieFragment extends Fragment implements SearchView.OnQueryTextListener {


    @BindView(R.id.rvMovie)
    RecyclerView rvMovie;
    Unbinder unbinder;
    @BindView(R.id.progressBarMovi)
    ProgressBar progressBarMovi;
    @BindView(R.id.svMovie)
    SearchView svMovie;

    private MovieAdapter movieAdapter;

    List<ResultsItemMovie> resultsItemMovieArrayList = new ArrayList<>();
    MovieViewModel movieViewModel;

    public static final String MOVIE = "extra_movie";


    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        svMovie.setQueryHint("Cari Movie");
        svMovie.setOnQueryTextListener(this);
        svMovie.setIconified(false);
        svMovie.clearFocus();

        showLoading(true);

        getMovie();

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
                if ((response.body() != null ? response.body().getResults() : null) != null){
                    ResponseMovie responseMovie = response.body();
                    resultsItemMovieArrayList = responseMovie.getResults();
                    movieAdapter = new MovieAdapter(getActivity(), resultsItemMovieArrayList);
                    rvMovie.setAdapter(movieAdapter);
                }else {
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

    public void getMovie() {
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.initMovie();
        movieViewModel.getMoviesModel().observe(this, new Observer<ResponseMovie>() {
            @Override
            public void onChanged(@Nullable ResponseMovie responseMovie) {
                if ((responseMovie != null ? responseMovie.getResults() : null) == null) {
                    Toast.makeText(getActivity(), getString(R.string.nodatafound), Toast.LENGTH_SHORT).show();
                    showLoading(false);
                } else {
                    List<ResultsItemMovie> resultsItemMovies = responseMovie.getResults();
                    resultsItemMovieArrayList.addAll(resultsItemMovies);
                    movieAdapter.notifyDataSetChanged();
                    showLoading(false);

                }
            }
        });

    }

    private void setupRecyclerView() {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(getActivity(), resultsItemMovieArrayList);
            rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvMovie.setAdapter(movieAdapter);
            rvMovie.setItemAnimator(new DefaultItemAnimator());
            rvMovie.setNestedScrollingEnabled(true);
        } else {
            movieAdapter.notifyDataSetChanged();
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarMovi.setVisibility(View.VISIBLE);
        } else {
            progressBarMovi.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getMovie();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
