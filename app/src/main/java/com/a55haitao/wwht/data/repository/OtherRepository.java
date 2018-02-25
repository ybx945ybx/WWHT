package com.a55haitao.wwht.data.repository;

import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.result.NewsFlashResult;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.OtherService;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.TreeMap;

import rx.Observable;

/**
 * Other接口 数据仓库
 *
 * @author 陶声
 * @since 2017-05-08
 */

public class OtherRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "55haitao_sns.OtherAPI/";
    private static final int    REQUEST_COUNT   = 20;

    private static volatile OtherRepository instance;
    private final OtherService mOtherService;

    private OtherRepository() {
        mOtherService = RetrofitFactory.createService(OtherService.class);
    }

    public static OtherRepository getInstance() {
        if (instance == null) {
            synchronized (OtherRepository.class) {
                if (instance == null) {
                    instance = new OtherRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 优惠快报
     *
     * @param page 当前页数
     */
    public Observable<NewsFlashResult> getLetters(int page) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_letters");
        map.put("page", page);
        map.put("count", REQUEST_COUNT / 2);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mOtherService.getLetters(map));
    }

    /**
     * 优惠快报更新浏览次数
     *
     * @param id ID
     */
    public Observable<Object> updateLettersCount(int id) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "update_letters_count");
        map.put("id", id);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mOtherService.updateLettersCount(map));
    }

}
