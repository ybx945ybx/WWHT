package com.a55haitao.wwht.adapter.easyopt;

import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.EasyOptBean;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 草单列表 - Adapter
 *
 * @author 陶声
 * @since 2017-05-09
 */

public class EasyoptListAdapter extends BaseQuickAdapter<EasyOptBean, BaseViewHolder> {
    public EasyoptListAdapter(List<EasyOptBean> data) {
        super(R.layout.item_easy_opt_layout_210_test, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EasyOptBean easyOptBean) {
        helper.setVisible(R.id.img_invisible, easyOptBean.is_visible == 1) // 仅自己可见
                .setText(R.id.likeCountTxt, String.valueOf(easyOptBean.like_count)) // 收藏数
                .setChecked(R.id.imgHeart, HaiUtils.transToBool(easyOptBean.likeit)) // 收藏状态
                .setText(R.id.eoNameTxt, easyOptBean.name) // 草单名
                .setText(R.id.userNameTxt, easyOptBean.owner.getNickname()) // 用户名
                .addOnClickListener(R.id.imgHeart);
        // 头像
        AvatarView avatar    = helper.getView(R.id.headImg);
        String     cornerUrl = null;
        if (easyOptBean.owner.getUserTitleList().size() != 0) {
            cornerUrl = easyOptBean.owner.getUserTitleList().get(0).getIconUrl();
        }
        avatar.loadImg(easyOptBean.owner.getHeadImg(), cornerUrl);
        // 商品图
        GridLayout      gridLayout = helper.getView(R.id.gridLayout);
        int             childCount = gridLayout.getChildCount();
        int             dataCount  = HaiUtils.getSize(easyOptBean.products);
        ImageView       tempView   = null;
        ProductBaseBean tempBean   = null;
        for (int i = childCount - 1; i >= 0; i--) {

            tempView = (ImageView) gridLayout.getChildAt(i);
            GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tempView.getLayoutParams();
            if (lp.width <= 0) {
                lp.width = (int) (DeviceUtils.getScreenWidth() - 55 * DeviceUtils.getDensity()) / 3;
                tempView.setLayoutParams(lp);
            }

            if (i < dataCount) {
                tempBean = easyOptBean.products.get(i);
                tempView.setTag(R.id.tag_for_img, tempBean.DOCID);
                tempView.setVisibility(View.VISIBLE);
                UPaiYunLoadManager.loadImage(mContext, tempBean.coverImgUrl, UpaiPictureLevel.TRIBBLE, R.mipmap.ic_default_square_small, tempView);
            } else {
                tempView.setVisibility(View.GONE);
            }
        }

        FrameLayout                  flContainer = helper.getView(R.id.fl_container);
        ViewGroup.MarginLayoutParams lp          = (ViewGroup.MarginLayoutParams) flContainer.getLayoutParams();
        // 最后一项
        if (mData.indexOf(easyOptBean) == mData.size() - 1) {
            lp.bottomMargin = DisplayUtils.dp2px(mContext, 20);
        } else {
            lp.bottomMargin = 0;
        }
        flContainer.setLayoutParams(lp);
    }
}
