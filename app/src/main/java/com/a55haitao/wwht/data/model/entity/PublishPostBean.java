package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 发布帖子实体类
 *
 * @author 陶声
 * @since 2016-11-14
 */

public class PublishPostBean implements Parcelable {
    public String                one_word;  // 帖子标题
    public String                content;   // 帖子文本
    public ArrayList<String>     tags;      // 标签
    public ArrayList<ImagesBean> images;    // 图片
    public LocationBean          location;  // 位置

    public static class LocationBean {
        public double latitude;
        public double region_id;
        public String location_desc;
        public double longitude;

        public LocationBean() {
            latitude = 0;
            region_id = 0;
            location_desc = "0";
            longitude = 0;
        }
    }

    public PublishPostBean() {
    }

    public PublishPostBean(String one_word, String content, ArrayList<String> tags, ArrayList<ImagesBean> images) {
        this.one_word = one_word;
        this.content = content;
        this.tags = tags;
        this.images = images;
    }

    protected PublishPostBean(Parcel in) {
        one_word = in.readString();
        content = in.readString();
        tags = in.createStringArrayList();
        images = in.createTypedArrayList(ImagesBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(one_word);
        dest.writeString(content);
        dest.writeStringList(tags);
        dest.writeTypedList(images);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PublishPostBean> CREATOR = new Creator<PublishPostBean>() {
        @Override
        public PublishPostBean createFromParcel(Parcel in) {
            return new PublishPostBean(in);
        }

        @Override
        public PublishPostBean[] newArray(int size) {
            return new PublishPostBean[size];
        }
    };
}
