package com.a55haitao.wwht.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.interfaces.IReponse;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.OrderDetailResult;
import com.a55haitao.wwht.data.repository.DeviceRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.myaccount.account.LoginMobileActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.orhanobut.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 公共工具类
 *
 * @author 陶声
 * @since 2016-10-10
 */

public class HaiUtils {

    /**
     * 判断app是否处于前台
     */
    public static boolean isForeground() {
        return HaiApplication.getAppCount() > 0;
    }

    /**
     * 用户是否登录
     */
    public static boolean isLogin() {
        UserBean userInfo = UserRepository.getInstance()
                .getUserInfo();
        return userInfo != null && userInfo.getUserToken() != null;
    }

    /**
     * 是否需要登录
     */
    public static boolean needLogin(Context context) {
        if (!isLogin()) {
            context.startActivity(new Intent(context, LoginMobileActivity.class)); // 如果没有登录,则跳到登录页面
            return true;
        }
        return false;
    }

    /**
     * 获取device_token
     */
    public static String getDeviceToken() {
        return DeviceRepository.getInstance()
                .getDeviceToken();
    }

    /**
     * 获取user_token
     */
    public static String getUserToken() {
        UserBean userInfo = UserRepository.getInstance()
                .getUserInfo();
        if (userInfo != null) {
            return userInfo.getUserToken();
        } else {
            return "";
        }
    }

    /**
     * 获取user_id
     */
    public static int getUserId() {
        UserBean userInfo = UserRepository.getInstance()
                .getUserInfo();
        if (userInfo != null) {
            return userInfo.getId();
        } else {
            return 0;
        }
    }

    /**
     * 获取用户手机号
     */
    public static String getMobile() {
        UserBean userInfo = UserRepository.getInstance()
                .getUserInfo();
        if (userInfo != null) {
            return userInfo.getMobile();
        } else {
            return "";
        }
    }

    /**
     * 获取用户草单数
     */
    public static int getEasyoptCount() {
        RealmResults<UserBean> users = Realm.getDefaultInstance().where(UserBean.class).findAll();
        if (users.size() > 0) {
            UserBean user = users.get(0);
            if (user.getCommCounts() != null)
                return user.getCommCounts().getEasyopt_count();
        }
        return 0;
    }

