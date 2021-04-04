package com.example.weatherandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.weatherandroid.activity.GuideActivity;
import com.example.weatherandroid.R;

import java.util.ArrayList;

/**
 * Describe:guide app viewPager adapter
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class GuidePageAdapter extends PagerAdapter {

    private GuideActivity mGuideActivity;
    private ArrayList<View> mLaunchImgList = new ArrayList<>();

    private ImageView mGuideImg;

    public GuidePageAdapter(final GuideActivity guideActivity, int[] imgArrays) {
        this.mGuideActivity = guideActivity;
        int length = imgArrays.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                View view = LayoutInflater.from(mGuideActivity).inflate(R.layout.guide_view_pager, null);
                mGuideImg = view.findViewById(R.id.iv_guide);
                mGuideImg.setImageResource(imgArrays[i]);
                mLaunchImgList.add(view);
            }
        }
    }

    @Override
    public int getCount() {
        return mLaunchImgList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mLaunchImgList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mLaunchImgList.get(position));
        return mLaunchImgList.get(position);
    }
}
