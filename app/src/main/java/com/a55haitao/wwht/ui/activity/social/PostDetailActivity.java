package com.a55haitao.wwht.ui.activity.social;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.CommentListAdapter;
import com.a55haitao.wwht.adapter.social.RelatedPostListAdapter;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.event.APIErrorEvent;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.PostChangeEvent;
import com.a55haitao.wwht.data.event.PostDetailChangeEvent;
import com.a55haitao.wwht.data.event.UserStartChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.CommentActivityType;
import com.a55haitao.wwht.data.model.entity.CommentBean;
import com.a55haitao.wwht.data.model.entity.ImagesBean;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.data.model.entity.PublishPostBean;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.data.model.entity.UserListBean;
import com.a55haitao.wwht.data.model.result.AddPostCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostResult;
import com.a55haitao.wwht.data.model.result.LikePostResult;
import com.a55haitao.wwht.data.model.result.PostDetailActivityResult;
import com.a55haitao.wwht.data.model.result.PostDetailResult;
import com.a55haitao.wwht.data.model.result.likePostCommentResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.PostLikeButton;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiTimeUtils;
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
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import rx.Observable;

/**
 * 笔记详情页
 *
 * @author 陶声
 * @since 2016-10-31
 */
@MLinkRouter(keys = {"postDetailKey"})
public class PostDetailActivity extends BaseNoFragmentActivity {
    @BindView(R.id.ib_title_edit)        ImageButton        mIbTitleEdit;        // 编辑帖子
    @BindView(R.id.ib_title_delete)      ImageButton        mIbTitleDelete;      // 删除帖子
    @BindView(R.id.rv_comment)           RecyclerView       mRvComment;          // 评论列表
    @BindView(R.id.ib_cancel)            ImageButton        mIbTitleBack;        // 返回按钮
    @BindView(R.id.ib_title_share)       ImageButton        mIbTitleShare;       // 分享按钮
    @BindView(R.id.img_avatar)           AvatarView         mAvatarView;          // 头像
    @BindView(R.id.tv_nickname)          TextView           mTvNickname;         // 昵称
    @BindView(R.id.tv_time)              TextView           mTvTime;             // 时间
    @BindView(R.id.tv_title)             TextView           mTvTitle;            // 晒物文字标题
    @BindView(R.id.tv_desc)              TextView           mTvContent;          // 晒物文字内容
    @BindView(R.id.like_button)          PostLikeButton     mLikeButton;         // 点赞
    @BindView(R.id.tv_comment_count)     TextView           mTvCommentCount;     // 评论数
    @BindView(R.id.ll_avator_container)  LinearLayout       mLlAvatorContainer;  // 点赞头像Container
    @BindView(R.id.tv_no_like)           TextView           mTvNoLike;           // 还没有人点赞
    @BindView(R.id.rv_related_post)      RecyclerView       mRvRelatedPost;      // 相关晒物
    @BindView(R.id.tv_show_all_comments) TextView           mTvShowAllComments;  // 查看全部评论
    @BindView(R.id.tv_no_comments)       TextView           mTvNoComments;       // 还木有人评论呢
    @BindView(R.id.tags)                 TagFlowLayout      mTagLayout;          // 标签容器
    @BindView(R.id.et_comment_content)   EditText           mEtCommentContent;   // 评论内容
    @BindView(R.id.tv_send_comment)      TextView           mTvSendComment;      // 发送评论
    @BindView(R.id.vp_pic)               ViewPager          pager;               // 图片ViewPager
    @BindView(R.id.tv_index)             HaiTextView        mTvIndex;            // 图片下标
    @BindView(R.id.ll_container)         LinearLayout       mLlContainer;
    @BindView(R.id.msv)                  MultipleStatusView mSv;

    @BindString(R.string.key_post_id) String KEY_POST_ID;

    @BindDimen(R.dimen.margin_medium)             int MARGIN_MEDIUM;    // margin
    @BindDimen(R.dimen.show_detail_avatar_margin) int SHOW_DETAIL_AVATAR_MARGIN;    // margin
    @BindDimen(R.dimen.avatar_medium)             int AVATAR_SIZE;

    private int     mPostId;                            // 晒物ID
    private int     mCommentId;
    private boolean isCommentIng;

