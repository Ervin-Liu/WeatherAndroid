package com.example.weatherandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherandroid.R;
import com.example.weatherandroid.adapter.ScreenSlidePageFragmentAdapter;
import com.example.weatherandroid.util.Utils;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe:Video Fragment
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class VideoFragment extends Fragment {

    private String mTitle;
    private View mView, mStatusView;

    private ViewPager mViewPager;
    private BubbleNavigationConstraintView mNavigationConstraintView;
    private List<Fragment> mFragmentList;
    private ScreenSlidePageFragmentAdapter mAdapter;

    public VideoFragment(String title) {
        mTitle = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video, container, false);
        Utils.setAndroidNativeLightStatusBar(getActivity(), true);
        init();
        return mView;
    }

    private void init() {
        autoSetBarSize();
        mFragmentList = new ArrayList<>();

        mViewPager = (ViewPager) mView.findViewById(R.id.view_pager);
        mNavigationConstraintView = (BubbleNavigationConstraintView) mView.findViewById(R.id.top_navigation_constraint);

        mFragmentList.add(new ScreenSlidePageFragment("动漫"));
        mFragmentList.add(new ScreenSlidePageFragment("风景"));
        mFragmentList.add(new ScreenSlidePageFragment("萌宠"));

        mAdapter = new ScreenSlidePageFragmentAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNavigationConstraintView.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mNavigationConstraintView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                mViewPager.setCurrentItem(position, true);
            }
        });
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        mStatusView = mView.findViewById(R.id.view_status);
        RelativeLayout.LayoutParams Params1 = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
        mStatusView.setBackground(getActivity().getResources().getDrawable(R.color.blue_active));
    }
}
