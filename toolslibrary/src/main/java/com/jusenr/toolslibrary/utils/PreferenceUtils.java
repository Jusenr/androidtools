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
import android.content.SharedPreferences;

import java.util.List;
import java.util.Set;

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
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Integer) {
            editor.putInt(key, Integer.valueOf(value.toString()));
        } else if (value instanceof Long) {
            editor.putLong(key, Long.valueOf(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key, Float.valueOf(value.toString()));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, Boolean.valueOf(value.toString()));
        }
        editor.apply();
    }

    private static <T> T get(String key, T defaultValue) {
        Object value = null;
        if (defaultValue instanceof String) {
            value = sp.getString(key, defaultValue.toString());
        } else if (defaultValue instanceof Integer) {
            value = sp.getInt(key, Integer.valueOf(defaultValue.toString()));
        } else if (defaultValue instanceof Long) {
            value = sp.getLong(key, Long.valueOf(defaultValue.toString()));
        } else if (defaultValue instanceof Float) {
            value = sp.getFloat(key, Float.valueOf(defaultValue.toString()));
        } else if (defaultValue instanceof Boolean) {
            value = sp.getBoolean(key, Boolean.valueOf(defaultValue.toString()));
        }

        return (T) value;
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
     * Get preference setting data
     *
     * @param key      key
     * @param defValue default values
     * @return Preference setting data
     */
    public static <T extends Set> T getValue(String key, Set defValue) {
        return (T) sp.getStringSet(key, defValue);
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