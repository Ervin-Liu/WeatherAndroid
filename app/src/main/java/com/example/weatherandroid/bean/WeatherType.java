package com.example.weatherandroid.bean;

/**
 * Describe: weather type
 * <p>
 * Created by Ervin Liu on 2021/04/15---17:14
 **/
public class WeatherType {
    private String mDay;
    private String mType;

    public WeatherType() {
    }

    public WeatherType(String mDay, String mType) {
        this.mDay = mDay;
        this.mType = mType;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String mDay) {
        this.mDay = mDay;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
