package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.myaccount.MyHomePagePagerAdapter;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.event.HomePageRefreshEvent;
import com.a55haitao.wwht.data.event.PostChangeEvent;
import com.a55haitao.wwht.data.event.UserInfoChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.FollowFansFragmentType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.view.AvatarPopupWindow;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadManager;
import com.upyun.library.listener.SignatureListener;
import com.upyun.library.utils.UpYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

import static com.a55haitao.wwht.R.id.img_avatar;

/**
 * 我的主页
 */
public class MyHomePageActivity extends BaseHasFragmentActivity {

    @BindView(R.id.msv)                MultipleStatusView msView;            // 多状态布局
    @BindView(R.id.title)              DynamicHeaderView  mTitle;            // 标题
    @BindView(R.id.tv_nickname)        HaiTextView        mTvNickname;       // 昵称
    @BindView(R.id.tv_user_title)      HaiTextView        mTvUserTitle;      // user_title
    @BindView(R.id.tv_following_count) HaiTextView        mTvFollowingCount; // 关注数
    @BindView(R.id.tv_follower_count)  HaiTextView        mTvFollowerCount;  // 粉丝数
    @BindView(R.id.tv_like_count)      HaiTextView        mTvLikeCount;      // 点赞数
    @BindView(img_avatar)              AvatarView         mImgAvatar;        // 头像
    @BindView(R.id.tab)                TabLayout          mTab;              // tab
    @BindView(R.id.vp_content)         ViewPager          mVpContent;        // 内容
    @BindView(R.id.appBar)             AppBarLayout       mAppBar;           // AppBarLayout
    @BindView(R.id.tv_signature)       HaiTextView        mTvSignature;      // 用户签名
    @BindView(R.id.ll_user_info)       LinearLayout       mLlUserInfo;

    @BindDimen(R.dimen.avatar_my_homepage) int AVATAR_SIZE; // 头像尺寸

    private static final int    REQUEST_CODE_GALLERY = 1001; // 选择照片
    private static final String KEY                  = "MJrxn7x0fP5c54QFfb0GlFiJ9pY=";
    private static final String SPACE                = "st-prod";
    private static final String savePath             = "/avatar/{year}/{mon}/{day}/{random32}";

