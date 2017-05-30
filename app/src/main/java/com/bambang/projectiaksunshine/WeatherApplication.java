package com.bambang.projectiaksunshine;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by bambanghs on 5/24/2017.
 */

public class WeatherApplication extends Application {
    private static WeatherApplication instance;
    public static WeatherApplication getInstance() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initialize();
    }

    private void initialize() {
        Paper.init(this);}
}
