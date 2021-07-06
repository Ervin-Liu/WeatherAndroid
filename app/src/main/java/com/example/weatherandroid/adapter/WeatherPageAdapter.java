package com.example.weatherandroid.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Describe:WeatherPageAdapter
 * <p>
 * Created by Ervin Liu on 2021/04/07---17:19
 **/
public class WeatherPageAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mList;

    public WeatherPageAdapter(@NonNull FragmentManager fm, List<Fragment> list) {
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
