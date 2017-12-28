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

import java.math.BigDecimal;

/**
 * Description: Four arithmetic operations (Double type)
 * Copyright  : Copyright (c) 2016
 * Email      : jusenr@163.com
 * Company    : putao Technology
 * Author     : Jusenr
 * Date       : 2016/7/26 19:36
 */
public final class ArithmeticDouble {

    private ArithmeticDouble() {
        throw new AssertionError();
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