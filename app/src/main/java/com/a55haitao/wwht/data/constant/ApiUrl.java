package com.a55haitao.wwht.data.constant;

/**
 * Created by 董猛 on 2016/10/8.
 */

/**
 * 接口
 */
public class ApiUrl {
    // *** service names ***
    public static final String SERV_MAIN_UC_NS      = "55haitao_uc.";
    public static final String SERV_MAIN_SNS_NS     = "55haitao_sns.";
    public static final String SERV_MINISHOP_SNS_NS = "minishop_sns.";

    //获取开屏页广告图 55haitao_sns.HomeAPI/index_advert
    public static final String MT_HOME_AD = SERV_MAIN_SNS_NS + "HomeAPI/index_advert";

    //获取首页Tab
    public static final String MT_HOME_TABS                = SERV_MAIN_SNS_NS + "HomeAPI/home_tabs_v1";
    //获取官网特卖及精选合集页Tab
    public static final String MT_GET_TABS                 = SERV_MAIN_SNS_NS + "HomeAPI/get_tabs";
    //获取Banner
    public static final String BANNER_TAB                  = "HomeAPI/tab_banners";
    public static final String BANNER_HOT                  = "HomeAPI/hot_banners";
    public static final String MT_HOME_TAB_BANNER          = SERV_MAIN_SNS_NS + BANNER_TAB;
    public static final String MT_HOME_HOT_BANNER          = SERV_MAIN_SNS_NS + BANNER_HOT;
    //获取广告位
    public static final String ADS_HOT                     = "HomeAPI/hot_banners_ads";
    public static final String MT_HOME_HOT_ADS             = SERV_MAIN_SNS_NS + ADS_HOT;
    //获取Mall Hot Statements
    public static final String ICON_HOT                    = "HomeAPI/icons";
    public static final String MT_HOME_MALL_STATEMENT      = SERV_MAIN_SNS_NS + ICON_HOT;
    //官网特卖
    public static final String FAVORABLE_TAB               = "HomeAPI/tab_favorables";
    public static final String FAVORABLE_HOT               = "HomeAPI/hot_favorables";             // dev用v1
    public static final String FAVORABLE_HOT_V1            = "HomeAPI/hot_favorables_v1";          // dev用v1
    public static final String FAVORABLE_HOT_V2            = "HomeAPI/hot_favorables_v2";
    public static final String FAVORABLE_TAB_V2            = "HomeAPI/tab_favorables_v2";
    public static final String MT_HOME_TAB_FAVORABLE       = SERV_MAIN_SNS_NS + FAVORABLE_TAB;
    public static final String MT_HOME_HOT_FAVORABLE       = SERV_MAIN_SNS_NS + FAVORABLE_HOT;
    public static final String MT_HOME_HOT_FAVORABLE_V1    = SERV_MAIN_SNS_NS + FAVORABLE_HOT_V1;
    public static final String MT_HOME_HOT_FAVORABLE_V2    = SERV_MAIN_SNS_NS + FAVORABLE_HOT_V2;
    public static final String MT_HOME_TAB_FAVORABLE_V2    = SERV_MAIN_SNS_NS + FAVORABLE_TAB_V2;
    public static final String MT_HOME_ALL_FAVORABLE       = SERV_MAIN_SNS_NS + "HomeAPI/all_favorables";
    public static final String MT_HOME_ALL_FAVORABLE_TABID = SERV_MAIN_SNS_NS + "HomeAPI/get_tab_favorable_all";

    // 超值单品 55haitao_sns.HomeAPI/get_better_products"
    public static final String MT_HOME_BETTER_PRODUCT       = SERV_MAIN_SNS_NS + "HomeAPI/better_products";
    // 合集精选推荐   "55haitao_sns.HomeAPI/get_tab_collect_specials"
    public static final String MT_HOME_TAB_COLLECT_SPECIALS = SERV_MAIN_SNS_NS + "HomeAPI/get_tab_collect_specials";

