package com.zeeb.moviecataloguelocalstorage.reminder;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.zeeb.moviecataloguelocalstorage.R;
import com.zeeb.moviecataloguelocalstorage.activity.DetailMovieActivity;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResponseMovie;
import com.zeeb.moviecataloguelocalstorage.data.remote.model.movie.ResultsItemMovie;
import com.zeeb.moviecataloguelocalstorage.network.ApiConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseTodayReminder extends BroadcastReceiver {

    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    private static int notifId = 1000;

    @Override
    public void onReceive(final Context context, Intent intent) {

        final int notifId = intent.getIntExtra("id", 0);
        final String title = intent.getStringExtra("title");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String now = dateFormat.format(date);

        getNotif(context, title, notifId,now);
    }

    public void getNotif(final Context context, final String title, final int notifid, final String tgl) {

        ApiConfig.getInitRetrofit().getReleaseToday(tgl, tgl).enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                ResponseMovie responseMovie = response.body();
                List<ResultsItemMovie> movies = responseMovie.getResults();
                for (ResultsItemMovie movie: movies){
                    if(movie.getReleaseDate().equals(tgl)){
                        showAlarmNotification(context ,title, notifid);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {

            }
        });

    }

    private void showAlarmNotification(Context context, String title, int notifId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, DetailMovieActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(String.format(context.getString(R.string.upcoming_reminder_msg), title))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmRingtone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notifId, builder.build());
    }

    public void setRepeatingAlarm(Context context, List<ResultsItemMovie> movies) {

        int delay = 0;

        for (ResultsItemMovie movie : movies) {
            cancelAlarm(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReleaseTodayReminder.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("id", notifId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT < Build.VERSION_CODES.KITKAT) {
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay, pendingIntent);
            } else if (SDK_INT > Build.VERSION_CODES.KITKAT && SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                );
            } else if (SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay, pendingIntent);
            }

            notifId += 1;
            delay += 5000;
            Log.e("title", movie.getTitle());
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context context) {
        /* get the application context */
        Intent alarmIntent = new Intent(context, ReleaseTodayReminder.class);

        boolean isAlarmOn = (PendingIntent.getBroadcast(context, notifId, alarmIntent,
                PendingIntent.FLAG_NO_CREATE) != null);

        Log.e("isAlarmOn : ", String.valueOf(isAlarmOn));

        Log.e("id_when_cancel", String.valueOf(notifId));

        return PendingIntent.getBroadcast(context, 101, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
