package com.a55haitao.wwht.data.model.entity;

import android.text.TextUtils;

import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.SpecTranslateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haotao_Fujie on 2016/10/31.
 */

public class ShoppingCartBean {
    public String                       total_price;
    public ArrayList<CartListStoreData> data;

    public float getTotalActiveAndSelectedRealPrice() {
        if (data == null || data.size() == 0) return 0;
        float ret = 0;

        for (CartListStoreData cartListStoreData : data) {
            ret += cartListStoreData.getTotalActiveAndSelectedRealPrice();
        }
        return ret;
    }

    public int getSelectedProductedCount() {
        int ret = 0;
        for (CartListStoreData cartListStoreData : data) {
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                if (cartdata.product.hasGroundingAndStock() && cartdata.is_select) {
                    ret += cartdata.count;
                }
            }
        }
        return ret;
    }

    public List getSelectedCartList() {
        List ret = new ArrayList<>();
        for (CartListStoreData cartListStoreData : data) {
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                if (cartdata.product.hasGroundingAndStock() && cartdata.is_select) {
                    ret.add(cartdata.cart_id);
                }
            }
        }
        return ret;
    }

    public String getSelectedSkuid() {
        String skuid = "";
        for (CartListStoreData cartListStoreData : data) {
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                if (cartdata.product.hasGroundingAndStock() && cartdata.is_select) {
                    if (TextUtils.isEmpty(skuid)){
                        skuid = cartdata.product.skuid;
                    }else {
                        skuid = skuid + "," + cartdata.product.skuid;
                    }
                }
            }
        }
        return skuid;
    }

    public ArrayList<CartInfoBean> getSelectedCartInfo() {
        ArrayList<CartInfoBean> cartInfos = new ArrayList<>();
        for (CartListStoreData cartListStoreData : data) {
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                if (cartdata.product.hasGroundingAndStock() && cartdata.is_select) {
                    CartInfoBean cartInfoBean = new CartInfoBean();
                    cartInfoBean.cart_id = cartdata.cart_id + "";
                    cartInfoBean.spuid = cartdata.product.spuid + "";
                    cartInfoBean.skuid = cartdata.product.skuid + "";
                    cartInfos.add(cartInfoBean);

                }
            }
        }
        return cartInfos;
    }

    /**
     * 全部选定
     *
     * @param selected
     */
    public void selectAll(boolean selected) {
        for (CartListStoreData cartListStoreData : data) {
            cartListStoreData.setIs_select(selected);
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                cartdata.setIs_select(selected);
            }
        }
    }

    /**
     * 该商家下全部选定
     *
     * @param store_id
     * @param selected
     */
    public void selectAll(String store_id, boolean selected) {
        for (CartListStoreData cartListStoreData : data) {
            if (store_id.equals(String.valueOf(cartListStoreData.store_id))) {
                cartListStoreData.setIs_select(selected);
                for (Cartdata cartdata : cartListStoreData.cart_list) {
                    cartdata.setIs_select(selected);
                }
                break;
            }
        }
    }

    /**
     * 选定单个商品
     *
     * @param cartId
     * @param selected
     */
    public void selectCartId(String cartId, boolean selected) {
        for (CartListStoreData cartListStoreData : data) {
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                int cartId_int = Integer.parseInt(cartId);
                if (cartId_int == cartdata.cart_id) {
                    cartdata.setIs_select(selected);
                    if (selected == true) {
                        // 判断是否该商户下的商品全部已经选择？
                        cartListStoreData.setIs_select(cartListStoreData.getSellerProductsSelected());
                    } else {
                        cartListStoreData.setIs_select(false);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 判断购物车内商品是否全部选中
     *
     * @return
     */
    public boolean getAllSelected() {
        for (CartListStoreData cartListStoreData : data) {
            if (cartListStoreData.getSellerProductsSelected() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 全部失效商品的CartIds
     *
     * @return
     */
    public String getAllInactiveCartIds() {
        String cartIds = "";
        for (CartListStoreData cartListStoreData : data) {
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                if (!cartdata.product.hasGroundingAndStock()) {
                    if (TextUtils.isEmpty(cartIds)) {
                        cartIds += cartdata.cart_id;
                    } else {
                        cartIds += "," + cartdata.cart_id;
                    }
                }
            }
        }
        return cartIds;
    }

    /**
     * 全部勾选的CartIds
     *
     * @return
     */
    public String getAllSelectedCartIds() {
        String cartIds = "";
        for (CartListStoreData cartListStoreData : data) {
            for (Cartdata cartdata : cartListStoreData.cart_list) {
                if (cartdata.product.hasGroundingAndStock() && cartdata.is_select == true) {
                    if (TextUtils.isEmpty(cartIds)) {
                        cartIds += cartdata.cart_id;
                    } else {
                        cartIds += "," + cartdata.cart_id;
                    }
                }
            }
        }
        return cartIds;
    }

    public String getSellerName(String store_id) {
        for (CartListStoreData cartListStoreData : data) {
            if (cartListStoreData.store_id == Integer.parseInt(store_id)) {
                if (!TextUtils.isEmpty(cartListStoreData.store_name_en)) {
                    return cartListStoreData.store_name_en;
                } else if (!TextUtils.isEmpty(cartListStoreData.store_name_cn)) {
                    return cartListStoreData.store_name_cn;
                }
            }
        }
        return "";
    }

    /**
     * 商家 -> (商家ID、商家名称、商家Logo、商家所在国家、商家下的商品s)
     */
    public class CartListStoreData {

        // 商家ID
        public int                 store_id;
        // 商家名称
        public String              store_name_en;
        public String              store_name_cn;
        // 商家Logo
        public String              store_logo;
        // 商家所在国家
        public CountryData         country;
        // 商家下的商品s
        public ArrayList<Cartdata> cart_list;
        // 满减金额
        public int                 product_fee_amount;
        // 邮寄费用
        public String              store_shipping_fee;
        //是否选中，默认为true
        public boolean             is_select;

        public void setIs_select(boolean is_select) {
            this.is_select = is_select;
        }

        // 获取商家下的所有商品是否全部选择
        public boolean getSellerProductsSelected() {
            for (Cartdata cartdata : cart_list) {
                if (cartdata.is_select == false) {
                    return false;
                }
            }
            return true;
        }

        public boolean getSellerProductsSelectedIgnoreInavtice() {
            int inActiveCount = 0;
            for (Cartdata cartdata : cart_list) {

                // 只要有一个在售商品，未勾选
                if (cartdata.is_select == false && cartdata.product.hasGroundingAndStock()) {
                    return false;
                }

                if (!cartdata.product.hasGroundingAndStock()) {
                    inActiveCount++;
                }
            }

            if (inActiveCount == cart_list.size()) {
                // 全部都是无效商品
                return false;
            }
            return true;
        }

        // 获取商家下的所有商品价格总和
        public float getTotalRealPrice() {

            if (cart_list == null || cart_list.size() == 0) return 0;

            float ret = 0;
            for (Cartdata cartdata : cart_list) {
                if (cartdata.product != null) {
                    ret += cartdata.product.helper_getRealPrice();
                }
            }
            return ret;
        }

        // 获取商家下的有效商品价格总和
        public float getTotalActiveRealPrice() {

            if (cart_list == null || cart_list.size() == 0) return 0;

            float ret = 0;
            for (Cartdata cartdata : cart_list) {

                if (!cartdata.product.hasGroundingAndStock()) {
                    // 无效、下架商品
                    continue;
                }
                if (cartdata.product != null) {
                    ret += cartdata.product.helper_getRealPrice();
                }
            }
            return ret;
        }

        public float getTotalActiveAndSelectedRealPrice() {
            if (cart_list == null || cart_list.size() == 0) return 0;

            float ret = 0;
            for (Cartdata cartdata : cart_list) {

                if (!cartdata.product.hasGroundingAndStock() || cartdata.is_select == false) {
                    // 无效、下架商品
                    continue;
                }
                if (cartdata.product != null) {
                    ret += cartdata.product.helper_getRealPrice();
                }
            }
            return ret;
        }

        // 获取商家名称
        public String getStoreName() {
            if (!TextUtils.isEmpty(store_name_en)) {
                if (store_name_en.contains("官网发货")) {
                    return store_name_en;
                } else {
                    return store_name_en + "官网发货";
                }

            } else if (!TextUtils.isEmpty(store_name_cn)) {
                if (store_name_cn.contains("官网发货")) {
                    return store_name_cn;
                } else {
                    return store_name_cn + "官网发货";
                }
            }
            return "";
        }

        // 满减标题
        public String getStoreFullCutoffTitle() {
            return "满" + ApiUrl.RMB_UNICODE + product_fee_amount + "免官网运费 ";
        }

        // 商家是否有满减活动
        public boolean getStoreFullCutoff() {
            return product_fee_amount > 0;
        }

        // 还差多少可以达到官网运费满减活动
        public int getStoreFullCutoffRemaing() {
            float                   currentTotalPrice = 0;
            ArrayList<String>       fids              = new ArrayList<>();  // 所有的活动
            HashMap<String, String> fidsCount         = new HashMap<>();

            for (Cartdata cartdata : cart_list) {
                if (cartdata.fullMinus != null) {
                    if (!fids.contains(cartdata.fullMinus.fid)) {
                        fids.add(cartdata.fullMinus.fid);
                    }
                } else {
                    if (!fids.contains("-1")) {
                        fids.add("-1");
                    }
                }
            }

            for (int i = 0; i < HaiUtils.getSize(fids); i++) {
                fidsCount.put(fids.get(i), "0");    //  0是还没计算1是计算了
            }

            for (Cartdata cartdata : cart_list) {
                if (cartdata.fullMinus != null) {

                    boolean meet         = false;
                    int     meetPosition = -1;

                    float  minCutoff = cartdata.fullMinus.cut_list.get(0).full;

                    float  maxCutoff = 0;

                    switch (cartdata.fullMinus.rtype) {  // 1 满钱满减, 2 满钱打折，3 满件满减, 4 满件打折
                        case 1:
                        case 2:
                            float activitiesCountAmount = getActivitiesCountAmount(cart_list, cartdata.fullMinus.fid);

                            for (int i = 0; i < HaiUtils.getSize(cartdata.fullMinus.cut_list); i++) {
                                ShoppingCartBean.CutData cutdata = cartdata.fullMinus.cut_list.get(i);

                                if (minCutoff > cutdata.full) {
                                    minCutoff = cutdata.full;
                                }

                                if (activitiesCountAmount >= cutdata.full) {
                                    if (i == 0) {
                                        maxCutoff = cutdata.full;
                                        meetPosition = 0;
                                    } else {
                                        if (maxCutoff < cutdata.full) {
                                            maxCutoff = cutdata.full;
                                            meetPosition = i;
                                        }
                                    }

                                    meet = true;
                                }

                            }

                            if (!meet) {
                                if (fidsCount.get(cartdata.fullMinus.fid).equals("0")) {
                                    fidsCount.put(cartdata.fullMinus.fid, "1");
                                    currentTotalPrice += activitiesCountAmount;
                                }
                            } else {
                                if (fidsCount.get(cartdata.fullMinus.fid).equals("0")) {
                                    fidsCount.put(cartdata.fullMinus.fid, "1");
                                    if (cartdata.fullMinus.rtype == 1) {
                                        currentTotalPrice += (activitiesCountAmount - cartdata.fullMinus.cut_list.get(meetPosition).cut);

                                    } else {
                                        currentTotalPrice += (activitiesCountAmount * cartdata.fullMinus.cut_list.get(meetPosition).cut / 100);

                                    }
                                }
                            }

                            break;
                        case 3:
                        case 4:
                            int activitiesProductCount = getActivitiesProductCount(cart_list, cartdata.fullMinus.fid);
                            float activitiesAmount = getActivitiesCountAmount(cart_list, cartdata.fullMinus.fid);

                            for (int i = 0; i < HaiUtils.getSize(cartdata.fullMinus.cut_list); i++) {
                                ShoppingCartBean.CutData cutdata = cartdata.fullMinus.cut_list.get(i);

                                if (minCutoff > cutdata.full) {
                                    minCutoff = cutdata.full;
                                }

                                if (activitiesProductCount >= cutdata.full) {
                                    if (cutdata.equals(cartdata.fullMinus.cut_list.get(0))) {
                                        maxCutoff = cutdata.full;
                                        meetPosition = 0;
                                    } else {
                                        if (maxCutoff < cutdata.full) {
                                            maxCutoff = cutdata.full;
                                            meetPosition = i;
                                        }
                                    }
                                    meet = true;
                                }

                            }

                            if (!meet) {
                                if (fidsCount.get(cartdata.fullMinus.fid).equals("0")) {
                                    fidsCount.put(cartdata.fullMinus.fid, "1");
                                    currentTotalPrice += activitiesAmount;
                                }
                            } else {
                                if (fidsCount.get(cartdata.fullMinus.fid).equals("0")) {
                                    fidsCount.put(cartdata.fullMinus.fid, "1");
                                    if (cartdata.fullMinus.rtype == 3) {
                                        currentTotalPrice += (activitiesAmount - cartdata.fullMinus.cut_list.get(meetPosition).cut);

                                    } else {
                                        currentTotalPrice += (activitiesAmount * cartdata.fullMinus.cut_list.get(meetPosition).cut / 100);

                                    }
                                }
                            }

                            break;
                    }
                } else {
                    if (cartdata.is_select)
                        currentTotalPrice += cartdata.product.helper_getRealPrice() * cartdata.count;
                }
            }

            if (currentTotalPrice >= product_fee_amount) {
                return 0;
            } else {
                return (int) (product_fee_amount - currentTotalPrice);
            }
        }

        // 获取相关活动的已选商品的总金额
        private float getActivitiesCountAmount(ArrayList<ShoppingCartBean.Cartdata> cartdatas, String fid) {
            float activitiesCountAmount = 0;
            for (ShoppingCartBean.Cartdata cartdata : cartdatas) {
                if (cartdata.fullMinus != null && cartdata.fullMinus.fid.equals(fid) && cartdata.is_select) {
                    activitiesCountAmount += cartdata.product.helper_getRealPrice() * cartdata.count;
                }
            }
            return activitiesCountAmount;
        }

        // 获取相关活动的已选商品的总件数
        private int getActivitiesProductCount(ArrayList<ShoppingCartBean.Cartdata> cartdatas, String fid) {
            int activitiesProductCount = 0;
            for (ShoppingCartBean.Cartdata cartdata : cartdatas) {
                if (cartdata.fullMinus != null && cartdata.fullMinus.fid.equals(fid) && cartdata.is_select) {
                    activitiesProductCount += cartdata.count;
                }
            }
            return activitiesProductCount;
        }
    }

    public class CountryData {
        public String flag;
        public String regionName;
        public String regionImgUrl;
        public int    regionCode;
        public int    regionId;
    }

    public class FullMinusData {
        public String             promotion_msg;   // 促销语
        public float              activitiesCountAmount;
        public float              activitiesProductCount;
        public int                ltype;       // 活动类型 0 单品， 1商家
        public int                rtype;       // 满减类型 1 满钱减钱， 2 满钱打折， 3 满件减钱， 4 满件打折
        public String             title;
        public String             code;
        public ArrayList<CutData> cut_list;
        public String             fid;

    }

    public class CutData {
        public float  cut;      // 减多少
        public float  full;     // 满多少
        public String id;
        public String fid;
        public String title;
    }

    /**
     * 购物车的商品 -> (商品对象、商品数量)
     */
    public class Cartdata {
        public int             media_id;
        public String          media_product_transurl;//媒体转换后的商品跟踪链接
        public CartProductdata product;
        public int             count;
        public long            ctime;
        public int             cart_id;
        // 活动满减信息
        public FullMinusData   fullMinus;
        public boolean         showfullMinusTitle;
        // Added By Client, 商品是否选择
        public boolean         is_select;

        public void setIs_select(boolean is_select) {
            this.is_select = is_select;
        }

        // 获取商家下的商品是否全部选择
        public boolean getProductSelected() {
            if (is_select == true) {
                // 勾选
                return true;
            } else if (!product.hasGroundingAndStock()) {
                // 商品无效
                return true;
            } else {
                // 其它
                return false;
            }
        }

    }

    /**
     * 商品对象 -> (spuid、skuid、规格、图片、品牌、价格、库存)
     */
    public class CartProductdata {
        public String                          unit;
        public String                          skuid;
        public String                          spuid;
        public String                          product_name;
        public String                          product_discount;
        public String                          coverImgUrl;
        public String                          unit_name;
        public String                          status;
        public CartBrandsData                  brand;
        public ArrayList<CartproductSelecData> select;
        public ArrayList<CartSkuData>          sku_list;
        public float                           realPrice;
        public float                           realPriceOrg;
        public int                             instock;
        public int                             stock;
        public int                             limitnum;
        public float                           mallPriceOrg;
        public int                             mallPrice;

        public String selectedSpecsDescription() {
            if (TextUtils.isEmpty(skuid)) {
                return "";
            }

            if (sku_list == null || sku_list.size() == 0)
                return "";

            for (CartSkuData cartSkuData : sku_list) {
                if (cartSkuData.skuid.equals(skuid)) {
                    String str = "";

                    for (SkuValueData skuValue : cartSkuData.skuValues) {

                        if (skuValue.equals(cartSkuData.skuValues.get(cartSkuData.skuValues.size() - 1))) {
                            str += SpecTranslateUtils.translateToChinese(skuValue.name) + ": " + skuValue.value;
                        } else {
                            str += SpecTranslateUtils.translateToChinese(skuValue.name) + ": " + skuValue.value + "\n";
                        }
                    }
                    return str;
                }
            }
            return "";
        }

        public void selectSku(String skuId, boolean selected) {
            for (CartListStoreData cartListStoreData : data) {
                for (Cartdata cartdata : cartListStoreData.cart_list) {
                    if (skuId.equals(cartdata.product.skuid)) {
                        cartdata.setIs_select(selected);
                        if (selected == true) {
                            // 判断是否该商户下的商品全部已经选择？
                            cartListStoreData.setIs_select(cartListStoreData.getSellerProductsSelected());
                        } else {
                            cartListStoreData.setIs_select(false);
                        }
                        break;
                    }
                }
            }
        }

        public boolean hasStock() {
            // inStock 0 没有库存， 1表示库存不明确， 2表示库存明确
            // stock 实际库存数
            // status 0 不可售  1 可售
            // return ([self.status intValue] == 1) && self.instock >= 1;
            return instock >= 1;
        }

        public boolean hasGroundingAndStock() {
            return (instock >= 1 && !status.equals("0"));
        }

        public float helper_getRealPrice() {
            if (TextUtils.isEmpty(skuid) || sku_list == null || sku_list.size() == 0) {
                return realPrice;
            }

            for (CartSkuData data : sku_list) {
                if (data.skuid.equals(skuid)) {
                    return PriceUtils.toFloat(data.realPrice);
                }
            }
            return realPrice;
        }

        public float helper_getMallPrice() {
            if (TextUtils.isEmpty(skuid) || sku_list == null || sku_list.size() == 0) {
                return mallPrice;
            }

            for (CartSkuData data : sku_list) {
                if (data.skuid.equals(skuid)) {
                    return PriceUtils.toFloat(data.mallPrice);
                }
            }
            return mallPrice;
        }
    }

    public class CartBrandsData {
        public int         brand_id;
        public CountryData country;
        public String      brand_name_cn;
        public String      brand_name_en;
        public String      brand_logo;

        public String getBrandName() {
            if (!TextUtils.isEmpty(brand_name_en)) {
                return brand_name_en;
            } else if (!TextUtils.isEmpty(brand_name_cn)) {
                return brand_name_cn;
            }
            return "";
        }
    }

    public class CartproductSelecData {
        public String    name;
        public ArrayList values;
    }

    public class CartSkuData {
        public String                  onsale;
        public String                  sku_discount;
        public String                  mallPrice;
        public String                  skuid;
        public String                  realPrice;
        public int                     inStock;
        public int                     stock;
        public boolean                 selected;
        public ArrayList<SkuValueData> skuValues;
    }

    public class SkuValueData {
        public String name;
        public String value;
    }

}
