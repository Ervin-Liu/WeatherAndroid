package com.example.weatherandroid.application;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Describe: WeatherAndroid application
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class WeatherApplication extends Application {

    private static final String TAG = "WeatherApplication";

    private static WeatherApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
//        Connector.getDatabase();
        sApplication = this;
    }

    /**
     * get application
     *
     * @return
     */
    public static Context getApplication() {
        return sApplication;
    }

}
