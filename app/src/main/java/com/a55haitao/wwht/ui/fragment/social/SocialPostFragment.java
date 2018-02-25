package com.a55haitao.wwht.ui.fragment.social;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.HomeTagListAdapter;
import com.a55haitao.wwht.adapter.social.SocialPostListAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.PostChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.CommentActivityType;
import com.a55haitao.wwht.data.model.annotation.PostFragmentLayoutType;
import com.a55haitao.wwht.data.model.entity.PostBannerBean;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.data.model.entity.PostFragmentData;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.data.model.result.FollowUserResult;
import com.a55haitao.wwht.data.model.result.LikePostResult;
import com.a55haitao.wwht.data.model.result.PostListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.activity.social.CommentListActivity;
import com.a55haitao.wwht.ui.activity.social.HotTagActivity;
import com.a55haitao.wwht.ui.activity.social.PostDetailActivity;
import com.a55haitao.wwht.ui.activity.social.TagDetailActivity;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;
import com.varunest.sparkbutton.SparkButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import rx.Observable;

/**
 * 社区 - 精选 Fragment
 *
 * @author 陶声
 * @since 2016-10-12
 */

public class SocialPostFragment extends SocialBaseFragment {
    @BindView(R.id.content_view)      RecyclerView       mRvContent;    // 内容
    @BindView(R.id.swipe)             SwipeRefreshLayout mSwipe;        // 下拉刷新
    @BindView(R.id.msv)               MultipleStatusView mSv;           // 多状态布局
    @BindColor(R.color.color_swipe)   int                colorSwipe;    // 下拉刷新颜色
    @BindDrawable(R.drawable.divider) Drawable           DR_DIVIDER;    // 分割线背景

    public static final int TURNING_TIME = 3000; // 自动翻页时间

    @PostFragmentLayoutType
    private int                   mType;
    private int                   mCurrentPage;
    private int                   mAllPage;
    private SocialPostListAdapter mAdapter;
    private RecyclerView          mRvTagList;
    private HomeTagListAdapter    mTagListAdapter;
    private ConvenientBanner      mConvenientBanner;

    private List<TagBean>              mTagListData;
    private StaggeredGridLayoutManager mSingleLayoutManager;
    private StaggeredGridLayoutManager mDoubleLayoutManager;
    private int                        mScreenWidth; // 屏幕宽度
    private View                       mBannerView;
    private View                       mHotTag;
    private View                       mPostTitle;
    private List<PostBean>             mPostList;
    private ToastPopuWindow            mPwToast;

    @Override
    protected int getLayout() {
        return R.layout.fragment_social_post;
    }

    @Override
    public void initVariables() {
        mScreenWidth = DisplayUtils.getScreenWidth(mActivity);
        mType = PostFragmentLayoutType.DOUBLE;
        mPostList = new ArrayList<>();

        mCurrentPage = 1;
        mAllPage = 1;
        EventBus.getDefault().register(this);
    }

    /**
     * 计算Banner高度
     *
     * @param width banner宽度
     * @return banner高度
     */
    private int calcBannerHeight(int width) {
        return (int) (width / (750 * 1.0f / 400));
    }

