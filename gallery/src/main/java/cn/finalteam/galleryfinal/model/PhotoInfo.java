/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.galleryfinal.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Desction:图片信息
 * Author:pengjianbo
 * Date:15/7/30 上午11:23
 */
public class PhotoInfo extends BasePhotoInfo implements Serializable, Parcelable {

    private int    photoId;
    private String photoPath;
    private String cropPhotoPath;   //  裁剪图片后的路径
    //private String thumbPath;
    private int    width;
    private int    height;

    public PhotoInfo() {
        super();
    }

    protected PhotoInfo(Parcel in) {
        super(in);
        photoId = in.readInt();
        photoPath = in.readString();
        cropPhotoPath = in.readString();
        width = in.readInt();
        height = in.readInt();
    }

    public static final Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
        @Override
        public PhotoInfo createFromParcel(Parcel in) {
            return new PhotoInfo(in);
        }

        @Override
        public PhotoInfo[] newArray(int size) {
            return new PhotoInfo[size];
        }
    };

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getCropPhotoPath() {
        return cropPhotoPath;
    }

    public void setCropPhotoPath(String cropPhotoPath) {
        this.cropPhotoPath = cropPhotoPath;
    }

    //
    //public String getThumbPath() {
    //    return thumbPath;
    //}
    //
    //public void setThumbPath(String thumbPath) {
    //    this.thumbPath = thumbPath;
    //}


    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof PhotoInfo)) {
            return false;
        }
        PhotoInfo info = (PhotoInfo) o;
        if (info == null) {
            return false;
        }

        return TextUtils.equals(info.getPhotoPath(), getPhotoPath());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(photoId);
        parcel.writeString(photoPath);
        parcel.writeString(cropPhotoPath);
        parcel.writeInt(width);
        parcel.writeInt(height);
    }

    @Override
    public String toString() {
        return "PhotoInfo{" +
                "photoId=" + photoId +
                ", photoPath='" + photoPath + '\'' +
                ", cropPhotoPath='" + cropPhotoPath + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
