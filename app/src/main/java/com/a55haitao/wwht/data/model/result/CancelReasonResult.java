package com.a55haitao.wwht.data.model.result;

import java.util.List;

/**
 * 取消订单理由
 *
 * @author 陶声
 * @since 2017-04-23
 */

public class CancelReasonResult {

    public List<ResonListBean> reson_list;

    public static class ResonListBean {
        public String reson_note;
        public int    reson_id;
    }
}
