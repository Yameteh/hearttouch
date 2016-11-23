package com.binary.heart.hearttouch.widget;

import android.content.Context;

import com.binary.heart.hearttouch.R;

/**
 * Created by yaoguoju on 16-3-31.
 */
public class ProgressDialog extends android.app.ProgressDialog{

    public ProgressDialog(Context context) {
        super(context, R.style.hPrgDialog_style);
        setProgressStyle(STYLE_SPINNER);
        setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.anim_progress));
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }


}
