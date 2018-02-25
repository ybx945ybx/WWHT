package com.a55haitao.wwht.data.event;

/**
 * Created by drolmen on 2017/1/15.
 * 草单收藏
 */

public class EasyOptLikeCountChangeEvent {
    public int count;                           // 草单被收藏数量
    public int easyoptId;                       // 草单id
    public int likeit;                          // 是否收藏

    public EasyOptLikeCountChangeEvent(int count) {
        this.count = count;
    }

    public EasyOptLikeCountChangeEvent(int count, int easyoptId, int likeit) {
        this.count = count;
        this.easyoptId = easyoptId;
        this.likeit = likeit;
    }
}
