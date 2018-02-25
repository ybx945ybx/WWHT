package com.a55haitao.wwht.utils;

/**
 * Created by a55 on 2017/6/20.
 */

public class TraceUtils {

    // 页面code
    public static final String PageCode_Hot                 = "AH"; // 首页热门
    public static final String PageCode_Tab                 = "AS"; // 首页频道页
    public static final String PageCode_OfficialSaleProduct = "A01"; // 专题双列
    public static final String PageCode_OfficalSale         = "A02"; // 官网特卖列表页
    public static final String PageCode_Album               = "A04"; // 精选草单列表
    public static final String PageCode_SpecialCollection   = "A06"; // 精选合集列表页
    public static final String PageCode_Express             = "A08"; // 优惠快报列表页
    public static final String PageCode_FlashSale           = "A09"; // 限时抢购列表页
    public static final String PageCode_Product             = "A10"; // 商品列表页（分类／搜索）
    public static final String PageCode_Cart                = "A30"; // 购物车Activity／fragment页
    public static final String PageCode_ProductDetail       = "A31"; // 商品详情页
    public static final String PageCode_OrderCreat          = "A32"; // 订单确认页
    public static final String PageCode_OrderList           = "A33"; // 订单列表页
    public static final String PageCode_OrderDetail         = "A34"; // 订单详情页
    public static final String PageCode_TransDetail         = "A35"; // 物流详情页
    public static final String PageCode_PayResult           = "A36"; // 付款结果页
    public static final String PageCode_Refund              = "A40"; // 申请退款页
    public static final String PageCode_CanRefundProducts   = "A41"; // 选择退款受理商品列表页
    public static final String PageCode_RefundCommitResult  = "A42"; // 申请退款提交结果页
    public static final String PageCode_RefundDetail        = "A43"; // 申请退款单详情页
    public static final String PageCode_RefundList          = "A44"; // 申请退款进度列表页
    public static final String PageCode_SearchWords         = "A50"; // 搜索页
    public static final String PageCode_H5                  = "A60"; // H5页

    public static final String PageCode_Category          = "B"; // 分类页
    public static final String PageCode_AllCategory       = "B01"; // 全部分类页
    public static final String PageCode_AllBrandOrSite    = "B02"; // 全部品牌／商家页
    public static final String PageCode_BrandOrSiteDetail = "B04"; // 商家／品牌详情页

    public static final String PageCode_Social          = "C"; // 社区页
    public static final String PageCode_PostDetail      = "C01"; // 笔记详情页
    public static final String PageCode_CommentsList    = "C02"; // 笔记详情全部评论列表页
    public static final String PageCode_PostLikeList    = "C03"; // 笔记详情全部点赞列表页
    public static final String PageCode_SocialDetail    = "C04"; // 社区专题详情页
    public static final String PageCode_HotTagList      = "C07"; // 热门标签列表页
    public static final String PageCode_HotTagDetail    = "C08"; // 热门标签详情页
    public static final String PageCode_HomePage        = "C10"; // 他人主页
    public static final String PageCode_FansOrFollows   = "C11"; // 他人主页关注／粉丝列表页
    public static final String PageCode_AddFriends      = "C12"; // 添加好友页
    public static final String PageCode_ContactsFriend  = "C13"; // 通讯录好友列表页
    public static final String PageCode_EasyCreat       = "C20"; // 新建/编辑草单页
    public static final String PageCode_EasyDetail      = "C21"; // 草单详情页
    public static final String PageCode_EasyComments    = "C22"; // 草单评论列表页
    public static final String PageCode_PostEdit        = "C30"; // 编辑笔记页
    public static final String PageCode_PostEditAddTags = "C31"; // 编辑笔记添加标签页

