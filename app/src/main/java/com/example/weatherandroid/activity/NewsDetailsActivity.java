package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.weatherandroid.R;
import com.example.weatherandroid.util.Utils;

/**
 * Describe: show news details activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class NewsDetailsActivity extends BaseActivity {
    private static final String TAG = "NewsDetailsActivity";

    private View mStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Utils.setAndroidNativeLightStatusBar(NewsDetailsActivity.this, true);
        autoSetBarSize();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        WebView webView = findViewById(R.id.web_view);
        //支持JavaScript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //在网页跳转的时候始终在WebView中显示
        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(NewsDetailsActivity.this);
        mStatusView = findViewById(R.id.view_news_details_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
        mStatusView.setBackground(getResources().getDrawable(R.color.blue_active));
    }
}