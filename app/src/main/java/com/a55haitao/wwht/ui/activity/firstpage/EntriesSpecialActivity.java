package com.a55haitao.wwht.ui.activity.firstpage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.EntriesSpecialDetailAdapter;
import com.a55haitao.wwht.adapter.product.ProductAdapter;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.EntriesSpecialResult;
import com.a55haitao.wwht.data.model.entity.LikeBean;
import com.a55haitao.wwht.data.model.entity.ProductSpecialLikeBean;
import com.a55haitao.wwht.data.model.entity.ShareModel;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SpecialRepository;
import com.a55haitao.wwht.data.repository.UserRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.activity.social.LikeListActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.HaiViewUtils;
import com.a55haitao.wwht.utils.ShareUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import cn.magicwindow.mlink.annotation.MLinkRouter;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 官网特卖
 * 商品专题 - 单列
 */
@MLinkRouter(keys = {"productSpecialKey"})
public class EntriesSpecialActivity extends BaseNoFragmentActivity {

    private ShareModel          mShareModel;
    private DynamicHeaderView   mHeadView;
    private MultipleStatusView  msView;
    private ListView            mContentListView;
    private ImageView           mLikedImgView;
    private RelativeLayout      mLikedParentLayout;
    private TextView            mCountTxtView;
    private LinearLayout        mLikePeopleLayout;
    private String              mSpecialId;
    private ArrayList<LikeBean> mLikes;
    private EntriesSpecialDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entries_special_layout);

        mHeadView = (DynamicHeaderView) findViewById(R.id.headView);
        mHeadView.setHeadClickListener(() -> {
            if (mShareModel != null) ShareUtils.showShareBoard(this, mShareModel.title, mShareModel.desc, mShareModel.url, mShareModel.icon, false);
        });
        mContentListView = (ListView) mActivity.findViewById(R.id.content_view);
        msView = (MultipleStatusView) findViewById(R.id.msv);
        msView.setOnRetryClickListener(v -> requestData());

        Intent intent = getIntent();
        if (intent != null) {
            // 获取魔窗字段
            String mwSpecialId = intent.getStringExtra("specialid");
            if (TextUtils.isEmpty(mwSpecialId)) {
                mSpecialId = intent.getStringExtra("special_id");
            } else {
                mSpecialId = mwSpecialId;
            }
        }

        // GA 事件
        HaiApplication application = (HaiApplication) getApplication();
        Tracker        tracker     = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("电商运营")
                .setAction("精选合集 Click")
                .setLabel("55haitao://ProductSpecial/" + mSpecialId)
                .build());

        requestData();
    }

    private void requestData() {
        msView.showLoading();

        SpecialRepository.getInstance()
                .getProductSpecialInfo(mSpecialId)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<EntriesSpecialResult>() {
                    @Override
                    public void onSuccess(EntriesSpecialResult bean) {
                        msView.showContent();
                        mShareModel = bean.share;
                        mLikes = bean.likes;
                        mHeadView.setHeadTitle(bean.name);

                        //初始化HeaderView
                        LinearLayout headerLayout =
                                (LinearLayout) LayoutInflater.from(mActivity)
                                        .inflate(R.layout.favorable_sp_header_view_layout, mContentListView, false);
                        HaiViewUtils.setText(headerLayout, R.id.descTxt, bean.desc);
                        HaiViewUtils.loadImg(headerLayout, R.id.bigCoverImg, bean.img_cover);
                        ButterKnife.findById(headerLayout, R.id.textView).setVisibility(View.GONE);
                        mContentListView.addHeaderView(headerLayout);

                        //初始化FooterView
                        LinearLayout footerLayout =
                                (LinearLayout) LayoutInflater.from(mActivity)
                                        .inflate(R.layout.entries_sp_footer_view_layout, mContentListView, false);
                        mLikePeopleLayout = (LinearLayout) footerLayout.findViewById(R.id.likePeopleLayout);
                        mLikedParentLayout = (RelativeLayout) footerLayout.findViewById(R.id.relLike);
                        mLikedParentLayout.setOnClickListener(v -> {
                            if (!HaiUtils.needLogin(mActivity)) {
                                SpecialRepository.getInstance()
                                        .likeProductSpecial(mSpecialId)
                                        .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                                        .subscribe(new DefaultSubscriber<ProductSpecialLikeBean>() {
                                            @Override
                                            public void onSuccess(ProductSpecialLikeBean result) {
                                                UserBean userInfo = UserRepository.getInstance().getUserInfo();
                                                if (result.liked) {
                                                    ToastUtils.showToast(EntriesSpecialActivity.this, StringConstans.LIKE_SPECIAL_SUCCESSFUL);
                                                    LikeBean bean = new LikeBean();
                                                    bean.id = userInfo.getId();
                                                    bean.head_img = userInfo.getHeadImg();
                                                    mLikes.add(0, bean);
                                                } else {
                                                    for (LikeBean like : mLikes) {
                                                        if (like.id == userInfo.getId()) {
                                                            mLikes.remove(like);
                                                            break;
                                                        }
                                                    }
                                                }
                                                changeLikeStatusAndCount(result.liked, result.like_count);
                                                changeLikeList();
                                            }

                                            @Override
                                            public void onFinish() {

                                            }
                                        });
                            }
                        });
                        mLikePeopleLayout.setOnClickListener(v -> {
                            Intent intent = new Intent(mActivity, LikeListActivity.class);
                            intent.putExtra(getString(R.string.key_product_special_id), Integer.valueOf(mSpecialId));
                            mActivity.startActivity(intent);
                        });
                        mLikedImgView = (ImageView) footerLayout.findViewById(R.id.imgHeart);
                        mCountTxtView = (HaiTextView) footerLayout.findViewById(R.id.countTxt);
                        changeLikeStatusAndCount(bean.is_liked, bean.like_count);
                        changeLikeList();
                        mContentListView.addFooterView(footerLayout);
                        mAdapter = new EntriesSpecialDetailAdapter(mActivity, bean.items);
                        mAdapter.setActivityAnalysListener(spuid -> {
                            // 转换率事件
                            HashMap<String, String> kv = new HashMap<String, String>();
                            kv.put(TraceUtils.Event_Kv_Goods_Id, spuid);
                            YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_Product, TraceUtils.Event_Category_Click, TraceUtils.Event_Action_Click_Goods, kv, "");
                            //                            TraceUtils.addTags(mActivity, TraceUtils.PS_Fashion + mSpecialId, TraceUtils.PS_Fashion + mSpecialId);
                        });
                        mContentListView.setAdapter(mAdapter);
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
//                        mHasLoad = true;
                    }
                });

    }


    public void changeLikeList() {

        mLikePeopleLayout.removeAllViews();
        if (mLikes == null || mLikes.size() == 0) {
            TextView textView = new TextView(this);
            textView.setTextSize(14);
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorGray999999));
            textView.setText("还没有人点赞");
            mLikePeopleLayout.addView(textView);

            return;
        }

        int length            = mLikes.size();
        int screenWidthHeight = DeviceUtils.getScreenWidth();
        int contentWidth      = 0;
        for (int i = 0; i < length; i++) {

            LikeBean likeBean = mLikes.get(i);

            ImageView avator = new ImageView(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(HaiConstants.CompoundSize.PX_56, HaiConstants.CompoundSize.PX_56);
            layoutParams.rightMargin = (int) (HaiConstants.CompoundSize.PX_10 * 0.8);
            avator.setLayoutParams(layoutParams);
            mLikePeopleLayout.addView(avator);

            contentWidth += HaiConstants.CompoundSize.PX_10 * 7.2;
            if (screenWidthHeight - contentWidth - HaiConstants.CompoundSize.PX_10 * 8 < 0) {
                avator.setImageResource(R.mipmap.ic_points);
                break;
            } else {
                UPaiYunLoadManager.loadImage(mActivity,
                        likeBean.head_img,
                        UpaiPictureLevel.FOURTH,
                        R.mipmap.ic_avatar_default_small,
                        avator,
                        true);
            }
        }
    }

    public void changeLikeStatusAndCount(boolean isliked, int count) {
        if (isliked) {
            mLikedParentLayout.setBackgroundResource(R.drawable.shape_banner_info_like_like);
            mLikedImgView.setImageResource(R.mipmap.ic_heart_light_red);
            mCountTxtView.setTextColor(Color.parseColor("#E93569"));
        } else {
            mLikedParentLayout.setBackgroundResource(R.drawable.shape_banner_info_like_not_like);
            mLikedImgView.setImageResource(R.mipmap.ic_heart_light_grey);
            mCountTxtView.setTextColor(Color.parseColor("#BFBFBF"));
        }
        mCountTxtView.setText(String.format("喜欢 %d", count));
    }

    public static void toThisActivity(Context context, String specialId) {
        toThisActivity(context, specialId, false);
    }

    /**
     * 跳转到本页面
     *
     * @param context   context
     * @param specialId 专题Id
     * @param newTask   开启新栈
     */
    public static void toThisActivity(Context context, String specialId, boolean newTask) {
        Intent intent = new Intent(context, EntriesSpecialActivity.class);
        intent.putExtra("special_id", specialId);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + mSpecialId;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShareUtils.dismissShareBoard();
    }
}
