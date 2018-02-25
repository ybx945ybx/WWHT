package com.a55haitao.wwht.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

/**
 * Created by 55haitao on 2016/10/28.
 */

public class HaiViewUtils {

    /**
     * 设置文本
     * @param parentView
     * @param viewId
     * @param msg
     */
    public static void setText(View parentView, int viewId, String msg) {
        TextView textView = (TextView) parentView.findViewById(viewId);
        if (textView != null) {
            textView.setText(msg);
        }
    }

    /**
     * 简单的图片加载，非商品封面
     * @param parentView
     * @param viewId
     * @param url
     */
    public static void loadImg(View parentView, int viewId, String url){
        UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(),
                url,
                UpaiPictureLevel.SINGLE,
                R.id.u_pai_yun_null_holder_tag,
                (ImageView) parentView.findViewById(viewId));
    }
}
