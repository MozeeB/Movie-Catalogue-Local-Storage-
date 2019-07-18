package com.zeeb.moviecataloguelocalstorage.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.adapter.TvShowAdapter;
import com.zeeb.moviecataloguelocalstorage.data.local.tvshow.TvShowDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShow extends Fragment {

    @BindView(R.id.rvFavTvShow)
    RecyclerView rvFavTvShow;
    @BindView(R.id.progressBarFavTvShow)
    ProgressBar progressBarFavTvShow;
    Unbinder unbinder;
    private FragmentManager fragmentManager;

    ArrayList<ResultsItemTvShow> resultsItemMovieArrayList = new ArrayList<>();
    TvShowDatabase tvShowDatabase;
    TvShowAdapter adapter;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static FavoriteTvShow newInstance(FragmentManager fragmentManager) {
        FavoriteTvShow fragment = new FavoriteTvShow();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    public FavoriteTvShow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showLoading(true);
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(getActivity());
        getFavorite();
        setupRecyclerView();
    }

    public void getFavorite() {
        if (tvShowDatabase.tvShowDao().getFavoriteTvShow() != null) {
            tvShowDatabase.tvShowDao().getFavoriteTvShow();
            showLoading(false);
        } else {
            Toast.makeText(getActivity(), "No favorite data here!", Toast.LENGTH_SHORT).show();
            showLoading(false);
        }
    }

    private void setupRecyclerView() {
        if (adapter == null) {
            adapter = new TvShowAdapter(getActivity(), resultsItemMovieArrayList);
            rvFavTvShow.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvFavTvShow.setAdapter(adapter);
            rvFavTvShow.setItemAnimator(new DefaultItemAnimator());
            rvFavTvShow.setNestedScrollingEnabled(true);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarFavTvShow.setVisibility(View.VISIBLE);
        } else {
            progressBarFavTvShow.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
