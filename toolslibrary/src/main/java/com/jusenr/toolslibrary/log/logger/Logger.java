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

package com.jusenr.toolslibrary.log.logger;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jusenr.toolslibrary.log.PTLogActivity;

import java.util.Date;
import java.util.List;

/**
 * But more pretty, simple and powerful
 */
public final class Logger {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private static Printer printer = new LoggerPrinter();

    private Logger() {
        //no instance
    }
    //--------------------------------------↓-↓-↓-↓-↓--------------------------------

    public static int mDisk_log_level = ERROR;
    private static Context mContext = null;
    public static PTSqliteHelper mSqliteHelper;

    public static void init(@NonNull Context context, @NonNull int diskLogLevel) {
        init(context, null, diskLogLevel);
    }

    public static void init(@NonNull Context context, @Nullable FormatStrategy formatStrategy, @NonNull int diskLogLevel) {
        mContext = context;
        mDisk_log_level = diskLogLevel;

        // init Logger
        if (formatStrategy != null) {
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        } else {
            Logger.addLogAdapter(new AndroidLogAdapter());
        }

        //init sqlite helper
        mSqliteHelper = PTSqliteHelper.getInstance(context);
    }

    public static void showViewer() {
        Intent intent = new Intent(mContext, PTLogActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public static List<PTLogBean> queryLog(@NonNull int priority, @NonNull Date begin, @NonNull Date end, @NonNull int limit) {
        List<PTLogBean> logList = mSqliteHelper.queryLog(priority, begin, end, limit);
        return logList;
    }

    public static int deleteLog(@NonNull int priority, @NonNull Date date) {
        int deleteCount = mSqliteHelper.deleteLog(priority, date);
        return deleteCount;
    }

    //--------------------------------------↑-↑-↑-↑-↑--------------------------------

    public static void printer(Printer printer) {
        Logger.printer = printer;
    }

    public static void addLogAdapter(LogAdapter adapter) {
        printer.addAdapter(adapter);
    }

    public static void clearLogAdapters() {
        printer.clearLogAdapters();
    }

    /**
     * Given tag will be used as tag only once for this method call regardless of the tag that's been
     * set during initialization. After this invocation, the general tag that's been set will
     * be used for the subsequent log calls
     */
    public static Printer t(String tag) {
        return printer.t(tag);
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    public static void log(int priority, String tag, String message, Throwable throwable) {
        printer.log(priority, tag, message, throwable);
    }

    public static void d(String message, Object... args) {
        printer.d(message, args);
    }

    public static void d(Object object) {
        printer.d(object);
    }

    public static void e(String message, Object... args) {
        printer.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        printer.e(throwable, message, args);
    }

    public static void i(String message, Object... args) {
        printer.i(message, args);
    }

    public static void v(String message, Object... args) {
        printer.v(message, args);
    }

    public static void w(String message, Object... args) {
        printer.w(message, args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void wtf(String message, Object... args) {
        printer.wtf(message, args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(String json) {
        printer.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(String xml) {
        printer.xml(xml);
    }

}