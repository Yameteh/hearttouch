package com.binary.heart.hearttouch.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.application.HttApplication;
import com.binary.heart.hearttouch.conf.PrefKeys;
import com.binary.heart.hearttouch.im.imessage.GameMessageCreater;
import com.binary.heart.hearttouch.im.imessage.MessageFactory;
import com.binary.heart.hearttouch.im.msgjson.Caiquan;
import com.binary.heart.hearttouch.widget.explosion.ExplosionField;
import com.binary.smartlib.handler.SmartHandler;
import com.binary.smartlib.io.SmartPref;
import com.binary.smartlib.log.SmartLog;
import com.binary.smartlib.ui.activity.SmartActivity;


import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yaoguoju on 16-4-19.
 */
public class CaiQuanActivity extends SmartActivity{

    private final static String TAG = "CaiQuan";
    private ImageView countDown;
    private AnimationDrawable animCountDown;

    private ViewFlipper viewFlipper;
    private ExplosionField mExplosionField;
    private LinearLayout me;
    private ImageView  meResult;
    private ImageView  meHead;
    private LinearLayout opponent;
    private ImageView  opponentResult;
    private ImageView  opponentHead;


    private LinearLayout result;
    private ImageView   resultImg;
    private TextView resultTxt;

    private GameMessageCreater mMsgCreater;

    private AtomicInteger count = new AtomicInteger(0);

    private boolean gameStarted = false;

    private void AddCount(){
        int i = count.incrementAndGet();
        if(i == 2) {
            stopCountDownAnim();
            showMeResult();
            showOpponentResult();
            final int state = checkResult();
            SmartHandler.get(context).postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResult(state);
                        }
                    });
                }
            },1000);

        }
    }

    private void resetCount(){
        count.set(0);
    }

    public static final String FROM_NOTIFY = "fromNotify";
    private boolean isFromNotify = false;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_caiquan);
        initActionBar();
        //mExplosionField = ExplosionField.attach2Window(this);

        countDown = (ImageView) findViewById(R.id.imgv_countdown);

        viewFlipper = (ViewFlipper) findViewById(R.id.vf_select);
        viewFlipper.setOnClickListener(this);
        me = (LinearLayout) findViewById(R.id.ll_me);
        meResult = (ImageView) findViewById(R.id.imgv_me_result);
        meHead  = (ImageView)findViewById(R.id.imgv_me_head);
        File file = new File(getFilesDir(),"head.png");
        meHead.setImageURI(Uri.fromFile(file));
        me.setVisibility(View.INVISIBLE);

        opponent = (LinearLayout) findViewById(R.id.ll_opponent);
        opponentResult = (ImageView)findViewById(R.id.imgv_opponent_result);
        opponentHead   = (ImageView)findViewById(R.id.imgv_opponent_head);
        opponent.setVisibility(View.INVISIBLE);
        File bindfile = new File(getFilesDir(),"bindhead.png");
        opponentHead.setImageURI(Uri.fromFile(bindfile));

        result = (LinearLayout) findViewById(R.id.ll_result);
        resultImg = (ImageView) findViewById(R.id.imgv_result);
        resultTxt = (TextView) findViewById(R.id.tv_result);
        hideResult();


        mMsgCreater = new GameMessageCreater();

    }

    /**
     * 初始化actionBar
     */
    private void initActionBar() {
        View actionbar = findViewById(R.id.rl_actionbar);
        int actionbar_height = getResources().getDimensionPixelSize(R.dimen.height_actionbar);
        int actionbar_width  = ViewGroup.LayoutParams.MATCH_PARENT;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(actionbar_width,actionbar_height);
        //沉浸ActionBar
        if(Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            params.setMargins(0, HttApplication.getStatusBarHeight(),0,0);
        }
        actionbar.setLayoutParams(params);

        TextView title = (TextView) findViewById(R.id.tv_actionbar_title);
        title.setText(R.string.title_caiquan);
        ImageButton back = (ImageButton)findViewById(R.id.btn_actionbar_back);
        back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent != null) {
            isFromNotify = intent.getBooleanExtra(FROM_NOTIFY,false);
        }

        if(isFromNotify) {
            //IMChattingHelper.sendECMessage(mMsgCreater.createMessage(MessageFactory.GAME_START,AccountHelper.getBindAccount(context),""));
            gameStarted = true;
            resetGame();
        }else {
            //IMChattingHelper.sendECMessage(mMsgCreater.createMessage(GameMessageCreater.GAME_INVITE, AccountHelper.getBindAccount(context),""));
        }

