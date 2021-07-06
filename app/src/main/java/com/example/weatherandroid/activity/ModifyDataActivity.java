package com.example.weatherandroid.activity;

import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.weatherandroid.R;
import com.example.weatherandroid.dialog.LoadingDialog;
import com.example.weatherandroid.util.Constant;
import com.example.weatherandroid.util.GlideEngine;
import com.example.weatherandroid.util.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Describe: modify data activity
 * <p>
 * Created by Ervin Liu on 2021/04/21---11:00
 **/
public class ModifyDataActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ModifyDataActivity";

    private View mStatusView;
    private ImageView mBack;
    private RelativeLayout mRelaUrl, mRelaName, mRelaGender, mRelaSignature;
    private CircleImageView mShowImgUrl;
    private TextView mShowName, mShowGender, mShowSignature;
    private PopupWindow pop;
    private Button mStartModify;
    private LoadingDialog mLoadingDialog;

    private String mIphone, mUrl, mName, mSignature, mModifyPath;
    private int mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_data);
        Utils.setAndroidNativeLightStatusBar(ModifyDataActivity.this, true);
        autoSetBarSize();
        init();
    }

    private void init() {
        Intent intent = getIntent();
        mIphone = intent.getStringExtra("iphone");
        mUrl = intent.getStringExtra("url");
        mName = intent.getStringExtra("name");
        mGender = intent.getIntExtra("gender", 0);
        mSignature = intent.getStringExtra("signature");

        mStartModify = findViewById(R.id.btn_start_modify);
        mBack = findViewById(R.id.iv_modify_data_back);

        mShowImgUrl = findViewById(R.id.civ_img_url);
        mShowName = findViewById(R.id.tv_name);
        mShowGender = findViewById(R.id.tv_gender);
        mShowSignature = findViewById(R.id.tv_signature);

        String url = Constant.sWeatherUrl + (mUrl.split("SSM_war_exploded/"))[1];
        Glide.with(ModifyDataActivity.this)
                .load(url)
                //Close the cache
                .skipMemoryCache(true)
                .signature(new ObjectKey(UUID.randomUUID().toString()))
                .into(mShowImgUrl);

        mShowName.setText(mName);
        if (mGender == 0) {
            mShowGender.setText("男");
        } else if (mGender == 1) {
            mShowGender.setText("女");
        }
        mShowSignature.setText(mSignature);

        mRelaUrl = findViewById(R.id.rela_url);
        mRelaName = findViewById(R.id.rela_name);
        mRelaGender = findViewById(R.id.rela_gender);
        mRelaSignature = findViewById(R.id.rela_signature);

        mRelaUrl.setOnClickListener(this);
        mRelaName.setOnClickListener(this);
        mRelaGender.setOnClickListener(this);
        mRelaSignature.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mStartModify.setOnClickListener(this);
    }

    /**
     * Set the view size according to the phone status bar size
     */
    private void autoSetBarSize() {
        float statusBarHeight = Utils.getStatusBarHeight(ModifyDataActivity.this);
        mStatusView = findViewById(R.id.view_modify_data_status_bar);
        LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) statusBarHeight);
        mStatusView.setLayoutParams(Params1);
    }

    public static void startActivity(Context context, String iphone, String url, String name, int gender, String signature) {
        Intent intent = new Intent(context, ModifyDataActivity.class);
        intent.putExtra("iphone", iphone);
        intent.putExtra("url", url);
        intent.putExtra("name", name);
        intent.putExtra("gender", gender);
        intent.putExtra("signature", signature);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_modify_data_back:
                finish();
                break;
            case R.id.rela_url:
                chooseImage();
                break;
            case R.id.rela_name:
                changeName("name");
                break;
            case R.id.rela_gender:
                showPop();
                break;
            case R.id.rela_signature:
                changeName("signature");
                break;
            case R.id.btn_start_modify:
                uploadFile();
            default:
                break;
        }
    }

    private void chooseImage() {
        PictureSelector.create(ModifyDataActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private void changeName(String type) {
        final EditText et = new EditText(this);
        if ("name".equals(type)) {
            et.setText(mShowName.getText().toString());
        } else if ("signature".equals(type)) {
            et.setText(mShowSignature.getText().toString());
        }
        new AlertDialog.Builder(this).setTitle("昵称")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ("name".equals(type)) {
                            mShowName.setText(et.getText().toString());
                        } else if ("signature".equals(type)) {
                            mShowSignature.setText(et.getText().toString());
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    images = PictureSelector.obtainMultipleResult(data);
                    mModifyPath = images.get(0).getRealPath();
                    Glide.with(ModifyDataActivity.this)
                            .load(mModifyPath)
                            //Close the cache
                            .skipMemoryCache(true)
                            .signature(new ObjectKey(UUID.randomUUID().toString()))
                            .into(mShowImgUrl);
                    break;
                default:
                    break;
            }
        }
    }

    public void uploadFile() {
        mLoadingDialog = new LoadingDialog(this, "修改中...", R.drawable.ic_loading);
        mLoadingDialog.show();
        String url = "http://"+ Constant.port+":8085/SSM_war_exploded/uploadUserInfo";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        try {
            param.put("file", new File(mModifyPath));
            param.put("iphone", mIphone);
            param.put("name", mShowName.getText().toString());
            String gender = mShowGender.getText().toString();
            if (gender.equals("男")) {
                param.put("gender", 0);
            } else if (gender.equals("女")) {
                param.put("gender", 1);
            }
            param.put("signature", mShowSignature.getText().toString());

            httpClient.post(url, param, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(ModifyDataActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    mLoadingDialog.dismiss();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(ModifyDataActivity.this, "修改失败,请重试", Toast.LENGTH_SHORT).show();
                    mLoadingDialog.dismiss();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ModifyDataActivity.this, "上传文件不存在", Toast.LENGTH_SHORT).show();
        }
    }

    public void showPop() {
        View bottomView = View.inflate(ModifyDataActivity.this, R.layout.gender_modify, null);
        TextView man = bottomView.findViewById(R.id.tv_modify_man);
        TextView woman = bottomView.findViewById(R.id.tv_modify_woman);
        TextView cancel = bottomView.findViewById(R.id.tv_modify_gender_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_modify_man:
                        mShowGender.setText("男");
                        break;
                    case R.id.tv_modify_woman:
                        mShowGender.setText("女");
                        break;
                    case R.id.tv_modify_gender_cancel:
                        closePopupWindow();
                        break;
                    default:
                        break;
                }
                closePopupWindow();
            }
        };

        man.setOnClickListener(clickListener);
        woman.setOnClickListener(clickListener);
        cancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }
}