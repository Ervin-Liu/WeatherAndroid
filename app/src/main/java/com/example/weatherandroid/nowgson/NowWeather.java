package com.example.weatherandroid.nowgson;

/**
 * Describe: NowWeather
 * <p>
 * Created by Ervin Liu on 2021/04/16---12:01
 **/
public class NowWeather {
    private String success;

    private Result result;

    public void setSuccess(String success){
        this.success = success;
    }
    public String getSuccess(){
        return this.success;
    }
    public void setResult(Result result){
        this.result = result;
    }
    public Result getResult(){
        return this.result;
    }
}
