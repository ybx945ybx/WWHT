package com.a55haitao.wwht.ui.fragment.firstpage;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.FirstPageHotAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.interfaces.IReponse;
import com.a55haitao.wwht.data.model.annotation.FirstPageViewType;
import com.a55haitao.wwht.data.model.entity.ActivityBean;
import com.a55haitao.wwht.data.model.entity.DailyProductBean;
import com.a55haitao.wwht.data.model.entity.FirstPageBodyBean;
import com.a55haitao.wwht.data.model.entity.HotFavorableBean;
import com.a55haitao.wwht.data.model.result.DailyProductResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.TestSubscriber;
import com.a55haitao.wwht.data.net.api.HomeService;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.ActivityPopupWindow;
import com.a55haitao.wwht.ui.view.GoCommentPopupWindow;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页热门Fragment
 */
public class FirstpageHotFragment extends BaseFragment {

    public static final String KEY_TAB_NAME = "k_t_n";

    private String              mTabName;
    private RecyclerView        mContentRecyclerView;
    private SwipeRefreshLayout  mSwipeRefreshLayout;
    private LinearLayout        ivQuickTop;
    private FirstPageHotAdapter mAdapter;
    private MultipleStatusView  mSview;

    private int mPage;                      //  超值单品分页
    private int totalPage;                  //  超值单品分页总页
    private int totalCount;                 //  超值单品总个数
    private int firstCount;                 //  超值单品第一页个数

    private int lastVisibleItemPosition;    // 最后一个可见的item，用于判断是否显示隐藏回到顶部按钮
    private static final int Quick_Top_Visible_Count = 30;   // 设为显示大于40个后显示回到顶部按钮

    public static FirstpageHotFragment newInstance(String tabName) {
        FirstpageHotFragment fragment = new FirstpageHotFragment();
        Bundle               bundle   = new Bundle();
        bundle.putString(KEY_TAB_NAME, tabName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabName = getArguments().getString(KEY_TAB_NAME);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firstpage_hot, container, false);

        initViews(view);
        initEvents();
        initData();

        return view;
    }

