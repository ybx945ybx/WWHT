package com.a55haitao.wwht.ui.fragment.myaccount.order;

import java.util.HashMap;

/**
 * 订单页 Fragment 工厂
 * <p>
 * Created by 陶声 on 16/7/22.
 */
public class OrderFragmentFactory {

    public static final int TYPE_COUNT = 5;

    private static HashMap<Integer, OrderBaseFragment> fragments = new HashMap<>();

    /**
     * 创建Fragment
     */
    public static OrderBaseFragment createFragment(int position) {

        OrderBaseFragment fragment = fragments.get(position);

        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment = new OrderAllFragment();
                    break;
                case 1:
                    fragment = new OrderUnPayFragment();
                    break;
                case 2:
                    fragment = new OrderUnDeliverFragment();
                    break;
                case 3:
                    fragment = new OrderUnReceiveFragment();
                    break;
                case 4:
                    fragment = new OrderCompleteFragment();
                    break;
                default:
                    break;
            }
            fragments.put(position, fragment);
        }
        return fragment;
    }

    /**
     * 获取Fragment类型
     */
    public static String getFragmentName(int type) {
        String result = null;
        switch (type) {
            case 0:
                result = "全部";
                break;
            case 1:
                result = "待付款";
                break;
            case 2:
                result = "待发货";
                break;
            case 3:
                result = "待收货";
                break;
            case 4:
                result = "已完成";
                break;
        }
        return result;
    }

    /**
     * 清除Fragment
     */
    public static void clearFragments() {
        if (fragments != null)
            fragments.clear();
    }
}
