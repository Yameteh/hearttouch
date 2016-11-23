package com.binary.smartlib.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by yaoguoju on 15-12-24.
 */
public class DensityUtil {

    /**
     * dp转px
     * @param context
     * @param dpValue
     * @return
     */
    public static float dp2px(Context context, float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                ,dpValue,context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue/scale);
    }

    /**
     * sp转px
     * @param context
     * @param spValue
     * @return
     */
    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP
                ,spValue,context.getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2sp(Context context, float pxValue) {
        return (pxValue/context.getResources().getDisplayMetrics().scaledDensity);
    }

}
