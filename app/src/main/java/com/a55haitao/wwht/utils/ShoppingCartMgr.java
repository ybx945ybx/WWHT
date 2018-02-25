package com.a55haitao.wwht.utils;

import com.a55haitao.wwht.data.event.LoginStateChangeEvent;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.data.model.entity.ShoppingCartCntBean;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.data.event.ShoppingCartCntChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Haotao_Fujie on 2016/11/1.
 */

public class ShoppingCartMgr {
//    // 购物车处理单例
//    private ShoppingCartMgr() {
//        EventBus.getDefault().register(this);
//        shoppingCartBadge = 0;
//    }
//
//    private static ShoppingCartMgr singleton = null;
//
//    public static ShoppingCartMgr sharedInstance() {
//        if (singleton == null) {
//            singleton = new ShoppingCartMgr();
//        }
//        return singleton;
//    }
//
//    public ShoppingCartBean mShoppingCartData;
//
//    public ShoppingCartChangeState openShoppingState = ShoppingCartChangeState.DO_NOTHING;
//    public ShoppingCartChangeState tabShoppingState  = ShoppingCartChangeState.DO_NOTHING;
//
//    private boolean hasLoadedShoppingCartBadgeFromServer = false;
//    private int                        shoppingCartBadge;
//    private ShoppingCartBadgeInterface mShoppingCartBadgeInterface;
//
//    public void getShoppingCartBadge(ShoppingCartBadgeInterface shoppingCartBadgeInterface) {
//        if (shoppingCartBadgeInterface == null) {
//            // 必须传入接收ShoppingCart Count的回调
//            return;
//        }
//
//        mShoppingCartBadgeInterface = shoppingCartBadgeInterface;
//
//        if (!HaiUtils.isLogin()) {
//            // 未登录
//            return;
//        }
//
//        if (hasLoadedShoppingCartBadgeFromServer == false) {
//
//            // 从网络获取购物车数量
//            ProductRepository.getInstance()
//                    .getShoppingCartListCnt()
//                    .subscribe(new DefaultSubscriber<ShoppingCartCntBean>() {
//                        @Override
//                        public void onSuccess(ShoppingCartCntBean shoppingCartCntBean) {
//
//                            shoppingCartBadge = shoppingCartCntBean.count;
//
//                            hasLoadedShoppingCartBadgeFromServer = true;
//
//                            if (mShoppingCartBadgeInterface != null) {
//                                mShoppingCartBadgeInterface.shoppingCartBadgeUpdated(shoppingCartCntBean.count);
//                            }
//                        }
//
//                        @Override
//                        public void onFinish() {
//
//                        }
//
//                    });
//
//        } else {
//            if (mShoppingCartBadgeInterface != null) {
//                mShoppingCartBadgeInterface.shoppingCartBadgeUpdated(this.shoppingCartBadge);
//            }
//        }
//    }
//
//    public void updateShoppingCartBadge() {
//
//        if (!HaiUtils.isLogin()) {
//            // 未登录
//            return;
//        }
//
//        // 从网络获取购物车数量
//        ProductRepository.getInstance()
//                .getShoppingCartListCnt()
//                .subscribe(new DefaultSubscriber<ShoppingCartCntBean>() {
//                    @Override
//                    public void onSuccess(ShoppingCartCntBean shoppingCartCntBean) {
//                        shoppingCartBadge = shoppingCartCntBean.count;
//                        hasLoadedShoppingCartBadgeFromServer = true;
//                        EventBus.getDefault().post(new ShoppingCartCntChangeEvent(shoppingCartCntBean.count));
//                    }
//
//                    @Override
//                    public void onFinish() {
//
//                    }
//
//                });
//    }
//
//    public void setShoppingCartData(ShoppingCartBean shoppingCartData) {
//        if (shoppingCartData == null || shoppingCartData.data == null) return;
//        this.mShoppingCartData = shoppingCartData;
//
//        // 默认设置为全部勾选
//        for (ShoppingCartBean.CartListStoreData cartListStoreData : this.mShoppingCartData.data) {
//            cartListStoreData.is_select = true;
//
//            for (ShoppingCartBean.Cartdata cartdata : cartListStoreData.cart_list) {
//                cartdata.is_select = true;
//            }
//        }
//    }
//
//    public void setShoppingCartData(ShoppingCartBean shoppingCartData, boolean isDelete) {
//        if (shoppingCartData == null || shoppingCartData.data == null) return;
//        this.mShoppingCartData = shoppingCartData;
//
//        if (isDelete)
//            return;
//        // 默认设置为全部勾选
//        for (ShoppingCartBean.CartListStoreData cartListStoreData : this.mShoppingCartData.data) {
//            cartListStoreData.is_select = true;
//
//            for (ShoppingCartBean.Cartdata cartdata : cartListStoreData.cart_list) {
//                cartdata.is_select = true;
//            }
//        }
//    }
//
//    public void resetAsReloadFromLocal() {
//        resetOpenShoppingCartStateAsReloadFromLocal();
//        resetTabShoppingCartStateAsReloadFromLocal();
//    }
//
//    public void resetAsReloadFromServer() {
//        resetOpenShoppingCartStateAsReloadFromServer();
//        resetTabShoppingCartStateAsReloadFromServer();
//    }
//
//    public void resetOpenShoppingCartStateAsDoNothing() {
//        openShoppingState = ShoppingCartChangeState.DO_NOTHING;
//    }
//
//    private void resetOpenShoppingCartStateAsReloadFromLocal() {
//        openShoppingState = ShoppingCartChangeState.NEEDRELOADFROM_LOCAL;
//    }
//
//    private void resetOpenShoppingCartStateAsReloadFromServer() {
//        openShoppingState = ShoppingCartChangeState.NEEDRELOADFROM_SERVER;
//
//        // 更新下购物车数量
//        updateShoppingCartBadge();
//    }
//
//    public void resetTabShoppingCartStateAsDoNothing() {
//        tabShoppingState = ShoppingCartChangeState.DO_NOTHING;
//    }
//
//    private void resetTabShoppingCartStateAsReloadFromLocal() {
//        tabShoppingState = ShoppingCartChangeState.NEEDRELOADFROM_LOCAL;
//    }
//
//    private void resetTabShoppingCartStateAsReloadFromServer() {
//        tabShoppingState = ShoppingCartChangeState.NEEDRELOADFROM_SERVER;
//
//        // 更新下购物车数量
//        // updateShoppingCartBadge();
//    }
//
//    public enum ShoppingCartChangeState {
//        DO_NOTHING,
//        NEEDRELOADFROM_SERVER,
//        NEEDRELOADFROM_LOCAL
//    }
//
//    public interface ShoppingCartBadgeInterface {
//        public void shoppingCartBadgeUpdated(int badge);
//    }
//
//    @Subscribe
//    public void onLoginStateChange(LoginStateChangeEvent event) {
//        if (event.isLogin) {
//
//        } else {
//            hasLoadedShoppingCartBadgeFromServer = false;
//            this.shoppingCartBadge = 0;
//            this.mShoppingCartData = null;
//            this.openShoppingState = ShoppingCartChangeState.NEEDRELOADFROM_SERVER;
//            this.tabShoppingState = ShoppingCartChangeState.NEEDRELOADFROM_SERVER;
//        }
//    }

}
