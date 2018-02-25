package com.a55haitao.wwht.ui.fragment.myaccount;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.OrderChangeEvent;
import com.a55haitao.wwht.data.event.UserInfoChangeEvent;
import com.a55haitao.wwht.data.event.UserStartChangeEvent;
import com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType;
import com.a55haitao.wwht.data.model.annotation.OrderFragmentType;
import com.a55haitao.wwht.data.model.entity.CommCountsBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.GetUserStarInfoCountsV12Result;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.discover.CouponListActivity;
import com.a55haitao.wwht.ui.activity.myaccount.BrandAndSellerActivity;
import com.a55haitao.wwht.ui.activity.myaccount.MessageActivity;
import com.a55haitao.wwht.ui.activity.myaccount.MyEasyoptActivity;
import com.a55haitao.wwht.ui.activity.myaccount.MyHomePageActivity;
import com.a55haitao.wwht.ui.activity.myaccount.MyMembershipPointActivity;
import com.a55haitao.wwht.ui.activity.myaccount.MyWishListActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OrderListActivity;
import com.a55haitao.wwht.ui.activity.myaccount.account.LoginMobileActivity;
import com.a55haitao.wwht.ui.activity.myaccount.settings.SettingsActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.ui.view.BadgeView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.a55haitao.wwht.utils.HaiUtils.isLogin;

/**
 * 个人中心
 *
 * @author 陶声
 * @since 2016-10-10
 */
public class CenterMyAccountFragment extends BaseFragment {

    //    @BindView(R.id.swipe)                HaiSwipeRefreshLayout mSwipe;              // 下拉刷新
    @BindView(R.id.ib_message)           ImageButton    mIbTitleMessage;     // 消息
    @BindView(R.id.ib_settings)          ImageButton    mIbTitleSettings;    // 设置
    @BindView(R.id.img_avatar)           AvatarView     mAvatar;             // 头像
    @BindView(R.id.tv_nickname)          HaiTextView    mTvNickname;         // 昵称
    @BindView(R.id.tv_enter_my_homepage) HaiTextView    mTvEnterMyHomepage;  // 进入我的主页
    @BindView(R.id.ll_user_info)         LinearLayout   mLlUserInfo;         // 用户信息
    @BindView(R.id.tv_order_unpay)       HaiTextView    mTvOrderUnPay;       // 待付款
    @BindView(R.id.tv_order_undeliver)   HaiTextView    mTvOrderUnDeliver;   // 待发货
    @BindView(R.id.tv_order_unreceive)   HaiTextView    mTvOrderUnReceive;   // 待收货
    @BindView(R.id.tv_order_all)         HaiTextView    mTvOrderAll;         // 全部订单
    @BindView(R.id.rl_coupon)            RelativeLayout mRlCoupon;           // 我的优惠券
    @BindView(R.id.tv_coupon_count)      HaiTextView    mTvCouponCount;      // 优惠券数
    @BindView(R.id.rl_membership_point)  RelativeLayout mRlMembershipPoint;  // 我的积分
    @BindView(R.id.tv_membership_point)  HaiTextView    mTvMembershipPoint;  // 积分数
    @BindView(R.id.rl_wishlist)          RelativeLayout mRlFavorProduct;     // 收藏的商品
    @BindView(R.id.rl_following_store)   RelativeLayout mRlFollowingStore;   // 关注的商家
    @BindView(R.id.rl_following_brand)   RelativeLayout mRlFollowingBrand;   // 关注的品牌

    @BindString(R.string.key_type)       String KEY_TYPE;
    @BindString(R.string.key_user_id)    String KEY_USER_ID;
    @BindString(R.string.key_mobile)     String KEY_MOBILE;
    @BindString(R.string.key_method)     String KEY_METHOD;
    @BindString(R.string.key_user_token) String KEY_USER_TOKEN;
    @BindString(R.string.key_head_img)   String KEY_HEAD_IMG;
    @BindString(R.string.key_nickname)   String KEY_NICKNAME;
    @BindString(R.string.key_sex)        String KEY_SEX;
    @BindString(R.string.key_location)   String KEY_LOCATION;
    @BindString(R.string.key_signature)  String KEY_SIGNATURE;
    @BindString(R.string.key_wb_open_id) String KEY_WB_OPEN_ID;
    @BindString(R.string.key_qq_open_id) String KEY_QQ_OPEN_ID;
    @BindString(R.string.key_wx_open_id) String KEY_WX_OPEN_ID;

