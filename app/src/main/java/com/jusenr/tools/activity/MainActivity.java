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

package com.jusenr.tools.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jusenr.tools.NativeLib;
import com.jusenr.tools.R;
import com.jusenr.tools.base.BaseActivity;
import com.jusenr.tools.utils.PermissionsUtils;
import com.jusenr.toolslibrary.log.logger.Logger;
import com.jusenr.toolslibrary.utils.AppUtils;
import com.jusenr.toolslibrary.utils.DateUtils;
import com.jusenr.toolslibrary.utils.PreferenceUtils;
import com.jusenr.toolslibrary.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.HashSet;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        NativeLib nativeLib = new NativeLib();
        tv.setText(nativeLib.stringFJNI());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showAlertToast(getApplicationContext(), "Hello See!");
                Logger.i("Hello See!");
            }
        });

        findViewById(R.id.tv_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.showViewer();
            }
        });
        findViewById(R.id.btn_debug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("Logger test debug");
                Logger.d("现在是第%d周了", DateUtils.getWeekInMills(System.currentTimeMillis() / 1000));
            }
        });
        findViewById(R.id.btn_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("Logger test info");
                Logger.i("8 * 9 = %d", 72);
                ToastUtils.show(getApplicationContext(), "1+1=%d", (Object) 2);
            }
        });
        findViewById(R.id.btn_warn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.w("Logger test warn");
                Logger.w("2 + 9 = %d", 11);
            }
        });
        findViewById(R.id.btn_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.e("Logger test error");
                Logger.e("15 - 9 = %d", 6);
            }
        });

        TextView tvText1 = (TextView) findViewById(R.id.tv_text1);
        TextView tvText2 = (TextView) findViewById(R.id.tv_text2);

        tvText1.setText(AppUtils.getVersionName(this));
        tvText2.setText(Integer.toString(AppUtils.getVersionCode(this)));
        final String s = null;
        tvText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = "s".equals(s);
            }
        });
        Logger.d("d");

        applyReadPhoneStatePermissions();

        final HashSet<Object> objectHashMap = new HashSet<>();
        objectHashMap.add("frist");
        objectHashMap.add(1);

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.save("is_frist", "123456");
                PreferenceUtils.save("is_frist1", 789);
                PreferenceUtils.save("is_frist2", 987654321012L);
                PreferenceUtils.save("is_frist3", 1.25f);
                PreferenceUtils.save("is_frist4", true);
                PreferenceUtils.save("is_frist5", objectHashMap);
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String is_frist = PreferenceUtils.getValue("is_frist", "hellpo");
                int is_frist1 = PreferenceUtils.getValue("is_frist1", 852);
                long is_frist2 = PreferenceUtils.getValue("is_frist2", 7418529630L);
                float is_frist3 = PreferenceUtils.getValue("is_frist3", 14.52f);
                boolean is_frist4 = PreferenceUtils.getValue("is_frist4", false);
                HashSet<Object> is_frist5 = PreferenceUtils.getValue("is_frist5", objectHashMap);
                Log.i(TAG, "onClick: is_frist=" + is_frist);
                Log.i(TAG, "onClick: is_frist1=" + is_frist1);
                Log.i(TAG, "onClick: is_frist2=" + is_frist2);
                Log.i(TAG, "onClick: is_frist3=" + is_frist3);
                Log.i(TAG, "onClick: is_frist4=" + is_frist4);
                Log.i(TAG, "onClick: is_frist5=" + is_frist5);
            }
        });
        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getLocalClassName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getLocalClassName());
        MobclickAgent.onPause(this);
    }

    private void applyReadPhoneStatePermissions() {
        PermissionsUtils.requestReadPhoneState(this, new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantPermissions) {
                if (AndPermission.hasPermission(mActivity, Manifest.permission.READ_PHONE_STATE)) {
                    String deviceInfo = AppUtils.getDeviceInfo(mActivity);
                    Logger.d(deviceInfo);

                    TextView tvText3 = (TextView) findViewById(R.id.tv_text3);
                    TextView tvText4 = (TextView) findViewById(R.id.tv_text4);

                    String imei = AppUtils.getIMEI(mActivity);
                    tvText3.setText(imei);
                    Logger.d(imei);

                    String imsi = AppUtils.getIMSI(mActivity);
                    tvText3.setText(imsi);
                    Logger.d(imsi);

                    tvText4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                if (!AndPermission.hasPermission(mActivity, Manifest.permission.READ_PHONE_STATE)) {
                    ToastUtils.show(mActivity, getString(R.string.permission_tips_get_phone_state));
                    PermissionsUtils.defineSettingDialog(mActivity, deniedPermissions);
                }
            }
        });
    }
}