package com.example.weatherandroid.bean;

/**
 * Describe: search city info bean
 * <p>
 * Created by Ervin Liu on 2021/04/17---22:20
 **/
public class SearchCityInfo {
    private String mProvince;
    private String mCity;
    private String mCounty;

    public SearchCityInfo() {
    }

    public SearchCityInfo(String mProvince, String mCity, String mCounty) {
        this.mProvince = mProvince;
        this.mCity = mCity;
        this.mCounty = mCounty;
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
}
