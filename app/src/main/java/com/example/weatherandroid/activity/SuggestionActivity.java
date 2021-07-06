package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weatherandroid.R;
import com.example.weatherandroid.util.Utils;

/**
 * Describe: suggestion activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class SuggestionActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SuggestionActivity";

    private View mStatusView;
    private ImageView mBack;
    private LinearLayout mIphone, mMail;
    private TextView mIphoneNum, mMailNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        Utils.setAndroidNativeLightStatusBar(SuggestionActivity.this, true);
        autoSetBarSize();
        init();
    }

    private void init() {
        mBack = findViewById(R.id.iv_suggestion_back);
        mIphone = findViewById(R.id.linear_iphone);
        mMail = findViewById(R.id.linear_mail);
        mIphoneNum = findViewById(R.id.tv_suggestion_iphone);
        mMailNum = findViewById(R.id.tv_suggestion_mail);

        mIphone.setOnClickListener(this);
        mMail.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(SuggestionActivity.this);
        mStatusView = findViewById(R.id.view_suggestion_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SuggestionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_suggestion_back:
                finish();
                break;
            case R.id.linear_iphone:
                Intent iphoneIntent = new Intent(Intent.ACTION_DIAL);
                String iphone = "tel:" + mIphoneNum.getText().toString().trim();
                iphoneIntent.setData(Uri.parse(iphone));
                startActivity(iphoneIntent);
                break;
            case R.id.linear_mail:
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                mailIntent.setType("text/plain");
                String mail = "mailto:" + mMailNum.getText().toString().trim();
                mailIntent.setData(Uri.parse(mail));
                mailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mailIntent);
                break;
            default:
                break;
        }
    }
}