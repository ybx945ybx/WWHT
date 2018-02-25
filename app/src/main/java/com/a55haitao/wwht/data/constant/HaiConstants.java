package com.a55haitao.wwht.data.constant;

import com.a55haitao.wwht.data.net.ActivityCollector;

/**
 * Created by 董猛 on 16/8/18.
 */
public class HaiConstants {

    public interface ReponseCode {


        /**
         * Device Token空
         */
        int Code_DTK_Empty = -90014;  // DTK空

        /**
         * UserToken 为空
         */
        int CODE_UTK_EMPTY = -90015;  // UTK空

        /**
         * 解析Device Token异常
         */
        int CODE_DECODE_DEVICE_TOKEN_ERROR = -30;

        /**
         * 解析User Token异常
         */
        int CODE_DECODE_USER_TOKEN_ERROR = -40;

        /**
         * User Token过期
         */
        int CODE_USER_TOKEN_EXPIRED = -41;

        /**
         * 其他地方登陆
         */
        int CODE_LOGINED_ON_OTHER_DEVICE = -42;

        /**
         * 不支持的签名方法
         */
        int CODE_SIGNATURE_METHOD_UNSUPPORTED = -50;

        /**
         * 签名错误
         */
        int CODE_SIGNATURE_ERROR = -51;

        /**
         * 无效笔记
         */
        int CODE_INVALIDATE_POST = 11101;

        /**
         * 商品未找到
         */
        int CODE_PRODUCT_NOT_FOUND = 14000;

        /**
         * 已绑定第三方账号
         */
        int CODE_SSO_ALREADY_BINDED = 1405;

        /**
         * // 提交订单时核价失败，请重试.返回了商品变化信息
         */
        int E_PRODUCT_CHeckPRICE_FAILURE         = 102046;