    @BindColor(R.color.colorWhite)                   int      colorWhite;
    @BindColor(R.color.color_swipe)                  int      colorSwipe;
    @BindColor(R.color.colorGray999999)              int      colorGrey;
    @BindColor(R.color.colorRed)                     int      colorRed;
    @BindDrawable(R.mipmap.ic_membership_point_plus) Drawable DR_LEFT_MEMBERSHIP;
    @BindDrawable(R.mipmap.ic_arrow_right_small)     Drawable DR_RIGHT_MEMBERSHIP;

    private int mUserId;    // 用户Id

    private BadgeView mBvOrderUnpay;
    private BadgeView mBvOrderUnDeliver;
    private BadgeView mBvOrderUnReceive;
    private BadgeView mBvOrderAll;
    private boolean   mFirstCreated;    // 是否已经创建
    private Tracker   mTracker;         // GA Tracker

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_account, container, false);
        ButterKnife.bind(this, v);
        EventBus.getDefault().register(this);
        initVariables();
        initViews(v, savedInstanceState);
        loadData(false);
        return v;
    }

    public void initVariables() {
        mFirstCreated = true;
        // GA Tracker
        HaiApplication application = (HaiApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("我的");
    }

    protected void initViews(View v, Bundle savedInstanceState) {

        int redCircleRightOffset = DisplayUtils.dp2px(mActivity, 8);
        int redCircleTopOffset   = DisplayUtils.dp2px(mActivity, 3);
        // 代付款红点
        mBvOrderUnpay = new BadgeView(mActivity);
        mBvOrderUnpay.setTargetView(mTvOrderUnPay);
        mBvOrderUnpay.setBadgeCount(0);
        mBvOrderUnpay.setBadgeMargin(0, redCircleTopOffset, redCircleRightOffset, 0);
        // 待发货完成
        mBvOrderUnDeliver = new BadgeView(mActivity);
        mBvOrderUnDeliver.setTargetView(mTvOrderUnDeliver);
        mBvOrderUnDeliver.setBadgeCount(0);
        mBvOrderUnDeliver.setBadgeMargin(0, redCircleTopOffset, redCircleRightOffset, 0);
        // 待收货红点
        mBvOrderUnReceive = new BadgeView(mActivity);
        mBvOrderUnReceive.setTargetView(mTvOrderUnReceive);
        mBvOrderUnReceive.setBadgeCount(0);
        mBvOrderUnReceive.setBadgeMargin(0, redCircleTopOffset, redCircleRightOffset, 0);
        // 全部订单
        mBvOrderAll = new BadgeView(mActivity);
        mBvOrderAll.setTargetView(mTvOrderAll);
        mBvOrderAll.setBadgeCount(0);
        mBvOrderAll.setBadgeMargin(0, redCircleTopOffset, redCircleRightOffset, 0);
    }

    /**
     * 切换登录布局
     */
    private void switchLoginState() {
        //        mSwipe.setEnabled(HaiUtils.isLogin());
        if (!isLogin()) {
            mBvOrderUnDeliver.setBadgeCount(0);
            mBvOrderUnpay.setBadgeCount(0);
            mBvOrderUnReceive.setBadgeCount(0);
            mBvOrderAll.setBadgeCount(0);
            // 头像
            mAvatar.loadImg(R.mipmap.ic_avatar_default_large);
            // 立即登录
            mTvNickname.setText(isLogin() ? "" : "立即登录");
        }
        // 进入我的主页
        mTvEnterMyHomepage.setVisibility(isLogin() ? View.VISIBLE : View.GONE);
        // 优惠券张数
        mTvCouponCount.setVisibility(isLogin() ? View.VISIBLE : View.GONE);
        // 积分数
        mTvMembershipPoint.setVisibility(isLogin() ? View.VISIBLE : View.GONE);
    }


    /**
     * 读取数据
     */
    public void loadData(boolean showProgressDialog) {
        switchLoginState();
        if (!isLogin()) // 登录验证
            return;

        if (mFirstCreated || showProgressDialog) {
            showProgressDialog(true);
        }

        UserRepository.getInstance()
                .myInfo()
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        // 用户信息
                        userBean.setUserToken(UserRepository.getInstance().getUserInfo().getUserToken());
                        setMyInfoView(userBean);
                        UserRepository.getInstance()
                                .saveUserInfo(userBean);
                        mUserId = HaiUtils.getUserId();

                        // 设置用户佣金等信息
                        setUseStarCountView(userBean.getCommCounts());

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                        mFirstCreated = false;
                    }
                });
    }

    /**
     * 根据用户信息，更新UI
     *
     * @param userBean 用户信息
     */
    private void setMyInfoView(UserBean userBean) {
        String cornerUrl = null;
        if (userBean.getUserTitleList().size() != 0) {
            cornerUrl = userBean.getUserTitleList().get(0).getIconUrl();
        }
        mAvatar.loadImg(userBean.getHeadImg(), cornerUrl);
        mTvNickname.setText(userBean.getNickname());
    }

    /**
     * 读取用户佣金等信息
     */
    private void loadUserStarCount() {
        SnsRepository.getInstance()
                .getUserStarInfoCountsV12()
                .subscribe(new DefaultSubscriber<GetUserStarInfoCountsV12Result>() {
                    @Override
                    public void onSuccess(GetUserStarInfoCountsV12Result result) {
                        setUseStarCountView(result);
                    }


                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 填充用户佣金等UI
     */
    private void setUseStarCountView(GetUserStarInfoCountsV12Result result) {
        mBvOrderUnpay.setBadgeCount(result.ready_to_pay_counts);         // 待付款
        mBvOrderUnDeliver.setBadgeCount(result.ready_to_deliver_counts); // 待发货
        mBvOrderUnReceive.setBadgeCount(result.ready_to_receive_counts); // 待收货

        mTvCouponCount.setText(String.format("%d张可用", result.coupon_counts)); // 优惠券数
        mTvMembershipPoint.setText(String.valueOf(result.membership_point));    // 积分数

    }

    private void setUseStarCountView(CommCountsBean commCounts) {
        mBvOrderUnpay.setBadgeCount(commCounts.getReady_to_pay_counts());         // 待付款
        mBvOrderUnDeliver.setBadgeCount(commCounts.getReady_to_deliver_counts()); // 待发货
        mBvOrderUnReceive.setBadgeCount(commCounts.getReady_to_receive_counts()); // 待收货

        mTvCouponCount.setText(String.format("%d张可用", commCounts.getCoupon_counts())); // 优惠券数
        mTvMembershipPoint.setText(String.valueOf(commCounts.getMembership_point()));    // 积分数

    }

    /* bind listener */
    @OnClick({R.id.ib_message, R.id.rl_coupon, R.id.rl_membership_point,
            R.id.rl_wishlist, R.id.rl_my_album, R.id.rl_following_brand, R.id.rl_following_store})
    public void onClick(View view) {
        if (HaiUtils.needLogin(mActivity))
            return;
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ib_message: // 消息
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_Message, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_NoticeMessage + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_Message, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_NoticeMessage, "");

                intent = new Intent(mActivity, MessageActivity.class);
                break;
            case R.id.rl_wishlist: // 我的心愿单
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_MyWhisList, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyPray + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_MyWhisList, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyPray, "");

                MyWishListActivity.actionStart(mActivity);
                return;
            case R.id.rl_my_album: // 我的草单
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_MyEasy, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyAlbum + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_MyEasy, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyAlbum, "");

                MyEasyoptActivity.actionStart(mActivity);
                return;
            case R.id.rl_following_brand: // 关注的品牌
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_MyFollowBrandSite, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyFollowingBrand + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_MyFollowBrandSite, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyFollowingBrand, "");

                intent = new Intent(mActivity, BrandAndSellerActivity.class);
                intent.putExtra(KEY_TYPE, BrandSellerFragmentType.BRAND);
                intent.putExtra(KEY_USER_ID, mUserId);
                break;
            case R.id.rl_following_store: // 关注的商家
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_MyFollowBrandSite, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyFollowingStore + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_MyFollowBrandSite, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyFollowingStore, "");

                intent = new Intent(mActivity, BrandAndSellerActivity.class);
                intent.putExtra(KEY_TYPE, BrandSellerFragmentType.SELLER);
                intent.putExtra(KEY_USER_ID, mUserId);
                break;
            case R.id.rl_coupon: // 我的优惠券
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_MyCouponList, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyCoupon + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_MyCouponList, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyCoupon, "");

                intent = new Intent(mActivity, CouponListActivity.class);
                break;
            case R.id.rl_membership_point: // 我的积分
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_MyMembershipPoint, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyPoint + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_MyMembershipPoint, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyPoint, "");

                intent = new Intent(mActivity, MyMembershipPointActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    /**
     * 设置
     */
    @OnClick(R.id.ib_settings)
    public void clickSettings() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_UserInfoSet, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_Setting + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_UserInfoSet, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_Setting, "");

        Intent intent = new Intent(mActivity, SettingsActivity.class);
        intent.putExtra(KEY_USER_ID, mUserId);
        startActivity(intent);
    }


    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        loadData(true);
    }

    @Subscribe
    public void onUserInfoChangeEvent(UserInfoChangeEvent event) {
        loadData(false);
    }

    @Subscribe
    public void onUserStarChangeEvent(UserStartChangeEvent event) {
        loadUserStarCount();
    }

    /**
     * 订单状态改变
     */
    @Subscribe
    public void onOrderChangeEvent(OrderChangeEvent event) {
        loadUserStarCount();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * 跳转到我的订单页面
     */
    @OnClick({R.id.tv_order_unpay, R.id.tv_order_undeliver, R.id.tv_order_unreceive, R.id.tv_order_all})
    public void clickMyOrder(View view) {
        if (HaiUtils.needLogin(mActivity))
            return;

        String type = OrderFragmentType.ALL;
        String subCode = "";
        switch (view.getId()) {
            case R.id.tv_order_unpay: // 待付款
                type = OrderFragmentType.UNPAY;
                subCode = "1";
                break;
            case R.id.tv_order_undeliver: // 待发货
                type = OrderFragmentType.UNDELIVER;
                subCode = "2";
                break;
            case R.id.tv_order_unreceive: // 待收获
                type = OrderFragmentType.UNRECEIVE;
                subCode = "3";
                break;
            case R.id.tv_order_all: // 全部订单
                type = OrderFragmentType.ALL;
                subCode = "0";
                break;
        }
        // 埋点   SubCode:0：全部 1：待付款 2：待发货 3：待收货
//        TraceUtils.addClick(TraceUtils.PageCode_OrderList, subCode + "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyOrderList + "", "");

//        TraceUtils.addAnalysAct(TraceUtils.PageCode_OrderList, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_MyOrderList, subCode);

        OrderListActivity.actionStart(mActivity, type);
    }

    // 跳转到个人主页
    @OnClick(R.id.ll_user_info)
    public void clickUserInfo() {
        if (!needLogin(mActivity)) {
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_HomePage, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_User + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_User, "");

            MyHomePageActivity.toThisActivity(mActivity);
        }
    }

    private boolean needLogin(Context context) {
        if (!isLogin()) {
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_LoginMobile, "", mActivity, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_Login + "", "");

//            TraceUtils.addAnalysAct(TraceUtils.PageCode_LoginMobile, TraceUtils.PageCode_MyAccount, TraceUtils.PositionCode_Login, "");

            context.startActivity(new Intent(context, LoginMobileActivity.class)); // 如果没有登录,则跳到登录页面
            return true;
        }
        return false;
    }

    /**
     * 联系客服
     */
    @OnClick(R.id.rl_contact_service)
    public void clickContactService() {
        NtalkerUtils.contactKF(mActivity, true);
    }

}
