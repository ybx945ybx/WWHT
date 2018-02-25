package com.a55haitao.wwht.data.model.entity;

import com.a55haitao.wwht.data.model.annotation.Sex;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * User
 *
 * @author 陶声
 * @since 2016-10-13
 */

public class UserBean extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;

    private String username;

    @SerializedName("comm_counts")
    private CommCountsBean commCounts;

    @Sex private int sex;

    private String nickname;

    @SerializedName("head_img")
    private String headImg;

    private String mobile;

    private String email;

    private String location;

    private String signature;

    @SerializedName("user_title")
    private RealmList<UserTitle> userTitleList;

    @SerializedName("membership_point")
    private int membershipPoint;

    @SerializedName("post_count")
    private int postCount;

    @SerializedName("following_count")
    private int followingCount;

    @SerializedName("follower_count")
    private int followerCount;

    @SerializedName("user_token")
    private String userToken;

    @SerializedName("ucenter_token")
    private String ucenterToken;

    @SerializedName("qq_open_id")
    private String qqOpenId;

    @SerializedName("wx_open_id")
    private String wxOpenId;

    @SerializedName("wb_open_id")
    private String wbOpenId;

    @SerializedName("like_count")
    private int likeCount;

    @SerializedName("register_dt")
    private int registerDt;

    @SerializedName("is_operation")
    private int isOperation;

    private int balance;

    private int commissions;

    @SerializedName("lastLogin")
    private int lastLogin;

    @SerializedName("lastLoginIp")
    private String lastLoginIp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public
    @Sex
    int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUcenterToken() {
        return ucenterToken;
    }

    public void setUcenterToken(String ucenterToken) {
        this.ucenterToken = ucenterToken;
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWbOpenId() {
        return wbOpenId;
    }

    public void setWbOpenId(String wbOpenId) {
        this.wbOpenId = wbOpenId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getRegisterDt() {
        return registerDt;
    }

    public void setRegisterDt(int registerDt) {
        this.registerDt = registerDt;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getIsOperation() {
        return isOperation;
    }

    public void setIsOperation(int isOperation) {
        this.isOperation = isOperation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(int lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public RealmList<UserTitle> getUserTitleList() {
        return userTitleList;
    }

    public void setUserTitleList(RealmList<UserTitle> userTitleList) {
        this.userTitleList = userTitleList;
    }

    public int getMembershipPoint() {
        return membershipPoint;
    }

    public void setMembershipPoint(int membershipPoint) {
        this.membershipPoint = membershipPoint;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getCommissions() {
        return commissions;
    }

    public void setCommissions(int commissions) {
        this.commissions = commissions;
    }

    public CommCountsBean getCommCounts() {
        return commCounts;
    }

    public void setCommCounts(CommCountsBean commCounts) {
        this.commCounts = commCounts;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", commCounts=" + commCounts +
                ", sex=" + sex +
                ", nickname='" + nickname + '\'' +
                ", headImg='" + headImg + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", signature='" + signature + '\'' +
                ", userTitleList=" + userTitleList +
                ", membershipPoint=" + membershipPoint +
                ", postCount=" + postCount +
                ", followingCount=" + followingCount +
                ", followerCount=" + followerCount +
                ", userToken='" + userToken + '\'' +
                ", ucenterToken='" + ucenterToken + '\'' +
                ", qqOpenId='" + qqOpenId + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", wbOpenId='" + wbOpenId + '\'' +
                ", likeCount=" + likeCount +
                ", registerDt=" + registerDt +
                ", isOperation=" + isOperation +
                ", balance=" + balance +
                ", commissions=" + commissions +
                ", lastLogin=" + lastLogin +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                '}';
    }
}

/*
{
        "following_count":5,
        "commissions":0,
        "sex":1,
        "wx_open_id":null,
        "like_count":7,
        "id":5952367,
        "post_count":7,
        "register_dt":1464178772,
        "location":"",
        "follower_count":0,
        "user_title":[
            {
                "comment":"",
                "icon_url":"http://7xidjv.com1.z0.glb.clouddn.com/sb001.ico",
                "title":"超新星",
                "id":1,
                "created":"2016-08-26 10:05:36"
            }
        ],
        "email":"",
        "username":"ht2205",
        "comm_counts":{
            "ready_to_receive_counts":0,
            "commission_counts":0,
            "coupon_counts":0,
            "ready_to_deliver_counts":0,
            "star_post_counts":13,
            "ready_to_pay_counts":0,
            "star_product_counts":19,
            "like_counts":7,
            "easyopt_start_count":8,
            "post_counts":7,
            "easyopt_count":15,
            "star_special_counts":4,
            "membership_point":375,
            "follower_counts":0,
            "following_counts":5
        },
        "ucenter_token":"257459854c53dd9.17224804",
        "is_operation":0,
        "wb_open_id":null,
        "lastlogin":1464178772,
        "nickname":"手机用户_004",
        "lastloginip":"180.168.59.6",
        "mobile":"18000000004",
        "membership_point":375,
        "signature":"",
        "head_img":"http://st-prod.b0.upaiyun.com/avatar/2016/08/02/006e76e21e18c3755a87d748c942f208",
        "balance":"0.00",
        "qq_open_id":"C8D48F114245384D3D13C37AEFE71CB7"
    }
*/
