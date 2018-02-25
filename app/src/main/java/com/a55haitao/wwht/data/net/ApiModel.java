package com.a55haitao.wwht.data.net;

/**
 * Api回调包装
 *
 * @author 陶声
 * @since 2016-10-10
 */

public class ApiModel<T> {
    public String msg;

    public StatBean stat;

    public int code;

    public T data;

    public static class StatBean{
        public String cid;
        public long   systime;
    }

    public T getData() {
        return data;
    }
}
