package com.a55haitao.wwht.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.a55haitao.wwht.ui.activity.discover.CategorySpecialActivity;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.ui.activity.base.H5Activity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.easyopt.EasyOptDetailActivity;
import com.a55haitao.wwht.ui.activity.easyopt.RecommendEasyOptActivity;
import com.a55haitao.wwht.ui.activity.firstpage.EntriesSpecialActivity;
import com.a55haitao.wwht.ui.activity.firstpage.FavorableSpecialActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.activity.social.SocialSpecialActivity;
import com.a55haitao.wwht.ui.activity.social.TagDetailActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by 董猛 on 16/9/21.
 */

public class HaiUriMatchUtils {

    /**
     * 根据字符窜匹配跳转页面
     */
    public static void matchUri(Activity activity, String uri) {

        if (uri == null) {
            ToastUtils.showToast(activity, ApiUrl.ILLEGAL_SPECIAL_URI);
            return;
        }

        Logger.d(uri);
        String targetId = uri.substring(uri.lastIndexOf("/") + 1);

        if (uri.contains("http://") || uri.contains("https://")) {    // H5页面
            H5Activity.toThisActivity(activity, uri, null);
        } else if (uri.contains("ProductSpecial/")) {        // 商品专题
            EntriesSpecialActivity.toThisActivity(activity, targetId);
        } else if (uri.contains("FavorableSpecial/")) {      // 特卖专题
            FavorableSpecialActivity.toThisActivity(activity, targetId);
        } else if (uri.contains("PostSpecial/")) {           // 社区专题
            SocialSpecialActivity.toThisActivity(activity, Integer.valueOf(targetId));
        } else if (uri.contains("SearchSpecial/")) {         // 搜索专题（类目专题）
            CategorySpecialActivity.toThisActivity(activity, TextUtils.isEmpty(targetId) ? -1 : Integer.valueOf(targetId), PageType.SPECIAL);
        } else if (uri.contains("Product/")) {
            ProductMainActivity.toThisAcivity(activity, targetId);
        } else if (uri.contains("HotTag/")) {            //热门标签
            TagDetailActivity.toThisActivity(activity, Integer.valueOf(targetId), "", 1);
        } else if (uri.contains("SellerHome/")) {             //商家主页
            SiteActivity.toThisActivity(activity, targetId, PageType.SELLER);
        } else if (uri.contains("BrandHome/")) {              //品牌主页
            SiteActivity.toThisActivity(activity, targetId, PageType.BRAND);
        } else if (uri.contains("UserCenter/")) {             //用户详情
            OthersHomePageActivity.actionStart(activity, Integer.valueOf(targetId));
        } else if (uri.contains("AlbumAllList/")) {
            activity.startActivity(new Intent(activity, RecommendEasyOptActivity.class));
        } else if (uri.contains("AlbumDetail/")) {
            EasyOptDetailActivity.toThisActivity(activity, Integer.valueOf(targetId));
        } else {
            return;
        }
    }

    public static boolean isHaiTaoUri(String str) {
        return (!TextUtils.isEmpty(str)) && str.contains("55haitao");
    }

}
