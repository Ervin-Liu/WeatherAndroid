package com.example.weatherandroid.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Describe: ok http util
 * <p>
 * Created by Ervin Liu on 2021/04/12---14:55
 **/
public class OkHttpUtil {
    public static void sendOkHttpRequestEnqueue(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public synchronized static Response sendOkHttpRequestExecute(String address) {
        OkHttpClient client = new OkHttpClient();
        Response execute = null;
        Request request = new Request.Builder().url(address).build();
        try {
            execute = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return execute;
    }
}
