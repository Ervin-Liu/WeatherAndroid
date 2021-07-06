package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherandroid.R;
import com.example.weatherandroid.dialog.LoadingDialog;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.OkHttpUtil;
import com.example.weatherandroid.util.SPKeyValueHelper;
import com.example.weatherandroid.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Describe: login activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private static final String TAG = "LoginActivity";

    private EditText mEtNum, mEtPass;
    private ImageView mIvNumDelete, mIvPassDelete, mIvPassSee;
    private RelativeLayout mRelaLogin;
    private TextView mTvNumNum, mTvForget, mTvRegister;
    private boolean mIsPassLook, mIsLogin;
    private String mMessage;
    private LoadingDialog mLoadingDialog;
    private String mLoginIphone, mLoginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.hideBottomUIMenu(LoginActivity.this);
        Utils.setAndroidNativeLightStatusBar(LoginActivity.this, true);
        init();
    }

    private void init() {

        mMessage = "";
        mLoginIphone = "";
        mLoginPass = "";
        mIsPassLook = false;
        mIsLogin = false;
        mEtNum = findViewById(R.id.et_num);
        mEtPass = findViewById(R.id.et_pass);
        mIvNumDelete = findViewById(R.id.iv_num_delete);
        mIvPassDelete = findViewById(R.id.iv_pass_delete);
        mIvPassSee = findViewById(R.id.iv_pass_see);
        mRelaLogin = findViewById(R.id.rela_start_login);
        mTvNumNum = findViewById(R.id.tv_num_num);
        mTvForget = findViewById(R.id.tv_login_forget);
        mTvRegister = findViewById(R.id.tv_login_register);

        mIvPassSee.setBackground(this.getResources().getDrawable(R.drawable.ic_pass_nu_look));

        mIvNumDelete.setVisibility(View.GONE);
        mIvPassDelete.setVisibility(View.GONE);
        mIvNumDelete.setClickable(false);
        mIvPassDelete.setClickable(false);

        mTvNumNum.setVisibility(View.GONE);

        mRelaLogin.setClickable(false);
        mRelaLogin.setBackground(this.getResources().getDrawable(R.drawable.login_icon_no));

        mIvNumDelete.setOnClickListener(this);
        mIvPassDelete.setOnClickListener(this);
        mIvPassSee.setOnClickListener(this);
        mRelaLogin.setOnClickListener(this);
        mTvForget.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);

        mEtNum.addTextChangedListener(this);
        mEtPass.addTextChangedListener(this);

        String iphone = SPKeyValueHelper.get(Constant.USER_IPHONE, "");
        String pass = SPKeyValueHelper.get(Constant.USER_PASS, "");
