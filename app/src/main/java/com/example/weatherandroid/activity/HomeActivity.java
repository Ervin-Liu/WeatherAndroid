package com.example.weatherandroid.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weatherandroid.R;
import com.example.weatherandroid.adapter.ScreenSlidePageFragmentAdapter;
import com.example.weatherandroid.application.WeatherApplication;
import com.example.weatherandroid.db.CityInfoBean;
import com.example.weatherandroid.dialog.CommDialog;
import com.example.weatherandroid.fragment.MyAccountFragment;
import com.example.weatherandroid.fragment.NewsFragment;
import com.example.weatherandroid.fragment.VideoFragment;
import com.example.weatherandroid.fragment.WeatherFragment;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.SPKeyValueHelper;
import com.example.weatherandroid.util.Utils;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements CommDialog.OnClickListener {

    private static final String TAG = "HomeActivity";
    private static boolean sIsGpsOpen;

    public enum City {
        北京, 天津, 上海, 重庆, 吉林, 香港, 澳门, 海南;
    }

    private static float sActionBarSize;

    private ViewPager mMainViewPager;
    private ScreenSlidePageFragmentAdapter mFragmentAdapter;
    private BubbleNavigationLinearView mBubbleNavigationLinearView;

    private long mFirstPressedTime;
    private List<Fragment> mFragmentList;
    private List<String> mPermissionList;
    private LocationClient mLocationClient;
    private LocationClientOption mLocationOption;
    private CommDialog commDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        event();
        permissionRequest();
        sActionBarSize = getActionBarHeight();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterLocation();
    }

    private void init() {
        mFragmentList = new ArrayList<>();
        mPermissionList = new ArrayList<>();
        sIsGpsOpen = false;

        mMainViewPager = (ViewPager) findViewById(R.id.vp_main_fragment);
        mBubbleNavigationLinearView = (BubbleNavigationLinearView) findViewById(R.id.bnlv_linear);

        mPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        mPermissionList.add(Manifest.permission.READ_PHONE_STATE);
        mPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        mFragmentList.add(new WeatherFragment(Constant.WEATHER));
        mFragmentList.add(new NewsFragment(Constant.NEWS));
        mFragmentList.add(new VideoFragment(Constant.VIDEO));
        mFragmentList.add(new MyAccountFragment(Constant.MY_ACCOUNT));

        setAdapter();
    }

    /**
     * set adapter
     */
    private void setAdapter() {
        mMainViewPager.removeAllViews();
        mFragmentAdapter = new ScreenSlidePageFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mMainViewPager.setAdapter(mFragmentAdapter);
        mMainViewPager.setCurrentItem(0);
    }

    private void event() {
        // ViewPager swipe to switch and change the navigation bar
        mMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBubbleNavigationLinearView.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //The navigation bar toggle changes the ViewPager
        mBubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                mMainViewPager.setCurrentItem(position, true);
            }
        });
    }

    private void permissionRequest() {
        PermissionX.init(this)
                //List of permissions to apply for
                .permissions(mPermissionList)
                //Used to explain the reason to the user before the permission request begins
                .explainReasonBeforeRequest()
                //Sets the dialog positiveButton and negativeButton font colors
                .setDialogTintColor(Color.parseColor("#008577"), Color.parseColor("#83e8dd"))
                //Click "Dialog" to forbid the pop-up, click "I understand",
                //It will pop up again to request permission,
                //Click Cancel and the pop-up window will pop up again the next time you enter the application
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, getString(R.string.permission_request), getString(R.string.i_known));
                    }
                })
                //The dialog popup will no longer be asked after clicking the "Ban" button.
                //Click "I have understood", and the setting interface will guide the user to open permissions.
                // If all permissions are not opened, the dialog will not disappear.
                //Click Cancel and the dialog will pop up again the next time you enter.
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, getString(R.string.set_permission), getString(R.string.i_known), getString(R.string.i_cancel));
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            Logg.d(TAG, "All applications have been approved");
                            onGps();
                        } else {
                            Logg.w(TAG, "You have denied the following permissions: " + deniedList);
                            if (deniedList.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                Logg.w(TAG, "You do not authorize location information!");
                                Toast.makeText(HomeActivity.this, getString(R.string.location_off), Toast.LENGTH_SHORT);
                                unregisterLocation();
                            } else {
                                onGps();
                            }
                        }
                        SPKeyValueHelper.put(Constant.IS_FIRST_GPS, false);
                    }
                });
    }

    /**
     * get ActionBar height
     *
     * @return
     */
    private float getActionBarHeight() {
        TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        return actionBarHeight;
    }

    private void registerLocation() {
        unregisterLocation();
        mLocationClient = new LocationClient(WeatherApplication.getApplication());
        mLocationOption = new LocationClientOption();
        //Sets whether address information is required. Default does not
        mLocationOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(mLocationOption);
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.start();
    }

    private void unregisterLocation() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(new MyLocationListener());
            mLocationClient = null;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    /**
     * Press the return key twice to exit the program
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mFirstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(HomeActivity.this, getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
            mFirstPressedTime = System.currentTimeMillis();
        }
    }

    private void onGps() {
        //Is it the first time to enter the APP location
        boolean isFirstGps = SPKeyValueHelper.get(Constant.IS_FIRST_GPS, true);
        //Whether to turn on GPS
        boolean isGpsOpen = Utils.isGpsOpen(HomeActivity.this);
        Logg.d(TAG, "onGps---->isFirstGps:" + isFirstGps + " isGpsOpen:" + isGpsOpen);
        if (isFirstGps && !isGpsOpen) {
            openGPS();
        } else if (!isFirstGps && !isGpsOpen) {
            sIsGpsOpen = false;
        } else {
            sIsGpsOpen = true;
        }
        if (sIsGpsOpen) {
            registerLocation();
        }
    }

    private void openGPS() {
        commDialog = new CommDialog(HomeActivity.this);
        commDialog.setTitle(getString(R.string.gps_switch));
        commDialog.setContent(getString(R.string.gps_off));
        commDialog.setOk(getString(R.string.open));
        commDialog.setCancel(getString(R.string.i_cancel));
        commDialog.setOnClickListener(this);
        commDialog.show();
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
        if (null != commDialog) {
            commDialog.dismiss();
            commDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            sIsGpsOpen = Utils.isGpsOpen(HomeActivity.this);
            if (sIsGpsOpen) {
                dismissDialog();
                registerLocation();
            }
        }
    }

    public static float getActionBarSize() {
        return sActionBarSize;
    }

    /**
     * Implement location callbacks
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String country = bdLocation.getCountry();
            String province = bdLocation.getProvince().substring(0,(bdLocation.getProvince().length()-1));
            String city = bdLocation.getCity().substring(0,(bdLocation.getCity().length()-1));
            String district = bdLocation.getDistrict().substring(0,(bdLocation.getDistrict().length()-1));
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
                    Toast.makeText(HomeActivity.this, getString(R.string.gps_error),Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Locate save to database
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
        setAdapter();
    }

    private void saveData(String province, String city, String county, boolean isGps) {
        CityInfoBean gpsCity = new  CityInfoBean();
        gpsCity.setProvince(province);
        gpsCity.setCity(city);
        gpsCity.setCounty(county);
        gpsCity.setIsGps(isGps);
        gpsCity.save();
    }
}