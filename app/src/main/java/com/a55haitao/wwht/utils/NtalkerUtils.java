package com.a55haitao.wwht.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.baoyz.actionsheet.ActionSheet;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orhanobut.logger.Logger;

import cn.xiaoneng.coreapi.ChatParamsBody;
import cn.xiaoneng.uiapi.Ntalker;
import cn.xiaoneng.utils.CoreData;

/**
 * Created by a55 on 2017/4/28.
 * 小能客服
 */

public class NtalkerUtils {

    private static String settingid = "kf_9566_1493358830761";

    public static void loginNtalker(UserBean userBean) {
        if (userBean == null) {
            Logger.e("客服登录----->用户为空");
            return;
        }
        if (Ntalker.getInstance().login("zg_" + String.valueOf(userBean.getId()), "", 0) == 0) {
            Logger.d("客服登录成功");
        } else {
            Logger.d("客服登录失败");
        }
    }

    public static void logoutNtalker() {
        Ntalker.getInstance().logout();
    }

    public static void contactNtalker(Context context) {
        startNtalke(context);
    }

    /**
     * 跳转到客服页面
     *
     * @param context context
     */
    public static void contactKF(Context context, boolean showDialService) {
        if (showDialService) {
            ActionSheet.createBuilder(context, ((AppCompatActivity) context).getSupportFragmentManager())
                    .setCancelableOnTouchOutside(true)
                    .setSubButtonTitle("工作日09:30 - 21:30 双休日/节假日10:00 - 18:00")
                    .setCancelButtonTitle("取消")
                    .setOtherButtonTitles("在线客服", "电话客服 400-920-0572转1")
                    .setListener(new ActionSheet.ActionSheetListener() {
                        @Override
                        public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                        }

                        @Override
                        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                            if (index == 0) {
                                startNtalke(context);
                            } else {
                                // GA 事件
                                Tracker tracker = ((HaiApplication) ((AppCompatActivity) context).getApplication()).getDefaultTracker();
                                tracker.send(new HitBuilders.EventBuilder()
                                        .setCategory("联系客服")
                                        .setAction("电话客服")
                                        .build());

                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:400-920-0572"));
                                context.startActivity(intent);
                            }
                        }
                    })
                    .show();
        } else {
            startNtalke(context);
        }
    }

    /**
     * 小能客服带商品信息
     *
     * @param context context
     */
    public static void contactNtalkeWithProduct(Context context, String goodsId) {
        ChatParamsBody chatparams = new ChatParamsBody();

//        chatparams.startPageTitle = "女装/女士精品 - 服饰 - 搜索店铺 - ECMall演示站";
//        chatparams.startPageUrl = "http://img.meicicdn.com/p/51/050010/h-050010-1.jpg";

        chatparams.itemparams.clicktoshow_type = CoreData.CLICK_TO_APP_COMPONENT;   // 点击商品展示在app内打开，在监听中可以处理点击事件

        chatparams.itemparams.clientgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;

        chatparams.itemparams.itemparam = "1";              // 用于区分平台，1是直购app，2是返利app，。。。

        chatparams.itemparams.appgoodsinfo_type = CoreData.SHOW_GOODS_BY_ID;
        chatparams.itemparams.goods_id = goodsId;
//        chatparams.itemparams.goods_name = goodsName;
//        chatparams.itemparams.goods_price = goodsPrice;
//        chatparams.itemparams.goods_image = goodsImage;     //URL必须以"http://"开头
//        chatparams.itemparams.goods_url = goodsUri;         //URL必须以"http://"开头

        Ntalker.getInstance().startChat(context, settingid, "", "", "", chatparams);
    }

    /**
     * 小能客服
     *
     * @param context context
     */
    private static void startNtalke(Context context) {
        ChatParamsBody chatparams = new ChatParamsBody();
        Ntalker.getInstance().startChat(context, settingid, "", "", "", chatparams);
    }
}