    private int                    mMaxLikeListAvatarCount;
    private Dialog                 mDialog;
    private boolean                mIsMyPost;
    private InputMethodManager     mImm;
    private int                    mScreenWidth;
    private RelatedPostListAdapter mRelatedPostListAdapter;
    private LayoutInflater         mInflater;
    private PostBean               mPostData;
    private CommentListAdapter     mCommentListAdapter;
    private List<CommentBean>      mCommentData;
    private float                  mWhRate;

    private List<ImageView> imageViews;

    //各个页面的高度
    private int[]             heights;
    private PagerAdapter      adapter;
    private int               mMemberShipPoint;  // 积分
    private Tracker           mTracker;          // GA Tracker
    private ArrayList<String> mTagNameList;
    private ToastPopuWindow   mPwToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
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
            // 获取魔窗字段
            String mwPostId = intent.getStringExtra("postid");
            if (TextUtils.isEmpty(mwPostId)) {
                mPostId = getIntent().getIntExtra(KEY_POST_ID, 0);
            } else {
                mPostId = Integer.valueOf(mwPostId);
            }
            Logger.t(TAG).d(mPostId);
            mWhRate = intent.getFloatExtra("wh_rate", 0f);
            mMemberShipPoint = intent.getIntExtra("membership_point", 0);
            //            switchViewType(mIsMyPost);
        }
        mScreenWidth = DisplayUtils.getScreenWidth(this);
        mMaxLikeListAvatarCount = calcLikeAvatarCount();
        // 是否显示编辑按钮
        mIbTitleEdit.setVisibility(mIsMyPost ? View.VISIBLE : View.GONE);
        mImm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mInflater = LayoutInflater.from(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // GA 屏幕跟踪
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("笔记_详情");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // GA 事件
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("社区运营")
                .setAction("笔记 Click")
                .setLabel(String.valueOf(mPostId))
                .build());

    }

    /**
     * 计算最大头像数目
     */
    private int calcLikeAvatarCount() {
        return (mScreenWidth - MARGIN_MEDIUM * 2 + SHOW_DETAIL_AVATAR_MARGIN) / (AVATAR_SIZE + SHOW_DETAIL_AVATAR_MARGIN) - 1;
    }

    /**
     * 根据是否是自己的帖子改变UI
     */
    private void switchViewType(boolean isMyPost) {
        mIsMyPost = isMyPost;
        mIbTitleEdit.setVisibility(isMyPost ? View.VISIBLE : View.GONE);
        mIbTitleDelete.setVisibility(isMyPost ? View.VISIBLE : View.GONE);
    }

    /**
     * 切换评论列表UI
     *
     * @param commentCount 评论数
     */
    private void switchCommentListView(int commentCount) {
        // 没有评论
        mTvNoComments.setVisibility(commentCount == 0 ? View.VISIBLE : View.GONE);
        // 查看所有评论
        mTvShowAllComments.setVisibility(commentCount > 5 ? View.VISIBLE : View.GONE);
        // 评论数
        mTvCommentCount.setText(String.format("评论(%d)", commentCount));
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        mIbTitleShare.setFocusable(true);
        mIbTitleShare.setFocusableInTouchMode(true);
        mIbTitleShare.requestFocus();

        mEtCommentContent.getViewTreeObserver()
                .addOnGlobalLayoutListener(() -> {
                    if (mMemberShipPoint != 0) {
                        mPwToast = ToastPopuWindow.makeText(mActivity, mMemberShipPoint, AlterPointViewType.AlterPointViewType_SendPost).parentView(mEtCommentContent);
                        mPwToast.show();
                        mMemberShipPoint = 0;
                    }
                });

        mSv.setOnRetryClickListener(v1 -> loadData());

        mRvComment.setHasFixedSize(true);
        mRvComment.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRvComment.setNestedScrollingEnabled(false);
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
                if (mIsMyPost || mCommentListAdapter.getData().get(position).user_info.id == HaiUtils.getUserId()) {
                    showDeleteCommentDlg(mCommentListAdapter.getData().get(position).id, position);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_avatar:
                    case R.id.tv_nickname:
                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_HomePage, mCommentListAdapter.getData().get(position).user_info.id + "", PostDetailActivity.this, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_User + "", "");

                        //                        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_User, mCommentListAdapter.getData().get(position).user_info.id + "");

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

        // 相关笔记
        mRvRelatedPost.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mRelatedPostListAdapter = new RelatedPostListAdapter(null, mActivity);
        mRvRelatedPost.setAdapter(mRelatedPostListAdapter);
        mRvRelatedPost.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_PostDetail, mRelatedPostListAdapter.getItem(position).post_id + "", PostDetailActivity.this, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_Post + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_PostDetail, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_Post, mRelatedPostListAdapter.getItem(position).post_id + "");

                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mRelatedPostListAdapter.getItem(position).post_id);
                intent.putExtra("img_url", mRelatedPostListAdapter.getItem(position).image_url);
                mActivity.startActivity(intent);
            }
        });
    }

    /**
     * 点赞评论网络请求
     *
     * @param commentId   评论Id
     * @param sb          点赞按钮
     * @param tvLikeCount 点赞数
     */
    private void requestLikeComment(int commentId, SparkButton sb, TextView tvLikeCount) {
        SnsRepository.getInstance()
                .likePostComment(commentId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<likePostCommentResult>() {
                    @Override
                    public void onSuccess(likePostCommentResult result) {
                        sb.setChecked(result.is_like_now);
                        tvLikeCount.setText(result.like_count > 0 ? String.valueOf(result.like_count) : "");
                    }

                    @Override
                    public void onFinish() {

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
     * 帖子内容网络请求
     */
    private void requestContent() {
        Observable.zip(SnsRepository.getInstance().getPostDetail(mPostId),
                SnsRepository.getInstance().getRandomPostList(mPostId),
                PostDetailActivityResult::new)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<PostDetailActivityResult>() {
                    @Override
                    public void onSuccess(PostDetailActivityResult postDetailActivityResult) {
                        mSv.showContent();
                        // 笔记详情
                        mPostData = postDetailActivityResult.postDetailResult.post;
                        mCommentData = postDetailActivityResult.postDetailResult.post_comment_list;

                        setPostDetailView(mPostData);
                        setLikeListView(postDetailActivityResult.postDetailResult.like_user_list);
                        setCommentListView(mCommentData);
                        // 相关笔记
                        setRelatedPostView(postDetailActivityResult.randomPostListResult.post_list);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }
                });
    }

    /**
     * 请求晒物详情数据
     */
    private void loadPostDetailData() {
        //        showProgressDialog();
        SnsRepository.getInstance()
                .getPostDetail(mPostId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<PostDetailResult>() {
                    @Override
                    public void onSuccess(PostDetailResult postDetailResult) {
                        mPostData = postDetailResult.post;
                        mCommentData = postDetailResult.post_comment_list;

                        setPostDetailView(mPostData);
                        setLikeListView(postDetailResult.like_user_list);
                        setCommentListView(mCommentData);

                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 填充晒物详情UI
     *
     * @param post 晒物信息
     */
    private void setPostDetailView(PostBean post) {
        switchViewType(post.owner.id == HaiUtils.getUserId());
        // 头像
        String cornerUrl = null;
        if (post.owner.user_title.size() != 0) {
            cornerUrl = post.owner.user_title.get(0).getIconUrl();
        }
        mAvatarView.loadImg(post.owner.head_img, cornerUrl);

        // 发布时间
        mTvTime.setText(String.format("%s 发布", HaiTimeUtils.showPostTime(post.create_dt)));
        // 昵称
        mTvNickname.setText(post.owner.nickname);
        // 设置图片
        setImgPager(post.images);
        // 图片下标
        if (post.images.size() > 1) {
            mTvIndex.setVisibility(View.VISIBLE);
            mTvIndex.setText(String.format("1/%d", post.images.size()));
        } else {
            mTvIndex.setVisibility(View.GONE);
        }
        // 标题
        mTvTitle.setVisibility(TextUtils.isEmpty(post.one_word) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(post.one_word)) {
            mTvTitle.setText(post.one_word);
        }
        mTvContent.setText(post.content);
        // 文字内容
        mTvContent.setText(post.content);
        // 标签
        mTagNameList = new ArrayList<>(post.tag_list.size());
        for (TagBean tag : post.tag_list) {
            mTagNameList.add(tag.name);
        }
        setTagListView(post.tag_list);
        // 点赞数
        mLikeButton.setCheckedNoAnim(post.is_liked);
        mLikeButton.setLikeCount(post.like_count);
        // 点赞监听
        mLikeButton.setLikeListener(isChecked -> {
            if (HaiUtils.needLogin(mActivity)) {
                mLikeButton.setChecked(mLikeButton.isChecked());
                return;
            }
            requestLikePost(post.post_id);
        });
        // 评论UI切换
        switchCommentListView(post.reply_count);
        // 跳转到所有评论页面
        if (mTvShowAllComments.getVisibility() == View.VISIBLE) {
            mTvShowAllComments.setOnClickListener(v -> CommentListActivity.toThisActivity(mActivity, CommentActivityType.POST, mPostId, mIsMyPost));
        }
    }

    /**
     * 图片
     *
     * @param images
     */
    private void setImgPager(List<ImagesBean> images) {

        while (images != null && images.contains(null)) {
            images.remove(null);
        }

        imageViews = new ArrayList<>(images.size());
        heights = new int[images.size()];

        //因为现在还不知道ViewPager的宽度，所以要等ViewPager布局完成之后再进行一些初始化
        pager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //初始化各个页面
                for (int i = 0, len = images.size(); i < len; i++) {
                    ImageView imageView = new ImageView(mActivity);
                    //计算该页面合适的高度，使得图片完整的显示，填充整个高度和宽度
                    float whRate = images.get(i).wh_rate;
                    if (Math.abs(whRate - 0) < 0.0001f) {
                        whRate = 1f;
                    }
                    heights[i] = (int) (mScreenWidth / whRate);
                    Glide.with(mActivity)
                            .load(UpyUrlManager.getUrl(images.get(i).url, mScreenWidth))
                            .placeholder(R.mipmap.ic_default_square_big)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .dontAnimate()
                            .into(imageView);

                    imageViews.add(imageView);
                }
                adapter.notifyDataSetChanged();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                //ViewPager的初始高度设置为第一个页面的高度
                ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
                layoutParams.height = heights[0];
                pager.setLayoutParams(layoutParams);
            }
        });

        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return imageViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = imageViews.get(position);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                container.addView(imageView);
                return imageView;
            }
        };
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 设置图片下标
                if (mPostData != null)
                    mTvIndex.setText(String.format("%d/%d", position + 1, mPostData.images.size()));

                if (position == heights.length - 1) {
                    return;
                }
                //计算ViewPager现在应该的高度,heights[]表示页面高度的数组。
                int height = (int) (heights[position] * (1 - positionOffset) + heights[position + 1] * positionOffset);

                //为ViewPager设置高度
                ViewGroup.LayoutParams params = pager.getLayoutParams();
                params.height = height;
                pager.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 填充点赞列表UI
     *
     * @param userList 点赞列表
     */
    private void setLikeListView(List<UserListBean> userList) {
        int count = 0;
        mLlAvatorContainer.removeAllViews();
        // 没有人点赞
        mTvNoLike.setVisibility(userList.size() == 0 ? View.VISIBLE : View.GONE);

        for (int i = 0, len = userList.size(); i < len; i++) {
            UserListBean user   = userList.get(i);
            ImageView    avatar = new ImageView(mActivity);

            Glide.with(mActivity)
                    .load(count < mMaxLikeListAvatarCount ? UpyUrlManager.getUrl(user.head_img, AVATAR_SIZE) : R.mipmap.ic_points)
                    .placeholder(R.mipmap.ic_avatar_default_medium)
                    .transform(new GlideCircleTransform(mActivity))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(avatar);

            LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(
                    AVATAR_SIZE, AVATAR_SIZE);

            if (i != 0) {
                avatarParams.leftMargin = SHOW_DETAIL_AVATAR_MARGIN;
            }

            avatar.setLayoutParams(avatarParams);

            mLlAvatorContainer.addView(avatar);
            if (++count > mMaxLikeListAvatarCount) {
                break;
            }
        }
    }

    /**
     * 发表评论请求
     *
     * @param postId    CommentId
     * @param content   评论内容
     * @param commentId 评论Id
     */
    private void requestSendComment(int postId, String content, int commentId) {
        SnsRepository.getInstance()
                .addPostComment(postId, content, commentId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<AddPostCommentResult>() {
                    @Override
                    public void onSuccess(AddPostCommentResult addPostCommentResult) {
                        mEtCommentContent.setText("");
                        mImm.hideSoftInputFromWindow(mEtCommentContent.getWindowToken(), 0);
                        mPwToast = ToastPopuWindow.makeText(mActivity, addPostCommentResult.membership_point, AlterPointViewType.AlterPointViewType_Comment).parentView(mEtCommentContent);
                        mPwToast.show();
                        // 评论完之后刷新数据
                        loadPostDetailData();
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
     * @param id       评论id
     * @param position 位置
     */
    private void requestDeleteComment(int id, int position) {
        SnsRepository.getInstance()
                .deletePostComment(id)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<DeletePostCommentResult>() {
                    @Override
                    public void onSuccess(DeletePostCommentResult result) {
                        if (result.success) {
                            ToastUtils.showToast(mActivity, "已删除");
                            loadPostDetailData();
                            EventBus.getDefault().post(new PostChangeEvent());

                            //                            mCommentData.remove(position);
                            //                            mCommentListAdapter.notifyDataSetChanged();
                            //                            switchCommentListView(result.reply_count);
                        }
                    }

                    @Override
                    public void onFinish() {
                        mDialog.dismiss();
                    }
                });
    }

    /**
     * 删除帖子请求
     */
    private void requestDeletePost() {
        showProgressDialog("正在删除", true);
        SnsRepository.getInstance()
                .deletePost(mPostId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<DeletePostResult>() {
                    @Override
                    public void onSuccess(DeletePostResult deletePostResult) {
                        ToastUtils.showToast(mActivity, "删除成功");
                        EventBus.getDefault().post(new PostChangeEvent());
                        EventBus.getDefault().post(new UserStartChangeEvent());

                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 填充评论列表UI
     *
     * @param postCommentList 评论列表
     */
    private void setCommentListView(List<CommentBean> postCommentList) {
        mCommentListAdapter.setNewData(postCommentList);
    }

    /**
     * 设置tagList
     *
     * @param tagList tag列表
     */
    private void setTagListView(List<TagBean> tagList) {
        mTagLayout.setAdapter(new TagAdapter<String>(mTagNameList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TagBean     tag   = tagList.get(position);
                HaiTextView tvTag = (HaiTextView) mInflater.inflate(tag.is_hot == 1 ? R.layout.tag_hot : R.layout.tag_normal, null);
                tvTag.setText(tag.is_hot == 1 ? String.format("# %s", tag.name) : tag.name);
                return tvTag;
            }
        });

        mTagLayout.setOnTagClickListener((view, position, parent) -> { // 点击tag跳转到tag晒物页
            jumpTagPostListActivity(tagList.get(position).id, tagList.get(position).name, tagList.get(position).is_hot);
            Logger.t(TAG).d(tagList.get(position).id);
            return true;
        });
    }

    /**
     * 弹出删除评论确认对话框
     *
     * @param id       评论id
     * @param position 位置
     */
    private void showDeleteCommentDlg(int id, int position) {
        mDialog = new Dialog(mActivity);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View     v    = LayoutInflater.from(mActivity).inflate(R.layout.dlg_single_choice, null);
        TextView desc = (HaiTextView) v.findViewById(R.id.tv_desc);
        desc.setText("删除评论");
        desc.setOnClickListener(view -> requestDeleteComment(id, position));
        mDialog.setContentView(v);
        mDialog.show();
    }


    /**
     * 填充相关晒物UI
     *
     * @param postList 晒物列表
     */
    private void setRelatedPostView(List<PostBean> postList) {
        mRelatedPostListAdapter.setNewData(postList);

    }

    /**
     * 跳转到tag晒物页面
     */
    private void jumpTagPostListActivity(int tagId, String tagName, int isHot) {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_HotTagDetail, tagId + "", PostDetailActivity.this, TraceUtils.PageCode_PostDetail, isHot != 0 ? TraceUtils.PositionCode_Tag + "" : TraceUtils.PositionCode_UserTag + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_HotTagDetail, TraceUtils.PageCode_PostDetail, isHot != 0 ? TraceUtils.PositionCode_Tag : TraceUtils.PositionCode_UserTag, tagId + "");

        Intent intent = new Intent(mActivity, TagDetailActivity.class);   // 跳转到tag晒物页
        intent.putExtra("tag_id", tagId);
        intent.putExtra("tag_name", tagName);
        intent.putExtra("is_hot", isHot);
        mActivity.startActivity(intent);
    }

    /**
     * 点赞帖子请求
     *
     * @param postId 帖子id
     */
    private void requestLikePost(int postId) {
        SnsRepository.getInstance()
                .likePost(postId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<LikePostResult>() {
                    @Override
                    public void onSuccess(LikePostResult likePostResult) {
                        if (likePostResult.is_like_now) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, likePostResult.membership_point, AlterPointViewType.AlterPointViewType_Like).parentView(mAvatarView);
                            mPwToast.show();
                        }
                        EventBus.getDefault().post(new PostChangeEvent());
                        loadPostDetailData();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 分享
     */
    @OnClick(R.id.ib_title_share)
    public void share() {
        if (mPostData == null || mPostData.share == null)
            return;
        ShareUtils.showShareBoard(mActivity,
                mPostData.share.title,  // 标题
                TextUtils.isEmpty(mPostData.share.desc) ? "来自55海淘官网直购社区" : mPostData.share.desc,   // 文本
                mPostData.share.url,    // small_icon
                mPostData.share.icon,
                true);  //icon
    }

    /**
     * 点击发送评论
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
            requestSendComment(mPostId, content, mCommentId);
        }
    }

    /**
     * 编辑帖子
     */
    @OnClick(R.id.ib_title_edit)
    public void toEditPost() { // 跳转到编辑帖子页面
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_PostEdit, mPostId + "", mActivity, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_PostEdit + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_PostEdit, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_PostEdit, "");

        PublishPostBean publishPostBean = new PublishPostBean(mPostData.one_word, mPostData.content, mTagNameList, mPostData.images);
        PostEditActivity.toThisActivity(mActivity, mPostId, publishPostBean);
    }

    /**
     * 删除帖子
     */
    @OnClick(R.id.ib_title_delete)
    public void deletePost() {
        new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                .setMessage("是否删除你的笔记?")
                .setPositiveButton("确定", (dialog, which) -> {
                    requestDeletePost();
                })
                .setNegativeButton("取消", (dialog, which) -> {

                })
                .show();
    }


    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        //        loadData();
        requestContent();
    }

    @Subscribe
    public void onPostDetailChangeEvent(PostDetailChangeEvent event) {
        //        loadData();
        requestContent();
    }

    @Subscribe
    public void onAPIErrorEvent(APIErrorEvent event) {
        if (event.code == HaiConstants.ReponseCode.CODE_INVALIDATE_POST) { // 帖子失效
            ToastUtils.showToast(mActivity, getString(R.string.toast_invalidate_post));
            finish();
        }
    }

    /*@Subscribe
    public void onCommentChangeEvent(CommentChangeEvent event) {
        loadPostDetailData();
    }*/

    /**
     * 页面跳转
     *
     * @param context 上下文
     * @param postId  帖子Id
     */
    public static void toThisActivity(Context context, int postId) {
        toThisActivity(context, postId, false);
    }

    /**
     * 页面跳转
     *
     * @param context 上下文
     * @param postId  帖子Id
     * @param newTask 是否开启新栈
     */
    public static void toThisActivity(Context context, int postId, boolean newTask) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra("post_id", postId);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    /**
     * 页面跳转
     *
     * @param context 上下文
     * @param postId  帖子Id
     * @param whRate  图片宽高比
     */
    public static void toThisActivity(Context context, int postId, float whRate) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra("post_id", postId);
        intent.putExtra("wh_rate", whRate);
        context.startActivity(intent);
    }

    /**
     * 跳转到所有点赞页面
     */
    @OnClick(R.id.ll_avator_container)
    public void jumpLikeListActivity() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_PostLikeList, mPostId + "", mActivity, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_ViewLikingUsers + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_PostLikeList, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_ViewLikingUsers, "");

        Intent intent = new Intent(mActivity, LikeListActivity.class);
        intent.putExtra(KEY_POST_ID, mPostId);
        startActivity(intent);
    }

    /**
     * 跳转到他人主页
     */
    @OnClick({R.id.img_avatar, R.id.tv_nickname})
    public void jumpOthersActivity(View view) {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_HomePage, mPostData.owner.id + "", mActivity, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_User + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_PostDetail, TraceUtils.PositionCode_User, mPostData.owner.id + "");

        OthersHomePageActivity.actionStart(mActivity, mPostData.owner.id);
    }

    /**
     * 返回
     */
    @OnClick(R.id.ib_cancel)
    public void clickBack() {
        onBackPressed();
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
    protected void onStop() {
        super.onStop();
        ShareUtils.dismissShareBoard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialog != null)
            mDialog.dismiss();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return TAG  + "?" + "id=" + mPostId;
    }
}