    /**
     * 获取国旗图标
     *
     * @param country
     * @param isFlat  是缶要波浪形图标
     * @return
     */
    public static int getFlagResourceId(String country, boolean isFlat) {

        int result = -1;

        if (country == null) {
            return result;
        }

        if (isFlat) {

            if (country.equalsIgnoreCase("澳大利亚") || country.equalsIgnoreCase("AUS") || country.equalsIgnoreCase("AU"))
                result = R.mipmap.ic_flag_australia_rect;
            else if (country.equalsIgnoreCase("加拿大") || country.equalsIgnoreCase("CA"))
                result = R.mipmap.ic_flag_canada_rect;
            else if (country.equalsIgnoreCase("中国") || country.equalsIgnoreCase("CN") || country.equalsIgnoreCase("ZH"))
                result = R.mipmap.ic_flag_china_rect;
            else if (country.equalsIgnoreCase("法国") || country.equalsIgnoreCase("FR"))
                result = R.mipmap.ic_flag_france_rect;
            else if (country.equalsIgnoreCase("德国") || country.equalsIgnoreCase("DE"))
                result = R.mipmap.ic_flag_germany_rect;
            else if (country.equalsIgnoreCase("意大利") || country.equalsIgnoreCase("IT"))
                result = R.mipmap.ic_flag_italy_rect;
            else if (country.equalsIgnoreCase("日本") || country.equalsIgnoreCase("JP"))
                result = R.mipmap.ic_flag_japan_rect;
            else if (country.equalsIgnoreCase("韩国") || country.equalsIgnoreCase("KR"))
                result = R.mipmap.ic_flag_korea_rect;
            else if (country.equalsIgnoreCase("新西兰") || country.equalsIgnoreCase("NZ"))
                result = R.mipmap.ic_flag_new_zealand_rect;
            else if (country.equalsIgnoreCase("俄罗斯") || country.equalsIgnoreCase("RU"))
                result = R.mipmap.ic_flag_russia_rect;
            else if (country.equalsIgnoreCase("新加坡") || country.equalsIgnoreCase("SG"))
                result = R.mipmap.ic_flag_singapore_rect;
            else if (country.equalsIgnoreCase("泰国") || country.equalsIgnoreCase("TH"))
                result = R.mipmap.ic_flag_thailand_rect;
            else if (country.equalsIgnoreCase("英国") || country.equalsIgnoreCase("UK"))
                result = R.mipmap.ic_flag_uk_rect;
            else if (country.equalsIgnoreCase("美国") || country.equalsIgnoreCase("USA") || country.equalsIgnoreCase("US"))
                result = R.mipmap.ic_flag_usa_rect;
        } else {

            if (country.equalsIgnoreCase("澳大利亚") || country.equalsIgnoreCase("AUS") || country.equalsIgnoreCase("AU"))
                result = R.mipmap.ic_flag_australia_wave;
            else if (country.equalsIgnoreCase("加拿大") || country.equalsIgnoreCase("CA"))
                result = R.mipmap.ic_flag_canada_wave;
            else if (country.equalsIgnoreCase("中国") || country.equalsIgnoreCase("CN") || country.equalsIgnoreCase("ZH"))
                result = R.mipmap.ic_flag_china_wave;
            else if (country.equalsIgnoreCase("法国") || country.equalsIgnoreCase("FR"))
                result = R.mipmap.ic_flag_france_wave;
            else if (country.equalsIgnoreCase("德国") || country.equalsIgnoreCase("DE"))
                result = R.mipmap.ic_flag_germany_wave;
            else if (country.equalsIgnoreCase("意大利") || country.equalsIgnoreCase("IT"))
                result = R.mipmap.ic_flag_italy_wave;
            else if (country.equalsIgnoreCase("日本") || country.equalsIgnoreCase("JP"))
                result = R.mipmap.ic_flag_japan_wave;
            else if (country.equalsIgnoreCase("韩国") || country.equalsIgnoreCase("KR"))
                result = R.mipmap.ic_flag_korea_wave;
            else if (country.equalsIgnoreCase("新西兰") || country.equalsIgnoreCase("NZ"))
                result = R.mipmap.ic_flag_new_zealand_wave;
            else if (country.equalsIgnoreCase("俄罗斯") || country.equalsIgnoreCase("RU"))
                result = R.mipmap.ic_flag_russia_wave;
            else if (country.equalsIgnoreCase("新加坡") || country.equalsIgnoreCase("SG"))
                result = R.mipmap.ic_flag_singapore_wave;
            else if (country.equalsIgnoreCase("泰国") || country.equalsIgnoreCase("TH"))
                result = R.mipmap.ic_flag_thailand_wave;
            else if (country.equalsIgnoreCase("英国") || country.equalsIgnoreCase("UK"))
                result = R.mipmap.ic_flag_uk_wave;
            else if (country.equalsIgnoreCase("美国") || country.equalsIgnoreCase("USA") || country.equalsIgnoreCase("US"))
                result = R.mipmap.ic_flag_usa_wave;
        }

        return result;
    }

    /**
     * 显示文本
     */
    public static void setText(View parentView, int viewId, String msg) {
        TextView textView = (TextView) parentView.findViewById(viewId);
        if (textView != null) {
            textView.setText(msg);
        }
    }

