package com.a55haitao.wwht.data.repository;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.widget.Toast;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ProductInfoChangeEvent;
import com.a55haitao.wwht.data.exception.HTException;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.data.net.ApiModel;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.Service;
import com.a55haitao.wwht.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Base数据仓库
 *
 * @author 陶声
 * @since 2016-10-10
 */

public class BaseRepository {

    private Service mService;

    public BaseRepository() {
        mService = RetrofitFactory.createService(Service.class);
    }

    protected <T> Observable<T> transform(Observable<ApiModel<T>> observable) {
        return this.transform(observable, null, false);
    }

    protected <T> Observable<T> transform(Observable<ApiModel<T>> observable, Context context, boolean needSystemTime) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(result -> {
                    if (result == null) {
                        return Observable.error(new NetworkErrorException());
                    } else {
                        if (result.code == 0) { // 成功
                            if (needSystemTime) {
                                if (result.stat != null) {
                                    Toast.makeText(context, result.stat.toString() + "解析的stat", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, result.stat.systime + "解析的", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, result.stat.cid + "解析的cid", Toast.LENGTH_SHORT).show();
                                    SPUtils.put(context, "systemTime", result.stat.systime);

                                    //                                    HaiApplication.productSystemTime = result.stat.systime;
                                }
                            }
                            return Observable.just(result.data);
                        } else if (result.code == HaiConstants.ReponseCode.E_PRODUCT_CHeckPRICE_FAILURE) {
                            EventBus.getDefault().post(new ProductInfoChangeEvent((OrderCommitBean) result.data, result.msg));
                            return Observable.error(HTException.createHTException(result.code, result.msg));
                        } else { // 错误
                            return Observable.error(HTException.createHTException(result.code, result.msg));
                        }
                    }
                });
    }

    /**
     * 通用创建一个Observable<ResponseBody>的方法
     *
     * @param map
     * @return
     */
    public final Observable<ResponseBody> createObservable(Map<String, Object> map) {
        return mService.retrieveData(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public TreeMap<String, Object> generateMTParam(String mt) {
        TreeMap<String, Object> param = new TreeMap<>();
        param.put("_mt", mt);
        return param;
    }
}
