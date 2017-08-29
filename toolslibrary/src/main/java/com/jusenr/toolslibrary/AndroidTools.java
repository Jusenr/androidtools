package com.jusenr.toolslibrary;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jusenr.toolslibrary.log.logger.FormatStrategy;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.log.logger.PrettyFormatStrategy;
import com.jusenr.toolslibrary.utils.PreferenceUtils;

/**
 * Description: Tools初始化
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/08/29
 * Time       : 19:04
 * Project    ：androidtools.
 */
public final class AndroidTools {

    public static void init(@NonNull Context context, @NonNull String logTag) {
        //偏好文件初始化
        PreferenceUtils.init(context);
        //Logger日志输出
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(2)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(new LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(logTag)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.init(context, formatStrategy, Logger.DEBUG);
    }
}
