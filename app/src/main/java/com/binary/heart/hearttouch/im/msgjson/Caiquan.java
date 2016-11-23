package com.binary.heart.hearttouch.im.msgjson;

/**
 * Created by yaoguoju on 16-4-20.
 */
public class Caiquan {
    public static final int CMD_INVITE = 0;
    public static final int CMD_RESULT = 1;
    public static final int CMD_START  = 2;

    private int cmd;
    private String content;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