    @Override
    protected void initViews(View v, Bundle savedInstanceState) {
        initAdapter();
        mSingleLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mDoubleLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        mRvContent.setLayoutManager(mDoubleLayoutManager);
        addClickListener();

        initHeaderView(v);

        mAdapter.addHeaderView(mBannerView);
        mAdapter.addHeaderView(mHotTag);
        mAdapter.addHeaderView(mPostTitle);
        mRvContent.setAdapter(mAdapter);

        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> {
            mRvContent.post(() -> {
                if (mAdapter.getData().size() < PAGE_SIZE) {
                    mAdapter.loadMoreEnd(true);
                } else if (mCurrentPage < mAllPage) {
                    mCurrentPage++;
                    mSwipe.setEnabled(false);
                    requestPostList();
                }
            });
        }, mRvContent);
        // 刷新
        mSwipe.setOnRefreshListener(this::refreshData);
        // 失败重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        mAdapter.setEnableLoadMore(false);
        loadData();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView(View v) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mBannerView = inflater.inflate(R.layout.social_post_banner, null, false);
        mHotTag = inflater.inflate(R.layout.hot_tag_container, null, false);
        mPostTitle = inflater.inflate(R.layout.social_post_header, null, false);
        // banner
        View                   banner = mBannerView.findViewById(R.id.convenientBanner);
        ViewGroup.LayoutParams lp     = banner.getLayoutParams();
        lp.width = mScreenWidth;
        lp.height = calcBannerHeight(mScreenWidth);
        banner.setLayoutParams(lp);

        mConvenientBanner = (ConvenientBanner) banner;
        RadioGroup rgpType = (RadioGroup) mPostTitle.findViewById(R.id.rgp_type);
        rgpType.setOnCheckedChangeListener((group, checkedId) -> {
            int offset;
            switch (checkedId) {
                case R.id.rdo_stream:
                    mType = PostFragmentLayoutType.SINGLE;
                    HaiApplication.layoutType = PostFragmentLayoutType.SINGLE;
                    offset = mPostTitle.getMeasuredHeight();
                    mRvContent.setLayoutManager(mSingleLayoutManager);
                    mRvContent.post(() -> mSingleLayoutManager.scrollToPositionWithOffset(1, offset));
                    Logger.t(TAG).d("offset = " + offset);
                    break;
                case R.id.rdo_waterfall:
                    mType = PostFragmentLayoutType.DOUBLE;
                    HaiApplication.layoutType = PostFragmentLayoutType.DOUBLE;
                    offset = mPostTitle.getMeasuredHeight();

                    mRvContent.setLayoutManager(mDoubleLayoutManager);
                    mRvContent.post(() -> mDoubleLayoutManager.scrollToPositionWithOffset(1, offset));

                    Logger.t(TAG).d("offset = " + offset);
                    break;
            }
        });

        // 热门标签
        mRvTagList = (RecyclerView) mHotTag.findViewById(R.id.rv_tags);
        initTagListAdapter();
        mRvTagList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

        mRvTagList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HotTagDetail, mTagListData.get(position).id + "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Tag + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HotTagDetail, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Tag, mTagListData.get(position).id + "");

