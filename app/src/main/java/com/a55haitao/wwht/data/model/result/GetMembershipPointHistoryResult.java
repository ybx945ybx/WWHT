package com.a55haitao.wwht.data.model.result;

import java.util.List;

/**
 * 获取积分历史记录
 *
 * @author 陶声
 * @since 2016-11-03
 */

public class GetMembershipPointHistoryResult {

    public int                       count;
    public int                       page;
    public int                    allpage;
    public List<MembershipPointBean> datas;

    public static class MembershipPointBean {
        public String comment;
        public int    user_id;
        public int    type_id;
        public String membership_point;
        public String created;
        public int    id;
    }
}
/*
{
		"count": 14,
		"page": 2,
		"datas": [{
			"comment": "\u53c2\u4e0e\u70b9\u8d5e",
			"user_id": 5948320,
			"type_id": 1,
			"membership_point": "+1",
			"created": "2016-09-12 18:54:43",
			"id": 321
		}, {
			"comment": "\u53c2\u4e0e\u70b9\u8d5e",
			"user_id": 5948320,
			"type_id": 1,
			"membership_point": "+1",
			"created": "2016-09-12 18:54:25",
			"id": 320
		}, {
			"comment": "\u53c2\u4e0e\u8bc4\u8bba",
			"user_id": 5948320,
			"type_id": 3,
			"membership_point": "+5",
			"created": "2016-09-12 18:50:44",
			"id": 319
		}, {
			"comment": "\u53c2\u4e0e\u8bc4\u8bba",
			"user_id": 5948320,
			"type_id": 3,
			"membership_point": "+5",
			"created": "2016-09-12 18:50:54",
			"id": 318
		}],
		"allpage": 2.0
	}
*/
