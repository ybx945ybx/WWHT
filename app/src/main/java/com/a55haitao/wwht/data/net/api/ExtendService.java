package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.result.FullcutDetailResult;
import com.a55haitao.wwht.data.model.result.FullcutHasCheckResult;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by a55 on 2017/7/12.
 */

public interface ExtendService {
    /**
     * 满减优惠活动商品列表
     */
    @POST("m.api")
    Observable<ApiModel<FullcutDetailResult>> getFullcutDetail(@Body Map<String, Object> body);

    /**
     * 商详页检查商品是否是满减优惠活动
     */
    @POST("m.api")
    Observable<ApiModel<FullcutHasCheckResult>> getFullcutHasCheck(@Body Map<String, Object> body);

}
