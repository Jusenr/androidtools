package com.jusenr.tools.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Jusenr on 2017/08/25.
 */

public class LocaleUtils {

    /**
     * 获取手机系统当前语言环境
     *
     * @return language
     */
    public static String getLocaleLanguage() {
        String language = Locale.getDefault().getLanguage();
        if (TextUtils.isEmpty(language) || (!language.startsWith("en")
                && !language.startsWith("fr")
                && !language.startsWith("zh"))) {
            language = "en";
        }
        Log.d("ApiLocale", "locale: " + language);
        return language;
    }
}
