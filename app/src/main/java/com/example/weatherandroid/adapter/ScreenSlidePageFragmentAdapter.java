package com.example.weatherandroid.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Describe:Screen Slide Page Fragment Adapter
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class ScreenSlidePageFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;

    public ScreenSlidePageFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> list) {
        super(fm);
        mList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
