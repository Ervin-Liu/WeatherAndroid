package com.example.weatherandroid.sogson;

/**
 * Describe: gson-Yesterday
 * <p>
 * Created by Ervin Liu on 2021/04/14---14:20
 **/
public class Yesterday {
    private String date;

    private String high;

    private String low;

    private String ymd;

    private String week;

    private String sunrise;

    private String sunset;

    private int aqi;

    private String fx;

    private String fl;

    private String type;

    private String notice;

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setHigh(String high){
        this.high = high;
    }
    public String getHigh(){
        return this.high;
    }
    public void setLow(String low){
        this.low = low;
    }
    public String getLow(){
        return this.low;
    }
    public void setYmd(String ymd){
        this.ymd = ymd;
    }
    public String getYmd(){
        return this.ymd;
    }
    public void setWeek(String week){
        this.week = week;
    }
    public String getWeek(){
        return this.week;
    }
    public void setSunrise(String sunrise){
        this.sunrise = sunrise;
    }
    public String getSunrise(){
        return this.sunrise;
    }
    public void setSunset(String sunset){
        this.sunset = sunset;
    }
    public String getSunset(){
        return this.sunset;
    }
    public void setAqi(int aqi){
        this.aqi = aqi;
    }
    public int getAqi(){
        return this.aqi;
    }
    public void setFx(String fx){
        this.fx = fx;
    }
    public String getFx(){
        return this.fx;
    }
    public void setFl(String fl){
        this.fl = fl;
    }
    public String getFl(){
        return this.fl;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setNotice(String notice){
        this.notice = notice;
    }
    public String getNotice(){
        return this.notice;
    }
}
