package com.a55haitao.wwht.data.model.entity;

import com.a55haitao.wwht.utils.HaiTimeUtils;

import java.util.ArrayList;

/**
 * Created by Haotao_Fujie on 16/10/12.
 */

public class GrantCouponsBean {

    public ArrayList<GrantCoupon> couponq_list;

    public class GrantCoupon {
        public long   id;
        public String type;
        public String title;
        public String subtitle;
        public String desc;
        public String images;
        public int    expiry;       // 优惠券领取后多少天内有效
        public int    rangetype;    // 0立即生效   1定时生
        public long   begintime;
        public long   endtime;
        // 是否已领取
        public int    is_receive;   // 0可领  1不可领
        public int    maxnum;      // 总共可领取数
        public int    receive;      // 总共已被领取数
        public int    user_receive; // 用户已领取数
        public int    user_max;     // 用户总共可领取数

        public boolean canReceive(){
            return (maxnum > 0 && receive >= maxnum) || user_receive >= user_max ? false : true;
        }
        public String getTermOfValidity() {
            return rangetype == 0 ? (expiry > 0 ? "领取后" + expiry + "天内有效" : HaiTimeUtils.showDate(begintime) + " - " + HaiTimeUtils.showDate(endtime)) : HaiTimeUtils.showDate(begintime) + " - " + HaiTimeUtils.showDate(endtime);
//            return expiry > 0 ? "领取后" + expiry + "天内有效" : HaiTimeUtils.showDate(begintime) + " - " + HaiTimeUtils.showDate(endtime);
        }

    }

}
