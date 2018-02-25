package com.a55haitao.wwht.utils;

import android.text.TextUtils;

import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.model.annotation.LoginLevels;

import java.util.TreeMap;

/**
 * Created by 董猛 on 16/6/22.
 */
public class HaiParamPrepare {

    private HaiParamPrepare() {
    }

    /**
     * 准备每个请求基本必备的参数
     * 包含: _aid,_pl,_chl,_vc
     *
     * @return
     */
    private static TreeMap<String, Object> baseParams() {
        TreeMap<String, Object> result = new TreeMap<>();
        result.put("_aid", 1001);
        result.put("_pl", "android");
        result.put("_chl", "");

        result.put("_did", DeviceUtils.getDevideId() + "_" + System.currentTimeMillis());
        result.put("_cid", DeviceUtils.getDevideId() + "_" + System.currentTimeMillis() + System.currentTimeMillis());
        result.put("_dtk", HaiUtils.getDeviceToken());

        result.put("_sm", "MD5");
        result.put("_vc", ServerUrl.SERV_VERSION);

        return result;
    }

    /**
     * 参数Build
     * Note 该方法没有返回值,参数build时,会自动添加到map中
     *
     * @param safeLevel
     * @param map       保存参数Build完成后的键值对
     */
    public static void buildParams(@LoginLevels int safeLevel, TreeMap<String, Object> map) {

        map.putAll(baseParams());

        String level = null;
        if (safeLevel == LoginLevels.NONE) {
            level = "55haitao.com";
        } else if (safeLevel == LoginLevels.DEVICE_LEVEL) {
            level = HaiUtils.getDeviceToken();
            map.put("_dtk", level);
            putUserTokenAndId(map);
            /*if (HaiUtils.isLogin()) {
                map.put("_tk", HaiUtils.getUserToken());
                if (!map.containsKey("user_id"))
                    map.put("user_id", HaiUtils.getUserId());
            }*/
        } else {
            level = HaiUtils.getUserToken();
            //            map.put("_tk", HaiUtils.getUserToken());
            putUserTokenAndId(map);
        }

//        Logger.d(map.toString());
        signParams(level, map);
    }

    public static void putUserTokenAndId(TreeMap<String, Object> map) {
        if (HaiUtils.isLogin()) {
            map.put("_tk", HaiUtils.getUserToken());
            if (!map.containsKey("user_id"))
                map.put("user_id", HaiUtils.getUserId());
        }
    }

    /**
     * 参数Build
     *
     * @param safeLevel
     * @param mt
     * @return
     */
    public static TreeMap<String, Object> buildParams(@LoginLevels int safeLevel, String mt) {
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("_mt", mt);
        buildParams(safeLevel, paramMap);
        return paramMap;
    }

    /**
     * 参数签名
     *
     * @param treeMap
     * @return
     */
    private static void signParams(String level, TreeMap<String, Object> treeMap) {

        if (treeMap == null) {
            throw new RuntimeException("提供的Map为空");
        }

        StringBuilder sb = new StringBuilder();
        for (String key : treeMap.keySet()) {
            sb.append(key + "=" + treeMap.get(key));
            if (!treeMap.lastKey().equals(key)) {
                sb.append("&");
            }
        }
        if (!TextUtils.isEmpty(level)) {
            sb.append(level);
        }
        treeMap.put("_sig", MessageDigestUtils.getMD5(sb.toString()));
    }

}
