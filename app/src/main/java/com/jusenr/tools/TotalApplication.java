/*
 * Copyright 2017 androidtools Jusenr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jusenr.tools;

import android.app.Application;
import android.os.Process;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jusenr.tools.base.api.BaseApi;
import com.jusenr.tools.widgets.fresco.ImagePipelineFactory;
import com.jusenr.toolslibrary.AndroidTools;
import com.jusenr.toolslibrary.CrashHandler;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.utils.AppUtils;
import com.jusenr.toolslibrary.utils.DiskFileCacheHelper;
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

//            //LeakCanary initialization
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis
//                // You should not init your app in this process
//                return;
//            }
//            LeakCanary.install(this);

            //AndroidTools initialise
            AndroidTools.init(getApplicationContext(), getLogTag());

            //DiskFileCacheHelper initialise
            mCacheHelper = DiskFileCacheHelper.get(getApplicationContext(), getLogTag());

            //UMeng initialise
            MobclickAgent.setDebugMode(BuildConfig.IS_TEST);
            MobclickAgent.setCatchUncaughtExceptions(true);
            MobclickAgent.openActivityDurationTrack(false);

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
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}