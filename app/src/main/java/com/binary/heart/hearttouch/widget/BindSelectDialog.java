package com.binary.heart.hearttouch.widget;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.BindReq;
import com.binary.heart.hearttouch.jsons.ProfileRsp;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.ui.toast.SmartToast;
import com.binary.smartlib.utils.ScreenUtil;

/**
 * Created by yaoguoju on 16-4-17.
 */
public class BindSelectDialog extends DialogFragment implements View.OnClickListener{
    public static final String ME = "me";
    public static final String TO = "to";
    private TextView me;
    private TextView to;
    private ImageButton bind;
    private ImageButton unbind;
    private String binder;
    private OnSelectListener listener;

    public boolean showbind = true;
    public boolean showunbind = true;
    public void setOnSelectListener(OnSelectListener l) {
        listener = l;
    }
    public interface OnSelectListener{
        public void onSelected(boolean bind);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_bindselect, null);
        me  = (TextView) v.findViewById(R.id.tv_bind_me);
        to  = (TextView) v.findViewById(R.id.tv_bind_to);
        bind = (ImageButton) v.findViewById(R.id.ibtn_bind);
        unbind = (ImageButton) v.findViewById(R.id.ibtn_unbind);
        if(showbind) {
            bind.setVisibility(View.VISIBLE);
        }else {
            bind.setVisibility(View.GONE);
        }
        if(showunbind) {
            unbind.setVisibility(View.VISIBLE);
        }else {
            unbind.setVisibility(View.GONE);
        }
        bind.setOnClickListener(this);
        unbind.setOnClickListener(this);
        Bundle bundle = getArguments();
        if(bundle != null) {
            me.setText(bundle.getString(ME));
            to.setText(bundle.getString(TO));
            binder = bundle.getString(TO);
        }
        return v;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ibtn_bind:
                if(listener != null) {
                    listener.onSelected(true);
                }
                break;
            case R.id.ibtn_unbind:
                if(listener != null) {
                    listener.onSelected(false);
                }
                break;
        }
        dismiss();
    }


}
