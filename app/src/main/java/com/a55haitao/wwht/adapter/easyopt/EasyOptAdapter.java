package com.a55haitao.wwht.adapter.easyopt;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.adapter.firstpage.RVBaseLoopAdapter;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.EasyOptBean;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.ui.activity.easyopt.EasyOptDetailActivity;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.ArrayList;

/**
 * 草单Item。支持左右循环滚动
 * Created by drolmen on 2016/12/27.
 */
public class EasyOptAdapter extends RVBaseLoopAdapter<EasyOptBean> {

    public EasyOptAdapter(Context context, ArrayList<EasyOptBean> arrayList) {
        super(context, arrayList, R.layout.item_easy_opt_layout_195_test);
        setClicklistenerForThis();
    }

    private void setClicklistenerForThis() {
        setOnItemClickListener(v -> {
            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_EasyDetail, String.valueOf(v.getTag()), mContext, TraceUtils.PageCode_Category, TraceUtils.PositionCode_Album + "", "");

            //            TraceUtils.addAnalysAct(TraceUtils.PageCode_EasyDetail, TraceUtils.PageCode_Category, TraceUtils.PositionCode_Album, (int) v.getTag() + "");

            EasyOptDetailActivity.toThisActivity(mContext, (int) v.getTag());
        });
    }

    public void bindView(RVBaseHolder holder, EasyOptBean easyOptBean) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (layoutParams.width <= 0) {

            holder.setVisibility(View.INVISIBLE, R.id.imgHeart, R.id.likeCountTxt);

            layoutParams.width = (int) (DeviceUtils.getScreenWidth() - DeviceUtils.getDensity() * 65);
            layoutParams.bottomMargin = (int) (DeviceUtils.getDensity() * 16);
            holder.itemView.setLayoutParams(layoutParams);
        }

        holder.itemView.setTag(easyOptBean.easyopt_id);

        holder.setText(R.id.eoNameTxt, easyOptBean.name);
        holder.setText(R.id.userNameTxt, easyOptBean.owner.getNickname());

        // 头像
        String cornerUrl = null;
        if (easyOptBean.owner.getUserTitleList().size() != 0) {
            cornerUrl = easyOptBean.owner.getUserTitleList().get(0).getIconUrl();
        }
        AvatarView avatar = holder.getView(R.id.headImg);
        avatar.loadImg(easyOptBean.owner.getHeadImg(), cornerUrl);

        GridLayout gridLayout     = holder.getView(R.id.gridLayout);
        int        childViewCount = gridLayout.getChildCount();
        int        productCount   = HaiUtils.getSize(easyOptBean.products);
        for (int i = 0; i < childViewCount; i++) {
            ImageView childAt = (ImageView) gridLayout.getChildAt(i);
            if (i < productCount) {
                childAt.setVisibility(View.VISIBLE);
                UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(), easyOptBean.products.get(i).coverImgUrl,
                        UpaiPictureLevel.TRIBBLE, R.mipmap.ic_default_square_small, childAt);
            } else {
                childAt.setVisibility(View.INVISIBLE);
            }
        }
    }
}
