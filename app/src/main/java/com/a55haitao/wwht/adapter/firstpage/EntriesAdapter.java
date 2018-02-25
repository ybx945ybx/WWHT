package com.a55haitao.wwht.adapter.firstpage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.TabEntryBean;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

import tom.ybxtracelibrary.YbxTrace;

/**
 * Created by a55 on 2017/4/25.
 */

public class EntriesAdapter extends BaseQuickAdapter<TabEntryBean.EntriesBean, BaseViewHolder> {

    private Activity mContext;
    private String   Tag;

    public EntriesAdapter(Activity mContext, ArrayList<TabEntryBean.EntriesBean> data) {
        super(R.layout.entries_list_item, data);
        this.mContext = mContext;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    @Override
    protected void convert(BaseViewHolder helper, TabEntryBean.EntriesBean item) {
        UPaiYunLoadManager.loadImage(mContext, item.image, UpaiPictureLevel.SINGLE, R.mipmap.ic_default_square_large, helper.getView(R.id.mainImg));
        if (!TextUtils.isEmpty(item.small_icon)) {
            UPaiYunLoadManager.loadImage(mContext, item.small_icon, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, helper.getView(R.id.smallCoverImg));
        }
        if (helper.getLayoutPosition() == 0) {
            helper.getView(R.id.viewline).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.viewline).setVisibility(View.VISIBLE);
        }
//        helper.getView(R.id.likeCheckBox).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!HaiUtils.needLogin(mContext)) {
//                    CommonReposity.getInstance()
//                            .likePdSpecial(item.uri.substring(item.uri.lastIndexOf("/") + 1))
//                            .subscribe(new TestSubscriber<>(new IReponse<ProductSpecialLikeBean>() {
//                                @Override
//                                public void onSuccess(ProductSpecialLikeBean likeBean) {
//                                    item.is_liked = likeBean.liked;
//                                    item.like_count = likeBean.like_count;
//                                    notifyItemChanged(helper.getLayoutPosition());
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                }
//
//                                @Override
//                                public void onError() {
//
//                                }
//                            }));
//                }
//            }
//        });
        helper.getView(R.id.mainImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 埋点
//                TraceUtils.addAnalysActMatchUri(mContext, TraceUtils.PageCode_SpecialCollection, TraceUtils.PositionCode_SpecialCollection, item.uri);
                YbxTrace.getInstance().event(mContext, ((BaseActivity)mContext).pref, ((BaseActivity)mContext).prefh, ((BaseActivity)mContext).purl, ((BaseActivity)mContext).purlh, "", TraceUtils.PositionCode_SpecialCollection, TraceUtils.Event_Category_Click, "", null, "");

                HaiUriMatchUtils.matchUri((BaseActivity) mContext, item.uri);
            }
        });


        RecyclerView                pd200RecyclerView = (RecyclerView) helper.getView(R.id.recyclerView);
        HorizontalPdAdapter adapter           = (HorizontalPdAdapter) pd200RecyclerView.getAdapter();
        if (adapter == null) {
            pd200RecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            if (item.items != null && item.items.size() > 0) {
                if (HaiUtils.getSize(item.items) > 12) {
                    item.items = new ArrayList<>(item.items.subList(0, 12));
                    item.items.get(11).showSeeAll = true;
                }
            }
            adapter = new HorizontalPdAdapter(mContext, item.items, R.layout.item_product_brief_200_layout);
            pd200RecyclerView.setAdapter(adapter);
            adapter.mUri = item.uri;
        } else {
            adapter.changeDatas(item.items);
        }

    }
}
