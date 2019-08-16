package com.zeeb.moviecataloguelocalstorage.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.orhanobut.hawk.Hawk;
import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;
import com.zeeb.moviecataloguelocalstorage.reminder.DailyReminder;
import com.zeeb.moviecataloguelocalstorage.reminder.ReleaseMovie;
import com.zeeb.moviecataloguelocalstorage.reminder.ReleaseTodayReminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReminderActivity extends AppCompatActivity {

    @BindView(R.id.swtrelease)
    Switch swtrelease;
    @BindView(R.id.swtDaily)
    Switch swtDaily;

    private DailyReminder dailyReminder;
    private ReleaseTodayReminder releaseTodayReminder;

    private ReleaseMovie releaseMovie;


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
        releaseMovie = new ReleaseMovie();

        setDailyReminder(this);

        setSwtDaily();


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

    public void setDailyReminder(final Context context){
        swtrelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    releaseMovie.setRepeatingAlrm(context);
                    Hawk.put("release", "");
                }else {
                    releaseMovie.cancelAlarm(context);
                    Hawk.delete("release");

                }
            }
        });
    }





}
