package com.a55haitao.wwht.data.event;

/**
 * class description here
 *
 * @author 陶声
 * @since 2017-01-13
 */

public class EasyoptListChangeEvent {

    public boolean needRefresh;

    public int easyId;
    public int newStatus;

    public EasyoptListChangeEvent() {
        this(true);
    }

    public EasyoptListChangeEvent(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

}
