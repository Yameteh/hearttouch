package com.binary.smartlib.io;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.binary.smartlib.log.SmartLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by yaoguoju on 15-12-25.
 */
public class SmartGraphic {

    public final static String TAG = "SmartGraphic";
    /**
     * 根据drawable id获取bitmap
     * @param context
     * @param drawableId
     * @return
     */
    public static Bitmap getBitmapByDrawableId(Context context,int drawableId) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res,drawableId);
    }

    /**
     * bitmap转换为byte数组
     * @param bm
     * @param format
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bm,Bitmap.CompressFormat format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format,100,baos);
        return baos.toByteArray();
    }

    /**
     * 字符数组获取bitmap
     * @param bytes
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes) {
        if(bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }else {
            return null;
        }
    }

    /**
     * 图片缩放
     * @param bm
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bm, int width, int height) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float)width / w);
        float scaleHeight = ((float)height / h);
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap newBm = Bitmap.createBitmap(bm,0,0,w,h,matrix,true);
        return newBm;
    }

    /**
     * 从drawable获取bitmap
     * @param drawable
     * @return
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        return ((BitmapDrawable)drawable).getBitmap();
    }

    /**
     * 从bitmap获取drawable
     * @param bm
     * @return
     */
    public static Drawable getDrawableFromBitmap(Bitmap bm) {
        return  new BitmapDrawable(bm);
    }
    /**
     * 获取圆角图片
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获取带阴影的图片
     * @param bitmap
     * @return
     */
    public static Bitmap getReflectionBitmap(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w,
                h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
                0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight()
                + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * 缩放drawable
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap oldbmp = getBitmapFromDrawable(drawable);
        Matrix matrix = new Matrix();
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        matrix.postScale(sx, sy);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(newbmp);
    }

    /**
     * 用bitmap生成图片
     * @param bm
     * @param file
     * @return
     */
    public static boolean newFileByBitmap(Bitmap bm, File file) {
        boolean ret = false;
        try {
            SmartFile.create(file);
            Bitmap.CompressFormat format ;
            String fileName = file.getName();
            if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                format = Bitmap.CompressFormat.JPEG;
            }else if(fileName.endsWith(".png")) {
                format = Bitmap.CompressFormat.PNG;
            }else {
                SmartLog.d(TAG,"file format error");
                return ret;
            }
            OutputStream outputStream = new FileOutputStream(file);
            ret = bm.compress(format,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取随机颜色
     * @return
     */
    public static int randomColor() {
        Random mRandom = new Random();
        return Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255));
    }
}
