package com.a55haitao.wwht.data.event;

/**
 * 添加商品到心愿单 - 事件
 *
 * @author 陶声
 * @since 2017-04-07
 */

public class ProductLikeEvent {
    public boolean liklikeState;
    public String spuid;

    public ProductLikeEvent(boolean likeState, String spuid) {
        this.liklikeState = likeState;
        this.spuid = spuid;
    }

}
