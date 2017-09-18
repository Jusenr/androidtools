package com.jusenr.toolslibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Preferences settings file tools class
 * Created by guchenkai on 2015/11/2.
 */
public final class PreferenceUtils {

    private static final String SP_NAME = "APP_SHAREDPREFERENCES";

    private static SharedPreferences sp;

    private PreferenceUtils() {
        throw new AssertionError();
    }

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
     * Saved data
     *
     * @param key   key
     * @param value value
     * @param <T>   type
     */
    public static <T> void save(String key, T value) {
        put(key, value);
    }

    /**
     * Saved data set
     *
     * @param key    key
     * @param values data set
     * @param <T>    type
     */
    public static <T> void save(String key, List<T> values) {
        put(key, values);
    }

    /**
     * Get preference setting data
     *
     * @param key      key
     * @param defValue default values
     * @param <T>      type
     * @return Preference setting data
     */
    public static <T> T getValue(String key, T defValue) {
        return get(key, defValue);
    }

    /**
     * Setting data based on key delete preferences
     *
     * @param keys key array
     */
    public static void remove(String... keys) {
        SharedPreferences.Editor editor = sp.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * Clear all preferences settings file
     */
    public static void clear() {
        sp.edit().clear().apply();
    }

    /**
     * According to the key comparison, does the key correspond to the data?
     *
     * @param key key
     * @return Is there [boolean]
     */
    public static boolean contains(String key) {
        return sp.contains(key);
    }
}
