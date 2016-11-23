package com.binary.smartlib.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * Created by yaoguoju on 15-12-24.
 */
public class SmartCornerButton extends SmartButton {

    public SmartCornerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SmartCornerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartCornerButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onPressDraw(canvas);
    }

    @Override
    protected void onPressDraw(Canvas canvas) {
        if (isPressed && isClickable() && isEnabled()) {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    10, 10, mPaint);
        }
    }
}
