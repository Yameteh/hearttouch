package com.binary.heart.hearttouch.jsons;

/**
 * Created by yaoguoju on 16-5-8.
 */
public class UnbindReq {
    private int userid;
    private String unbindAccount;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUnbindAccount() {
        return unbindAccount;
    }

    public void setUnbindAccount(String unbindAccount) {
        this.unbindAccount = unbindAccount;
    }
}
