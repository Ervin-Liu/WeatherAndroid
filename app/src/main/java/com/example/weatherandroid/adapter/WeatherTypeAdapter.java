package com.example.weatherandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherandroid.R;
import com.example.weatherandroid.sogson.Forecast;
import com.example.weatherandroid.util.Constant;

import java.util.List;

/**
 * Describe: list city adapter
 * <p>
 * Created by Ervin Liu on 2021/04/12---20:40
 **/
public class WeatherTypeAdapter extends RecyclerView.Adapter<WeatherTypeAdapter.ViewHolder> {

    private List<Forecast> mForecastList;
    private int mWidthPixels;
    private Context mContext;

    public WeatherTypeAdapter(List<Forecast> mForecastList, int widthPixels, Context mContext) {
        this.mForecastList = mForecastList;
        this.mWidthPixels = widthPixels;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_day_weather_type, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Forecast forecast = mForecastList.get(position);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                (mWidthPixels - 100) / mForecastList.size(), ViewGroup.LayoutParams.MATCH_PARENT);
        holder.mLinearType.setLayoutParams(Params1);
        if (position == 0) {
            holder.mDayTV.setText(Constant.TODAY);
        } else {
            String day = Constant.WEEK + ((forecast.getWeek()).split(Constant.WEEK_SOME_ONE)[1]);
            holder.mDayTV.setText(day);
        }
        String type = forecast.getType();
        holder.mTypeTV.setText(type);
        switch (type) {
            case "暴雪":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bao_xue));
                break;
            case "冰雹":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bin_bao));
                break;
            case "大暴雨":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_da_bao_yu));
                break;
            case "大雪":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_da_xue));
                break;
            case "大雨":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_da_yu));
                break;
            case "多云":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_duo_yun));
                break;
            case "雷阵雨":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_lei_zhen_yu));
                break;
            case "强雷阵雨":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic__qiang_lei_zhen_yu));
                break;
            case "轻度霾":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_qing_du_mai));
                break;
            case "晴":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_qing));
                break;
            case "特强浓雾":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic__te_qiang_nong_wu));
                break;
            case "小雪":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_xiao_xue));
            case "小雨":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_xiao_yu));
                break;
            case "阴":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_yin));
                break;
            case "雨夹雪":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_yu_jia_xue));
                break;
            case "中度霾":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_mai));
                break;
            case "中度雾":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_wu));
                break;
            case "中雪":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_xue));
                break;
            case "中雨":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_yu));
                break;
            case "霾":
                holder.mTypeIv.setBackground(mContext.getResources().getDrawable(R.drawable.ic_qing_du_mai));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (null == mForecastList || mForecastList.size() == 0) {
            return 0;
        }
        return mForecastList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLinearType;
        TextView mDayTV, mTypeTV;
        ImageView mTypeIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLinearType = itemView.findViewById(R.id.linear_type);
            mDayTV = itemView.findViewById(R.id.tv_day);
            mTypeTV = itemView.findViewById(R.id.tv_type);
            mTypeIv = itemView.findViewById(R.id.iv_weather_type);
        }

    }
}