    public static final String PageCode_MyAccount         = "E"; // 我的页
    public static final String PageCode_MyCouponList      = "E02"; // 优惠券列表页
    public static final String PageCode_MyMembershipPoint = "E03"; // 我的积分页
    public static final String PageCode_MyWhisList        = "E04"; // 我的心愿单页
    public static final String PageCode_MyEasy            = "E05"; // 我的草单页
    public static final String PageCode_MyFollowBrandSite = "E06"; // 关注的品牌商家页
    public static final String PageCode_Message           = "E07"; // 消息通知页
    public static final String PageCode_BindThird         = "E08"; // 绑定第三方页
    public static final String PageCode_ChangePassWord    = "E09"; // 修改密码页
    public static final String PageCode_AddressList       = "E10"; // 地址管理页
    public static final String PageCode_AddressEdit       = "E11"; // 新增地址页
    public static final String PageCode_AboutUs           = "E12"; // 关于我们页
    public static final String PageCode_FeedBack          = "E13"; // 反馈意见页
    public static final String PageCode_UserInfoSet       = "E14"; // 个人信息设置页 设置
    public static final String PageCode_LoginMobile       = "E20"; // 手机号登录页
    public static final String PageCode_Register          = "E21"; // 注册页
    public static final String PageCode_ResetPassword     = "E22"; // 重置密码页
    public static final String PageCode_LoginMail         = "E23"; // 邮箱登录页
    public static final String PageCode_BindPhone         = "E24"; // 绑定手机页
    public static final String PageCode_LoginVerifyCode   = "E25"; // 手机验证码登录页

    public static final String PageCode_ADS     = "F01"; // 开屏广告页
    public static final String PageCode_Country = "F02"; // 选择国家