//        boolean login = SPKeyValueHelper.get(Constant.USER_LOGIN, false);
        if (!TextUtils.isEmpty(iphone) && !TextUtils.isEmpty(pass)) {
            mEtNum.setText(iphone);
            mEtPass.setText(pass);
            mTvNumNum.setVisibility(View.VISIBLE);
            mIvNumDelete.setVisibility(View.VISIBLE);
            mIvNumDelete.setClickable(true);
            mIvPassDelete.setVisibility(View.VISIBLE);
            mIvPassDelete.setClickable(true);
            mRelaLogin.setClickable(true);
            mRelaLogin.setBackground(this.getResources().getDrawable(R.drawable.login_icon_yes));
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_num_delete:
                mEtNum.setText("");
                mTvNumNum.setVisibility(View.GONE);
                break;
            case R.id.iv_pass_delete:
                mEtPass.setText("");
                break;
            case R.id.iv_pass_see:
                mIsPassLook = !mIsPassLook;
                if (mIsPassLook) {
                    mIvPassSee.setBackground(this.getResources().getDrawable(R.drawable.ic_pass_look));
                    mEtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mIvPassSee.setBackground(this.getResources().getDrawable(R.drawable.ic_pass_nu_look));
                    mEtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.rela_start_login:
                login();
                break;
            case R.id.tv_login_forget:
                ModifyPassActivity.startActivity(LoginActivity.this);
                finish();
                break;
            case R.id.tv_login_register:
                RegisterActivity.startActivity(LoginActivity.this);
                finish();
                break;
            default:
                break;
        }
    }

    private void login() {
        mLoginIphone = mEtNum.getText().toString().trim();
        mLoginPass = mEtPass.getText().toString().trim();
        if (!TextUtils.isEmpty(mLoginIphone) && !TextUtils.isEmpty(mLoginPass)) {
            if (isChinaPhoneLegal(mLoginIphone)) {
                if (mLoginIphone.length() == 11 && (mLoginPass.length() > 5 && mLoginPass.length() < 17)) {
                    mLoadingDialog = new LoadingDialog(this, "登录中...", R.drawable.ic_loading);
                    mLoadingDialog.show();
                    String url = Constant.sWeatherUrl + "checkLogin?iphone=" + mLoginIphone + "&pass=" + mLoginPass;
                    OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            mLoadingDialog.dismiss();
                            showToast("fail");
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            if (response != null) {
                                try {
                                    String responseStr = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseStr);
                                    String statusCode = jsonObject.getString("statusCode");
                                    if (statusCode.equals("200")) {
                                        String msg = jsonObject.getString("msg");
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        }
                                        mLoadingDialog.dismiss();
                                        showToast(msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                mLoadingDialog.dismiss();
                                showToast("fail");
                            }
                            if (mIsLogin) {
                                try {
                                    Thread.sleep(500);
                                    SPKeyValueHelper.put(Constant.USER_IPHONE, mLoginIphone);
                                    SPKeyValueHelper.put(Constant.USER_PASS, mLoginPass);
                                    SPKeyValueHelper.put(Constant.USER_LOGIN, true);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                } else {
                    showToast("error");
                }
            } else {
                showToast("numFail");
            }
        }
    }

    private void showToast(String msg) {
        if (msg.equals("register")) {
            mMessage = Constant.IPHONE_NO_REGISTER;
        } else if (msg.equals("noLogin")) {
            mMessage = Constant.PASS_FAIL;
        } else if (msg.equals("fail")) {
            mMessage = Constant.LOGIN_FAIL;
        } else if (msg.equals("numFail")) {
            mMessage = Constant.LOGIN_IPHONE_ERROR;
        } else if (msg.equals("error")) {
            mMessage = Constant.LOGIN_IPHONE_OR_PASS_ERROR;
        }
        mIsLogin = false;
        if (msg.equals("login")) {
            mMessage = Constant.LOGIN_SUCCESS;
            mIsLogin = true;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(LoginActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }


    /**
     * Verify phone number
     *
     * @param str
     * @return
     */
    private static boolean isChinaPhoneLegal(String str) {
        String regExp = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[3,5,6,7,8])" +
                "|(18[0-9])|(19[8,9]))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String num = mEtNum.getText().toString().trim();
        String pass = mEtPass.getText().toString().trim();
        if (!TextUtils.isEmpty(num)) {
            mIvNumDelete.setVisibility(View.VISIBLE);
            mIvNumDelete.setClickable(true);
            if (num.length() >= 3) {
                mTvNumNum.setVisibility(View.VISIBLE);
            } else {
                mTvNumNum.setVisibility(View.GONE);
            }
        } else {
            mIvNumDelete.setVisibility(View.GONE);
            mIvNumDelete.setClickable(false);
        }
        if (!TextUtils.isEmpty(pass)) {
            mIvPassDelete.setVisibility(View.VISIBLE);
            mIvPassDelete.setClickable(true);
        } else {
            mIvPassDelete.setVisibility(View.GONE);
            mIvPassDelete.setClickable(false);
        }
        if (!TextUtils.isEmpty(num) && !TextUtils.isEmpty(pass)) {
            mRelaLogin.setClickable(true);
            mRelaLogin.setBackground(this.getResources().getDrawable(R.drawable.login_icon_yes));
        } else {
            mRelaLogin.setClickable(false);
            mRelaLogin.setBackground(this.getResources().getDrawable(R.drawable.login_icon_no));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}