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
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Soft keyboard tools class
 * Created by guchenkai on 2015/11/26.
 */
public final class KeyboardUtils {

    private KeyboardUtils() {
        throw new AssertionError();
    }

    /**
     * Open soft keyboard
     *
     * @param context   context
     * @param focusView input field
     */
    public static void openKeyboard(Context context, View focusView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(focusView, InputMethodManager.RESULT_SHOWN);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Close soft keyboard
     *
     * @param context   context
     * @param focusView input field
     */
    public static void closeKeyboard(Context context, View focusView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }
}