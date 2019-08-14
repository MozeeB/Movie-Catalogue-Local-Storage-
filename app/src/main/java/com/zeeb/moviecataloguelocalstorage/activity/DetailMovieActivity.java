package com.zeeb.moviecataloguelocalstorage.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.zeeb.moviecataloguelocalstorage.BuildConfig;
import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.data.local.movie.MovieDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRAMOVIE = "extra_movie";

    @BindView(R.id.imgDetailMovie)
    ImageView imgDetailMovie;
    @BindView(R.id.tvJudulDetail)
    TextView tvJudulDetail;
    @BindView(R.id.tvDetailScore)
    TextView tvDetailScore;
    @BindView(R.id.tvDetailDes)
    TextView tvDetailDes;
    @BindView(R.id.progressBarDetailMovie)
    ProgressBar progressBarDetailMovie;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.toolbar3)
    Toolbar toolbar3;

    ResultsItemMovie resultsItemMovie;
    @BindView(R.id.judldetailtol)
    TextView judldetailtol;
    private MovieDatabase movieDatabase;

    MaterialFavoriteButton materialFavoriteButtonNice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar3);

        materialFavoriteButtonNice = findViewById(R.id.favorite_nice);
        resultsItemMovie = getIntent().getParcelableExtra(EXTRAMOVIE);

        if (savedInstanceState != null) {
            showLoading(false);
        } else {
            showLoading(true);

        }


        movieDatabase = MovieDatabase.getMovieDatabase(this);
        if (movieDatabase.movieDao().selectItemNoCursor(String.valueOf(resultsItemMovie.getId())) != null) {
            materialFavoriteButtonNice.setFavorite(true);
        }
        imageView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        materialFavoriteButtonNice.setVisibility(View.GONE);
        setUpDelay();

        initFavorite(this);


    }

    private void setUpDelay() {
        new Handler().postDelayed(new Runnable() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void run() {
                imageView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                materialFavoriteButtonNice.setVisibility(View.VISIBLE);
                judldetailtol.setText(getString(R.string.detailfilm) + " " + resultsItemMovie.getOriginalTitle());
                Glide.with(DetailMovieActivity.this).load(BuildConfig.URLIMAGE + resultsItemMovie.getBackdropPath()).into(imgDetailMovie);
                tvJudulDetail.setText(resultsItemMovie.getOriginalTitle());
                tvDetailScore.setText(String.valueOf(resultsItemMovie.getVoteAverage()));
                tvDetailDes.setText(resultsItemMovie.getOverview());
                showLoading(false);
            }
        }, 1000);
    }

    private void initFavorite(final Context context) {
        materialFavoriteButtonNice.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            movieDatabase = MovieDatabase.getMovieDatabase(context);
                            movieDatabase.movieDao().insert(resultsItemMovie);
                            Toasty.success(DetailMovieActivity.this, R.string.addFav, Toasty.LENGTH_SHORT).show();

                        } else {
                            movieDatabase = MovieDatabase.getMovieDatabase(context);
                            movieDatabase.movieDao().deleteById(resultsItemMovie.getId());
                        }

                    }
                });

    }



    private void showLoading(Boolean state) {
        if (state) {
            progressBarDetailMovie.setVisibility(View.VISIBLE);
        } else {
            progressBarDetailMovie.setVisibility(View.GONE);
        }
    }
}
