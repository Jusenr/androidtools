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

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

/**
 * App information tool class
 * Created by guchenkai on 2015/11/25.
 */
public final class AppUtils {

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * Detects whether the permission is authorized
     *
     * @param context    context
     * @param permission permission
     * @return is authorized
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Get Device info, you need access to mobile phone information permissions.
     * [Manifest.permission.READ_PHONE_STATE]
     *
     * @param context context
     * @return Device info
     */
    public static String getDeviceInfo(Context context) {
        try {
            String device_id = null;
            String mac = null;
            FileReader fstream;
            JSONObject json = new JSONObject();
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                device_id = tm.getDeviceId();
            }
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(fstream, 1024);
                mac = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Access to mobile phone IMEI number, you need access to mobile phone information permissions.
     * [Manifest.permission.READ_PHONE_STATE]
     *
     * @param context context
     * @return mobile phone IMEI number
     */
    public static String getIMEI(Context context) {
        String imei = null;
        if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        }
        return imei;
    }

    /**
     * Access to mobile phone IMSI number, you need access to mobile phone information permissions.
     * [Manifest.permission.READ_PHONE_STATE]
     *
     * @param context context
     * @return mobile phone IMSI number
     */
    public static String getIMSI(Context context) {
        String imsi = null;
        if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = mTelephonyMgr.getSubscriberId();
        }
        return imsi;
    }

    /**
     * Get device Id
     *
     * @param context context
     * @return device Id
     */
    public static String getRealDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Get device name
     *
     * @return device name
     */
    public static String getDeviceName() {
        return android.os.Build.MODEL;
    }

    /**
     * Gets the name of the application
     *
     * @param context     context
     * @param packagename Application package name
     * @return App name
     */
    public String getAppName(Context context, String packagename) {
        //Package management operations management class
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packagename, 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return packagename;
    }

    /**
     * Gets the name of the current application
     *
     * @param context context
     * @return App name
     */
    public String getAppName(Context context) {
        //Package management operations management class
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return context.getPackageName();
    }

    /**
     * Get the application icon
     *
     * @param context context
     * @return The application icon
     */
    public Drawable getAppIcon(Context context) {
        try {
            //Package management operations management class
            PackageManager pm = context.getPackageManager();
            //Get the application information
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the application icon
     *
     * @param context     context
     * @param packagename Application package name
     * @return The application icon
     */
    public Drawable getAppIcon(Context context, String packagename) {
        try {
            //Package management operations management class
            PackageManager pm = context.getPackageManager();
            //Get the application information
            ApplicationInfo info = pm.getApplicationInfo(packagename, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the current application version number information
     *
     * @param context context
     * @return The version number for the current application
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get permissions to the application
     *
     * @param context     context
     * @param packagename Application package name
     * @return permission-group
     */
    public String[] getAllPermissions(Context context, String packagename) {
        try {
            //Package management operations management class
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packagename, PackageManager.GET_PERMISSIONS);
            //Get all permissions
            return packinfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the permissions for the current application
     *
     * @param context context
     * @return Permission array
     */
    public String[] getAllPermissions(Context context) {
        try {
            //Package management operations management class
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            //Get all permissions
            return packinfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the signature of the current application
     *
     * @param context     context
     * @param packagename Application package name
     * @return Signature of the current application
     */
    public static String getAppSignature(Context context, String packagename) {
        try {
            //Package management operations management class
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packagename, PackageManager.GET_SIGNATURES);
            //Gets the current application signature
            return packinfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packagename;
    }

    /**
     * Gets the signature of the current application
     *
     * @param context context
     * @return Signature of the current application
     */
    public static String getAppSignature(Context context) {
        try {
            //Package management operations management class
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            //Gets the current application signature
            return packinfo.signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return context.getPackageName();
    }

    /**
     * Get the ApplicationInfo object
     *
     * @param context context
     * @return applicationInfo
     */
    public static ApplicationInfo getApplicationInfo(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * check and see if an Activity exists on your device
     *
     * @param context context
     * @param intent  intent
     * @return boolean result
     */
    public static boolean isCallable(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        int size = list.size();
        return size > 0;
    }

    /**
     * Gets the current application version number
     *
     * @param context context
     * @return The version name of the current application
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param context context
     * @return The current application does not have a version name of D or V prefix
     */
    public static String getNoPrefixVersionName(Context context) {
        String versionName = getVersionName(context);
        if (TextUtils.isEmpty(versionName))
            return "";
        return versionName.substring(1);
    }

    /**
     * Read the meta-data information under the application tag
     *
     * @param context context
     * @return Bundle information for metaData under application tag [Bundle]
     */
    public static Bundle getMetaDaApplication(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Read the meta-data information under the application tag
     *
     * @param context context
     * @param name    key
     * @return value info
     */
    public static int getMetaDataInt(Context context, String name) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getInt(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Read the meta-data information under the application tag
     *
     * @param context context
     * @param name    key
     * @return value info
     */
    public static String getMetaDataString(Context context, String name) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Read the meta-data information under the activity tag
     *
     * @param activity activity
     * @return Bundle information for metaData under activity tag [Bundle]
     */
    public static Bundle getMetaDaActivity(Activity activity) {
        try {
            ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            return activityInfo.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Read the meta-data information under the activity tag
     *
     * @param activity activity
     * @param name     key
     * @return value info
     */
    public static int getMetaDataInt(Activity activity, String name) {
        try {
            ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            return activityInfo.metaData.getInt(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Read the meta-data information under the activity tag
     *
     * @param activity activity
     * @param name     key
     * @return value info
     */
    public static String getMetaDataString(Activity activity, String name) {
        try {
            ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            return activityInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gets the maximum memory that the application is running
     *
     * @return Max memory
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory() / 1024;
    }

    /**
     * Install apk
     *
     * @param context context
     * @param file    APK file
     */
    public static void installApl(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * Install apk
     *
     * @param context context
     * @param file    APK file uri
     */
    public static void installApk(Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * Uninstall apk
     *
     * @param context     context
     * @param packagename packagename
     */
    public static void uninstallApk(Context context, String packagename) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packagename);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * Is the test service running?
     *
     * @param context   context
     * @param className className
     * @return The state of being running
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName()))
                isRunning = true;
        }
        return isRunning;
    }

    /**
     * Stop running service
     *
     * @param context   context
     * @param className className
     * @return boolean result
     */
    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null)
            ret = context.stopService(intent_service);
        return ret;
    }

    /**
     * Get CPU core number
     *
     * @return CPU core number
     */
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * whether this process is named with processName
     *
     * @param context     context
     * @param processname processname
     * @return boolean result
     */
    public static boolean isNamedProcess(Context context, String processname) {
        if (context == null || TextUtils.isEmpty(processname)) return false;
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (processInfoList == null) return true;
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid && processname.equalsIgnoreCase(processInfo.processName))
                return true;
        }
        return false;
    }

    /**
     * Does the application run in the background (requires permissions android.permission.GET_TASKS)?
     *
     * @param context context
     * @return boolean result
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName()))
                return true;
        }
        return false;
    }

    /**
     * Gets the currently displayed Activity name
     *
     * @param context context
     * @return currently displayed Activity name
     */
    private static String getCurrentActivityName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    /**
     * Get application signature
     *
     * @param context     context
     * @param packagename package name
     * @return Returns the signature of the application
     */
    public static String getSign(Context context, String packagename) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(packagename, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a signature string into the required 32 bit signature
     *
     * @param paramArrayOfByte Signature byte array
     * @return 32 bit signature string
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) return new String(arrayOfChar);
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Clean up background processes and services
     *
     * @param context context
     * @param pid     current pid
     * @return The amount cleared
     */
    public static int gc(Context context, int pid) {
        long i = getDeviceUsableMemory(context);
        int count = 0; // Number of cleared processes
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // Get a list of the running service
        List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null) {
            try {
                for (ActivityManager.RunningServiceInfo service : serviceList) {
                    if (service.pid == pid) continue;
                    android.os.Process.killProcess(service.pid);
                    count++;
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        // Gets a list of running processes
        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if (processList != null) {
            try {
                for (ActivityManager.RunningAppProcessInfo process : processList) {
                    // Processes with values greater than RunningAppProcessInfo.IMPORTANCE_SERVICE have long been useless or empty processes
                    // Processes with values greater than RunningAppProcessInfo.IMPORTANCE_VISIBLE are non visible processes, that is, running in the background
                    if (process.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                        // pkgList: Gets the package name that is running under this process
                        String[] pkgList = process.pkgList;
                        for (String pkgName : pkgList) {
                            am.killBackgroundProcesses(pkgName);
                            count++;
                        }
                    }
                }
            } catch (Exception e) { // Prevent accidents
                e.getStackTrace();
            }
        }
        return count;
    }

    /**
     * Gets the available memory size of the device
     *
     * @param context context
     * @return Current memory size
     */
    public static int getDeviceUsableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // Returns the available memory for the current system
//        int size = (int) (mi.availMem / (1024 * 1024));
        int size1 = (int) (mi.availMem >> 20);
        return size1;
    }

    /**
     * Get all the applications in the system
     *
     * @param context context
     * @return Application information List
     */
    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<>();
        PackageManager pManager = context.getPackageManager();
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0, length = paklist.size(); i < length; i++) {
            PackageInfo pak = paklist.get(i);
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
                // customs applications
                apps.add(pak);
        }
        return apps;
    }

    /**
     * Get the SDK version of the mobile phone system
     *
     * @return Current SDK version
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * Whether Dalvik mode?
     *
     * @return boolean result
     */
    public static boolean isDalvik() {
        return "Dalvik".equals(getCurrentRuntimeValue());
    }

    /**
     * Whether ART mode?
     *
     * @return boolean result
     */
    public static boolean isART() {
        String currentRuntime = getCurrentRuntimeValue();
        return "ART".equals(currentRuntime) || "ART debug build".equals(currentRuntime);
    }

    /**
     * Get the current Runtime of your phone
     *
     * @return Under normal circumstances, you may value Dalvik, ART, ART, debug, build.
     */
    public static String getCurrentRuntimeValue() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get", String.class, String.class);
                if (get == null) return "WTF?!";
                try {
                    final String value = (String) get.invoke(systemProperties, "persist.sys.dalvik.vm.lib", "Dalvik");
                    if ("libdvm.so".equals(value))
                        return "Dalvik";
                    else if ("libart.so".equals(value))
                        return "ART";
                    else if ("libartd.so".equals(value))
                        return "ART debug build";
                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
        }
    }

    private final static X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    /**
     * Check if the current application is the Debug version
     *
     * @param context context
     * @return boolean result
     */
    public static boolean isDebuggable(Context context) {
        boolean debuggable = false;
        ByteArrayInputStream stream = null;
        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature signatures[] = pinfo.signatures;
            for (int i = 0, length = signatures.length; i < length; i++) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) break;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return debuggable;
    }

    /**
     * Gets the cache path, first Sdcard, and then internal storage
     *
     * @param context context
     * @return cache path
     */
    public static String getAppCachePath(Context context) {
        String cachePath = null;
        boolean equals = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (equals && context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    /**
     * Get the process name based on process ID
     *
     * @param context context
     * @param pid     current pid
     * @return process name
     */
    public static String getProcessName(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = am.getRunningAppProcesses();
        if (processInfoList == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }
}