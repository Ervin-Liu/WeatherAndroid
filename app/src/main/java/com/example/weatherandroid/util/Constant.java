package com.example.weatherandroid.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Describe: static constant
 * <p>
 * Created by Ervin Liu on 2021/3/24
 **/
public class Constant {

    public static final String IS_FIRST = "isFirst";
    public static final String IS_FIRST_GPS = "isFirstGps";
    public static final String USER_IPHONE = "userIphone";
    public static final String USER_PASS = "userPass";
    public static final String USER_LOGIN = "userLogin";

    public static final String WEATHER = "天气";
    public static final String NEWS = "新闻";
    public static final String VIDEO = "视频";
    public static final String MY_ACCOUNT = "我的";
    public static final String GET_WEATHER_FAIL = "获取%s天气信息失败";
    public static final String GET_CITY_INFO_FAIL = "获取城市信息失败";
    public static final String GET_DAY_ONE_FAIL = "获取每日一句失败";
    public static final String TODAY = "今天";
    public static final String WEEK = "周";
    public static final String WEEK_SOME_ONE = "星期";
    public static final String AIR_QUR = "空气";
    public static final String TEMP_UNIT = "℃";
    public static final String HOR_Line = "-";
    public static final String SLASH = "/";
    public static final String TEMP_UNIT_OTHER = "°";
    public static final String JSON_DATA = "data";
    public static final String JSON_TEMP = "wendu";
    public static final String JSON_FORECAST = "forecast";
    public static final String JSON_TYPE = "type";
    public static final String LIST_TEMP = "° C";
    public static final String LOADING = "正在加载...";
    public static final String THE_CITY = "城区";
    public static final String HAVE_CITY = "已添加过该城市，请勿重复添加";
    public static final String LOCATION_NO = "检查到未授予位置信息，可能导致某些功能不可用";
    public static final String LOCATION_DELETE_NO = "定位城市不能够删除";
    public static final String IPHONE_NO_REGISTER = "该手机号未被注册";
    public static final String PASS_FAIL = "密码错误";
    public static final String LOGIN_FAIL = "登录失败";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String LOGIN_IPHONE_ERROR = "手机号格式错误";
    public static final String LOGIN_IPHONE_OR_PASS_ERROR = "请正确输入手机号或密码";

    public static final String port = "172.20.10.3";
    public static String sWeatherUrl = "http://" + port + ":8085/SSM_war_exploded/";
}
