package com.a55haitao.wwht.adapter.easyopt;

import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.EasyoptList4AddResult;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 收藏至我的草单 - Adapter
 *
 * @author 陶声
 * @since 2017-01-03
 */

public class AddToEasyoptListAdapter extends BaseQuickAdapter<EasyoptList4AddResult.EasyoptBean, BaseViewHolder> {
    public AddToEasyoptListAdapter(List<EasyoptList4AddResult.EasyoptBean> data) {
        super(R.layout.item_add_to_my_easyopt, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EasyoptList4AddResult.EasyoptBean item) {
        // 草单Logo
        Glide.with(mContext)
                .load(UpyUrlManager.getUrl(item.img_cover, DisplayUtils.dp2px(mContext, 40)))
                .placeholder(R.mipmap.ic_default_square_tiny)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into((ImageView) helper.getView(R.id.img_easyopt_logo));
        helper.setText(R.id.tv_easyopt_name, item.name)
                .setText(R.id.tv_easyopt_count, String.format("商品 %d", item.product_count));

        if (item.contain == 0) {
            helper.setAlpha(R.id.ll_container, 1);
        } else {
            helper.setAlpha(R.id.ll_container, 0.5f);
        }
    }
}
