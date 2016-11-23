package com.binary.heart.hearttouch.widget;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.utils.ScreenUtil;

/**
 * Created by yaoguoju on 16-4-17.
 */
public class SexSelectDialog extends DialogFragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    public static final String SEX = "sex";
    public static int MALE = 1;
    public static int FEMALE = 2;
    public interface OnSelectListener{
        public void onSexSelect(int id);
    }
    private boolean isInitChecked1 = false;
    private boolean isInitChecked2 = false;
    private OnSelectListener listener;

    public void setOnSelectListener(OnSelectListener l) {
        listener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_sexselect, null);
        int width = ScreenUtil.getScreenWidth(getActivity())*4/5;
        radioGroup = (RadioGroup) v.findViewById(R.id.rg_sex);
        radioGroup.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        radioGroup.setOnCheckedChangeListener(this);
        Bundle bundle = getArguments();
        if(bundle != null) {
            int check = bundle.getInt(SEX);
            isInitChecked1 = true;
            isInitChecked2 = true;
            radioGroup.check(check);
        }
        return v;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int checkid = group.getCheckedRadioButtonId();
        SmartLog.d(Configure.TAG,"checkd id "+checkedId);
        if(isInitChecked1) {
            isInitChecked1 = false;
            return;
        }
        if(isInitChecked2) {
            isInitChecked2 = false;
            return;
        }
        if(listener != null) {
            if(checkedId == R.id.rbtn_male) {
                listener.onSexSelect(MALE);
            }else if(checkedId == R.id.rbtn_female) {
                listener.onSexSelect(FEMALE);
            }
        }
        dismiss();
    }

    public void setCheck(int id) {
        if(id == FEMALE) {
            radioGroup.check(R.id.rbtn_female);
        }else if(id == MALE) {
            radioGroup.check(R.id.rbtn_male);
        }
    }
}
