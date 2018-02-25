package com.a55haitao.wwht.ui.fragment.myaccount.message;


import android.util.SparseArray;

import com.a55haitao.wwht.data.model.annotation.MessageFragmentType;

import static com.a55haitao.wwht.data.model.annotation.MessageFragmentType.MESSAGE;
import static com.a55haitao.wwht.data.model.annotation.MessageFragmentType.NOTIFICATION;

/**
 * 个人中心 - 消息 Fragment工厂
 * <p>
 * Created by 陶声 on 16/7/13.
 */
public class MessageFragmentFactory {

    public static final int TYPE_COUNT = 2;

    private static SparseArray<MessageBaseFragment> fragments = new SparseArray<>();

    /**
     * 创建Fragment
     */
    public static MessageBaseFragment createFragment(@MessageFragmentType int type) {
        MessageBaseFragment fragment = fragments.get(type);
        if (fragment == null) {
            switch (type) {
                case MESSAGE:
                    fragment = new MessageFragment();
                    break;
                case NOTIFICATION:
                    fragment = new NotificationFragment();
                    break;
                default:
                    break;
            }
            fragments.put(type, fragment);
        }
        return fragment;
    }

    /**
     * 获取Fragment名
     */
    public static String getFragmentName(@MessageFragmentType int type) {
        String result = "";
        switch (type) {
            case MESSAGE:
                result = "消息";
                break;
            case NOTIFICATION:
                result = "通知";
                break;
        }
        return result;
    }

    /**
     * 清除
     */
    public static void clearFragments() {
        if (fragments != null)
            fragments.clear();
    }
}
