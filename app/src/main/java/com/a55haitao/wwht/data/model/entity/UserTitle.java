package com.a55haitao.wwht.data.model.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * 用户等级
 *
 * @author 陶声
 * @since 2016-10-13
 */

public class UserTitle extends RealmObject implements Serializable {
    private int id;

    @SerializedName("icon_url")
    private String iconUrl;

    private String comment;

    private String title;

    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "UserTitle{" +
                "id=" + id +
                ", iconUrl='" + iconUrl + '\'' +
                ", comment='" + comment + '\'' +
                ", title='" + title + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
