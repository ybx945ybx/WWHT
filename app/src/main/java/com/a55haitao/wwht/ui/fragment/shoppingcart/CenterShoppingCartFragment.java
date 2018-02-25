package com.a55haitao.wwht.ui.fragment.shoppingcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.shoppingcart.ShoppingCartExpandableListAdapter;
import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;
import com.a55haitao.wwht.data.event.SwitchTabEvent;
import com.a55haitao.wwht.data.interfaces.OrderModel;
import com.a55haitao.wwht.data.model.annotation.AlertViewType;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.CartInfoBean;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.entity.ProductInfoChangeBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.CartRepository;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.activity.firstpage.CenterActivity;
import com.a55haitao.wwht.ui.activity.product.ProductMainActivity;
import com.a55haitao.wwht.ui.activity.shoppingcart.OrderCreateActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.HaiSwipeRefreshLayout;
import com.a55haitao.wwht.ui.view.ProductInfoChangePopWindow;
import com.a55haitao.wwht.ui.view.ToastPopuWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.NtalkerUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 购物车Fragment
 */
public class CenterShoppingCartFragment extends BaseFragment {

    @BindView(R.id.ll_back)                LinearLayout          mLlBack;
    @BindView(R.id.tv_edit)                TextView              mTvEdit;
    @BindView(R.id.img_service)            ImageView             mImgService;
    @BindView(R.id.ll_header)              LinearLayout          mLlHeader;
    @BindView(R.id.lv_content)             ExpandableListView    mLvContent;
    @BindView(R.id.rl_bottom_bar)          RelativeLayout        mRlBottomBar;
    @BindView(R.id.btn_go_shopping)        Button                mBtnGoShopping;
    @BindView(R.id.ll_shopping_cart_empty) LinearLayout          mLlShoppingCartEmpty;
    @BindView(R.id.tv_total_price)         TextView              mTvTotalPrice;
    @BindView(R.id.tv_go_cash)             TextView              mTvGoCash;
    @BindView(R.id.chk_select_all)         CheckBox              mChkSelectAll;
    @BindView(R.id.ll_product_info)        LinearLayout          mLlProductInfo;
    @BindView(R.id.tv_delete)              TextView              mTvDelete;
    @BindView(R.id.ll_edit)                LinearLayout          mLlEdit;
    @BindView(R.id.swipe)                  HaiSwipeRefreshLayout mSwipe;

    private boolean mIsTabShoppingCart;             // true ：是Tab进去的购物车  false：非Tab进去的购物车
    private boolean inEditing;

    private ShoppingCartBean                  mShoppingData;
    private ShoppingCartExpandableListAdapter mAdapter;
    private ToastPopuWindow                   mPwToast;
    private View                              mFooterView;

    private Tracker mTracker;                       // GA Tracker

    public CenterShoppingCartFragment() {
        mIsTabShoppingCart = false;
    }

