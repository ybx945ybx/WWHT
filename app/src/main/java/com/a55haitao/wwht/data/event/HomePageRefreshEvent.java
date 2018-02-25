package com.a55haitao.wwht.data.event;

/**
 * 用户主页刷新事件
 *
 * @author 陶声
 * @since 2016-12-07
 */

public class HomePageRefreshEvent {
    public HomePageRefreshEvent() {
    }

    public HomePageRefreshEvent(boolean isMyHomePage) {
        this.isMyHomePage = isMyHomePage;
    }

    public boolean isMyHomePage; // 是否是我的主页
}
