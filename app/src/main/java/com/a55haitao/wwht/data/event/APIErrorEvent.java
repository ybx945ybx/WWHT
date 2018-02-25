package com.a55haitao.wwht.data.event;

/**
 * Created by Haotao_Fujie on 2016/11/12.
 */

public class APIErrorEvent {
    public int code;
    public String msg;

    public APIErrorEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
