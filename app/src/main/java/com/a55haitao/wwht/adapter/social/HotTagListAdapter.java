package com.a55haitao.wwht.adapter.social;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.HotTagListResult;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.GlideRoundTransform;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 热门标签页面 - 标签列表
 *
 * @author 陶声
 * @since 2016-10-27
 */

public class HotTagListAdapter extends BaseQuickAdapter<HotTagListResult.RetBean, BaseViewHolder> {

    private int[] mImgIds = {R.id.img_pic1, R.id.img_pic2, R.id.img_pic3};

    private Activity mActivity;
    private int      mScreenWidth;// 屏幕高度
    private int      mPicSize;    // 图片尺寸

    public HotTagListAdapter(Activity activity, List<HotTagListResult.RetBean> data) {
        super(R.layout.item_hot_tag_list, data);
        mActivity = activity;
        init();
    }

    /**
     * 计算图片尺寸
     */
    private void init() {
        mScreenWidth = DisplayUtils.getScreenWidth(mActivity);
        mPicSize = (mScreenWidth - DisplayUtils.dp2px(mActivity, 60)) / 3;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotTagListResult.RetBean data) {
        // 标签名
        helper.setText(R.id.tv_title, String.valueOf("#" + data.tag.name));
        // 实际长度
        int len = data.posts.size();
        // 隐藏空图片
        for (int i = 0; i < 3; i++) {
            helper.setVisible(mImgIds[i], i < len);
        }
        // 加载图片
        for (int i = 0; i < len; i++) {
            ImageView              img = helper.getView(mImgIds[i]);
            ViewGroup.LayoutParams lp  = img.getLayoutParams();
            lp.width = mPicSize;
            lp.height = mPicSize;
            img.setLayoutParams(lp);

            Glide.with(mContext)
                    .load(UpyUrlManager.getUrl(data.posts.get(i).image_url, mPicSize))
                    .centerCrop()
                    .placeholder(R.mipmap.ic_default_square_tiny)
                    .transform(new GlideRoundTransform(mContext, 8))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(img);

            helper.addOnClickListener(mImgIds[i]);
        }
        // 最后一条数据隐藏Divider
        helper.setVisible(R.id.view_divider, mData.indexOf(data) != mData.size() - 1);
    }
}
