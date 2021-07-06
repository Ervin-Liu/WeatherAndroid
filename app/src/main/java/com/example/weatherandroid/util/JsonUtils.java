package com.example.weatherandroid.util;

import com.example.weatherandroid.nowgson.NowWeather;
import com.example.weatherandroid.sogson.SoJsonWeather;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Describe: json utils
 * <p>
 * Created by Ervin Liu on 2021/04/14---14:23
 **/
public class JsonUtils {

    public static SoJsonWeather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, SoJsonWeather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NowWeather handleNowWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, NowWeather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
