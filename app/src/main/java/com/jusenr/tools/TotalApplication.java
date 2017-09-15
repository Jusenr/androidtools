package com.jusenr.tools;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDex;

import com.jusenr.tools.api.BaseApi;
import com.jusenr.toolslibrary.AndroidTools;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.utils.AppUtils;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

/**
 * Description: Application
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/08/25
 * Time       : 10:18
 * Project    ：AndroidTools.
 */
public class TotalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //MultiDex initialization
        MultiDex.install(getApplicationContext());

        //API initialise.
        BaseApi.init();

        //LeakCanary initialization
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        //AndroidTools initialise.
        AndroidTools.init(getApplicationContext(), getLogTag());

        //UMeng initialise.
        ApplicationInfo info = AppUtils.getApplicationInfo(getApplicationContext());
        String umeng_appkey = info.metaData.getString("UMENG_APPKEY");
        String umeng_channel = info.metaData.getString("UMENG_CHANNEL");
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(), umeng_appkey, umeng_channel);
        MobclickAgent.startWithConfigure(config);
        MobclickAgent.setDebugMode(BuildConfig.IS_TEST);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.openActivityDurationTrack(false);

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
