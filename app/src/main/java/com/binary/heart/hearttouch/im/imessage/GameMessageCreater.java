package com.binary.heart.hearttouch.im.imessage;

import com.alibaba.fastjson.JSON;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.im.msgjson.Caiquan;
import com.binary.heart.hearttouch.jsons.Account;


/**
 * Created by yaoguoju on 16-4-20.
 */
public class GameMessageCreater extends MessageFactory {


//    public ECMessage createMessage(int msg,String to,String content) {
//        ECMessage message = null;
//        switch (msg) {
//            case GAME_INVITE:
//                message = gameInvite(to);
//                break;
//            case GAME_START:
//                message = gameStart(to);
//                break;
//            case GAME_RESULT:
//                message = gameResult(to,content);
//                break;
//        }
//        return message;
//    }
//
//    private ECMessage gameInvite(String to) {
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
//        Caiquan caiquan = new Caiquan();
//        caiquan.setCmd(Caiquan.CMD_INVITE);
//        ECTextMessageBody msgBody = new ECTextMessageBody(JSON.toJSONString(caiquan));
//        msg.setUserData(USER_DATA+GAME_INVITE);
//        msg.setBody(msgBody);
//        return msg;
//    }
//
//    private ECMessage gameStart(String to) {
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
//        Caiquan caiquan = new Caiquan();
//        caiquan.setCmd(Caiquan.CMD_START);
//        ECTextMessageBody msgBody = new ECTextMessageBody(JSON.toJSONString(caiquan));
//        msg.setUserData(USER_DATA+GAME_START);
//        msg.setBody(msgBody);
//        return msg;
//    }
//
//    private ECMessage gameResult(String to,String content) {
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
//        Caiquan caiquan = new Caiquan();
//        caiquan.setCmd(Caiquan.CMD_RESULT);
//        caiquan.setContent(content);
//        ECTextMessageBody msgBody = new ECTextMessageBody(JSON.toJSONString(caiquan));
//        msg.setUserData(USER_DATA+GAME_RESULT);
//        msg.setBody(msgBody);
//        return msg;
//    }
}
