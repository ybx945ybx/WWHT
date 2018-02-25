package com.a55haitao.wwht.ui.fragment.easyopt;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.easyopt.EasyoptListAdapter;
import com.a55haitao.wwht.data.event.EasyOptLikeCountChangeEvent;
import com.a55haitao.wwht.data.event.EasyoptListChangeEvent;
import com.a55haitao.wwht.data.interfaces.ILoad;
import com.a55haitao.wwht.data.model.entity.CommCountsBean;
import com.a55haitao.wwht.data.model.entity.EasyOptBean;
import com.a55haitao.wwht.data.model.entity.EasyOptTagBean;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.EasyOptListResult;
import com.a55haitao.wwht.data.model.result.EasyoptStarResult;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.easyopt.EasyOptDetailActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import rx.Observable;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 草单Fragment
 */
public class EasyOptFragment extends BaseFragment implements ILoad {

    @BindView(R.id.content_view) RecyclerView          mRvContent;
    @BindView(R.id.msView)       MultipleStatusView    mSv;
    @BindView(R.id.swipe)        HaiSwipeRefreshLayout mSwipe;

    private final static int TYPE_TAG   = 1;
    private final static int TYPE_OWER  = 2;
    private final static int TYPE_LIKED = 3;
    protected            int PAGE_SIZE  = 10;

    private EasyOptTagBean mTagBean;
    private int            mOtherUserId;
    private int            mType;

    private EasyoptListAdapter     mAdapter;
    private Unbinder               mUnbinder;
    private ArrayList<EasyOptBean> mEasyoptList;
    private int                    mAllPage;
    private int                    mCurrentPage;

    public static EasyOptFragment newInstanceForTag(EasyOptTagBean bean) {
        EasyOptFragment f      = new EasyOptFragment();
        Bundle          bundle = new Bundle();
        bundle.putParcelable("data", bean);
        bundle.putInt("type", TYPE_TAG);
        f.setArguments(bundle);
        return f;
    }

    public static EasyOptFragment newInstanceForOwer(int userId) {
        EasyOptFragment f      = new EasyOptFragment();
        Bundle          bundle = new Bundle();
        bundle.putInt("data", userId);
        bundle.putInt("type", TYPE_OWER);
        f.setArguments(bundle);
        return f;
    }

