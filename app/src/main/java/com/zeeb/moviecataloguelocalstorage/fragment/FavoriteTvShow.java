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
import com.zeeb.moviecataloguelocalstorage.adapter.FavTvShowAdapater;
import com.zeeb.moviecataloguelocalstorage.data.local.tvshow.TvShowDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

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

    List<ResultsItemTvShow> resultsItemMovieArrayList = new ArrayList<>();
    TvShowDatabase tvShowDatabase;
    FavTvShowAdapater adapter;

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeFav((long) viewHolder.itemView.getTag());
                resultsItemMovieArrayList.remove(resultsItemMovieArrayList.get(viewHolder.getAdapterPosition()));
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(rvFavTvShow);
    }

    private void removeFav(long tag) {
        tvShowDatabase = TvShowDatabase.getTvShowDatabase(getActivity());
        tvShowDatabase.tvShowDao().deleteTvShow(tag);
        Toasty.success(getActivity(), R.string.successDel, Toast.LENGTH_SHORT).show();
    }

    public void getFavorite() {
        if (tvShowDatabase.tvShowDao().getFavoriteTvShow() == null) {
            Toast.makeText(getActivity(), R.string.noFavData, Toast.LENGTH_SHORT).show();
            showLoading(false);
        } else {
            tvShowDatabase.tvShowDao().getFavoriteTvShow();
            showLoading(false);
        }
    }

    private void setupRecyclerView() {
        resultsItemMovieArrayList = tvShowDatabase.tvShowDao().getFavoriteTvShow();
        if (adapter == null) {
            adapter = new FavTvShowAdapater(getActivity(), resultsItemMovieArrayList);
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
