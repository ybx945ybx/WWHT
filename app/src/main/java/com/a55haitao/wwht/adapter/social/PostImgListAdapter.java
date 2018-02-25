package com.a55haitao.wwht.adapter.social;

import android.text.TextUtils;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.ImagesBean;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.finalteam.galleryfinal.model.BasePhotoInfo;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 编辑帖子 - Adapter
 *
 * @author 陶声
 * @since 2016-11-14
 */

public class PostImgListAdapter extends BaseQuickAdapter<BasePhotoInfo, BaseViewHolder> {

    public PostImgListAdapter(List<BasePhotoInfo> data) {
        super(R.layout.item_post_img, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BasePhotoInfo item) {
        ImageView imgPic = helper.getView(R.id.img_pic);
        if (item instanceof PhotoInfo) { // 本地图片
            PhotoInfo photoInfo = (PhotoInfo) item;
            String    path;
            if (!TextUtils.isEmpty(photoInfo.getCropPhotoPath())) {
                path = photoInfo.getCropPhotoPath();
            } else {
                path = photoInfo.getPhotoPath();
            }
            Glide.with(mContext)
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .dontAnimate()
                    .into(imgPic);
        } else if (item instanceof ImagesBean) { // 网络图片
            String imgUrl = UpyUrlManager.getUrl(((ImagesBean) item).url, imgPic.getMeasuredWidth());

            Glide.with(mContext)
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.ic_default_square_tiny)
                    .dontAnimate()
                    .into(imgPic);
        }
    }
}
