package com.jusenr.toolslibrary.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jusenr.toolslibrary.R;

import static android.widget.Toast.makeText;

/**
 * Description: Android Toast 封装
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/08/25
 * Time       : 14:58
 * Project    ：Tools.
 */
public final class ToastUtils {

    private static Toast mToast;

    public static void showToastLong(Context context, String msg) {
        if (!TextUtils.isEmpty(msg))
            showNoRepeatToast(context, msg, Toast.LENGTH_LONG);
        //      showToast(context, msg, Toast.LENGTH_LONG);
    }

    public static void showToastShort(Context context, String msg) {
//        showToast(context, msg, Toast.LENGTH_SHORT);
        if (!TextUtils.isEmpty(msg))
            showNoRepeatToast(context, msg);
    }

    public static void showToastShort(Context context, int strRes) {
        showToast(context, context.getString(strRes), Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, int strRes) {
        showToast(context, context.getString(strRes), Toast.LENGTH_LONG);
    }

    public static void showToast(Context context, String msg, int duration) {
        if (!TextUtils.isEmpty(msg))
            makeText(context, msg, duration).show();
    }

    public static void showNoRepeatToast(Context context, String msg) {
        if (!TextUtils.isEmpty(msg))
            showNoRepeatToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showNoRepeatToast(Context context, String msg, int duration) {
        if (!TextUtils.isEmpty(msg))
            if (mToast == null) {
                mToast = makeText(context.getApplicationContext(), msg, duration);
            }
        mToast.setText(msg);
        mToast.show();
    }

    public static void showAlertToast(Context context, String title) {
        showAlertToast(context, title, 0, 0);
    }

    public static void showAlertToast(Context context, String title, @DrawableRes int drawableId) {
        showAlertToast(context, title, drawableId, 1);
    }

    public static void showAlertToast(Context context, String title, @DrawableRes int drawableId, int duration) {
        Toast toast = makeText(context.getApplicationContext(), title, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);//设置位置
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
        int width = (int) (DensityUtils.getScreenW(context) / 1.5f);
        CardView.LayoutParams lp = (CardView.LayoutParams) llContent.getLayoutParams();
        lp.width = width;
        llContent.setLayoutParams(lp);
//        llContent.requestLayout();
        toast.setView(view);//设置外观
        toast.show();
    }

}
