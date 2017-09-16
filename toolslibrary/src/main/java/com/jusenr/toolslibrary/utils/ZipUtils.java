package com.jusenr.toolslibrary.utils;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by leo on 2016/12/13.
 */

public final class ZipUtils {
    /**
     * gzip compress string
     *
     * @param str string
     * @return gzipString
     * @throws IOException exception
     */
    public static String gzipCompress2Base64(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.finish();
        gzip.flush();
        gzip.close();
        String gzipString = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
        out.flush();
        out.close();
        return gzipString;
    }

    /**
     * gzip uncompress string
     *
     * @param str gzipString
     * @return string
     * @throws IOException exception
     */
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // ToString () uses platform default encoding, and can explicitly specify such as toString ("GBK").
        return out.toString();
    }
}
