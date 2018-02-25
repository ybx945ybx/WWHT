package com.a55haitao.wwht.data.event;

/**
 * Created by a55 on 2017/3/27.
 * 草单点赞事件
 */

public class EasyOptFavourCountChangeEvent {
    public int point_count;                           // 草单被点赞数量
    public int easyoptId;                       // 草单id
    public int favourit;                          // 是否赞

    public EasyOptFavourCountChangeEvent(int count) {
        this.point_count = count;
    }

    public EasyOptFavourCountChangeEvent(int point_count, int easyoptId) {
        this.point_count = point_count;
        this.easyoptId = easyoptId;
    }
}
