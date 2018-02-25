package com.a55haitao.wwht.adapter.social;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 晒物 - 精选 - 热门标签Adapter
 *
 * @author 陶声
 * @since 2016-10-27
 */

public class HomeTagListAdapter extends BaseQuickAdapter<TagBean, BaseViewHolder> {
    private Fragment mFragment;
    private Activity mActivity;
    private int      mPicSize; // 图片尺寸

    public HomeTagListAdapter(List<TagBean> data, Fragment fragment) {
        super(R.layout.item_hot_tag_home, data);
        mFragment = fragment;
        mActivity = mFragment.getActivity();
        calcPicSize();
    }

    /**
     * 计算图片尺寸
     */
    private void calcPicSize() {
        mPicSize = mActivity.getResources().getDimensionPixelSize(R.dimen.hot_tag_pic_size);
    }

    @Override
    protected void convert(BaseViewHolder helper, TagBean item) {
        // 标签名
        helper.setText(R.id.tv_tag_name, String.valueOf("#" + item.name));

        // 图片
        Glide.with(mContext)
                .load(UpyUrlManager.getUrl(item.image_url, mPicSize))
                .placeholder(R.mipmap.ic_default_square_tiny)
                .transform(new GlideRoundTransform(mContext, 4))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .dontAnimate()
                .into((ImageView) helper.getView(R.id.img_pic));
    }
}
