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

package com.jusenr.toolslibrary.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jusenr.toolslibrary.R;

import static android.widget.Toast.makeText;

/**
 * Description: Android Toast tools class
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/08/25
 * Time       : 14:58
 * Project    ：AndroidTools.
 */
public final class ToastUtils {

    private ToastUtils() {
        throw new AssertionError();
    }

    public static void show(Context context, @StringRes int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, @StringRes int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * Show toast view
     *
     * @param context  context
     * @param text     text
     * @param duration duration
     */
    public static void show(Context context, CharSequence text, int duration) {
        if (!TextUtils.isEmpty(text)){
            Toast.makeText(context, text, duration).show();
        }
    }

    public static void show(Context context, @StringRes int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, @StringRes int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }

    public static void showAlertToast(Context context, @StringRes int resId) {
        showAlertToast(context, context.getResources().getString(resId), 0);
    }

    public static void showAlertToast(Context context, String title) {
        showAlertToast(context, title, 0);
    }

    public static void showAlertToast(Context context, String title, @DrawableRes int drawableId) {
        showAlertToast(context, title, drawableId, 1);
    }

    /**
     * Show alert toast view
     *
     * @param context    context
     * @param title      title
     * @param drawableId drawable Id
     * @param duration   duration
     */
    public static void showAlertToast(Context context, String title, @DrawableRes int drawableId, int duration) {
        Toast toast = makeText(context.getApplicationContext(), title, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);//the setting position of
        View view = LayoutInflater.from(context).inflate(R.layout.layout_alert_toast, null);
        LinearLayout llContent = (LinearLayout) view.findViewById(R.id.ll_content);
        ImageView alertIcon = (ImageView) view.findViewById(R.id.iv_alert_icon);
        TextView alertTitle = (TextView) view.findViewById(R.id.tv_alert_title);
        if (drawableId != 0) {
            alertIcon.setVisibility(View.VISIBLE);
            alertIcon.setImageResource(drawableId);
        } else {
            alertIcon.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(title)) {
            alertTitle.setText(title);
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int width = (int) (screenWidth / 1.5f);
        CardView.LayoutParams lp = (CardView.LayoutParams) llContent.getLayoutParams();
        lp.width = width;
        llContent.setLayoutParams(lp);
        llContent.requestLayout();
        toast.setView(view);//Set appearance
        toast.show();
    }

    public static void showCenterToast(Context context, String title) {
        showCenterToast(context, title, 0);
    }

    public static void showCenterToast(Context context, String title, @DrawableRes int drawableId) {
        showCenterToast(context, title, drawableId, Toast.LENGTH_SHORT);
    }

    public static void showCenterToast(Context context, String title, @DrawableRes int drawableId, int duration) {
        Toast toast = makeText(context.getApplicationContext(), title, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_center_toast, null);
        LinearLayout rlContent = (LinearLayout) view.findViewById(R.id.rl_content);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        ImageView alertIcon = (ImageView) view.findViewById(R.id.iv_icon);
        if (drawableId != 0) {
            alertIcon.setVisibility(View.VISIBLE);
            alertIcon.setImageResource(drawableId);
        } else {
            alertIcon.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(title)) {
            content.setText(title);
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        int width = (int) (screenWidth / 2f);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rlContent.getLayoutParams();
        lp.width = width;
        rlContent.setLayoutParams(lp);
        rlContent.requestLayout();
        toast.setView(view);
        toast.show();
    }
}