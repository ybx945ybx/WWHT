package com.a55haitao.wwht.utils;

/**
 * Created by 董猛 on 2016/10/15.
 */

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.data.model.entity.QueryBean;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;

/**
 * 自定义数据绑定属性
 */
public class DataBindiHp {
    @BindingAdapter({"imgUrl","size"})
    public static void imageLoad(ImageView imageView, String url,@UpaiPictureLevel int size){
        if (TextUtils.isEmpty(url)){
            return;
        }

        String tag = (String) imageView.getTag(R.id.u_pai_yun_origin_url);
        if (tag != null && tag.equals(url)){        //不需要加载
            return;
        }
        imageView.setTag(R.id.u_pai_yun_origin_url,url);
        UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(), url, size, R.mipmap.ic_default_square_small, imageView);
    }

    @BindingAdapter({"bindFlag"})
    public static void imageLoad(ImageView imageView, ProductBaseBean.SellerModel sellerModel){
        if (sellerModel == null || sellerModel.country == null) {
            return;
        }
        int flagResourceId = HaiUtils.getFlagResourceId(sellerModel.country, false);
        if (flagResourceId == -1){
            int px20 = HaiConstants.CompoundSize.PX_20;
            Glide.with(ActivityCollector.getTopActivity())
                    .load(sellerModel.flag)
                    .override((int) (1.8 * px20), px20)
                    .into(imageView);
        }else {
            imageView.setImageResource(flagResourceId);
        }
    }

    public interface SiteHandler {
        void onSiteAllClick(boolean isBrand) ;
    }

    public interface CategoryHandler extends SiteHandler {
        void onCategoryAllClick() ;
        void onItemClick(@PageType int pagetype , String name , QueryBean queryBean) ;
    }
}
