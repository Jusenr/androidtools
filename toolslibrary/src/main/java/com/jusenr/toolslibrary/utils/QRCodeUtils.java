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
 * Description: 二维码操作
 * Copyright  : Copyright (c) 2016
 * Email      : jusenr@163.com
 * Company    : 葡萄科技
 * Author     : Jusenr
 * Date       : 2016/12/16 15:11.
 */

public class QRCodeUtils {

    /**
     * 生成二维码
     *
     * @param content                  内容
     * @param widthPix                 图片宽度
     * @param heightPix                图片高度
     * @param openErrorCorrectionLevel 开启容错率
     * @param logoBitmap               二维码中心的Logo图标（可以为null）
     * @param filePath                 用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRCode(String content, int widthPix, int heightPix, boolean openErrorCorrectionLevel, Bitmap logoBitmap, String filePath) {
        try {
            if (TextUtils.isEmpty(content) || TextUtils.equals("null", content) || "".equals(content)) {
                return false;
            }
            Map hints = openErrorCorrectionLevel(openErrorCorrectionLevel);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(new String(content.getBytes("UTF-8"), "iso-8859-1"), BarcodeFormat.QR_CODE, widthPix, heightPix, hints);

            Bitmap bitmap = generateQRBitmap(bitMatrix);

            if (logoBitmap != null) {
                bitmap = addLogo(bitmap, logoBitmap);
            }
            boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
//            ImageUtils
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 生成一个二维码图像
     *
     * @param content                  传入的字符串，通常是一个URL
     * @param widthAndHeight           图像的宽高
     * @param openErrorCorrectionLevel 开启容错率
     */
    public static Bitmap createQRCode(String content, int widthAndHeight, boolean openErrorCorrectionLevel) {
        try {
            if (TextUtils.isEmpty(content) || TextUtils.equals("null", content) || "".equals(content)) {
                return null;
            }
            // 处理汉字，如果不用更改源码，将字符串转换成ISO-8859-1编码
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

    public static BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle();   //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) {   //循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * 生成一个二维码图像
     *
     * @param content
     * @return
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
     * 生成一个二维码图像
     *
     * @param qrCodeUrl
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createQRCode(String qrCodeUrl, int width, int height) {
        Bitmap bitmap = createQRCode(qrCodeUrl, width, height, false);
        return bitmap;
    }

    /**
     * 生成一个二维码图像
     *
     * @param content
     * @param width
     * @param height
     * @return
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
     * 绘制二维码
     *
     * @param matrix
     * @return
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
     * 配置参数
     *
     * @param tag
     * @return
     */
    public static Map openErrorCorrectionLevel(boolean tag) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        if (tag) {
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); //中文乱码，注释掉
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  //容错级别
//            hints.put(EncodeHintType.MARGIN, 2);  //设置空白边距的宽度 default is 4
        }
        return hints;
    }

    /**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
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
        //logo大小为二维码整体大小的1/5
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
