package com.binary.smartlib.utils;

/**
 * Created by yaoguoju on 16-4-10.
 */
public class MathUtil {

    /**
     * 将double转换为String
     * @param value
     * @return
     */
    public static String d2s(double value) {
        return String.valueOf(value);
    }

    /**
     * string装double
     * @param value
     * @return
     */
    public static double s2d(String value) {
        return  Double.parseDouble(value);
    }
}
