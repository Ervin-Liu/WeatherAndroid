package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.weatherandroid.R;
import com.example.weatherandroid.util.Utils;

/**
 * Describe: login or register activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class LoginOrRegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginOrRegisterActivity";

    private TextView mLoginNewUser, mLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        Utils.hideBottomUIMenu(LoginOrRegisterActivity.this);
        Utils.setAndroidNativeLightStatusBar(LoginOrRegisterActivity.this, true);
        init();
    }

    private void init() {
        mLoginNewUser = findViewById(R.id.tv_login_new_user);
        mLoginUser = findViewById(R.id.tv_login_user);

        mLoginUser.setOnClickListener(this);
        mLoginNewUser.setOnClickListener(this);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginOrRegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_new_user:
                break;
            case R.id.tv_login_user:
                LoginActivity.startActivity(LoginOrRegisterActivity.this);
                break;
            default:
                break;
        }
    }
}