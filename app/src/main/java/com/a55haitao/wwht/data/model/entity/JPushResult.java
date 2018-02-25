package com.a55haitao.wwht.data.model.entity;

/**
 * class description here
 *
 * @author 陶声
 * @since 2017-04-21
 */

public class JPushResult {
    public int         type;
    public MessageBean notice;

    @Override
    public String toString() {
        return "JPushResult{" +
                "type=" + type +
                ", notice=" + notice +
                '}';
    }
}
