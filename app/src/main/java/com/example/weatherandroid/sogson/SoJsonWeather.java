package com.example.weatherandroid.sogson;

/**
 * Describe: gson-Root
 * <p>
 * Created by Ervin Liu on 2021/04/14---14:21
 **/
public class SoJsonWeather {
    private String message;

    private int status;

    private String date;

    private String time;

    private CityInfo cityInfo;

    private Data data;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setTime(String time){
        this.time = time;
    }
    public String getTime(){
        return this.time;
    }
    public void setCityInfo(CityInfo cityInfo){
        this.cityInfo = cityInfo;
    }
    public CityInfo getCityInfo(){
        return this.cityInfo;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }
}
