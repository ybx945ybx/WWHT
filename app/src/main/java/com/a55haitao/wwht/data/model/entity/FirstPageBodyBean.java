package com.a55haitao.wwht.data.model.entity;

import android.text.TextUtils;

import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.model.annotation.FirstPageViewType;
import com.a55haitao.wwht.data.model.result.CollectSpecialsResult;
import com.a55haitao.wwht.data.model.result.DailyProductResult;
import com.a55haitao.wwht.data.model.result.NewsFlashResult;
import com.a55haitao.wwht.data.model.result.EasyOptListResult;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by drolmen on 2016/12/15.
 */

public class FirstPageBodyBean implements Comparable {

    private String uri;

    private int priority;

    private
    @FirstPageViewType int viewType = FirstPageViewType.Invalid;

    private ReqBean req;

    private Object mData;

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof FirstPageBodyBean)) {
            return 0;
        }
        FirstPageBodyBean bean = (FirstPageBodyBean) o;
        return priority - bean.priority;
    }

    public void parse(JSONObject jsonObject) throws JSONException, JsonSyntaxException {
        uri = jsonObject.getString("uri");
        priority = jsonObject.getInt("priority");
        Gson gson = new Gson();
        req = gson.fromJson(jsonObject.optJSONObject("req").toString(), ReqBean.class);

        JSONObject body = jsonObject.optJSONObject("body");
        if (body == null) {
            return;
        }

        String data = body.optString("data");
        if (TextUtils.isEmpty(data)) {
            return;
        }

        if (uriEquals(ApiUrl.MT_HOME_HOT_BANNER)) {        // 首页Banner

            mData = gson.fromJson(data, new TypeToken<ArrayList<TabBannerBean>>() {
            }.getType());
            ArrayList<TabBannerBean> listBanner = ((ArrayList<TabBannerBean>)mData);
            if(listBanner != null && listBanner.size() > 0){
                viewType = FirstPageViewType.Banner;
            }
        } else if (uriEquals(ApiUrl.MT_HOME_TAB_BANNER)) {                              // 频道页banner
            viewType = FirstPageViewType.Banner;
            mData = gson.fromJson(data, new TypeToken<ArrayList<TabBannerBean>>() {
            }.getType());
        } else if (uriEquals(ApiUrl.MT_HOME_HOT_ADS)) {                                 // 广告位
            mData = gson.fromJson(data, new TypeToken<ArrayList<HotAdBean>>() {
            }.getType());
            ArrayList<HotAdBean> listAds = ((ArrayList<HotAdBean>)mData);
            if(listAds != null && listAds.size() > 0){
                viewType = FirstPageViewType.Ads;
            }
        } else if (uriEquals(ApiUrl.MT_HOME_MALL_STATEMENT)) {                          //四个icon
            mData = gson.fromJson(data, new TypeToken<ArrayList<MallStatementBean>>() {
            }.getType());
            ArrayList<MallStatementBean> listStat = ((ArrayList<MallStatementBean>)mData);
            if(listStat != null && listStat.size() > 0){
                viewType = FirstPageViewType.MallStatement;
            }
        }
        else if (uriEquals(ApiUrl.MT_HOME_HOT_PRODUCTS)) {                            //Product,hot页面是限时抢购
            mData = gson.fromJson(data, new TypeToken<ArrayList<ProductBaseBean>>() {
            }.getType());
            ArrayList<ProductBaseBean> listProduct = ((ArrayList<ProductBaseBean>)mData);
            if(listProduct != null && listProduct.size() > 0){
                viewType = FirstPageViewType.FlashSale;
            }
        }
        else if (uriEquals(ApiUrl.MT_HOME_TAB_PRODUCTS)) {                             //Product,tab页面是底部双列商品
            mData = gson.fromJson(data, new TypeToken<ArrayList<ProductBaseBean>>() {
            }.getType());
            ArrayList<ProductBaseBean> listProduct = ((ArrayList<ProductBaseBean>)mData);
            if(listProduct != null && listProduct.size() > 0){
                viewType = FirstPageViewType.Product_Twice;
            }
        } else if (uriEquals(ApiUrl.MT_HOME_TAB_BRANDS)) {                              //品牌
            mData = gson.fromJson(data, new TypeToken<ArrayList<SellerBean.SellerBaseBean>>() {
            }.getType());
            ArrayList<SellerBean.SellerBaseBean> listProduct = ((ArrayList<SellerBean.SellerBaseBean>)mData);
            if(listProduct != null && listProduct.size() > 0){
                viewType = FirstPageViewType.Recycler_Brand;
            }
        } else if (uriEquals(ApiUrl.MT_HOME_TAB_CATEGORIES)) {                          //分类
            ArrayList<CategoryBean> list = gson.fromJson(data, new TypeToken<ArrayList<CategoryBean>>() {
            }.getType());

            if(list != null && list.size() > 0){
                viewType = FirstPageViewType.Category;
                //分类数据做处理
                if (HaiUtils.getSize(list) > 6) {
                    mData = new ArrayList<>(list.subList(0, 6));
                } else {
                    mData = list;
                }
            }

        } else if (uriEquals(ApiUrl.MT_HOME_TAB_FAVORABLE_V2)) {                               //tab官网特卖,数据结构与热门页一样，但是全部取出只单列
            TabFavorableBean tabFavorableBean = gson.fromJson(data, TabFavorableBean.class);
            if(tabFavorableBean.favorables != null && tabFavorableBean.favorables.size() > 0) {
                for (TabFavorableBean.SpecialsBean special : tabFavorableBean.favorables) {
                    for (HotFavorableBean.SpecialsBean.SpecialItem item : special.special_items) {

                        item.small_icon = tabFavorableBean.small_icon;
                    }
                }
                mData = tabFavorableBean;
                viewType = FirstPageViewType.Image_Flavor;
            }
        }else if (uriEquals(ApiUrl.MT_HOME_HOT_FAVORABLE_V2)) {                                //热门官网特卖,单双列自由配置0单列1双列
            HotFavorableBean hotFavorableBean = gson.fromJson(data, HotFavorableBean.class);
            if(hotFavorableBean.favorables != null && hotFavorableBean.favorables.size() > 0) {
                for (HotFavorableBean.SpecialsBean special : hotFavorableBean.favorables) {
                    for (HotFavorableBean.SpecialsBean.SpecialItem item : special.special_items) {

                        item.small_icon = hotFavorableBean.small_icon;
                    }
                }
                mData = hotFavorableBean;
                viewType = FirstPageViewType.Image_Flavor_Hot;
            }
        }else if (uriEquals(ApiUrl.MT_HOME_HOT_ENTRIES, ApiUrl.MT_HOME_TAB_ENTRIES)) {         //潮流风尚  (精选合集)
            TabEntryBean bean = gson.fromJson(data, TabEntryBean.class);
            //潮流风尚数据做处理
            if (HaiUtils.getSize(bean.entries) > 0) {
                for (TabEntryBean.EntriesBean entriesBean : bean.entries) {
                    entriesBean.small_icon = bean.small_icon;
                    if (HaiUtils.getSize(entriesBean.items) > 12) {
                        entriesBean.items = new ArrayList<>(entriesBean.items.subList(0, 12));
                        entriesBean.items.get(11).showSeeAll = true;
                    }
                }
                viewType = FirstPageViewType.Image_Entry;
            }
            mData = bean;

        }
        else if (uriEquals(ApiUrl.MT_HOME_HOT_EASYOPT)) {                               // 推荐草单
            mData = new Gson().fromJson(data, EasyOptListResult.class);
            if(HaiUtils.getSize(((EasyOptListResult)mData).easyopts) > 0){
                viewType = FirstPageViewType.Easy;
            }

        }
        else if (uriEquals(ApiUrl.MT_HOME_HOT_NEWSFLASH)) {                              // 优惠快报
            mData = new Gson().fromJson(data, NewsFlashResult.class);
            if(HaiUtils.getSize(((NewsFlashResult)mData).letters) > 0){
                viewType = FirstPageViewType.NewsFlash;
            }
        }
        else if (uriEquals(ApiUrl.MT_HOME_BETTER_PRODUCT)) {                              // 超值单品
            mData = new Gson().fromJson(data, DailyProductResult.class);
            if(HaiUtils.getSize(((DailyProductResult)mData).products) > 0){
                viewType = FirstPageViewType.Daily_Product;
            }
        }
        else if (uriEquals(ApiUrl.MT_HOME_TAB_COLLECT_SPECIALS)) {                              // 超值单品
            mData = new Gson().fromJson(data, CollectSpecialsResult.class);
            if(HaiUtils.getSize(((CollectSpecialsResult)mData).data) > 0){
                viewType = FirstPageViewType.Collect_Specials;
            }
        }

    }

    public boolean uriEquals(String... uris) {

        if (TextUtils.isEmpty(uri)) {
            return false;
        }
        boolean result = false;

        for (int i = 0; i < uris.length; i++) {
            if (uri.equals(uris[i])) {
                result = true;
                break;
            }
        }

        return result;
    }

    public
    @FirstPageViewType
    int getViewType() {
        return viewType;
    }

    public ReqBean getReq() {
        return req;
    }

    public Object getData() {
        return mData;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setmData(Object mData) {
        this.mData = mData;
    }
}
