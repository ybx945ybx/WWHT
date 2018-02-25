package com.a55haitao.wwht.ui.activity.firstpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.RelateFavorableAdapter;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.event.ProductLikeEvent;
import com.a55haitao.wwht.data.interfaces.ILoad;
import com.a55haitao.wwht.data.model.entity.FavorableSpecialBean;
import com.a55haitao.wwht.data.model.entity.PaginationBean;
import com.a55haitao.wwht.data.model.entity.ShareModel;
import com.a55haitao.wwht.data.model.entity.TabFavorableBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.HomeRepository;
import com.a55haitao.wwht.data.repository.SpecialRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.HaiViewUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 商城专题——双列 （官网特卖）
 */
@MLinkRouter(keys = {"preferentialSpecialKey"})
public class FavorableSpecialActivity extends BaseNoFragmentActivity implements ILoad {

    @BindView(R.id.headView)     DynamicHeaderView  mHeadView;
    @BindView(R.id.content_view) RecyclerView       mGridView;
    @BindView(R.id.msView)       MultipleStatusView mMsView;

    private LinearLayout    mReommendFooterView;         // 推荐
    private ProductAdapter  mAdapter;
    private ShareModel      mShareModel; //分享内容
    private PaginationBean  mPagination;
    private String          mSpecialId;  //专题id
    private Tracker         mTracker;    // GA Tracker
    private ToastPopuWindow mPwToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorable_special);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initVariables();
        initView();
        requestData();
    }

    private void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            // 获取魔窗字段
            String mwSpecialId = intent.getStringExtra("specialid");
            Logger.d("mSpecialId-----" + mwSpecialId);
            if (TextUtils.isEmpty(mwSpecialId)) {
                mSpecialId = intent.getStringExtra("specialId");
            } else {
                mSpecialId = mwSpecialId;
            }
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("商城专题_双列");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        // GA 事件
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("电商运营")
                .setAction("官网特卖 Click")
                .setLabel("55haitao://FavorableSpecial/" + mSpecialId)
                .build());

    }

    private void initView() {
        mMsView.setOnRetryClickListener(v -> requestData());

        mHeadView.setHeadClickListener(() -> {
            if (mShareModel != null) {
                ShareUtils.showShareBoard(FavorableSpecialActivity.this, mShareModel.title, mShareModel.desc, mShareModel.url, mShareModel.icon, false);
            }
        });
        mHeadView.setHeaderRightImg(R.mipmap.ic_title_share);

        mAdapter = new ProductAdapter(null, mActivity, mPwToast, mGridView);
        mAdapter.setOnLoadMoreListener(() -> loadMOreData());
        mAdapter.setActivityAnalysListener(new ProductAdapter.ActivityAnalysListener() {
            @Override
            public void OnActivityAnalys(String spuid) {
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_ProductDetail, spuid, mActivity, TraceUtils.PageCode_OfficialSaleProduct, TraceUtils.PositionCode_OfficialSaleProduct + "", "");
                HashMap<String, String> kv = new HashMap<String, String>();
                kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PageCode_OfficialSaleProduct, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

            }
        });
        mGridView.setLayoutManager(new GridLayoutManager(FavorableSpecialActivity.this, 2));
        mGridView.setAdapter(mAdapter);

    }

    // 请求数据
    private void requestData() {
        mMsView.showLoading();
        SpecialRepository.getInstance()
                .getFavorableSpecialInfo(mSpecialId, 1)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<FavorableSpecialBean>() {
                    @Override
                    public void onSuccess(FavorableSpecialBean result) {
                        mPagination = result.pagination;
                        mHeadView.setHeadTitle(result.name);
                        mShareModel = result.share;
                        addHeadView(result.desc, result.img_cover);
                        if (HaiUtils.getSize(result.goods) > 0) {
                            mAdapter.setNewData(result.goods);
                            if (hasMore()) {
                                mAdapter.loadMoreComplete();
                            } else {
                                mAdapter.loadMoreEnd();
                                getRelateFavorable(Integer.parseInt(mSpecialId));
                            }
                        } else {
                            mAdapter.loadMoreEnd();
                            getRelateFavorable(Integer.parseInt(mSpecialId));
                        }
                        mMsView.showContent();
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mMsView, e, mHasLoad);
                        return mHasLoad;
                    }
                });
    }

    //添加HeaderView
    private void addHeadView(String desc, String imgCover) {
        LinearLayout headerLayout = (LinearLayout) LayoutInflater.from(mActivity)
                .inflate(R.layout.favorable_sp_header_view_layout, null);
        HaiViewUtils.setText(headerLayout, R.id.descTxt, desc);
        HaiViewUtils.loadImg(headerLayout, R.id.bigCoverImg, imgCover);
        mAdapter.addHeaderView(headerLayout);
    }

    //  加载更多
    private void loadMOreData() {
        SpecialRepository.getInstance()
                .getFavorableSpecialInfo(mSpecialId, mPagination.page + 1)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<FavorableSpecialBean>() {
                    @Override
                    public void onSuccess(FavorableSpecialBean result) {
                        mPagination = result.pagination;
                        if (HaiUtils.getSize(result.goods) > 0) {
                            mAdapter.addData(result.goods);
                            if (hasMore()) {
                                mAdapter.loadMoreComplete();
                            } else {
                                getRelateFavorable(Integer.parseInt(mSpecialId));
                                mAdapter.loadMoreEnd();
                            }
                        } else {
                            getRelateFavorable(Integer.parseInt(mSpecialId));
                            mAdapter.loadMoreEnd();
                        }
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    // 获取底部推荐
    private void getRelateFavorable(int special_id) {
        HomeRepository.getInstance()
                .getRelateFavorable(String.valueOf(special_id), 1)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<TabFavorableBean>() {
                    @Override
                    public void onSuccess(TabFavorableBean result) {
                        if (HaiUtils.getSize(result.favorables) > 0) {
                            mReommendFooterView = (LinearLayout) LayoutInflater.from(FavorableSpecialActivity.this)
                                    .inflate(R.layout.favorable_footer_recommend_layout, null);
                            RecyclerView rycvRecommend = (RecyclerView) mReommendFooterView.findViewById(R.id.rycv_recommend);
                            rycvRecommend.setLayoutManager(new LinearLayoutManager(FavorableSpecialActivity.this));
                            rycvRecommend.setAdapter(new RelateFavorableAdapter(FavorableSpecialActivity.this, result.favorables));
                            mReommendFooterView.setVisibility(View.VISIBLE);
                            mAdapter.addFooterView(mReommendFooterView);
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }

    @Override
    public boolean hasMore() {
        return mPagination == null || mPagination.page < mPagination.allpage;
    }

    @Override
    public void loadMore() {
        loadMOreData();
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

    public static void toThisActivity(Context context, String specialId) {
        toThisActivity(context, specialId, false);
    }

    public static void toThisActivity(Context context, String specialId, boolean newTask) {
        Intent intent = new Intent(context, FavorableSpecialActivity.class);
        intent.putExtra("specialId", specialId);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShareUtils.dismissShareBoard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + mSpecialId;
    }

    @Subscribe
    public void changeLikeState(ProductLikeEvent event) {
        if (HaiUtils.getSize(mAdapter.getData()) > 0) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (mAdapter.getData().get(i).spuid.equals(event.spuid)) {
                    if (mAdapter.getData().get(i).is_star != event.liklikeState) {
                        mAdapter.getData().get(i).is_star = event.liklikeState;
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

}
