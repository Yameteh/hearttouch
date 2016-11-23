package com.binary.heart.hearttouch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.activity.CaiQuanActivity;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.ui.fragment.SmartFragment;

/**
 * Created by yaoguoju on 16-4-18.
 */
public class VsFragment extends SmartFragment {

    private TextView tvCaiquan;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vs,null);
        v.findViewById(R.id.tv_caiquan).setOnClickListener(this);
        tvCaiquan = (TextView) v.findViewById(R.id.tv_cq_rate);
        return v;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_caiquan:
                toActivity(context, CaiQuanActivity.class,null);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String cqResult = (String) SmartPref.get(context, PrefKeys.CAIQUAN_RESULT,"");
        tvCaiquan.setText(cqResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


}
