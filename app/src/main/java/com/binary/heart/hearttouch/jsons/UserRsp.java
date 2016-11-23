package com.binary.heart.hearttouch.jsons;

/**
 * Created by yaoguoju on 16-4-2.
 */
public class UserRsp {
    private int userid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    private String account;
}
