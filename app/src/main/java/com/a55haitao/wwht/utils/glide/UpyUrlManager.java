package com.a55haitao.wwht.utils.glide;

import android.text.TextUtils;

/**
 * 社区 - 拼接图片url
 *
 * @author 陶声
 * @since 2016-11-19
 */

public class UpyUrlManager {
    public static String getUrl(String url, int picWidth) {
        String result = url;
        if (TextUtils.isEmpty(url))
            return result;

        if (url.contains("upaiyun")) {
            if (url.contains("format")) { // 已经拼参数
                //                result = small_icon + "/fw/" + picWidth + "/quality/80";
                result = url + "/fw/" + picWidth;
            } else {
                //                result = small_icon + "!/fw/" + picWidth + "/quality/80";
                result = url + "!/fw/" + picWidth;
            }
        }
        return result;
    }
}
