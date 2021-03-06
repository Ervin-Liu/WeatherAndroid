package com.example.weatherandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherandroid.R;
import com.example.weatherandroid.sogson.Forecast;
import com.example.weatherandroid.util.Constant;

import java.util.List;

/**
 * Describe:AllWeatherInfoAdapter
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class AllWeatherInfoAdapter extends ArrayAdapter<Forecast> {

    private int mResourceId;

    public AllWeatherInfoAdapter(Context context, int textViewResourceId,
                                 List<Forecast> objects) {
        super(context, textViewResourceId, objects);
        mResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Forecast forecast = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(mResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mWeekDate = view.findViewById(R.id.tv_week_data);
            viewHolder.mDate = view.findViewById(R.id.tv_data);
            viewHolder.mTypeIV = view.findViewById(R.id.iv_all_weather_type);
            viewHolder.mTemp = view.findViewById(R.id.tv_all_weather_temp);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        String[] dates = forecast.getYmd().split(Constant.HOR_Line);
        String week = (forecast.getWeek().split(Constant.WEEK_SOME_ONE))[1];
        String highTemp = (((forecast.getHigh().split(" "))[1]).split(Constant.TEMP_UNIT))[0] + Constant.TEMP_UNIT_OTHER;
        String lowTemp = (((forecast.getLow().split(" "))[1]).split(Constant.TEMP_UNIT))[0] + Constant.TEMP_UNIT_OTHER;
        viewHolder.mWeekDate.setText(Constant.WEEK + week);
        viewHolder.mDate.setText(dates[1] + Constant.SLASH + dates[2]);
        viewHolder.mTemp.setText(lowTemp + Constant.SLASH + highTemp);
        switch (forecast.getType()) {
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_bao_xue));
                break;
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_bin_bao));
                break;
            case "?????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_da_bao_yu));
                break;
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_da_xue));
                break;
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_da_yu));
                break;
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_duo_yun));
                break;
            case "?????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_lei_zhen_yu));
                break;
            case "????????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic__qiang_lei_zhen_yu));
                break;
            case "?????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_qing_du_mai));
                break;
            case "???":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_qing));
                break;
            case "????????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic__te_qiang_nong_wu));
                break;
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_xiao_xue));
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_xiao_yu));
                break;
            case "???":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_yin));
                break;
            case "?????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_yu_jia_xue));
                break;
            case "?????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_zhong_mai));
                break;
            case "?????????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_zhong_wu));
                break;
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_zhong_xue));
                break;
            case "??????":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_zhong_yu));
                break;
            case "???":
                viewHolder.mTypeIV.setBackground(getContext().getResources().getDrawable(R.drawable.ic_qing_du_mai));
                break;
            default:
                break;
        }
        return view;
    }

    class ViewHolder {
        TextView mWeekDate, mDate, mTemp;
        ImageView mTypeIV;
    }

}
