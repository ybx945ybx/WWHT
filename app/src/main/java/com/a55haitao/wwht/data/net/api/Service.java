package com.a55haitao.wwht.data.net.api;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by 董猛 on 2016/10/24.
 */

public interface Service {
    @POST("m.api")
    Observable<ResponseBody> retrieveData(@Body Map<String, Object> body);
}