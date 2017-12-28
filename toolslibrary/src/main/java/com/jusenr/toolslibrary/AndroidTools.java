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

package com.jusenr.toolslibrary;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jusenr.toolslibrary.log.logger.FormatStrategy;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.log.logger.PrettyFormatStrategy;
import com.jusenr.toolslibrary.utils.PreferenceUtils;

import java.util.Date;

/**
 * Description: AndroidTools initialization.
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/08/29
 * Time       : 19:04
 * Project    ：androidtools.
 */
public final class AndroidTools {

    private AndroidTools() {
        throw new AssertionError();
    }

    public static void init(@NonNull Context context, @NonNull String logtag) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(logtag)
                .build();
        init(context, formatStrategy, Logger.VERBOSE);
    }

    public static void init(@NonNull Context context, @NonNull FormatStrategy formatStrategy, int logLevel) {
        //Preference file initialization.
        PreferenceUtils.init(context);
        //AndroidTools initialise.
        //e.g.
//        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
//                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
//                .methodCount(2)         // (Optional) How many method line to show. Default 2
//                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
//                .logStrategy(new LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
//                .tag("logtag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
//                .build();
        Logger.init(context, formatStrategy, logLevel);
        //Log keeps one month time.
        Date date = new Date(System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000L);
        Logger.deleteLog(Logger.VERBOSE, date);
    }
}