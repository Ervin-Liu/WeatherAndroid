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
import com.example.weatherandroid.bean.News;

import java.util.List;
import java.util.UUID;

/**
 * Describe: list city adapter
 * <p>
 * Created by Ervin Liu on 2021/04/12---20:40
 **/
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder>{

    private List<String> mImageUrlList;
    private OnItemClickListener mClickListener;
    private Context mContext;
    private String mTitle;

    public ImageListAdapter(List<String> mImageUrlList, Context mContext, String mTitle) {
        this.mImageUrlList = mImageUrlList;
        this.mContext = mContext;
        this.mTitle = mTitle;
    }

    public void setClickListener(OnItemClickListener onItemClickListener) {
        this.mClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_item, parent, false);
        final ViewHolder holder = new ViewHolder(view, mClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = mImageUrlList.get(position);
        holder.mTitle.setText(mTitle);
        Glide.with(mContext)
                .load(url)
                //Close the cache
                .skipMemoryCache(true)
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into(holder.mIvNewImg);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mImageUrlList || mImageUrlList.size() == 0) {
            return 0;
        }
        return mImageUrlList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvNewImg;
        TextView mTitle;
        OnItemClickListener mOnItemClickListener;
        View view;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            mIvNewImg = itemView.findViewById(R.id.iv_image_image);
            mTitle = itemView.findViewById(R.id.tv_image_title);
            this.mOnItemClickListener = onItemClickListener;
            view = itemView;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
