package com.example.weatherandroid.bean;

/**
 * Describe: city name and gps
 * <p>
 * Created by Ervin Liu on 2021/04/13---13:36
 **/
public class AllCityNameGps {
    private String mCityName;
    private boolean mCityGps;

    public AllCityNameGps(String mCityName, boolean mCityGps) {
        this.mCityName = mCityName;
        this.mCityGps = mCityGps;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public boolean isCityGps() {
        return mCityGps;
    }

    public void setCityGps(boolean mCityGps) {
        this.mCityGps = mCityGps;
    }
}
