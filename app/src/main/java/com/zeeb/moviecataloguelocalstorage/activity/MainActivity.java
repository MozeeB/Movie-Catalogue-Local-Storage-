package com.zeeb.moviecataloguelocalstorage.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.fragment.FavoriteFragment;
import com.zeeb.moviecataloguelocalstorage.fragment.MovieFragment;
import com.zeeb.moviecataloguelocalstorage.fragment.TvShowFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    MovieFragment movieFragment;
    TvShowFragment tvShowFragment;
    FavoriteFragment favoriteFragment;
    @BindView(R.id.setLenguage)
    ImageView setLenguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        if (savedInstanceState == null) {
            movieFragment = new MovieFragment();
            tvShowFragment = new TvShowFragment();
            favoriteFragment = new FavoriteFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new MovieFragment()).addToBackStack("frag")
                    .commit();

        } else {
            movieFragment = (MovieFragment) getSupportFragmentManager().findFragmentByTag("frag");
            tvShowFragment = (TvShowFragment) getSupportFragmentManager().findFragmentByTag("frag");
            favoriteFragment = (FavoriteFragment) getSupportFragmentManager().findFragmentByTag("frag");

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_movie:
                    selectedFragment = new MovieFragment();
                    bundle.putString(MovieFragment.MOVIE, "MOVIE");
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.navigation_tvshow:
                    selectedFragment = new TvShowFragment();
                    bundle.putString(TvShowFragment.TVSHOW, "TVSHOW");
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.navigation_favorite:
                    selectedFragment = new FavoriteFragment();
                    bundle.putString(FavoriteFragment.FAVORITE, "FAVORITE");
                    selectedFragment.setArguments(bundle);
                    break;
            }


            FragmentManager mFragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            if (selectedFragment != null) {
                mFragmentTransaction.replace(R.id.fragment_container, selectedFragment).commit();
            }
            return true;
        }
    };


    @OnClick(R.id.setLenguage)
    public void onViewClicked() {
        Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(mIntent);
    }
}
