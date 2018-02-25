package com.a55haitao.wwht.data.event;

/**
 * Created by drolmen on 2017/1/16.
 */

public class EasyOptAction {

    public static final int DEL = 1;
    public static final int UPDATE = 2;

    private int actionType;

    public EasyOptAction(int actionType) {
        this.actionType = actionType;
    }

    public int getActionType() {
        return actionType;
    }
}
