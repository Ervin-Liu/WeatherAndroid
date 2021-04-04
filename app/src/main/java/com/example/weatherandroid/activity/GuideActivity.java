package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.weatherandroid.R;
import com.example.weatherandroid.adapter.GuidePageAdapter;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.SPKeyValueHelper;
import com.example.weatherandroid.util.UIUtils;

/**
 * Describe:guide app viewPager adapter
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "GuideActivity";

    private final int[] mImgArray = {R.mipmap.ic_help_view_one
            , R.mipmap.ic_help_view_two
            , R.mipmap.ic_help_view_three
            , R.mipmap.ic_help_view_four};

    private ViewPager mGuideViewPager;
    private LinearLayout mLineGuide;
    private ImageView mGuideOne, mGuideTwo, mGuideThree, mGuideFour;
    private Button mStart;
    private TextView mSkip;

    private GuidePageAdapter mGuidePageAdapter;

    private boolean mIsFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        UIUtils.hideBottomUIMenu(this);
        //Whether it is the first time to enter the app
        mIsFirst = SPKeyValueHelper.get(Constant.IS_FIRST, true);
        if (mIsFirst) {
            init();
            setListener();
        } else {
            startActivity();
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

    private void init() {
        mGuideViewPager = findViewById(R.id.vp_guide);
        mLineGuide = findViewById(R.id.line_guide_point);
        mGuideOne = findViewById(R.id.iv_guide_point_one);
        mGuideTwo = findViewById(R.id.iv_guide_point_two);
        mGuideThree = findViewById(R.id.iv_guide_point_three);
        mGuideFour = findViewById(R.id.iv_guide_point_four);
        mSkip = findViewById(R.id.tv_skip);
        mStart = findViewById(R.id.btn_start_app);
        mGuidePageAdapter = new GuidePageAdapter(this, mImgArray);
        mGuideViewPager.setAdapter(mGuidePageAdapter);
        mGuideViewPager.setCurrentItem(0);
    }

    /**
     * set listener
     */
    private void setListener() {
        mSkip.setOnClickListener(this);
        mStart.setOnClickListener(this);
        mGuideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Set the remote point, etc., based on the ViewPager location
     * @param position
     */
    private void selectPoint(int position) {
        Logg.d(TAG, ""  + position);
        switch (position) {
            case 0:
                mLineGuide.setVisibility(View.VISIBLE);
                mSkip.setVisibility(View.VISIBLE);
                mStart.setVisibility(View.GONE);
                mGuideOne.setImageResource(R.mipmap.img_guide_point_select);
                mGuideTwo.setImageResource(R.mipmap.img_guide_point);
                mGuideThree.setImageResource(R.mipmap.img_guide_point);
                mGuideFour.setImageResource(R.mipmap.img_guide_point);
                break;
            case 1:
                mLineGuide.setVisibility(View.VISIBLE);
                mSkip.setVisibility(View.VISIBLE);
                mStart.setVisibility(View.GONE);
                mGuideOne.setImageResource(R.mipmap.img_guide_point);
                mGuideTwo.setImageResource(R.mipmap.img_guide_point_select);
                mGuideThree.setImageResource(R.mipmap.img_guide_point);
                mGuideFour.setImageResource(R.mipmap.img_guide_point);
                break;
            case 2:
                mLineGuide.setVisibility(View.VISIBLE);
                mSkip.setVisibility(View.VISIBLE);
                mStart.setVisibility(View.GONE);
                mGuideOne.setImageResource(R.mipmap.img_guide_point);
                mGuideTwo.setImageResource(R.mipmap.img_guide_point);
                mGuideThree.setImageResource(R.mipmap.img_guide_point_select);
                mGuideFour.setImageResource(R.mipmap.img_guide_point);
                break;
            case 3:
                mLineGuide.setVisibility(View.GONE);
                mSkip.setVisibility(View.GONE);
                mStart.setVisibility(View.VISIBLE);
                break;
            default:
                Logg.d(TAG, "selectPoint---->viewpager number error!");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_skip:
            case R.id.btn_start_app:
                startActivity();
                break;
            default:
                Logg.d(TAG, "onClick---->unknown error!");
                break;
        }
    }

    /**
     * start HomeActivity
     */
    private void startActivity() {
        SPKeyValueHelper.put(Constant.IS_FIRST, false);
        HomeActivity.startActivity(GuideActivity.this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logg.d(TAG, "onResume-----");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logg.d(TAG, "onDestroy-----");
    }
}