package com.a55haitao.wwht.data.exception;

/**
 * Created by Haotao_Fujie on 2016/11/17.
 */

public class HTException extends Exception{
    public int code;
    public String msg;

    public static HTException createHTException(int code, String msg) {
        HTException ex = new HTException(code, msg);
        ex.code = code;
        ex.msg = msg;
        return ex;
    }

    public HTException(int code, String msg) {
        super(code + "###" + msg);
    }

}
