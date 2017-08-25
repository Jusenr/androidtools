package com.jusenr.toolslibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * 偏好设置文件工具
 * Created by guchenkai on 2015/11/2.
 */
public final class PreferenceUtils {

    private static final String SP_NAME = "app_preference";

    private static SharedPreferences sp;

    public static void init(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static <T> void put(String key, T value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, String.valueOf(value));
        editor.apply();
    }

    private static <T> T get(String key) {
        return (T) sp.getString(key, null);
    }

    private static <T> T get(String key, T defaultVal) {
        T value = get(key);
        if (value == null) {
            value = defaultVal;
        }
        return value;
    }

    /**
     * 保存编号设置数据
     *
     * @param key   key
     * @param value 数据
     * @param <T>   类型
     */
    public static <T> void save(String key, T value) {
        put(key, value);
    }

    /**
     * 保存编号设置数据集合
     *
     * @param key    key
     * @param values 数据集合
     * @param <T>    类型
     */
    public static <T> void save(String key, List<T> values) {
        put(key, values);
    }

    /**
     * 获得偏好设置数据
     *
     * @param key      key
     * @param defValue 默认值
     * @param <T>      类型
     * @return 偏好设置数据
     */
    public static <T> T getValue(String key, T defValue) {
        return get(key, defValue);
    }

    /**
     * 根据key删除偏好设置数据
     *
     * @param keys 多个key
     */
    public static void remove(String... keys) {
        SharedPreferences.Editor editor = sp.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * 清空偏好设置文件
     */
    public static void clear() {
        sp.edit().clear().apply();
    }

    /**
     * 根据key比对是否存在key对应的数据
     *
     * @param key key
     * @return 是否存在
     */
    public static boolean contains(String key) {
        return sp.contains(key);
    }
}
