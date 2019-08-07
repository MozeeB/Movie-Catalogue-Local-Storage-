package com.zeeb.moviecataloguelocalstorage.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.orhanobut.hawk.Hawk;
import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;
import com.zeeb.moviecataloguelocalstorage.network.ApiConfig;
import com.zeeb.moviecataloguelocalstorage.reminder.DailyReminder;
import com.zeeb.moviecataloguelocalstorage.reminder.ReleaseTodayReminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    String formattedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);

        if (Hawk.contains("daily")){
            swtDaily.setChecked(true);
        }else{
            swtDaily.setChecked(false);
        }


        if (Hawk.contains("release")){
            swtrelease.setChecked(true);
        }else {
            swtrelease.setChecked(false);
        }

        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c);


        dailyReminder = new DailyReminder();
        releaseTodayReminder = new ReleaseTodayReminder();

        getReleaseToday();

        setDailyReminder();

        setSwtDaily();



    }

    public void getReleaseToday(){
        ApiConfig.getInitRetrofit().getReleaseToday(formattedDate, formattedDate).enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                if (response.isSuccessful()){
                    ResponseMovie responseMovie = response.body();
                    resultsItemMovies = responseMovie.getResults();
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
                    Hawk.put("daily", "");
                }else {
                    dailyReminder.cancelAlarm(ReminderActivity.this);
                    Hawk.delete("daily");

                }

            }
        });
    }

    public void setDailyReminder(){
        swtrelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    releaseTodayReminder.setRepeatingAlarm(ReminderActivity.this, resultsItemMovies);
                    Hawk.put("release", "");
                }else {
                    releaseTodayReminder.cancelAlarm(ReminderActivity.this);
                    Hawk.delete("release");

                }
            }
        });
    }





}
