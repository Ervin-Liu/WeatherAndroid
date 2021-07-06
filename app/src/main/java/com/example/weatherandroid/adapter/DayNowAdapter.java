package com.example.weatherandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.weatherandroid.R;
import com.example.weatherandroid.bean.DayNow;
import com.example.weatherandroid.bean.News;
import com.example.weatherandroid.util.Logg;

import java.util.List;
import java.util.UUID;

/**
 * Describe: list city adapter
 * <p>
 * Created by Ervin Liu on 2021/04/12---20:40
 **/
public class DayNowAdapter extends RecyclerView.Adapter<DayNowAdapter.ViewHolder> {

    private List<DayNow> mListDayNow;
    private Context mContext;

    public DayNowAdapter(List<DayNow> mListDayNow, Context mContext) {
        this.mListDayNow = mListDayNow;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_now_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DayNow dayNow = mListDayNow.get(position);
        Logg.d("TAG", "adapter=================");
        holder.mTime.setText("时间: " + dayNow.getTime());
        holder.mEvent.setText("事件: " + dayNow.getEvent());
    }

    @Override
    public int getItemCount() {
        if (null == mListDayNow || mListDayNow.size() == 0) {
            return 0;
        }
        return mListDayNow.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTime, mEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTime = itemView.findViewById(R.id.tv_time_day_now);
            mEvent = itemView.findViewById(R.id.tv_shi_jian);

        }

    }
}
