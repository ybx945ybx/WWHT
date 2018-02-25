package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.ShareBean;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-11-02
 */

public class GetPostTagResult {
    public int       status;
    public int       create_dt;
    public String    name;
    public int       weight;
    public String    image_url_small;
    public ShareBean share;
    public String    content;
    public String    image_url;
    public int       is_hot;
    public int       id;
}

/*
{
		"status": 1,
		"create_dt": 1463740341,
		"name": "YSL",
		"weight": 6,
		"image_url_small": "http://st-prod.b0.upaiyun.com/zeus/2016/10/25/7f0c6ae21d3d0c18fd1c68380a7bff99!/format/jpg",
		"share": {
			"small_icon": "http://h5.dev.55haitao.com/page/post_tag/14",
			"desc": "eeeeeeeeeeeeeeeeee",
			"icon": "http://searchimg.b0.upaiyun.com/cateOptImg/73/1476784589",
			"title": "YSL"
		},
		"content": "eeeeeeeeeeeeeeeeee",
		"image_url": "http://searchimg.b0.upaiyun.com/cateOptImg/73/1476784589",
		"is_hot": 1,
		"id": 14
	}
*/