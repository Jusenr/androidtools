package com.jusenr.toolslibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Resources File tools class
 * Created by guchenkai on 2015/11/3.
 */
public final class ResourcesUtils {

    private ResourcesUtils() {
        throw new AssertionError();
    }

    /**
     * Reads the contents of the Assets text resource
     *
     * @param resName resName
     * @return resource [String]
     */
    public static String getAssetsTextFile(Context context, String resName) {
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            reader = new InputStreamReader(getAssets(context).open(resName));
            br = new BufferedReader(reader);
            String line;
            String Result = "";
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i == 0) Result = line;
                else Result = Result + "\n" + line;
                i++;
            }
            return Result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Get the BitmapDrawable of the picture resource
     *
     * @param resId resId
     * @return picture resource [BitmapDrawable]
     */
    public static BitmapDrawable getBitmapDrawable(Context context, int resId) {
        return (BitmapDrawable) context.getResources().getDrawable(resId);
    }

    /**
     * Gets a string resource based on string resource ID
     *
     * @param context context
     * @param resId   resId
     * @return string resource
     */
    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * Get a string array of resources according to the string array resource ID (Array)
     *
     * @param context context
     * @param resId   resId
     * @return string array of resources
     */
    public static String[] getStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }

    /**
     * Gets the string array resource based on the string array resource ID(List)
     *
     * @param context context
     * @param resId   resId
     * @return string array resource
     */
    public static List<String> getStringList(Context context, int resId) {
        List<String> strings = new ArrayList<String>();
        String[] strs = getStringArray(context, resId);
        for (String string : strs) {
            strings.add(string);
        }
        return strings;
    }

    /**
     * Obtain the resource picture from the picture resource ID
     *
     * @param context context
     * @param resId   resId
     * @return resource [Drawable]
     */
    public static Drawable getDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * Obtain the resource picture from the picture resource ID
     *
     * @param context context
     * @param resId   resId
     * @return resource [Bitmap]
     */
    public static Bitmap getBitmap(Context context, int resId) {
        Drawable drawable = getDrawable(context, resId);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * Get the Asset Explorer
     *
     * @param context context
     * @return AssetManager
     */
    public static AssetManager getAssets(Context context) {
        return context.getResources().getAssets();
    }
}
