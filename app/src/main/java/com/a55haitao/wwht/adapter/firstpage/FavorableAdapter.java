package com.a55haitao.wwht.adapter.firstpage;

import android.content.Context;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.TabFavorableBean;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/4/25.
 */

public class FavorableAdapter extends BaseQuickAdapter<TabFavorableBean.SpecialsBean, BaseViewHolder> {

    private Context mContext;

    public FavorableAdapter(Context mContext, ArrayList<TabFavorableBean.SpecialsBean> data) {
        super(R.layout.favorable_special_img_item, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, TabFavorableBean.SpecialsBean item) {
//        helper.setVisible(R.id.viewline, helper.getLayoutPosition() == 0 ? false : true);
        Glide.with(mContext)
                .load(UPaiYunLoadManager.formatImageUrl(item.image, UpaiPictureLevel.SINGLE))
                .transform(new GlideRoundTransform(mContext, 8))
                .placeholder(R.mipmap.ic_default_400_240)
                .dontAnimate()
                .into((ImageView)helper.getView(R.id.imgView));
    }
}
