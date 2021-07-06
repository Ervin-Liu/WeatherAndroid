package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherandroid.R;
import com.example.weatherandroid.adapter.ListCityAdapter;
import com.example.weatherandroid.bean.ListCity;
import com.example.weatherandroid.db.CityInfoBean;
import com.example.weatherandroid.dialog.CommDialog;
import com.example.weatherandroid.fragment.WeatherFragment;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.OkHttpUtil;
import com.example.weatherandroid.util.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Describe:Select City Activity
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class ListCityActivity extends BaseActivity implements ListCityAdapter.OnItemClickListener, View.OnClickListener, CommDialog.OnClickListener {

    private static final String TAG = "ListCityActivity";

    private View mStatusView;
    private RecyclerView mCityListRecycler;
    private ImageView mBack, mDelete;
    private TextView mCancel;
    private FloatingActionButton mFabAddCity;

    private ListCityAdapter mListCityAdapter;
    private List<ListCity> mListCity;
    private List<CityInfoBean> mAllCityInfo;
    private CommDialog mCommDialog;
    private String mDeleteProvince, mDeleteCity, mDeleteCounty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_city);
        Utils.setAndroidNativeLightStatusBar(ListCityActivity.this, true);
        autoSetBarSize();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logg.d(TAG, "onResume");
        refreshList(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logg.d(TAG, "onDestroy");
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ListCityActivity.class);
        context.startActivity(intent);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(ListCityActivity.this);
        mStatusView = findViewById(R.id.view_list_city_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    private void init() {
        mDeleteProvince = "";
        mDeleteCity = "";
        mDeleteCounty = "";

        mListCity = new ArrayList<>();
        mAllCityInfo = new ArrayList<>();

        mCityListRecycler = findViewById(R.id.rv_list_city);
        mBack = findViewById(R.id.iv_list_back);
        mDelete = findViewById(R.id.iv_list_delete);
        mCancel = findViewById(R.id.tv_list_cancel);
        mFabAddCity = findViewById(R.id.fab_add_city);
        mDelete.setVisibility(View.VISIBLE);
        mCancel.setVisibility(View.GONE);

        mBack.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mFabAddCity.setOnClickListener(this);

        mListCityAdapter = new ListCityAdapter(ListCityActivity.this, mListCity);
        mListCityAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListCityActivity.this);
        mCityListRecycler.setLayoutManager(layoutManager);
        mCityListRecycler.setAdapter(mListCityAdapter);
    }

    private void setNameAndTemp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (CityInfoBean cityInfoBean : mAllCityInfo) {
                    String showCityName = "";
                    String county = cityInfoBean.getCounty();
                    String city = cityInfoBean.getCity();
                    String province = cityInfoBean.getProvince();
                    boolean isGps = cityInfoBean.isIsGps();
                    if (null != county && !county.equals("")) {
                        Logg.d(TAG, "setNameAndTemp---->set county name");
                        if (county.equals(Constant.THE_CITY)) {
                            showCityName = city;
                        } else {
                            showCityName = county;
                        }
                        queryTemp(province, city, county, showCityName, isGps, i);
                    } else if (null != city && !city.equals("")) {
                        Logg.d(TAG, "setNameAndTemp---->set city name");
                        if (city.equals(Constant.THE_CITY)) {
                            showCityName = province;
                        } else {
                            showCityName = city;
                        }
                        queryTemp(province, city, county, showCityName, isGps, i);
                    } else if (null != province && !province.equals("")) {
                        for (WeatherFragment.City cityName : WeatherFragment.City.values()) {
                            String strCityName = String.valueOf(cityName);
                            if (strCityName.equals(province)) {
                                Logg.d(TAG, "setNameAndTemp---->set province name");
                                queryTemp(province, city, county, province, isGps, i);
                                break;
                            }
                        }
                    } else {
                        Logg.w(TAG, "setNameAndTemp---->city name is error!");
                    }
                    i++;
                }

            }
        }).start();

    }

    private void queryTemp(String province, String city, String county, String cityName, boolean isGps, int i) {
        String queryWeatherUrl = Constant.sWeatherUrl + "queryWeather?" + "province=" + province + "&city=" + city + "&county=" + county;
        Response response = OkHttpUtil.sendOkHttpRequestExecute(queryWeatherUrl);
        String temp = "";
        String type = "";
        try {
            if (response != null) {
                String responseStr = response.body().string();
                if (responseStr != null && !responseStr.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        JSONObject data = jsonObject.getJSONObject(Constant.JSON_DATA);
                        temp = data.getString(Constant.JSON_TEMP);
                        temp = temp + Constant.LIST_TEMP;
                        JSONArray forecast = data.getJSONArray(Constant.JSON_FORECAST);
                        type = forecast.getJSONObject(0).getString(Constant.JSON_TYPE);
                        Logg.d(TAG, "temp ----> " + temp + "type---->" + type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Logg.d(TAG, "queryTemp---->For failure on: " + cityName);
                    showToast(cityName);
                }
            } else {
                showToast(cityName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mListCity.add(new ListCity(cityName, temp ,isGps, type));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (ListCityActivity.this) {
                    mListCityAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showToast(String cityName) {
        String fail = String.format(Constant.GET_WEATHER_FAIL, cityName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(ListCityActivity.this, fail, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void onListItemClick(View view, int position) {
        Logg.d(TAG, "onListItemClick-----------------");
        WeatherFragment.setCurrentItem(position);
        finish();
    }

    @Override
    public void onDeleteItemClick(View view, int position) {
        Logg.d(TAG, "onDeleteItemClick-----------------");

        mDeleteProvince = mAllCityInfo.get(position).getProvince();
        mDeleteCity = mAllCityInfo.get(position).getCity();
        mDeleteCounty= mAllCityInfo.get(position).getCounty();
        boolean isGps = mAllCityInfo.get(position).isIsGps();
        if (isGps) {
            Toast.makeText(ListCityActivity.this, Constant.LOCATION_DELETE_NO, Toast.LENGTH_SHORT).show();
        } else {
            mCommDialog = new CommDialog(ListCityActivity.this);
            mCommDialog.setTitle("提示");
            mCommDialog.setContent("确定要移除吗");
            mCommDialog.setOk("移除");
            mCommDialog.setCancel("取消");
            mCommDialog.setOnClickListener(this);
            mCommDialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_list_back:
                finish();
                break;
            case R.id.iv_list_delete:
                refreshList(true);
                break;
            case R.id.tv_list_cancel:
                refreshList(false);
                break;
            case R.id.fab_add_city:
                SelectCityActivity.startActivity(ListCityActivity.this);
                break;
            default:
                break;
        }
    }

    private void refreshList(boolean flag) {
        mListCityAdapter.setIDelete(flag);
        mListCity.clear();
        mAllCityInfo = LitePal.findAll(CityInfoBean.class);
        setNameAndTemp();
        if (flag) {
            mDelete.setVisibility(View.GONE);
            mCancel.setVisibility(View.VISIBLE);
        } else {
            mDelete.setVisibility(View.VISIBLE);
            mCancel.setVisibility(View.GONE);
        }

    }

    @Override
    public void okClick() {
        LitePal.deleteAll(CityInfoBean.class, "mProvince = ? and mCity = ? and mCounty = ? and mIsGps = 0",
                mDeleteProvince, mDeleteCity, mDeleteCounty );
        refreshList(true);
        dismissDialog();
    }

    @Override
    public void cancelClick() {
        mDeleteProvince = "";
        mDeleteCity = "";
        mDeleteCounty = "";
        dismissDialog();
    }

    private void dismissDialog() {
        if (null != mCommDialog) {
            mCommDialog.dismiss();
            mCommDialog = null;
        }
    }
}