package com.example.weatherandroid.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weatherandroid.util.Utils;

/**
 * Describe: this is a base activity
 * <p>
 * Created by Ervin Liu on 2021/04/04---21:16
 **/
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTranslucent(this);
    }

}