    public void setTabShoppingCart(boolean tabShoppingCart) {
        mIsTabShoppingCart = tabShoppingCart;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initVars();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_center_shopping_cart, container, false);
        ButterKnife.bind(this, view);
        initViews(inflater);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        // GA
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    private void initVars() {
        // GA Tracker
        HaiApplication application = (HaiApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("购物车");
    }

    private void initViews(LayoutInflater inflater) {
        mFooterView = inflater.inflate(R.layout.layout_clear_invalidate_product, null, false);
        mFooterView.findViewById(R.id.tv_clear_invalidate_product).setOnClickListener(v -> {
            String cartIds = mShoppingData.getAllInactiveCartIds();
            deleteCartIds(cartIds, true);
        });
        // 刷新控件
        mSwipe.setOnRefreshListener(() -> getShoppingCartData());
        // 是否显示返回按钮
        mLlBack.setVisibility(mIsTabShoppingCart ? View.INVISIBLE : View.VISIBLE);
        // 导航栏事件
        mLlBack.setOnClickListener(v -> mActivity.finish());
        // 客服
        mImgService.setOnClickListener(v -> {
            // GA 事件
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("联系客服")
                    .setAction("在线客服")
                    .setLabel("购物车")
                    .build());
            NtalkerUtils.contactNtalker(mActivity);
        });
        // 前往购物
        mBtnGoShopping.setOnClickListener(v -> {

            // 埋点
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", "", TraceUtils.Event_Category_Click, "", null, "");

            if (mIsTabShoppingCart) {
                EventBus.getDefault().post(new SwitchTabEvent(0));
            } else {
                Intent intent = new Intent(mActivity, CenterActivity.class);
                mActivity.startActivity(intent);
            }
        });
        // 前往结算
        mTvGoCash.setOnClickListener(v -> {
            // GA 事件
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("交易相关")
                    .setAction("前往结算")
                    .build());

            List selectedCartList = mShoppingData.getSelectedCartList();
            if (selectedCartList == null || selectedCartList.size() == 0) {
                mPwToast = ToastPopuWindow.makeText(mActivity, "您还未选择商品", AlertViewType.AlertViewType_Warning);
                mPwToast.show();
                return;
            }

            // 埋点
            HashMap<String, String> kv            = new HashMap<String, String>();
            ArrayList<CartInfoBean> cartInfoBeans = mShoppingData.getSelectedCartInfo();
            String                  cartInfojson  = new Gson().toJson(cartInfoBeans);
            kv.put(TraceUtils.Event_Kv_CartInfo, cartInfojson);
            YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Purchase, TraceUtils.Event_Category_Order, TraceUtils.Event_Action_Order_Cart_Buy, kv, "");

            int[] a = new int[selectedCartList.size()];
            for (int i = 0; i < selectedCartList.size(); i++) {
                a[i] = (int) selectedCartList.get(i);
            }
            OrderModel.CartType cartType = new OrderModel.CartType(a);
            OrderCreateActivity.toThisActivity(mActivity, cartType, 1);
        });
        // 勾选/取消勾选全部
        mChkSelectAll.setOnClickListener(v -> {
            if (v instanceof CompoundButton) {
                CompoundButton buttonView = (CompoundButton) v;
                boolean        isChecked  = buttonView.isChecked();
                if (mShoppingData == null) return;
                mShoppingData.selectAll(isChecked);
                updateShoppingCartContent();
            }
        });
        // 删除商品
        mTvDelete.setOnClickListener(v -> {
            String cartIds = mShoppingData.getAllSelectedCartIds();
            deleteCartIds(cartIds, false);
        });
        // 编辑（删除全部商品、删除失效商品）
        mTvEdit.setOnClickListener(v -> {
            switchEditingView(inEditing);
            inEditing = !inEditing;
        });

        mSwipe.setRefreshing(true);

    }

    private void loadData() {
        getShoppingCartData();
    }

    private void getShoppingCartData() {
        if (HaiUtils.isLogin())
            CartRepository.getInstance()
                    .cartList()
                    .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                    .subscribe(new DefaultSubscriber<ShoppingCartBean>() {
                        @Override
                        public void onSuccess(ShoppingCartBean shoppingCartBean) {
                            // 默认设置为全部勾选  如果是相同活动进行分类
                            for (ShoppingCartBean.CartListStoreData cartListStoreData : shoppingCartBean.data) {
                                cartListStoreData.is_select = true;

                                ArrayList<String> fids        = new ArrayList<>();  // 活动编号集合,无活动的设为-1
                                boolean           hasActivity = false;

                                for (ShoppingCartBean.Cartdata cartdata : cartListStoreData.cart_list) {
                                    cartdata.is_select = true;

                                    // 获取所有活动编号集合
                                    if (cartdata.fullMinus != null) {
                                        hasActivity = true;
                                        if (!fids.contains(cartdata.fullMinus.fid)) {
                                            fids.add(cartdata.fullMinus.fid);
                                        }
                                    } else {
                                        if (!fids.contains("-1")) {
                                            fids.add(0, "-1");
                                        }
                                    }

                                }

                                // 对相同活动的商品进行分类
                                if (hasActivity) {
                                    ArrayList<ShoppingCartBean.Cartdata> cart_list = new ArrayList<>();

                                    for (String fid : fids) {
                                        boolean added = false;
                                        for (ShoppingCartBean.Cartdata cartdata : cartListStoreData.cart_list) {

                                            // 获取所有活动编号集合
                                            if (fid.equals("-1")) {
                                                if (cartdata.fullMinus == null) {
                                                    if (!added) {
                                                        added = true;
                                                        cartdata.showfullMinusTitle = true;
                                                    }
                                                    cart_list.add(cartdata);
                                                }
                                            } else {
                                                if (cartdata.fullMinus != null) {
                                                    if (fid.equals(cartdata.fullMinus.fid)) {
                                                        if (!added) {
                                                            added = true;
                                                            cartdata.showfullMinusTitle = true;
                                                        }
                                                        cart_list.add(cartdata);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    cartListStoreData.cart_list = cart_list;
                                }
                            }

                            mShoppingData = shoppingCartBean;
                            renderUI(false);
                        }

                        @Override
                        public void onFinish() {
                            mSwipe.setRefreshing(false);
                        }
                    });
    }


    public void switchEditingView(boolean isEditing) {
        mTvEdit.setText(isEditing ? "编辑" : "完成");
        mLlProductInfo.setVisibility(isEditing ? View.VISIBLE : View.INVISIBLE);
        mTvGoCash.setVisibility(isEditing ? View.VISIBLE : View.INVISIBLE);
        mLlEdit.setVisibility(isEditing ? View.INVISIBLE : View.VISIBLE);
    }

    private void renderUI(boolean isDelete) {
        // 购物车列表中是否有商品
        if (mShoppingData == null || mShoppingData.data == null || mShoppingData.data.size() == 0) {
            // 购物车空
            mLlShoppingCartEmpty.setVisibility(View.VISIBLE);
            mLvContent.setVisibility(View.INVISIBLE);
            mRlBottomBar.setVisibility(View.INVISIBLE);
            mTvEdit.setVisibility(View.INVISIBLE);
        } else {
            // 购物车非空
            mLlShoppingCartEmpty.setVisibility(View.INVISIBLE);
            mLvContent.setVisibility(View.VISIBLE);
            mRlBottomBar.setVisibility(View.VISIBLE);
            mTvEdit.setVisibility(View.VISIBLE);

            // 设置Adapter
            setAdapter();

            mTvTotalPrice.setText(PriceUtils.toRMBFromYuan(mShoppingData.getTotalActiveAndSelectedRealPrice()));
            mTvGoCash.setText("前往结算(" + mShoppingData.getSelectedProductedCount() + ")");
            mTvDelete.setText("删除(" + mShoppingData.getSelectedProductedCount() + ")");


            if (!mChkSelectAll.isChecked() && !isDelete) {
                mChkSelectAll.performClick();
            }
        }
    }

    private void setAdapter() {
        if (mAdapter == null) {
            // 设置ShoppingCartContent Adapter
            mAdapter = new ShoppingCartExpandableListAdapter(mActivity);

            // 更新购物车数据
            mAdapter.setShoppingData(mShoppingData);

            // 初始化shoppingCartContentView
            mLvContent.setGroupIndicator(null);
            mLvContent.setOnGroupClickListener((parent, v, groupPosition, id) -> true);

            // 设置
            mLvContent.setAdapter(mAdapter);

            // 设置OnShoppingCartChangeListener
            mAdapter.setOnShoppingCartChangeListener(new ShoppingCartExpandableListAdapter.OnShoppingCartChangeListener() {
                @Override
                public void onLongClicked(String cartId) {
                }

                @Override
                public void onClicked(boolean headerView, String store_id, String spuid, String name) {
                    if (headerView) {
                        String title = mShoppingData.getSellerName(store_id);

                        // 埋点
                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Store, TraceUtils.Event_Category_Click, "", null, "");

                        SiteActivity.toThisActivity(mActivity, title, PageType.SELLER);
                    } else {
                        // 埋点
                        HashMap<String, String> kv = new HashMap<String, String>();
                        kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                        YbxTrace.getInstance().event(mActivity, ((BaseActivity) mActivity).pref, ((BaseActivity) mActivity).prefh, ((BaseActivity) mActivity).purl, ((BaseActivity) mActivity).purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");

                        ProductMainActivity.toThisAcivity(mActivity, spuid, name);
                    }
                }

                @Override
                public void onCheckBoxChanged(boolean headerView, String store_id, String cart_id, boolean checked) {
                    if (headerView) {
                        mShoppingData.selectAll(store_id, checked);
                        updateShoppingCartContent();
                    } else {
                        mShoppingData.selectCartId(cart_id, checked);
                        updateShoppingCartContent();
                    }
                }
            });
        } else {
            mAdapter.setShoppingData(mShoppingData);
            mAdapter.notifyDataSetChanged();
        }

        // 展开所有子项
        expandAllGroup();

        // 显示清空失效商品
        switchClearInvalidateProductView();
    }

    /**
     * 显示清空失效商品
     */
    private void switchClearInvalidateProductView() {
        boolean hasInvalidateProduct = !TextUtils.isEmpty(mShoppingData.getAllInactiveCartIds());
        if (hasInvalidateProduct && mLvContent.getFooterViewsCount() == 0) {
            mLvContent.addFooterView(mFooterView);
        } else if (!hasInvalidateProduct && mLvContent.getFooterViewsCount() > 0) {
            mLvContent.removeFooterView(mFooterView);
        }
    }

    /**
     * 购物车勾选状态改变后更新界面
     */
    private void updateShoppingCartContent() {
        // 购物车列表中是否有商品
        if (mShoppingData == null || mShoppingData.data == null || mShoppingData.data.size() == 0) {
            mLlShoppingCartEmpty.setVisibility(View.VISIBLE);
            mLvContent.setVisibility(View.INVISIBLE);
            mRlBottomBar.setVisibility(View.INVISIBLE);
            mTvEdit.setVisibility(View.INVISIBLE);
        } else {
            mLlShoppingCartEmpty.setVisibility(View.INVISIBLE);
            mLvContent.setVisibility(View.VISIBLE);
            mRlBottomBar.setVisibility(View.VISIBLE);
            mTvEdit.setVisibility(View.VISIBLE);

            mAdapter.setShoppingData(mShoppingData);
            mAdapter.notifyDataSetChanged();
            expandAllGroup();

            // 更新价格
            mTvTotalPrice.setText(PriceUtils.toRMBFromYuan(mShoppingData.getTotalActiveAndSelectedRealPrice()));
            mTvGoCash.setText("前往结算(" + mShoppingData.getSelectedProductedCount() + ")");
            mTvDelete.setText("删除(" + mShoppingData.getSelectedProductedCount() + ")");

            // 更新勾选状态
            mChkSelectAll.setChecked(mShoppingData.getAllSelected());
        }
    }

    // 打开所有子项
    private void expandAllGroup() {
        if (mShoppingData != null && mShoppingData.data != null)
            for (int i = 0; i < mShoppingData.data.size(); i++) {
                mLvContent.expandGroup(i);
            }
    }

    /**
     * 删除商品
     *
     * @param cartIds
     * @param clearInactive
     */
    private void deleteCartIds(String cartIds, boolean clearInactive) {
        if (TextUtils.isEmpty(cartIds)) {
            if (clearInactive) {
                mPwToast = ToastPopuWindow.makeText(mActivity, "您的购物车没有失效商品", AlertViewType.AlertViewType_Warning);
                mPwToast.show();
            } else {
                mPwToast = ToastPopuWindow.makeText(mActivity, "您还没有选择商品", AlertViewType.AlertViewType_Warning);
                mPwToast.show();
            }
            return;
        }

        String title  = "很抢手哦，下次不一定能买到，确定删除吗？";
        String result = "删除商品成功";
        if (clearInactive) {
            title = "确定清除失效商品吗？";
            result = "清除失效商品成功";
        }

        AlertDialog.Builder deletionAlertBuilder = new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self);
        deletionAlertBuilder.setMessage(title);
        String finalResult = result;
        deletionAlertBuilder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            CartRepository.getInstance()
                    .delCart(cartIds)
                    .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                    .subscribe(new DefaultSubscriber<CommonDataBean>() {
                        @Override
                        public void onSuccess(CommonDataBean commonDataBean) {
                            mPwToast = ToastPopuWindow.makeText(mActivity, finalResult, AlertViewType.AlertViewType_Warning);
                            mPwToast.show();
                            getShoppingCartData();
                            switchEditingView(true);

                            // 获取购物车数量更新
                            getShoppingCartBadge();
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
        });
        deletionAlertBuilder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = deletionAlertBuilder.create();
        alertDialog.show();
    }

    private void getShoppingCartBadge() {
        if (!HaiUtils.isLogin()) {
            return;
        }
        ProductRepository.getInstance()
                .getShoppingCartListCnt()
                .subscribe(new DefaultSubscriber<ShoppingCartCntBean>() {
                    @Override
                    public void onSuccess(ShoppingCartCntBean shoppingCartCntBean) {
                        EventBus.getDefault().post(new ShoppingCartCntChangeEvent(shoppingCartCntBean.count));
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }

    @Subscribe
    public void onLoginStateChangeEvent(LoginStateChangeEvent event) {
        if (event.isLogin == false) {
            mShoppingData = null;
            renderUI(false);
        }
    }

    @Override
    public void onStop() {
        mSwipe.setRefreshing(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPwToast != null && mPwToast.isShowing()) {
            mPwToast.dismiss();
        }
    }

}
