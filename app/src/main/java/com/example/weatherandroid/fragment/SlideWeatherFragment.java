package com.example.weatherandroid.fragment;

import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherandroid.R;
import com.example.weatherandroid.activity.HomeActivity;
import com.example.weatherandroid.activity.SelectCityActivity;
import com.example.weatherandroid.adapter.AllWeatherInfoAdapter;
import com.example.weatherandroid.adapter.HourWeatherAdapter;
import com.example.weatherandroid.adapter.WeatherTypeAdapter;
import com.example.weatherandroid.db.CityInfoBean;
import com.example.weatherandroid.nowgson.Ct;
import com.example.weatherandroid.nowgson.FutureHour;
import com.example.weatherandroid.nowgson.LifeIndex;
import com.example.weatherandroid.nowgson.NowWeather;
import com.example.weatherandroid.nowgson.Uv;
import com.example.weatherandroid.nowgson.Xc;
import com.example.weatherandroid.nowgson.Xt;
import com.example.weatherandroid.sogson.Forecast;
import com.example.weatherandroid.sogson.SoJsonWeather;
import com.example.weatherandroid.util.CircleBarView;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.JsonUtils;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.OkHttpUtil;
import com.example.weatherandroid.util.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.yalantis.phoenix.PullToRefreshView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Describe:News Fragment
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class SlideWeatherFragment extends Fragment implements TextToSpeech.OnInitListener{

    private static final String TAG = "SlideWeatherFragment";

    private static final int REFRESH_SHOW = 0;
    private static final int REFRESH_DAY_ONE = 1;

    private CityInfoBean mCityInfoBean;

    private TextToSpeech mTextToSpeech;

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.CHINA);
//            textToSpeech.speak("Hello World", TextToSpeech.QUEUE_FLUSH, null);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getActivity(), "语音文件加载失败", Toast.LENGTH_SHORT).show();
            }
            mTextToSpeech.setPitch(0.5f);
            mTextToSpeech.setSpeechRate(1.0f);
        }
    }

    public enum City {
        北京, 天津, 上海, 重庆, 吉林, 香港, 澳门, 海南;
    }

    private View mView, mStatusView;
    private LineChart mLineChart;
    private TextView mTempTV, mDuTV, mNowType, mYesDayDate, mYesType, mYesTemp;
    private TextView mUvTitleTV, mUvResultTV, mUvContentTv;
