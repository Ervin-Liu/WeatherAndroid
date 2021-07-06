package com.example.weatherandroid.sogson;

import java.util.List;

/**
 * Describe: gson-Data
 * <p>
 * Created by Ervin Liu on 2021/04/14---14:20
 **/
public class Data {
    private String shidu;

    private int pm25;

    private int pm10;

    private String quality;

    private String wendu;

    private String ganmao;

    private List<Forecast> forecast;

    private Yesterday yesterday;

    public void setShidu(String shidu){
        this.shidu = shidu;
    }
    public String getShidu(){
        return this.shidu;
    }
    public void setPm25(int pm25){
        this.pm25 = pm25;
    }
    public int getPm25(){
        return this.pm25;
    }
    public void setPm10(int pm10){
        this.pm10 = pm10;
    }
    public int getPm10(){
        return this.pm10;
    }
    public void setQuality(String quality){
        this.quality = quality;
    }
    public String getQuality(){
        return this.quality;
    }
    public void setWendu(String wendu){
        this.wendu = wendu;
    }
    public String getWendu(){
        return this.wendu;
    }
    public void setGanmao(String ganmao){
        this.ganmao = ganmao;
    }
    public String getGanmao(){
        return this.ganmao;
    }
    public void setForecast(List<Forecast> forecast){
        this.forecast = forecast;
    }
    public List<Forecast> getForecast(){
        return this.forecast;
    }
    public void setYesterday(Yesterday yesterday){
        this.yesterday = yesterday;
    }
    public Yesterday getYesterday(){
        return this.yesterday;
    }
}
