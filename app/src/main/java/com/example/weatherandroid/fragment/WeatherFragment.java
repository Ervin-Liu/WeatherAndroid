package com.example.weatherandroid.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherandroid.R;
import com.example.weatherandroid.activity.ListCityActivity;
import com.example.weatherandroid.adapter.WeatherPageAdapter;
import com.example.weatherandroid.bean.AllCityNameGps;
import com.example.weatherandroid.db.CityInfoBean;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.Utils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:Weather Fragment
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class WeatherFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "WeatherFragment";

    public enum City {
        北京, 天津, 上海, 重庆, 吉林, 香港, 澳门, 海南;
    }

    private static int sCurrentItem;

    private String mTitle;

    private View mView, mStatusView;
    private ViewPager mWeatherViewPager;
    private LinearLayout mLinearCityPoint;
    private ImageView mSetting, mSelectCity;

    private WeatherPageAdapter mWeatherPageAdapter;
    private List<Fragment> mFragmentList;
    private List<CityInfoBean> mAllCityInfo;
    private List<AllCityNameGps> mAllCityNameGps;
    private TextView mCityText;

    public WeatherFragment(String title) {
        mTitle = title;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_weather, container, false);
        init();
        eventListener();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFragmentList.clear();
        mAllCityNameGps.clear();
        mLinearCityPoint.removeAllViews();
        mAllCityInfo = LitePal.findAll(CityInfoBean.class);
        autoAddFragment();
        mWeatherPageAdapter = new WeatherPageAdapter(getChildFragmentManager(), mFragmentList);
        mWeatherViewPager.setAdapter(mWeatherPageAdapter);
        mWeatherViewPager.setCurrentItem(sCurrentItem);
        Logg.d(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logg.d(TAG, "onDestroy");
    }

    private void init() {
        //Set the height of the status bar
        autoSetBarSize();
        sCurrentItem = 0;
        mAllCityInfo = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        mAllCityNameGps = new ArrayList<>();

        mCityText = mView.findViewById(R.id.tv_city);
        mLinearCityPoint = mView.findViewById(R.id.linear_city_point);
        mWeatherViewPager = mView.findViewById(R.id.vp_weather);
        mSelectCity = mView.findViewById(R.id.iv_list_city);
    }

    private void eventListener() {
        mSelectCity.setOnClickListener(this);
        mWeatherViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logg.d(TAG, "onPageSelected---->position: " + position);
                int mapPosition = 0;
                for (AllCityNameGps allCityNameGps : mAllCityNameGps) {
                    String cityName = allCityNameGps.getCityName();
                    boolean cityGps = allCityNameGps.isCityGps();
                    ImageView imageView = mLinearCityPoint.findViewById(mapPosition);
                    if (mapPosition == position) {
                        imageView.setImageResource(R.drawable.ic_point_select);
                        mCityText.setText(cityName);
                        if (cityGps) {
                            Drawable nav_up=getResources().getDrawable(R.drawable.ic_gps);
                            nav_up.setBounds(-10, 0, (nav_up.getMinimumWidth()-12), (nav_up.getMinimumHeight()-12));
                            mCityText.setCompoundDrawables(nav_up, null, null, null);
                        } else {
                            mCityText.setCompoundDrawables(null, null, null, null);
                        }
                    } else {
                        imageView.setImageResource(R.drawable.ic_point_no_select);
                    }
                    mapPosition++;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Add a fragment that displays the weather dynamically
     */
    public void autoAddFragment() {
        int imageViewId = 0;
        for (CityInfoBean cityInfoBean : mAllCityInfo) {
            String showCityName = "";
            String county = cityInfoBean.getCounty();
            String city = cityInfoBean.getCity();
            String province = cityInfoBean.getProvince();
            boolean isGps = cityInfoBean.isIsGps();
            if (null != county && !county.equals("")) {
                Logg.d(TAG, "autoAddFragment---->add county name");
                if (county.equals(Constant.THE_CITY)) {
                    showCityName = city;
                } else {
                    showCityName = county;
                }
                setFragment(imageViewId, county, isGps, cityInfoBean);
                mAllCityNameGps.add(new AllCityNameGps(showCityName, isGps));
            } else if (null != city && !city.equals("")) {
                Logg.d(TAG, "autoAddFragment---->add city name");
                if (city.equals(Constant.THE_CITY)) {
                    showCityName = province;
                } else {
                    showCityName = city;
                }
                setFragment(imageViewId, city, isGps, cityInfoBean);
                mAllCityNameGps.add(new AllCityNameGps(showCityName, isGps));
            } else if (null != province && !province.equals("")) {
                for (City cityName : City.values()) {
                    String strCityName = String.valueOf(cityName);
                    if (strCityName.equals(province)) {
                        Logg.d(TAG, "autoAddFragment---->add province name");
                        setFragment(imageViewId, province, isGps, cityInfoBean);
                        mAllCityNameGps.add(new AllCityNameGps(province, isGps));
                        break;
                    }
                }
            } else {
                Logg.w(TAG, "autoAddFragment---->city name is error!");
            }
            imageViewId++;
        }
    }

    private void setFragment(int imageViewId, String cityName, boolean isGps, CityInfoBean cityInfoBean) {
        ImageView imageView = new ImageView(getActivity());
        if (imageViewId == 0) {
            mCityText.setText(cityName);
            imageView.setImageResource(R.drawable.ic_point_select);
            if (isGps) {
                Drawable nav_up=getResources().getDrawable(R.drawable.ic_gps);
                nav_up.setBounds(-10, 0, (nav_up.getMinimumWidth()-12), (nav_up.getMinimumHeight()-12));
                mCityText.setCompoundDrawables(nav_up, null, null, null);
            } else {
                mCityText.setCompoundDrawables(null, null, null, null);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_point_no_select);
        }
        LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(
                40,40);
        imageView.setLayoutParams(linearLp);
        imageView.setId(imageViewId);
        mLinearCityPoint.addView(imageView);
        mFragmentList.add(new SlideWeatherFragment(cityInfoBean));
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        mStatusView = mView.findViewById(R.id.view_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_list_city:
                ListCityActivity.startActivity(getActivity());
                break;
            default:
                Logg.w(TAG, " onclick---->unknown error!");
                break;
        }
    }

    public static void setCurrentItem (int currentItem) {
        sCurrentItem = currentItem;
    }

    public static int getCurrentItem() {
        return sCurrentItem;
    }
}
