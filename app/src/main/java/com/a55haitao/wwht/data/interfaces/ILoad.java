package com.a55haitao.wwht.data.interfaces;

/**
 * Created by 55haitao on 2016/10/28.
 */

public interface ILoad {
    /**
     * 是否还有更多
     * @return
     */
    boolean hasMore() ;

    void loadMore();
}
