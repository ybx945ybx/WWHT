package com.a55haitao.wwht.data.repository;

import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.CartService;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.TreeMap;

import rx.Observable;

/**
 * 购物车 数据仓库
 *
 * @author 陶声
 * @since 2017-05-08
 */

public class CartRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "minishop_sns.CartAPI/";

    private static volatile CartRepository instance;
    private final           CartService    mCartService;

    private CartRepository() {
        mCartService = RetrofitFactory.createService(CartService.class);
    }

    public static CartRepository getInstance() {
        if (instance == null) {
            synchronized (CartRepository.class) {
                if (instance == null) {
                    instance = new CartRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 添加购物车
     *
     * @param spuid spuid
     * @param skuid skuid
     * @param count 个数
     */
    public Observable<CommonDataBean> addCart(String spuid, String skuid, int count) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "add_cart");
        map.put("spuid", spuid);
        map.put("skuid", skuid);
        map.put("count", count);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mCartService.addCart(map));
    }

    /**
     * 购物车列表
     */
    public Observable<ShoppingCartBean> cartList() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "cart_list");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mCartService.cartList(map));
    }

    /**
     * 删除购物车
     *
     * @param cartIds id
     */
    public Observable<CommonDataBean> delCart(String cartIds) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "del_cart");
        map.put("cart_ids", cartIds);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mCartService.delCart(map));
    }

}
