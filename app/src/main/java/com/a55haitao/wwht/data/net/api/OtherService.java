package com.a55haitao.wwht.data.net.api;

import com.a55haitao.wwht.data.model.result.NewsFlashResult;
import com.a55haitao.wwht.data.net.ApiModel;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Other 接口
 *
 * @author 陶声
 * @since 2017-05-08
 */

public interface OtherService {
    /**
     * 优惠快报
     */
    @POST("m.api")
    Observable<ApiModel<NewsFlashResult>> getLetters(@Body Map<String, Object> body);

    /**
     * 优惠快报更新浏览次数
     */
    @POST("m.api")
    Observable<ApiModel<Object>> updateLettersCount(@Body Map<String, Object> body);
}
