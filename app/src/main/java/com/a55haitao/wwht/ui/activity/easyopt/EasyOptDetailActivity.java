package com.a55haitao.wwht.ui.activity.easyopt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.easyopt.EasyOptDragAdapter;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.event.EasyOptAction;
import com.a55haitao.wwht.data.event.EasyoptCommentCountChangeEvent;
import com.a55haitao.wwht.data.event.EasyoptListChangeEvent;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.PriorityModel;
import com.a55haitao.wwht.data.model.entity.ShareModel;
import com.a55haitao.wwht.data.model.result.EasyOptListResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.layout.EasyOptHeaderLayout;
import com.a55haitao.wwht.ui.view.LoadMoreFooterView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.ui.view.SiteNagivationLayout;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.FastBlurUtil;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 草单详情
 */
@MLinkRouter(keys = {"albumDetailKey"})
public class EasyOptDetailActivity extends BaseNoFragmentActivity {

    @BindView(R.id.gridView)             RecyclerView         mGridView;
    @BindView(R.id.msView)               MultipleStatusView   mStatusView;
    @BindView(R.id.floatingHeaderLayout) SiteNagivationLayout mFloatingHeaderLayout;
    @BindView(R.id.iv_title)             ImageView            ivtitle;
    @BindView(R.id.deleteBottomTxt)      TextView             mDeleteBottomTxt;

    private EasyOptDragAdapter  mDragAdapter;   // 可拖动的adapter
    private GridLayoutManager   mGridLayoutManager;
    private EasyOptHeaderLayout mHeaderLayout;  // 头布局
    private ShareModel          mShareModel;
    private Bitmap              blurBitmap;

    private int     mId;
    private Tracker mTracker;       // GA Tracker

