package com.a55haitao.wwht.data.event;

/**
 * 草单评论数修改
 *
 * @author 陶声
 * @since 2017-01-18
 */

public class EasyoptCommentCountChangeEvent {
    public int count;

    public EasyoptCommentCountChangeEvent(int count) {
        this.count = count;
    }
}
