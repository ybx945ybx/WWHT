package com.a55haitao.wwht.adapter.social;

import android.app.Activity;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.PostBean;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 相关晒物 Adapter
 *
 * @author 陶声
 * @since 2016-11-05
 */

public class RelatedPostListAdapter extends BaseQuickAdapter<PostBean, BaseViewHolder> {
    private Activity mActivity;
    private int      mPicWidth;

    public RelatedPostListAdapter(List<PostBean> data, Activity activity) {
        super(R.layout.item_related_post, data);
        mActivity = activity;
        calcPicSize();
    }

    private void calcPicSize() {
        mPicWidth = mActivity.getResources().getDimensionPixelSize(R.dimen.related_post_pic_width);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostBean data) {
        // 文本
        helper.setText(R.id.tv_desc, data.content);

        // 晒物图
        Glide.with(mContext)
                .load(UpyUrlManager.getUrl(data.image_url, mPicWidth))
                .placeholder(R.mipmap.ic_default_square_small)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.img_pic));
    }

}
