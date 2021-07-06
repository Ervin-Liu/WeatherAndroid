package com.example.weatherandroid.bean;

/**
 * Describe: show city
 * <p>
 * Created by Ervin Liu on 2021/04/16---15:54
 **/
public class ShowCity {
    private String mCity;
    private String mParentCity;
    private boolean mIsGps;

    public ShowCity() {
    }

    public ShowCity(String mCity, String mParentCity, boolean mIsGps) {
        this.mCity = mCity;
        this.mParentCity = mParentCity;
        this.mIsGps = mIsGps;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getParentCity() {
        return mParentCity;
    }

    public void setParentCity(String mParentCity) {
        this.mParentCity = mParentCity;
    }

    public boolean isIsGps() {
        return mIsGps;
    }

    public void setIsGps(boolean mIsGps) {
        this.mIsGps = mIsGps;
    }
}
