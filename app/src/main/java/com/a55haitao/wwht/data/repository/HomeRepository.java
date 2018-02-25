package com.a55haitao.wwht.data.repository;

import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.entity.ActivityBean;
import com.a55haitao.wwht.data.model.entity.TabBean;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.data.model.entity.TabFavorableBean;
import com.a55haitao.wwht.data.model.result.AdResult;
import com.a55haitao.wwht.data.model.result.DailyProductResult;
import com.a55haitao.wwht.data.model.result.FlashSaleResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.TestSubscriber;
import com.a55haitao.wwht.data.net.api.HomeService;
import com.a55haitao.wwht.utils.HaiParamPrepare;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.TreeMap;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Home 数据仓库
 *
 * @author 陶声
 * @since 2017-05-08
 */

public class HomeRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "55haitao_sns.HomeAPI/";
    private static final int    REQUEST_COUNT   = 20;

    private static volatile HomeRepository instance;
    private final           HomeService    mHomeService;

    private HomeRepository() {
        mHomeService = RetrofitFactory.createService(HomeService.class);
    }

    public static HomeRepository getInstance() {
        if (instance == null) {
            synchronized (HomeRepository.class) {
                if (instance == null) {
                    instance = new HomeRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 专题底部推荐
     */
    public Observable<TabFavorableBean> getRelateFavorable(String specialId, int page) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_ralate_favorable");
        map.put("favorable_id", specialId);
        map.put("page", page);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.getRelateFavorable(map));
    }

    /**
     * For hot, position 0 热卖单品 position 1 新品推荐
     *
     * @param page 当前页数
     */
    public Observable<FlashSaleResult> hotProductsV11(int page) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "hot_products_v11");
        map.put("page", page);
        map.put("count", REQUEST_COUNT);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.hotProductsV11(map));
    }

    public Observable<ResponseBody> getFlashSaleList(int page) {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        treeMap.put("_mt", BASE_METHOD_URL + "hot_products_v11");
        treeMap.put("page", page);
        treeMap.put("count", 20);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, treeMap);
        return createObservable(treeMap);
    }

    /**
     * 开屏页广告图
     */
    public Observable<AdResult> indexAdvert() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "index_advert");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.indexAdvert(map));
    }

    /**
     * 首页Tab
     */
    public Observable<TabBean> homeTabs() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "home_tabs_v1");
        HaiParamPrepare.buildParams(LoginLevels.NONE, map);
        return transform(mHomeService.homeTabs(map));
    }

    /**
     * 首页数据
     */
    public void getTabDataWithName(String name, TestSubscriber<JSONArray> inSubscribe) {
        int uid = 0;
        if (HaiUtils.isLogin()) {
            uid = UserRepository.getInstance().getUserInfo().getId();
        }
        mHomeService.retrieveTabDataWithName(name.equals("热门") ? "index" : name, uid, "2.3")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(inSubscribe);
    }

    /**
     * 获取首页的活动
     */
    public Observable<ActivityBean> getActivity() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_activity");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.getActivity(map));
    }

    /**
     * 获取官网特卖及精选合集页Tab
     */
    public Observable<JsonArray> getTabs() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_tabs");
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.Tabs(map));
    }

    /**
     * 根据tab_id获取精选合集列表
     */
    public Observable<TabEntryBean> getEntriesById(int page, int tab_id) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_tab_entry_v2");
        map.put("page", page == -1 ? 1 : page + 1);
        map.put("count", 20);
        map.put("tab_id", tab_id);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.getEntriesList(map));
    }

    /**
     * 根据tab_id获取官网特卖列表
     */
    public Observable<TabFavorableBean> getFavorableById(int page, int tab_id) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_tab_favorable_all");
        map.put("page", page == -1 ? 1 : page + 1);
        map.put("count", 20);
        map.put("tab_id", tab_id);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.getFavorablesList(map));
    }

    /**
     * 超值单品分页加载
     */
    public Observable<DailyProductResult> getDailyProduct(int page) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "better_products");
        map.put("page", page == -1 ? 1 : page);
        map.put("count", 10);
        HaiParamPrepare.buildParams(LoginLevels.DEVICE_LEVEL, map);
        return transform(mHomeService.getDailyProduct(map));
    }

}
