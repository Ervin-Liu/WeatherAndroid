package com.example.weatherandroid.activity;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherandroid.R;
import com.example.weatherandroid.dialog.CommDialog;
import com.example.weatherandroid.dialog.LoadingDialog;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.OkHttpUtil;
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
 * Describe: modify pass activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class ModifyPassActivity extends BaseActivity implements View.OnClickListener, TextWatcher, CommDialog.OnClickListener {

    public static final int START_TIME = 0;
    public static final int STOP_TIME = 1;


    private View mStatusView;
    private ImageView mRegisterBack, mIvIphoneDelete, mIvPassSee, mIvPassDelete, mIvAgainPassSee, mIvAgainPassDelete, mIvCode, mIvCodeDelete;
    private TextView mTvNum, mTvGetCode, mTvTitle;
    private EditText mEtIphone, mEtPass, mEtAgainPass, mEtCode;
    private Button mRegister;
    private boolean mIsPassLook, mIsAgainPassLook;
    private String mMessage, mCode;
    private int i = 60;
    private CommDialog mCommDialog;
    private LoadingDialog mLoadingDialog;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == START_TIME) {
                mTvGetCode.setClickable(false);
                mTvGetCode.setText(i + "S");
                i--;
                if (i > -1) {
                    mHandler.sendEmptyMessageDelayed(START_TIME, 1000);
                } else {
                    mHandler.sendEmptyMessage(STOP_TIME);
                }
            } else if (msg.what == STOP_TIME) {
                i = 60;
                mTvGetCode.setClickable(true);
                mTvGetCode.setText("获取验证码");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pass);
        autoSetBarSize();
        Utils.setAndroidNativeLightStatusBar(ModifyPassActivity.this, true);
        init();
    }

    private void init() {

        mIsAgainPassLook = false;
        mIsPassLook = false;
        mMessage = "";
        mCode = "";

        mTvTitle = findViewById(R.id.tv_modify_title);
        mRegisterBack = findViewById(R.id.iv_modify_back);

        mTvNum = findViewById(R.id.tv_modify_num_num);
        mEtIphone = findViewById(R.id.et_modify_num);
        mIvIphoneDelete = findViewById(R.id.iv_modify_num_delete);

        mTvGetCode = findViewById(R.id.tv_modify_get_code);
        mTvGetCode.setClickable(false);

        mTvNum.setVisibility(View.GONE);
        mIvIphoneDelete.setClickable(false);
        mIvIphoneDelete.setVisibility(View.GONE);

        mIvPassSee = findViewById(R.id.iv_modify_pass_see);
        mEtPass = findViewById(R.id.et_modify_pass);
        mIvPassDelete = findViewById(R.id.iv_modify_pass_delete);

        mIvPassDelete.setClickable(false);
        mIvPassDelete.setVisibility(View.GONE);

        mIvAgainPassSee = findViewById(R.id.iv_modify_again_pass_see);
        mEtAgainPass = findViewById(R.id.et_modify_again_pass);
        mIvAgainPassDelete = findViewById(R.id.iv_modify_again_pass_delete);

        mIvAgainPassDelete.setClickable(false);
        mIvAgainPassDelete.setVisibility(View.GONE);

        mIvCode = findViewById(R.id.iv_modify_code);
        mEtCode = findViewById(R.id.et_modify_code);
        mIvCodeDelete = findViewById(R.id.iv_modify_code_delete);

        mIvCodeDelete.setClickable(false);
        mIvCodeDelete.setVisibility(View.GONE);

        mRegister = findViewById(R.id.btn_modify);
        mRegister.setClickable(false);
        mRegister.setTextColor(getResources().getColor(R.color.colorBlack));
        mRegister.setBackground(getResources().getDrawable(R.drawable.register_no));

        mRegisterBack.setOnClickListener(this);
        mIvIphoneDelete.setOnClickListener(this);
        mTvGetCode.setOnClickListener(this);
        mIvPassSee.setOnClickListener(this);
        mIvPassDelete.setOnClickListener(this);
        mIvAgainPassSee.setOnClickListener(this);
        mIvAgainPassDelete.setOnClickListener(this);
        mIvCodeDelete.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        mEtIphone.addTextChangedListener(this);
        mEtPass.addTextChangedListener(this);
        mEtAgainPass.addTextChangedListener(this);
        mEtCode.addTextChangedListener(this);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ModifyPassActivity.class);
        context.startActivity(intent);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(ModifyPassActivity.this);
        mStatusView = findViewById(R.id.view_modify_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_modify_back:
                finish();
                break;
            case R.id.iv_modify_num_delete:
                mEtIphone.setText("");
                break;
            case R.id.tv_modify_get_code:
                checkIphone();
                break;
            case R.id.iv_modify_pass_see:
                mIsPassLook = !mIsPassLook;
                if (mIsPassLook) {
                    mIvPassSee.setBackground(this.getResources().getDrawable(R.drawable.ic_pass_look));
                    mEtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mIvPassSee.setBackground(this.getResources().getDrawable(R.drawable.ic_pass_nu_look));
                    mEtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.iv_modify_pass_delete:
                mEtPass.setText("");
                break;
            case R.id.iv_modify_again_pass_see:
                mIsAgainPassLook = !mIsAgainPassLook;
                if (mIsAgainPassLook) {
                    mIvAgainPassSee.setBackground(this.getResources().getDrawable(R.drawable.ic_pass_look));
                    mEtAgainPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mIvAgainPassSee.setBackground(this.getResources().getDrawable(R.drawable.ic_pass_nu_look));
                    mEtAgainPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.iv_modify_again_pass_delete:
                mEtAgainPass.setText("");
                break;
            case R.id.iv_modify_code_delete:
                mEtCode.setText("");
                break;
            case R.id.btn_modify:
                modify();
                break;
            default:
                break;
        }
    }

    private void modify() {
        String iphone = mEtIphone.getText().toString().trim();
        String pass = mEtPass.getText().toString().trim();
        String againPass = mEtAgainPass.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();
        if (!TextUtils.isEmpty(iphone) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(againPass) && !TextUtils.isEmpty(code)) {
            if (iphone.length() == 11 && isChinaPhoneLegal(iphone)) {
                if ((pass.length() > 5 && pass.length() < 17) && (againPass.length() > 5 && againPass.length() < 17)) {
                    if (pass.equals(againPass)) {
                        if (mCode.equals(code)) {
                            modifyPass(iphone, pass);
                        } else {
                            showToast("验证码输入错误");
                        }
                    } else {
                        showToast("输入的两次密码不相同");
                    }
                } else {
                    showToast("密码长度为6~16位");
                }
            } else {
                showToast("请输入正确的手机号");
            }
        }
    }

    private void modifyPass(String iphone, String pass) {
        mLoadingDialog = new LoadingDialog(this, "修改中...", R.drawable.ic_loading);
        mLoadingDialog.show();
        String url = Constant.sWeatherUrl + "modifyPass?iphone=" + iphone + "&pass=" + pass;
        OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mLoadingDialog.dismiss();
                showToast("修改失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response != null) {
                    String responseStr = response.body().string();
                    if (responseStr.equals("1")) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        mLoadingDialog.dismiss();
                        showToast("修改成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LoginActivity.startActivity(ModifyPassActivity.this);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                finish();
                            }
                        });
                    }
                } else {
                    mLoadingDialog.dismiss();
                    showToast("修改失败");
                }
            }
        });
    }

    private void checkIphone() {
        String iphone = mEtIphone.getText().toString().trim();
        if (iphone.length() == 11 && isChinaPhoneLegal(iphone)) {
            mHandler.sendEmptyMessage(START_TIME);
            String url = Constant.sWeatherUrl + "checkIphone?iphone=" + iphone;
            OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    mHandler.removeMessages(START_TIME);
                    mHandler.sendEmptyMessage(STOP_TIME);
                    showToast("获取验证码失败");
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
                                if (msg.equals("Login")) {
                                    mCode = jsonObject.getJSONArray("data").getString(0);
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mCommDialog = new CommDialog(ModifyPassActivity.this);
                                            mCommDialog.setOk(getString(R.string.i_ok));
                                            mCommDialog.setTitle("验证码");
                                            mCommDialog.setContent(mCode);
                                            mCommDialog.setIsSign(true);
                                            mCommDialog.setOnClickListener(ModifyPassActivity.this);
                                            mCommDialog.show();
                                        }
                                    });

                                } else if (msg.equals("register")) {
                                    mHandler.removeMessages(START_TIME);
                                    mHandler.sendEmptyMessage(STOP_TIME);
                                    showToast("该手机号未被注册");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        mHandler.removeMessages(START_TIME);
                        mHandler.sendEmptyMessage(STOP_TIME);
                        showToast("获取验证码失败");
                    }
                }
            });
        }
    }

    private void showToast(String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(ModifyPassActivity.this, msg, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String iphone = mEtIphone.getText().toString().trim();
        String pass = mEtPass.getText().toString().trim();
        String againPass = mEtAgainPass.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();
        if (iphone.length() != 0) {
            mTvGetCode.setClickable(true);
        } else {
            mTvGetCode.setClickable(false);
        }
        if (!TextUtils.isEmpty(iphone)) {
            mIvIphoneDelete.setClickable(true);
            mIvIphoneDelete.setVisibility(View.VISIBLE);
            if (iphone.length() >= 3) {
                mTvNum.setVisibility(View.VISIBLE);
            } else {
                mTvNum.setVisibility(View.GONE);
            }
        } else {
            mIvIphoneDelete.setClickable(false);
            mIvIphoneDelete.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(pass)) {
            mIvPassDelete.setClickable(true);
            mIvPassDelete.setVisibility(View.VISIBLE);
        } else {
            mIvPassDelete.setClickable(false);
            mIvPassDelete.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(againPass)) {
            mIvAgainPassDelete.setClickable(true);
            mIvAgainPassDelete.setVisibility(View.VISIBLE);
        } else {
            mIvAgainPassDelete.setClickable(false);
            mIvAgainPassDelete.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(code)) {
            mIvCodeDelete.setClickable(true);
            mIvCodeDelete.setVisibility(View.VISIBLE);
        } else {
            mIvCodeDelete.setClickable(false);
            mIvCodeDelete.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(iphone) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(againPass) && !TextUtils.isEmpty(code)) {
            mRegister.setClickable(true);
            mRegister.setBackground(getResources().getDrawable(R.drawable.register_yes));
            mRegister.setTextColor(getResources().getColor(R.color.colorWhite));
        } else {
            mRegister.setClickable(false);
            mRegister.setBackground(getResources().getDrawable(R.drawable.register_no));
            mRegister.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

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
    public void okClick() {
        mCommDialog.dismiss();
    }

    @Override
    public void cancelClick() {

    }
}