    private OnItemDragListener onItemDragListener;
    private ItemTouchHelper    itemTouchHelper;
    private LoadMoreFooterView loadMoreFooterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy_opt_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initVars();
        initViews();
        initData();

    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            // 获取魔窗字段
            String mwEasyoptId = intent.getStringExtra("albumid");
            if (TextUtils.isEmpty(mwEasyoptId)) {
                mId = getIntent().getIntExtra("data", -1);
            } else {
                mId = Integer.valueOf(mwEasyoptId);
            }
        }

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("草单_详情");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initViews() {
        mHeaderLayout = (EasyOptHeaderLayout) LayoutInflater.
                from(EasyOptDetailActivity.this).inflate(R.layout.easy_opt_detail_header_layout, null);
        mHeaderLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = mHeaderLayout.getPartHeight();

                RelativeLayout.LayoutParams lp1 = (RelativeLayout.LayoutParams) ivtitle.getLayoutParams();
                lp1.height = height;
                lp1.topMargin = mFloatingHeaderLayout.getNagivationHeight() - height;
                ivtitle.setLayoutParams(lp1);
                //                ivtitle.scrollTo(0, mHeaderLayout.getPartHeight() + mFloatingHeaderLayout.getNagivationHeight());
                mHeaderLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mStatusView.setOnRetryClickListener(v -> getEasyOptDetail());

        mDeleteBottomTxt.setOnClickListener(v -> {
            int checkedCount = mDragAdapter.getCheckedCount();
            if (checkedCount == 0) {
                ToastUtils.showToast(this, "请选择要删除的商品");
            } else {
                deleteEasyOpt();
            }
        });

        mFloatingHeaderLayout.setMainHandler(new SiteNagivationLayout.NagivationHandlerIml() {
            @Override
            public void onShareClick() {
                ShareUtils.showShareBoard(EasyOptDetailActivity.this, mShareModel.title, mShareModel.desc, mShareModel.url, mShareModel.icon, false);
            }

            @Override
            public void onStarClick() {
                editActived(true);
                mDeleteBottomTxt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBackClick() {
                onBackPressed();
            }
        });

        mFloatingHeaderLayout.setRightHandler(this::updateEasyOpt);

        onItemDragListener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
            }
        };

    }

    private void initData() {
        getEasyOptDetail();
    }

    private void getEasyOptDetail() {
        mStatusView.showLoading();

        SnsRepository.getInstance()
                .easyoptDetail(mId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<EasyOptListResult>() {
                    @Override
                    public void onSuccess(EasyOptListResult result) {
                        mStatusView.showContent();

                        mDragAdapter = new EasyOptDragAdapter(mActivity, result.easyopt.products);
                        mDragAdapter.setActivityAnalysListener(new ProductAdapter.ActivityAnalysListener() {
                            @Override
                            public void OnActivityAnalys(String spuid) {
                                // 埋点
                                //                                TraceUtils.addClick(TraceUtils.PageCode_ProductDetail, spuid, EasyOptDetailActivity.this, TraceUtils.PageCode_EasyDetail, TraceUtils.PositionCode_Product + "", "");
                                HashMap<String, String> kv = new HashMap<String, String>();
                                kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                                YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");
                            }
                        });
                        mDragAdapter.setOnCheckClickListener(count -> mDeleteBottomTxt.setText(String.format("删除%d个商品", count)));

                        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mDragAdapter);
                        itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
                        itemTouchHelper.attachToRecyclerView(mGridView);

                        //add Header
                        mDragAdapter.addHeaderView(mHeaderLayout);

                        if (TextUtils.isEmpty(result.easyopt.img_cover)) {
                            mHeaderLayout.setData(result, result.easyopt, result.share, null);
                        } else {
                            String url = UPaiYunLoadManager.formatImageUrl(result.easyopt.img_cover, UpaiPictureLevel.FOURTH);
                            Glide.with(mActivity)
                                    .load(url)
                                    .downloadOnly(new SimpleTarget<File>() {
                                        @Override
                                        public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                                            Bitmap bitmap     = BitmapFactory.decodeFile(resource.getAbsolutePath());
                                            int    scaleRatio = 8;
                                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                                                    bitmap.getWidth() / scaleRatio,
                                                    bitmap.getHeight() / scaleRatio,
                                                    false);
                                            blurBitmap = FastBlurUtil.blurBitmap(scaledBitmap, mActivity);
                                            mHeaderLayout.setData(result, result.easyopt, result.share, blurBitmap);
                                            ivtitle.setImageBitmap(blurBitmap);
                                        }

                                    });

                            // 兼容之前版本用户未上传封面图，上面的高斯模糊会失败
                            if (blurBitmap == null){
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_easyopt_logo_default);
                                int    scaleRatio = 8;
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                                        bitmap.getWidth() / scaleRatio,
                                        bitmap.getHeight() / scaleRatio,
                                        false);
                                blurBitmap = FastBlurUtil.blurBitmap(scaledBitmap, mActivity);
                                mHeaderLayout.setData(result, result.easyopt, result.share, blurBitmap);
                                ivtitle.setImageBitmap(blurBitmap);
                            }

                        }
                        mGridLayoutManager = new GridLayoutManager(mActivity, 2);
                        mGridView.setLayoutManager(mGridLayoutManager);
                        mGridView.setAdapter(mDragAdapter);
                        mGridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (mGridLayoutManager.findFirstVisibleItemPosition() > 0) {
                                    ivtitle.setVisibility(View.VISIBLE);
                                } else {
                                    ivtitle.setVisibility(View.INVISIBLE);
                                }

                            }
                        });

                        if (HaiUtils.getSize(result.easyopt.products) > 2) {  // 底部别拉了我是有底线的提示
                            loadMoreFooterView = new LoadMoreFooterView(mActivity);
                            loadMoreFooterView.setLoadStatus(false);
                            mDragAdapter.addFooterView(loadMoreFooterView);
                        }

                        mShareModel = result.share;

                        if (HaiUtils.isLogin() && UserRepository.getInstance().getUserInfo().getId() == result.easyopt.owner.getId()) {
                            mFloatingHeaderLayout.setStarStatus(false, "编辑");
                        }

                        // GA 事件
                        mTracker.send(new HitBuilders.EventBuilder()
                                .setCategory("社区运营")
                                .setAction("草单 Click")
                                .setLabel(String.valueOf(mId) + "," + result.easyopt.name)
                                .build());
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mStatusView, e, mHasLoad);
                        return mHasLoad;
                    }
                });

    }

    private void updateEasyOpt() {
        showProgressDialog(StringConstans.TOAST_SAVE, false);
        ArrayList<PriorityModel> sortedInfo = mDragAdapter.getUpdateInfo();

        SnsRepository.getInstance()
                .easyoptUpdate(mId, sortedInfo)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        ToastUtils.showToast("修改成功");
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                        EventBus.getDefault().post(new EasyoptListChangeEvent());
                        editActived(false);
                    }
                });
    }

    public void deleteEasyOpt() {
        showProgressDialog(StringConstans.TOAST_SAVE, false);
        ArrayList<PriorityModel> sortedInfo = mDragAdapter.getDeleteInfo();

        SnsRepository.getInstance()
                .easyoptUpdate(mId, sortedInfo)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        ToastUtils.showToast("删除成功");
                        mDragAdapter.reloadAfterDelete();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                        EventBus.getDefault().post(new EasyoptListChangeEvent());
                        editActived(false);
                    }
                });
    }

    public void editActived(boolean b) {
        mFloatingHeaderLayout.statusSwitchForEO(b);
        mHeaderLayout.switchEditStatus(b, mDragAdapter.getcount());
        mDragAdapter.setEditable(b);
        if (b) {
            // 开启拖拽
            mDragAdapter.enableDragItem(itemTouchHelper, R.id.rlyt_product, true);
            mDragAdapter.setOnItemDragListener(onItemDragListener);
        } else {
            mDragAdapter.disableDragItem();
        }

        mDragAdapter.notifyDataSetChanged();

        if (!b) {
            mDeleteBottomTxt.setVisibility(View.GONE);
        } else {
            mDeleteBottomTxt.setText(String.format("删除%d个商品", mDragAdapter.getCheckedCount()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHeaderLayout.canclePopuWindow();
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + mId;
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

    @Subscribe
    public void onEasyOtAction(EasyOptAction action) {
        finish();
    }


    @Subscribe
    public void onCommentCountChangeEvent(EasyoptCommentCountChangeEvent event) {
        mHeaderLayout.changeCommentCount(event.count);
    }

    public static void toThisActivity(Context c, int easyOptId) {
        Intent intent = new Intent(c, EasyOptDetailActivity.class);
        intent.putExtra("data", easyOptId);
        c.startActivity(intent);
    }

}
