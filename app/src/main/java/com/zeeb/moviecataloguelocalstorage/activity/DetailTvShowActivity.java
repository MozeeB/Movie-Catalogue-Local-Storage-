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
import com.zeeb.moviecataloguelocalstorage.data.local.tvshow.TvShowDatabase;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow.ResultsItemTvShow;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class DetailTvShowActivity extends AppCompatActivity {

    public static final String EXTRATVSHOW = "extra_tvshow";

    @BindView(R.id.imgDetailTvShow)
    ImageView imgDetailTvShow;
    @BindView(R.id.tvJudulDetailTvShow)
    TextView tvJudulDetailTvShow;
    @BindView(R.id.tvDetailScoreTvShow)
    TextView tvDetailScoreTvShow;
    @BindView(R.id.tvDetailDesTvShow)
    TextView tvDetailDesTvShow;
    @BindView(R.id.progressBarDetailTvShow)
    ProgressBar progressBarDetailTvShow;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView3)
    TextView textView3;

    ResultsItemTvShow resultsItemTvShow;
    @BindView(R.id.toolbar2)
    Toolbar toolbar2;
    @BindView(R.id.judldetailtol)
    TextView judldetailtol;
    private TvShowDatabase tvShowDatabase;

    MaterialFavoriteButton materialFavoriteButtonNice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar2);


        materialFavoriteButtonNice = (MaterialFavoriteButton) findViewById(R.id.favorite_nice);
        resultsItemTvShow = getIntent().getParcelableExtra(EXTRATVSHOW);

        if (savedInstanceState != null) {
            showLoading(false);
        } else {
            showLoading(true);

        }

        tvShowDatabase = TvShowDatabase.getTvShowDatabase(this);
        if (tvShowDatabase.tvShowDao().selectItem(String.valueOf(resultsItemTvShow.getId())) != null) {
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
                judldetailtol.setText(getString(R.string.detailfilm) + " " + resultsItemTvShow.getName());
                Glide.with(DetailTvShowActivity.this).load(BuildConfig.URLIMAGE + resultsItemTvShow.getBackdropPath()).into(imgDetailTvShow);
                tvJudulDetailTvShow.setText(resultsItemTvShow.getName());
                tvDetailScoreTvShow.setText(String.valueOf(resultsItemTvShow.getVoteAverage()));
                tvDetailDesTvShow.setText(resultsItemTvShow.getOverview());
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
                            tvShowDatabase = TvShowDatabase.getTvShowDatabase(context);
                            tvShowDatabase.tvShowDao().InsertTvShow(resultsItemTvShow);
                            Toasty.success(DetailTvShowActivity.this, R.string.addFav, Toasty.LENGTH_SHORT).show();
                        } else {
                            tvShowDatabase = TvShowDatabase.getTvShowDatabase(context);
                            tvShowDatabase.tvShowDao().deleteTvShow(resultsItemTvShow.getId());
                        }
                    }
                });

    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBarDetailTvShow.setVisibility(View.VISIBLE);
        } else {
            progressBarDetailTvShow.setVisibility(View.GONE);
        }
    }


}
