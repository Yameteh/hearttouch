package com.binary.heart.hearttouch.im.notify;

/**
 * Created by yaoguoju on 16-5-6.
 */
public class HeartNotify {
    private int type;
    private String content;
    private String sender;
    private String time;

    @Override
    public String toString() {
        return "HeartNotify{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
