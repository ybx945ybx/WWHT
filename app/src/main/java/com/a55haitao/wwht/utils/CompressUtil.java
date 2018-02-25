package com.a55haitao.wwht.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩工具类
 * 压缩分两种：
 *      1.按尺寸压缩
 *      2.按质量压缩
 */
public class CompressUtil {

    /**
     * 按宽/高压缩
     * 注意，会覆盖原图
     */
    public static void compressWithSize(String fileName, int width,int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false ;
        Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
        if (bitmap != null){
            FileUtils.saveToFile(bitmap,100,new File(fileName));
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     *
     * @param size  压缩为Size大小的文件,size为kb
     */
    public static void compressWithQuality(String originFile,int size){
        Bitmap bitmap = BitmapFactory.decodeFile(originFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int quality = 100 ;
        if (bitmap !=null){
            // 1. 图片在内存中压缩
            do {
                if (quality > 10){
                    quality -= 10 ;
                }else {
                    quality -= 2 ;
                }
                if (quality <= 0){
                    break;
                }
                bos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            }while (bos.size() > size * 1024);

            FileOutputStream fos = null ;
            try {
                // 2.压缩到文件
                fos = new FileOutputStream(originFile);
                fos.write(bos.toByteArray());
                // 3.关闭流 回收资源
                bitmap.recycle();
                bos.close();
                fos.flush();
                fos.close();
            } catch (IOException e) {
                try {
                    bos.close();
                    fos.flush();
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

}
