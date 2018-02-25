package com.a55haitao.wwht.data.model.result;

import java.util.List;

/**
 * 个人中心 - 专题
 *
 * @author 陶声
 * @since 2016-11-15
 */

public class GetUserStarSpecialsV11Result {

    public int               count;
    public int               page;
    public int               allpage;
    public List<SpecialBean> datas;

    public static class SpecialBean {
        public String image;
        public int    type;
        public String uri;
        public String name;
        public int    created;
    }
}
