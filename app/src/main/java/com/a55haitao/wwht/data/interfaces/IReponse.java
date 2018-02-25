package com.a55haitao.wwht.data.interfaces;

/**
 * Created by 董猛 on 2016/10/27.
 */

public interface IReponse<T> {
    void onSuccess(T t);

    void onFinish();

    void onError(Throwable e);
}