                Intent intent = new Intent(mActivity, TagDetailActivity.class);
                intent.putExtra("tag_id", mTagListData.get(position).id);
                intent.putExtra("tag_name", mTagListData.get(position).name);
                intent.putExtra("is_hot", mTagListData.get(position).is_hot);
                startActivity(intent);
            }
        });

        // 跳转到热门标签页面
        (mHotTag.findViewById(R.id.ib_more)).setOnClickListener(v1 -> {
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_HotTagList, "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_TagMore + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_HotTagList, TraceUtils.PageCode_Social, TraceUtils.PositionCode_TagMore, "");

            Intent intent = new Intent(mActivity, HotTagActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 添加点击事件
     */
    private void addClickListener() {
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_PostDetail, mAdapter.getData().get(position).post_id + "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Post + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_PostDetail, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Post, mAdapter.getData().get(position).post_id + "");

                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mAdapter.getData().get(position).post_id);
                intent.putExtra("wh_rate", mAdapter.getData().get(position).images.get(0).wh_rate);
                startActivity(intent);
            }


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PostBean post = mAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.img_avatar: // 用户主页
                    case R.id.tv_nickname:
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_HomePage, post.owner.id + "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_User + "", "");

//                        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_Social, TraceUtils.PositionCode_User, post.owner.id + "");

                        OthersHomePageActivity.actionStart(mActivity, post.owner.id);
                        break;
                    case R.id.chk_follow_user: // 关注用户
                        CheckBox chkFollowUser = (CheckBox) view;
                        if (HaiUtils.needLogin(mActivity)) {
                            chkFollowUser.setChecked(!chkFollowUser.isChecked());
                        } else {
                            requestFollowUser(chkFollowUser, post.owner.id, position);
                        }
                        break;
                    case R.id.ll_tag_container: // 标签容器
                        break;
                    case R.id.sb_like: // 点赞
                        SparkButton sb = (SparkButton) view;
                        TextView tvLikeCount = (TextView) ((ViewGroup) sb.getParent()).findViewById(R.id.tv_like_count);
                        if (HaiUtils.needLogin(mActivity)) {
                            sb.setChecked(post.is_liked);
                        } else {
                            requestLikePost(post.post_id, sb, tvLikeCount, position);
                        }
                        break;
                    case R.id.ib_comment: // 评论
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_CommentsList, post.post_id + "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_ViewComments + "", "");

//                        TraceUtils.addAnalysAct(TraceUtils.PageCode_CommentsList, TraceUtils.PageCode_Social, TraceUtils.PositionCode_ViewComments, "");

                        CommentListActivity.toThisActivity(mActivity, CommentActivityType.POST, post.post_id, post.owner.id == HaiUtils.getUserId());
                        break;
                    case R.id.ib_share: // 分享
                        ShareUtils.showShareBoard(mActivity,
                                post.share.title,
                                TextUtils.isEmpty(post.share.desc) ? "来自55海淘官网直购社区" : post.share.desc,
                                post.share.url,
                                post.share.icon,
                                true);
                        break;
                    default:
                        break;
                }
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);
//        dismissProgressDialog();
//    }

    /**
     * 关注用户
     *
     * @param chkFollowUser 关注按钮
     * @param id            用户id
     */
    private void requestFollowUser(CheckBox chkFollowUser, int id, int position) {
        SnsRepository.getInstance()
                .followUser(id)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<FollowUserResult>() {
                    @Override
                    public void onSuccess(FollowUserResult result) {
                        if (result.is_following) {
                            ToastUtils.showToast(mActivity, "关注成功");
                        }
                        chkFollowUser.setChecked(result.is_following);
                        // 修改数据
                        mAdapter.getData().get(position).is_following = result.is_following;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        chkFollowUser.setChecked(!chkFollowUser.isChecked());
                        return super.onFailed(e);
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
     * @param position
     */
    private void requestLikePost(int postId, SparkButton sb, TextView tvLikeCount, int position) {
        SnsRepository.getInstance()
                .likePost(postId)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<LikePostResult>() {
                    @Override
                    public void onSuccess(LikePostResult result) {
                        if (result.is_like_now) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, result.membership_point, AlterPointViewType.AlterPointViewType_Like).parentView(mRvContent);
                            mPwToast.show();
                        }
                        sb.setChecked(result.is_like_now);
                        if (HaiApplication.layoutType == PostFragmentLayoutType.SINGLE) {
                            tvLikeCount.setText(String.format("赞 %d", result.like_count));
                        } else {
                            tvLikeCount.setText(String.format("%d", result.like_count));
                        }

                        PostBean post = mAdapter.getData().get(position);
                        post.is_liked = result.is_like_now;
                        post.like_count = result.like_count;
                        // 发送事件
                        EventBus.getDefault().post(new PostChangeEvent());
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 创建热门标签Adapter
     */
    private void initTagListAdapter() {
        mTagListAdapter = new HomeTagListAdapter(null, mFragment);
        mRvTagList.setAdapter(mTagListAdapter);
    }

    /**
     * 创建晒物Adapter
     */
    private void initAdapter() {
        mAdapter = new SocialPostListAdapter(mPostList, mFragment);
    }

    @Override
    public void loadData() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;
        Observable.zip(SnsRepository.getInstance().postBanner(),
                SnsRepository.getInstance().getPostHotTagForHomeList(),
                SnsRepository.getInstance().getRecommendPostList(mCurrentPage),
                PostFragmentData::new)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<PostFragmentData>() {
                    @Override
                    public void onSuccess(PostFragmentData postFragmentData) {
                        mSv.showContent();
                        // banner
                        setBannerView(postFragmentData.bannerList);
                        // tag
                        mTagListData = postFragmentData.tagList;
                        setHotTagListView(mTagListData);
                        // post
                        mAllPage = postFragmentData.postListResult.allpage;
                        if (mSwipe.isRefreshing()) {
                            mAdapter.setNewData(postFragmentData.postListResult.post_list);
                        } else {
                            mAdapter.addData(postFragmentData.postListResult.post_list);
                        }
                        mAdapter.loadMoreComplete();
                        mHasData = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasData);
                        return mHasData;
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setEnabled(true);
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }

    /**
     * 加载晒物列表
     */
    public void requestPostList() {
        SnsRepository.getInstance()
                .getRecommendPostList(mCurrentPage)
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), FragmentEvent.DESTROY))
                .subscribe(new DefaultSubscriber<PostListResult>() {
                    @Override
                    public void onSuccess(PostListResult postListResult) {
                        mAllPage = postListResult.allpage;
                        if (mSwipe.isRefreshing()) {
                            mAdapter.setNewData(postListResult.post_list);
                        } else {
                            mAdapter.addData(postListResult.post_list);
                            if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= postListResult.count) {
                                mAdapter.loadMoreEnd();
                            } else {
                                mAdapter.loadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setEnabled(true);
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }

    /**
     * 填充banner UI
     */
    private void setBannerView(List<PostBannerBean> bannerList) {
        mConvenientBanner.setPages(LocalImageHolderView::new, bannerList)
                .setPageIndicator(new int[]{R.mipmap.ic_banner_show_indicator_normal, R.mipmap.ic_banner_show_indicator_selected})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setOnItemClickListener(position -> {
                    // GA Tracker
                    HaiApplication application = (HaiApplication) mActivity.getApplication();
                    Tracker        tracker     = application.getDefaultTracker();
                    PostBannerBean bannerBean  = bannerList.get(position);
                    tracker.send(new HitBuilders.EventBuilder()
                            .setCategory("社区运营")
                            .setAction("Banner Click")
                            .setLabel(bannerBean.uri + "," + bannerBean.name)
                            .setValue(position + 1)
                            .build());
                    // 埋点
//                    TraceUtils.addAnalysActMatchUri(mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Banner, bannerBean.uri);

                    jumpSpecialDetail(bannerBean);
                });
    }

    /**
     * 填充热门标签列表
     */
    private void setHotTagListView(List<TagBean> tagList) {
        mTagListAdapter.setNewData(tagList);
    }

    /**
     * 图片加载Holder
     */
    public class LocalImageHolderView implements Holder<PostBannerBean> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, PostBannerBean data) {
            Glide.with(mActivity)
                    .load(UpyUrlManager.getUrl(data.image, mScreenWidth))
                    .placeholder(R.mipmap.ic_default_rect)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(imageView);
        }
    }

    /**
     * 跳转到专题详情页面
     */
    private void jumpSpecialDetail(PostBannerBean data) {
        // 页面跳转
        HaiUriMatchUtils.matchUri(mActivity, data.uri);
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser) {
        super.onVisibilityChangedToUser(isVisibleToUser);
        if (isVisibleToUser) {
            mConvenientBanner.startTurning(TURNING_TIME);
        } else {
            mConvenientBanner.stopTurning();
            mSwipe.setRefreshing(false);
        }
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        refreshData();
    }

    @Subscribe
    public void onPostChangeEvent(PostChangeEvent event) {
        refreshData();
    }

    @Override
    public void onStop() {
        super.onStop();
        ShareUtils.dismissShareBoard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }
}
