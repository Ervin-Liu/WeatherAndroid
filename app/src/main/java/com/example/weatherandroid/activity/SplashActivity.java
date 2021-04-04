package com.example.weatherandroid.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.weatherandroid.R;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.SPKeyValueHelper;
import com.example.weatherandroid.util.UIUtils;

/**
 * Describe:splash activity
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private static final int START_GUIDE_ACTIVITY  = 0;
    private static final int START_HOME_ACTIVITY  = 1;

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        UIUtils.hideBottomUIMenu(this);
        //Whether it is the first time to enter the app
        final boolean isFirst = SPKeyValueHelper.get(Constant.IS_FIRST, true);
        init();
        if (isFirst) {
            mHandler.sendEmptyMessageDelayed(START_GUIDE_ACTIVITY, 2000);
        } else {
            mHandler.sendEmptyMessageDelayed(START_HOME_ACTIVITY, 2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }

    }

    /**
     * The startup diagram displays for one second and then launches the app
     */
    private void init() {
        mHandlerThread = new HandlerThread("Splash");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == START_GUIDE_ACTIVITY) {
                    startActivity();
                } else if (msg.what == START_HOME_ACTIVITY) {
                    HomeActivity.startActivity(SplashActivity.this);
                    finish();
                }
            }
        };
    }

    /**
     * start GuideActivity
     */
    private void startActivity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GuideActivity.startActivity(SplashActivity.this);
                finish();
            }
        }).start();
    }

}