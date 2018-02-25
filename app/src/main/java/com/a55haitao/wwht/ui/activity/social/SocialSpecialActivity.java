package com.a55haitao.wwht.ui.activity.social;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.CommentListAdapter;
import com.a55haitao.wwht.adapter.social.SpecialDetailAdapter;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.CommentActivityType;
import com.a55haitao.wwht.data.model.entity.CommentBean;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.data.model.result.AddPostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.GetPostSpecialInfoResult;
import com.a55haitao.wwht.data.model.result.LikePostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.LikePostSpecialResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SpecialRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.PostLikeButton;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.GlideCircleTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;
import com.varunest.sparkbutton.SparkButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 社区专题详情
 */
@MLinkRouter(keys = {"postSpecialKey"})
public class SocialSpecialActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)              DynamicHeaderView  mTitle;           // 标题
    @BindView(R.id.content_view)       RecyclerView       mRvContent;       // 内容
    @BindView(R.id.et_comment_content) EditText           mEtCommentContent;// 评论内容
    @BindView(R.id.tv_send_comment)    HaiTextView        mTvSendComment;   // 发表评论
    @BindView(R.id.msv)                MultipleStatusView mSv;              // 多状态布局

    @BindDimen(R.dimen.avatar_medium)             int AVATAR_SIZE;             // 头像尺寸
    @BindDimen(R.dimen.margin_medium)             int MARGIN_MEDIUM;             // margin
    @BindDimen(R.dimen.show_detail_avatar_margin) int SHOW_DETAIL_AVATAR_MARGIN; // margin

    private SpecialDetailAdapter     mAdapter;
    private ImageView                mImgBanner;
    private TextView                 mTvTitle;
    private TextView                 mTvCount;
    private int                      mScreenWidth;
    private PostLikeButton           mLikeButton;
    private LinearLayout             mLlAvatarContainer;
    private TextView                 mTvNoLike;
    private TextView                 mTvCommentCount;
    private RecyclerView             mRvComment;
    private TextView                 mTvShowAllComments;
    private TextView                 mTvNoComments;
    private Dialog                   mDialog;
    private InputMethodManager       mImm;
    private boolean                  isCommentIng;
    private int                      mSpecialId;
    private int                      mMaxLikesAvatarCount;
    private CommentListAdapter       mCommentListAdapter;
    private List<CommentBean>        mComments;
    private int                      mCommentId;    // 评论Id
    private GetPostSpecialInfoResult mSpecialInfo;
    private Tracker                  mTracker;      // GA Tracker
    private ToastPopuWindow          mPwToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_special);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            // 魔窗获取字段
            String mwSpecialId = intent.getStringExtra("specialid");
            if (TextUtils.isEmpty(mwSpecialId)) {
                mSpecialId = getIntent().getIntExtra("special_id", -1);
            } else {
                mSpecialId = Integer.valueOf(mwSpecialId);
            }
        }
        Logger.d(mSpecialId);
        //        mSpecialId = getIntent().getIntExtra("special_id", -1);
        calcBannerHeight();
        mMaxLikesAvatarCount = calcLikeAvatarCount();
        mImm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //        EventBus.getDefault().register(this);
        mComments = new ArrayList<>();
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("社区专题");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /**
     * 计算Banner高度
     */
    private int calcBannerHeight() {
        mScreenWidth = DisplayUtils.getScreenWidth(mActivity);
        float whRate = 750 * 1.0f / 400;
        return (int) (mScreenWidth / whRate);
    }

    /**
     * 计算最大点赞头像个数
     */
    private int calcLikeAvatarCount() {
        return (mScreenWidth - MARGIN_MEDIUM * 2 + SHOW_DETAIL_AVATAR_MARGIN) / (AVATAR_SIZE + SHOW_DETAIL_AVATAR_MARGIN) - 1;
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        mTitle.setHeadClickListener(() -> {
            if (mSpecialInfo == null || mSpecialInfo.share == null)
                return;
            // 分享
            ShareUtils.showShareBoard(mActivity,
                    mSpecialInfo.share.title,
                    mSpecialInfo.share.desc,
                    mSpecialInfo.share.url,
                    mSpecialInfo.share.icon,
                    true);
        });
        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SpecialDetailAdapter(mActivity, null);
        // headerView
        initHeaderView();
        // footerView
        initFooterView();
        // setAdatper
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_product: // 跳转到商品详情
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_ProductDetail, mSpecialInfo.content.get(position).data.spuid, SocialSpecialActivity.this, TraceUtils.PageCode_SocialDetail, TraceUtils.PositionCode_Product + "", "");
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Goods_Id, mSpecialInfo.content.get(position).data.spuid);
                        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                        ProductMainActivity.toThisAcivity(mActivity, mSpecialInfo.content.get(position).data.spuid, mSpecialInfo.content.get(position).data.name);
                        break;
                }
            }
        });
        // 重试
        mSv.setOnRetryClickListener(v -> loadData());
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.header_activity_special_detail, null);
        mImgBanner = (ImageView) headerView.findViewById(R.id.img_banner);
        mTvTitle = (HaiTextView) headerView.findViewById(R.id.tv_title);
        mTvCount = (HaiTextView) headerView.findViewById(R.id.tv_count);
        mAdapter.addHeaderView(headerView);

        // 适配banner图尺寸
        ViewGroup.LayoutParams lp = mImgBanner.getLayoutParams();
        lp.width = mScreenWidth;
        lp.height = calcBannerHeight();
        mImgBanner.setLayoutParams(lp);
    }

    /**
     * 初始化尾布局
     */
    private void initFooterView() {
        View footerView = getLayoutInflater().inflate(R.layout.footer_activity_special_detail, null);
        mLikeButton = (PostLikeButton) footerView.findViewById(R.id.like_button);
        mLlAvatarContainer = (LinearLayout) footerView.findViewById(R.id.ll_avator_container);
        mTvNoLike = (HaiTextView) footerView.findViewById(R.id.tv_no_like);
        mTvCommentCount = (HaiTextView) footerView.findViewById(R.id.tv_comment_count);
        mRvComment = (RecyclerView) footerView.findViewById(R.id.rv_comment);
        mTvShowAllComments = (HaiTextView) footerView.findViewById(R.id.tv_show_all_comments);
        mTvNoComments = (HaiTextView) footerView.findViewById(R.id.tv_no_comments);

        mAdapter.addFooterView(footerView);

        // 跳转到全部点赞页面
        mLlAvatarContainer.setOnClickListener(v -> {
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_PostLikeList, mSpecialId + "", this, TraceUtils.PageCode_SocialDetail, TraceUtils.PositionCode_ViewLikingUsers + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_PostLikeList, TraceUtils.PageCode_SocialDetail, TraceUtils.PositionCode_ViewLikingUsers, "");

            Intent intent = new Intent(mActivity, LikeListActivity.class);
            intent.putExtra("special_id", mSpecialId);
            startActivity(intent);
        });

        mRvComment.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mCommentListAdapter = new CommentListAdapter(null);
        mRvComment.setAdapter(mCommentListAdapter);

        mRvComment.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCommentId = mCommentListAdapter.getData().get(position).id;

                isCommentIng = !isCommentIng;

                if (isCommentIng) {
                    mEtCommentContent.setHint("回复 " + mCommentListAdapter.getData().get(position).user_info.nickname + ":");
                    mImm.showSoftInput(mEtCommentContent, 0);
                } else {
                    mEtCommentContent.setHint("请输入您的评论内容");
                    mImm.hideSoftInputFromWindow(mEtCommentContent.getWindowToken(), 0);
                }
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mCommentListAdapter.getData().get(position).user_info.id == HaiUtils.getUserId()) {
                    showDialog(mCommentListAdapter.getData().get(position).id, position);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_avatar:
                    case R.id.tv_nickname:
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_HomePage, mCommentListAdapter.getData().get(position).user_info.id + "", SocialSpecialActivity.this, TraceUtils.PageCode_SocialDetail, TraceUtils.PositionCode_User + "", "");

                        //                        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_PostLikeList, TraceUtils.PositionCode_User, mCommentListAdapter.getData().get(position).user_info.id + "");

                        OthersHomePageActivity.actionStart(mActivity, mCommentListAdapter.getData().get(position).user_info.id);
                        break;
                    case R.id.sb_like:
                        SparkButton sb = (SparkButton) view;
                        TextView tvLikeCount = (TextView) ((ViewGroup) sb.getParent()).findViewById(R.id.tv_like_count);
                        if (HaiUtils.needLogin(mActivity)) {
                            sb.setChecked(mCommentListAdapter.getData().get(position).is_liked);
                        } else {
                            requestLikeComment(mCommentListAdapter.getData().get(position).id, sb, tvLikeCount);
                        }
                        break;
                }
            }
        });

    }

    /**
     * 加载数据
     */
    public void loadData() {
        mSv.showLoading();
        requestContent();
    }

    /**
     * 网络请求
     */
    private void requestContent() {
        SpecialRepository.getInstance()
                .getPostSpecialInfo(mSpecialId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetPostSpecialInfoResult>() {
                    @Override
                    public void onSuccess(GetPostSpecialInfoResult specialInfo) {
                        mSv.showContent();

                        mSpecialInfo = specialInfo;
                        //                        mCommentListAdapter.setNewData(mSpecialInfo.comments);
                        setHeaderView(mSpecialInfo);
                        mAdapter.setNewData(mSpecialInfo.content);
                        setFooterView(mSpecialInfo);
                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 填充头布局
     *
     * @param specialInfo 专题数据
     */
    private void setHeaderView(GetPostSpecialInfoResult specialInfo) {
        // Banner图
        Glide.with(mActivity)
                .load(UpyUrlManager.getUrl(specialInfo.img_cover, mScreenWidth))
                .placeholder(R.mipmap.ic_default_rect)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into(mImgBanner);
        // 标题
        mTvTitle.setText(specialInfo.name);
        // 点赞评论数目
        mTvCount.setText(String.format("%d 个赞，%d 条评论", specialInfo.like_count, specialInfo.reply_count));
    }

    /**
     * 填充底布局
     *
     * @param specialInfo 专题数据
     */
    private void setFooterView(GetPostSpecialInfoResult specialInfo) {
        // 点赞按钮
        //        Logger.d(result.is_liked);
        mLikeButton.setCheckedNoAnim(specialInfo.is_liked);
        mLikeButton.setLikeCount(specialInfo.like_count);
        // 点赞监听
        mLikeButton.setLikeListener(isChecked -> {
            if (HaiUtils.needLogin(mActivity)) {
                mLikeButton.setChecked(mLikeButton.isChecked());
                return;
            }
            requestLikeSpecial(specialInfo.special_id);
        });
        // 点赞头像
        setLikeListView(specialInfo.likes);
        // 评论数目
        mTvCommentCount.setText(String.format("评论(%d)", specialInfo.reply_count));
        // 没有评论
        mTvNoComments.setVisibility(specialInfo.reply_count == 0 ? View.VISIBLE : View.GONE);
        // 评论列表
        mCommentListAdapter.setNewData(specialInfo.reply_count > 5 ? specialInfo.comments.subList(0, 5) : specialInfo.comments);
        // 查看所有评论
        mTvShowAllComments.setVisibility(specialInfo.reply_count > 5 ? View.VISIBLE : View.GONE);
        // 跳转到所有评论页面
        if (mTvShowAllComments.getVisibility() == View.VISIBLE) {
            mTvShowAllComments.setOnClickListener(v -> {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_CommentsList, specialInfo.special_id + "", SocialSpecialActivity.this, TraceUtils.PageCode_SocialDetail, TraceUtils.PositionCode_ViewComments + "", "");

//                TraceUtils.addAnalysAct(TraceUtils.PageCode_CommentsList, TraceUtils.PageCode_SocialDetail, TraceUtils.PositionCode_ViewComments, "");

                CommentListActivity.toThisActivity(mActivity, CommentActivityType.SPECIAL, specialInfo.special_id);
            });
        }
    }

    /**
     * 点赞评论请求
     *
     * @param commentId   评论Id
     * @param sb          点赞按钮
     * @param tvLikeCount 点赞数
     */
    private void requestLikeComment(int commentId, SparkButton sb, TextView tvLikeCount) {
        SpecialRepository.getInstance()
                .likePostSpecialComment(commentId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<LikePostSpecialCommentResult>() {
                    @Override
                    public void onSuccess(LikePostSpecialCommentResult result) {
                        sb.setChecked(result.liked);
                        tvLikeCount.setText(result.like_count > 0 ? String.valueOf(result.like_count) : "");
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 点赞专题
     *
     * @param specialId 专题Id
     */
    private void requestLikeSpecial(int specialId) {
        SpecialRepository.getInstance()
                .likePostSpecial(specialId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<LikePostSpecialResult>() {
                    @Override
                    public void onSuccess(LikePostSpecialResult result) {
                        if (result.liked) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, result.membership_point, AlterPointViewType.AlterPointViewType_Like).parentView(mTvSendComment);
                            mPwToast.show();
                        }
                        //                        mLikeButton.setChecked(result.liked);
                        //                        mLikeButton.setLikeCount(result.like_count);
                        requestContent();

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 填充点赞头像列表UI
     *
     * @param userList 点赞列表
     */
    private void setLikeListView(List<UserListBean> userList) {
        int count = 0;
        mLlAvatarContainer.removeAllViews();
        // 没有人点赞
        mTvNoLike.setVisibility(userList.size() == 0 ? View.VISIBLE : View.GONE);

        for (int i = 0, len = userList.size(); i < len; i++) {
            UserListBean user   = userList.get(i);
            ImageView    avatar = new ImageView(mActivity);

            Glide.with(mActivity)
                    .load(count < mMaxLikesAvatarCount ? UpyUrlManager.getUrl(user.head_img, AVATAR_SIZE) : R.mipmap.ic_points)
                    .placeholder(R.mipmap.ic_avatar_default_small)
                    .transform(new GlideCircleTransform(mActivity))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(avatar);

            LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(
                    AVATAR_SIZE, AVATAR_SIZE);

            if (i != 0) {
                avatarParams.leftMargin = SHOW_DETAIL_AVATAR_MARGIN;
            }

            avatar.setLayoutParams(avatarParams);

            mLlAvatarContainer.addView(avatar);
            if (++count > mMaxLikesAvatarCount) {
                break;
            }
        }
    }


    /**
     * 发表评论请求
     */
    public void requestSendComment(int specialId, String content, int commentId) {
        SpecialRepository.getInstance()
                .addPostSpecialComment(specialId, content, commentId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<AddPostSpecialCommentResult>() {
                    @Override
                    public void onSuccess(AddPostSpecialCommentResult addPostSpecialCommentResult) {
                        mEtCommentContent.setText("");
                        mImm.hideSoftInputFromWindow(mEtCommentContent.getWindowToken(), 0);
                        mPwToast = ToastPopuWindow.makeText(mActivity, addPostSpecialCommentResult.membership_point, AlterPointViewType.AlterPointViewType_Comment).parentView(mTvSendComment);
                        mPwToast.show();
                        // 评论完之后刷新数据
                        requestContent();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 删除评论网络请求
     *
     * @param commentId 评论id
     */
    private void requestDeleteComment(int commentId) {
        SpecialRepository.getInstance()
                .deletePostSpecialComment(commentId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<DeletePostSpecialCommentResult>() {
                    @Override
                    public void onSuccess(DeletePostSpecialCommentResult result) {
                        if (result.success) {
                            ToastUtils.showToast(mActivity, "已删除");
                            requestContent();
                        }
                    }

                    @Override
                    public void onFinish() {
                        mDialog.dismiss();
                    }
                });
    }


    /**
     * 弹出删除评论确认对话框
     *
     * @param id       评论id
     * @param position 位置
     */
    private void showDialog(int id, int position) {
        mDialog = new Dialog(mActivity);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View     v    = LayoutInflater.from(mActivity).inflate(R.layout.dlg_single_choice, null);
        TextView desc = (HaiTextView) v.findViewById(R.id.tv_desc);
        desc.setText("删除评论");
        desc.setOnClickListener(view -> requestDeleteComment(id));
        mDialog.setContentView(v);
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        //        EventBus.getDefault().unregister(this);
        super.onDestroy();
        UMShareAPI.get(this).release();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id = " + mSpecialId;
    }

    public static void toThisActivity(Context context, int specialId) {
        toThisActivity(context, specialId, false);
    }

    public static void toThisActivity(Context context, int specialId, boolean newTask) {
        Intent intent = new Intent(context, SocialSpecialActivity.class);
        intent.putExtra("special_id", specialId);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    /**
     * 发送评论
     */
    @OnClick(R.id.tv_send_comment)
    public void clickSendComment() {
        if (HaiUtils.needLogin(mActivity))
            return;

        String content = mEtCommentContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showToast(mActivity, "评论内容不能为空");
        } else {
            showProgressDialog();
            requestSendComment(mSpecialId, content, mCommentId);
        }
    }
}
