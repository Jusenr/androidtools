package com.jusenr.toolslibrary.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: Two-dimensional code operation
 * Copyright  : Copyright (c) 2016
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2016/12/16 15:11.
 */

public final class QRCodeUtils {

    private QRCodeUtils() {
        throw new AssertionError();
    }

    /**
     * Create QR code
     *
     * @param content                  content
     * @param widthPix                 widthPix
     * @param heightPix                heightPix
     * @param openErrorCorrectionLevel Does fault tolerance open?
     * @param logoBitmap               The two-dimensional code Logo icon (Center for null)
     * @param filePath                 The file path used to store two-dimensional code images
     * @return Generate two-dimensional code and save the file is successful [boolean]
     */
    public static boolean createQRCode(String content, int widthPix, int heightPix, boolean openErrorCorrectionLevel, Bitmap logoBitmap, String filePath) {
        try {
            if (TextUtils.isEmpty(content) || TextUtils.equals("null", content) || "".equals(content)) {
                return false;
            }
            Map hints = openErrorCorrectionLevel(openErrorCorrectionLevel);
            // Image data conversion, the use of matrix conversion
            BitMatrix bitMatrix = new QRCodeWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"), BarcodeFormat.QR_CODE, widthPix, heightPix, hints);

            Bitmap bitmap = generateQRBitmap(bitMatrix);

            if (logoBitmap != null) {
                bitmap = addLogo(bitmap, logoBitmap);
            }
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
            //You must use the compress method to save the bitmap to the file and then read it.
            //The bitmap returned directly is without any compression, and the memory consumption is huge!
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Generate a two-dimensional code image
     *
     * @param content                  An incoming string, usually a URL
     * @param widthAndHeight           widthAndHeight
     * @param openErrorCorrectionLevel Does fault tolerance open?
     * @return A two-dimensional code image [Bitmap]
     */
    public static Bitmap createQRCode(String content, int widthAndHeight, boolean openErrorCorrectionLevel) {
        try {
            if (TextUtils.isEmpty(content) || TextUtils.equals("null", content) || "".equals(content)) {
                return null;
            }
            //Processing Chinese characters, if you do not need to change the source code, convert the string into ISO-8859-1 code.
            Map hints = openErrorCorrectionLevel(openErrorCorrectionLevel);
            BitMatrix matrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "ISO-8859-1"), BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
            matrix = updateBit(matrix, 8);
            Bitmap bitmap = generateQRBitmap(matrix);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get BitMatrix
     *
     * @param matrix BitMatrix
     * @param margin margin
     * @return BitMatrix
     */
    public static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle();   //Gets the properties of the two-dimensional code pattern
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); //Generates a new BitMatrix according to the custom borders
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) {   //Loop to draw the 2D code pattern into the new bitMatrix
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * Generate a two-dimensional code image
     *
     * @param content content
     * @return A two-dimensional code image
     */
    public static Bitmap createQRCode(String content) {
        try {
            if (TextUtils.isEmpty(content) || TextUtils.equals("null", content) || "".equals(content)) {
                return null;
            }
            BitMatrix matrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"), BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = generateQRBitmap(matrix);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generate a two-dimensional code image
     *
     * @param qrCodeUrl content
     * @param width     width
     * @param height    height
     * @return A two-dimensional code image
     */
    public static Bitmap createQRCode(String qrCodeUrl, int width, int height) {
        Bitmap bitmap = createQRCode(qrCodeUrl, width, height, false);
        return bitmap;
    }

    /**
     * Generate a two-dimensional code image
     *
     * @param content content
     * @param width   width
     * @param height  height
     * @return A two-dimensional code image
     */
    public static Bitmap createQRCode(String content, int width, int height, boolean openFaultTolerantRate) {
        try {
            if (TextUtils.isEmpty(content) || TextUtils.equals("null", content) || "".equals(content)) {
                return null;
            }
            Map hints = openErrorCorrectionLevel(openFaultTolerantRate);
            BitMatrix matrix = new MultiFormatWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"), BarcodeFormat.QR_CODE, width, height, hints);
            Bitmap bitmap = generateQRBitmap(matrix);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Draw two-dimensional code
     *
     * @param matrix BitMatrix
     * @return [Bitmap]
     */
    public static Bitmap generateQRBitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    /**
     * Configuration parameter
     *
     * @param tag Does fault tolerance open?
     * @return map
     */
    public static Map openErrorCorrectionLevel(boolean tag) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        if (tag) {
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //Set code
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  //Fault tolerance level
//            hints.put(EncodeHintType.MARGIN, 2);  //Sets the width of the blank margin default is 4
        }
        return hints;
    }

    /**
     * Add a Logo pattern to the middle of the two-dimensional code
     *
     * @param src  QR code
     * @param logo logo
     * @return QR code [Bitmap]
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //Get the width and height of the picture
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //Logo size is the size of the two-dimensional code, the size of the 1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
//        logo = ThumbnailUtils.extractThumbnail(logo, srcWidth * 1 / 5, srcHeight * 1 / 5, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();

            canvas.drawBitmap(src, 0, 0, paint);
            canvas.drawCircle(0, 0, 5f, paint);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();

//            if(bitmap.isRecycled())
//                bitmap.recycle();
        } catch (Exception e) {
            bitmap = null;
            e.printStackTrace();
        }
        return bitmap;
    }
}
