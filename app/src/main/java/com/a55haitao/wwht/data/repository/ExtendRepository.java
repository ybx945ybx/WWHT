package com.a55haitao.wwht.data.repository;

import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.result.FullcutDetailResult;
import com.a55haitao.wwht.data.model.result.FullcutHasCheckResult;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.ExtendService;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.TreeMap;

import rx.Observable;

/**
 * Created by a55 on 2017/7/12.
 */

public class ExtendRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "55haitao_sns.ExtendAPI/";

    private static volatile ExtendRepository instance;
    private final           ExtendService    mExtendService;

    private ExtendRepository() {
        mExtendService = RetrofitFactory.createService(ExtendService.class);
    }

    public static ExtendRepository getInstance() {
        if (instance == null) {
            synchronized (ExtendRepository.class) {
                if (instance == null) {
                    instance = new ExtendRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 满减优惠活动商品列表
     */
    public Observable<FullcutDetailResult> getFullcutDetail(int page, String fid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "fullcut_detail");
        paramMap.put("page", page);
        paramMap.put("fid", fid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mExtendService.getFullcutDetail(paramMap));
    }

    /**
     * 满减优惠活动商品列表
     */
    public Observable<FullcutHasCheckResult> getFullcutHasCheck(String spuid) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", BASE_METHOD_URL + "fullcut_has_check");
        paramMap.put("spuid", spuid);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, paramMap);
        return transform(mExtendService.getFullcutHasCheck(paramMap));
    }
}
