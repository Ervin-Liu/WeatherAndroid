package com.example.weatherandroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.example.weatherandroid.R;
import com.example.weatherandroid.activity.NewsDetailsActivity;
import com.example.weatherandroid.adapter.NewsListAdapter;
import com.example.weatherandroid.bean.News;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.Logg;
import com.example.weatherandroid.util.OkHttpUtil;
import com.example.weatherandroid.util.Utils;

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
 * Describe:News Fragment
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class NewsFragment extends Fragment implements NewsListAdapter.OnItemClickListener, PullToRefreshListener {

    private static final String TAG = "NewsFragment";
    private static int sNewsStart = 0;

    private String mTitle;
    private View mView, mStatusView;
    private List<News> mListNews;
    //    private RecyclerView mNewsRecyclerView;
    private PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private ProgressBar mProgressBar;

    private NewsListAdapter mNewsListAdapter;

    public NewsFragment(String title) {
        mTitle = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_news, container, false);
        Utils.setAndroidNativeLightStatusBar(getActivity(), true);
        autoSetBarSize();
        init();
        return mView;
    }

    private void init() {
        mListNews = new ArrayList<>();
        mPullToRefreshRecyclerView = mView.findViewById(R.id.rv_new_list);
        mProgressBar = mView.findViewById(R.id.new_pb);
        mProgressBar.setVisibility(View.VISIBLE);
        mPullToRefreshRecyclerView.setVisibility(View.GONE);
        mNewsListAdapter = new NewsListAdapter(mListNews, getActivity());
        mNewsListAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPullToRefreshRecyclerView.setLayoutManager(layoutManager);
        mPullToRefreshRecyclerView.setAdapter(mNewsListAdapter);
        //Whether to enable the drop-down refresh function
        mPullToRefreshRecyclerView.setPullRefreshEnabled(true);
        //Whether to enable pull-up loading
        mPullToRefreshRecyclerView.setLoadingMoreEnabled(true);
        //Sets whether the last refresh time is displayed
        mPullToRefreshRecyclerView.displayLastRefreshTime(true);
        //Set the refresh callback
        mPullToRefreshRecyclerView.setPullToRefreshListener(this);
        refreshNews(String.valueOf(sNewsStart));
    }

    private void refreshNews(String start) {
        String url = Constant.sWeatherUrl + "queryNews?start=" + start;
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
                                    String title = data.getJSONObject(i).getString("title");
                                    String time = data.getJSONObject(i).getString("time");
                                    String pic = data.getJSONObject(i).getString("pic");
                                    String weburl = data.getJSONObject(i).getString("weburl");
                                    mListNews.add(new News(title, time, pic, weburl));
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
                        mProgressBar.setVisibility(View.GONE);
                        mPullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                        mNewsListAdapter.notifyDataSetChanged();
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
    public void onDestroy() {
        super.onDestroy();
        Logg.d(TAG, "onDestroy");
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(getActivity());
        mStatusView = mView.findViewById(R.id.view_news_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
        mStatusView.setBackground(getActivity().getResources().getDrawable(R.color.blue_active));
    }

    @Override
    public void onItemClick(View view, int position) {
        try {
            Logg.d(TAG, "--------" + position);
            String webUrl = mListNews.get(position).getWebUrl();
            NewsDetailsActivity.startActivity(getActivity(), webUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        mPullToRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.VISIBLE);
                mPullToRefreshRecyclerView.setVisibility(View.GONE);
                mPullToRefreshRecyclerView.setRefreshComplete();
                mListNews.clear();
                sNewsStart = 0;
                refreshNews(String.valueOf((sNewsStart)));
            }
        }, 1500);

    }

    @Override
    public void onLoadMore() {
        mPullToRefreshRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshRecyclerView.setLoadMoreComplete();
                sNewsStart += 10;
                refreshNews(String.valueOf((sNewsStart)));
            }
        }, 1500);
    }
}
