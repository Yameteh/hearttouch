package com.binary.heart.hearttouch.jsons;

/**
 * Created by yaoguoju on 16-3-31.
 */
public class RegRsp {

    public final static int CODE_ISREGED = 100;
    public final static int CODE_REGERR  = 200;
    public final static int CODE_REGOK   = 0;
    private int code;
    private int userid;

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
