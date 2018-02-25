package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.product.ProductInfoChangeAdapter;
import com.a55haitao.wwht.data.model.entity.OrderCommitBean;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by a55 on 2017/11/16.
 */

public class ProductInfoChangePopWindow extends PopupWindow {

    @BindView(R.id.centerView)   LinearLayout centerView;
    @BindView(R.id.iv_close)     ImageView    ivClose;
    @BindView(R.id.rycv_product) RecyclerView rycvProduct;
    @BindView(R.id.tv_title)     HaiTextView  tvTitle;
    @BindView(R.id.tv_back)      HaiTextView  tvBack;
    @BindView(R.id.tv_go_pay)    HaiTextView  tvGoPay;

    private ProductInfoChangeAdapter mAdapter;
    private Activity                 mActivity;
    private LayoutInflater           mInflater;
    private View                     mContentView;
    private View                     mParentView;
    private GoBuyInterface           mInterface;
    private String                   title;
    private ArrayList<String>        allCarts;         //  正常的商品

    private int from;          //  0是商详直接购买过来，1是其他


    public ProductInfoChangePopWindow(Activity activity, OrderCommitBean data, String title, ArrayList<String> allCarts, int from) {

        mActivity = activity;
        this.title = title;
        this.allCarts = allCarts;
        this.from = from;

        // 初始化UI
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.product_info_change_popu_window, null);

        ButterKnife.bind(this, mContentView);
        this.setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();

        // 设置动画
        // this.setAnimationStyle(R.style.AnimationSpecPopuWindow);

        // 渲染UI
        renderUI(data);
    }

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rycvProduct.getLayoutParams();
                    lp.width = msg.arg1;
                    lp.height = msg.arg2;

                    Logger.d("lp Height == " + lp.height + "lp Width == " + lp.width);

                    rycvProduct.setLayoutParams(lp);
                    break;
            }

        }

    };

    public void setGoBuyInterface(GoBuyInterface mInterface) {
        this.mInterface = mInterface;
    }

    private void renderUI(OrderCommitBean data) {
        tvTitle.setText(title);

        int     width   = DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 60);
        int     height  = data.ext.size() > 3 ? DisplayUtils.dp2px(mActivity, 75) * 3 : DisplayUtils.dp2px(mActivity, 75) * data.ext.size();
        Message message = new Message();
        message.arg1 = width;
        message.arg2 = height;
        message.what = 0;
        mHander.sendMessage(message);   //   在主线程中更新ui操作

        // 活动图
        rycvProduct.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new ProductInfoChangeAdapter(data.ext, mActivity);
        rycvProduct.setAdapter(mAdapter);

        // 关闭按钮
        ivClose.setOnClickListener(v -> showOrDismiss(mParentView));

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.getTopActivity().onBackPressed();

            }
        });

        tvGoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelectStatus(data.ext);
                //                Toast.makeText(mActivity, "商品无效或没有选择商品，无法继续支付", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 检查商品现在的状态
    private void checkSelectStatus(ArrayList<OrderCommitBean.FailedExtData> ext) {
        ArrayList<OrderCommitBean.FailedExtData> selectList   = new ArrayList<>();
        int                                      invalidcount = 0;
        for (OrderCommitBean.FailedExtData data : ext) {

            if ("invalid".equals(data.price_status)) {
                invalidcount++;
            } else {
                if (data.is_select) {
                    selectList.add(data);
                }
            }
        }

        if (HaiUtils.getSize(allCarts) > 0) { // 有正常商品
            int[] a = new int[selectList.size() + HaiUtils.getSize(allCarts)];
            for (int i = 0; i < selectList.size(); i++) {
                a[i] = Integer.valueOf(selectList.get(i).cart_id);
            }
            if (HaiUtils.getSize(allCarts) > 0) {
                for (int i = selectList.size(); i < selectList.size() + HaiUtils.getSize(allCarts); i++) {
                    a[i] = Integer.valueOf(allCarts.get(i - selectList.size()));
                }
            }
            if (mInterface != null) {
                mInterface.goBuy(a);
                showOrDismiss(mParentView);
            }
        } else {
            if (invalidcount == ext.size()) {
                Toast.makeText(mActivity, "商品无效，无法继续支付", Toast.LENGTH_SHORT).show();

            } else if (selectList.size() == 0) {
                Toast.makeText(mActivity, "没有选择商品，无法继续支付", Toast.LENGTH_SHORT).show();

            } else {

                int[] a = new int[selectList.size()];
                if (from == 1) {
                    for (int i = 0; i < selectList.size(); i++) {
                        a[i] = Integer.valueOf(selectList.get(i).cart_id);
                    }
                }
                if (mInterface != null) {
                    mInterface.goBuy(a);
                    showOrDismiss(mParentView);
                }
            }
        }
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;

        if (mActivity == null || mActivity.isDestroyed() || mActivity.isFinishing()) {
            Logger.d("已经被销毁了");
            return;
        }

        mParentView = parent;

        long duration = 300;

        if (!this.isShowing()) {

            // Appear
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            centerView.startAnimation(scaleAnimation);

        } else {

            // Animation
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setRepeatCount(0);
            alphaAnimation.setDuration(duration);
            mContentView.startAnimation(alphaAnimation);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.1f, 1.0f, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setRepeatCount(0);
            scaleAnimation.setDuration(duration);
            centerView.startAnimation(scaleAnimation);

            // Dismiss
            new Handler().postDelayed(() -> dismiss(), duration);
        }
    }

    public interface GoBuyInterface {

        void goBuy(int[] selectCarts);

    }

}
