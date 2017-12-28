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

package com.jusenr.tools.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.widget.Button;

import com.jusenr.tools.R;
import com.jusenr.tools.widgets.dialog.SelectDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

/**
 * Description: 6.0+ Permissions Request
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/04/21
 * Time       : 13:49.
 */
public class PermissionsUtils {
    public static final int REQUEST_CODE = 0x001;
    public static final int REQUEST_CODE_SETTING = 0x00100;//手机设置
    private static SelectDialog mSelectDialog;
    private static RationaleListener mRationaleListener;

    /**
     * 权限请求(原生api)
     *
     * @param activity
     * @param requestCode
     * @param permissions
     */
    public static void requestPermissions(Activity activity, int requestCode, String... permissions) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 申请SDCard权限
     */
    public static void requestSDCard(Context context, PermissionListener listener) {
        getRationaleListener(context, context.getString(R.string.permission_tips_title_sdcard));
        AndPermission.with(context)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .callback(mRationaleListener)
                .start();
    }

    /**
     * 申请SDCard权限
     *
     * @param activity
     */
    public static void requestSDCard(final Activity activity, PermissionListener listener) {
        getRationaleListener(activity, activity.getString(R.string.permission_tips_title_sdcard));
        AndPermission.with(activity)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(listener)
                .rationale(mRationaleListener)
                .start();
    }

    /**
     * 申请打开相机权限
     */
    public static void requestCamera(Context context, PermissionListener listener) {
        getRationaleListener(context, context.getString(R.string.permission_tips_title_camera));
        AndPermission.with(context)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .callback(listener)
                .callback(mRationaleListener)
                .start();
    }

    /**
     * 申请打开相机权限
     *
     * @param activity
     */
    public static void requestCamera(final Activity activity, PermissionListener listener) {
        getRationaleListener(activity, activity.getString(R.string.permission_tips_title_camera));
        AndPermission.with(activity)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .callback(listener)
                .rationale(mRationaleListener)
                .start();
    }

    /**
     * 申请获取手机信息权限
     */
    public static void requestReadPhoneState(Context context, PermissionListener listener) {
        getRationaleListener(context, context.getString(R.string.permission_tips_title_read_phone_state));
        AndPermission.with(context)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.READ_PHONE_STATE)
                .callback(listener)
                .callback(mRationaleListener)
                .start();
    }


    /**
     * 申请获取手机信息权限
     *
     * @param activity
     */
    public static void requestReadPhoneState(final Activity activity, PermissionListener listener) {
        getRationaleListener(activity, activity.getString(R.string.permission_tips_title_read_phone_state));
        AndPermission.with(activity)
                .requestCode(REQUEST_CODE)
                .permission(Manifest.permission.READ_PHONE_STATE)
                .callback(listener)
                .rationale(mRationaleListener)
                .start();
    }

    /**
     * Rationale支持
     *
     * @param context
     * @param title
     */
    private static void getRationaleListener(final Context context, final String title) {
        if (mRationaleListener == null) {
            mRationaleListener = new RationaleListener() {
                @Override
                public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
                    // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                    mSelectDialog = new SelectDialog.Builder(context)
                            .setCancelable(false)
                            .setCanceledOnTouchOutside(false)
                            .setMessage(context.getString(R.string.permission_tips_message))
                            .setTitle(title)
                            .setNegativeButton(context.getString(R.string.permission_button_item_refuse), new SelectDialog.OnNegativeClickListener() {
                                @Override
                                public void onClick(SelectDialog dialog, Button button) {
                                    dialog.dismiss();
                                    rationale.cancel();
                                }
                            })
                            .setPositiveButton(context.getString(R.string.permission_button_item_agree), new SelectDialog.OnPositiveClickListener() {
                                @Override
                                public void onClick(SelectDialog dialog, Button button) {
                                    dialog.dismiss();
                                    rationale.resume();
                                }
                            })
                            .build();

                    mSelectDialog.show();
                }
            };
        }
    }

    public static void defineSettingDialog(Activity activity, List<String> deniedPermissions) {
        if (AndPermission.hasAlwaysDeniedPermission(activity, deniedPermissions)) {
            final SettingService settingService = AndPermission.defineSettingDialog(activity, REQUEST_CODE_SETTING);
            mSelectDialog = new SelectDialog.Builder(activity)
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setMessage(activity.getString(R.string.permission_tips_message))
                    .setTitle(activity.getString(R.string.permission_tips_title_to_system_setting))
                    .setNegativeButton(activity.getString(R.string.permission_button_item_refuse), new SelectDialog.OnNegativeClickListener() {
                        @Override
                        public void onClick(SelectDialog dialog, Button button) {
                            dialog.dismiss();
                            settingService.cancel();
                        }
                    })
                    .setPositiveButton(activity.getString(R.string.permission_button_item_to_system_setting), new SelectDialog.OnPositiveClickListener() {
                        @Override
                        public void onClick(SelectDialog dialog, Button button) {
                            dialog.dismiss();
                            settingService.execute();
                        }
                    })
                    .build();

            mSelectDialog.show();
        }
    }
}