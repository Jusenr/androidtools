package com.jusenr.tools;

import android.app.Application;
import android.content.pm.ApplicationInfo;

import com.jusenr.tools.api.BaseApi;
import com.jusenr.toolslibrary.log.logger.FormatStrategy;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.log.logger.PrettyFormatStrategy;
import com.jusenr.toolslibrary.utils.AppUtils;
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
        //API初始化
        BaseApi.init();
        //偏好文件初始化
        PreferenceUtils.init(this);
        //Logger日志输出
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(2)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(new LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(getLogTag())   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.init(getApplicationContext(), formatStrategy, Logger.DEBUG);

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
