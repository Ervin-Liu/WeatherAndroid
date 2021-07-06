package com.example.weatherandroid.bean;

/**
 * Describe: day now
 * <p>
 * Created by Ervin Liu on 2021/04/22---20:56
 **/
public class DayNow {
    private String time;
    private String event;

    public DayNow() {
    }

    public DayNow(String time, String event) {
        this.time = time;
        this.event = event;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
