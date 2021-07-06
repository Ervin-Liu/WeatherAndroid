package com.example.weatherandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherandroid.R;
import com.example.weatherandroid.bean.ShowCity;

import java.util.List;

/**
 * Describe:SelectShowCityAdapter
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class SelectShowCityAdapter extends ArrayAdapter<ShowCity> {

    private int mResourceId;

    public SelectShowCityAdapter(Context context, int textViewResourceId,
                                 List<ShowCity> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ShowCity showCity = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mCityTV = view.findViewById(R.id.tv_city);
            viewHolder.mParentCityTV = view.findViewById(R.id.tv_parent_city);
            viewHolder.mGpsImage = view.findViewById(R.id.rela_your_gps);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (showCity.isIsGps()) {
            viewHolder.mGpsImage.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mGpsImage.setVisibility(View.GONE);
        }
        viewHolder.mParentCityTV.setText(showCity.getParentCity());
        viewHolder.mCityTV.setText(showCity.getCity());
        return view;
    }

    class ViewHolder {
        TextView mCityTV, mParentCityTV;
        RelativeLayout mGpsImage;
    }

}
