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
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(focusView,
                    InputMethodManager.RESULT_SHOWN);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Close soft keyboard
     *
     * @param context   context
     * @param focusView input field
     */
    public static void closeKeyboard(Context context, View focusView) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
    }
}
