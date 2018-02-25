package com.a55haitao.wwht.ui.fragment.firstpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.FirstPageTabAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ProductLikeEvent;
import com.a55haitao.wwht.data.interfaces.IReponse;
import com.a55haitao.wwht.data.model.annotation.FirstPageViewType;
import com.a55haitao.wwht.data.model.entity.FirstPageBodyBean;
import com.a55haitao.wwht.data.model.entity.HotFavorableBean;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.data.net.RetrofitFactory;
import com.a55haitao.wwht.data.net.TestSubscriber;
import com.a55haitao.wwht.data.net.api.HomeService;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.gson.JsonSyntaxException;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.FragmentEvent;

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
 * 首页频道页Fragment
 */
public class FirstpageTabFragment extends BaseFragment {

    public static final String KEY_TAB_NAME = "k_t_n";

    private RecyclerView        mContentRecyclerView;
    private SwipeRefreshLayout  mSrlyt;
    private FirstPageTabAdapter mAdapter;
    private MultipleStatusView  mSview;

    private String mTabName;

    public static FirstpageTabFragment newInstance(String tabName) {
        FirstpageTabFragment fragment = new FirstpageTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_firstpage_tab, container, false);

        initViews(view);
        initEvents();
        initData();

        return view;
    }

    private void initViews(View view) {
        mSview = (MultipleStatusView) view.findViewById(R.id.ms_view);
        mSrlyt = findViewById(view, R.id.content_view);
        mContentRecyclerView = findViewById(view, R.id.recyclerView);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mContentRecyclerView.setNestedScrollingEnabled(false);
        mContentRecyclerView.setHasFixedSize(true);
        mAdapter = new FirstPageTabAdapter(mActivity, new ArrayList<>());
        mAdapter.setTabName(mTabName);
        mContentRecyclerView.setAdapter(mAdapter);

    }

    private void initEvents() {
        mSview.setOnRetryClickListener(v -> {
            mSrlyt.setRefreshing(true);
            requestData();
        });
        mSrlyt.setOnRefreshListener(() -> requestData());
        mAdapter.setOnLoadMoreListener(() -> {
            //            if (hasMore()) {
            //                loadMore();
            //            }
        }, mContentRecyclerView);
    }

    private void initData() {
        mSrlyt.setRefreshing(true);
        requestData();

    }

    private void requestData() {
        int uid = 0;
        if (HaiUtils.isLogin()) {
            uid = UserRepository.getInstance().getUserInfo().getId();
        }

        RetrofitFactory.createService(HomeService.class)
                .retrieveTabDataWithName(mTabName, uid, "2.10")
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new TestSubscriber<>(new IReponse<JSONArray>() {
                    @Override
                    public void onSuccess(JSONArray s) {       //处理首页数据

                        Subscriber<ArrayList<FirstPageBodyBean>> observer = new Subscriber<ArrayList<FirstPageBodyBean>>() {
                            @Override
                            public void onCompleted() {
                                mSrlyt.setRefreshing(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mSrlyt.setRefreshing(false);
                            }

                            @Override
                            public void onNext(ArrayList<FirstPageBodyBean> firstPageBodyBeen) {
                                mSview.showContent();
                                mAdapter.setNewData(firstPageBodyBeen);
                                mAdapter.notifyDataSetChanged();
                                mAdapter.loadMoreEnd();
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
                        mSrlyt.setRefreshing(false);
                    }
                }));

    }

    private ArrayList<FirstPageBodyBean> initData(ArrayList<FirstPageBodyBean> originDatas) {
        ArrayList<FirstPageBodyBean> arrayList = new ArrayList();   //修饰后的数据

        for (FirstPageBodyBean bean : originDatas) {
            switch (bean.getViewType()) {
                case FirstPageViewType.Banner:                   // banner
                case FirstPageViewType.Collect_Specials:         // 合集推荐
                    arrayList.add(bean);
                    break;
                case FirstPageViewType.Recycler_Brand:           // 人气品牌
                    arrayList.add(creatTextBean("人 气 品 牌"));
                    arrayList.add(bean);
                    break;
                case FirstPageViewType.Image_Flavor:
                    arrayList.add(creatTextBean("官 网 特 卖"));

                    HotFavorableBean favorableTemp = (HotFavorableBean) bean.getData();
                    for (Object object : favorableTemp.favorables) {
                        if (((HotFavorableBean.SpecialsBean) object).type == 0) {
                            FirstPageBodyBean bodyBean = new FirstPageBodyBean();
                            bodyBean.setViewType(FirstPageViewType.Image_Flavor);
                            bodyBean.setmData(((HotFavorableBean.SpecialsBean) object).special_items.get(0));
                            arrayList.add(bodyBean);
                        }
//                        else {
//                            FirstPageBodyBean bodyBean = new FirstPageBodyBean();
//                            bodyBean.setViewType(FirstPageViewType.Image_Flavor_Twice);
//                            bodyBean.setmData(((HotFavorableBean.SpecialsBean) object).special_items);
//                            arrayList.add(bodyBean);
//                        }
                    }
                    break;
                case FirstPageViewType.Image_Entry:
                    arrayList.add(creatTextBean("精 选 合 集"));

                    TabEntryBean entryTemp = (TabEntryBean) bean.getData();
                    for (TabEntryBean.EntriesBean entriesBean : entryTemp.entries) {
                        // 大图
                        FirstPageBodyBean bodyBean = new FirstPageBodyBean();
                        bodyBean.setViewType(FirstPageViewType.Image_Entry);
                        bodyBean.setmData(entriesBean);
                        arrayList.add(bodyBean);

                        // 商品横向列表
                        if (HaiUtils.getSize(entriesBean.items) > 0) {
                            FirstPageBodyBean bodyHorBean = new FirstPageBodyBean();
                            bodyHorBean.setViewType(FirstPageViewType.Recycler_Product_200);
                            bodyHorBean.setmData(entriesBean.items);
                            arrayList.add(bodyHorBean);
                        }

                    }
                    break;
                case FirstPageViewType.Product_Twice:           // 单品推荐
                    arrayList.add(creatTextBean("单 品 推 荐"));
                    arrayList.add(bean);
                    break;

            }
        }

        return arrayList;
    }

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

    @Subscribe
    public void changeLikeState(ProductLikeEvent event) {
        mAdapter.changeLikeState(event.spuid, event.liklikeState);
    }

}
