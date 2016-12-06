package com.binary.heart.hearttouch.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.activity.LoginActivity;
import com.binary.heart.hearttouch.activity.MyInfoActivity;
import com.binary.heart.hearttouch.activity.RegisterActivity;
import com.binary.heart.hearttouch.conf.Configure;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.Account;
import com.binary.heart.hearttouch.jsons.ProfileRsp;
import com.binary.heart.hearttouch.widget.AvatarImageView;
import com.binary.smartlib.io.SmartGraphic;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.net.SmartImageLoader;
import com.binary.smartlib.net.thirdlib.ApacheHttp;
import com.binary.smartlib.net.thirdlib.ApacheHttpHeaders;
import com.binary.smartlib.net.thirdlib.ApacheHttpMultiBody;
import com.binary.smartlib.net.thirdlib.ApacheHttpUrlParams;
import com.binary.smartlib.ui.fragment.SmartFragment;
import com.binary.smartlib.ui.toast.SmartToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**
 * Created by yaoguoju on 16-3-30.
 */
public class ProfileFragment extends SmartFragment{

    private AvatarImageView imgvHead;

    private File saveHead;

    private SmartImageLoader smartImageLoader;

    private TextView nick;
    private TextView sign;
    private ImageView sex;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,null);
        saveHead = new File(context.getFilesDir(), "head.png");
        imgvHead = (AvatarImageView) v.findViewById(R.id.imgv_head);
        imgvHead.setAfterCropListener(new AvatarImageView.AfterCropListener() {
            @Override
            public void afterCrop(Bitmap photo) {
                SmartLog.d(Configure.TAG, "save head " + saveHead.getAbsolutePath());
                SmartGraphic.newFileByBitmap(photo, saveHead);
                uploadHeadPic(saveHead);
            }
        });
        v.findViewById(R.id.ll_profile).setOnClickListener(this);
        nick = (TextView) v.findViewById(R.id.tv_nick);
        sign = (TextView) v.findViewById(R.id.tv_sign);
        sex  = (ImageView) v.findViewById(R.id.imgv_sex);
        return v;
    }

    private void uploadHeadPic(final File saveHead) {
        if(!saveHead.exists()) {
            SmartLog.e(Configure.TAG,"save head file not find");
            return;
        }
        try {
            SmartLog.d(Configure.TAG,"upload head pic");
            ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(context)).build();

            ApacheHttpMultiBody body = new ApacheHttpMultiBody.Builder().addFilePart("headphoto",saveHead)
                    .addStringPart("user", String.valueOf(AccountHelper.getUserId(context))).build();
            ApacheHttp.postMultiBody(WebUrls.PHOTO, body, headers, null, new ApacheHttp.Callback() {
                @Override
                public void onSuccess(byte[] response, int code) {
                  //  refreshHead();
                }

                @Override
                public void onError(int code) {

                }

                @Override
                public void onStart() {

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void refreshHead() {
          if(saveHead.exists()) {
              imgvHead.setImageURI(Uri.fromFile(saveHead));
          }else {
              HashMap<String, String> headers = new HashMap<String, String>();
              headers.put("authHeader", AccountHelper.getAuthValue(context));
              smartImageLoader.setImage(WebUrls.PHOTO + "?user=" + String.valueOf(AccountHelper.getUserId(context)), headers, imgvHead, new SmartImageLoader.ImageLoadCallback() {

                  @Override
                  public void onLoaderStart() {

                  }

                  @Override
                  public void onLoaderSuccess(Bitmap bm) {
                      SmartGraphic.newFileByBitmap(bm, saveHead);
                  }

                  @Override
                  public void onLoaderError(int code) {

                  }
              });
          }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smartImageLoader.recycleLoader();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        smartImageLoader = new SmartImageLoader.Builder(context).setMemRate(0.5f).setCacheEnable(true).build();
        refreshHead();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUserInfo();
    }

    private void refreshUserInfo() {
        ApacheHttpHeaders headers = new ApacheHttpHeaders.Builder().add("authHeader", AccountHelper.getAuthValue(context)).build();
        ApacheHttpUrlParams params = new ApacheHttpUrlParams.Builder().add("user",AccountHelper.getUserId(context)).build();
        ApacheHttp.get(WebUrls.PROFILE, headers, params, new ApacheHttp.Callback() {
            @Override
            public void onSuccess(byte[] response, int code) {
                ProfileRsp rsp = JSON.parseObject(new String(response),ProfileRsp.class);
                if(rsp.getCode() == 0) {
                    nick.setText(rsp.getNick());
                    AccountHelper.setNick(context, rsp.getNick());
                    sign.setText(rsp.getSignature());
                    AccountHelper.setSign(context,rsp.getSignature());
                    String s = rsp.getSex();
                    AccountHelper.setSex(context,s);
                    if("male".equals(s)) {
                        sex.setImageResource(R.drawable.ic_alive_male);
                    }else if("female".equals(s)) {
                        sex.setImageResource(R.drawable.ic_alive_female);
                    }
                    int bindid = rsp.getCurbind();
                    AccountHelper.setBindUserId(context,bindid);
                }
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
    protected void onViewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.imgv_head:
                if(!AccountHelper.isRegisted(context)) {
                    toActivity(getActivity(),RegisterActivity.class,null);
                    return ;
                }

                if(!AccountHelper.isLogined(context)) {
                    toActivity(getActivity(), LoginActivity.class,null);
                    return ;
                }

                break;
            case R.id.ll_profile:
                toActivity(context, MyInfoActivity.class,null);
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(imgvHead != null) {
            imgvHead.onActivityResult(requestCode, resultCode, data);
        }
    }

}
