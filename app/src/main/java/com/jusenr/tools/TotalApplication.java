package com.jusenr.tools;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jusenr.tools.api.BaseApi;
import com.jusenr.tools.widgets.fresco.ImagePipelineFactory;
import com.jusenr.toolslibrary.AndroidTools;
import com.jusenr.toolslibrary.CrashHandler;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.utils.AppUtils;
import com.jusenr.toolslibrary.utils.DiskFileCacheHelper;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

import okhttp3.OkHttpClient;

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
    private static final String TAG = TotalApplication.class.getSimpleName();

    private static DiskFileCacheHelper mCacheHelper;//磁盘文件缓存器

    @Override
    public void onCreate() {
        super.onCreate();
        //MultiDex initialization
        MultiDex.install(getApplicationContext());
        //多进程只对主进程初始化
        String processName = AppUtils.getProcessName(getApplicationContext(), Process.myPid());
        if (getPackageName().equals(processName)) {
            //API initialise
            BaseApi.init();

            //LeakCanary initialization
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis
                // You should not init your app in this process
                return;
            }
            LeakCanary.install(this);

            //AndroidTools initialise
            AndroidTools.init(getApplicationContext(), getLogTag());

            //DiskFileCacheHelper initialise
            mCacheHelper = DiskFileCacheHelper.get(getApplicationContext(), getLogTag());

            //UMeng initialise
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

            Fresco.initialize(getApplicationContext(), ImagePipelineFactory.imagePipelineConfig(getApplicationContext()
                    , new OkHttpClient()
                    , getCacheDir().getAbsolutePath()));
        }

        CrashHandler.instance().init(new CrashHandler.OnCrashHandler() {
            @Override
            public void onCrashHandler(String phoneInfo, Throwable e) {
                onCrash(phoneInfo, e);
            }
        });
    }

    private String getLogTag() {
        return BuildConfig.LOG_TAG;
    }

    private String getCurPackageName() {
        return getApplicationContext().getPackageName();
    }

    public static DiskFileCacheHelper getDiskFileCacheHelper() {
        return mCacheHelper;
    }

    private void onCrash(String phoneInfo, Throwable e) {
        Logger.e(phoneInfo + "\n" + e.getMessage());
        e.printStackTrace();
    }
}
