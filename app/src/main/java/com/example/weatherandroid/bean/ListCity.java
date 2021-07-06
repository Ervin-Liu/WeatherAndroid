package com.example.weatherandroid.bean;

/**
 * Describe: list city bean
 * <p>
 * Created by Ervin Liu on 2021/04/12---20:42
 **/
public class ListCity {
    private String mCityName;
    private String mTemp;
    private boolean mIsGps;
    private String mType;

    public ListCity() {
    }

    public ListCity(String mCityName, String mTemp, boolean mIsGps, String mType) {
        this.mCityName = mCityName;
        this.mTemp = mTemp;
        this.mIsGps = mIsGps;
        this.mType = mType;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String mCityName) {
        this.mCityName = mCityName;
    }

    public String getTemp() {
        return mTemp;
    }

    public void setTemp(String mTemp) {
        this.mTemp = mTemp;
    }

    public boolean isIsGps() {
        return mIsGps;
    }

    public void setIsGps(boolean mIsGps) {
        this.mIsGps = mIsGps;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}
