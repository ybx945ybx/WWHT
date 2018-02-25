package com.a55haitao.wwht.adapter.common;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.result.GetFollowBrandStoreResult;
import com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 关注的品牌/商家列表 Adapter
 *
 * @author 陶声
 * @since 2016-10-20
 */

public class BrandSellerListAdapter extends BaseQuickAdapter<GetFollowBrandStoreResult.DataBean, BaseViewHolder> {
    @BrandSellerFragmentType int mType;
    private final            int mLogoSize;

    public BrandSellerListAdapter(List<GetFollowBrandStoreResult.DataBean> data, @BrandSellerFragmentType int type, int logoSize) {
        super(R.layout.item_brand_store, data);
        mType = type;
        mLogoSize = logoSize;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetFollowBrandStoreResult.DataBean item) {
        String title = item.nameen;
        if (mType == BrandSellerFragmentType.BRAND && !TextUtils.isEmpty(item.namecn) && !TextUtils.equals(item.namecn, item.nameen)) {
            title = String.format("%s/%s", item.nameen, item.namecn);
        }
        helper.setText(R.id.tv_name, title) // 品商家名
                .setText(R.id.tv_desc, item.desc) // 品牌/商家介绍
                .setChecked(R.id.chk_follow_brand_store, item.is_followed) // 关注状态
                .addOnClickListener(R.id.chk_follow_brand_store);
        // 品牌商家Logo
        if (TextUtils.isEmpty(item.logo1)) {
            helper.setVisible(R.id.smallCoverTex, true);
            helper.setVisible(R.id.smallCoverImg, false);

            String logoName = "";
            if (!TextUtils.isEmpty(item.nameen)) {
                logoName = item.nameen;
            } else if (!TextUtils.isEmpty(item.namecn)) {
                logoName = item.namecn;
            } else {
                logoName = item.name;
            }

            HaiUtils.setBrandOrSellerImg(helper.getView(R.id.smallCoverTex), logoName, 2);

        } else {
            helper.setVisible(R.id.smallCoverTex, false);
            helper.setVisible(R.id.smallCoverImg, true);

            UPaiYunLoadManager.loadImage(mContext, item.logo1, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, helper.getView(R.id.smallCoverImg));
        }

        // 最后一条数据隐藏Divider
        helper.setVisible(R.id.view_divider, mData.indexOf(item) != mData.size() - 1);
    }
}
