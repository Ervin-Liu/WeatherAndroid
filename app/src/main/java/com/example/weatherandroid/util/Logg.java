package com.example.weatherandroid.util;

import android.util.Log;

import com.example.weatherandroid.BuildConfig;

/**
 * Describe: log print debug---print log    release----not print log
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class Logg {

    public static boolean DEBUG = BuildConfig.LOG_DEBUG;

    public Logg() {
    }

    public static void i(String tag, String log) {
        if (DEBUG) {
            Log.i(tag, log);
        }
    }

    public static void d(String tag, String log) {
        if (DEBUG) {
            Log.d(tag, log);
        }
    }

    public static void v(String tag, String log) {
        if (DEBUG) {
            Log.v(tag, log);
        }
    }

    public static void w(String tag, String log) {
        if (DEBUG) {
            Log.w(tag, log);
        }
    }

    public static void w(String tag, String log, Throwable tr) {
        if (DEBUG) {
            Log.w(tag, log, tr);
        }
    }

    public static void e(String tag, String log) {
        if (DEBUG) {
            Log.e(tag, log);
        }
    }

    public static void e(String tag, String log, Throwable tr) {
        if (DEBUG) {
            Log.e(tag, log, tr);
        }
    }
}