    // 热区code
    public static final String PositionCode_Ignore                   = "-2"; // 忽略[Update]
    public static final String PositionCode_Null                     = "-1"; // 自动弹
    public static final String PositionCode_SearchBar                = "0";  // 搜索栏
    public static final String PositionCode_Banner                   = "1";  // Banner条
    public static final String PositionCode_PageControl              = "2";  // 分页控制条      SubCode:Hot：热门, Clothe：服装, Shoe：鞋靴,  Bag：包包, Cosmetic：美妆, ChildClothe：童装, URI
    public static final String PositionCode_Icon                     = "3";  // 首页4个按钮     SubCode:Index[0,N]
    public static final String PositionCode_Express                  = "4";  // 优惠快报        SubCode:商品SpuId
    public static final String PositionCode_ExpressMore              = "5";  // 优惠快报更多
    public static final String PositionCode_FlashSale                = "6";  // 限时抢购        SubCode:商品SpuId
    public static final String PositionCode_FlashSaleMore            = "7";  // 限时抢购更多
    public static final String PositionCode_OfficialSale             = "8";  // 官网特卖        SubCode:Hot：热门, Clothe：服装, Shoe：鞋靴,  Bag：包包, Cosmetic：美妆, ChildClothe：童装
    public static final String PositionCode_OfficialSaleMore         = "9";  // 官网特卖更多
    public static final String PositionCode_Album                    = "10"; // 草单            SubCode:草单Id[Update]
    public static final String PositionCode_TabBar                   = "11"; // 底部TabBar条
    public static final String PositionCode_HotCategory              = "12"; // 分类           SubCode:名称
    public static final String PositionCode_SpecialCollection        = "13"; // 精选合集        SubCode:Hot：热门, Clothe：服装, Shoe：鞋靴,  Bag：包包, Cosmetic：美妆, ChildClothe：童装
    public static final String PositionCode_SpecialCollectionMore    = "14"; // 精选合集更多
    public static final String PositionCode_SpecialCollectionProduct = "15"; // 精选合集商品     SubCode:Index[0,N]
    public static final String PositionCode_RecommendProduct         = "16"; // 单品推荐商品     SubCode:商品SpuId
    public static final String PositionCode_OfficialSaleProduct      = "17"; // 官网特卖商品     SubCode:商品SpuId
    public static final String PositionCode_Product                  = "18"; // 商品           SubCode:商品SpuId
    public static final String PositionCode_Cart                     = "19"; // 购物车
    public static final String PositionCode_Store                    = "20"; // 商家           SubCode:商家Id
    public static final String PositionCode_Purchase                 = "21"; // 立即购买
    public static final String PositionCode_UseCoupon                = "22"; // 优惠券
    public static final String PositionCode_Brand                    = "23"; // 品牌           SubCode:品牌Id
    public static final String PositionCode_BrandProduct             = "24"; // 品牌商品
    public static final String PositionCode_EveryBuyingProduct       = "25"; // 大家都在买
    public static final String PositionCode_Post                     = "26"; // 帖子           SubCode:帖子Id
    public static final String PositionCode_MustKnow                 = "27"; // 购物须知
    public static final String PositionCode_Address                  = "28"; // 地址
    //    public static final int PositionCode_Order                    = 29; // 订单           SubCode:0：全部 1：待付款 2：待发货 3：待收货 4：已完成
    public static final String PositionCode_Logistic                 = "30"; // 物流           SubCode:订单Id
    public static final String PositionCode_AfterSaleProgress        = "31"; // 查看退款申请进度 SubCode:订单Id
    public static final String PositionCode_ApplyAfterSale           = "32"; // 申请退款
    public static final String PositionCode_ViewOrder                = "33"; // 查看订单        SubCode:订单Id
    public static final String PositionCode_GoAround                 = "34"; // 继续逛逛
    public static final String PositionCode_SelectProduct            = "35"; // 选择商品
    public static final String PositionCode_SubmitAfterSale          = "36"; // 提交申请退款
    public static final String PositionCode_ViewAfterSale            = "37"; // 查看退款申请单   SubCode:订单Id
    public static final String PositionCode_SearchKey                = "38"; // 搜索关键字      SubCode:key
    public static final String PositionCode_Filter                   = "39"; // 筛选
    public static final String PositionCode_AllCategory              = "40"; // 全部分类
    public static final String PositionCode_AllBrand                 = "41"; // 全部品牌
    public static final String PositionCode_AllStore                 = "42"; // 全部商家
    public static final String PositionCode_Friend                   = "43"; // 添加好友
    public static final String PositionCode_SendPost                 = "44"; // 发帖
    public static final String PositionCode_User                     = "29"; // 用户
    public static final String PositionCode_Tag                      = "45"; // 标签           SubCode:标签Id
    public static final String PositionCode_TagMore                  = "46"; // 标签更多
    public static final String PositionCode_UserTag                  = "47"; // 用户标签
    public static final String PositionCode_ViewLikingUsers          = "48"; // 查看点赞用户
    public static final String PositionCode_ViewComments             = "49"; // 查看评论列表
    public static final String PositionCode_Following                = "50"; // 关注
    public static final String PositionCode_Follower                 = "51"; // 粉丝
    public static final String PositionCode_SearchContact            = "52"; // 通讯录好友
    public static final String PositionCode_AddUserTag               = "53"; // 添加用户标签
    public static final String PositionCode_NoticeMessage            = "54"; // 通知消息
    public static final String PositionCode_Setting                  = "55"; // 设置
    public static final String PositionCode_Login                    = "56"; // 登录
    public static final String PositionCode_MyOrderList              = "57"; // 订单列表      SubCode:0：全部 1：待付款 2：待发货 3：待收货
    public static final String PositionCode_MyCoupon                 = "58"; // 我的优惠券
    public static final String PositionCode_MyPoint                  = "59"; // 我的积分
    public static final String PositionCode_MyPray                   = "60"; // 我的心愿单
    public static final String PositionCode_MyAlbum                  = "61"; // 我的草单
    public static final String PositionCode_MyFollowingStore         = "62"; // 我关注的商家
    public static final String PositionCode_MyFollowingBrand         = "63"; // 我关注的品牌
    public static final String PositionCode_NoticeItem               = "64";// 通知      subcode   uri
    public static final String PositionCode_AddAddress               = "65"; // 添加地址
    public static final String PositionCode_ViewAddress              = "66"; // 查看地址
    public static final String PositionCode_UserLicense              = "67"; // 用户协议
    public static final String PositionCode_PrivacyStatement         = "68"; // 隐私声明
    public static final String PositionCode_Register                 = "69"; // 注册
    public static final String PositionCode_OtherLogin               = "70"; // 其它方式登录  SubCode:0：Account 1：Code
    public static final String PositionCode_ForgetPassword           = "71"; // 忘记密码
    public static final String PositionCode_AlbumCreate              = "72"; // 创建、编辑草单
    public static final String PositionCode_EditInfomation           = "73"; // 编辑个人信息
    public static final String PositionCode_Jumping                  = "74"; // 跳转（磨窗、Scheme等方式唤醒app方式）
    public static final String PositionCode_Pay                      = "75"; // 支付
    public static final String PositionCode_AlbumSelectedList        = "76"; // 精选草单列表(更多)
    public static final String PositionCode_Settlement               = "77"; // 购物车前往结算
    public static final String PositionCode_Ads                      = "78"; // 广告点击
    public static final String PositionCode_MallSpecial              = "79"; // 潮流风尚     SubCode:潮流风尚专题Id
    public static final String PositionCode_MallSearchSpecial        = "80"; // 搜索专题     SubCode:搜索专题 Id
    public static final String PositionCode_HotSkuMore               = "81"; // 热卖单品更多
    public static final String PositionCode_SelectCountry            = "82"; // 选择国家地区
    public static final String PositionCode_Feedback                 = "83"; // 意见反馈
    public static final String PositionCode_SNSSpecial               = "84"; // 社区专题
    public static final String PositionCode_SimilarProduct           = "85"; // 相似品牌商品
    public static final String PositionCode_Bind3rdParty             = "86"; // 绑定第三方
    public static final String PositionCode_ChangePassword           = "87"; // 修改密码
    public static final String PositionCode_AboutUs                  = "88"; // 关于我们
    public static final String PositionCode_MallActivity             = "89"; // 活动弹窗
    public static final String PositionCode_PostEdit                 = "90"; // 帖子编辑
    public static final String PositionCode_AddCart                  = "91"; // 加入购物车
    public static final String PositionCode_HotAd                    = "92"; // 热门页广告栏
    public static final String PositionCode_HotBrand                 = "93"; // 人气品牌

