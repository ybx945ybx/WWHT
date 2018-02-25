package com.a55haitao.wwht.adapter.firstpage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.TabFavorableBean;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by a55 on 2017/4/19.
 */

public class RelateFavorableAdapter extends BaseQuickAdapter<TabFavorableBean.SpecialsBean, BaseViewHolder> {

    private Context mContext;

    public RelateFavorableAdapter(Context mContext, List<TabFavorableBean.SpecialsBean> data) {
        super(R.layout.first_page_favorable_img_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, TabFavorableBean.SpecialsBean item) {
        Glide.with(mContext)
                .load(UPaiYunLoadManager.formatImageUrl(item.image, UpaiPictureLevel.SINGLE))
                .transform(new GlideRoundTransform(mContext, 8))
                .placeholder(R.mipmap.ic_default_400_240)
                .dontAnimate()
                .diskCacheStrategy(UPaiYunLoadManager.sDiskCacheStrategy)
                .into((ImageView) helper.getView(R.id.coverImg));
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HaiUriMatchUtils.matchUri((BaseActivity)mContext, item.uri);
            }
        });
    }
}
