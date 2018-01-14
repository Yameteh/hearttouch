package com.binary.heart.hearttouch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;

import com.beetle.bauhinia.PeerMessageActivity;
import com.beetle.im.IMService;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;

import com.binary.heart.hearttouch.adapter.SearchAdapter;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.im.imessage.CommonMessageCreater;
import com.binary.heart.hearttouch.im.imessage.MessageFactory;
import com.binary.heart.hearttouch.jsons.SearchRsp;

import com.binary.heart.hearttouch.widget.InputDialog;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.ui.fragment.SmartFragment;

import org.w3c.dom.Text;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by yaoguoju on 16-3-30.
 */
public class DiscoverFragment extends SmartFragment implements PtrHandler{

    private PtrFrameLayout mPtrFrame;
    private List<SearchRsp> searchResult;

    private ListView listView;
    private SearchAdapter mAdapter;
    private CommonMessageCreater mMessageCreater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmartLog.d("YYY", "discover oncreate");
        mMessageCreater = new CommonMessageCreater();
        AccountHelper.checkProfile(context);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discover,null);
        mPtrFrame = (PtrFrameLayout) v.findViewById(R.id.store_house_ptr_frame);
        initPtrFrame();
        listView = (ListView) v.findViewById(R.id.lstv_search);
        mAdapter = new SearchAdapter(context);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchRsp rsp = searchResult.get(position);

                enterPeerMessage(rsp.getUserid());
                //showBindReqDialog(rsp.getAccount(),rsp.getNick());
                //SmartPref.put(getActivity(), PrefKeys.BINDACCOUNT, rsp.getAccount());
                //SmartPref.put(getActivity(), PrefKeys.BINDUSERID, rsp.getUserid());
                //mAdapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    private void enterPeerMessage(int sendId) {
        Intent intent = new Intent(context, PeerMessageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("peer_uid", Long.valueOf(sendId));
        intent.putExtra("peer_name", "测试");
        intent.putExtra("current_uid", Long.valueOf(AccountHelper.getUserId(context)));
        startActivity(intent);

    }

    private void showBindReqDialog(final String bindaccount,String bindnick) {
        InputDialog dialog = new InputDialog();
        Bundle bundle = new Bundle();
        String bind = bindnick;
        if(TextUtils.isEmpty(bind)) {
            bind = bindaccount;
        }
        bundle.putString(InputDialog.TITLE,String.format(getString(R.string.title_bindreq),bind));
        bundle.putString(InputDialog.HINT,getString(R.string.hint_bindreq));
        String me = AccountHelper.getNick(context);
        if(TextUtils.isEmpty(me)) {
            me = AccountHelper.getAccount(context);
        }
        bundle.putString(InputDialog.INPUT,String.format(getString(R.string.hint_thisme),me));
        dialog.setArguments(bundle);
        dialog.setOnInputListener(new InputDialog.OnInputListener() {
            @Override
            public void onConfirm(String input) {
                //IMChattingHelper.sendECMessage(mMessageCreater.createMessage(MessageFactory.BIND_REQ,bindaccount,input));
            }

            @Override
            public void onCancel() {

            }
        });
        dialog.show(getFragmentManager(),"bindreq");
    }

    private void initPtrFrame() {
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(true);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        LayoutInflater inflater = LayoutInflater.from(context);
        View headview = inflater.inflate(R.layout.ll_search_head,null);
        mPtrFrame.setHeaderView(headview);
        mPtrFrame.setPtrHandler(this);
    }
    private void initMap() {

    }

    @Override
    public void onResume() {
        super.onResume();
        SmartLog.d("YYY", "discover onresume");

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SmartLog.d("YYY","discover destory");
        if(mAdapter != null) {
            mAdapter.recycle();
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initMap();
        SmartLog.d("YYY","discover initdata");

    }

//    private void markerSelf() {
//        MarkerOptions self = new MarkerOptions();
//        BitmapDescriptor marker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
//        self.icon(marker);
//        LatLng p = new LatLng(LocationHelper.get(context).getLatitude(),
//                LocationHelper.get(context).getLongitude());
//        SmartLog.d(Configure.TAG, "latitude = " + p.latitude + ",longitude = " + p.longitude);
//
//        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(p,15)));
//        self.position(p);
//        aMap.addMarker(self);
//    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btn_search:
//                RadarSearchFragment dialog = new RadarSearchFragment();
//                dialog.show(getFragmentManager(),"1");
//                break;
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void search() {
        int useid = AccountHelper.getUserId(context);
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(getActivity())).build();
        ApacheHttp.post(WebUrls.SEARCH, (""+useid).getBytes(), headers, null, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                mPtrFrame.refreshComplete();
                String rsp = new String(response);
                searchResult = JSON.parseArray(rsp, SearchRsp.class);
                mAdapter.setList(searchResult);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int code) {

            }

            @Override
            public void onStart() {

            }
        });
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if(AccountHelper.getBindUserId(context) > 0 ) {
            return false;
        }
        return true;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        search();
    }
}
