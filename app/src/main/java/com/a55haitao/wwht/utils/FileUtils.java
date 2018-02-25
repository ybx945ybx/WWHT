package com.a55haitao.wwht.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.a55haitao.wwht.data.constant.ServerUrl;
import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by drolmen on 2016/11/16.
 */

public class FileUtils {

    public static        String PIC_PATH   = "";

    public static byte[] readFileToByte(String fileName){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = bos.toByteArray() ;
        return bos.toByteArray();
    }

    /**
     * 将图片正常化
     */
    public static void normalsizeFile(String fileName,int quality){
        if (quality <= 0){
            quality = 75 ;
        }
        int bitmapDegree = getBitmapDegree(fileName);

//        图片被翻转，调整图片
        if (bitmapDegree != 0){
            Bitmap bitmap = rotateBitmapByDegree(BitmapFactory.decodeFile(fileName), bitmapDegree);
            if (bitmap != null){
                FileOutputStream fos = null ;
                try {
                    fos = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                    bitmap.recycle();
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    public static void saveToFile(Bitmap bitmap, int quality, File file){
        if (!file.exists()){
            throw new RuntimeException("file not create");
        }
        FileOutputStream fos = null ;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            bitmap.recycle();
            fos.flush();
            fos.close();
        } catch (IOException e) {
            try {
                fos.flush();
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public static String saveBitmap(Context context, Bitmap b, String fileName) {
        String path     = getPicPath(context);
        String filePath = path + fileName;
        Logger.d("saveBitmap:jpegName = " + filePath);

        try {
            FileOutputStream     fout = new FileOutputStream(filePath);
            BufferedOutputStream bos  = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.PNG, 80, bos);
            bos.flush();
            bos.close();
            Logger.d("saveBitmapSuccess");
            return filePath;
        } catch (IOException e) {
            Logger.e("saveBitmap:Fail");
            e.printStackTrace();
            return null;
        }
    }

    public static void initPicPath(Context context) {
        File f = new File(context.getExternalCacheDir() + ServerUrl.PIC_PATH);
        if (!f.exists()) {
            f.mkdirs();
        }
        PIC_PATH = f.getAbsolutePath();
    }

    public static String getPicPath(Context context) {
        if (TextUtils.isEmpty(PIC_PATH)) {
            initPicPath(context);
        }
        return PIC_PATH;
    }
}
