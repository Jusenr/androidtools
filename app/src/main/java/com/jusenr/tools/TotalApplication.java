package com.jusenr.tools;

import android.app.Application;

import com.jusenr.tools.api.BaseApi;
import com.jusenr.toolslibrary.utils.Logger;
import com.jusenr.toolslibrary.utils.PreferenceUtils;

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
    }

    private String getLogTag() {
        return BuildConfig.LOG_TAG;
    }
}
