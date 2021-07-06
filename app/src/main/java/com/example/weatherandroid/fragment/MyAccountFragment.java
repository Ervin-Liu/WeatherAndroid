package com.example.weatherandroid.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.weatherandroid.R;
import com.example.weatherandroid.activity.AccountSettingActivity;
import com.example.weatherandroid.activity.DayNowActivity;
import com.example.weatherandroid.activity.LoginActivity;
import com.example.weatherandroid.activity.ModifyDataActivity;
import com.example.weatherandroid.activity.ModifyPassActivity;
import com.example.weatherandroid.activity.QQActivity;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.OkHttpUtil;
import com.example.weatherandroid.util.SPKeyValueHelper;
import com.example.weatherandroid.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Describe:My Account Fragment
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class MyAccountFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MyAccountFragment";

    private String mTitle;

    private View mView, mStatusView;
    private ImageView mTouXiangIv, mAccountSettingIv, mUserSettingIv;
    private TextView mUserNameTv, mUserSignTv;
    private String mLoginIphone, mLoginPass, mLoginName, mLoginSignature, mLoginUrl;
    private int mLoginGender;
    private boolean mIsLogin;
    private PopupWindow pop;
    private LinearLayout mDayNow, mQQ;

    public MyAccountFragment(String title) {
        mTitle = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_account, container, false);
        Utils.setAndroidNativeLightStatusBar(getActivity(), true);
        autoSetBarSize();
        init();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logg.d(TAG, "onResume");
        mLoginIphone = SPKeyValueHelper.get(Constant.USER_IPHONE, "");
        mLoginPass = SPKeyValueHelper.get(Constant.USER_PASS, "");
        mIsLogin = SPKeyValueHelper.get(Constant.USER_LOGIN, false);
        if (!TextUtils.isEmpty(mLoginIphone) && !TextUtils.isEmpty(mLoginPass) && mIsLogin) {
            refreshAccount(mLoginIphone);
        } else {
            mLoginIphone = "";
            mLoginPass = "";
            mIsLogin = false;
            mLoginName = "";
            mLoginGender = 0;
            mLoginSignature = "";
            mLoginUrl = "";
            mUserNameTv.setText("登陆/注册");
            mUserNameTv.setClickable(true);
            mUserSignTv.setText("暂无介绍");
            mTouXiangIv.setImageResource(R.drawable.ic_default_tou_xiang);
            mUserSettingIv.setClickable(false);
        }
    }

    private void init() {
        mLoginIphone = "";
        mLoginPass = "";
        mIsLogin = false;
        mLoginName = "";
        mLoginGender = 0;
        mLoginSignature = "";
        mLoginUrl = "";
        mTouXiangIv = mView.findViewById(R.id.civ_tou_xiang);
        mUserSettingIv = mView.findViewById(R.id.iv_user_setting);
        mAccountSettingIv = mView.findViewById(R.id.iv_account_setting);
        mUserNameTv = mView.findViewById(R.id.tv_user_name);
        mUserSignTv = mView.findViewById(R.id.tv_user_signature);

        mDayNow = mView.findViewById(R.id.linear_day_now);
        mQQ = mView.findViewById(R.id.linear_qq);

        mDayNow.setOnClickListener(this);
        mQQ.setOnClickListener(this);

        mUserNameTv.setClickable(true);
        mUserSettingIv.setClickable(false);

        mUserNameTv.setOnClickListener(this);
        mUserSettingIv.setOnClickListener(this);
        mAccountSettingIv.setOnClickListener(this);

    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        mStatusView = mView.findViewById(R.id.view_account_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_setting:
                showPop();
                break;
            case R.id.iv_account_setting:
                AccountSettingActivity.startActivity(getActivity());
                break;
            case R.id.tv_user_name:
                LoginActivity.startActivity(getActivity());
                break;
            case R.id.linear_day_now:
                DayNowActivity.startActivity(getActivity());
                break;
            case R.id.linear_qq:
                QQActivity.startActivity(getActivity());
                break;
            default:
                break;
        }
    }


    public void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.pop_modify, null);
        TextView changePass = bottomView.findViewById(R.id.tv_modify_pass);
        TextView changeData = bottomView.findViewById(R.id.tv_modify_data);
        TextView cancel = bottomView.findViewById(R.id.tv_modify_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_modify_pass:
                        ModifyPassActivity.startActivity(getActivity());
                        break;
                    case R.id.tv_modify_data:
                        ModifyDataActivity.startActivity(getActivity(), mLoginIphone, mLoginUrl, mLoginName, mLoginGender, mLoginSignature);
                        break;
                    case R.id.tv_modify_cancel:
                        closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        changePass.setOnClickListener(clickListener);
        changeData.setOnClickListener(clickListener);
        cancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    private void refreshAccount(String iphone) {
        String url = Constant.sWeatherUrl + "queryAccount?iphone=" + iphone;
        OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showToast();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response != null) {
                    try {
                        String responseStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseStr);
                        String statusCode = jsonObject.getString("statusCode");
                        if (statusCode.equals("200")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            mLoginName = data.getJSONObject(0).getString("name");
                            mLoginGender = data.getJSONObject(0).getInt("gender");
                            mLoginSignature = data.getJSONObject(0).getString("signature");
                            mLoginUrl = data.getJSONObject(0).getString("url");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUserNameTv.setText(mLoginName);
                                    mUserNameTv.setClickable(false);
                                    mUserSettingIv.setClickable(true);
                                    if (mLoginSignature.equals("")) {
                                        mUserSignTv.setText("这个人很懒，什么都没留下...");
                                    } else {
                                        mUserSignTv.setText(mLoginSignature);
                                    }
                                    if (!mLoginUrl.equals("")) {
                                        String url = Constant.sWeatherUrl + (mLoginUrl.split("SSM_war_exploded/"))[1];
                                        Glide.with(getActivity())
                                                .load(url)
                                                //Close the cache
                                                .skipMemoryCache(true)
                                                .signature(new ObjectKey(UUID.randomUUID().toString()))
                                                .into(mTouXiangIv);
                                    }
                                }
                            });
                        } else {
                            showToast();
                        }
                    } catch (JSONException e) {

                    }
                } else {
                    showToast();
                }
            }
        });
    }

    private void showToast() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(getActivity(), "登录失败,请稍后重试", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }
}
