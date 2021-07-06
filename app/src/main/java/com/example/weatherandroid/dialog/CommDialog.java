package com.example.weatherandroid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.weatherandroid.R;

/**
 * Describe: comm Dialog
 * <p>
 * Created by Ervin Liu on 2021/04/10---15:47
 **/
public class CommDialog extends Dialog {

    private TextView mTitleTv, mContentTv, mOkTv, mCancelTv;
    private String mTitle, mContent, mOk, mCancel;
    private OnClickListener mOnClickListener;
    private boolean mIsSign;

    public CommDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comm);
        setCanceledOnTouchOutside(false);
        init();
        refreshView();
        initEvent();
    }

    private void init() {
        mTitleTv = findViewById(R.id.tv_dialog_title);
        mContentTv = findViewById(R.id.tv_dialog_content);
        mOkTv = findViewById(R.id.tv_dialog_ok);
        mCancelTv = findViewById(R.id.tv_dialog_cancel);
        //Set the Dialog to 90% of the width of the phone
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (d.getWidth() * 0.9);
        getWindow().setAttributes(p);
    }

    private void refreshView() {
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleTv.setText(mTitle);
            mTitleTv.setVisibility(View.VISIBLE);
        } else {
            mTitleTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mContent)) {
            mContentTv.setText(mContent);
        }
        if (!TextUtils.isEmpty(mOk)) {
            mOkTv.setText(mOk);
        } else {
            mOkTv.setText(getContext().getString(R.string.i_ok));
        }
        if (!TextUtils.isEmpty(mCancel)) {
            mCancelTv.setText(mCancel);
        } else {
            mCancelTv.setText(getContext().getString(R.string.i_cancel));
        }
        if (mIsSign) {
            mCancelTv.setClickable(false);
            mCancelTv.setVisibility(View.GONE);
        } else {
            mCancelTv.setClickable(true);
            mCancelTv.setVisibility(View.VISIBLE);
        }

    }

    private void initEvent() {
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.okClick();
            }
        });
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.cancelClick();
            }
        });
    }

    public void show() {
        super.show();
    }

    public void dismiss() {
        super.dismiss();
    }

    public void setTitle(String Title) {
        this.mTitle = Title;
    }

    public void setContent(String Content) {
        this.mContent = Content;
    }

    public void setOk(String Ok) {
        this.mOk = Ok;
    }

    public void setCancel(String Cancel) {
        this.mCancel = Cancel;
    }

    public void setOnClickListener(OnClickListener OnClickListener) {
        this.mOnClickListener = OnClickListener;
    }

    public boolean isIsSign() {
        return mIsSign;
    }

    public void setIsSign(boolean mIsSign) {
        this.mIsSign = mIsSign;
    }

    public interface OnClickListener {
        void okClick();

        void cancelClick();
    }
}
