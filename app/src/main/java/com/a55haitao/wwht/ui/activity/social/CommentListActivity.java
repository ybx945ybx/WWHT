package com.a55haitao.wwht.ui.activity.social;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.CommentListAdapter;
import com.a55haitao.wwht.adapter.social.EasyoptCommentListAdapter;
import com.a55haitao.wwht.data.event.EasyoptCommentCountChangeEvent;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.PostChangeEvent;
import com.a55haitao.wwht.data.model.annotation.AlterPointViewType;
import com.a55haitao.wwht.data.model.annotation.CommentActivityType;
import com.a55haitao.wwht.data.model.entity.CommentBean;
import com.a55haitao.wwht.data.model.entity.EasyoptCommentBean;
import com.a55haitao.wwht.data.model.result.AddEasyoptCommentResult;
import com.a55haitao.wwht.data.model.result.AddPostCommentResult;
import com.a55haitao.wwht.data.model.result.AddPostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostCommentResult;
import com.a55haitao.wwht.data.model.result.DeletePostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.EasyoptCommentLikeResult;
import com.a55haitao.wwht.data.model.result.EasyoptCommentListResult;
import com.a55haitao.wwht.data.model.result.GetPostSpecialCommentsResult;
import com.a55haitao.wwht.data.model.result.LikePostSpecialCommentResult;
import com.a55haitao.wwht.data.model.result.getCommentListResult;
import com.a55haitao.wwht.data.model.result.likePostCommentResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.SpecialRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.varunest.sparkbutton.SparkButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 全部评论列表页
 * <p>
 * CommentActivityType.POST     帖子评论
 * CommentActivityType.SPECIAL  专题评论
 * CommentActivityType.EASYOPT  草单评论
 */
public class CommentListActivity extends BaseNoFragmentActivity {
    @BindView(R.id.title)              DynamicHeaderView     mTitle;            // 标题
    @BindView(R.id.content_view)       RecyclerView          mRvContent;        // 评论列表
    @BindView(R.id.swipe)              HaiSwipeRefreshLayout mSwipe;            // 下拉刷新
    //    @BindView(R.id.tv_title)           TextView              mTvTitle;          // 标题
    @BindView(R.id.et_comment_content) EditText              mEtCommentContent; // 评论内容
    @BindView(R.id.tv_send_comment)    TextView              mTvSendComment;    // 发送评论
    @BindView(R.id.msv_layout)         MultipleStatusView    mSv;               // 多布局容器

    @BindString(R.string.key_post_id)      String KEY_POST_ID;
    @BindString(R.string.key_special_id)   String KEY_SPECIAL_ID;
    @BindString(R.string.key_type)         String KEY_TYPE;
    @BindString(R.string.key_reply_count)  String KEY_REPLY_COUNT;
    @BindString(R.string.key_count)        String KEY_COUNT;
    @BindString(R.string.key_page)         String KEY_PAGE;
    @BindString(R.string.key_comment_list) String KEY_COMMENT_LIST;
    @BindString(R.string.key_content)      String KEY_CONTENT;
    @BindString(R.string.key_comment_id)   String KEY_COMMENT_ID;

    @BindColor(R.color.color_swipe)   int      colorSwipe; // 下拉刷新颜色
    @BindDrawable(R.mipmap.dot_line)  Drawable DIVIDER_BG;
    @BindDimen(R.dimen.avatar_medium) int      AVATAR_SIZE;

    private int mCurrentPage = 1;
    private int                      mId;
    private int                      mCommentId;
    private InputMethodManager       mImm;
    private boolean                  isCommentIng;
    private List<CommentBean>        mComments;         // 帖子、专题评论
    private List<EasyoptCommentBean> mEasyoptComments;  // 草单评论
    private int mAllPage = 1;
    private int mType;

