package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.data.event.OrderChangeEvent;
import com.a55haitao.wwht.data.model.annotation.RefundStateType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.OrderDelResult;
import com.a55haitao.wwht.data.model.result.RefundDetailResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.OrderRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.AvatarPopupWindow;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.HaiTimeUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

/**
 * 退款申请单详情
 * Created by a55 on 2017/6/7.
 */

public class RefundDetailActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)                  DynamicHeaderView  headerView;
    @BindView(R.id.msView)                 MultipleStatusView msView;
    @BindView(R.id.tv_order_number)        HaiTextView        tvrefundNo;
    @BindView(R.id.tv_order_create_time)   HaiTextView        tvCreatTime;
    @BindView(R.id.tv_order_contact)       HaiTextView        tvContact;
    @BindView(R.id.tv_stat)                HaiTextView        tvStat;
    @BindView(R.id.tv_stat_info)           HaiTextView        tvStatInfo;
    @BindView(R.id.tv_products_num)        HaiTextView        tvProodyctNUm;
    @BindView(R.id.rycv_selected_products) RecyclerView       rycvProducts;
    @BindView(R.id.tv_desc)                HaiTextView        tvDesc;
    @BindView(R.id.rycv_imgs)              RecyclerView       rycvImgs;
    @BindView(R.id.ll_order_action)        LinearLayout       llytAction;

    @BindString(R.string.key_order_id)  String KEY_ORDER_ID;
    @BindString(R.string.key_refund_no) String KEY_REFUND_NO;

    private RVBaseAdapter<String>                       mImgAdapter;
    private RVBaseAdapter<RefundDetailResult.ItemsBean> mProductAdapter;

    private AvatarPopupWindow mAvatarPopupWindow;

    private String order_id;
    private String refund_no;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_detail);
        ButterKnife.bind(this);

        initVars();
        initViews();
        initData();
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            order_id = intent.getStringExtra(KEY_ORDER_ID);
            refund_no = intent.getStringExtra(KEY_REFUND_NO);
        }

    }

    private void initViews() {
        headerView.setHeaderRightHidden();

        msView.setOnRetryClickListener(v -> initData());

    }

    private void initData() {
        msView.showLoading();
        getRefundDetail();
    }

    /**
     * 获取退款单详情
     */
    private void getRefundDetail() {
        OrderRepository.getInstance()
                .getRefundDetail(order_id, refund_no)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<RefundDetailResult>() {
                    @Override
                    public void onSuccess(RefundDetailResult refundDetailResult) {
                        msView.showContent();
                        setUI(refundDetailResult);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return super.onFailed(e);
                    }
                });
    }

    /**
     * 渲染界面
     *
     * @param refundDetailResult
     */
    private void setUI(RefundDetailResult refundDetailResult) {
        // 申请单号
        tvrefundNo.setText(refundDetailResult.refund_no + "");
        // 提交时间
        tvCreatTime.setText(HaiTimeUtils.showDetailWSecTime(refundDetailResult.cTime));
        // 联系方式
        tvContact.setText(refundDetailResult.linkman_contacts + " , " + refundDetailResult.phone);
        // 受理状态
        setRefundState(refundDetailResult);
        // 受理状态详细描述
        tvStatInfo.setText(refundDetailResult.refund_status_note);
        // 受理的商品数量
        tvProodyctNUm.setText("共" + refundDetailResult.goods_size + "件商品");
        // 受理的商品select_refund_product_item
        rycvProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mProductAdapter = new RVBaseAdapter<RefundDetailResult.ItemsBean>(this, refundDetailResult.items, R.layout.select_refund_product_item) {
            @Override
            public void bindView(RVBaseHolder holder, RefundDetailResult.ItemsBean item) {
                UPaiYunLoadManager.loadImage(mActivity, item.cover_img_url, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.getView(R.id.iv_product));
            }
        };
        rycvProducts.setAdapter(mProductAdapter);

        // 问题描述
        tvDesc.setText(refundDetailResult.note);
        // 上传的图片
        String            voucher_url = refundDetailResult.voucher_url;
        ArrayList<String> vouchers    = new ArrayList<>();
        if (!TextUtils.isEmpty(voucher_url)) {
            if (voucher_url.contains(",")) {
                String[] vouvherSplits = voucher_url.split(",");
                for (int i = 0; i < vouvherSplits.length; i++) {
                    vouchers.add(vouvherSplits[i]);
                }
            } else {
                vouchers.add(voucher_url);
            }
        } else {
            rycvImgs.setVisibility(GONE);
        }
        rycvImgs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImgAdapter = new RVBaseAdapter<String>(this, vouchers, R.layout.select_img_item) {
            @Override
            public void bindView(RVBaseHolder holder, String path) {
                Glide.with(mContext)
                        .load(path)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into((ImageView) holder.getView(R.id.iv_img));
                holder.setVisibility(GONE, R.id.iv_delete);
                // 点击可查看大图
                holder.itemView.setOnClickListener(v -> {
                    mAvatarPopupWindow = new AvatarPopupWindow(mActivity, path, false, 1);
                    rycvImgs.postDelayed(() -> mAvatarPopupWindow.showOrDismiss(rycvImgs), 200);

                });
            }
        };
        rycvImgs.setAdapter(mImgAdapter);
        // 底部操作按钮
        if (refundDetailResult.refund_status == RefundStateType.ORDER_REFUND_REQUEST) {
            llytAction.setVisibility(View.VISIBLE);
        } else {
            llytAction.setVisibility(View.GONE);
        }

    }

    private void setRefundState(RefundDetailResult refundDetailResult) {
        tvStat.setText(refundDetailResult.refund_status_name);
        switch (refundDetailResult.refund_status) {
            case RefundStateType.ORDER_REFUND_REQUEST:
            case RefundStateType.ORDER_REFUND_ACCEPTED:
                tvStat.setTextColor(ContextCompat.getColor(mActivity, R.color.product_status_red));
                break;
            case RefundStateType.ORDER_REFUND_SUCCESS:
                tvStat.setTextColor(ContextCompat.getColor(mActivity, R.color.product_status_orange));
                break;
            case RefundStateType.ORDER_REFUND_CANCEL:
            case RefundStateType.ORDER_REFUND_USER_CANCEL:
                tvStat.setTextColor(ContextCompat.getColor(mActivity, R.color.colorGray999999));
                break;
        }
    }

    /**
     * 复制单号
     */
    @OnClick(R.id.tv_copy)
    public void clickCopyOrderId() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData         clip      = ClipData.newPlainText("Copied Text", refund_no);
        clipboard.setPrimaryClip(clip);
        ToastUtils.showToast(mActivity, "复制成功");
    }

    /**
     * 取消退款
     */
    @OnClick(R.id.tv_refund_cancle)
    public void refundCancle() {
        new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                .setMessage("是否确认取消退款")
                .setPositiveButton("确定", (dialog, which) -> {
                    requestRefundCancle();
                }).setNegativeButton("取消", (dialog, which) -> {
        }).show();

    }

    private void requestRefundCancle() {
        OrderRepository.getInstance()
                .refundCancle(order_id, refund_no)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<OrderDelResult>() {
                    @Override
                    public void onSuccess(OrderDelResult orderDelResult) {
                        getRefundDetail();
                        EventBus.getDefault().post(new OrderChangeEvent());
                        Toast.makeText(mActivity, "已取消", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

}
