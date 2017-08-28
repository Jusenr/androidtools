package com.jusenr.tools;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.jusenr.tools.api.BaseApi;
import com.jusenr.toolslibrary.utils.AppUtils;
import com.jusenr.toolslibrary.utils.Logger;
import com.jusenr.toolslibrary.utils.PreferenceUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Description:
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/08/25
 * Time       : 10:18
 * Project    ：Tools.
 */
public class TotalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        BaseApi.init();

        //偏好文件初始化
        PreferenceUtils.init(this);
        //日志输出
        Logger.init(getApplicationContext(), getLogTag())
                .hideThreadInfo()
//                .setMethodCount(3)
                .setLogLevel(BuildConfig.IS_TEST ? Logger.LogLevel.FULL : Logger.LogLevel.NONE)
                .setSaveLog(true);

        //UMeng
        ApplicationInfo info = AppUtils.getApplicationInfo(getApplicationContext());
        String umeng_appkey = info.metaData.getString("UMENG_APPKEY");
        String umeng_channel = info.metaData.getString("UMENG_CHANNEL");
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(), umeng_appkey, umeng_channel);
        MobclickAgent.startWithConfigure(config);
        MobclickAgent.setDebugMode(BuildConfig.IS_TEST);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.openActivityDurationTrack(false);
        Logger.i(umeng_appkey);
        Logger.i(umeng_channel);

        Logger.i(getCurPackageName());
    }

    private String getLogTag() {
        return BuildConfig.LOG_TAG;
    }

    private String getCurPackageName() {
        return getApplicationContext().getPackageName();
    }
}
