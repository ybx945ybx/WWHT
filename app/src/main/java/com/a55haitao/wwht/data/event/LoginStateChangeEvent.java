package com.a55haitao.wwht.data.event;

/**
 * Created by 董猛 on 16/8/29.
 */
public class LoginStateChangeEvent {
    public boolean isLogin;

    public LoginStateChangeEvent() {
    }

    public LoginStateChangeEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
