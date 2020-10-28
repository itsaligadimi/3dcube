package com.agadimi.a3dcube;

import android.app.Application;

import timber.log.Timber;

public class TheApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
