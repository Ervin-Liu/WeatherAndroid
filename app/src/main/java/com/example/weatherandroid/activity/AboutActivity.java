package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.weatherandroid.R;
import com.example.weatherandroid.util.Utils;

/**
 * Describe: about activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class AboutActivity extends BaseActivity {

    private View mStatusView;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Utils.setAndroidNativeLightStatusBar(AboutActivity.this, true);
        autoSetBarSize();
        init();
    }

    private void init() {
        mBack = findViewById(R.id.iv_about_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(AboutActivity.this);
        mStatusView = findViewById(R.id.view_about_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}