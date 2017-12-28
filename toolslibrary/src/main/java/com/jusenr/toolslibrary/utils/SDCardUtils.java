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
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SdCard operation tools class
 * Created by guchenkai on 2015/11/3.
 */
public final class SDCardUtils {

    private SDCardUtils() {
        if (!isSDCardEnable())
            System.err.println("SDCard is unavailable");
        /* cannot be instantiated */
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * Determine if SDCard is available
     *
     * @return IS available [boolean]
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Get SD card path
     *
     * @return SD card path
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * Get SD card path
     *
     * @param mContext Context
     * @return SD card path
     */
    public static String getSDCardPath(Context mContext) {
        return getSDCardPath(mContext, false);
    }

    /**
     * Get SD card path
     *
     * @param mContext    Context
     * @param is_removale When is_removable is false, you get the built-in SD card path, and true for the external SD card path.
     * @return SD card path
     */
    private static String getSDCardPath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets the remaining capacity unit of SD card byte
     *
     * @return Remaining capacity unit of SD card byte
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // Gets the number of free blocks of data
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // Gets the size of a single block of data (byte)
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * Gets the remaining number of bytes of available capacity in the space in the specified path, in units byte
     *
     * @param filePath file path
     * @return Capacity bytes, SDCard, free space, internal storage, free space
     */
    public static long getFreeBytes(String filePath) {
        // If it is the next path of the SD card, the available capacity of the SD card is obtained.
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// If the path is stored inside, the available capacity of obtaining memory.
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * Get system storage path
     *
     * @return System storage path
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}