//    private TextView mSportTitleTV, mSportResultTV, mSportContentTv;
    private TextView mClothesTitleTV, mClothesResultTV, mClothesContentTv;
    private TextView mCarTitleTV, mCarResultTV, mCarContentTv;
    private TextView mSugarTitleTV, mSugarResultTV, mSugarContentTv;
    private TextView mTipTitleTV, mTipResultTV, mTipContentTv;
    private RecyclerView mSevenRv, mHourRv;
    private ListView mAllWeatherListView;
    private Button mAirQur;
    private PullToRefreshView mPullToRefreshView;
    private ImageView mSpeakWeather;
    private TextView mDayOne;

    private String mProvince, mCity, mCounty;
    private SoJsonWeather mSoJsonWeather;
    private NowWeather mNowWeather;
    private List<Forecast> mSevenWeatherInfo;
    private List<Forecast> mSevenWeatherType;
    private List<Entry> mLowTemp, mHighTemp;
    private List<String> mLowTempStr, mHighTempStr;
    private LineDataSet mLowDataSet, mHighDataSet;
    private List<ILineDataSet> mSetData;
    private List<Forecast> mAllWeatherList;
    private List<Forecast> mAllForecast;
    private List<FutureHour> mHourWeatherList;
    private LineData mLineData;
    private WeatherTypeAdapter mWeatherTypeAdapter;
    private AllWeatherInfoAdapter mAllWeatherInfoAdapter;
    private String mQurCityName, mDayOneStr;
    private HourWeatherAdapter mHourWeatherAdapter;
    private TextView mFengXiang, mFengLi, mShiDu, mPM, mSunChu, mSunLuo;
    private CircleBarView mCircleBarView;
    private TextView mAQINum, mAQIShow;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_SHOW:
                    try{
                        //Set air quality
                        String quality = mSoJsonWeather.getData().getQuality();
                        mAirQur.setVisibility(View.VISIBLE);
                        mAirQur.setText(Constant.AIR_QUR + quality);
                        refreshChart();
                        refreshSevenWeatherType();
                        refreshYesterday();
                        refreshAllWeather();
                        refreshLife();
                        refreshHourWeather();
                        refreshSun();
                        mCircleBarView.setProgressNum(mSoJsonWeather.getData().getForecast().get(0).getAqi(), 1000);
                        mAQINum.setText(mSoJsonWeather.getData().getQuality());
                        mAQIShow.setText("" + mSoJsonWeather.getData().getForecast().get(0).getAqi());
                        //Set the temperature
                        mTempTV.setText(mSoJsonWeather.getData().getWendu());
                        mDuTV.setVisibility(View.VISIBLE);
                        mDuTV.setText(Constant.TEMP_UNIT_OTHER);
                        //Set the current weather conditions
                        String type = mSoJsonWeather.getData().getForecast().get(1).getType();
                        mNowType.setVisibility(View.VISIBLE);
                        mNowType.setText(type);
                        mSpeakWeather.setClickable(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case REFRESH_DAY_ONE:
                    mDayOne.setText(mDayOneStr);
                    mDayOne.setSelected(true);
                    break;
                default:
                    break;
            }

        }
    };

    public SlideWeatherFragment(CityInfoBean cityInfoBean) {
        mCityInfoBean = cityInfoBean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_slide_weather, container, false);
        init();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Logg.d(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        mStatusView = mView.findViewById(R.id.weather_status);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    private void autoSetScreen() {
        int realHeight = Utils.getRealHeight(getActivity());
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        float actionBarSize = HomeActivity.getActionBarSize();
        int a = (int) (realHeight - statusBarHeight - actionBarSize - 410);
        RelativeLayout test = mView.findViewById(R.id.rela_temp_day);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, a);
        test.setLayoutParams(Params1);
    }

    public void init() {
        autoSetBarSize();
        autoSetScreen();

        mTextToSpeech = new TextToSpeech(getActivity(), this);

        mQurCityName = "";
        mDayOneStr = "";

        mAirQur = mView.findViewById(R.id.tv_air_qur);
        mAirQur.setVisibility(View.GONE);
        mLineChart = mView.findViewById(R.id.line_chart);
        mTempTV = mView.findViewById(R.id.tv_weather_temp);
        mDuTV = mView.findViewById(R.id.tv_weather_du);
        mDuTV.setVisibility(View.GONE);
        mNowType = mView.findViewById(R.id.tv_now_type);
        mNowType.setVisibility(View.INVISIBLE);
        mSevenRv = mView.findViewById(R.id.rv_day_weather_type);
        mAllWeatherListView = mView.findViewById(R.id.lv_all_weather);
        mYesDayDate = mView.findViewById(R.id.tv_yesterday_day);
        mYesType = mView.findViewById(R.id.tv_yesterday_type);
        mYesTemp = mView.findViewById(R.id.tv_yesterday_temp);
        mLineChart.setNoDataText(getActivity().getString(R.string.get_weather_fail));
        mLineChart.setNoDataTextColor(Color.WHITE);
        mHourRv = mView.findViewById(R.id.rv_hour_weather);
        mFengXiang = mView.findViewById(R.id.tv_feng_xiang);
        mFengLi = mView.findViewById(R.id.tv_feng_li);
        mShiDu = mView.findViewById(R.id.tv_shi_du);
        mPM = mView.findViewById(R.id.tv_pm);
        mSunChu = mView.findViewById(R.id.tv_sun_chu);
        mSunLuo = mView.findViewById(R.id.tv_sun_luo);
        mCircleBarView = mView.findViewById(R.id.cb_pro);
        mCircleBarView.setMaxNum(500);
        mAQINum = mView.findViewById(R.id.tv_aqi_num);
        mAQIShow = mView.findViewById(R.id.tv_aqi_show);

        mUvTitleTV = mView.findViewById(R.id.tv_uv_title);
        mUvResultTV = mView.findViewById(R.id.tv_uv_result);
        mUvContentTv = mView.findViewById(R.id.tv_uv_content);
        mUvResultTV.setVisibility(View.GONE);

//        mSportTitleTV = mView.findViewById(R.id.tv_sport_title);
//        mSportResultTV = mView.findViewById(R.id.tv_sport_result);
//        mSportContentTv = mView.findViewById(R.id.tv_sport_content);

        mClothesTitleTV = mView.findViewById(R.id.tv_clothes_title);
        mClothesResultTV = mView.findViewById(R.id.tv_clothes_result);
        mClothesContentTv = mView.findViewById(R.id.tv_clothes_content);
        mClothesResultTV.setVisibility(View.GONE);

        mCarTitleTV = mView.findViewById(R.id.tv_car_title);
        mCarResultTV = mView.findViewById(R.id.tv_car_result);
        mCarContentTv = mView.findViewById(R.id.tv_car_content);
        mCarResultTV.setVisibility(View.GONE);

        mSugarTitleTV = mView.findViewById(R.id.tv_sugar_title);
        mSugarResultTV = mView.findViewById(R.id.tv_sugar_result);
        mSugarContentTv = mView.findViewById(R.id.tv_sugar_content);
        mSugarResultTV.setVisibility(View.GONE);

        mTipTitleTV = mView.findViewById(R.id.tv_tip_title);
        mTipResultTV = mView.findViewById(R.id.tv_tip_result);
        mTipContentTv = mView.findViewById(R.id.tv_tip_content);
        mTipResultTV.setVisibility(View.GONE);

        mPullToRefreshView = mView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryWeather();
            }
        });

        mSpeakWeather = mView.findViewById(R.id.iv_speak_weather);
        mSpeakWeather.setClickable(false);
        mSpeakWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
                    mTextToSpeech.setPitch(1f);
                    mTextToSpeech.setSpeechRate(0.8f);
                    String time = "";
                    String city = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    String nowDate = sdf.format(new Date());
                    String[] split = nowDate.split(":");
                    int hour = Integer.parseInt(split[0]);
                    if (hour < 12) {
                        time = "上午好";
                    } else if (hour < 18){
                        time = "下午好";
                    } else {
                        time = "晚上好";
                    }
                    if (!mCounty.equals("") && !mCounty.equals("城区")) {
                        city = mCounty;
                    } else if (!mCity.equals("") && !mCity.equals("城区")) {
                        city = mCity;
                    } else if (!mProvince.equals("") && !mProvince.equals("城区")) {
                        city = mProvince;
                    }
                    String speak = time + "..知心天气为您播报..现在时间为:..." + nowDate + "..." + city + "...今天天气.."
                            + mSoJsonWeather.getData().getForecast().get(0).getType()
                            + "...今天最高温度: " + mSoJsonWeather.getData().getForecast().get(0).getHigh().split(" ")[1]
                            + "...最低温度:" + mSoJsonWeather.getData().getForecast().get(0).getLow().split(" ")[1]
                            + "..." + mSoJsonWeather.getData().getForecast().get(0).getFx();
                    mTextToSpeech.speak(speak, TextToSpeech.QUEUE_FLUSH, null);

                } else {
                    Toast.makeText(getActivity(), "遇到些问题,请重试",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDayOne = mView.findViewById(R.id.tv_day_one);

        mSevenWeatherInfo = new ArrayList<>();
        mLowTemp = new ArrayList<>();
        mHighTemp = new ArrayList<>();
        mSetData = new ArrayList<>();
        mHighTempStr = new ArrayList<>();
        mLowTempStr = new ArrayList<>();
        mSevenWeatherType = new ArrayList<>();
        mAllWeatherList = new ArrayList<>();
        mHourWeatherList = new ArrayList<>();

        mProvince = mCityInfoBean.getProvince();
        mCity = mCityInfoBean.getCity();
        mCounty = mCityInfoBean.getCounty();

        queryWeather();

        queryDayOne();
    }

    private void queryDayOne() {
        String url = Constant.sWeatherUrl + "queryDayOne";
        OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showFail();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response != null && !response.equals("")) {
                    String responseStr = response.body().string();
                    if (!responseStr.equals("")) {
                        mDayOneStr = responseStr;
                        handler.sendEmptyMessage(REFRESH_DAY_ONE);
                    }
                } else {
                    showFail();
                }
            }
        });
    }

    private void showFail() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), Constant.GET_DAY_ONE_FAIL, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void queryWeather() {
        checkCityName();
        List<String> url = new ArrayList<>();
        String queryWeatherUrl = Constant.sWeatherUrl + "queryWeather?" + "province=" + mProvince + "&city=" + mCity + "&county=" + mCounty;
        String queryWeatherNewUrl = Constant.sWeatherUrl + "queryWeatherNew?" + "province=" + mProvince + "&city=" + mCity + "&county=" + mCounty;
        url.add(queryWeatherUrl);
        url.add(queryWeatherNewUrl);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String queryUrl : url) {
                    try {
                        Response response = OkHttpUtil.sendOkHttpRequestExecute(queryUrl);
                        if (response != null && !response.equals("")) {
                            final String responseText = response.body().string();
                            Logg.d(TAG, responseText);
                            if (queryUrl.equals(queryWeatherUrl)) {
                                mSoJsonWeather = null;
                                mSoJsonWeather = JsonUtils.handleWeatherResponse(responseText);
                                Logg.d(TAG, String.valueOf(mSoJsonWeather == null));
                            } else if (queryUrl.equals(queryWeatherNewUrl)) {
                                mNowWeather = null;
                                mNowWeather = JsonUtils.handleNowWeatherResponse(responseText);
                                Logg.d(TAG, String.valueOf(mNowWeather == null));
                            }
                        } else {
                            String fail = String.format(Constant.GET_WEATHER_FAIL, mQurCityName);
                            Toast.makeText(getActivity(), fail, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mSoJsonWeather != null && mNowWeather != null) {
                    handler.sendEmptyMessage(REFRESH_SHOW);
                }
                //stop refresh
                mPullToRefreshView.setRefreshing(false);
            }
        }).start();
    }

    private void checkCityName() {
        if (null != mCounty && !mCounty.equals("")) {
            mQurCityName= mCounty;
        } else if (null != mCity && !mCity.equals("")) {
            mQurCityName = mCity;
        } else if (null != mProvince && !mProvince.equals("")) {
            for (City name : City.values()) {
                String strCityName = String.valueOf(mQurCityName);
                if (strCityName.equals(mProvince)) {
                    mQurCityName = mProvince;
                    break;
                }
            }
        } else {
            Logg.w(TAG, "queryWeather---->city name is error!");
        }
    }

    private void refreshChart() {
        try {
            mLowTemp.clear();
            mHighTemp.clear();
            mSevenWeatherInfo = mSoJsonWeather.getData().getForecast();
            for (int i = 0; i < 7; i++) {
                //The high temperature
                String high = mSevenWeatherInfo.get(i).getHigh();
                mHighTempStr.add(high);
                String highTemp = ((high.split(" "))[1].split(Constant.TEMP_UNIT))[0];
                //The low temperature
                String low = mSevenWeatherInfo.get(i).getLow();
                mLowTempStr.add(low);
                String lowTemp = ((low.split(" "))[1].split(Constant.TEMP_UNIT))[0];
                mLowTemp.add(new Entry(i, Float.parseFloat(lowTemp)));
                mHighTemp.add(new Entry(i, Float.parseFloat(highTemp)));
            }
            mHighDataSet = new LineDataSet(mHighTemp, "");
            mLowDataSet = new LineDataSet(mLowTemp, "");
            //Default is line graph, this is graph
            mHighDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            mLowDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //set Customize display data styles
            mHighDataSet.setValueFormatter(new newValueFormatter());
            mLowDataSet.setValueFormatter(new newValueFormatter());
            //Sets the size of the line
            mHighDataSet.setLineWidth(2.5f);
            mLowDataSet.setLineWidth(2.5f);
            //Sets the size of the CircleRadius
            mHighDataSet.setCircleRadius(2.5f);
            mLowDataSet.setCircleRadius(2.5f);
            //set HighLightColor
            mHighDataSet.setHighLightColor(Color.rgb(244, 117, 117));
            mLowDataSet.setHighLightColor(Color.rgb(244, 117, 117));
            //Sets the color of the line
            mHighDataSet.setColor(Color.WHITE);
            mLowDataSet.setColor(Color.WHITE);
            //Set the color of the dot
            mHighDataSet.setCircleColor(Color.WHITE);
            mLowDataSet.setCircleColor(Color.WHITE);
            //Sets the font color of the display data
            mHighDataSet.setValueTextColor(Color.WHITE);
            mLowDataSet.setValueTextColor(Color.WHITE);
            //Sets the display numeric font size
            mHighDataSet.setValueTextSize(13);
            mLowDataSet.setValueTextSize(13);
            //add data
            mSetData.add(mLowDataSet);
            mSetData.add(mHighDataSet);
            mLineData = new LineData(mSetData);
            //Sets guides not visible
            mLineChart.getXAxis().setEnabled(false);
            mLineChart.getAxisLeft().setEnabled(false);
            mLineChart.getAxisRight().setEnabled(false);
            //Set Description not to be displayed
            mLineChart.getDescription().setEnabled(false);
            //Set background not to be displayed
            mLineChart.setDrawGridBackground(false);

            mLineChart.setTouchEnabled(false);
            mLineChart.setDragEnabled(false);
            mLineChart.setScaleEnabled(false);
            mLineChart.setScaleXEnabled(false);
            mLineChart.setScaleYEnabled(false);
            mLineChart.setPinchZoom(false);
            mLineChart.setDoubleTapToZoomEnabled(false);
            mLineChart.setDragDecelerationEnabled(false);
            mLineChart.setDragDecelerationFrictionCoef(0);

            Legend legend = mLineChart.getLegend();
            //Sets the prompt message not to be displayed
            legend.setFormSize(0);
            legend.setForm(Legend.LegendForm.CIRCLE);
            //Set the animation
            mLineChart.animateX(750);
            //Set data
            mLineChart.setData(mLineData);
            //refresh
            mLineChart.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSevenWeatherType() {
        mSevenWeatherType.clear();
        mSevenWeatherInfo = mSoJsonWeather.getData().getForecast();
        for (int i = 0; i < 7; i++) {
            Forecast forecast = mSevenWeatherInfo.get(i);
            mSevenWeatherType.add(forecast);
        }
        mWeatherTypeAdapter = new WeatherTypeAdapter(mSevenWeatherType, Utils.getWidthPixels(getActivity()), getActivity());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSevenRv.setLayoutManager(layoutManager);
        mSevenRv.setAdapter(mWeatherTypeAdapter);
    }

    private void refreshYesterday() {
        String date = mSoJsonWeather.getData().getYesterday().getYmd();
        String type = mSoJsonWeather.getData().getYesterday().getType();
        String high = mSoJsonWeather.getData().getYesterday().getHigh();
        String low = mSoJsonWeather.getData().getYesterday().getLow();
        String[] dataSplit = date.split(Constant.HOR_Line);
        String dataView = dataSplit[1] + Constant.SLASH + dataSplit[2];
        mYesDayDate.setText(dataView);
        mYesType.setText(type);
        String highTemp = ((high.split(" "))[1].split(Constant.TEMP_UNIT))[0] + Constant.TEMP_UNIT_OTHER;
        String lowTemp = ((low.split(" "))[1].split(Constant.TEMP_UNIT))[0] + Constant.TEMP_UNIT_OTHER;
        mYesTemp.setText(lowTemp + Constant.SLASH + highTemp);
    }

    private void refreshAllWeather() {
        mAllWeatherList.clear();
        mAllForecast = mSoJsonWeather.getData().getForecast();
        int size = mAllForecast.size();
        for (int i = 7; i < size; i ++) {
            mAllWeatherList.add(mAllForecast.get(i));
        }
        mAllWeatherInfoAdapter  = new AllWeatherInfoAdapter(getActivity(), R.layout.list_all_weather_info, mAllWeatherList);
        mAllWeatherListView.setAdapter(mAllWeatherInfoAdapter);
    }

    private void refreshLife() {
        LifeIndex lifeIndex = mNowWeather.getResult().getToday().getLifeIndex();
        Uv uv = lifeIndex.getUv();
        String uvLiNm = uv.getLiNm();
        String uvLiAttr = uv.getLiAttr();
        String uvLiDese = uv.getLiDese();
        mUvTitleTV.setText(uvLiNm);
        mUvResultTV.setVisibility(View.VISIBLE);
        mUvResultTV.setText(uvLiAttr);
        mUvContentTv.setText(uvLiDese);

        Ct ct = lifeIndex.getCt();
        String ctLiNm = ct.getLiNm();
        String ctLiAttr = ct.getLiAttr();
        String ctLiDese = ct.getLiDese();
        mClothesTitleTV.setText(ctLiNm);
        mClothesResultTV.setVisibility(View.VISIBLE);
        mClothesResultTV.setText(ctLiAttr);
        mClothesContentTv.setText(ctLiDese);

        Xc xc = lifeIndex.getXc();
        String xcLiNm = xc.getLiNm();
        String xcLiAttr = xc.getLiAttr();
        String xcLiDese = xc.getLiDese();
        mCarTitleTV.setText(xcLiNm);
        mCarResultTV.setVisibility(View.VISIBLE);
        mCarResultTV.setText(xcLiAttr);
        mCarContentTv.setText(xcLiDese);

        Xt xt = lifeIndex.getXt();
        String xtLiNm = xt.getLiNm();
        String xtLiAttr = xt.getLiAttr();
        String xtLiDese = xt.getLiDese();
        mSugarTitleTV.setText(xtLiNm);
        mSugarResultTV.setVisibility(View.VISIBLE);
        mSugarResultTV.setText(xtLiAttr);
        mSugarContentTv.setText(xtLiDese);

        String notice = mSoJsonWeather.getData().getForecast().get(0).getNotice();
        mTipTitleTV.setText("建议");
        mTipResultTV.setVisibility(View.VISIBLE);
        mTipResultTV.setText("tips");
        mTipContentTv.setText(notice);

    }

    private void refreshHourWeather() {
        mHourWeatherList.clear();
        List<FutureHour> futureHour = mNowWeather.getResult().getFutureHour();
        int length = futureHour.size();
        for (int i = 0; i < length; i++) {
            mHourWeatherList.add(futureHour.get(i));
        }
        Logg.d(TAG ,"-------=-=-=-=-=-=-=-" + mHourWeatherList.size());
//        mHourWeatherAdapter.notifyDataSetChanged();
        mHourWeatherAdapter = new HourWeatherAdapter(mHourWeatherList, getActivity());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHourRv.setLayoutManager(layoutManager);
        mHourRv.setAdapter(mHourWeatherAdapter);
    }

    public void refreshSun() {
        mFengXiang.setText(mSoJsonWeather.getData().getForecast().get(0).getFx());
        mFengLi.setText(mSoJsonWeather.getData().getForecast().get(0).getFl());
        mShiDu.setText(mSoJsonWeather.getData().getShidu());
        mPM.setText("" + mSoJsonWeather.getData().getPm25());
        mSunChu.setText(mSoJsonWeather.getData().getForecast().get(0).getSunrise());
        mSunLuo.setText(mSoJsonWeather.getData().getForecast().get(0).getSunset());
    }

    /**
     * Customize display data styles
     */
    class newValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int)value) + "℃";
        }
    }
}