    /**
     * 设置显示/隐藏
     */
    public static void setVisibie(View parentView, int viewId, boolean visible) {
        View view = parentView.findViewById(viewId);
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置文本颜色
     */
    public static void setTextColor(View parentView, int viewId, @ColorInt int color) {
        TextView textView = (TextView) parentView.findViewById(viewId);
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public static String transformSpec(String specString) {

        if ("color".equals(specString.toLowerCase())) {
            specString = "颜色";
        } else if ("size".equals(specString.toLowerCase())) {
            specString = "尺寸";
        } else if ("length".equals(specString.toLowerCase())) {
            specString = "长度";
        } else if ("width".equals(specString.toLowerCase())) {
            specString = "宽度";
        }

        return specString;
    }

    /**
     * 分享Icon Url转换
     *
     * @param originIcon
     * @return
     */
    public static String transformSharIcon(String originIcon) {
        String result = "";
        if (TextUtils.isEmpty(originIcon)) {
            return result;
        } else if (originIcon.contains("st-prod.b0.upaiyun.com")) {
            result = originIcon;
        } else {
            result = URLEncoder.encode(originIcon);
            result = "https://h5.55haitao.com/detect?f=" + result;
        }

        return result;
    }

    public static boolean transToBool(int i) {
        return i == 1;
    }

    public static void reduceData(ArrayList arrayList) {
        int size = arrayList.size();
        if (size % 2 != 0) {
            arrayList.remove(size - 1);
        }
    }

    public static int getSize(ArrayList arrayList) {
        return arrayList == null ? 0 : arrayList.size();
    }

    public static int getSize(List arrayList) {
        return arrayList == null ? 0 : arrayList.size();
    }

    public static int getSize(String[] list) {
        return list == null ? 0 : list.length;
    }

    public static int getSize(String string) {
        return string == null ? 0 : string.length();
    }

    public static void getQueryParam(QueryBean queryBean, TreeMap<String, Object> paramMap) {
        if (queryBean == null) {
            return;
        }
        if (queryBean.query != null) {
            paramMap.put("query", queryBean.query);
        }
        if (queryBean.category != null) {
            paramMap.put("category", queryBean.category);
        }
        if (queryBean.brand != null) {
            paramMap.put("brand", queryBean.brand);
        }
        if (queryBean.seller != null) {
            paramMap.put("seller", queryBean.seller);
        }
    }

    /**
     * 获取talkingData的channelId
     *
     * @param umengChannelId 友盟渠道Id
     * @return channelId
     */
    public static String getTalkingDataChannelId(String umengChannelId) {
        Logger.t("testtest").d(umengChannelId);

        String result = "";
        switch (umengChannelId) {
            case "小米":
                result = "Xiaomi_1";
                break;
            case "360手机助手":
                result = "360store_1";
                break;
            case "百度":
                result = "Baidu_1";
                break;
            case "腾讯应用宝":
                result = "Sjqq_1";
                break;
            case "华为智汇云":
                result = "Vmall_2";
                break;
            case "魅族开发者社区":
                result = "Meizu_1";
                break;
            case "豌豆荚":
                result = "Wandoujia_1";
                break;
            case "应用汇":
                result = "Appchina_1";
                break;
            case "机锋市场":
                result = "Gfan_1";
                break;
            case "优亿市场":
                result = "Eoemarket_1";
                break;
            case "安智市场":
                result = "AnZhi_1";
                break;
            case "联想乐商店":
                result = "Lenovo_1";
                break;
            case "搜狗应用市场":
                result = "Zonesohu_1";
                break;
            case "OPPO":
                result = "Nearme_1";
                break;
            case "PP助手":
                result = "Taobao_1";
                break;
            case "乐视":
                result = "leshi_1";
                break;
            case "木蚂蚁":
                result = "Mumayi_1";
                break;
            case "金立":
                result = "Aoratec_1";
                break;
            case "VIVO":
                result = "vivo";
                break;
            case "三星":
                result = "Samsung_1";
                break;
            case "安软":
                result = "Tgbus_1";
                break;
            case "联通":
                result = "WOstore_1";
                break;
            case "锤子":
                result = "chuizi";
                break;
            case "td_tengxun":
                result = "td_tengxun";
                break;
            case "mc_tengxun":
                result = "mc_tengxun";
                break;
            case "alishoufa":
                result = "alishoufa";
                break;
            case "ppfuli":
                result = "ppfuli";
                break;
            default:
                result = "tongyong";
                break;
        }
        Logger.t("testtest").d(result);
        return result;
    }

    public static Type getParamterized(IReponse iName) {
        Type result = null;
        //1.获取实现的类的集合
        Type[] genericInterfaces = iName.getClass().getGenericInterfaces();
        //2.遍历
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericInterface).getActualTypeArguments();
                result = actualTypeArguments[0];
                break;
            }
        }
        return result;
    }

    /**
     * 计算详细地址
     *
     * @param address 地址信息
     * @return 详细地址
     */
    public static String getAddressDetail(OrderDetailResult.OrderDetailBean.OrderAddressBean address) {
        StringBuilder sbResult = new StringBuilder(address.prov);
        // 城市
        if (!TextUtils.isEmpty(address.city) && !TextUtils.equals(address.prov, address.city)) {
            sbResult.append(" ").append(address.city);
        }
        // 区
        if (!TextUtils.isEmpty(address.dist)) {
            sbResult.append(" ").append(address.dist);
        }
        // 街道
        if (!TextUtils.isEmpty(address.street)) {
            sbResult.append(" ").append(address.street);
        }
        return sbResult.toString();
    }

    /**
     * 品牌商家没图片时设置成黑底白字的
     */
    public static void setBrandOrSellerImg(HaiTextView mSmallCoverTex, String logoName, int level) {
        if (level == 1) {      //   品牌商家主页logo
            if (logoName.length() > 20) {
                mSmallCoverTex.setTextSize(5);
                mSmallCoverTex.setSingleLine();
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 18) {
                mSmallCoverTex.setTextSize(6);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 16) {
                mSmallCoverTex.setTextSize(6);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 14) {
                mSmallCoverTex.setTextSize(7);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 12) {
                mSmallCoverTex.setTextSize(7);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 10) {
                mSmallCoverTex.setTextSize(8);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 8) {
                mSmallCoverTex.setTextSize(8);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 6) {
                mSmallCoverTex.setTextSize(9);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 5) {
                mSmallCoverTex.setTextSize(11);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 4) {
                mSmallCoverTex.setTextSize(12);
                mSmallCoverTex.setText(logoName);
            } else {
                mSmallCoverTex.setTextSize(14);
                mSmallCoverTex.setText(logoName);
            }
        } else if (level == 3) {    // 商详页
            if (logoName.length() > 20) {
                mSmallCoverTex.setTextSize(1);
                mSmallCoverTex.setSingleLine();
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 18) {
                mSmallCoverTex.setTextSize(2);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 16) {
                mSmallCoverTex.setTextSize(2);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 14) {
                mSmallCoverTex.setTextSize(3);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 12) {
                mSmallCoverTex.setTextSize(3);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 10) {
                mSmallCoverTex.setTextSize(4);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 8) {
                mSmallCoverTex.setTextSize(4);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 6) {
                mSmallCoverTex.setTextSize(5);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 5) {
                mSmallCoverTex.setTextSize(7);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 4) {
                mSmallCoverTex.setTextSize(8);
                mSmallCoverTex.setText(logoName);
            } else {
                mSmallCoverTex.setTextSize(10);
                mSmallCoverTex.setText(logoName);
            }

        } else {      // 品牌商家列表页
            if (logoName.length() > 20) {
                mSmallCoverTex.setTextSize(3);
                mSmallCoverTex.setSingleLine();
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 18) {
                mSmallCoverTex.setTextSize(4);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 16) {
                mSmallCoverTex.setTextSize(4);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 14) {
                mSmallCoverTex.setTextSize(5);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 12) {
                mSmallCoverTex.setTextSize(5);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 10) {
                mSmallCoverTex.setTextSize(6);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 8) {
                mSmallCoverTex.setTextSize(6);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 6) {
                mSmallCoverTex.setTextSize(7);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 5) {
                mSmallCoverTex.setTextSize(9);
                mSmallCoverTex.setText(logoName);
            } else if (logoName.length() > 4) {
                mSmallCoverTex.setTextSize(10);
                mSmallCoverTex.setText(logoName);
            } else {
                mSmallCoverTex.setTextSize(12);
                mSmallCoverTex.setText(logoName);
            }
        }

    }

    /**
     * 判断商品是否售光
     */
    public static boolean isSoldOut(int inStock, int stock) {
        if (inStock == 0) {
            return true;
        } else if (inStock == 2) {
            if (stock == 0) {
                return true;
            }
        }
        return false;
    }

    // 保留一位小数
    public static String FloatKeepOne(float f) {
        float         f1 = Math.round(f * 10) / 10f;
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(f1);
    }

    /**
     * 退款单状态转换成相应的颜色
     */
    public static int refundStatToColor(Context context, int refund_status) {
        switch (refund_status) {
            case 0:      // 待受理
                return ContextCompat.getColor(context, R.color.product_status_orange);
            case 1:      // 受理中
                return ContextCompat.getColor(context, R.color.product_status_orange);
            case 2:      // 已受理
                return ContextCompat.getColor(context, R.color.product_status_green);
            default:
                return ContextCompat.getColor(context, R.color.product_status_orange);
        }

    }


    private final static Set<String> PublicSuffixSet = new HashSet<String>(
            Arrays.asList(new String(
                    "com|org|net|gov|edu|co|tv|mobi|info|asia|xxx|onion|cn|com.cn|edu.cn|gov.cn|net.cn|org.cn|jp|kr|tw|com.hk|hk|com.hk|org.hk|se|com.se|org.se")
                    .split("\\|")));

    private static Pattern IP_PATTERN = Pattern
            .compile("(\\d{1,3}\\.){3}(\\d{1,3})");
    /**
     * 获取url的顶级域名
     * @param url
     * @return
     */
    public static String getDomainName(URL url) {
        String host = url.getHost();
        if (host.endsWith("."))
            host = host.substring(0, host.length() - 1);
        if (IP_PATTERN.matcher(host).matches())
            return host;

        int index = 0;
        String candidate = host;
        for (; index >= 0;) {
            index = candidate.indexOf('.');
            String subCandidate = candidate.substring(index + 1);
            if (PublicSuffixSet.contains(subCandidate)) {
                return candidate;
            }
            candidate = subCandidate;
        }
        return candidate;
    }

    /**
     * 获取url的顶级域名
     * @param url
     * @return
     * @throws MalformedURLException
     */
    public static String getDomainName(String url) throws MalformedURLException {
        return getDomainName(new URL(url));
    }

}
