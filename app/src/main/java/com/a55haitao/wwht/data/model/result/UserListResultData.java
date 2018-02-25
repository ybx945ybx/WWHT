package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.UserListBean;

import java.util.List;

/**
 * class description here
 *
 * @author 陶声
 * @since 2016-10-14
 */

public class UserListResultData {

    public int                count;
    public int                page;
    public int                allpage;
    public List<UserListBean> users;
}
/*
{
		"count": 12,
		"page": 1,
		"users": [{
			"username": "ht2405",
			"following_count": 92,
			"post_count": 4,
			"ucenter_token": "1057639987b906e4.76819708",
			"id": 5980844,
			"is_operation": 0,
			"sex": 1,
			"like_count": 57,
			"is_following": false,
			"location": "\u4e2d\u56fd,\u4e0a\u6d77\u5e02",
			"signature": "\u5076\u5c14\u559c\u6b22\u901b\u4e00\u4e9b\u65f6\u5c1a\u7f51\u7ad9\uff0c\u6bd4\u8f83\u559c\u6b22Gucci\u3001Lv\u3001Chanel\u3001Coach\uff01",
			"follower_count": 26,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/07/27/dcd505be9a349f72d7d61af351f8b8b0",
			"nickname": "Kandy|\u5b87\u7fd4",
			"email": ""
		}, {
			"username": "ht11002100045",
			"following_count": 44,
			"post_count": 0,
			"ucenter_token": "1057a1d31b44e294.06375586",
			"id": 6003551,
			"is_operation": 0,
			"sex": 2,
			"like_count": 0,
			"is_following": false,
			"location": "\u4f26\u6566",
			"signature": "\u7d2f\u554a",
			"follower_count": 2,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/08/03/09fcc7ec0a079d9c0842eef3a68ee170",
			"nickname": "Amy\u5728\u4f26\u6566",
			"email": "11002100045@ent.baijie.in"
		}, {
			"username": "ht11002100042",
			"following_count": 45,
			"post_count": 0,
			"ucenter_token": "657a1824aeec637.52700226",
			"id": 6003548,
			"is_operation": 0,
			"sex": 2,
			"like_count": 0,
			"is_following": false,
			"location": "\u4e2d\u56fd,\u4e0a\u6d77\u5e02",
			"signature": "",
			"follower_count": 0,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/08/03/ffc7c6db8242c6310f19c96151a19fe4",
			"nickname": "\u9c7c\u4ed4",
			"email": "11002100042@ent.baijie.in"
		}, {
			"username": "ht11002100040",
			"following_count": 50,
			"post_count": 0,
			"ucenter_token": "657a0533092b443.27632612",
			"id": 6003546,
			"is_operation": 0,
			"sex": 2,
			"like_count": 0,
			"is_following": false,
			"location": "\u5317\u4eac",
			"signature": "",
			"follower_count": 0,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/08/02/303662067debc5a0461abf692ccfd770",
			"nickname": "Emma_Hu",
			"email": "11002100040@ent.baijie.in"
		}, {
			"username": "ht11002100038",
			"following_count": 47,
			"post_count": 1,
			"ucenter_token": "2579f2ef62ce329.56239988",
			"id": 6003544,
			"is_operation": 0,
			"sex": 2,
			"like_count": 0,
			"is_following": false,
			"location": "\u4e2d\u56fd,\u4e0a\u6d77\u5e02",
			"signature": "",
			"follower_count": 1,
			"head_img": "http://st-prod.b0.upaiyun.com/fake_account/74752e8d3d148dec42b68dd3083a4bc0.jpg",
			"nickname": "KellyWang",
			"email": "11002100038@ent.baijie.in"
		}, {
			"username": "ht11002100036",
			"following_count": 69,
			"post_count": 0,
			"ucenter_token": "7579f2edd8ce3f4.83628124",
			"id": 6003542,
			"is_operation": 0,
			"sex": 2,
			"like_count": 0,
			"is_following": false,
			"location": "\u4e2d\u56fd,\u4e0a\u6d77\u5e02",
			"signature": "",
			"follower_count": 2,
			"head_img": "http://st-prod.b0.upaiyun.com/fake_account/a433cf699fe4195d724b2f14feaf37be.jpg",
			"nickname": "\u840c\u5e0c\u5e0c",
			"email": "11002100036@ent.baijie.in"
		}, {
			"username": "Anna_ytt",
			"following_count": 36,
			"post_count": 2,
			"ucenter_token": "057973703741bc3.06303469",
			"id": 6001583,
			"is_operation": 0,
			"sex": 2,
			"like_count": 24,
			"is_following": false,
			"location": "\u4e2d\u56fd,\u4e0a\u6d77\u5e02",
			"signature": "",
			"follower_count": 11,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/08/03/27088fe3297d0d3673dd32849448707a",
			"nickname": "\u5c0f\u5c0f\u5c0fX\u590f\u5b50",
			"email": ""
		}, {
			"username": "ht3504",
			"following_count": 64,
			"post_count": 4,
			"ucenter_token": "85798b698a21851.83421831",
			"id": 6002238,
			"is_operation": 0,
			"sex": 2,
			"like_count": 52,
			"is_following": false,
			"location": "\u4e2d\u56fd,\u4e0a\u6d77\u5e02",
			"signature": "\u5173\u7231\u963f\u59e8\uff0c\u4ece\u4f60\u505a\u8d77\u3002",
			"follower_count": 10,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/07/27/1d3224ba067adb876347c5528dcff336",
			"nickname": "Ouchan\u6c6a\u6c6a\u9171",
			"email": ""
		}, {
			"username": "ht11002100041",
			"following_count": 54,
			"post_count": 0,
			"ucenter_token": "157a170ef4ae532.74353582",
			"id": 6003547,
			"is_operation": 0,
			"sex": 2,
			"like_count": 0,
			"is_following": false,
			"location": "\u6c5f\u897f",
			"signature": "",
			"follower_count": 0,
			"head_img": "http://st-prod.b0.upaiyun.com/fake_account/551b46d8bbb715f782543610da02e928.jpg",
			"nickname": "\u9a6c\u5c0f\u5b81",
			"email": "11002100041@ent.baijie.in"
		}, {
			"username": "ht11002100039",
			"following_count": 47,
			"post_count": 0,
			"ucenter_token": "557a04ebd65bd53.24629664",
			"id": 6003545,
			"is_operation": 0,
			"sex": 2,
			"like_count": 0,
			"is_following": false,
			"location": "\u6d77\u5357",
			"signature": "",
			"follower_count": 1,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/08/02/6a6509aa6972a8a54c19a50608580167",
			"nickname": "\u4e56\u56e1\u56e1L",
			"email": "11002100039@ent.baijie.in"
		}, {
			"username": "ht_11002100017",
			"following_count": 46,
			"post_count": 3,
			"ucenter_token": "9574ff9a57a3b39.32589716",
			"id": 5951392,
			"is_operation": 0,
			"sex": 0,
			"like_count": 9,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 5,
			"head_img": "http://st-prod.b0.upaiyun.com/fake_account/daf6b162e261a542463225e2aef293ed.jpg",
			"nickname": "\u5662\u539f\u6765\u662f\u5c0f\u9ed1\u83c7\u51c9",
			"email": "11002100017@ent.baijie.in"
		}, {
			"username": "ht11002100037",
			"following_count": 58,
			"post_count": 2,
			"ucenter_token": "657a046d8676dc7.03294987",
			"id": 6003543,
			"is_operation": 0,
			"sex": 2,
			"like_count": 32,
			"is_following": false,
			"location": "\u6c5f\u82cf",
			"signature": "",
			"follower_count": 16,
			"head_img": "http://st-prod.b0.upaiyun.com/fake_account/da127ee11eeafc13f5e172e2a34a9e29.jpg",
			"nickname": "\u6843\u5b50\u9171",
			"email": "11002100037@ent.baijie.in"
		}],
		"allpage": 1.0
	}
*/
