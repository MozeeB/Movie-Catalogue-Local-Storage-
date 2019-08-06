package com.zeeb.moviecataloguelocalstorage.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;
import com.zeeb.moviecataloguelocalstorage.network.ApiConfig;
import com.zeeb.moviecataloguelocalstorage.reminder.DailyReminder;
import com.zeeb.moviecataloguelocalstorage.reminder.ReleaseTodayReminder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderActivity extends AppCompatActivity {

    @BindView(R.id.swtrelease)
    Switch swtrelease;
    @BindView(R.id.swtDaily)
    Switch swtDaily;

    private DailyReminder dailyReminder;
    private ReleaseTodayReminder releaseTodayReminder;

    List<ResultsItemMovie> resultsItemMovies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);

        dailyReminder = new DailyReminder();
        releaseTodayReminder = new ReleaseTodayReminder();

        getReleaseToday();

        setDailyReminder();

        setSwtDaily();

    }

    public void getReleaseToday(){
        ApiConfig.getInitRetrofit().getReleaseToday("2019-08-06", "2019-08-06").enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                if (response.isSuccessful()){
                    ResponseMovie responseMovie = response.body();
                    resultsItemMovies = responseMovie.getResults();
                    Toasty.success(ReminderActivity.this, "data here", Toasty.LENGTH_LONG).show();

                }else {
                    Toasty.error(ReminderActivity.this, "No data", Toasty.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {
                Toasty.error(ReminderActivity.this, "No data", Toasty.LENGTH_LONG).show();
            }
        });
    }

    public void setSwtDaily(){
        swtDaily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    dailyReminder.setRepeatingAlrm(ReminderActivity.this);
                    Toasty.info(ReminderActivity.this, "Switch on ", Toasty.LENGTH_LONG).show();
                }else {
                    Toasty.info(ReminderActivity.this, "Switch off ", Toasty.LENGTH_LONG).show();
                    dailyReminder.cancelAlarm(ReminderActivity.this);

                }

            }
        });
    }

    public void setDailyReminder(){
        swtrelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    Toasty.info(ReminderActivity.this, "Switch on ", Toasty.LENGTH_LONG).show();
                    releaseTodayReminder.setRepeatingAlarm(ReminderActivity.this, resultsItemMovies);
                }else {
                    Toasty.info(ReminderActivity.this, "Switch off ", Toasty.LENGTH_LONG).show();
                    releaseTodayReminder.cancelAlarm(ReminderActivity.this);

                }
            }
        });
    }





}