    private String            mNickname;
    private AvatarPopupWindow mAvatarPopupWindow;
    private String            mLocalHeadImgPath; // 用户选择头像
    private String            mNewHeadImgPath;
    private String            mCornerUrl;
    private Tracker           mTracker; // GA Tracker
    private ToastPopuWindow   mPwToast;
    private FunctionConfig mFunctionConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home_page);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    private void initVariables() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mNickname = "";
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("个人主页");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setCropSquare(true)
                .setForceCrop(true)
                .setForceCropEdit(false)
                .build();

    }

    private void initViews(Bundle savedInstanceState) {
        mTitle.setHeadClickListener(new DynamicHeaderView.OnHeadViewClickListener() {
            @Override
            public void onRightImgClick() {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_UserInfoSet, "", mActivity, TraceUtils.PageCode_HomePage, TraceUtils.PositionCode_EditInfomation + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_UserInfoSet, TraceUtils.PageCode_HomePage, TraceUtils.PositionCode_EditInfomation, "");
                UserInfoSettingsActivity.toThisActivity(mActivity);
            }
        });
        msView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新用户信息
                MyHomePageActivity.this.loadMyInfo();
                // 通知ViewPager更新
                EventBus.getDefault().post(new HomePageRefreshEvent(true));
            }
        });
        mAppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            mTitle.setHeadTitle(Math.abs(verticalOffset) >= mLlUserInfo.getMeasuredHeight() ?
                    mNickname : "");
        });
        mVpContent.setAdapter(new MyHomePagePagerAdapter(getSupportFragmentManager(), HaiUtils.getUserId()));
        mTab.setupWithViewPager(mVpContent);
        loadMyInfo();
        //        setUserInfoView(UserRepository.getInstance().getUserInfo());
    }

    /**
     * 读取用户信息
     */
    private void loadMyInfo() {
        msView.showLoading();
        refreshData();
    }

    private void refreshData() {
        UserRepository.getInstance()
                .myInfo()
                .compose(bindToLifecycle())
                .subscribe(new DefaultSubscriber<UserBean>() {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        msView.showContent();
                        userBean.setUserToken(UserRepository.getInstance().getUserInfo().getUserToken());
                        UserRepository.getInstance()
                                .saveUserInfo(userBean);
                        // 更新View
                        setUserInfoView(userBean);
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    /**
     * 设置个人信息View
     *
     * @param userBean 个人信息
     */
    private void setUserInfoView(UserBean userBean) {
        // 头像
        /*Glide.with(mActivity)
                .load(UpyUrlManager.getUrl(userBean.getHeadImg(), AVATAR_SIZE))
                .transform(new GlideCircleTransform(mActivity))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.ic_avatar_default_ultra)
                .into(mImgAvatar);*/

        if (userBean.getUserTitleList().size() != 0) {
            mCornerUrl = userBean.getUserTitleList().get(0).getIconUrl();
        }
        mImgAvatar.loadImg(userBean.getHeadImg(), mCornerUrl);
        // 昵称
        mNickname = userBean.getNickname();
        mTvNickname.setText(mNickname);
        // user_title
        if (userBean.getUserTitleList().size() != 0) {
            mTvUserTitle.setVisibility(View.VISIBLE);
            mTvUserTitle.setText(userBean.getUserTitleList().get(0).getTitle());
        } else {
            mTvUserTitle.setVisibility(View.INVISIBLE);
        }
        // 关注数
        mTvFollowingCount.setText(String.format("%d关注", userBean.getFollowingCount()));
        // 粉丝数
        mTvFollowerCount.setText(String.format("%d粉丝", userBean.getFollowerCount()));
        // 赞数
        mTvLikeCount.setText(String.format("%d点赞", userBean.getLikeCount()));
        // 笔记数
        mTab.getTabAt(0).setText(String.format("笔记 · %d", userBean.getPostCount()));
        // 点赞数
        if (userBean.getCommCounts() != null) {
            mTab.getTabAt(1).setText(String.format("点赞 · %d", userBean.getCommCounts().getStar_post_counts()));
        }
        // 签名
        String signature = userBean.getSignature();
        if (TextUtils.isEmpty(signature)) {
            mTvSignature.setVisibility(View.INVISIBLE);
        } else {
            mTvSignature.setVisibility(View.VISIBLE);
            mTvSignature.setText(signature);
        }
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     */
    public static void toThisActivity(Context context) {
        context.startActivity(new Intent(context, MyHomePageActivity.class));
    }

    /**
     * 跳转到用户信息设置界面
     */
    @OnClick(img_avatar)
    public void clickAvatar() {
        mAvatarPopupWindow = new AvatarPopupWindow(mActivity, UserRepository.getInstance().getUserInfo().getHeadImg(), true);
        mAvatarPopupWindow.setOnChangeAvatarListener(() -> {
            mAvatarPopupWindow.dismiss();
            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mFunctionConfig, mOnHanlderResultCallback);

        });
        mTitle.postDelayed(() -> mAvatarPopupWindow.showOrDismiss(mTitle), 200);
    }

    /**
     * 图片选择回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
            if (resultList != null) {
                mLocalHeadImgPath = TextUtils.isEmpty(resultList.get(0).getCropPhotoPath()) ? resultList.get(0).getPhotoPath() : resultList.get(0).getCropPhotoPath();
                compress();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };

    /**
     * 压缩图片后上传又拍云
     */
    private void compress() {
        File file        = new File(mLocalHeadImgPath);
        long fileContent = file.length();
        if (fileContent > 200 * 1024) {
            Luban.get(mActivity)
                    .load(file)
                    .putGear(Luban.THIRD_GEAR)
                    .asObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<File>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(File file) {
                            mLocalHeadImgPath = file.getAbsolutePath();
                            requestUpyunUpload();
                        }
                    });
        } else {
            requestUpyunUpload();
        }
    }

    /**
     * 又拍云上传图片
     */
    private void requestUpyunUpload() {

        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, SPACE);
        paramsMap.put(Params.SAVE_KEY, savePath);

        UploadManager.getInstance().formUpload(new File(mLocalHeadImgPath),
                paramsMap,
                signatureListener,
                (isSuccess, result) -> {
                    //                    dismissProgressDialog();
                    Logger.t(TAG).d(result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.optInt("code") == 200) {
                            //                            mNewHeadImgPath = ServerUrl.UPYUN_BASE + jsonObject.optString("small_icon");
                            mNewHeadImgPath = ServerUrl.UPYUN_BASE + jsonObject.optString("url");
                            requestSaveUserInfo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (bytesWrite, contentLength) -> {
                });

    }

    SignatureListener signatureListener = raw -> UpYunUtils.md5(raw + KEY);

    /**
     * 保存个人信息
     */
    private void requestSaveUserInfo() {
        UserBean userBean = UserRepository.getInstance().getUserInfo();
        UserRepository.getInstance()
                .updateProfile(mNickname,
                        userBean.getSex(),
                        userBean.getLocation(),
                        userBean.getSignature(),
                        mNewHeadImgPath)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean result) {
                        EventBus.getDefault().post(new UserInfoChangeEvent());
                        if (result.membership_point > 0) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, result.membership_point, AlterPointViewType.AlterPointViewType_UploadAvatar).parentView(mImgAvatar);
                        } else {
                            mPwToast = ToastPopuWindow.makeText(mActivity, "更新成功", AlertViewType.AlertViewType_OK).parentView(mImgAvatar);
                        }
                        mPwToast.show();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 关注 & 粉丝
     *
     * @param view
     */
    @OnClick({R.id.tv_following_count, R.id.tv_follower_count})
    public void onClick(View view) {
        @FollowFansFragmentType int type = FollowFansFragmentType.FOLLOW;
        if (view.getId() == R.id.tv_follower_count) {
            type = FollowFansFragmentType.FANS;
        }

        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_FansOrFollows, "", mActivity, TraceUtils.PageCode_HomePage, type == FollowFansFragmentType.FANS ? TraceUtils.PositionCode_Follower + "" : TraceUtils.PositionCode_Following + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_FansOrFollows, TraceUtils.PageCode_HomePage, type == FollowFansFragmentType.FANS ? TraceUtils.PositionCode_Follower : TraceUtils.PositionCode_Following, "");

        FollowAndFansActivity.toThisActivity(mActivity, type, HaiUtils.getUserId());
    }

    /**
     * 用户信息更新
     */
    @Subscribe
    public void onUserInfoChangeEvent(UserInfoChangeEvent event) {
        refreshData();
    }

    /**
     * 用户信息更新
     */
    @Subscribe
    public void onPostChangeEvent(PostChangeEvent event) {
        //        loadMyInfo();
        refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }
}