    public static EasyOptFragment newInstanceForLiked() {
        EasyOptFragment f      = new EasyOptFragment();
        Bundle          bundle = new Bundle();
        bundle.putInt("type", TYPE_LIKED);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVars();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_easy_opt, container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initViews();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    private void initVars() {
        Bundle b = getArguments();
        mType = b.getInt("type");

        mAllPage = 1;
        mCurrentPage = 1;

        if (mType == TYPE_TAG) {
            mTagBean = b.getParcelable("data");
        } else if (mType == TYPE_OWER) {
            mOtherUserId = b.getInt("data", -1);
        } else if (mType == TYPE_LIKED) {

        }
        EventBus.getDefault().register(this);
        mEasyoptList = new ArrayList<>();
    }

    private void initViews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mRvContent.setLayoutManager(layoutManager);
        // adapter
        mAdapter = new EasyoptListAdapter(mEasyoptList);
        mRvContent.setAdapter(mAdapter);
        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                // 埋点
//                String page = "";
//                if (mType == TYPE_TAG) {
//                    page = TraceUtils.PageCode_Album;
//
//                } else if (mType == TYPE_OWER) {
//                    page = TraceUtils.PageCode_MyEasy;
//
//                } else if (mType == TYPE_LIKED) {
//                    page = TraceUtils.PageCode_HomePage;
//
//                }
//                TraceUtils.addClick(TraceUtils.PageCode_EasyDetail, mAdapter.getData().get(position).easyopt_id + "", mActivity, page, TraceUtils.PositionCode_Album + "", "");

                YbxTrace.getInstance().event(mActivity, ((BaseActivity)mActivity).pref, ((BaseActivity)mActivity).prefh, ((BaseActivity)mActivity).purl, ((BaseActivity)mActivity).purlh, "", TraceUtils.PositionCode_Album, TraceUtils.Event_Category_Click, "", null, "");

                // 草单详情
                EasyOptDetailActivity.toThisActivity(getContext(), mAdapter.getData().get(position).easyopt_id);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EasyOptBean easyOptBean = mAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.imgHeart: // 收藏
                        SnsRepository.getInstance()
                                .easyoptLike(easyOptBean.easyopt_id)
                                .compose(ActivityCollector.getTopActivity().bindToLifecycle())
                                .subscribe(new DefaultSubscriber<EasyoptStarResult>() {
                                    @Override
                                    public void onSuccess(EasyoptStarResult result) {
                                        easyOptBean.likeit = result.islike;
                                        easyOptBean.like_count = result.like_count;

                                        ((CheckedTextView) view).setChecked(result.islike == 1);
                                        if (result.islike == 1) {         //收藏成功
                                            ToastUtils.showToast("收藏成功");
                                        }

                                        Realm.getDefaultInstance()
                                                .executeTransactionAsync(realm -> {
                                                    UserBean       user       = realm.where(UserBean.class).findFirst();
                                                    CommCountsBean commCounts = user.getCommCounts();
                                                    if (commCounts != null) {
                                                        commCounts.setEasyopt_start_count(result.user_like_count);
                                                    }
                                                });
                                        EventBus.getDefault().post(new EasyOptLikeCountChangeEvent(result.user_like_count));
                                        EventBus.getDefault().post(new EasyoptListChangeEvent());

                                        mAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                });
                        break;
                }
            }
        });
        // 重试
        mSv.setOnRetryClickListener(v -> loadData());
        // 刷新
        mSwipe.setOnRefreshListener(() -> {
            /*page = LoadType.LOAD_REFRESH;
            loadData();*/
            mAdapter.setEnableLoadMore(false);
            loadData();
        });
        // 加载更多
        mAdapter.setOnLoadMoreListener(() -> mRvContent.post(() -> {
            if (hasMore()) {
                mSwipe.setEnabled(false);
                mCurrentPage++;
                requestEasyoptList();
            }
        }));
    }

    private void loadData() {
        mCurrentPage = 1;
        mSwipe.setRefreshing(true);
        requestEasyoptList();
    }

    private void requestEasyoptList() {
        Observable<EasyOptListResult> observable = null;

        if (mType == TYPE_TAG) {
            //            observable = EasyOptRepoisty.getInstance().getListWithTag(mTagBean.id, HaiUtils.getPage(page));
            observable = SnsRepository.getInstance().easyoptList4Tag(mTagBean.id, mCurrentPage);
        } else if (mType == TYPE_OWER) {
            //            observable = EasyOptRepoisty.getInstance().getListWithOwer(mOtherUserId, HaiUtils.getPage(page));
            observable = SnsRepository.getInstance().easyoptList4User(mOtherUserId, mCurrentPage);
        } else if (mType == TYPE_LIKED) {
            //            observable = EasyOptRepoisty.getInstance().getListWithLiked(HaiUtils.getPage(page));
            observable = SnsRepository.getInstance().easyoptList4Like(mCurrentPage);
        }

        observable.compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<EasyOptListResult>() {
                    @Override
                    public void onSuccess(EasyOptListResult result) {

                        mCurrentPage = result.page;
                        mAllPage = result.allpage;

                        if (HaiUtils.getSize(result.easyopts) == 0) {
                            if (mCurrentPage == 1) {
                                mSv.showEmpty();
                                mAdapter.notifyDataSetChanged();
                                mAdapter.loadMoreFail();
                            }else {
                                mAdapter.loadMoreEnd();
                            }
                        } else {
                            mSv.showContent();
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.easyopts);
                                if (hasMore()) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    if (HaiUtils.getSize(result.easyopts) > 3){
                                        mAdapter.loadMoreEnd();
                                    }else {
                                        mAdapter.loadMoreEnd(true);
                                    }
                                }
                            } else {
                                mAdapter.addData(result.easyopts);
                                if (hasMore()) {
                                    mAdapter.loadMoreComplete();
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            }
                        }
                        mHasData = true;
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setEnabled(true);
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasData);
                        return mHasData;
                    }
                });
    }

    public String getTitle() {
        return mTagBean.name;
    }

    @Override
    public boolean hasMore() {
        return mCurrentPage < mAllPage;
    }

    @Override
    public void loadMore() {
        //        loadData();
        requestEasyoptList();
    }

    @Subscribe
    public void onEOListChange(EasyoptListChangeEvent event) {
        if (mType == TYPE_OWER) {
            com.orhanobut.logger.Logger.d(((BaseActivity) getActivity()).mIsResumed);
        }
        /*if (mType == TYPE_TAG || mType == TYPE_OWER && getUserVisibleHint()) {
            return;
        }*/
        if (mType == TYPE_TAG || mType == TYPE_OWER && ((BaseActivity) getActivity()).mIsResumed && getUserVisibleHint()) {
            return;
        }
        loadData();
    }

    @Subscribe
    public void onEasyoptStarChange(EasyOptLikeCountChangeEvent event) {   //  收藏草单事件
        for (int i = 0; i < HaiUtils.getSize(mAdapter.getData()); i++) {
            if (mAdapter.getItem(i).easyopt_id == event.easyoptId) {
                mAdapter.getItem(i).like_count = event.count;
                mAdapter.getItem(i).likeit = event.likeit;
                mAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHasData = false;
        mUnbinder.unbind();
    }
}
