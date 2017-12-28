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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Android disk file caching utility.
 * Created by guchenkai on 2015/3/6.
 */
public final class DiskFileCacheHelper {
    public static final int TIME_HOUR = 60 * 60;
    public static final int TIME_DAY = TIME_HOUR * 24;
    private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
    private static final int MAX_COUNT = Integer.MAX_VALUE; // The amount of data that is not restricted.
    private static Map<String, DiskFileCacheHelper> mInstanceMap = new HashMap<String, DiskFileCacheHelper>();
    private AndroidCache mCache;

    private DiskFileCacheHelper() {
        throw new AssertionError();
    }

    public static DiskFileCacheHelper get(Context context) {
        return get(context, "AndroidCache");
    }

    public static DiskFileCacheHelper get(Context context, String cacheName) {
        File f = new File(context.getCacheDir(), cacheName);
        return get(f, MAX_SIZE, MAX_COUNT);
    }

    public static DiskFileCacheHelper get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    public static DiskFileCacheHelper get(Context ctx, long max_zise, int max_count) {
        File f = new File(ctx.getCacheDir(), "AndroidCache");
        return get(f, max_zise, max_count);
    }

    public static DiskFileCacheHelper get(File cacheDir, long max_zise, int max_count) {
        DiskFileCacheHelper manager = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (manager == null) {
            manager = new DiskFileCacheHelper(cacheDir, max_zise, max_count);
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), manager);
        }
        return manager;
    }

    private static String myPid() {
        return "_" + android.os.Process.myPid();
    }

    private DiskFileCacheHelper(File cacheDir, long max_size, int max_count) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
        }
        mCache = new AndroidCache(cacheDir, max_size, max_count);
    }

    /**
     * Save the String data into the cache
     *
     * @param key   Saved key
     * @param value String data storage
     */
    public void put(String key, String value) {
        File file = mCache.newFile(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }

    /**
     * Save the String data into the cache
     *
     * @param key      Saved key
     * @param value    String data storage
     * @param saveTime To save time, in seconds
     */
    public void put(String key, String value, int saveTime) {
        put(key, Utils.newStringWithDateInfo(saveTime, value));
    }

    /**
     * Read String data
     *
     * @param key Read key
     * @return String data storage
     */
    public String getAsString(String key) {
        File file = mCache.get(key);
        if (!file.exists())
            return null;
        boolean removeFile = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            if (!Utils.isDue(readString)) {
                return Utils.clearDateInfo(readString);
            } else {
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
        return null;
    }

    /**
     * Save the JSONObject data into the cache
     *
     * @param key   Saved key
     * @param value Saved JSON data
     */
    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    /**
     * Save the JSONObject data into the cache
     *
     * @param key      Saved key
     * @param value    Saved JSONObject data
     * @param saveTime To save time, in seconds
     */
    public void put(String key, JSONObject value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * Read JSONObject data
     *
     * @param key Read key
     * @return JSONObject data
     */
    public JSONObject getAsJSONObject(String key) {
        String JSONString = getAsString(key);
        try {
            JSONObject obj1 = new JSONObject(JSONString);
            return obj1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Save the JSONArray data into the cache
     *
     * @param key   Saved key
     * @param value Saved JSONArray data
     */
    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    /**
     * Save the JSONArray data into the cache
     *
     * @param key      Saved key
     * @param value    Saved JSONArray data
     * @param saveTime To save time, in seconds
     */
    public void put(String key, JSONArray value, int saveTime) {
        put(key, value.toString(), saveTime);
    }

    /**
     * Read JSONArray data
     *
     * @param key Read key
     * @return JSONArray data
     */
    public JSONArray getAsJSONArray(String key) {
        String JSONString = getAsString(key);
        try {
            JSONArray obj1 = new JSONArray(JSONString);
            return obj1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Save the byte data into the cache
     *
     * @param key   Saved key
     * @param value Saved byte data
     */
    public void put(String key, byte[] value) {
        File file = mCache.newFile(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCache.put(file);
        }
    }

    /**
     * Save the byte data into the cache
     *
     * @param key      Saved key
     * @param value    Saved byte data
     * @param saveTime To save time, in seconds
     */
    public void put(String key, byte[] value, int saveTime) {
        put(key, Utils.newByteArrayWithDateInfo(saveTime, value));
    }

    /**
     * Read byte data
     *
     * @param key Read key
     * @return Byte data
     */
    public byte[] getAsBinary(String key) {
        RandomAccessFile RAFile = null;
        boolean removeFile = false;
        try {
            File file = mCache.get(key);
            if (!file.exists())
                return null;
            RAFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) RAFile.length()];
            RAFile.read(byteArray);
            if (!Utils.isDue(byteArray)) {
                return Utils.clearDateInfo(byteArray);
            } else {
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (RAFile != null) {
                try {
                    RAFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile)
                remove(key);
        }
        return null;
    }

    /**
     * Save serializable to cache
     *
     * @param key   Saved key
     * @param value Saved value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * Save serializable to cache
     *
     * @param key      Saved key
     * @param value    Saved value
     * @param saveTime To save time, in seconds
     */
    public void put(String key, Serializable value, int saveTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (saveTime != -1) {
                put(key, data, saveTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Read serializable data
     *
     * @param key Read key
     * @return Serializable data
     */
    public Object getAsSerializable(String key) {
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                Object reObject1 = ois.readObject();
                return reObject1;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bais != null) {
                    try {
                        bais.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Save bitmap to cache
     *
     * @param key   Saved key
     * @param value Saved bitmap data
     */
    public void put(String key, Bitmap value) {
        put(key, Utils.Bitmap2Bytes(value));
    }

    /**
     * Save bitmap to cache
     *
     * @param key      Saved key
     * @param value    Saved bitmap data
     * @param saveTime To save time, in seconds
     */
    public void put(String key, Bitmap value, int saveTime) {
        put(key, Utils.Bitmap2Bytes(value), saveTime);
    }

    /**
     * Read bitmap data
     *
     * @param key Read key
     * @return bitmap date
     */
    public Bitmap getAsBitmap(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return Utils.Bytes2Bimap(getAsBinary(key));
    }

    /**
     * Save drawable to cache
     *
     * @param key   Saved key
     * @param value Saved drawable data
     */
    public void put(String key, Drawable value) {
        put(key, Utils.drawable2Bitmap(value));
    }

    /**
     * Save drawable to cache
     *
     * @param key      Saved key
     * @param value    Saved drawable data
     * @param saveTime To save time, in seconds
     */
    public void put(String key, Drawable value, int saveTime) {
        put(key, Utils.drawable2Bitmap(value), saveTime);
    }

    /**
     * Read Drawable data
     *
     * @param key Read key
     * @return Drawable data
     */
    public Drawable getAsDrawable(String key) {
        if (getAsBinary(key) == null) {
            return null;
        }
        return Utils.bitmap2Drawable(Utils.Bytes2Bimap(getAsBinary(key)));
    }

    /**
     * Get cached files
     *
     * @param key get key
     * @return value Cached files
     */
    public File file(String key) {
        File f = mCache.newFile(key);
        if (f.exists())
            return f;
        return null;
    }

    /**
     * Remove a key
     *
     * @param key remove key
     * @return Remove successfully [boolean]
     */
    public boolean remove(String key) {
        return mCache.remove(key);
    }

    /**
     * Clear all data
     */
    public void clear() {
        mCache.clear();
    }

    /**
     * cacheManager
     */
    private class AndroidCache {
        private final AtomicLong cacheSize;
        private final AtomicInteger cacheCount;
        private final long sizeLimit;
        private final int countLimit;
        private final Map<File, Long> lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
        private File cacheDir;

        private AndroidCache(File cacheDir, long sizeLimit, int countLimit) {
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            cacheSize = new AtomicLong();
            cacheCount = new AtomicInteger();
            calculateCacheSizeAndCacheCount();
        }

        /**
         * Calculate cacheSize and cacheCount
         */
        private void calculateCacheSizeAndCacheCount() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int size = 0;
                    int count = 0;
                    File[] cachedFiles = cacheDir.listFiles();
                    if (cachedFiles != null) {
                        for (File cachedFile : cachedFiles) {
                            size += calculateSize(cachedFile);
                            count += 1;
                            lastUsageDates.put(cachedFile, cachedFile.lastModified());
                        }
                        cacheSize.set(size);
                        cacheCount.set(count);
                    }
                }
            }).start();
        }

        private void put(File file) {
            int curCacheCount = cacheCount.get();
            while (curCacheCount + 1 > countLimit) {
                long freedSize = removeNext();
                cacheSize.addAndGet(-freedSize);
                curCacheCount = cacheCount.addAndGet(-1);
            }
            cacheCount.addAndGet(1);
            long valueSize = calculateSize(file);
            long curCacheSize = cacheSize.get();
            while (curCacheSize + valueSize > sizeLimit) {
                long freedSize = removeNext();
                curCacheSize = cacheSize.addAndGet(-freedSize);
            }
            cacheSize.addAndGet(valueSize);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);
        }

        private File get(String key) {
            File file = newFile(key);
            Long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            lastUsageDates.put(file, currentTime);
            return file;
        }

        private File newFile(String key) {
            return new File(cacheDir, key.hashCode() + "");
        }

        private boolean remove(String key) {
            File image = get(key);
            return image.delete();
        }

        private void clear() {
            lastUsageDates.clear();
            cacheSize.set(0);
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
        }

        /**
         * Remove old files
         *
         * @return Removed size of old file
         */
        private long removeNext() {
            if (lastUsageDates.isEmpty()) {
                return 0;
            }
            Long oldestUsage = null;
            File mostLongUsedFile = null;
            Set<Entry<File, Long>> entries = lastUsageDates.entrySet();
            synchronized (lastUsageDates) {
                for (Entry<File, Long> entry : entries) {
                    if (mostLongUsedFile == null) {
                        mostLongUsedFile = entry.getKey();
                        oldestUsage = entry.getValue();
                    } else {
                        Long lastValueUsage = entry.getValue();
                        if (lastValueUsage < oldestUsage) {
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = entry.getKey();
                        }
                    }
                }
            }
            long fileSize = calculateSize(mostLongUsedFile);
            if (mostLongUsedFile != null && mostLongUsedFile.delete()) {
                lastUsageDates.remove(mostLongUsedFile);
            }
            return fileSize;
        }

        private long calculateSize(File file) {
            return file.length();
        }
    }

    /**
     * Time calculation tools class
     */
    private static class Utils {

        /**
         * Determines whether the cached String data is expired
         *
         * @param str string
         * @return True: due false: not yet due
         */
        private static boolean isDue(String str) {
            return isDue(str.getBytes());
        }

        /**
         * Determines whether the cached byte data is expired
         *
         * @param data data
         * @return True: due false: not yet due
         */
        private static boolean isDue(byte[] data) {
            String[] strs = getDateInfoFromDate(data);
            if (strs != null && strs.length == 2) {
                String saveTimeStr = strs[0];
                while (saveTimeStr.startsWith("0")) {
                    saveTimeStr = saveTimeStr.substring(1, saveTimeStr.length());
                }
                long saveTime = Long.valueOf(saveTimeStr);
                long deleteAfter = Long.valueOf(strs[1]);
                if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                    return true;
                }
            }
            return false;
        }

        private static String newStringWithDateInfo(int second, String strInfo) {
            return createDateInfo(second) + strInfo;
        }

        private static byte[] newByteArrayWithDateInfo(int second, byte[] data2) {
            byte[] data1 = createDateInfo(second).getBytes();
            byte[] retdata = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retdata, 0, data1.length);
            System.arraycopy(data2, 0, retdata, data1.length, data2.length);
            return retdata;
        }

        private static String clearDateInfo(String strInfo) {
            if (strInfo != null && hasDateInfo(strInfo.getBytes())) {
                strInfo = strInfo.substring(strInfo.indexOf(mSeparator) + 1, strInfo.length());
            }
            return strInfo;
        }

        private static byte[] clearDateInfo(byte[] data) {
            if (hasDateInfo(data)) {
                return copyOfRange(data, indexOf(data, mSeparator) + 1, data.length);
            }
            return data;
        }

        private static boolean hasDateInfo(byte[] data) {
            return data != null && data.length > 15 && data[13] == '-' && indexOf(data, mSeparator) > 14;
        }

        private static String[] getDateInfoFromDate(byte[] data) {
            if (hasDateInfo(data)) {
                String saveDate = new String(copyOfRange(data, 0, 13));
                String deleteAfter = new String(copyOfRange(data, 14, indexOf(data, mSeparator)));
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        private static int indexOf(byte[] data, char c) {
            for (int i = 0, length = data.length; i < length; i++) {
                if (data[i] == c) {
                    return i;
                }
            }
            return -1;
        }

        private static byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (newLength < 0)
                throw new IllegalArgumentException(from + " > " + to);
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private static final char mSeparator = ' ';

        private static String createDateInfo(int second) {
            String currentTime = Long.toString(System.currentTimeMillis());
            while (currentTime.length() < 13) {
                currentTime = "0" + currentTime;
            }
            return currentTime + "-" + second + mSeparator;
        }

        /**
         * Bitmap → byte[]
         */
        private static byte[] Bitmap2Bytes(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        /**
         * byte[] → Bitmap
         */
        private static Bitmap Bytes2Bimap(byte[] b) {
            if (b.length == 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }

        /**
         * Drawable → Bitmap
         */
        private static Bitmap drawable2Bitmap(Drawable drawable) {
            if (drawable == null) {
                return null;
            }
            // Take the length and width of drawable
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // Take the color format for drawable
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            // Establishing corresponding bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // Create a canvas corresponding to bitmap
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // The content of the drawable to the canvas
            drawable.draw(canvas);
            return bitmap;
        }

        /**
         * Bitmap → Drawable
         */
        @SuppressWarnings("deprecation")
        private static Drawable bitmap2Drawable(Bitmap bm) {
            if (bm == null) {
                return null;
            }
            return new BitmapDrawable(bm);
        }
    }
}