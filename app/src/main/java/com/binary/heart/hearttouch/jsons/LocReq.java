package com.binary.heart.hearttouch.jsons;

import android.os.SystemClock;

/**
 * Created by yaoguoju on 16-4-10.
 */
public class LocReq {
    private int userid;
    private String latitude;
    private String longitude;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
