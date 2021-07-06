package com.example.weatherandroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherandroid.R;
import com.example.weatherandroid.adapter.DayNowAdapter;
import com.example.weatherandroid.bean.DayNow;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Describe:DayNowActivity
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class DayNowActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DayNowActivity";

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mTime.setText(mSdf.format(new Date()));
            handler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    private View mStatusView;
    private ImageView mBack;
    private TextView mTime;
    private RecyclerView mListView;
    private SimpleDateFormat mSdf;
    private DayNowAdapter mDayNowAdapter;
    private List<DayNow> mListDayNow;
    private LoadingDialog mLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_now);
        Utils.setAndroidNativeLightStatusBar(DayNowActivity.this, true);
        autoSetBarSize();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    private void init() {
        mSdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");

        handler.sendEmptyMessageDelayed(0, 1000);
        mListDayNow = new ArrayList<>();
        mDayNowAdapter = new DayNowAdapter(mListDayNow, DayNowActivity.this);

        mBack = findViewById(R.id.iv_day_now_back);
        mTime = findViewById(R.id.tv_now_time);
        mListView = findViewById(R.id.list_day_now);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DayNowActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.addItemDecoration(new DividerItemDecoration(DayNowActivity.this, DividerItemDecoration.VERTICAL));
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mDayNowAdapter);

        mBack.setOnClickListener(this);

        queryDayNow();
    }

    private void queryDayNow() {
        mLoadingDialog = new LoadingDialog(DayNowActivity.this, "加载中...", R.drawable.ic_loading);
        mLoadingDialog.show();
        String url = Constant.sWeatherUrl + "queryDayNow";
        OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showFail();
                mLoadingDialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response != null) {
                    try {
                        String responseStr = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseStr);
                        JSONArray result = jsonObject.getJSONArray("result");
                        int length = result.length();
                        for (int i = 0;i < length; i++) {
                            String date = result.getJSONObject(i).getString("date");
                            String title = result.getJSONObject(i).getString("title");
                            mListDayNow.add(new DayNow(date, title));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showFail();
                    mLoadingDialog.dismiss();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.dismiss();
                        mDayNowAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void showFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DayNowActivity.this, "查询失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DayNowActivity.class);
        context.startActivity(intent);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(DayNowActivity.this);
        mStatusView = findViewById(R.id.view_day_now_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_day_now_back:
                finish();
                break;
            default:
                break;
        }
    }
}