    private void initViews(View view) {
        mSwipeRefreshLayout = findViewById(view, R.id.content_view);
        mSview = (MultipleStatusView) view.findViewById(R.id.msView);
        mContentRecyclerView = findViewById(view, R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mContentRecyclerView.setLayoutManager(layoutManager);
        mContentRecyclerView.setNestedScrollingEnabled(false);
        mContentRecyclerView.setHasFixedSize(true);
        mAdapter = new FirstPageHotAdapter(mActivity, new ArrayList<>());
        mAdapter.setTabName(mTabName);
        mContentRecyclerView.setAdapter(mAdapter);

        ivQuickTop = findViewById(view, R.id.iv_quick_top);

    }

    private void initEvents() {
        ivQuickTop.setOnClickListener(v -> mContentRecyclerView.scrollToPosition(0));
        mSview.setOnRetryClickListener(v -> {
            mSwipeRefreshLayout.setRefreshing(true);
            if (mAdapter != null) {
                mAdapter.stopMarqueeView();
            }
            requestData();
        });
        mSwipeRefreshLayout.setOnRefreshListener(() -> requestData());
        mAdapter.setOnLoadMoreListener(() -> {
            if (hasMore()) {
                loadMore();
            }
        }, mContentRecyclerView);
        mContentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                LinearLayoutManager        linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//                if (lastVisibleItemPosition > Quick_Top_Visible_Count) {
//                    ivQuickTop.setVisibility(View.VISIBLE);
//                }else {
//                    ivQuickTop.setVisibility(View.GONE);
//                }
            }
        });
    }

    private void initData() {
        mSwipeRefreshLayout.setRefreshing(true);
        requestData();
    }

    private void requestData() {
        mPage = 1;

        int uid = 0;
        if (HaiUtils.isLogin()) {
            uid = UserRepository.getInstance().getUserInfo().getId();
        }

        RetrofitFactory.createService(HomeService.class)
                .retrieveTabDataWithName("index", uid, "2.10")
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new TestSubscriber<>(new IReponse<JSONArray>() {
                    @Override
                    public void onSuccess(JSONArray s) {       //处理首页数据

                        Subscriber<ArrayList<FirstPageBodyBean>> observer = new Subscriber<ArrayList<FirstPageBodyBean>>() {
                            @Override
                            public void onCompleted() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onNext(ArrayList<FirstPageBodyBean> firstPageBodyBeen) {
                                mSview.showContent();
                                mAdapter.setNewData(firstPageBodyBeen);
                                mAdapter.setSysTime();
                                mAdapter.notifyDataSetChanged();
                                // 是否弹出活动框
                                checkIfPromptActivityWindow();
                            }
                        };

                        Observable.just(s)
                                .map(jsonArray -> {
                                    ArrayList<FirstPageBodyBean> arrayList = new ArrayList();   //修饰后的数据

                                    for (int i = 0; i < s.length(); i++) {
                                        try {
                                            JSONObject        item     = s.getJSONObject(i);
                                            FirstPageBodyBean itemBean = new FirstPageBodyBean();
                                            itemBean.parse(item);
                                            arrayList.add(itemBean);
                                        } catch (JsonSyntaxException | JSONException ignored) {

                                        }
                                    }

                                    return initData(arrayList);
                                })
                                .compose(RxLifecycle.bindUntilEvent(lifecycle(), FragmentEvent.DESTROY))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(observer);
                    }

                    @Override
                    public void onFinish() {
                        mHasData = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mHasData) {
                            showFailView(e);
                        } else {
                            showFailView(mSview, e, mHasData);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }));

    }

    private boolean isPromptActivityWindow = false;

    private void checkIfPromptActivityWindow() {

        if (isPromptActivityWindow) return;
        isPromptActivityWindow = true;

        HomeRepository.getInstance()
                .getActivity()
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), FragmentEvent.DESTROY))
                .subscribe(new DefaultSubscriber<ActivityBean>() {
                    @Override
                    public void onSuccess(ActivityBean activityBean) {
                        ActivityPopupWindow activityPopupWindow = new ActivityPopupWindow(mActivity, activityBean);
                        activityPopupWindow.setActivityPopuWindowInterface(uri -> HaiUriMatchUtils.matchUri(getActivity(), uri));
                        activityPopupWindow.showOrDismiss(mContentRecyclerView);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    private void loadMore() {
        mPage++;
        HomeRepository.getInstance()
                .getDailyProduct(mPage)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<DailyProductResult>() {
                    @Override
                    public void onSuccess(DailyProductResult dailyProductResult) {
                        mPage = dailyProductResult.pagenation.page;
                        totalPage = dailyProductResult.pagenation.allpage;

                        if (HaiUtils.getSize(dailyProductResult.products) > 0) {
                            ArrayList<FirstPageBodyBean> arrayList = new ArrayList();
                            for (DailyProductBean dailyBean : dailyProductResult.products) {
                                FirstPageBodyBean firstPageBodyBean = new FirstPageBodyBean();
                                firstPageBodyBean.setViewType(FirstPageViewType.Daily_Product);
                                firstPageBodyBean.setmData(dailyBean);
                                arrayList.add(firstPageBodyBean);
                            }
                            mAdapter.addData(arrayList);
                            mAdapter.notifyDataSetChanged();
                        }

                        if (hasMore()) {
                            mAdapter.loadMoreComplete();
                        } else {
                            mAdapter.loadMoreEnd();
                        }

                    }

                    @Override
                    public void onFinish() {
                    }
                });
        // 第一次划到第三页的时候  弹出去应用市场评论的提示框
        if (mPage == 3 && !(Boolean) SPUtils.get(mActivity, "isComment", false)) {
            GoCommentPopupWindow commentPopupWindow = new GoCommentPopupWindow(mActivity);
            new Handler().postDelayed(() -> commentPopupWindow.showOrDismiss(mContentRecyclerView), 300);
            SPUtils.put(mActivity, "isComment", true);

        }
    }

    private boolean hasMore() {
        if (mPage == 1) {
            return firstCount < totalCount;
        }
        return mPage < totalPage;
    }

    // 初始化首页热门数据
    private ArrayList<FirstPageBodyBean> initData(ArrayList<FirstPageBodyBean> originDatas) {
        ArrayList<FirstPageBodyBean> arrayList = new ArrayList();   //修饰后的数据

        for (FirstPageBodyBean bean : originDatas) {
            switch (bean.getViewType()) {
                case FirstPageViewType.Banner:              // banner、
                case FirstPageViewType.MallStatement:       // 4个icon
                case FirstPageViewType.NewsFlash:           // 优惠快报
                case FirstPageViewType.Ads:                 // 活动位
                    arrayList.add(bean);
                    break;
                case FirstPageViewType.FlashSale:           // 限时抢购
                    arrayList.add(creatTextBean("限 时 抢 购"));
                    arrayList.add(bean);
                    break;
                case FirstPageViewType.Image_Flavor_Hot:
                    arrayList.add(creatTextBean("官 网 特 卖"));

                    HotFavorableBean favorableTemp = (HotFavorableBean) bean.getData();
                    for (Object object : favorableTemp.favorables) {
                        if (((HotFavorableBean.SpecialsBean) object).type == 0) {   //  只加单列单列
                            FirstPageBodyBean bodyBean = new FirstPageBodyBean();
                            bodyBean.setViewType(FirstPageViewType.Image_Flavor_Hot);
                            bodyBean.setmData(((HotFavorableBean.SpecialsBean) object).special_items.get(0));
                            arrayList.add(bodyBean);
                        }
                        //                        else {
                        //                            FirstPageBodyBean bodyBean = new FirstPageBodyBean();
                        //                            bodyBean.setViewType(FirstPageViewType.Image_Flavor_Twice_Hot);
                        //                            bodyBean.setmData(((HotFavorableBean.SpecialsBean) object).special_items);
                        //                            arrayList.add(bodyBean);
                        //                        }
                    }
                    // 底部添加查看全部
                    FirstPageBodyBean bodyBean = new FirstPageBodyBean();
                    bodyBean.setViewType(FirstPageViewType.Flavor_Hot_Foot_All);
                    bodyBean.setmData("首页官网特卖查看全部");
                    arrayList.add(bodyBean);
                    break;
                case FirstPageViewType.Daily_Product:
                    arrayList.add(creatTextBean("超 值 单 品"));
                    DailyProductResult productResult = (DailyProductResult) bean.getData();
                    totalCount = productResult.count;
                    firstCount = HaiUtils.getSize(productResult.products);

                    for (DailyProductBean dailyBean : productResult.products) {
                        FirstPageBodyBean firstPageBodyBean = new FirstPageBodyBean();
                        firstPageBodyBean.setViewType(FirstPageViewType.Daily_Product);
                        firstPageBodyBean.setmData(dailyBean);
                        arrayList.add(firstPageBodyBean);
                    }
                    break;

            }
        }

        return arrayList;
    }

    // 标题
    private FirstPageBodyBean creatTextBean(String text) {
        FirstPageBodyBean bodyBean = new FirstPageBodyBean();
        bodyBean.setViewType(FirstPageViewType.SubTitle);
        bodyBean.setmData(text);
        return bodyBean;
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {   // 用户登录状态改变后重新请求数据刷新banner
        requestData();
    }

    @Override
    public void onVisibilityChangedToUser(boolean isVisibleToUser) {
        super.onVisibilityChangedToUser(isVisibleToUser);
        if (mAdapter != null) {
            if (isVisibleToUser) {
                mAdapter.starTurning();
            } else {
                mAdapter.pauseTurning();
            }
        }
    }

}
