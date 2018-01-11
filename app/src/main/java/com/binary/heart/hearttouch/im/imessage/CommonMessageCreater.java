package com.binary.heart.hearttouch.im.imessage;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.im.msgjson.Caiquan;
/**
 * Created by yaoguoju on 16-5-5.
 */
public class CommonMessageCreater extends MessageFactory{


//    @Override
//    public ECMessage createMessage(int msg, String to, String content) {
//        ECMessage message = null;
//        switch (msg) {
//            case BIND_REQ:
//                message = bindReq(to,content);
//                break;
//        }
//        return message;
//    }
//
//    private ECMessage bindReq(String to,String content) {
//        // 组建一个待发送的ECMessage
//        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
//        //设置消息的属性：发出者，接受者，发送时间等
//        msg.setForm(ECDeviceKit.getInstance().getUserId());
//        msg.setMsgTime(System.currentTimeMillis());
//        // 设置消息接收者
//        msg.setTo(to);
//        msg.setSessionId(to);
//        // 设置消息发送类型（发送或者接收）
//        msg.setDirection(ECMessage.Direction.SEND);
//        // 创建一个文本消息体，并添加到消息对象中
//
//        ECTextMessageBody msgBody = new ECTextMessageBody(content);
//        msg.setUserData(USER_DATA+BIND_REQ);
//        msg.setBody(msgBody);
//        return msg;
//    }
}
