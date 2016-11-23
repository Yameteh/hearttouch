package com.binary.heart.hearttouch.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binary.heart.hearttouch.R;

/**
 * Created by yaoguoju on 16-3-31.
 */
public class LoadingView extends FrameLayout{

    private TextView msg;

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.ll_loading,this);
        msg = (TextView) findViewById(R.id.tv_loading);
        this.setVisibility(View.GONE);
        this.setBackgroundColor(Color.WHITE);
    }

    public LoadingView(Context context) {
        super(context,null);
    }

    public void setMsg(int msgid) {
        this.msg.setText(msgid);
    }

    public void setMsg(String msg) {
        this.msg.setText(msg);
    }

    public void show() {
        this.setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        this.setVisibility(View.GONE);
    }
}
