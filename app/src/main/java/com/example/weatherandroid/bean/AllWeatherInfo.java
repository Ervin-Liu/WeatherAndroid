package com.example.weatherandroid.bean;

/**
 * Describe: all weather info
 * <p>
 * Created by Ervin Liu on 2021/04/15---21:58
 **/
public class AllWeatherInfo {
    private String mWeekDate;
    private String mDate;
    private String mType;
    private String mHighTemp;
    private String mLowTemp;

    public AllWeatherInfo() {
    }

    public AllWeatherInfo(String mWeekDate, String mDate, String mType, String mHighTemp, String mLowTemp) {
        this.mWeekDate = mWeekDate;
        this.mDate = mDate;
        this.mType = mType;
        this.mHighTemp = mHighTemp;
        this.mLowTemp = mLowTemp;
    }

    public String getWeekDate() {
        return mWeekDate;
    }

    public void setWeekDate(String mWeekDate) {
        this.mWeekDate = mWeekDate;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getHighTemp() {
        return mHighTemp;
    }

    public void setHighTemp(String mHighTemp) {
        this.mHighTemp = mHighTemp;
    }

    public String getLowTemp() {
        return mLowTemp;
    }

    public void setLowTemp(String mLowTemp) {
        this.mLowTemp = mLowTemp;
    }
}
