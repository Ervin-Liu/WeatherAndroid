package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherandroid.R;
import com.example.weatherandroid.application.WeatherApplication;
import com.example.weatherandroid.dialog.LoadingDialog;
import com.example.weatherandroid.util.CleanMessageUtil;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.SPKeyValueHelper;
import com.example.weatherandroid.util.Utils;

/**
 * Describe: account setting activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class AccountSettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AccountSettingActivity";

    private View mStatusView;
    private ImageView mAccountSettingBack;
    private TextView mClearCache, mAbout, mSuggestion, mVersionUpdate;
    private LoadingDialog mLoadingDialog;
    private Button mRegisterLogin;
    private String mLoginIphone, mLoginPass;
    private boolean mIsLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        Utils.setAndroidNativeLightStatusBar(AccountSettingActivity.this, true);
        autoSetBarSize();
        init();
    }

    private void init() {
        mLoginIphone = "";
        mLoginPass = "";
        mIsLogin = false;

        mAccountSettingBack = findViewById(R.id.iv_account_setting_back);
        mClearCache = findViewById(R.id.tv_clear_cache);
        mAbout = findViewById(R.id.tv_about_our);
        mSuggestion = findViewById(R.id.tv_suggestion);
        mVersionUpdate = findViewById(R.id.tv_version_update);
        mRegisterLogin = findViewById(R.id.btn_login_register);

        mAccountSettingBack.setOnClickListener(this);
        mClearCache.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
        mVersionUpdate.setOnClickListener(this);
        mRegisterLogin.setOnClickListener(this);

        mLoginIphone = SPKeyValueHelper.get(Constant.USER_IPHONE, "");
        mLoginPass = SPKeyValueHelper.get(Constant.USER_PASS, "");
        mIsLogin = SPKeyValueHelper.get(Constant.USER_LOGIN, false);
        if (!TextUtils.isEmpty(mLoginIphone) && !TextUtils.isEmpty(mLoginPass) && mIsLogin) {
            mRegisterLogin.setText("退出登录");
        } else {
            mRegisterLogin.setText("登录");
        }
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(AccountSettingActivity.this);
        mStatusView = findViewById(R.id.view_account_setting_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AccountSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_account_setting_back:
                finish();
                break;
            case R.id.tv_clear_cache:
                mLoadingDialog = new LoadingDialog(this, "清除中...", R.drawable.ic_loading);
                mLoadingDialog.show();
                CleanMessageUtil.clearAllCache(WeatherApplication.getApplication());
                mClearCache.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AccountSettingActivity.this, "清除完成", Toast.LENGTH_SHORT).show();
                        mLoadingDialog.dismiss();
                    }
                }, 1000);
                break;
            case R.id.tv_about_our:
                AboutActivity.startActivity(AccountSettingActivity.this);
                break;
            case R.id.tv_suggestion:
                SuggestionActivity.startActivity(AccountSettingActivity.this);
                break;
            case R.id.tv_version_update:
                mLoadingDialog = new LoadingDialog(this, "检查中...", R.drawable.ic_loading);
                mLoadingDialog.show();
                CleanMessageUtil.clearAllCache(WeatherApplication.getApplication());
                mVersionUpdate.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AccountSettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                        mLoadingDialog.dismiss();
                    }
                }, 1000);
                break;
            case R.id.btn_login_register:
                if (mIsLogin) {
                    SPKeyValueHelper.put(Constant.USER_LOGIN, false);
                    mIsLogin = false;
                    mRegisterLogin.setText("登录");
                    LoginActivity.startActivity(AccountSettingActivity.this);
                    Toast.makeText(AccountSettingActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                } else {
                    LoginActivity.startActivity(AccountSettingActivity.this);
                    mRegisterLogin.setText("退出登录");
                }
                finish();
                break;
            default:
                break;
        }

    }
}