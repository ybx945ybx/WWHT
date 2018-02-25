package com.a55haitao.wwht.adapter.firstpage;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.NewsFlashBean;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by a55 on 2017/3/17.
 */

public class NewsFlashAdapter extends BaseQuickAdapter<NewsFlashBean, BaseViewHolder> {
    private Context mContext;

    public NewsFlashAdapter(List<NewsFlashBean> data, Context mContext){
        super(R.layout.news_flash_list_item, data);
        this.mContext = mContext;
    }
    @Override
    protected void convert(BaseViewHolder helper, NewsFlashBean item) {
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_discount, item.sub_title)
                .setText(R.id.tv_browse, "浏览" + item.pv_count + "次")
        .setVisible(R.id.iv_time_out, isValid(item.state))
        .setVisible(R.id.viewline, helper.getLayoutPosition() == 0 ? false : true);

        Glide.with(mContext)
                .load(UPaiYunLoadManager.formatImageUrl(item.image_url, UpaiPictureLevel.TWICE))
                .transform(new GlideRoundTransform(mContext, 2))
                .placeholder(R.mipmap.ic_default_square_small)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.iv_goods));

        TextView textView = helper.getView(R.id.tv_title);
        textView.setEllipsize(TextUtils.TruncateAt.END);

    }

    private boolean isValid(int state) {    //  1是正常状态未过期，图标不显示
        return state == 1 ? false : true;
    }

}
