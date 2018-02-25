package com.a55haitao.wwht.data.event;

/**
 * 创建草单数量变化
 */

public class EasyOptCreateCountChangeEvent {
    public int count;

    public EasyOptCreateCountChangeEvent(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return super.toString() + " count: " + count;
    }
}
