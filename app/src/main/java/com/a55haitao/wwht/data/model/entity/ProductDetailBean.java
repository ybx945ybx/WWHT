package com.a55haitao.wwht.data.model.entity;

import android.text.TextUtils;

import com.a55haitao.wwht.utils.PriceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Haotao_Fujie on 16/10/10.
 */

public class ProductDetailBean {

    // 商品封面（单张）
    public String coverImgUrl;

    // 品牌名
    // public String brand;

    // 商品封面（多张）
    public ArrayList<String> coverImgList;

    // 海外直购、官网正品等字样
    public ArrayList<String> tags;

    // 分类信息
    public ProductCateData category;

    // 商品的spuid
    public String spuid;

    // 默认的skuid
    public String defaultSkuid;

    // SkuId
    public String skuid;

    // 商城的外币价格
    public float mallPriceOrg;

    // 商城的人民币价格
    public float mallPrice;

    // 现价的外币价格
    public float realPriceOrg;

    // 现价的人民币价格
    public float realPrice;

    // 促销信息
    public PromotionCodeBean promotionCode;
    // Not Used
    public float             oldRealPriceOrg;
    public float             oldRealPrice;

    // 外币汇率
    public float exchangeRate;

    // 外币名称
    public String unit;

    // 类别id
    public int cateId;

    // brand 品牌信息
    public BrandBaseDesModel brandInfo;

    // seller 商家信息
    public SellerBean.SellerDetailBean sellerInfo;

    // name 商品名称
    public String name;

    // stock 是否有库存
    public int inStock;
    public int stock;

    // website 官网原址
    public String pageUrl;
    public int    pageId;

    // tax 税收
    public ProductTaxInfo taxInfo;

    // content
    public ProductContent content;

    // sku inofn SKU信息
    public ProductSkuinfoData skuInfo;

    // 是否收藏
    public boolean is_star;

    // 收藏人数
    public int star_count;

    // 税费信息
    public ProductDetailFees fees;

    // 关税弹出框信息
    public ProductDetailFeesNotice tariffNotice;

    // 编辑、编集语信息
    public ProductDetailEditor editor;

    // Query.问题
    public ArrayList<ProductDetailQuestion> faq;

    // 优惠券
    public ArrayList<ProductCoupon> available_coupons;

    // 分享信息
    public ShareModel share;

    // 是否更新
    public boolean isUpdate;

    // 是否单一规格
    public boolean _isSingleSpecAndValue;

    // 是否正在核价
    private boolean inProductRTing;

    public void setInProductRTing(boolean inProductRTing) {
        this.inProductRTing = inProductRTing;
    }

    public boolean getInProductRTing() {
        return this.inProductRTing;
    }

    public ArrayList<TabBannerBean> generateTabBannerBean() {

        if (skuInfo.style != null && skuInfo.style.skustylelist != null && skuInfo.style.skustylelist.size() > 0) {
            ArrayList<TabBannerBean> list          = new ArrayList<>();
            StyleListData            styleListData = skuInfo.style.skustylelist.get(0);
            for (String s : styleListData.imgList) {
                TabBannerBean bean = new TabBannerBean();
                bean.image = s;
                list.add(bean);
            }
            return list;
        } else {
            if (coverImgList != null && coverImgList.size() > 0) {
                ArrayList<TabBannerBean> list = new ArrayList<>();
                for (String s : coverImgList) {
                    TabBannerBean bean = new TabBannerBean();
                    bean.image = s;
                    list.add(bean);
                }
                return list;
            }
        }
        return null;
    }

    public String getNameFormatWithBrandName() {
        if (brandInfo != null && !TextUtils.isEmpty(brandInfo.nameen) && !name.contains(brandInfo.nameen)) {
            return brandInfo.nameen + " " + name;
        }
        return name;
    }

    // 官网运费
    public String displayDomesticShippingFee() {

        if (fees.shopShipping <= 0) {
            return "免运费";
        } else {
            if (fees.shopNoShipping > 0) {
                if (realPrice > fees.shopNoShipping) {
                    return "免运费";
                } else {
                    return PriceUtils.toRMBFromFen(fees.shopShipping) + "（购物满" + PriceUtils.toRMBFromFen(fees.shopNoShipping) + "免官网运费）";
                }

            } else {
                return PriceUtils.toRMBFromFen(fees.shopShipping);
            }
        }
    }

    // 预估国际运费
    public String displayInternatialShippingFee() {
        return PriceUtils.toRMBFromFen(fees.internShipping);
    }

    // 消费税
    public String displayConsumptionFee() {
        return PriceUtils.toRMBFromFen(fees.consume, "免消费税");
    }

    // 关税
    public String displayTariffFee() {
        if (fees.tariff <= 0) {
            if (fees.tariff == 0) {
                return "免关税";
            } else {
                return "商品价格包含关税";
            }

        } else {
            return PriceUtils.toRMBFromFen(fees.tariff);
        }
    }

    public boolean isSoldOut() {
        if (inStock == 0) {
            return true;
        } else if (inStock == 2) {
            if (stock == 0) {
                return true;
            }
        }
        return false;
    }

    public class ProductCateData {

        String cate1;
        String cate2;
        String cate3;

        public String getCategoryInfo() {
            String result = "";

            if (!TextUtils.isEmpty(cate1)) {
                result += cate1;
            }
            if (!TextUtils.isEmpty(cate2)) {
                result += cate2;
            }
            if (!TextUtils.isEmpty(cate3)) {
                result += cate3;
            }
            return result;
        }
    }

