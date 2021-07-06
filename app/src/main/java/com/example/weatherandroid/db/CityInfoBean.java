package com.example.weatherandroid.db;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * Describe: city info
 * <p>
 * Created by Ervin Liu on 2021/04/13---11:34
 **/
public class CityInfoBean extends LitePalSupport {
    private int mId;
    private String mProvince;
    private String mCity;
    private String mCounty;
    private boolean mIsGps;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getProvince() {
        return mProvince;
    }

    public void setProvince(String mProvince) {
        this.mProvince = mProvince;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getCounty() {
        return mCounty;
    }

    public void setCounty(String mCounty) {
        this.mCounty = mCounty;
    }

    public boolean isIsGps() {
        return mIsGps;
    }

    public void setIsGps(boolean mIsGps) {
        this.mIsGps = mIsGps;
    }
}