    public static final int SubCode_SearchWordsNearly = 0; // 最近搜
    public static final int SubCode_SearchWordsHot    = 1; // 大家都在搜

    public static final int SubCode_OrderAll       = 0; // 全部
    public static final int SubCode_OrderUnPay     = 1; // 待付款
    public static final int SubCode_OrderUnDeliver = 2; // 待发货
    public static final int SubCode_OrderUnReceive = 3; // 待收货
    public static final int SubCode_OrderComplete  = 4; // 已完成

    public static final String SubCode_Hot         = "Hot"; // 热门
    public static final String SubCode_Clothe      = "Clothe"; // 服装
    public static final String SubCode_Shoe        = "Shoe"; // 鞋靴
    public static final String SubCode_Bag         = "Bag"; // 包包
    public static final String SubCode_Cosmetic    = "Cosmetic"; // 美妆
    public static final String SubCode_ChildClothe = "ChildClothe"; // 童装

    // pa


    // 渠道号
    public static final String Chid_Hot_Search      = "H_K";   // 热门页搜索
    public static final String Chid_Hot_Banner      = "H_B-";  // 热门banner
    public static final String Chid_Hot_Icon_One    = "H_I-0"; // 热门四个icon
    public static final String Chid_Hot_Icon_Two    = "H_I-1"; // 热门四个icon
    public static final String Chid_Hot_Icon_Three  = "H_I-2"; // 热门四个icon
    public static final String Chid_Hot_Icon_Four   = "H_I-3"; // 热门四个icon
    public static final String Chid_Hot_Letter      = "H_L";   // 热门优惠快报
    public static final String Chid_Hot_Ads         = "H_AD";  // 热门广告栏
    public static final String Chid_Hot_FlashSale   = "H_SS";  // 热门限时抢购
    public static final String Chid_Hot_OfficalSale = "H_OS";  // 热门官网特卖
    public static final String Chid_Hot_ALbum       = "H_A";   // 热门推荐草单

