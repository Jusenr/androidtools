package com.jusenr.toolslibrary.utils;

import java.math.BigDecimal;

/**
 * Description: unit conversion class
 * Copyright  : Copyright (c) 2016
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/3/24 18:24.
 */
//Height is measured in inches (IN), weight IBS
//      IN(CM)    IBS(KG)
//      Conversion formula:
//      1IN=2.54CM
//      1IBS=0.454KG

public class UnitConversion {
    public static final String TAG = UnitConversion.class.getSimpleName();

    public static final double IN_CM_RATE = 2.54;//rate of conversion
    public static final double IBS_KG_RATE = 0.454;//rate of conversion

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
     * The number of five homes in Double
     *
     * @param d     Type double data
     * @param scale definition
     * @return Double value
     */
    public static double round(double d, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(d));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Add two Double numbers together
     *
     * @param d1 Type double data
     * @param d2 Type double data
     * @return Double value
     */
    public static Double add(Double d1, Double d2) {
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.add(b2).doubleValue();
    }

    /**
     * Two Double number subtraction
     *
     * @param v1 Type double data
     * @param v2 Type double data
     * @return Double value
     */
    public static Double sub(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.subtract(b2).doubleValue();
    }


    /**
     * Multiply two Double numbers
     *
     * @param v1 Type double data
     * @param v2 Type double data
     * @return Double value
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
    }


    /**
     * Two Double number division, and keep the scale decimal
     *
     * @param d1    Type double data
     * @param d2    Type double data
     * @param scale definition
     * @return Double value
     */
    public static Double div(Double d1, Double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(d1.toString());
        BigDecimal b2 = new BigDecimal(d2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
