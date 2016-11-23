package com.binary.heart.hearttouch.jsons;

/**
 * Created by yaoguoju on 16-4-17.
 */
public class ProfileReq {
    private int userid;
    private String nick;
    private String signature;
    private String sex;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
