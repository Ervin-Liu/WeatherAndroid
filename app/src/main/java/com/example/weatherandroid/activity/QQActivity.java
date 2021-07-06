package com.example.weatherandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherandroid.R;
import com.example.weatherandroid.dialog.LoadingDialog;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.OkHttpUtil;
import com.example.weatherandroid.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Describe:QQActivity
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class QQActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "QQActivity";

    private View mStatusView;
    private ImageView mBack;
    private EditText mEditText;
    private TextView mQQShow, mConShow, mAnaShow;
    private ImageView mQQDelete;
    private Button mBtnStart;
    private LoadingDialog mLoadingDialog;
    private String mConclusion;
    private String mAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_q);
        Utils.setAndroidNativeLightStatusBar(QQActivity.this, true);
        autoSetBarSize();
        init();
    }

    private void init() {
        mConclusion = "";
        mAnalysis = "";

        mBack = findViewById(R.id.iv_qq_back);
        mEditText = findViewById(R.id.et_qq);
        mQQShow = findViewById(R.id.tv_qq_qq);
        mQQDelete = findViewById(R.id.iv_qq_delete);
        mBtnStart = findViewById(R.id.btn_start);
        mConShow = findViewById(R.id.tv_conclusion);
        mAnaShow = findViewById(R.id.tv_analysis);
        mConShow.setVisibility(View.GONE);
        mAnaShow.setVisibility(View.GONE);

        mBtnStart.setTextColor(getResources().getColor(R.color.colorBlack));
        mBtnStart.setBackground(getResources().getDrawable(R.drawable.register_no));
        mQQShow.setVisibility(View.GONE);
        mQQDelete.setVisibility(View.GONE);
        mEditText.addTextChangedListener(this);

        mBtnStart.setClickable(false);

        mBack.setOnClickListener(this);
        mQQDelete.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, QQActivity.class);
        context.startActivity(intent);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(QQActivity.this);
        mStatusView = findViewById(R.id.view_qq_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_qq_back:
                finish();
                break;
            case R.id.iv_qq_delete:
                mEditText.setText("");
                break;
            case R.id.btn_start:
                startTest();
                break;
            default:
                break;
        }
    }

    private void startTest() {
        String qq = mEditText.getText().toString().trim();
        if (qq.length() > 5 && qq.length() < 12) {
            mLoadingDialog = new LoadingDialog(QQActivity.this, "测试中...", R.drawable.ic_loading);
            mLoadingDialog.show();
            String url = Constant.sWeatherUrl +  "testQQ?qq=" + qq;
            OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    showFail();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response != null) {
                        String responseStr = response.body().string();
                        Logg.d(TAG,"-------------responseStr" + responseStr);
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            JSONObject result = jsonObject.getJSONObject("result");
                            JSONObject data = result.getJSONObject("data");
                            mConclusion = data.getString("conclusion");
                            mAnalysis = data.getString("analysis");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        showFail();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLoadingDialog.dismiss();
                            mConShow.setVisibility(View.VISIBLE);
                            mAnaShow.setVisibility(View.VISIBLE);
                            mConShow.setText("结论: " + mConclusion);
                            mAnaShow.setText("分析：" + mAnalysis);
                        }
                    });
                }
            });
        } else {
            Toast.makeText(QQActivity.this, "请输入正确的QQ号", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFail() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingDialog.dismiss();
                Toast.makeText(QQActivity.this, "测试失败，请重试", Toast.LENGTH_SHORT).show();
                mConclusion = "";
                mAnalysis = "";
                mConShow.setVisibility(View.GONE);
                mAnaShow.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String qq = mEditText.getText().toString().trim();
        if (qq.length() != 0) {
            mQQShow.setVisibility(View.VISIBLE);
            mQQDelete.setVisibility(View.VISIBLE);
            mBtnStart.setBackground(getResources().getDrawable(R.drawable.register_yes));
            mBtnStart.setTextColor(getResources().getColor(R.color.colorWhite));
            mBtnStart.setClickable(true);
        } else {
            mQQShow.setVisibility(View.GONE);
            mQQDelete.setVisibility(View.GONE);
            mBtnStart.setBackground(getResources().getDrawable(R.drawable.register_no));
            mBtnStart.setTextColor(getResources().getColor(R.color.colorBlack));
            mBtnStart.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}