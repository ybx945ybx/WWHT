package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 帖子草稿实体类
 *
 * @author 陶声
 * @since 2016-11-14
 */

public class DraftPostBean implements Parcelable {
    public String               one_word;  // 帖子标题
    public String               content;   // 帖子文本
    public ArrayList<String>    tags;      // 标签
    public ArrayList<PhotoInfo> localImagePaths;    // 图片

    public DraftPostBean() {
    }

    protected DraftPostBean(Parcel in) {
        one_word = in.readString();
        content = in.readString();
        tags = in.createStringArrayList();
        localImagePaths = in.createTypedArrayList(PhotoInfo.CREATOR);
    }

    public static final Creator<DraftPostBean> CREATOR = new Creator<DraftPostBean>() {
        @Override
        public DraftPostBean createFromParcel(Parcel in) {
            return new DraftPostBean(in);
        }

        @Override
        public DraftPostBean[] newArray(int size) {
            return new DraftPostBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(one_word);
        dest.writeString(content);
        dest.writeStringList(tags);
        dest.writeTypedList(localImagePaths);
    }

    @Override
    public String toString() {
        return "DraftPostBean{" +
                "one_word='" + one_word + '\'' +
                ", content='" + content + '\'' +
                ", tags=" + tags +
                ", localImagePaths=" + localImagePaths +
                '}';
    }
}