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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jusenr.toolslibrary.R;

import static android.widget.Toast.makeText;

/**
 * Description: Android Toast package
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

    public static void show(Context context, CharSequence text, int duration) {
        Toast.makeText(context, text, duration).show();
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
        showAlertToast(context, context.getResources().getString(resId), 0, 0);
    }

    public static void showAlertToast(Context context, String title) {
        showAlertToast(context, title, 0, 0);
    }

    public static void showAlertToast(Context context, String title, @DrawableRes int drawableId) {
        showAlertToast(context, title, drawableId, 1);
    }

    public static void showAlertToast(Context context, String title, @DrawableRes int drawableId, int duration) {
        Toast toast = makeText(context.getApplicationContext(), title, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);//the setting position of
        View view = LayoutInflater.from(context).inflate(R.layout.toast_alert, null);
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
//        llContent.requestLayout();
        toast.setView(view);//Set appearance
        toast.show();
    }

}
