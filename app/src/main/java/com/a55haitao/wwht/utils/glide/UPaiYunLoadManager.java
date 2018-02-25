package com.a55haitao.wwht.utils.glide;

/**
 * Created by 董猛 on 2016/9/23.
 */

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.utils.MessageDigestUtils;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

/**
 * UPaiYun图片加载管理器
 */
public class UPaiYunLoadManager {
    // 又拍云参数
    private static String FORMAT_START = "!";
    private static String FW           = "/fw/";
    private static String QUALITY      = "/quality/100";
    private static String FORMAT_END   = "/format";
    private static String JPG          = "/jpg";
    private static String PNG          = "/png";

    public final static DiskCacheStrategy sDiskCacheStrategy = DiskCacheStrategy.SOURCE;

    private final static RequestListener<String, GlideDrawable> sRequestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

            if (target instanceof ImageViewTarget) {
                // image
                ImageViewTarget viewTarget = (ImageViewTarget) target;
                ImageView       imageView  = (ImageView) viewTarget.getView();
                // 原始图片地址
                String originUrl = (String) imageView.getTag(R.id.u_pai_yun_default_tag);
                // holder
                Object resHolder = imageView.getTag(R.id.u_pai_yun_default_holder_tag);
                // 是否是圆形图片
                Object  roundTag = imageView.getTag(R.id.u_pai_yun_rounded_tag);
                boolean rounded  = roundTag != null && (boolean) roundTag;
                //圆角的角度
                Object roundDipTag = imageView.getTag(R.id.u_pai_yun_rounded_dip);
                // 重新加载原始图片
                DrawableRequestBuilder<String> glideBuilder = Glide.with(imageView.getContext())
                        .load(originUrl)
                        .diskCacheStrategy(sDiskCacheStrategy)
                        .dontAnimate();
                // placeholder
                if (resHolder != null) {
                    glideBuilder.placeholder((int) resHolder);
                }
                // transform
                if (rounded) {
                    glideBuilder.transform(new GlideCircleTransform(imageView.getContext()));
                } else if (roundDipTag != null) {
                    glideBuilder.transform(new GlideRoundTransform(imageView.getContext(), (int) roundDipTag));
                }
                glideBuilder.into(imageView);
            }