    //热门品牌
    public static final String BRAND_TAB             = "HomeAPI/tab_brands";
    public static final String MT_HOME_TAB_BRANDS    = SERV_MAIN_SNS_NS + BRAND_TAB;
    //潮流风尚
    public static final String ENTRIES_TAB           = "HomeAPI/tab_entry_list";
    public static final String ENTRIES_HOT           = "HomeAPI/hot_entry_list";
    public static final String MT_HOME_TAB_ENTRIES   = SERV_MAIN_SNS_NS + ENTRIES_TAB;
    public static final String MT_HOME_HOT_ENTRIES   = SERV_MAIN_SNS_NS + ENTRIES_HOT;
    public static final String MT_HOME_ENTRIES_TABID = SERV_MAIN_SNS_NS + "HomeAPI/get_tab_entry_v2";

    //优惠快报
    public static final String NEWSFLASH_HOT                  = "OtherAPI/get_letters";
    public static final String MT_HOME_HOT_NEWSFLASH          = SERV_MAIN_SNS_NS + NEWSFLASH_HOT;
    // 优惠快报更新浏览次数 55haitao_sns.OtherAPI/update_letters_count
    public static final String NEWSFLASH_UPDATE_COUNT         = "OtherAPI/update_letters_count";
    public static final String MT_HOME_UPDATE_NEWSFLASH_COUNT = SERV_MAIN_SNS_NS + NEWSFLASH_UPDATE_COUNT;

    //草单推荐
    public static final String EASYOPT_HOT         = "SnsAPI/easyopt_list4tag";
    public static final String MT_HOME_HOT_EASYOPT = SERV_MAIN_SNS_NS + EASYOPT_HOT;