    public static String getFirstTabChidHead(String tabName) {
        return "S-" + tabName + "_";
    }

    // event事件类目
    public static final String Event_Category_Click = "c";        // 点击类目
    public static final String Event_Category_Order = "o";        // 订单类目

    // event事件动作
    public static final String Event_Action_Click_Goods     = "cg";  // 点击商品
    public static final String Event_Action_Click_Banner    = "cb";  // 点击banner
    public static final String Event_Action_Click_Register  = "cr";  // 点击注册
    public static final String Event_Action_Click_Register3 = "cr3"; // 点击注册（通过第三方账户）

    public static final String Event_Action_Order_Goods_Buy = "ogb"; // 商品详情点击购买时触发（订单确认）
    public static final String Event_Action_Order_Cart_Buy  = "ocb"; // 购物车点击前往结算时触发（订单确认）
    public static final String Event_Action_Order_Address   = "od";  // 用户填写信息
    public static final String Event_Action_Order_Cart      = "oc";  // 加入购物车
    public static final String Event_Action_Order_Sign      = "or";  // 用户签收（确认收货）
    public static final String Event_Action_Order_Pay       = "os";  // 用户支付（产生了订单）
    public static final String Event_Action_Order_Refund    = "oq";  // 用户退款（用户支付完后未抢单前可以取消订单）

    // kv参
    public static final String Event_Kv_Goods_Id  = "kv_spuid";   // 商品编号
    public static final String Event_Kv_Banner_Id = "kv_idx";     // Banner Index下标
    public static final String Event_Kv_Mobile    = "kv_mobile";  // 注册手机
    public static final String Event_Kv_Country   = "kv_country"; // 注册国家代码
    public static final String Event_Kv_Account   = "kv_account"; // 第三方账户类型
    public static final String Event_Kv_Openid    = "kv_openid";  // 第三方账户OpenID
    public static final String Event_Kv_Code      = "kv_code";    // 是否注册成功(0表示成功 其它表示服务器返回的错误码)

    public static final String Event_Kv_Order_Id        = "kv_oid";      // 订单编号
    public static final String Event_Kv_Order_Spuid     = "kv_spuid";    // 商品SPU编号
    public static final String Event_Kv_Order_Skuid     = "kv_skuid";    // 商品SKU编号(如果是多个SKUID，用逗号分隔)
    public static final String Event_Kv_Order_Count     = "kv_cnt";      // 商品数量
    public static final String Event_Kv_Order_Name      = "kv_on";       // 订单名称 order name 商品名称
    public static final String Event_Kv_Order_Amount    = "kv_cua";      // 订单金额
    public static final String Event_Kv_Order_CashType  = "kv_cut";      // 货币类型
    public static final String Event_Kv_Orderr_PayType  = "kv_pt";       // 支付方式
    public static final String Event_Kv_Orderr_Tradeno  = "kv_tradeno";  // 交易流水号
    public static final String Event_Kv_Order_OrderInfo = "kv_orderinfo";  // 交易流水号
    public static final String Event_Kv_Order_OrderPre = "kv_orderprepare";  // 交易流水号
    public static final String Event_Kv_Cartid          = "kv_cartid";   // 加入购物车id
    public static final String Event_Kv_CartInfo        = "kv_cartinfo";  // 加入购物车id

}
