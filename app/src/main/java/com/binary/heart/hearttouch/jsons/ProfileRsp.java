package com.binary.heart.hearttouch.jsons;

/**
 * Created by yaoguoju on 16-4-17.
 */
public class ProfileRsp {
    private int code;
    private String nick;
    private String signature;
    private String sex;
    private int curbind;
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getCurbind() {
        return curbind;
    }

    public void setCurbind(int curbind) {
        this.curbind = curbind;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
