package com.binary.heart.hearttouch.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.SearchRsp;
import com.binary.heart.hearttouch.widget.LoadingView;
import com.binary.heart.hearttouch.widget.RandomTextView;
import com.binary.heart.hearttouch.widget.RippleView;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.ui.widget.SmartCornerButton;

import java.util.List;

/**
 * Created by yaoguoju on 16-4-11.
 */
public class RadarSearchFragment extends DialogFragment{
    private RandomTextView tvRandom;
    private LoadingView  binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.fragment_radarsearch,null);
        tvRandom = (RandomTextView) v.findViewById(R.id.tv_random);
        tvRandom.setOnRippleViewClickListener(new RandomTextView.OnRippleViewClickListener() {
            @Override
            public void onRippleViewClicked(View view) {
                RippleView v = (RippleView) view;
                String account =  v.getText().toString();
                binding.setMsg("正在绑定用户" + account);
                SmartLog.d(Configure.TAG, "click account " + account);
                binding.show();
                for(SearchRsp rsp : searchResult) {
                    if(rsp.getAccount().equals(account)){
                        SmartPref.put(getActivity(),PrefKeys.BINDACCOUNT,account);
                        SmartPref.put(getActivity(),PrefKeys.BINDUSERID,rsp.getUserid());
                    }
                }
                dismiss();
            }
        });
        binding = (LoadingView) v.findViewById(R.id.v_binding);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search();
    }

    private List<SearchRsp> searchResult;
    private void search() {
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(getActivity())).build();
        ApacheHttp.post(WebUrls.SEARCH, "".getBytes(), headers, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                String rsp = new String(response);
                searchResult = JSON.parseArray(rsp,SearchRsp.class);
                for(SearchRsp r : searchResult) {
                    SmartLog.d(Configure.TAG, "r==" + r.getAccount());
                    tvRandom.addKeyWord(r.getAccount());
                }
                tvRandom.show();
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onStart() {

            }
        });
    }

}
