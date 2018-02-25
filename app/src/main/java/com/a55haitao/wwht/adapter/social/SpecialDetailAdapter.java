package com.a55haitao.wwht.adapter.social;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.result.GetPostSpecialInfoResult;
import com.a55haitao.wwht.data.model.annotation.SpecialDetailContentType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUriMatchUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-11-10
 */

public class SpecialDetailAdapter extends BaseMultiItemQuickAdapter<GetPostSpecialInfoResult.ContentBean, BaseViewHolder> {

    private Activity mActivity;
    private int      mPicWidth; // 图片宽度

    public SpecialDetailAdapter(Activity activity, List<GetPostSpecialInfoResult.ContentBean> data) {
        super(data);
        addItemType(SpecialDetailContentType.TYPE_TEXT, R.layout.item_text);
        addItemType(SpecialDetailContentType.TYPE_IMG, R.layout.item_img);
        addItemType(SpecialDetailContentType.TYPE_PRODUCT, R.layout.item_product_special);
        mActivity = activity;
        calcPicWidth();
    }

    /**
     * 计算图片宽度
     */
    public void calcPicWidth() {
        mPicWidth = DisplayUtils.getScreenWidth(mActivity) - DisplayUtils.dp2px(mActivity, 25) * 2;
    }

    @Override
    protected void convert(BaseViewHolder helper, GetPostSpecialInfoResult.ContentBean data) {
        switch (helper.getItemViewType()) {
            case SpecialDetailContentType.TYPE_TEXT: // 文字
                helper.setText(R.id.tv_content, data.data.content);
                break;
            case SpecialDetailContentType.TYPE_IMG: // 图片

                float scaleRate = data.data.imgSize.width * 1.0f / mPicWidth;
                int picHeight = (int) (data.data.imgSize.height / scaleRate);

                ImageView img = helper.getView(R.id.img_pic);
                ViewGroup.LayoutParams lp = img.getLayoutParams();
                lp.width = mPicWidth;
                lp.height = picHeight;
                img.setLayoutParams(lp);

                Glide.with(mContext)
                        .load(UpyUrlManager.getUrl(data.data.content, mPicWidth))
                        .override(mPicWidth, picHeight)
                        .placeholder(R.mipmap.ic_default_rect)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .dontAnimate()
                        .into((ImageView) helper.getView(R.id.img_pic));
                helper.getView(R.id.img_pic).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(data.data.uri)){
                            return;
                        }
                        HaiUriMatchUtils.matchUri(mActivity, data.data.uri);
                    }
                });
                break;
            case SpecialDetailContentType.TYPE_PRODUCT: // 商品
                helper.setText(R.id.tv_title, TextUtils.isEmpty(data.data.brand.name_cn) ? data.data.brand.name_en : data.data.brand.name_cn)
                        .setText(R.id.tv_desc, data.data.name)
                        .setText(R.id.tv_price, String.format("¥%.0f", data.data.price / 100))
                        .addOnClickListener(R.id.ll_product);

                UPaiYunLoadManager.loadImage(mActivity,data.data.img_cover, UpaiPictureLevel.FOURTH,R.mipmap.ic_default_square_tiny,(ImageView) helper.getView(R.id.img_pic));
                break;
        }
    }
}
