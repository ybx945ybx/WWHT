package com.a55haitao.wwht.utils;

import android.text.InputFilter;

/**
 * 文字处理工具类
 *
 * @author 陶声
 * @since 2016-09-05
 */
public class TxtUtils {
    /**
     * 过滤emoji
     */
    public static InputFilter EMOJI_FILTER = (source, start, end, dest, dstart, dend) -> {
        for (int index = start; index < end; index++) {

            int type = Character.getType(source.charAt(index));

            if (type == Character.SURROGATE) {
                return "";
            }
        }
        return null;
    };


}
