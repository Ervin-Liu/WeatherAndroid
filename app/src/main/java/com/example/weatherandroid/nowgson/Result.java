package com.example.weatherandroid.nowgson;

import java.util.List;

/**
 * Describe: Result
 * <p>
 * Created by Ervin Liu on 2021/04/16---12:00
 **/
public class Result {
    private String weaid;

    private String cityid;

    private String area_1;

    private String area_2;

    private String area_3;

    private RealTime realTime;

    private Today today;

    private List<FutureDay> futureDay;

    private List<FutureHour> futureHour;

    public void setWeaid(String weaid){
        this.weaid = weaid;
    }
    public String getWeaid(){
        return this.weaid;
    }
    public void setCityid(String cityid){
        this.cityid = cityid;
    }
    public String getCityid(){
        return this.cityid;
    }
    public void setArea_1(String area_1){
        this.area_1 = area_1;
    }
    public String getArea_1(){
        return this.area_1;
    }
    public void setArea_2(String area_2){
        this.area_2 = area_2;
    }
    public String getArea_2(){
        return this.area_2;
    }
    public void setArea_3(String area_3){
        this.area_3 = area_3;
    }
    public String getArea_3(){
        return this.area_3;
    }
    public void setRealTime(RealTime realTime){
        this.realTime = realTime;
    }
    public RealTime getRealTime(){
        return this.realTime;
    }
    public void setToday(Today today){
        this.today = today;
    }
    public Today getToday(){
        return this.today;
    }
    public void setFutureDay(List<FutureDay> futureDay){
        this.futureDay = futureDay;
    }
    public List<FutureDay> getFutureDay(){
        return this.futureDay;
    }
    public void setFutureHour(List<FutureHour> futureHour){
        this.futureHour = futureHour;
    }
    public List<FutureHour> getFutureHour(){
        return this.futureHour;
    }
}
