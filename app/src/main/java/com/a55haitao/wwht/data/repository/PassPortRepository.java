package com.a55haitao.wwht.data.repository;

import android.text.TextUtils;

import com.a55haitao.wwht.data.model.annotation.LoginLevels;
import com.a55haitao.wwht.data.model.entity.AddressItemBean;
import com.a55haitao.wwht.data.model.entity.AddressListBean;
import com.a55haitao.wwht.data.model.entity.CommonSuccessResult;
import com.a55haitao.wwht.data.model.entity.CouponListBean;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.api.PassPortService;
import com.a55haitao.wwht.utils.HaiParamPrepare;

import java.util.TreeMap;

import rx.Observable;

/**
 * class description here
 *
 * @author 陶声
 * @since 2017-05-08
 */

public class PassPortRepository extends BaseRepository {
    private static final String BASE_METHOD_URL = "minishop_sns.PassportAPI/";

    private static volatile PassPortRepository instance;
    private final           PassPortService    mPassPortService;

    private PassPortRepository() {
        mPassPortService = RetrofitFactory.createService(PassPortService.class);
    }

    public static PassPortRepository getInstance() {
        if (instance == null) {
            synchronized (PassPortRepository.class) {
                if (instance == null) {
                    instance = new PassPortRepository();
                }
            }
        }
        return instance;
    }

    /**
     * 地址详情
     */
    public Observable<AddressItemBean> addressDetail(int id) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "address_detail");
        map.put("id", id);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mPassPortService.addressDetail(map));
    }

    /**
     * 删除地址
     */
    public Observable<CommonSuccessResult> deleteAddress(int id) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "del_address");
        map.put("ids", id);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mPassPortService.deleteAddress(map));
    }

    /**
     * 收货地址列表
     */
    public Observable<AddressListBean> addressList() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "address_list");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mPassPortService.addressList(map));
    }

    /**
     * 优惠券列表
     */
    public Observable<CouponListBean> getCouponList(int type) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "getcouponqList");
        map.put("type", type);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mPassPortService.getCouponList(map));
    }

    /**
     * 领优惠券
     */
    public Observable<Object> receiveCoupon(String couponId) {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "receive_couponq");
        map.put("couponq_id", couponId);
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mPassPortService.receiveCoupon(map));
    }

    /**
     * 新增/修改地址
     */
    public Observable<AddressItemBean> addOrUpdateAddress(int id, String phone, String name, String IDNumber, String province, String city, String dist, String street, String IDImgA, String IDImgB, boolean isDefault) {
        TreeMap<String, Object> map = new TreeMap<>();

        if (id == -1) {
            map.put("_mt", BASE_METHOD_URL + "add_address");
        } else {
            map.put("_mt", BASE_METHOD_URL + "update_address");
            map.put("id", id);
        }
        map.put("phone", phone);
        map.put("name", name);
        map.put("idt_number", IDNumber);
        map.put("province", province);
        map.put("city", city);
        map.put("dist", dist);
        map.put("street", street);
        map.put("idt_a", TextUtils.isEmpty(IDImgA) ? "" : IDImgA);
        map.put("idt_b", TextUtils.isEmpty(IDImgB) ? "" : IDImgB);

        map.put("is_default", isDefault ? "1" : "0");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mPassPortService.addOrUpdateAddress(map));
    }

    /**
     * 获取默认收货地址
     */
    public Observable<AddressItemBean> getDefaultAddress() {
        TreeMap<String, Object> map = new TreeMap<>();
        map.put("_mt", BASE_METHOD_URL + "get_default_address");
        HaiParamPrepare.buildParams(LoginLevels.USER_LEVEL, map);
        return transform(mPassPortService.getDefaultAddress(map));
    }
}