        /* 参考CODE
        //设备(100-200)
        int E_DEVICE_REGISTED                                   = 101; // 已经注册的设备(设备id冲突),

        //通用错误 (1000 - 1999)
        int E_COMMON_UNKNOWN_ERROR                              = 1001; // 不明错误
        int E_COMMON_KEY_NOT_EXIST                              = 1002; // key不存在
        int E_COMMON_ASSERT_FAIL                                = 1003; // assert失败
        int E_COMMON_VALUE_NONE                                 = 1004; // 数据为None
        int E_COMMON_VALUE_TYPE_ERROR                           = 1005; // 数据的类型错误
        int E_COMMON_MYSQL_ERROR                                = 1006; // 数据库错误
        int E_COMMON_ROW_NOT_FOUND                              = 1007; // 此行数据不存在
        int E_COMMON_SETTING_NOT_FOUND                          = 1008; // 未找到配置参数
        int E_COMMON_SENSITIVE_WORDS_INCLUDED                   = 1009; // 包含敏感词
        int E_COMMON_OPERATION_FORBIDDEN                        = 1010; // 操作禁止
        int E_COMMON_DB_ERROR                                   = 1011; // 数据库读写失败
        int E_COMMON_VALUE_NONE_OR_EMPTY                        = 1012; // 数据为None或者空字符串
        int E_COMMON_ARGUMENT_MISSING                           = 1013; // 参数缺失
        int E_COMMON_PHONE_FORMAT_ERROR                         = 1014; // 手机号码格式不正确
        int E_COMMON_BIZ_ERROR                                  = 1015; // 业务异常
        int E_COMMON_EMOJI_UNSUPPORT                            = 1016; // 不支持表情
        int E_COMMON_INVALID_VALUE                              = 1017; // 不合法的数值
        int E_COMMON_SENSITIVE_WORDS                            = 1018; // 包含敏感词


        // 验证码
        int E_VERIFY_CODE_WRONG                                 = 1105; // 验证码错误
        int E_VERIFY_CODE_TYPE_ERROR                            = 1106; // 验证码类型错误
        int E_VERIFY_CODE_PHONE_EXPIRED                         = 1107; // 验证码已经过期
        int E_VERIFY_CODE_TOO_FAST                              = 1109; // 获取验证码太快
        int E_VERIFY_CODE_NULL                                  = 1110; // 空的验证码
        int E_VERIFY_PASSWD_NOT_SAME_REPEAT                     = 1111; // 新密码和验证码不一致

        // 用户
        int E_USER_QQ_PHONE_BINDED                              = 1161; // 手机已绑定其他QQ号
        int E_USER_WEIBO_PHONE_BINDED                           = 1162; // 手机已绑定其他微博号
        int E_USER_WECHAT_PHONE_BINDED                          = 1163; // 手机已绑定其他微信号
        int E_USER_USERNAME_EXISTED                             = 1164; // 用户名已经存在
        int E_USER_INVALID_USERNAME                             = 1165; // 不存在的用户名
        int E_USER_SIGN_IN_FAIL                                 = 1166; // 登录失败
        int E_USER_WRONG_PASSWORD                               = 1167; // 密码错误
        int E_USER_LOGIN_FAIL                                   = 1168; // 登录失败
        int E_USER_UNBIND_PHONE                                 = 1169; // 未绑定的手机号
        int E_USER_INVALID_USER_ID                              = 1170; // 不存在的uid
        int E_USER_NICKNAME_TOOLONG                             = 1171; // 昵称太长
        int E_USER_NICKNAME_EXIST                               = 1172; // 昵称已经存在
        int E_USER_PASSWD_LENGTH_ERROR                          = 1173; // 密码长度不合法,新密码长度必须在6~16位之间

        // 手机号码
        int E_PHONE_REGISTERED                                  = 1201; // 手机号码已经注册
        int E_PHONE_UNREGISTERED                                = 1202; // 手机号码错误,未注册
        int E_PHONE_BIND_BY_OTHERS                              = 1203; // 手机号码绑定其他手机

        // 地址
        int E_ADDRESS_RECEIVER_CANT_NULL                        = 1301; // 收件人不能为空
        int E_ADDRESS_PHONE_WRONG                               = 1302; // 错误的手机号
        int E_ADDRESS_ADDR_CANT_NULL                            = 1303; // 详细地址不能为空
        int E_ADDRESS_MOBILE_OR_TEL                             = 1304; // 手机号码和固定号码必填一项
        int E_ADDRESS_INVALID_IDT                               = 1305; // 无效的身份证

        // 格式
        int E_FORMAT_IDT_NUMBER_ERROR                           = 501; // 身份证号码错误
        int E_FORMAT_EMAIL_ERROR                                = 502; // 邮箱格式错误
        int E_FORMAT_MOBILE_ERROR                               = 503; // 手机格式错误
        int E_FORMAT_TEL_ERROR                                  = 504; // 电话格式错误
        int E_FORMAT_AREA_ERROR                                 = 505; // 地区编码错误
        int E_FORMAT_POSTCODE_ERROR                             = 506; // 邮编错误

        // 活动(activity) error (12000, 12999)
        int E_ACTIVITY_NOT_FOUND                                = 12000; // 活动未找到
        int E_ACTIVITY_PRIZE_NOT_FOUND                          = 12001; // 活动的奖品未找到
        int E_ACTIVITY_COMMENT_NOT_FOUND                        = 12002; // 活动的评论未找到
        int E_ACTIVITY_COMMENT_SENSITIVE                        = 12003; // 评论包含敏感词

        // special error (13000, 13999)
        int E_SPECIAL_INVALID_SPECIAL_ID                        = 13000;
        int E_SPECIAL_INVALID_COMMENT_ID                        = 13001;
        int E_SPECIAL_BUSI_ERROR                                = 13002;
        int E_SPECIAL_INCLUDE_SENSITIVE_WORDS                   = 13003;
        int E_SPECIAL_INVALID_YEAR                              = 13004;
        int E_SPECIAL_INVALID_PARAM                             = 13005;

        // 商品(product) error (14000, 14999)
        int E_PRODUCT_NOT_FOUND                                 = 14000;  // 商品未找到
        int E_PRODUCT_SKU_NOT_FOUND                             = 14001;  // 商品SKU未找到
        int E_PRODUCT_UNAVAILABLE                               = 14002;  // 商品当前不可购买
        int E_ADDRESS_NOT_FOUND                                 = 14003;  // 地址未找到
        int E_PRODUCT_ORDER_FAIL                                = 14004;  // 创建账单失败
        int E_PRODUCT_ORDER_NOT_FOUND                           = 14005;  // 账单未找到
        int E_PRODUCT_WEIXIN_UNIFIEDORDER                       = 14006;  // 微信统一下单接口错误
        int E_PRODUCT_INVALID_PAYTYPE                           = 14007;  // 无效的支付方式
        int E_PRODUCT_OUT_COUNT                                 = 102054; // 购买商品数量超出限制(每次限购一件)

        int E_PRODUCT_PRICE_CHANGED                             = 102045; // 部分商品价格发生了变化,请返回重新选购.
        int E_PRODUCT_CHeckPRICE_FAILURE                        = 102046; // 核价失败，请重试.
        int E_PRODUCT_SOLD_OUT                                  = 106001; // 手速太慢啦，部分商品已售罄，请返回重新选购.

        // user error(15000, 15999)
        int E_USER_NOT_EXIST                                    = 15000; // 用户不存在
        int E_USER_ALREADY_FOLLOW                               = 15001; // 用户已关注
        int E_USER_NOT_FOLLOW                                   = 15002; // 用户还未关注
        int E_USER_MUST_FOLLOW                                  = 15003; // 该用户为必须关注的用户

        // misc error(11000, 12000)
        int E_TOPIC_TAG_FORBIDDEN                               = 11100; // topic的tag是禁止状态
        int E_INVALID_POST_ID                                   = 11101; // 无效笔记ID
        int E_LIKE_FORBIDDEN                                    = 11102;
        int E_TOPIC_NOT_FOUND                                   = 11103;
        int E_TOPIC_SENSITIVE_CONTENT                           = 11104; // 笔记内容包含敏感词
        int E_TOPIC_TAG_EMOJI                                   = 11105; // 笔记标签不支持表情
        int E_TOPIC_COMMENT_FORBIDDEN                           = 11106;
        int E_INVALID_COMMENT_ID                                = 11107;
        int E_COMMENT_FORBIDDEN                                 = 11108;
        */
    }

    public static class CompoundSize {
        static {
            float density = ActivityCollector.getTopActivity().getResources().getDisplayMetrics().density;
            PX_420 = (int) (210 * density);
            PX_400 = (int) (200 * density);
            PX_280 = (int) (140 * density);
            PX_240 = (int) (120 * density);
            PX_100 = (int) (50 * density);
            PX_56 = (int) (28 * density);
            PX_30 = (int) (15 * density);
            PX_20 = (int) (10 * density);
            PX_10 = (int) (5 * density);

            PX_Single = (int) (375 * density);
            PX_Twice = (int) (188 * density);
            PX_Tribble = (int) (125 * density);
            PX_Fourth = (int) (90 * density);
        }

        public final static int PX_420;
        public final static int PX_400;
        public final static int PX_280;
        public final static int PX_240;
        public final static int PX_100;
        public final static int PX_30;
        public final static int PX_56;
        public final static int PX_20;
        public final static int PX_10;

        // 单列、双列、三列和最小的图片宽度
        public final static int PX_Single;
        public final static int PX_Twice;
        public final static int PX_Tribble;
        public final static int PX_Fourth;
    }

    //人民币符号
    public static final String RMB_UNICODE = "\u00A5";
}
