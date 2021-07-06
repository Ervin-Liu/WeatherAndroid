package com.example.weatherandroid.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherandroid.R;
import com.example.weatherandroid.bean.ListCity;

import java.util.List;

/**
 * Describe: list city adapter
 * <p>
 * Created by Ervin Liu on 2021/04/12---20:40
 **/
public class ListCityAdapter extends RecyclerView.Adapter<ListCityAdapter.ViewHolder> {

    private List<ListCity> mListCity;
    private OnItemClickListener mClickListener;
    private Context mContext;
    private static boolean sIsDelete;

    public ListCityAdapter(Context context, List<ListCity> mListCity) {
        this.mContext = context;
        this.mListCity = mListCity;
        sIsDelete = false;
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.mClickListener = onItemClickListener;
    }

    public void setIDelete(boolean isDelete) {
        sIsDelete = isDelete;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_city, parent, false);
        final ViewHolder holder = new ViewHolder(view, mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListCity listCity = mListCity.get(position);
        String cityName = listCity.getCityName();
        String temp = listCity.getTemp();
        boolean isGps = listCity.isIsGps();
        if (sIsDelete) {
            holder.mDeleteRela.setVisibility(View.VISIBLE);
            holder.mCityListRela.setClickable(false);
            holder.mDeleteRela.setClickable(true);
        } else {
            holder.mDeleteRela.setVisibility(View.GONE);
            holder.mCityListRela.setClickable(true);
            holder.mDeleteRela.setClickable(false);
        }
        if (isGps) {
            Drawable nav_up = mContext.getResources().getDrawable(R.drawable.ic_gps);
            nav_up.setBounds(-10, 0, (nav_up.getMinimumWidth() - 12), (nav_up.getMinimumHeight() - 12));
            holder.mCityName.setCompoundDrawables(nav_up, null, null, null);
        } else {
            holder.mCityName.setCompoundDrawables(null, null, null, null);
        }
        holder.mCityName.setText(cityName);
        holder.mCityTemp.setText(temp);
        switch (listCity.getType()) {
            case "暴雪":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bao_xue));
                break;
            case "冰雹":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_bin_bao));
                break;
            case "大暴雨":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_da_bao_yu));
                break;
            case "大雪":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_da_xue));
                break;
            case "大雨":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_da_yu));
                break;
            case "多云":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_duo_yun));
                break;
            case "雷阵雨":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_lei_zhen_yu));
                break;
            case "强雷阵雨":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic__qiang_lei_zhen_yu));
                break;
            case "轻度霾":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_qing_du_mai));
                break;
            case "晴":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_qing));
                break;
            case "特强浓雾":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic__te_qiang_nong_wu));
                break;
            case "小雪":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_xiao_xue));
            case "小雨":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_xiao_yu));
                break;
            case "阴":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_yin));
                break;
            case "雨夹雪":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_yu_jia_xue));
                break;
            case "中度霾":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_mai));
                break;
            case "中度雾":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_wu));
                break;
            case "中雪":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_xue));
                break;
            case "中雨":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_zhong_yu));
                break;
            case "霾":
                holder.mWeatherIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_qing_du_mai));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (null == mListCity || mListCity.size() == 0) {
            return 0;
        }
        return mListCity.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mCityListRela, mDeleteRela;
        LinearLayout mLinearInfo;
        ImageView mWeatherIcon, mDeleteIcon;
        TextView mCityName, mCityTemp;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            mCityListRela = itemView.findViewById(R.id.rela_cit_list);
            mDeleteRela = itemView.findViewById(R.id.rela_delete);
            mLinearInfo = itemView.findViewById(R.id.linear_city_name_temp);
            mWeatherIcon = itemView.findViewById(R.id.iv_weather_icon);
            mDeleteIcon = itemView.findViewById(R.id.iv_delete_icon);
            mCityName = itemView.findViewById(R.id.tv_city_name);
            mCityTemp = itemView.findViewById(R.id.tv_city_temp);

            mOnItemClickListener = onItemClickListener;

            mCityListRela.setOnClickListener(this);
            mDeleteRela.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.rela_cit_list:
                    mOnItemClickListener.onListItemClick(view, getAdapterPosition());
                    break;
                case R.id.rela_delete:
                    mOnItemClickListener.onDeleteItemClick(view, getAdapterPosition());
                    break;
                default:
                    break;
            }

        }
    }

    public interface OnItemClickListener {
        void onListItemClick(View view, int position);

        void onDeleteItemClick(View view, int position);
    }
}
