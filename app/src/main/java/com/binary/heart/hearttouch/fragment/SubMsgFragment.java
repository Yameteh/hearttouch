package com.binary.heart.hearttouch.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.MainActivity;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.adapter.NotifyAdapter;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.im.notify.HeartNotify;
import com.binary.heart.hearttouch.jsons.BindReq;
import com.binary.heart.hearttouch.jsons.BindRsp;
import com.binary.heart.hearttouch.jsons.UnbindReq;
import com.binary.heart.hearttouch.widget.BindSelectDialog;
import com.binary.smartlib.io.SmartDao;
import com.binary.smartlib.io.SmartDb;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.ui.fragment.SmartFragment;
import com.yuntongxun.kitsdk.fragment.ConversationListFragment;

import java.util.List;

/**
 * Created by yaoguoju on 16-5-6.
 */
public class SubMsgFragment extends SmartFragment{

    private TextView titleIM;
    private TextView titleNotify;
    private ListView notifyList;

    private NotifyAdapter notifyAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_submsg,null);
        titleIM = (TextView) v.findViewById(R.id.tv_msg_im);
        titleNotify = (TextView) v.findViewById(R.id.tv_msg_notify);
        notifyList  = (ListView) v.findViewById(R.id.lstv_notify);
        notifyAdapter = new NotifyAdapter(context);
        notifyList.setAdapter(notifyAdapter);
        notifyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HeartNotify notify = (HeartNotify) notifyAdapter.getItem(position);
                showBindSelect(notify.getSender());
            }
        });
        titleNotify.setVisibility(View.GONE);
        return v;
    }

    private void showBindSelect(final String to) {
        BindSelectDialog dialog = new BindSelectDialog();
        Bundle bundle = new Bundle();
        bundle.putString(BindSelectDialog.ME,AccountHelper.getAccount(context));
        bundle.putString(BindSelectDialog.TO,to);
        dialog.setArguments(bundle);
        dialog.setOnSelectListener(new BindSelectDialog.OnSelectListener() {
            @Override
            public void onSelected(boolean bind) {
                if (bind) {
                    actionBind(to);
                } else {
                    actionUnBind(to);
                }
            }
        });
        dialog.show(getFragmentManager(), "bindselect");
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        setupImMsgView();
        refreshNotify();

    }

    private void refreshNotify() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Object> notifys = SmartDb.getDb(context).getSmartDao(HeartNotify.class).queryAll(HeartNotify.class);
                    notifyAdapter.setList(notifys);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(notifyAdapter.getCount()>0) {
                                titleNotify.setVisibility(View.VISIBLE);
                            }else {
                                titleNotify.setVisibility(View.GONE);
                            }
                            notifyAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void setupImMsgView() {
        if(AccountHelper.getBindUserId(context) < 1 ) {
            titleIM.setVisibility(View.GONE);
            return ;
        }
        ConversationListFragment fragment = new ConversationListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConversationListFragment.EMPTY_TARGET,AccountHelper.getBindAccount(context));
        bundle.putString(ConversationListFragment.EMPTY_NICK,AccountHelper.getBindNick(context));
        fragment.setArguments(bundle);
        asignFragment(R.id.fl_msg_im, fragment);
    }

    private void actionUnBind(final String to) {
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(getActivity())).build();
        UnbindReq req = new UnbindReq();
        req.setUserid(AccountHelper.getUserId(getActivity()));
        req.setUnbindAccount(to);
        String body = JSON.toJSONString(req);
        ApacheHttp.post(WebUrls.UNBIND, body.getBytes(), headers, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                SmartDb.getDb(context).getSmartDao(HeartNotify.class).delete("id > 0 and sender = '"+ to +"'");
                AccountHelper.setBindUserId(context,0);
                MainActivity activity = (MainActivity) getActivity();
                activity.reloadMsgFragment();
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onStart() {

            }
        });
    }

    private void actionBind(String to) {
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(getActivity())).build();
        BindReq req = new BindReq();
        req.setUserid(AccountHelper.getUserId(getActivity()));
        req.setBindAccount(to);
        String body = JSON.toJSONString(req);
        ApacheHttp.post(WebUrls.BIND, body.getBytes(), headers, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                BindRsp rsp = JSON.parseObject(new String(response),BindRsp.class);
                if(rsp.getCode() == 0) {
                    AccountHelper.setBindUserId(context,rsp.getUserid());
                }
                SmartDb.getDb(context).getSmartDao(HeartNotify.class).delete("id > 0 and type = 100");
                MainActivity activity = (MainActivity) getActivity();
                activity.reloadMsgFragment();
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
