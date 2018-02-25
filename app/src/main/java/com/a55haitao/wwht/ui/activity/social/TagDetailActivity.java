package com.a55haitao.wwht.ui.activity.social;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.PostListTwoColumnAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.PostChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.TagDetailPostType;
import com.a55haitao.wwht.data.model.entity.DraftPostBean;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.data.model.result.GetPostTagResult;
import com.a55haitao.wwht.data.model.result.GetTagHotPostHotListResult;
import com.a55haitao.wwht.data.model.result.LikePostResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;
import com.varunest.sparkbutton.SparkButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.magicwindow.mlink.annotation.MLinkRouter;

/**
 * 标签详情 页
 */
@MLinkRouter(keys = {"hotTagKey"})
public class
TagDetailActivity extends BaseHasFragmentActivity {

    @BindView(R.id.title)          DynamicHeaderView     mTitle;        // 标题
    @BindView(R.id.content_view)   RecyclerView          mRvContent;    // 帖子列表
    @BindView(R.id.swipe)          HaiSwipeRefreshLayout mSwipe;
    @BindView(R.id.msv)            MultipleStatusView    mSv;
    @BindView(R.id.llyt_send_post) LinearLayout          llytSendPost;

    @BindDimen(R.dimen.margin_big) int mRvSpacing;

    private int                      mTagId;        // 标签Id
    private String                   mTagName;      // 标签名
    private int                      mCurrentPage;
    private int                      mAllPage;
    private PostListTwoColumnAdapter mAdapter;
    private ImageView                mImgBanner;    // Banner图
    private TextView                 mTvTitle;      // 标题
    private TextView                 mTvDesc;       // 描述
    private CheckBox                 mChkHot;       // 热门
    private CheckBox                 mChkLatest;    // 最新
    private int                      mScreenWidth;  // 屏幕宽度
    private List<PostBean>           mPostList;     // 帖子列表
    private GetPostTagResult         mTagInfo;      // 标签信息
    private int                      mIsHot;        // 是否是热门标签
    @TagDetailPostType
    private int                      mType;         // 0 热门 1 最新
    private Tracker                  mTracker;      // GA Tracker
    private ToastPopuWindow          mPwToast;

    private FunctionConfig mFunctionConfig;
    private static final int REQUEST_CODE_GALLERY = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    private void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            // 魔窗获取字段
            String mwTagId = intent.getStringExtra("tagid");
            if (TextUtils.isEmpty(mwTagId)) {
                mTagId = getIntent().getIntExtra("tag_id", -1);
                mIsHot = intent.getIntExtra("is_hot", 0);
            } else {
                mTagId = Integer.valueOf(mwTagId);
                mIsHot = 1;
            }
            mTagName = intent.getStringExtra("tag_name");
            mTitle.setHeadTitle(mTagName);
        }
        mCurrentPage = 1;
        mAllPage = 1;
        mPostList = new ArrayList<>();
        // 屏幕宽度
        mScreenWidth = DisplayUtils.getScreenWidth(mActivity);
        // 是否热门
        if (mIsHot != 0) {
            mType = TagDetailPostType.HOT;
        } else {
            mType = TagDetailPostType.LATEST;
        }
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(mIsHot != 0 ? "标签详情_热门" : "标签详情_普通");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // EventBus
        EventBus.getDefault().register(this);

        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setMutiSelectMaxSize(9)
                .setEnablePreview(false)
                .build();

    }

    /**
     * 计算Banner高度
     */
    private int calcBannerHeight() {
        return (int) (mScreenWidth / (750 * 1.0f / 300));
    }

    private void initViews(Bundle savedInstanceState) {
        if (mIsHot == 0) {
            mTitle.setHeaderRightHidden();
        } else {
            mTitle.setHeadClickListener(() -> {
                // 分享
                ShareUtils.showShareBoard(mActivity,
                        mTagInfo.share.title,
                        mTagInfo.share.desc,
                        mTagInfo.share.url,
                        mTagInfo.share.icon,
                        false);
            });
        }
        mRvContent.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new PostListTwoColumnAdapter(mPostList, this);

        if (mIsHot == 1) {
            // 头布局
            initHeaderView();
            // 热门
            mChkHot.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mChkLatest.setChecked(!isChecked);
                if (isChecked) {
                    mType = TagDetailPostType.HOT;
                    refreshPostList();
                }
                mChkHot.setEnabled(!isChecked);
                mChkLatest.setEnabled(isChecked);
            });
            // 最新
            mChkLatest.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mChkHot.setChecked(!isChecked);
                if (isChecked) {
                    mType = TagDetailPostType.LATEST;
                    refreshPostList();
                }
                mChkHot.setEnabled(isChecked);
                mChkLatest.setEnabled(!isChecked);
            });
        }

        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_PostDetail, mAdapter.getData().get(position).post_id + "", TagDetailActivity.this, TraceUtils.PageCode_HotTagDetail, TraceUtils.PositionCode_Post + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_PostDetail, TraceUtils.PageCode_HotTagDetail, TraceUtils.PositionCode_Post, mAdapter.getData().get(position).post_id + "");

                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mAdapter.getData().get(position).post_id);
                intent.putExtra("wh_rate", mAdapter.getData().get(position).images.get(0).wh_rate);
                startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PostBean post = mAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.img_avatar:
                    case R.id.tv_nickname:
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_HomePage, post.owner.id + "", TagDetailActivity.this, TraceUtils.PageCode_HotTagDetail, TraceUtils.PositionCode_User + "", "");

                        //                        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_HotTagDetail, TraceUtils.PositionCode_User, post.owner.id + "");

                        OthersHomePageActivity.actionStart(mActivity, post.owner.id);
                        break;
                    case R.id.sb_like:
                        SparkButton sb = (SparkButton) view;
                        TextView tvLikeCount = (TextView) ((ViewGroup) sb.getParent()).findViewById(R.id.tv_like_count);
                        if (HaiUtils.needLogin(mActivity)) {
                            sb.setChecked(post.is_liked);
                        } else {
                            requestLikePost(post.post_id, sb, tvLikeCount, position);
                        }
                        break;
                }
            }
        });

        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                if (mAdapter.getData().size() < PAGE_SIZE) {
                    mAdapter.loadMoreEnd(true);
                } else if (mCurrentPage < mAllPage) {
                    mCurrentPage++;
                    if (mType == TagDetailPostType.HOT) {
                        requestHotPost();
                    } else {
                        requestLatestPost();
                    }
                }
            });
        });
        // 刷新
        mSwipe.setOnRefreshListener(this::refreshData);
        // 重新加载
        mSv.setOnRetryClickListener(v -> loadData());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        dismissProgressDialog();
    }

    /**
     * 头布局
     */
    private void initHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.header_activity_tag_detail, null);
        mAdapter.addHeaderView(headerView);

        mImgBanner = (ImageView) headerView.findViewById(R.id.img_banner);
        mTvTitle = (HaiTextView) headerView.findViewById(R.id.tv_title);
        mTvDesc = (HaiTextView) headerView.findViewById(R.id.tv_desc);
        mChkHot = (CheckBox) headerView.findViewById(R.id.chk_hot);
        mChkLatest = (CheckBox) headerView.findViewById(R.id.chk_latest);

        ViewGroup.LayoutParams lp = mImgBanner.getLayoutParams();
        lp.width = mScreenWidth;
        lp.height = calcBannerHeight();
        mImgBanner.setLayoutParams(lp);
    }

    public void refreshData() {
        if (!mSwipe.isRefreshing()) {
            mSwipe.post(() -> mSwipe.setRefreshing(true));
        }
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

    public void refreshPostList() {
        if (!mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(true);
        }
        mAdapter.setEnableLoadMore(false);
        loadPostList();
    }

    private void loadData() {
        if (mTagId == -1)
            return;

        if (mIsHot == 1) {
            requestTagInfo();
        }
        loadPostList();
    }

    /**
     * 请求帖子列表
     */
    public void loadPostList() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;

        if (mType == TagDetailPostType.HOT) {
            requestHotPost();
        } else {
            requestLatestPost();
        }
    }

    /**
     * 请求热门帖子列表
     */
    public void requestHotPost() {
        SnsRepository.getInstance()
                .getTagHotPostHotList(mTagId, mCurrentPage)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetTagHotPostHotListResult>() {
                    @Override
                    public void onSuccess(GetTagHotPostHotListResult result) {
                        mSv.showContent();
                        mAllPage = result.allpage;

                        if (result.count == 0) { // 数据为空
                            if (mSwipe.isRefreshing()) {
                                //                                mAdapter.getData().clear();
                                mAdapter.setNewData(null);
                            }
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.post_list);
                            } else {
                                mAdapter.addData(result.post_list);
                                if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= result.count) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        }
                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }

    /**
     * 请求最新帖子列表
     */
    public void requestLatestPost() {
        SnsRepository.getInstance()
                .getTagPostList(mTagId, mCurrentPage)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetTagHotPostHotListResult>() {
                    @Override
                    public void onSuccess(GetTagHotPostHotListResult result) {
                        mSv.showContent();
                        mAllPage = result.allpage;

                        if (result.count == 0) { // 数据为空
                            if (mSwipe.isRefreshing()) {
                                //                                mAdapter.getData().clear();
                                mAdapter.setNewData(null);
                            }
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.post_list);
                            } else {
                                mAdapter.addData(result.post_list);
                                if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= result.count) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        }
                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }

    /**
     * 获取标签详情网络请求
     */
    public void requestTagInfo() {
        SnsRepository.getInstance()
                .getPostTag(mTagId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<GetPostTagResult>() {
                    @Override
                    public void onSuccess(GetPostTagResult getPostTagResult) {
                        // 标题
                        mTagInfo = getPostTagResult;
                        mTvTitle.setText(String.format("#%s", mTagInfo.name));
                        mTitle.setHeadTitle(String.format("#%s", mTagInfo.name));
                        // 文本
                        mTvDesc.setText(mTagInfo.content);
                        // banner图片
                        Glide.with(mActivity)
                                .load(UpyUrlManager.getUrl(mTagInfo.image_url, mScreenWidth))
                                .placeholder(R.mipmap.ic_default_rect)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .dontAnimate()
                                .into(mImgBanner);
                        // GA 事件
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("社区运营")
                                .setAction("笔记标签 Click")
                                .setLabel(mTagInfo.name)
                                .build());
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        return false;
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }


    /**
     * 点赞请求
     *
     * @param postId      帖子Id
     * @param sb          点赞按钮
     * @param tvLikeCount 点赞数
     */
    private void requestLikePost(int postId, SparkButton sb, TextView tvLikeCount, int position) {
        SnsRepository.getInstance()
                .likePost(postId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<LikePostResult>() {
                    @Override
                    public void onSuccess(LikePostResult result) {
                        if (result.is_like_now) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, result.membership_point, AlterPointViewType.AlterPointViewType_Like).parentView(mRvContent);
                            mPwToast.show();
                        }
                        sb.setChecked(result.is_like_now);
                        tvLikeCount.setText(String.format("%d", result.like_count));

                        PostBean post = mAdapter.getData().get(position);
                        post.is_liked = result.is_like_now;
                        post.like_count = result.like_count;
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 点击发帖
     */
    @OnClick(R.id.llyt_send_post)
    public void clickPublishPost() {
        if (HaiUtils.needLogin(mActivity))
            return;

        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_PostEdit, "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_SendPost + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_PostEdit, TraceUtils.PageCode_Social, TraceUtils.PositionCode_SendPost, "");

        String postDraft = (String) SPUtils.get(mActivity, "post_draft", "");
        Logger.d(postDraft);
        if (!TextUtils.isEmpty(postDraft)) {
            try {
                new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                        .setMessage("是否打开上一次的草稿")
                        .setPositiveButton("恢复", (dialog, which) -> {
                            DraftPostBean draftPostBean = new Gson().fromJson(postDraft, DraftPostBean.class);
                            PostEditActivity.toThisActivity(mActivity, draftPostBean);
                        })
                        .setNegativeButton("新建", (dialog, which) -> {
                            SPUtils.remove(mActivity, "post_draft");
                            openGallery();
                        }).show();

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                openGallery();
            }
        } else {
            openGallery();
        }
    }

    /**
     * 打开相册
     */
    private void openGallery() {
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, mFunctionConfig, mOnHanlderResultCallback);
    }

    /**
     * 图片选择回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
            Intent intent = new Intent(mActivity, PostEditActivity.class);
            intent.putParcelableArrayListExtra("img_path", resultList);
            intent.putExtra("tag_name", mTagName);
            mActivity.startActivity(intent);
            Logger.t(TAG).d(resultList.toString());
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        ShareUtils.dismissShareBoard();
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
        return TAG + "?" + "id=" + mTagId;
    }

    public static void toThisActivity(Activity activity, int tagId, String tagName, int isHot) {
        Intent intent = new Intent(activity, TagDetailActivity.class);
        intent.putExtra("tag_id", tagId);
        intent.putExtra("tag_name", tagName);
        intent.putExtra("is_hot", isHot);
        activity.startActivity(intent);
    }

    /**
     * 登录状态变化
     */
    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        refreshPostList();
    }

    /**
     * 帖子变化
     */
    @Subscribe
    public void onPostChange(PostChangeEvent event) {
        refreshPostList();
    }
}
