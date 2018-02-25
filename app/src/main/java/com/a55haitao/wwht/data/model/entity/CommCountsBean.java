package com.a55haitao.wwht.data.model.entity;

import io.realm.RealmObject;

/**
 * myinfo
 *
 * @author 陶声
 * @since 2017-01-12
 */

public class CommCountsBean extends RealmObject {

    private int ready_to_receive_counts;
    private int commission_counts;
    private int coupon_counts;
    private int ready_to_deliver_counts;
    private int star_post_counts;
    private int ready_to_pay_counts;
    private int star_product_counts;
    private int like_counts;
    private int easyopt_start_count;
    private int post_counts;
    private int easyopt_count;
    private int star_special_counts;
    private int membership_point;
    private int follower_counts;
    private int following_counts;

    public int getReady_to_receive_counts() {
        return ready_to_receive_counts;
    }

    public void setReady_to_receive_counts(int ready_to_receive_counts) {
        this.ready_to_receive_counts = ready_to_receive_counts;
    }

    public int getCommission_counts() {
        return commission_counts;
    }

    public void setCommission_counts(int commission_counts) {
        this.commission_counts = commission_counts;
    }

    public int getCoupon_counts() {
        return coupon_counts;
    }

    public void setCoupon_counts(int coupon_counts) {
        this.coupon_counts = coupon_counts;
    }

    public int getReady_to_deliver_counts() {
        return ready_to_deliver_counts;
    }

    public void setReady_to_deliver_counts(int ready_to_deliver_counts) {
        this.ready_to_deliver_counts = ready_to_deliver_counts;
    }

    public int getStar_post_counts() {
        return star_post_counts;
    }

    public void setStar_post_counts(int star_post_counts) {
        this.star_post_counts = star_post_counts;
    }

    public int getReady_to_pay_counts() {
        return ready_to_pay_counts;
    }

    public void setReady_to_pay_counts(int ready_to_pay_counts) {
        this.ready_to_pay_counts = ready_to_pay_counts;
    }

    public int getStar_product_counts() {
        return star_product_counts;
    }

    public void setStar_product_counts(int star_product_counts) {
        this.star_product_counts = star_product_counts;
    }

    public int getLike_counts() {
        return like_counts;
    }

    public void setLike_counts(int like_counts) {
        this.like_counts = like_counts;
    }

    public int getEasyopt_start_count() {
        return easyopt_start_count;
    }

    public void setEasyopt_start_count(int easyopt_start_count) {
        this.easyopt_start_count = easyopt_start_count;
    }

    public int getPost_counts() {
        return post_counts;
    }

    public void setPost_counts(int post_counts) {
        this.post_counts = post_counts;
    }

    public int getEasyopt_count() {
        return easyopt_count;
    }

    public void setEasyopt_count(int easyopt_count) {
        this.easyopt_count = easyopt_count;
    }

    public int getStar_special_counts() {
        return star_special_counts;
    }

    public void setStar_special_counts(int star_special_counts) {
        this.star_special_counts = star_special_counts;
    }

    public int getMembership_point() {
        return membership_point;
    }

    public void setMembership_point(int membership_point) {
        this.membership_point = membership_point;
    }

    public int getFollower_counts() {
        return follower_counts;
    }

    public void setFollower_counts(int follower_counts) {
        this.follower_counts = follower_counts;
    }

    public int getFollowing_counts() {
        return following_counts;
    }

    public void setFollowing_counts(int following_counts) {
        this.following_counts = following_counts;
    }

    @Override
    public String toString() {
        return "CommCountsBean{" +
                "ready_to_receive_counts=" + ready_to_receive_counts +
                ", commission_counts=" + commission_counts +
                ", coupon_counts=" + coupon_counts +
                ", ready_to_deliver_counts=" + ready_to_deliver_counts +
                ", star_post_counts=" + star_post_counts +
                ", ready_to_pay_counts=" + ready_to_pay_counts +
                ", star_product_counts=" + star_product_counts +
                ", like_counts=" + like_counts +
                ", easyopt_start_count=" + easyopt_start_count +
                ", post_counts=" + post_counts +
                ", easyopt_count=" + easyopt_count +
                ", star_special_counts=" + star_special_counts +
                ", membership_point=" + membership_point +
                ", follower_counts=" + follower_counts +
                ", following_counts=" + following_counts +
                '}';
    }
}