    private Dialog                    mDialog;
    private CommentListAdapter        mAdapter;
    private EasyoptCommentListAdapter mEasyoptCommentAdapter;
    private String                    mEasyoptLogo;
    private String                    mEasyoptName;
    private String                    mEasyoptOwnerNickname;
    private HaiTextView               mTvLatestComment;
    private Tracker                   mTracker; // GA Tracker
    private ToastPopuWindow           mPwToast;
    private boolean                   mIsMine; // 是否是自己的笔记/草单

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
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
            mId = intent.getIntExtra("id", 0);
            mType = intent.getIntExtra(KEY_TYPE, CommentActivityType.POST);
            mIsMine = intent.getBooleanExtra("is_mine", false);
            if (mType == CommentActivityType.EASYOPT) {
                mEasyoptLogo = intent.getStringExtra("easyopt_logo");
                mEasyoptName = intent.getStringExtra("easyopt_name");
                mEasyoptOwnerNickname = intent.getStringExtra("easyopt_owner_nickname");
            }
        }
        mImm = (InputMethodManager) mEtCommentContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mComments = new ArrayList<>();
        mEasyoptComments = new ArrayList<>();
        EventBus.getDefault().register(this);
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(getScreenName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /**
     * 获取页面名称
     *
     * @return screenName
     */
    private String getScreenName() {
        String screenName = "笔记_评论";
        if (mType == CommentActivityType.EASYOPT) {
            screenName = "草单_评论";
        } else if (mType == CommentActivityType.SPECIAL) {
            screenName = "社区专题_评论";
        }
        return screenName;
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        mSwipe.setColorSchemeColors(colorSwipe);

        mRvContent.setLayoutManager(new LinearLayoutManager(this));
        if (mType != CommentActivityType.EASYOPT) {
            mAdapter = new CommentListAdapter(mComments);
            mRvContent.setAdapter(mAdapter);
        } else {
            mEasyoptCommentAdapter = new EasyoptCommentListAdapter(mEasyoptComments, AVATAR_SIZE);

            View headerView = LayoutInflater.from(this).inflate(R.layout.header_activity_comment_list, null, false);
            ((HaiTextView) headerView.findViewById(R.id.tv_easyopt_name)).setText(mEasyoptName);
            ((HaiTextView) headerView.findViewById(R.id.tv_easyopt_owner)).setText(String.format("草单by %s", mEasyoptOwnerNickname));
            mTvLatestComment = (HaiTextView) headerView.findViewById(R.id.tv_latest_comment);
            // 进入草单详情
            headerView.findViewById(R.id.ll_easyopt).setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
            });
            Glide.with(mActivity)
                    .load(mEasyoptLogo)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into((ImageView) headerView.findViewById(R.id.img_easyopt_logo));
            mEasyoptCommentAdapter.addHeaderView(headerView);
            mRvContent.setAdapter(mEasyoptCommentAdapter);
        }
        // 点击事件
        bindListener();
    }

    /**
     * 点击事件
     */
    private void bindListener() {
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                isCommentIng = !isCommentIng;

                if (isCommentIng) {
                    if (mType != CommentActivityType.EASYOPT) {
                        mEtCommentContent.setHint("回复 " + mAdapter.getData().get(position).user_info.nickname + ":");
                        mCommentId = mAdapter.getData().get(position).id;
                    } else {
                        mEtCommentContent.setHint("回复 " + mEasyoptCommentAdapter.getData().get(position).owner.getNickname() + ":");
                        mCommentId = mEasyoptCommentAdapter.getData().get(position).comment_id;
                    }
                    mImm.showSoftInput(mEtCommentContent, 0);
                } else {
                    mEtCommentContent.setHint("请输入您的评论内容");
                    mCommentId = 0;
                    mImm.hideSoftInputFromWindow(mEtCommentContent.getWindowToken(), 0);
                }
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (mType != CommentActivityType.EASYOPT && (mIsMine || mAdapter.getData().get(position).user_info.id == HaiUtils.getUserId())) {
                    showDeleteDlg(mAdapter.getData().get(position).id, position);
                } else if (mType == CommentActivityType.EASYOPT && (mIsMine || mEasyoptCommentAdapter.getData().get(position).owner.getId() == HaiUtils.getUserId())) {
                    showDeleteDlg(mEasyoptCommentAdapter.getData().get(position).comment_id, position);
                }
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_avatar:
                    case R.id.tv_nickname:
                        int userId;
                        if (mType != CommentActivityType.EASYOPT) {
                            userId = mAdapter.getData().get(position).user_info.id;
                        } else {
                            userId = mEasyoptCommentAdapter.getData().get(position).owner.getId();
                        }

                        // 埋点
//                        TraceUtils.addClick(TraceUtils.PageCode_HomePage, userId + "", mActivity, mType == CommentActivityType.EASYOPT ? TraceUtils.PageCode_EasyComments : TraceUtils.PageCode_CommentsList, TraceUtils.PositionCode_User + "", "");

//                        TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, mType == CommentActivityType.EASYOPT ? TraceUtils.PageCode_EasyComments : TraceUtils.PageCode_CommentsList, TraceUtils.PositionCode_User, userId + "");

                        OthersHomePageActivity.actionStart(mActivity, userId);
                        break;
                    case R.id.sb_like:
                        SparkButton sb = (SparkButton) view;
                        TextView tvLikeCount = (TextView) ((ViewGroup) sb.getParent()).findViewById(R.id.tv_like_count);
                        if (HaiUtils.needLogin(mActivity)) {
                            if (mType != CommentActivityType.EASYOPT) {
                                sb.setChecked(mAdapter.getData().get(position).is_liked);
                            } else {
                                sb.setChecked(mEasyoptCommentAdapter.getData().get(position).islike == 1);
                            }
                        } else {
                            if (mType != CommentActivityType.EASYOPT) {
                                requestLikeComment(mAdapter.getData().get(position).id, sb, tvLikeCount, position);
                            } else {
                                requestLikeComment(mEasyoptCommentAdapter.getData().get(position).comment_id, sb, tvLikeCount, position);
                            }
                        }
                        break;
                }
            }
        });

        // 加载更多
        if (mType != CommentActivityType.EASYOPT) {
            mAdapter.setOnLoadMoreListener(() -> {
                mRvContent.post(() -> {
                    if (mAdapter.getData().size() < PAGE_SIZE) {
                        mAdapter.loadMoreEnd(true);
                    } else if (mCurrentPage < mAllPage) {
                        mCurrentPage++;
                        mSwipe.setEnabled(false);
                        requestGetCommentData();
                    }
                });
            });
        } else {
            mEasyoptCommentAdapter.setOnLoadMoreListener(() -> {
                mRvContent.post(() -> {
                    if (mEasyoptCommentAdapter.getData().size() < PAGE_SIZE) {
                        mEasyoptCommentAdapter.loadMoreEnd(true);
                    } else if (mCurrentPage < mAllPage) {
                        mCurrentPage++;
                        mSwipe.setEnabled(false);
                        requestGetCommentData();
                    }
                });
            });
        }
        // 刷新
        mSwipe.setOnRefreshListener(this::refreshData);
        // 重试
        mSv.setOnRetryClickListener(v1 -> loadData());
    }

    private void refreshData() {
        if (mType != CommentActivityType.EASYOPT) {
            mAdapter.setEnableLoadMore(false);
        } else {
            mEasyoptCommentAdapter.setEnableLoadMore(false);
        }
        loadData();
    }

    /**
     * 加载数据
     */
    public void loadData() {
        mSwipe.setRefreshing(true);
        mCurrentPage = 1;
        requestGetCommentData();
    }

    /**
     * 请求评论数据
     */
    private void requestGetCommentData() {
        if (mType == CommentActivityType.POST) {
            SnsRepository.getInstance()
                    .getCommentList(mId, mCurrentPage)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<getCommentListResult>() {
                        @Override
                        public void onSuccess(getCommentListResult result) {
                            if (result.count == 0) {
                                mSv.showEmpty();
                                mAdapter.notifyDataSetChanged();
                                mAdapter.loadMoreFail();
                            } else {
                                mSv.showContent();
                                mTitle.setHeadTitle(String.format("评论(%d)", result.count));
                                mAllPage = result.allpage;
                                if (mSwipe.isRefreshing()) {
                                    mAdapter.setNewData(result.reply_list);
                                } else {
                                    mAdapter.addData(result.reply_list);
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
                            mSwipe.setEnabled(true);
                            mSwipe.setRefreshing(false);
                            mAdapter.setEnableLoadMore(true);
                        }
                    });
        } else if (mType == CommentActivityType.SPECIAL) {
            SpecialRepository.getInstance()
                    .getPostSpecialComments(mId, mCurrentPage)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<GetPostSpecialCommentsResult>() {
                        @Override
                        public void onSuccess(GetPostSpecialCommentsResult result) {
                            if (result.count == 0) {
                                mSv.showEmpty();
                                mAdapter.notifyDataSetChanged();
                                mAdapter.loadMoreFail();
                            } else {
                                mSv.showContent();
                                mTitle.setHeadTitle(String.format("评论(%d)", result.count));
                                mAllPage = result.allpage;
                                if (mSwipe.isRefreshing()) {
                                    mAdapter.setNewData(result.comments);
                                } else {
                                    mAdapter.addData(result.comments);
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
                            mSwipe.setEnabled(true);
                            mSwipe.setRefreshing(false);
                            mAdapter.setEnableLoadMore(true);
                        }
                    });
        } else if (mType == CommentActivityType.EASYOPT) {
            SnsRepository.getInstance()
                    .easyoptCommentList(mId, mCurrentPage)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<EasyoptCommentListResult>() {
                        @Override
                        public void onSuccess(EasyoptCommentListResult result) {
                            if (result.count == 0) {
                                mSv.showEmpty();
                                mEasyoptCommentAdapter.notifyDataSetChanged();
                                mEasyoptCommentAdapter.loadMoreFail();
                            } else {
                                mSv.showContent();
                                mTitle.setHeadTitle(String.format("评论(%d)", result.total_count));
                                mTvLatestComment.setText(String.format("最新评论(%d)", result.total_count));
                                mAllPage = result.allpage;
                                if (mSwipe.isRefreshing()) {
                                    mEasyoptCommentAdapter.setNewData(result.comments);
                                } else {
                                    mEasyoptCommentAdapter.addData(result.comments);
                                    if (mEasyoptCommentAdapter.getData().size() > PAGE_SIZE && mEasyoptCommentAdapter.getData().size() >= result.count) {
                                        mEasyoptCommentAdapter.loadMoreEnd();
                                    } else {
                                        mEasyoptCommentAdapter.loadMoreComplete();
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
                            mSwipe.setEnabled(true);
                            mSwipe.setRefreshing(false);
                            mEasyoptCommentAdapter.setEnableLoadMore(true);
                        }
                    });

        }

    }

    /**
     * 点赞评论请求
     *
     * @param id          评论Id
     * @param sb          点赞按钮
     * @param tvLikeCount 点赞数
     */
    private void requestLikeComment(int id, SparkButton sb, TextView tvLikeCount, int position) {
        if (mType == CommentActivityType.POST) {
            SnsRepository.getInstance()
                    .likePostComment(id)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<likePostCommentResult>() {
                        @Override
                        public void onSuccess(likePostCommentResult result) {
                            sb.setChecked(result.is_like_now);
                            tvLikeCount.setText(result.like_count > 0 ? String.valueOf(result.like_count) : "");

                            CommentBean comment = mAdapter.getData().get(position);
                            comment.is_liked = result.is_like_now;
                            comment.like_count = result.like_count;
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        } else if (mType == CommentActivityType.SPECIAL) {
            SpecialRepository.getInstance()
                    .likePostSpecialComment(id)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<LikePostSpecialCommentResult>() {
                        @Override
                        public void onSuccess(LikePostSpecialCommentResult result) {
                            sb.setChecked(result.liked);
                            tvLikeCount.setText(result.like_count > 0 ? String.valueOf(result.like_count) : "");

                            CommentBean comment = mAdapter.getData().get(position);
                            comment.is_liked = result.liked;
                            comment.like_count = result.like_count;
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        } else if (mType == CommentActivityType.EASYOPT) {
            SnsRepository.getInstance()
                    .easyoptCommentLike(id)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<EasyoptCommentLikeResult>() {
                        @Override
                        public void onSuccess(EasyoptCommentLikeResult result) {
                            sb.setChecked(result.islike == 1);
                            tvLikeCount.setText(result.like_count > 0 ? String.valueOf(result.like_count) : "");

                            EasyoptCommentBean comment = mEasyoptCommentAdapter.getData().get(position);
                            comment.islike = result.islike;
                            comment.like_count = result.like_count;
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        }
    }

    /**
     * 发送评论
     *
     * @param content 评论内容
     */
    private void requestSendComment(String content) {
        if (mType == CommentActivityType.POST) {
            SnsRepository.getInstance()
                    .addPostComment(mId, content, mCommentId)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<AddPostCommentResult>() {
                        @Override
                        public void onSuccess(AddPostCommentResult result) {
                            mEtCommentContent.setText("");
                            mEtCommentContent.setHint("请输入您的评论内容");
                            mCommentId = 0;
                            mImm.hideSoftInputFromWindow(mEtCommentContent.getWindowToken(), 0);
                            mPwToast = ToastPopuWindow.makeText(mActivity, result.membership_point, AlterPointViewType.AlterPointViewType_Comment).parentView(mEtCommentContent);
                            mPwToast.show();
                            EventBus.getDefault().post(new PostChangeEvent());
                            // 评论完之后刷新数据
                            refreshData();
                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    });
        } else if (mType == CommentActivityType.SPECIAL) {
            SpecialRepository.getInstance()
                    .addPostSpecialComment(mId, content, mCommentId)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<AddPostSpecialCommentResult>() {
                        @Override
                        public void onSuccess(AddPostSpecialCommentResult addPostSpecialCommentResult) {
                            mEtCommentContent.setText("");
                            mEtCommentContent.setHint("请输入您的评论内容");
                            mCommentId = 0;
                            mImm.hideSoftInputFromWindow(mEtCommentContent.getWindowToken(), 0);
                            mPwToast = ToastPopuWindow.makeText(mActivity, addPostSpecialCommentResult.membership_point, AlterPointViewType.AlterPointViewType_Comment).parentView(mTvSendComment);
                            mPwToast.show();
                            EventBus.getDefault().post(new PostChangeEvent());
                            // 评论完之后刷新数据
                            refreshData();
                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    });
        } else if (mType == CommentActivityType.EASYOPT) {
            SnsRepository.getInstance()
                    .easyoptComment(mId, content, mCommentId)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<AddEasyoptCommentResult>() {
                        @Override
                        public void onSuccess(AddEasyoptCommentResult result) {
                            mEtCommentContent.setText("");
                            mEtCommentContent.setHint("请输入您的评论内容");
                            mCommentId = 0;
                            mImm.hideSoftInputFromWindow(mEtCommentContent.getWindowToken(), 0);
                            // 草单评论数改变
                            EventBus.getDefault().post(new EasyoptCommentCountChangeEvent(result.reply_count));
                            // 评论完之后刷新数据
                            refreshData();
                        }

                        @Override
                        public void onFinish() {
                            dismissProgressDialog();
                        }
                    });
        }
    }

    /**
     * 检查评论
     */
    private boolean checkComment(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showToast(mActivity, "评论内容不能为空");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 弹出删除评论对话框
     *
     * @param id       评论id
     * @param position 位置
     */
    private void showDeleteDlg(int id, int position) {
        mDialog = new Dialog(mActivity);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View     v    = LayoutInflater.from(mActivity).inflate(R.layout.dlg_single_choice, null);
        HaiTextView desc = (HaiTextView) v.findViewById(R.id.tv_desc);
        desc.setText("删除评论");
        desc.setOnClickListener(view -> requestDeleteComment(id));
        mDialog.setContentView(v);
        mDialog.show();
    }

    /**
     * 删除评论网络请求
     *
     * @param commentId 评论id
     */
    private void requestDeleteComment(int commentId) {
        if (mType == CommentActivityType.POST) {
            SnsRepository.getInstance()
                    .deletePostComment(commentId)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<DeletePostCommentResult>() {
                        @Override
                        public void onSuccess(DeletePostCommentResult result) {
                            if (result.success) {
                                ToastUtils.showToast(mActivity, "删除成功");
                                EventBus.getDefault().post(new PostChangeEvent());
                                refreshData();
                            }
                        }

                        @Override
                        public void onFinish() {
                            mDialog.dismiss();
                        }
                    });
        } else if (mType == CommentActivityType.SPECIAL) {
            SpecialRepository.getInstance()
                    .deletePostSpecialComment(commentId)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<DeletePostSpecialCommentResult>() {
                        @Override
                        public void onSuccess(DeletePostSpecialCommentResult result) {
                            if (result.success) {
                                ToastUtils.showToast(mActivity, "删除成功");
                                EventBus.getDefault().post(new PostChangeEvent());
                                refreshData();
                            }
                        }

                        @Override
                        public void onFinish() {
                            mDialog.dismiss();
                        }
                    });
        } else if (mType == CommentActivityType.EASYOPT) {
            SnsRepository.getInstance()
                    .easyoptCommentDel(commentId, mId)
                    .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                    .subscribe(new DefaultSubscriber<Object>() {
                        @Override
                        public void onSuccess(Object obj) {
                            ToastUtils.showToast(mActivity, "删除成功");
                            refreshData();
                        }

                        @Override
                        public void onFinish() {
                            mDialog.dismiss();
                        }
                    });
        }
    }

    /**
     * 评论
     */
    @OnClick(R.id.tv_send_comment)
    public void sendComment() {
        if (HaiUtils.needLogin(mActivity))
            return;

        String content = mEtCommentContent.getText().toString();
        if (checkComment(content)) {
            showProgressDialog();
            requestSendComment(content);
        }
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        refreshData();
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param type    类型
     * @param id      id
     */
    public static void toThisActivity(Context context, @CommentActivityType int type, int id) {
        toThisActivity(context, type, id, false);
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param type    类型
     * @param id      id
     * @param isMine  是否是自己的帖子/草单
     */
    public static void toThisActivity(Context context, @CommentActivityType int type, int id, boolean isMine) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        intent.putExtra("is_mine", isMine);
        context.startActivity(intent);
    }

    /**
     * 跳转到本页面
     *
     * @param context              context
     * @param easyoptId            草单Id
     * @param easyoptLogo          草单Logo
     * @param easyoptName          草单名
     * @param easyoptOwnerNickname 草单作者昵称
     */
    public static void toThisActivityEasyopt(Context context, int easyoptId, String easyoptLogo, String easyoptName, String easyoptOwnerNickname) {
        toThisActivityEasyopt(context, easyoptId, easyoptLogo, easyoptName, easyoptOwnerNickname, false);
    }

    /**
     * 跳转到本页面
     *
     * @param context              context
     * @param easyoptId            草单Id
     * @param easyoptLogo          草单Logo
     * @param easyoptName          草单名
     * @param easyoptOwnerNickname 草单作者昵称
     * @param isMine               是否是自己的草单
     */
    public static void toThisActivityEasyopt(Context context, int easyoptId, String easyoptLogo, String easyoptName, String easyoptOwnerNickname, boolean isMine) {
        Intent intent = new Intent(context, CommentListActivity.class);
        intent.putExtra("type", CommentActivityType.EASYOPT);
        intent.putExtra("id", easyoptId);
        intent.putExtra("easyopt_logo", easyoptLogo);
        intent.putExtra("easyopt_name", easyoptName);
        intent.putExtra("easyopt_owner_nickname", easyoptOwnerNickname);
        intent.putExtra("is_mine", isMine);
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSwipe.setRefreshing(false);
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
