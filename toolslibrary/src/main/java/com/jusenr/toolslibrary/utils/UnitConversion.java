package com.jusenr.toolslibrary.utils;

import java.math.BigDecimal;

/**
 * Description: 单位换算
 * Copyright  : Copyright (c) 2016
 * Email      : jusenr@163.com
 * Company    : 葡萄科技
 * Author     : Jusenr
 * Date       : 2017/3/24 18:24.
 */
//身高换算成英尺(IN)，体重IBS
//      IN(CM)    IBS(KG)
//      换算公式：
//      1IN=2.54CM
//      1IBS=0.454KG

public class UnitConversion {
    public static final String TAG = UnitConversion.class.getSimpleName();

    public static final double IN_CM_RATE = 2.54;//换算率
    public static final double IBS_KG_RATE = 0.454;//换算率

    public static double getIN2CM(double xIN, boolean isRound) {
        Double CM_result = mul(xIN, Double.valueOf(IN_CM_RATE));
        return isRound ? getRound(CM_result) : CM_result;
    }

    public static double getCM2IN(double xCM, boolean isRound) {
        Double IN_result = div(xCM, Double.valueOf(IN_CM_RATE), 3);
        return isRound ? getRound(IN_result) : IN_result;
    }

    public static double getIBS2KG(double xIBS, boolean isRound) {
        Double KG_result = mul(xIBS, Double.valueOf(IBS_KG_RATE));
        return isRound ? getRound(KG_result) : KG_result;
    }

    public static double getKG2IBS(double xKG, boolean isRound) {
        Double IBS_result = div(xKG, Double.valueOf(IBS_KG_RATE), 3);
        return isRound ? getRound(IBS_result) : IBS_result;
    }

    private static int getRound(double x) {
        int round = (int) round(x, 0);

        return round;
    }

    /**
     * Double数四舍五入
     *
     * @param v     double型数据
     * @param scale 精确度
     * @return Double
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 两个Double数相加
     *
     * @param v1 double型数据
     * @param v2 double型数据
     * @return Double
     */
    public static Double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * 两个Double数相减
     *
     * @param v1 double型数据
     * @param v2 double型数据
     * @return Double
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 两个Double数相乘
     *
     * @param v1 double型数据
     * @param v2 double型数据
     * @return Double
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 两个Double数相除，并保留scale位小数
     *
     * @param v1    double型数据
     * @param v2    double型数据
     * @param scale 精确度
     * @return Double
     */
    public static Double div(Double v1, Double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
