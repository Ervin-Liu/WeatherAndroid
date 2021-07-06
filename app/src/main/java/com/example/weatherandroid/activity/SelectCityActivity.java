package com.example.weatherandroid.activity;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weatherandroid.R;
import com.example.weatherandroid.adapter.SelectShowCityAdapter;
import com.example.weatherandroid.application.WeatherApplication;
import com.example.weatherandroid.bean.SearchCityInfo;
import com.example.weatherandroid.bean.ShowCity;
import com.example.weatherandroid.db.CityInfoBean;
import com.example.weatherandroid.dialog.CommDialog;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.OkHttpUtil;
import com.example.weatherandroid.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Describe: select city activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class SelectCityActivity extends BaseActivity implements CommDialog.OnClickListener, View.OnClickListener {

    private static final String TAG = "SelectCityActivity";
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private static final int LEVEL_SEARCH = 3;

    public enum City {
        北京, 天津, 上海, 重庆, 吉林, 香港, 澳门, 海南;
    }

    public enum Special {
        桦甸, 蛟河, 磐石, 舒兰, 永吉;
    }

    private View mStatusView;
    private ListView mListView;
    private ImageView mBackImage;
    private EditText mSearchCity;
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;
    private CommDialog mCommDialog;

    private LocationClient mLocationClient;
    private LocationClientOption mLocationOption;
    private boolean isGpsOpen;
    private List<CityInfoBean> mAllCityInfo;
    private List<String> mProvinceList, mCityList, mCountyList;
    private List<ShowCity> mShowCityList;
    private SelectShowCityAdapter mShowCityAdapter;
    private int mCurrentLevel;
    private String mSelectProvince, mSelectCity, mSelectCounty;
    private List<CityInfoBean> mGpsCity;
    private List<SearchCityInfo> mSearchCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        Utils.setAndroidNativeLightStatusBar(SelectCityActivity.this, true);
        autoSetBarSize();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logg.d(TAG, "onDestroy");
        unregisterLocation();
    }

    private void init() {

        mListView = findViewById(R.id.list_select_city);
        mProgressBar = findViewById(R.id.pb_loading);
        mBackImage = findViewById(R.id.iv_select_city_back);
        mSearchCity = findViewById(R.id.et_search_city);
        //Only Chinese characters can be entered
        mSearchCity.setFilters(new InputFilter[]{Utils.getInputFilter()});
        mSearchCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = textView.getText().toString().trim();
                    if (!keyword.equals("")) {
                        mCurrentLevel = LEVEL_SEARCH;
                        mListView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mSelectProvince = "";
                        mSelectCity = "";
                        mSelectCounty = "";
                        searchCityInfo(keyword);
                    }
                    return true;
                }
                return false;

            }
        });

        mBackImage.setOnClickListener(this);

        mShowCityList = new ArrayList<>();
        mProvinceList = new ArrayList<>();
        mCityList = new ArrayList<>();
        mCountyList = new ArrayList<>();
        mGpsCity = new ArrayList<>();
        mSearchCityList = new ArrayList<>();

        mCurrentLevel = LEVEL_PROVINCE;
        mSelectProvince = "";
        mSelectCity = "";
        mSelectCounty = "";

        mShowCityAdapter = new SelectShowCityAdapter(SelectCityActivity.this, R.layout.list_select_city, mShowCityList);
        mListView.setAdapter(mShowCityAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentLevel == LEVEL_PROVINCE) {
                    if (mGpsCity.size() != 0 && mGpsCity != null && position == 0) {
                        saveData(mGpsCity.get(0).getProvince(), mGpsCity.get(0).getCity(), mGpsCity.get(0).getCounty());
                        mCurrentLevel = LEVEL_PROVINCE;
                    } else {
                        mSelectProvince = mShowCityList.get(position).getCity();
                        queryCityInfo(LEVEL_CITY);
                        mCurrentLevel = LEVEL_CITY;
                    }
                } else if (mCurrentLevel == LEVEL_CITY) {
                    if (mGpsCity.size() != 0 && mGpsCity != null && position == 0) {
                        saveData(mGpsCity.get(0).getProvince(), mGpsCity.get(0).getCity(), mGpsCity.get(0).getCounty());
                        mCurrentLevel = LEVEL_CITY;
                    } else {
                        if (mShowCityList.get(position).getCity().equals(mSelectProvince)) {
                            mSelectCity = Constant.THE_CITY;
                            saveData(mSelectProvince, mSelectCity, mSelectCounty);
                        } else {
                            mSelectCity = mShowCityList.get(position).getCity();
                            queryCityInfo(LEVEL_COUNTY);
                            mCurrentLevel = LEVEL_COUNTY;
                        }
                    }
                } else if (mCurrentLevel == LEVEL_COUNTY) {
                    if (mGpsCity.size() != 0 && mGpsCity != null && position == 0) {
                        saveData(mGpsCity.get(0).getProvince(), mGpsCity.get(0).getCity(), mGpsCity.get(0).getCounty());
                        mCurrentLevel = LEVEL_COUNTY;
                    } else {
                        if (mShowCityList.get(position).getCity().equals(mSelectCity)) {
                            for (City cityName : City.values()) {
                                String strCityName = String.valueOf(cityName);
                                if (strCityName.equals(mSelectProvince)) {
                                    mSelectCounty = "";
                                    break;
                                } else {
                                    mSelectCounty = Constant.THE_CITY;
                                }
                            }
                            if (mSelectProvince.equals("吉林")) {
                                for (Special special : Special.values()) {
                                    String specialCityName = String.valueOf(special);
                                    if (specialCityName.equals(mSelectCity)) {
                                        mSelectCounty = "";
                                        break;
                                    } else {
                                        mSelectCounty = Constant.THE_CITY;
                                    }
                                }
                            }
                        } else {
                            mSelectCounty = mShowCityList.get(position).getCity();
                        }
                        saveData(mSelectProvince, mSelectCity, mSelectCounty);
                    }
                } else if (mCurrentLevel == LEVEL_SEARCH) {
                    SearchCityInfo searchCityInfo = mSearchCityList.get(position);
                    saveData(searchCityInfo.getProvince(), searchCityInfo.getCity(), searchCityInfo.getCounty());
                }
            }
        });

        if (ContextCompat.checkSelfPermission(SelectCityActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(SelectCityActivity.this, Constant.LOCATION_NO, Toast.LENGTH_SHORT).show();
        } else {
            isGpsOpen = Utils.isGpsOpen(SelectCityActivity.this);
            if (!isGpsOpen) {
                openGPS();
            } else {
                queryCityInfo(mCurrentLevel);
            }
        }
    }

    private void queryCityInfo(int level) {
        showProgressDialog();
        mListView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        String url = "";
        if (level == LEVEL_PROVINCE) {
            url = Constant.sWeatherUrl + "province";
        } else if (level == LEVEL_CITY) {
            url = Constant.sWeatherUrl + "city?province=" + mSelectProvince;
        } else if (level == LEVEL_COUNTY) {
            url = Constant.sWeatherUrl + "county?province=" + mSelectProvince + "&city=" + mSelectCity;
        }
        OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showFail();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response != null) {
                    try {
                        String responseText = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseText);
                        String statusCode = jsonObject.getString("statusCode");
                        if (statusCode.equals("200")) {
                            mShowCityList.clear();
                            JSONArray data = jsonObject.getJSONArray(Constant.JSON_DATA);
                            int length = data.length();
                            mAllCityInfo = LitePal.findAll(CityInfoBean.class);
                            mGpsCity.add(mAllCityInfo.get(0));
                            if (mGpsCity.size() != 0) {
                                CityInfoBean gpsCityInfo = mGpsCity.get(0);
                                String gpsCounty = gpsCityInfo.getCounty();
                                String gpsCity = gpsCityInfo.getCity();
                                String gpsProvince = gpsCityInfo.getProvince();
                                if (gpsCounty != null && !gpsCounty.equals("")) {
                                    mShowCityList.add(new ShowCity(gpsCounty, gpsCity, true));
                                } else if (gpsCity != null && !gpsCity.equals("")) {
                                    mShowCityList.add(new ShowCity(gpsCity, gpsProvince, true));
                                } else if (gpsProvince != null && !gpsProvince.equals("")) {
                                    for (City cityName : City.values()) {
                                        String strCityName = String.valueOf(cityName);
                                        if (strCityName.equals(gpsProvince)) {
                                            mShowCityList.add(new ShowCity(gpsProvince, gpsProvince, true));
                                            break;
                                        }
                                    }
                                }
                            }
                            if (level == LEVEL_PROVINCE) {
                                for (int i = 0; i < length; i++) {
                                    if (data.getString(i).equals(getString(R.string.china))) {
                                        continue;
                                    }
                                    mShowCityList.add(new ShowCity(data.getString(i), getString(R.string.china), false));
                                }
                            } else if (level == LEVEL_CITY) {
                                for (int i = 0; i < length; i++) {
                                    if (data.getString(i).equals(Constant.THE_CITY)) {
                                        mShowCityList.add(new ShowCity(mSelectProvince, mSelectProvince, false));
                                    } else {
                                        mShowCityList.add(new ShowCity(data.getString(i), mSelectProvince, false));
                                    }
                                }
                            } else if (level == LEVEL_COUNTY) {
                                for (int i = 0; i < length; i++) {
                                    if (data.getString(i).equals(Constant.THE_CITY) || data.getString(i).equals("")) {
                                        mShowCityList.add(new ShowCity(mSelectCity, mSelectCity, false));
                                    } else {
                                        mShowCityList.add(new ShowCity(data.getString(i), mSelectCity, false));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showFail();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mShowCityAdapter.notifyDataSetChanged();
                        mListView.setSelection(0);
                    }
                });
                closeProgressDialog();
            }
        });
    }

    private void showFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                Toast.makeText(SelectCityActivity.this, Constant.GET_CITY_INFO_FAIL, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(SelectCityActivity.this);
            mProgressDialog.setMessage(Constant.LOADING);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(SelectCityActivity.this);
        mStatusView = findViewById(R.id.view_select_city_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SelectCityActivity.class);
        context.startActivity(intent);
    }

    private void openGPS() {
        mCommDialog = new CommDialog(SelectCityActivity.this);
        mCommDialog.setTitle(getString(R.string.gps_switch));
        mCommDialog.setContent(getString(R.string.gps_off));
        mCommDialog.setOk(getString(R.string.open));
        mCommDialog.setCancel(getString(R.string.i_cancel));
        mCommDialog.setOnClickListener(this);
        mCommDialog.show();
    }

    @Override
    public void okClick() {
        //Open the setting interface of GPS
        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0);
    }

    @Override
    public void cancelClick() {
        dismissDialog();
    }

    private void dismissDialog() {
        if (null != mCommDialog) {
            mCommDialog.dismiss();
            mCommDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            isGpsOpen = Utils.isGpsOpen(SelectCityActivity.this);
            if (isGpsOpen) {
                dismissDialog();
                registerLocation();
            }
        }
    }

    private void registerLocation() {
        unregisterLocation();
        mLocationClient = new LocationClient(WeatherApplication.getApplication());
        mLocationOption = new LocationClientOption();
        //Sets whether address information is required. Default does not
        mLocationOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(mLocationOption);
        mLocationClient.registerLocationListener(new SelectCityActivity.MyLocationListener());
        mLocationClient.start();
    }

    private void unregisterLocation() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(new SelectCityActivity.MyLocationListener());
            mLocationClient = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_select_city_back:
                back();
                break;
            default:
                break;
        }
    }

    /**
     * Implement location callbacks
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String country = bdLocation.getCountry();
            String province = bdLocation.getProvince().substring(0, (bdLocation.getProvince().length() - 1));
            String city = bdLocation.getCity().substring(0, (bdLocation.getCity().length() - 1));
            String district = bdLocation.getDistrict().substring(0, (bdLocation.getDistrict().length() - 1));
            Logg.d(TAG, "MyLocationListener---->country: " + country + ",province: "
                    + province + ", city: " + city + ",district: " + district);
            if (getString(R.string.china).equals(country)) {
                if (null != province) {
                    if (null == city) {
                        city = "";
                    }
                    if (null == district) {
                        district = "";
                    }
                    saveGpsCity(province, city, district);
                } else {
                    Toast.makeText(SelectCityActivity.this, getString(R.string.gps_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Locate save to database
     *
     * @param province
     * @param city
     * @param county
     */
    private void saveGpsCity(String province, String city, String county) {
        List<CityInfoBean> allCityInfo = LitePal.findAll(CityInfoBean.class);
        if (allCityInfo.size() > 0) {
            LitePal.deleteAll(CityInfoBean.class);
            int i = 0;
            for (CityInfoBean cityInfoBean : allCityInfo) {
                String findCounty = cityInfoBean.getCounty();
                String findCity = cityInfoBean.getCity();
                String findProvince = cityInfoBean.getProvince();
                boolean findIsGps = cityInfoBean.isIsGps();
                if (i == 0) {
                    saveData(province, city, county, true);
//                    if (((findCounty.equals(county) && findCity.equals(city) && findProvince.equals(province)) && (!findIsGps))
//                            || (!findCounty.equals(county) || !findCity.equals(city) || !findProvince.equals(province))) {
//                        Logg.d(TAG, "saveGpsCity---->Positioning changes!");
//                        LitePal.deleteAll(CityInfoBean.class);
//                        saveData(province, city, county, true);
//                        i++;
//                        continue;
//                    }
                } else {
                    if (!findCounty.equals(county) || !findCity.equals(city) || !findProvince.equals(province)) {
                        saveData(findProvince, findCity, findCounty, false);
                    }
//                    if (findCounty.equals(county) && findCity.equals(city) && findProvince.equals(province)) {
////                        LitePal.deleteAll(CityInfoBean.class, "mProvince = ? and mCity = ? and mCounty = ? and mIsGps = 0",
////                                province, city, county );
//                        Logg.d(TAG, "saveGpsCity---->There are repeated positions!");
//                        i++;
//                        continue;
//                    }
//                    Logg.d(TAG, "saveGpsCity---->Add a location that is not positioned!");
//                    saveData(findProvince, findCity, findCounty, false);
                }
                i++;
            }
        } else {
            saveData(province, city, county, true);
        }
        Toast.makeText(SelectCityActivity.this, "已为您自动添加定位城市", Toast.LENGTH_SHORT).show();
        queryCityInfo(LEVEL_PROVINCE);
    }

    private void saveData(String province, String city, String county, boolean isGps) {
        CityInfoBean gpsCity = new CityInfoBean();
        gpsCity.setProvince(province);
        gpsCity.setCity(city);
        gpsCity.setCounty(county);
        gpsCity.setIsGps(isGps);
        gpsCity.save();
    }

    private void saveData(String province, String city, String county) {
        boolean flag = false;
        List<CityInfoBean> allCityInfo = LitePal.findAll(CityInfoBean.class);
        if (allCityInfo.size() > 0) {
            for (CityInfoBean cityInfoBean : allCityInfo) {
                String findCounty = cityInfoBean.getCounty();
                String findCity = cityInfoBean.getCity();
                String findProvince = cityInfoBean.getProvince();
                if (findCounty.equals(county) && findCity.equals(city) && findProvince.equals(province)) {
                    flag = true;
                    break;
                }
            }
        } else {
            saveData(province, city, county, false);
        }
        if (flag) {
            Toast.makeText(SelectCityActivity.this, Constant.HAVE_CITY, Toast.LENGTH_SHORT).show();
        } else {
            saveData(province, city, county, false);
            finish();
        }
    }


    private void back() {
        if (mCurrentLevel == LEVEL_PROVINCE) {
            mSelectProvince = "";
            finish();
        } else if (mCurrentLevel == LEVEL_CITY) {
            queryCityInfo(LEVEL_PROVINCE);
            mCurrentLevel = LEVEL_PROVINCE;
            mSelectCity = "";
            mSelectProvince = "";
        } else if (mCurrentLevel == LEVEL_COUNTY) {
            queryCityInfo(LEVEL_CITY);
            mCurrentLevel = LEVEL_CITY;
            mSelectCounty = "";
            mSelectCity = "";
        } else if (mCurrentLevel == LEVEL_SEARCH) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentLevel == LEVEL_PROVINCE) {
            super.onBackPressed();
        } else {
            back();
        }

    }

    private void searchCityInfo(String keyword) {
        String searchUrl = Constant.sWeatherUrl + "searchCity?key=" + keyword;
        OkHttpUtil.sendOkHttpRequestEnqueue(searchUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showFail();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response != null) {
                    try {
                        String responseText = response.body().string();
                        Logg.d(TAG,"----" + responseText);
                        JSONObject jsonObject = new JSONObject(responseText);
                        String statusCode = jsonObject.getString("statusCode");
                        if (statusCode.equals("200")) {
                            String msg = jsonObject.getString("msg");
                            JSONArray data = jsonObject.getJSONArray("data");
                            int length = data.length();
                            mShowCityList.clear();
                            mSearchCityList.clear();
                            if (msg.equals("province")) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject jsonObject1 = data.getJSONObject(i);
                                    String areaNm1 = jsonObject1.getString("areaNm1");
                                    if (areaNm1.equals(keyword)) {
                                        String areaNm2 = jsonObject1.getString("areaNm2");
                                        String areaNm3 = jsonObject1.getString("areaNm3");
                                        if (areaNm2.equals("") || areaNm2.equals(Constant.THE_CITY)) {
                                            mShowCityList.add(new ShowCity(areaNm1, areaNm1, false));
                                        } else if (areaNm3.equals("") || areaNm3.equals(Constant.THE_CITY)) {
                                            mShowCityList.add(new ShowCity(areaNm2, areaNm2, false));
                                        } else {
                                            mShowCityList.add(new ShowCity(areaNm3, areaNm2, false));
                                        }
                                        mSearchCityList.add(new SearchCityInfo(areaNm1, areaNm2, areaNm3));
                                    }
                                }
                            } else if (msg.equals("city")) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject jsonObject1 = data.getJSONObject(i);
                                    String areaNm1 = jsonObject1.getString("areaNm1");
                                    String areaNm2 = jsonObject1.getString("areaNm2");
                                    if (areaNm2.equals(keyword)) {
                                        String areaNm3 = jsonObject1.getString("areaNm3");
                                        if (areaNm3.equals("") || areaNm3.equals(Constant.THE_CITY)) {
                                            mShowCityList.add(new ShowCity(areaNm2, areaNm1, false));
                                        } else {
                                            mShowCityList.add(new ShowCity(areaNm3, areaNm2, false));
                                        }
                                        mSearchCityList.add(new SearchCityInfo(areaNm1, areaNm2, areaNm3));
                                    }
                                }
                            } else if (msg.equals("county")) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject jsonObject1 = data.getJSONObject(i);
                                    String areaNm1 = jsonObject1.getString("areaNm1");
                                    String areaNm2 = jsonObject1.getString("areaNm2");
                                    String areaNm3 = jsonObject1.getString("areaNm3");
                                    if (areaNm3.equals(keyword)) {
                                        if (areaNm3.equals("") || areaNm3.equals(Constant.THE_CITY)) {
                                            Logg.w(TAG, "error");
                                            mShowCityList.add(new ShowCity(areaNm2, areaNm2, false));
                                        } else {
                                            mShowCityList.add(new ShowCity(areaNm3, areaNm2, false));
                                        }
                                        mSearchCityList.add(new SearchCityInfo(areaNm1, areaNm2, areaNm3));
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showFail();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                        mShowCityAdapter.notifyDataSetChanged();
                        mListView.setSelection(0);
                    }
                });
            }
        });
    }
}