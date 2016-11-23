package com.binary.heart.hearttouch.widget;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.utils.ScreenUtil;

/**
 * Created by yaoguoju on 16-4-17.
 */
public class InputDialog extends DialogFragment implements View.OnClickListener{

    private TextView title;
    private EditText input;
    public final static String INPUT = "input";
    public final static String TITLE = "title";
    public final static String HINT  = "hint";
    public interface OnInputListener {
        public void onConfirm(String input);
        public void onCancel();
    }

    private OnInputListener listener;
    public void setOnInputListener(OnInputListener l) {
        listener = l;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_input,null);
        LinearLayout vc = (LinearLayout) v.findViewById(R.id.ll_container);
        int width = ScreenUtil.getScreenWidth(getActivity())*4/5;
        vc.setMinimumWidth(width);
        title = (TextView) v.findViewById(R.id.tv_dialog_title);
        input = (EditText)v.findViewById(R.id.tv_dialog_input);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String s = bundle.getString(INPUT);
            input.setText(s);
            String h = bundle.getString(HINT);
            input.setHint(h);
            String t = bundle.getString(TITLE);
            title.setText(t);
        }
        v.findViewById(R.id.btn_dialog_ok).setOnClickListener(this);
        v.findViewById(R.id.btn_dialog_cancel).setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_dialog_ok:
                if(listener != null) {
                    listener.onConfirm(input.getText().toString());
                }
                dismiss();
                break;
            case R.id.btn_dialog_cancel:
                if(listener != null) {
                    listener.onCancel();
                }
                dismiss();
                break;
        }
    }


}