    public class BrandBase {
        public int    brand_id;
        public String name;

        public String logo1;
        public String logo2;
        public String logo3;
        public String img_cover;

        public String pdb_logo;//原始logo，如果要用需要适配

        public String getLogoUrl() {
            if (!TextUtils.isEmpty(logo1)) {
                return logo1;
            } else if (!TextUtils.isEmpty(pdb_logo)) {
                return pdb_logo;
            }
            return "";
        }

        public QueryBean query;
    }

    public class BrandBaseDesModel extends BrandBase {

        public String  desc;
        public String  namecn;
        public String  nameen;
        public String  country;
        public boolean is_followed;

        public String getBrandName() {
            if (!TextUtils.isEmpty(nameen)) {
                return nameen;
            } else if (!TextUtils.isEmpty(namecn)) {
                return namecn;
            } else {
                return name;
            }
        }

        public String getBrandWholeName() {
            String name = null;
            if (!TextUtils.isEmpty(nameen) && !TextUtils.isEmpty(namecn)) {// 都不空

                if (namecn.equals(nameen)) {
                    name = nameen;
                } else {
                    name = nameen + "/" + namecn;
                }

            } else if (!TextUtils.isEmpty(nameen)) { // 中文名空, 则取英文名
                name = nameen;

            } else if (!TextUtils.isEmpty(namecn)) { // 英文名空, 则取中文名
                name = namecn;

            }

            if (name != null) {
                return name;
            } else {
                if (this.name != null) {
                    return this.name;
                }
                return "";
            }
        }
    }

    public class ProductTaxInfo {
        public float  tariffRate;
        public float  packageSize;
        public String comsumerTax;
        public String weight;
        public float  shipMoney;
        public float  RealTariffRate;
    }

    public class ProductContent {
        public String detail;
        public String desc;

        public String getProductDesc() {
            return desc + "\r\n" + detail;
        }
    }

    public class ProductSkuinfoData {
        public ProductStyleData      style;
        public ArrayList<SelectData> select;
    }

    public class ProductStyleData {
        public String                   name;
        public ArrayList<StyleListData> skustylelist;

        public ArrayList<TabBannerBean> generateTabBannerBean(int index) {
            ArrayList<TabBannerBean> list = new ArrayList<>();

            StyleListData data = skustylelist.get(index);
            for (String s : data.imgList) {
                TabBannerBean bean = new TabBannerBean();
                bean.image = s;
                list.add(bean);
            }
            return list;
        }

        public String getStyleValue(int index) {
            StyleListData data = skustylelist.get(index);
            return data.value;
        }
    }

    public class StyleListData {
        public ArrayList<String> imgList;
        public String            value;
        public String            imgCover;
    }

    public class SelectData {

        public String                       style;
        public String                       skuid;
        public ArrayList<SkuSelectListData> skuselectlist;
        public int                          inStock;
        public int                          stock;
        public float                        mallPriceOrg;
        public float                        realPriceOrg;
        public float                        mallPrice;
        public float                        realPrice;

        public boolean isSoldOut() {
            if (inStock == 0) {
                return true;
            } else if (inStock == 2) {
                if (stock == 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public class SkuSelectListData {

        public String value;
        public String select;

    }

    public class ProductDetailFees {
        public float internShipping;                   // 预估国际运费
        public float tariff;                           // 关税
        public float consume;                          // 消费税
        public float shopShipping;                     // 官网运费
        public float shopNoShipping;                   // 官网满多少元, 免运费
    }

    public class ProductDetailEditor {
        public String editor_id;
        public String head_img;
        public String name;
        public String text;
    }

    public class ProductDetailQuestion {
        public String title;
        public String content;
    }

    public class ProductCoupon {
        public int    coupon_id;
        public int    amount;
        public String name;
        public String subname;
        public String desc;
        public String image;

        public long start_time2;
        public long end_time2;

        public boolean got;                                 // 是否领取
    }

    public class ProductDetailFeesNotice {
        public String desc;
        public String notice;
        public String url;
    }

    public class PromotionCodeBean {
        public String content;
        public long   start;
        public long   end;
        public String title;
    }

    // 以下弹出选择规格弹窗用
    /**
     * eg: 传入参数specs: [{ "specs":["Color", "Size"], "values":["Black","5"], YES, "11111111"}, { "specs":["Color", "Size"], "values":["Black","6"], YES, "22222222"}]
     */
    public ArrayList<ProductDetailBean.AssistSpecItem> specs;
    /**
     * {A:[a1,a2], B:[b1,b2,b3]}
     */
    public HashMap<String, ArrayList<String>> cache_allSpecs;

    /**
     * [A:"", B:b1] , ""代表没有选择
     */
    public HashMap<String, String> cache_selectedSpecs;

    public static class AssistSpecItem {
        public ArrayList<String> specs;
        public ArrayList<String> values;
        public int               stock;
        public String            skuid;
    }

    public static class AvailableSpectItemValue {
        public String          spec;
        public HashSet<String> availableValues;

        public AvailableSpectItemValue() {
            availableValues = new HashSet<String>();
        }

        @Override
        public String toString() {

            String ret = spec;

            for (String value : availableValues) {
                ret += "[" + value + "]";
            }

            return spec;

        }
    }
}
