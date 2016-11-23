package com.binary.heart.hearttouch.im.imessage;

import com.yuntongxun.ecsdk.ECMessage;

/**
 * Created by yaoguoju on 16-4-20.
 */
public abstract class MessageFactory {
    public static final int GAME_INVITE = 0;

    public static final int GAME_START = 1;
    public static final int GAME_RESULT = 2;

    public static final int BIND_REQ = 3;


    public static final String USER_DATA = "heartdata:";

    public abstract ECMessage createMessage(int msg,String to,String content);
}