            return true;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            return false;
        }
    };

    /**
     * 加载图片,内涵upaiyun图片加载失败的处理（商品图片）
     */
    public static void loadImage(Context context, String originUrl, @UpaiPictureLevel int level, int holderResId, ImageView targetView) {
        loadImage(context, originUrl, level, holderResId, targetView, false);
    }

    /**
     * 加载图片,内涵upaiyun图片加载失败的处理（圆形图片）
     */
    public static void loadImage(Context context, String originUrl, @UpaiPictureLevel int level, int holderResId, ImageView targetView, boolean rounded) {
        // 缓存原始url
        targetView.setTag(R.id.u_pai_yun_default_tag, originUrl);
        // 检查是否是u盘云url，是的话直接加载，不是的话转换成u盘云
        String url = formatImageUrl(originUrl, level);
        // GlideBuilder
        DrawableRequestBuilder<String> glideBuilder = Glide.with(context)
                .load(url)
                .listener(sRequestListener)
                .diskCacheStrategy(sDiskCacheStrategy)
                .dontAnimate();
        // placeholder
        if (holderResId != R.id.u_pai_yun_null_holder_tag) {
            glideBuilder.placeholder(holderResId);
            targetView.setTag(R.id.u_pai_yun_default_holder_tag, holderResId);
        }
        // transform
        if (rounded) {
            glideBuilder.transform(new GlideCircleTransform(context));
            targetView.setTag(R.id.u_pai_yun_rounded_tag, rounded);
        }
        glideBuilder.into(targetView);
    }

    /**
     * 加载图片,内涵upaiyun图片加载失败的处理（圆角图片）
     */
    public static void loadImage(Context context, String originUrl, @UpaiPictureLevel int level, int holderResId, ImageView targetView, int dip) {
        // 缓存原始url
        targetView.setTag(R.id.u_pai_yun_default_tag, originUrl);
        // 检查是否是u盘云url，是的话直接加载，不是的话转换成u盘云
        String url = formatImageUrl(originUrl, level);
        // GlideBuilder
        DrawableRequestBuilder<String> glideBuilder = Glide.with(context)
                .load(url)
                .listener(sRequestListener)
                .transform(new GlideRoundTransform(context, dip))
                .diskCacheStrategy(sDiskCacheStrategy)
                .dontAnimate();
        // placeholder
        if (holderResId != R.id.u_pai_yun_null_holder_tag) {
            glideBuilder.placeholder(holderResId);
            targetView.setTag(R.id.u_pai_yun_default_holder_tag, holderResId);
        }
        // transform
        targetView.setTag(R.id.u_pai_yun_rounded_dip, dip);
        glideBuilder.into(targetView);
    }

    /**
     * @param url   任意url，如果是又拍云url则加format参数，若不是又拍云url，则先翻译为又拍云url后加format参数
     * @param level 指宽度
     * @return
     */
    public static String formatImageUrl(String url, @UpaiPictureLevel int level) {

        if (url.contains("//st-prod.b0.upaiyun.com")) {
            //是u盘云图片
            String thumb = null;
            switch (level) {
                case UpaiPictureLevel.SINGLE: {
                    thumb = FORMAT_START + FW + HaiConstants.CompoundSize.PX_Single + QUALITY + FORMAT_END;
                    break;
                }
                case UpaiPictureLevel.TWICE: {
                    thumb = FORMAT_START + FW + HaiConstants.CompoundSize.PX_Twice + QUALITY + FORMAT_END;
                    break;
                }
                case UpaiPictureLevel.TRIBBLE: {
                    thumb = FORMAT_START + FW + HaiConstants.CompoundSize.PX_Tribble + QUALITY + FORMAT_END;
                    break;
                }
                case UpaiPictureLevel.FOURTH: {
                    thumb = FORMAT_START + FW + HaiConstants.CompoundSize.PX_Fourth + QUALITY + FORMAT_END;
                    break;
                }

                default:
                    return null;
            }

            // 又拍云
            if (url.contains("!/format/png")) {
                // png
                thumb += PNG;
                // 替换
                url.replace("!/format/png", thumb);

            } else if (url.contains("!/format/jpg")) {
                // jpg
                thumb += JPG;
                url.replace("!/format/jpg", thumb);

            } else {
                if (url.contains(".png")) {
                    // png
                    thumb += PNG;
                } else {
                    // jpg
                    thumb += JPG;
                }
                url += thumb;
            }

        } else {
            // 不是的话转成upy
            String upUrl = transformProductThumail(url) + ".jpg";
            return formatImageUrl(upUrl, level);

        }
        return url;
    }

    /**
     * 商品图片URL转换
     *
     * @param originUrl 原始地址
     * @return
     */
    public static String transformProductThumail(String originUrl, int width, int height) {

        if (originUrl.contains("st-prod.b0.upaiyun.com")) {
            return originUrl;
        }

        String md5String     = MessageDigestUtils.getMD5(originUrl);
        String normalizeHost = normalizeHost(md5String.substring(0, md5String.length() / 2));
        return ApiUrl.NORMALIZE_HOST + normalizeHost + ".jpg!/fwfh/" + width + "x" + height + "/format/jpg";
    }

    /**
     * 商品图片URL转换
     *
     * @param originUrl 原始地址
     * @return
     */
    private static String transformProductThumail(String originUrl) {

        if (originUrl.contains("st-prod.b0.upaiyun.com")) {
            return originUrl;
        }

        String md5String     = MessageDigestUtils.getMD5(originUrl);
        String normalizeHost = normalizeHost(md5String.substring(0, md5String.length() / 2));
        return ApiUrl.NORMALIZE_HOST + normalizeHost;
    }

    private static String normalizeHost(String harf) {
        String result = "";

        if (!TextUtils.isEmpty(harf) && harf.length() >= 2) {
            char   startChar     = harf.charAt(0);
            String repleceString = null;

            switch (startChar) {
                case '8':
                    repleceString = "0";
                    break;
                case '9':
                    repleceString = "1";
                    break;
                case 'a':
                case 'A':
                    repleceString = "2";
                    break;
                case 'b':
                case 'B':
                    repleceString = "3";
                    break;
                case 'c':
                case 'C':
                    repleceString = "4";
                    break;
                case 'd':
                case 'D':
                    repleceString = "5";
                    break;
                case 'e':
                case 'E':
                    repleceString = "6";
                    break;
                case 'f':
                case 'F':
                    repleceString = "7";
                    break;
            }

            if (repleceString != null) {
                result = repleceString + harf.substring(1);
            } else {
                result = harf;
            }
        }

        return result;
    }

}
