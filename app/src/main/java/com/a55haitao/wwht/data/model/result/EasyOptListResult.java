package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.EasyOptBean;
import com.a55haitao.wwht.data.model.entity.ShareModel;

import java.util.ArrayList;

/**
 * Created by drolmen on 2016/12/27.
 */

public class EasyOptListResult {
    public ArrayList<EasyOptBean> easyopts; //获取草单列表时使用
    public EasyOptBean            easyopt;             //获取草单详情时使用

    public ShareModel share;               //分享

    public int page;
    public int allpage;
    public int count;
}
