package com.example.weatherandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.weatherandroid.application.WeatherApplication;

/**
 * Describe: log print debug---print log    release----not print log
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public final class SPKeyValueHelper {

    private static final String FILE_NAME = "share_data";

    private SPKeyValueHelper() {
    }

    /**
     * get key value
     *
     * @param key
     * @param def
     * @return
     */
    public static synchronized int get(String key, int def) {
        return getSharedPreferences().getInt(key, def);
    }

    /**
     * get key value
     *
     * @param key
     * @param def
     * @return
     */
    public static synchronized long get(String key, long def) {
        return getSharedPreferences().getLong(key, def);
    }

    /**
     * get key value
     *
     * @param key
     * @param def
     * @return
     */
    public static synchronized boolean get(String key, boolean def) {
        return getSharedPreferences().getBoolean(key, def);
    }

    /**
     * get key value
     *
     * @param key
     * @param def
     * @return
     */
    public static synchronized String get(String key, String def) {
        return getSharedPreferences().getString(key, def);
    }

    /**
     * put key value
     *
     * @param key
     * @param value
     * @return
     */
    public static synchronized boolean put(String key, int value) {
        return getSharedPreferences().edit().putInt(key, value).commit();
    }

    /**
     * put key value
     *
     * @param key
     * @param value
     * @return
     */
    public static synchronized boolean put(String key, long value) {
        return getSharedPreferences().edit().putLong(key, value).commit();
    }

    /**
     * put key value
     *
     * @param key
     * @param value
     * @return
     */
    public static synchronized boolean put(String key, boolean value) {
        return getSharedPreferences().edit().putBoolean(key, value).commit();
    }
    /**
     * put key value
     *
     * @param key
     * @param value
     * @return
     */
    public static synchronized boolean put(String key, String value) {
        return getSharedPreferences().edit().putString(key, value).commit();
    }


    /**
     * Get SharedPreferences
     *
     * @return
     */
    private static SharedPreferences getSharedPreferences() {
        return WeatherApplication.getApplication().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

}
