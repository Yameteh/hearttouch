package com.binary.smartlib.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yaoguoju on 15-12-24.
 */
public class SmartIndicator extends View {

    private Bitmap active;
    private Bitmap unActive;

    private int total;
    private int current;
    private int bitmapWidth;
    private int bitmapHeight;
    private int space;

    private Paint paint = new Paint();

    public SmartIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w;
        int h;
        if(active == null || unActive == null) {
            bitmapWidth = bitmapHeight = 0;
            w = h = 0;
        }else {
            w = bitmapWidth * total + space * (total - 1);
            h = bitmapHeight;
            if(w <= 0) {
                w = 1;
            }
            if(h <= 1) {
                h = 1;
            }
        }
        int widthSize = resolveSizeAndState(w,widthMeasureSpec,0);
        int heightSize = resolveSizeAndState(h,heightMeasureSpec,0);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(active == null || unActive == null) {
            return ;
        }

        for(int i = 0; i < total; i++) {
            if(i == current) {
                canvas.drawBitmap(active,i*bitmapWidth + space*i,0,paint);
            }else {
                canvas.drawBitmap(unActive,i*bitmapWidth+space*i,0,paint);
            }
        }
    }

    /**
     * 设置图标
     * @param active
     * @param unActive
     */
    public void setDisplayImage(Drawable active,Drawable unActive) {
        this.active = ((BitmapDrawable)active).getBitmap();
        this.unActive = ((BitmapDrawable)unActive).getBitmap();
        bitmapWidth = this.active.getWidth();
        bitmapHeight = this.active.getHeight();
        requestLayout();
        invalidate();
    }

    /**
     * 设置节点总数
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 设置当前节点
     * @param cur
     */
    public void setCurrent(int cur) {
        this.current = cur;
        invalidate();
    }

}