    //tab今日推荐
    public static final String PRODUCTS_TAB                 = "HomeAPI/tab_products";
    public static final String MT_HOME_TAB_PRODUCTS         = SERV_MAIN_SNS_NS + PRODUCTS_TAB;
    public static final String MT_HOME_TAB_PRODUCTS_V11     = MT_HOME_TAB_PRODUCTS + "_v11";
    // For hot, position 0 热卖单品 position 1 新品推荐
    public static final String PRODUCTS_HOT                 = "HomeAPI/hot_products";
    public static final String MT_HOME_HOT_PRODUCTS         = SERV_MAIN_SNS_NS + PRODUCTS_HOT;
    public static final String MT_HOME_HOT_PRODUCTS_V11     = MT_HOME_HOT_PRODUCTS + "_v11";
    //热门分类
    public static final String CATEGORY_TAB                 = "HomeAPI/tab_categories";
    public static final String MT_HOME_TAB_CATEGORIES       = SERV_MAIN_SNS_NS + CATEGORY_TAB;
    //全部分类
    public static final String MT_ALL_CATEGORIES            = SERV_MAIN_SNS_NS + "ProductAPI/get_whole_categories_v11";
    public static final String MT_ALL_BRANDS                = SERV_MAIN_SNS_NS + "ProductAPI/get_whole_brands_v11";
    public static final String MT_ALL_SELLERS               = SERV_MAIN_SNS_NS + "ProductAPI/get_whole_sellers_v11";
    // 获取首页的活动
    public static final String MT_HOME_ACTIVITY             = SERV_MAIN_SNS_NS + "HomeAPI/get_activity";
    // 商品详情
    public static final String MT_PRODUCT_DETAIL            = SERV_MAIN_SNS_NS + "ProductAPI/product_detail";
    // 可领取的优惠劵列表
    public static final String MT_PRODUCT_GRANTCOUPON       = SERV_MINISHOP_SNS_NS + "PassportAPI/grant_Couponq_list";
    // 获取 "大家都在买" 商品
    public static final String MT_PRODUCT_RECOMMEND         = SERV_MAIN_SNS_NS + "ProductAPI/product_recommand";
    // 推荐 商品
    public static final String MT_PRODUCT_RECOMMEND_V11     = SERV_MAIN_SNS_NS + "ProductAPI/get_recommend_product_v11";
    // 获取相似品牌
    public static final String MT_PRODUCT_SIMILAR_BRAND     = SERV_MAIN_SNS_NS + "ProductAPI/get_related_brand_v11";
    // 获取相似品牌
    public static final String MT_PRODUCT_TRANSLATE         = SERV_MAIN_SNS_NS + "ProductAPI/get_translate_v11";
    // 收藏商品, 加入商品心愿单
    public static final String MT_PRODUCT_LIKE              = SERV_MAIN_SNS_NS + "ProductAPI/star_product_v11";
    // 搜索商品
    public static final String MT_PRODUCT_SEARCH            = SERV_MAIN_SNS_NS + "ProductAPI/product_search";
    // 实时核价
    public static final String MT_PRODUCT_RT                = SERV_MAIN_SNS_NS + "ProductAPI/product_RT";
    // 领优惠券
    public static final String MT_PRODUCT_GET_COUPON        = SERV_MINISHOP_SNS_NS + "PassportAPI/receive_couponq";
    // 购物车列表
    public static final String MT_SHOPPINGCART_PUT          = SERV_MINISHOP_SNS_NS + "CartAPI/add_cart";
    // 购物车列表
    public static final String MT_SHOPPINGCART_LIST         = SERV_MINISHOP_SNS_NS + "CartAPI/cart_list";
    // 删除购物车列表
    public static final String MT_REMOVE_SHOPPINGCART_LIST  = SERV_MINISHOP_SNS_NS + "CartAPI/del_cart";
    // 获取购物车列表数量
    public static final String MT_GET_SHOPPINGCART_LIST_CNT = SERV_MINISHOP_SNS_NS + "CartAPI/cart_count";
    // 订单详情
    public static final String MT_ORDER_DETAIL              = SERV_MINISHOP_SNS_NS + "OrderAPI/order_detail_v3";
    //获取相关品牌
    public static final String MT_GET_RELATE_BRAND          = SERV_MAIN_SNS_NS + "ProductAPI/get_related_brand_v11";
    //获取相关商家
    public static final String MT_GET_RELATE_SELLER         = SERV_MAIN_SNS_NS + "ProductAPI/get_related_seller_v11";
    // 获取关注的品牌
    public static final String MT_GET_FOLLOW_BRAND          = SERV_MAIN_SNS_NS + "ProductAPI/get_follow_brand_v11";
    // 关注&&取消关注品牌
    public static final String MT_FOLLOW_BRAND              = SERV_MAIN_SNS_NS + "ProductAPI/follow_brand_v11";
    // 获取关注的商家
    public static final String MT_GET_FOLLOW_SELLER         = SERV_MAIN_SNS_NS + "ProductAPI/get_follow_seller_v11";
    // 关注&&取消关注商家
    public static final String MT_FOLLOW_SELLER             = SERV_MAIN_SNS_NS + "ProductAPI/follow_seller_v11";
    //获取分类页面数据
    public static final String MT_GET_CATEGORY              = SERV_MAIN_SNS_NS + "ProductAPI/get_hot_home_v11";
    // 喜欢/取消喜欢 商品专题
    public static final String MT_PRODUCT_SP_LIKE           = SERV_MAIN_SNS_NS + "SpecialAPI/like_product_special";
    // 商品专题star用户列表
    public static final String MT_PRODUCT_SP_ALL_LIKE       = SERV_MAIN_SNS_NS + "SpecialAPI/get_product_special_likes";
    //潮流风尚专题详情
    public static final String MT_ENTRY_SP_DETAIL           = SERV_MAIN_SNS_NS + "SpecialAPI/get_product_special_info";
    //专题底部推荐
    public static final String MT_RELATE_FAVORABLE          = SERV_MAIN_SNS_NS + "HomeAPI/get_ralate_favorable";
    //官网特卖专题详情
    public static final String MT_FAVORABLE_SP_DETAIL       = SERV_MAIN_SNS_NS + "SpecialAPI/get_favorable_special_info";
    //热门搜索词
    public static final String MT_HOT_SEARCH_WORDS          = SERV_MAIN_SNS_NS + "ProductAPI/get_hot_search_words_v11";
    //获取关联词
    public static final String MT_AUTO_FILL_WORDS           = SERV_MAIN_SNS_NS + "ProductAPI/get_autofill_v11";
    //收货地址
    public static final String MT_ADDRESS_LIST              = SERV_MINISHOP_SNS_NS + "PassportAPI/address_list";
    //收货地址详情
    public static final String MT_ADDRESS_DETAIL            = SERV_MINISHOP_SNS_NS + "PassportAPI/address_detail";
    //新增收货地址
    public static final String MT_ADDRESS_ADD               = SERV_MINISHOP_SNS_NS + "PassportAPI/add_address";
    //更新收货地址
    public static final String MT_ADDRESS_UPDATE            = SERV_MINISHOP_SNS_NS + "PassportAPI/update_address";
    //删除收货地址
    public static final String MT_ADDRESS_DELETE            = SERV_MINISHOP_SNS_NS + "PassportAPI/del_address";
    //获取默认收货地址
    public static final String MT_DEFAULT_ADDRESS           = SERV_MINISHOP_SNS_NS + "PassportAPI/get_default_address";
    //获取用户优惠券
    public static final String MT_COUPON_ALL                = SERV_MINISHOP_SNS_NS + "PassportAPI/getcouponqList";
    //兑换优惠券
    public static final String MT_COUPON_EXCHANGE           = SERV_MAIN_SNS_NS + "SnsAPI/receive_coupon_from_code";
    //订单准备
    public static final String MT_ORDER_PREPARE             = SERV_MINISHOP_SNS_NS + "OrderAPI/order_prepare";
    //订单提交
    public static final String MT_ORDER_COMMIT              = SERV_MINISHOP_SNS_NS + "OrderAPI/order_commit_v2";
    //订单取消
    public static final String MT_ORDER_CANCLE              = SERV_MINISHOP_SNS_NS + "OrderAPI/order_cancel";
    //确认收货
    public static final String MT_ORDER_RECIEVED            = SERV_MINISHOP_SNS_NS + "OrderAPI/order_confirm";
    //获取物流信息
    public static final String MT_TRACK_INFO                = SERV_MINISHOP_SNS_NS + "OrderAPI/getTrackInfo";
    //用户佣金
    public static final String MT_COMMISION                 = SERV_MAIN_SNS_NS + "SnsAPI/get_commission";
    // 获取分享积分
    public static final String MT_SHARE_MEMBERPOINT         = SERV_MAIN_SNS_NS + "SnsAPI/share_success";
    // 强制更新
    public static final String MT_ENFORCE_UPDATE            = SERV_MAIN_SNS_NS + "SnsAPI/latest_appver";

