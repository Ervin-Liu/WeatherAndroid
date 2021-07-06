package com.example.weatherandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.weatherandroid.R;
import com.example.weatherandroid.coreprogress.ProgressHelper;
import com.example.weatherandroid.coreprogress.ProgressUIListener;
import com.example.weatherandroid.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class ImageLookActivity extends BaseActivity {
    private static final String TAG = "ImageLookActivity";

    private ImageView mShow, mBack;
    private View mStatusView;
    private ProgressBar mDownPb;
    private TextView mTvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_look);
        Utils.setAndroidNativeLightStatusBar(ImageLookActivity.this, true);
        autoSetBarSize();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        mShow = findViewById(R.id.iv_show_image);
        mBack = findViewById(R.id.iv_show_back);
        mDownPb = findViewById(R.id.pb_down);
        mDownPb.setVisibility(View.INVISIBLE);
        mTvShow = findViewById(R.id.tv_p_show);
        mTvShow.setVisibility(View.INVISIBLE);
        mTvShow.setText("");

        Glide.with(ImageLookActivity.this)
                .load(url)
                //Close the cache
                .skipMemoryCache(true)
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into(mShow);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
                mDownPb.setVisibility(View.VISIBLE);
                mTvShow.setVisibility(View.VISIBLE);
                downLoad(url);
            }
        });
    }

    private void downLoad(String url) {
        //client
        OkHttpClient okHttpClient = new OkHttpClient();
//request builder
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.get();
//call
        Call call = okHttpClient.newCall(builder.build());
//enqueue
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", "=============onFailure===============");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "=============onResponse===============");
                Log.e("TAG", "request headers:" + response.request().headers());
                Log.e("TAG", "response headers:" + response.headers());

                //your original response body
                ResponseBody body = response.body();
                //wrap the original response body with progress
                ResponseBody responseBody = ProgressHelper.withProgress(body, new ProgressUIListener() {

                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                    @Override
                    public void onUIProgressStart(long totalBytes) {
                        super.onUIProgressStart(totalBytes);
                        Log.e("TAG", "onUIProgressStart:" + totalBytes);
                    }

                    @Override
                    public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                        Log.e("TAG", "=============start===============");
                        Log.e("TAG", "numBytes:" + numBytes);
                        Log.e("TAG", "totalBytes:" + totalBytes);
                        Log.e("TAG", "percent:" + percent);
                        Log.e("TAG", "speed:" + speed);
                        Log.e("TAG", "============= end ===============");
                        int p = (int) (100 * percent);
                        mDownPb.setProgress(p);
                        if (p == 100) {
                            mTvShow.setText(p + "%(下载完成)");
                        } else {
                            mTvShow.setText(p + "%");
                        }

//                        downloadInfo.setText("numBytes:" + numBytes + " bytes" + "\ntotalBytes:" + totalBytes + " bytes" + "\npercent:" + percent * 100 + " %" + "\nspeed:" + speed * 1000 / 1024 / 1024 + " MB/秒");
                    }

                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
                    @Override
                    public void onUIProgressFinish() {
                        super.onUIProgressFinish();
                        Log.e("TAG", "onUIProgressFinish:");
                        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                        Toast.makeText(getApplicationContext(), "下载成功,存储路径:" + directory, Toast.LENGTH_SHORT).show();
                    }

                });
                //read the body to file
                BufferedSource source = responseBody.source();
                String fileName=url.substring(url.lastIndexOf("/"));
                String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                File outFile = new File(directory+fileName);
                outFile.delete();
                outFile.getParentFile().mkdirs();
                outFile.createNewFile();
                BufferedSink sink = Okio.buffer(Okio.sink(outFile));
                source.readAll(sink);
                sink.flush();
                source.close();
            }
        });
    }

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, ImageLookActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(ImageLookActivity.this);
        mStatusView = findViewById(R.id.view_show_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }
}