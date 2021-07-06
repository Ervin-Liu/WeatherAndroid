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
import com.example.weatherandroid.nowgson.FutureHour;
import com.example.weatherandroid.sogson.Forecast;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;

import java.util.List;

/**
 * Describe: list city adapter
 * <p>
 * Created by Ervin Liu on 2021/04/12---20:40
 **/
public class HourWeatherAdapter extends RecyclerView.Adapter<HourWeatherAdapter.ViewHolder> {

    private List<FutureHour> mFutureHourList;
    private Context mContext;

    public HourWeatherAdapter(List<FutureHour> mFutureHourList, Context mContext) {
        this.mFutureHourList = mFutureHourList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_hour_weather, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FutureHour futureHour = mFutureHourList.get(position);
        String dateYmdh = futureHour.getDateYmdh();
        String[] dateYmdhSplit = dateYmdh.split(" ");
        String[] split = dateYmdhSplit[1].split(":");
        holder.mTimeTV.setText(split[0] + ":" + split[1]);
        holder.mTempTV.setText(futureHour.getWtTemp() + "°");
        String type = futureHour.getWtNm();
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
        if (null == mFutureHourList || mFutureHourList.size() == 0) {
            return 0;
        }
        return mFutureHourList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLinearType;
        TextView mTimeTV, mTempTV;
        ImageView mTypeIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLinearType = itemView.findViewById(R.id.linear_hour_weather_re);
            mTimeTV = itemView.findViewById(R.id.tv_hour_time);
            mTempTV = itemView.findViewById(R.id.tv_temp);
            mTypeIv = itemView.findViewById(R.id.iv_hour_weather_type);
        }

    }
}
