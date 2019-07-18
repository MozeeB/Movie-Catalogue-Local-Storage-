package com.zeeb.moviecataloguelocalstorage.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.adapter.TablayoutAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {


    private TablayoutAdapter tabLayoutAdapter;
    private FragmentManager fragmentManager;


    public static final String FAVORITE = "extra_movie";

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPager(view);
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public static FavoriteFragment newInstance(FragmentManager fragmentManager) {
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    private void setPager(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.vp_containerELearning);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tl_tabsAbsence);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        tabLayoutAdapter = new TablayoutAdapter(getChildFragmentManager());
        tabLayoutAdapter.addFragment(FavoriteMovie.newInstance(fragmentManager), getString(R.string.Movies));
        tabLayoutAdapter.addFragment(FavoriteTvShow.newInstance(fragmentManager), getString(R.string.TvShows));
        viewPager.setAdapter(tabLayoutAdapter);
    }


}
