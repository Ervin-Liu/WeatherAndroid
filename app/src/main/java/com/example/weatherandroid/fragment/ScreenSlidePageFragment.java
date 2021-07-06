package com.example.weatherandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.example.weatherandroid.R;
import com.example.weatherandroid.activity.ImageLookActivity;
import com.example.weatherandroid.adapter.ImageListAdapter;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.OkHttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Describe:ScreenSlidePageFragment
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class ScreenSlidePageFragment extends Fragment implements ImageListAdapter.OnItemClickListener, PullToRefreshListener {

    private static final String TAG = "ScreenSlidePageFragment";

    private View mView;
    private String mTitle;
    private ProgressBar mImagePro;

    private List<String> mImageUrlList;
    //    private RecyclerView mNewsRecyclerView;
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    private ImageListAdapter mImageListAdapter;

    public ScreenSlidePageFragment(String title) {
        mTitle = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_screenslidepage, container, false);
        init();
        Logg.d(TAG, "onCreateView----" + mTitle);
        return mView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Logg.d(TAG, "onAttach----" + mTitle);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logg.d(TAG, "onCreate----" + mTitle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logg.d(TAG, "onActivityCreated----" + mTitle);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshImage();
        Logg.d(TAG, "onStart----" + mTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logg.d(TAG, "onResume----" + mTitle);
    }

    @Override
    public void onPause() {
        super.onPause();
        Logg.d(TAG, "onPause----" + mTitle);
    }

    @Override
    public void onStop() {
        super.onStop();
        Logg.d(TAG, "onStop----" + mTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logg.d(TAG, "onDestroy----" + mTitle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logg.d(TAG, "onDestroyView----" + mTitle);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logg.d(TAG, "onDetach----" + mTitle);
    }

    private void init() {
        mImageUrlList = new ArrayList<>();
        mPullToRefreshRecyclerView = mView.findViewById(R.id.rv_img_list);
        mImagePro = mView.findViewById(R.id.image_pro);
        mPullToRefreshRecyclerView.setVisibility(View.GONE);
        mImagePro.setVisibility(View.VISIBLE);
        mImageListAdapter = new ImageListAdapter(mImageUrlList, getActivity(), mTitle);
        mImageListAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPullToRefreshRecyclerView.setLayoutManager(layoutManager);
//        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
//        mPullToRefreshRecyclerView.setLayoutManager(gridLayoutManager);
        mPullToRefreshRecyclerView.setAdapter(mImageListAdapter);
        //Whether to enable the drop-down refresh function
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        //Whether to enable pull-up loading
        mPullToRefreshRecyclerView.setLoadingMoreEnabled(true);
        //Sets whether the last refresh time is displayed
        mPullToRefreshRecyclerView.displayLastRefreshTime(true);
        //Set the refresh callback
        mPullToRefreshRecyclerView.setPullToRefreshListener(this);

    }

    private void refreshImage() {
        String url = Constant.sWeatherUrl + "queryImage?type=" + mTitle;
        OkHttpUtil.sendOkHttpRequestEnqueue(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showFail();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response != null) {
                    try {
                        String responseText = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseText);
                        String statusCode = jsonObject.getString("statusCode");
                        if (statusCode.equals("200")) {
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("ok")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                int length = data.length();
                                for (int i = 0; i < length; i++) {
                                    boolean is = false;
                                    String url = data.getJSONObject(i).getString("url");
                                    Logg.d(TAG, "URL IS" + mTitle);
                                    int size = mImageUrlList.size();
                                    if (size > 0) {
                                        for (int j = 0; j < mImageUrlList.size(); j++) {
                                            if (mImageUrlList.get(j).equals(url)) {
                                                is = true;
                                                break;
                                            }
                                        }
                                        if (!is) {
                                            mImageUrlList.add(url);
                                        }
                                    } else {
                                        mImageUrlList.add(url);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showFail();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Logg.d(TAG, "-----------------" +mTitle);
                        mPullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                        mImagePro.setVisibility(View.GONE);
                        mImageListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void showFail() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        mPullToRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshRecyclerView.setVisibility(View.GONE);
                mImagePro.setVisibility(View.VISIBLE);
                mPullToRefreshRecyclerView.setRefreshComplete();
                mImageUrlList.clear();
                refreshImage();
            }
        }, 1500);
    }

    @Override
    public void onLoadMore() {
        mPullToRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshRecyclerView.setLoadMoreComplete();
                refreshImage();
            }
        }, 1500);
    }

    @Override
    public void onItemClick(View view, int position) {
        String url = mImageUrlList.get(position);
        ImageLookActivity.startActivity(getActivity(), url);
    }
}
