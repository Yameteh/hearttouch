package com.binary.heart.hearttouch.jsons;

/**
 * Created by yaoguoju on 16-4-1.
 */
public class LoginRsp {
    public final static int CODE_LOGINERR  = 200;
    public final static int CODE_LOGINGOK   = 0;
    private int code;
    private int userid;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
