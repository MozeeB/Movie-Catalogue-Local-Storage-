package com.zeeb.moviecataloguelocalstorage.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.adapter.FavMovieAdapter;
import com.zeeb.moviecataloguelocalstorage.adapter.MovieAdapter;
import com.zeeb.moviecataloguelocalstorage.data.local.movie.MovieDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovie extends Fragment {


    @BindView(R.id.rvFavMovie)
    RecyclerView rvFavMovie;
    @BindView(R.id.progressBarFavMov)
    ProgressBar progressBarFavMov;
    Unbinder unbinder;
    private FragmentManager fragmentManager;

//    public static final String KEY_MOVIES = "movies";

    private MovieDatabase movieDatabase;
    private FavMovieAdapter movieAdapter;

    List<ResultsItemMovie> resultsItemMovies = new ArrayList<>();


    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static FavoriteMovie newInstance(FragmentManager fragmentManager) {
        FavoriteMovie fragment = new FavoriteMovie();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    public FavoriteMovie() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoading(true);
        movieDatabase = MovieDatabase.getMovieDatabase(getActivity());
        getFavorite();
        setupRecyclerView();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeFav((long) viewHolder.itemView.getTag());
                resultsItemMovies.remove(resultsItemMovies.get(viewHolder.getAdapterPosition()));
                movieAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(rvFavMovie);
    }

    private void removeFav(long tag) {
        movieDatabase = MovieDatabase.getMovieDatabase(getActivity());
        movieDatabase.movieDao().deleteMovie(tag);
        Toasty.success(getActivity(), R.string.successDel, Toast.LENGTH_SHORT).show();
    }

    public void getFavorite() {
        if (movieDatabase.movieDao().getFavoriteMovie() == null) {
            Toast.makeText(getActivity(), getString(R.string.noFavData), Toast.LENGTH_SHORT).show();
            showLoading(false);
        } else {
            movieDatabase.movieDao().getFavoriteMovie();
            showLoading(false);
        }
    }

    private void setupRecyclerView() {
        resultsItemMovies = movieDatabase.movieDao().getFavoriteNoCursor();
        if (movieAdapter == null) {
            movieAdapter = new FavMovieAdapter(getActivity(), resultsItemMovies);
            rvFavMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvFavMovie.setAdapter(movieAdapter);
            rvFavMovie.setItemAnimator(new DefaultItemAnimator());
            rvFavMovie.setNestedScrollingEnabled(true);
        } else {
            movieAdapter.notifyDataSetChanged();
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarFavMov.setVisibility(View.VISIBLE);
        } else {
            progressBarFavMov.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
