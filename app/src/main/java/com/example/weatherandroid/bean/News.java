package com.example.weatherandroid.bean;

/**
 * Describe: news bean
 * <p>
 * Created by Ervin Liu on 2021/04/20---14:53
 **/
public class News {
    private String mTitle;
    private String mTime;
    private String mPic;
    private String mWebUrl;

    public News() {
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String mTime) {
        this.mTime = mTime;
    }

    public String getPic() {
        return mPic;
    }

    public void setPic(String mPic) {
        this.mPic = mPic;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public void setWebUrl(String mWebUrl) {
        this.mWebUrl = mWebUrl;
    }

    public News(String mTitle, String mTime, String mPic, String mWebUrl) {
        this.mTitle = mTitle;
        this.mTime = mTime;
        this.mPic = mPic;
        this.mWebUrl = mWebUrl;
    }
}