//        IMChattingHelper.setOnMessageReportCallback(new IMChattingHelper.OnMessageReportCallback() {
//            @Override
//            public void onMessageReport(ECError error, ECMessage message) {
//
//            }
//
//            @Override
//            public void onPushMessage(String sessionId, List<ECMessage> msgs) {
//                SmartLog.d(TAG, "sessionId == " + sessionId + ",msg count " + msgs.size());
//                if (msgs != null) {
//                    ECMessage msg = msgs.get(0);
//
//                    String msgUserData = msg.getUserData();
//                    String[] m = msgUserData.split(":");
//                    int type = Integer.parseInt(m[1]);
//                    if (type == MessageFactory.GAME_START) {
//                        gameStarted = true;
//                        resetGame();
//                    } else if (type == MessageFactory.GAME_RESULT) {
//                        ECTextMessageBody body = (ECTextMessageBody) msg.getBody();
//                        Caiquan cai = JSON.parseObject(body.getMessage(), Caiquan.class);
//                        final int id = Integer.parseInt(cai.getContent());
//                        opponetSelect = id;
//                        SmartLog.d("YYY","oppentSelect" +opponetSelect);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                switch (id) {
//                                    case R.id.imgv_chuitou:
//                                        SmartLog.d("YYY","chuitou "+R.id.imgv_chuitou);
//                                        opponentResult.setImageResource(R.drawable.ic_chuitou);
//                                        AddCount();
//                                        break;
//                                    case R.id.imgv_bu:
//                                        SmartLog.d("YYY","imgv_bu "+R.id.imgv_bu);
//                                        opponentResult.setImageResource(R.drawable.ic_bu);
//                                        AddCount();
//
//                                        break;
//                                    case R.id.imgv_jiandao:
//                                        SmartLog.d("YYY","imgv_jiandao "+R.id.imgv_jiandao);
//
//                                        AddCount();
//                                        opponentResult.setImageResource(R.drawable.ic_jiandao);
//                                        break;
//
//                                }
//                            }
//                        });
//
//
//                    }
//
//                }
//            }
//        });
    }

    private void resetGame() {
        meSelect = 0;
        opponetSelect = 0;
        resetCount();
        startSelect();
        hideResult();
        startCountDownAnim();
    }


    private void showOpponentResult() {
        opponent.setVisibility(View.VISIBLE);
//        ObjectAnimator opponentin = ObjectAnimator.ofFloat(opponent,"translationX",-500.0f,0.0f).setDuration(1000);
//        opponentin.start();
    }

    private void hideOpponentResult() {
        opponent.setVisibility(View.INVISIBLE);
//        ObjectAnimator opponentout = ObjectAnimator.ofFloat(opponent,"alpha",1.0f,0f).setDuration(500);
//        opponentout.start();
//        SmartHandler.get(context).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        opponent.setVisibility(View.INVISIBLE);
//                    }
//                });
//            }
//        }, 500);
    }

    private void showMeResult() {
        me.setVisibility(View.VISIBLE);
//        ObjectAnimator mein = ObjectAnimator.ofFloat(me,"translationX",500.0f,0.0f).setDuration(1000);
//        mein.start();
    }

    private void hideMeResult() {
        me.setVisibility(View.INVISIBLE);

//        ObjectAnimator meout = ObjectAnimator.ofFloat(me,"alpha",1.0f,0f).setDuration(500);
//        SmartHandler.get(context).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        me.setVisibility(View.INVISIBLE);
//                    }
//                });
//            }
//        }, 500);
    }

    private void startSelect(){
        viewFlipper.setVisibility(View.VISIBLE);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(800);
        viewFlipper.startFlipping();
    }

    private void stopSelect() {
        viewFlipper.setVisibility(View.INVISIBLE);
        viewFlipper.stopFlipping();
    }

    private void startCountDownAnim() {
        countDown.setVisibility(View.VISIBLE);
        countDown.setBackgroundResource(R.drawable.anim_countdown);
        animCountDown = (AnimationDrawable)countDown.getBackground();
        animCountDown.start();
        SmartHandler.get(context).postDelayed(
                timeout,5500);
    }

    private Runnable timeout = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stopCountDownAnim();
                    int state = checkResult();
                    showResult(state);
                    //showMeResult();
                }
            });

        }
    };


    private int checkResult() {
        if (meSelect == 0 || opponetSelect == 0) {
            return 0;
        }

        if (meSelect == R.id.imgv_chuitou) {
            if(opponetSelect == R.id.imgv_jiandao) {
                return 1;
            }else if(opponetSelect == R.id.imgv_chuitou) {
                return 3;
            }else if(opponetSelect == R.id.imgv_bu) {
                return -1;
            }
        }else if(meSelect == R.id.imgv_jiandao) {
            if(opponetSelect == R.id.imgv_bu) {
                return 1;
            }else if(opponetSelect == R.id.imgv_chuitou) {
                return -1;
            }else if(opponetSelect == R.id.imgv_jiandao) {
                return 3;
            }
        }else if(meSelect == R.id.imgv_bu) {
            if(opponetSelect == R.id.imgv_bu) {
                return 3;
            }else if(opponetSelect == R.id.imgv_chuitou) {
                return 1;
            }else if(opponetSelect == R.id.imgv_jiandao) {
                return -1;
            }
        }
        return 3;
    }

    private void stopCountDownAnim() {
        SmartHandler.get(context).removeCallbacks(timeout);
        countDown.setVisibility(View.INVISIBLE);
        if(animCountDown != null) {
            animCountDown.stop();
        }
    }
    private int win = 0;
    private int lose = 0;
    private void showResult(int state) {
        hideMeResult();
        hideOpponentResult();
        result.setVisibility(View.VISIBLE);
        if(state == 1) {
            resultImg.setImageResource(R.drawable.ic_youwin);
            win ++;
            resultTxt.setText("赢");
        }else if(state == -1) {
            resultImg.setImageResource(R.drawable.ic_youlose);
            lose ++;
            resultTxt.setText("输");
        }else if(state == 3) {
            resultImg.setImageDrawable(null);
            resultTxt.setText("平");
        }else if(state == 0) {
            resultTxt.setText("本轮超时");
        }
        SmartHandler.get(context).postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resetGame();
                    }
                });
            }
        },1000);
    }

    private void hideResult() {
        result.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void onViewClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.vf_select:
                setMeSelect();
              //  mExplosionField.explode(v);
                break;
            case R.id.btn_actionbar_back:
                finish();
                break;
        }
    }

    private void setMeSelect() {
        if(!gameStarted) {
            return ;
        }
        int id = viewFlipper.getCurrentView().getId();
        switch (id) {
            case R.id.imgv_chuitou:
                meResult.setImageResource(R.drawable.ic_chuitou);
                break;
            case R.id.imgv_bu:
                meResult.setImageResource(R.drawable.ic_bu);
                break;
            case R.id.imgv_jiandao:
                meResult.setImageResource(R.drawable.ic_jiandao);
                break;
        }
        meSelect = id;
        AddCount();
        //IMChattingHelper.sendECMessage(mMsgCreater.createMessage(MessageFactory.GAME_RESULT, AccountHelper.getBindAccount(context), String.valueOf(id)));
        stopSelect();
       // mExplosionField.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String text = "W:"+win+",L:"+lose;
        SmartPref.put(context, PrefKeys.CAIQUAN_RESULT,text);
    }

    private int meSelect ;
    private int opponetSelect;
}
