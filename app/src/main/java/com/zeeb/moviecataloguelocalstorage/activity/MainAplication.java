package com.zeeb.moviecataloguelocalstorage.activity;

import android.app.Application;

import com.onesignal.OneSignal;
import com.orhanobut.hawk.Hawk;

public class MainAplication extends Application {

    public static final String KEY_TITLE = "title";
    public static final String KEY_FRAGMENT = "fragment";

    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        Hawk.init(this).build();

    }
}