    public static final String MT_SELLER_INFO         = SERV_MAIN_SNS_NS + "ProductAPI/get_seller_info_v11";
    public static final String MT_BRAND_INFO          = SERV_MAIN_SNS_NS + "ProductAPI/get_brand_info_v11";
    // 获取搜索专题信息
    public static final String MT_SEARCH_SPECIAL_INFO = SERV_MAIN_SNS_NS + "SpecialAPI/get_search_special_info";  //55haitao_sns.SpecialAPI/get_search_special_info

    //UPY 转换根网址
    public static final String NORMALIZE_HOST = "http://st-prod.b0.upaiyun.com/prodimage/";

    //精选草单
    public static final String EO_SHOW_SELECT     = "55haitao_sns.SnsAPI/easyopt_select";
    //获取草单TAG列表
    public static final String EO_SHOW_TAGS       = "55haitao_sns.SnsAPI/easyopt_tag_list";
    //获取草单TAG对应的列表
    public static final String EO_SHOW_TAG_LISTS  = "55haitao_sns.SnsAPI/easyopt_list4tag";
    //获取草单USER对应的列表
    public static final String EO_SHOW_USER_LISTS = "55haitao_sns.SnsAPI/easyopt_list4user";
    //获取我收藏的草单
    public static final String EO_SHOW_LIKE_LISTS = "55haitao_sns.SnsAPI/easyopt_list4like";

    //获取草单详情
    public static final String EO_SHOW_DETAIL = "55haitao_sns.SnsAPI/easyopt_detail";

    // 草单更新、新建
    public static final String EO_ACTION_UPDATE = "55haitao_sns.SnsAPI/easyopt_update";
    //草单收藏
    public static final String EO_ACTION_STAR   = "55haitao_sns.SnsAPI/easyopt_like";
    //草单点赞
    public static final String EO_ACTION_LIKE   = "55haitao_sns.SnsAPI/easyopt_point";

    //人民币符号
    public static final String RMB_UNICODE         = "\u00A5";
    public static final String RMB_UNICODE_SYMBOL  = "￥";
    //错误提示
    public static final String ILLEGAL_SPECIAL_URI = "无效的Uri版本";
}
