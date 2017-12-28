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