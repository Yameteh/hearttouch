package com.binary.heart.hearttouch.fragment;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.widget.heartlayout.HeartLayout;
import com.binary.smartlib.io.SmartGraphic;
import com.binary.smartlib.net.SmartImageLoader;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yaoguoju on 16-4-22.
 */
public class AdDialogFragment extends DialogFragment implements View.OnClickListener {

    private Timer mTimer = new Timer();
    private HeartLayout mHeartLayout;
    private ImageView mAd;
    private SmartImageLoader smartImageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        smartImageLoader = new SmartImageLoader.Builder(getActivity()).setCacheEnable(false).setMemRate(0.5f).build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ad,null);
        mHeartLayout = (HeartLayout) v.findViewById(R.id.hl_heartanim);
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHeartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mHeartLayout.addHeart(SmartGraphic.randomColor());
                    }
                });
            }
        }, 500, 200);

        mHeartLayout.setOnClickListener(this);

        mAd = (ImageView) v.findViewById(R.id.imgv_ad);
        refreshAd();
        return v;
    }

    private void refreshAd() {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put(WebUrls.AUTH_KEY, AccountHelper.getAuthValue(getActivity()));
            smartImageLoader.fillImage(WebUrls.AD + "?user=" + String.valueOf(AccountHelper.getUserId(getActivity())), headers, mAd, new SmartImageLoader.ImageLoadCallback() {

                @Override
                public void onLoaderStart() {

                }

                @Override
                public void onLoaderSuccess(Bitmap bm) {
                }

                @Override
                public void onLoaderError(int code) {

                }
            });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.hl_heartanim:
                dismiss();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTimer.cancel();
        smartImageLoader.recycleLoader();
    }
}
