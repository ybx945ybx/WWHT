package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.entity.UserTitle;

import java.util.List;

/**
 * 推荐用户
 *
 * @author 陶声
 * @since 2016-11-01
 */

public class GetHotUserListResult {

    public List<UsersBean> users;

    public static class UsersBean {
        public String          username;
        public int             following_count;
        public int             post_count;
        public String          ucenter_token;
        public int             membership_point;
        public int             is_operation;
        public int             sex;
        public String          email;
        public String          nickname;
        public int             like_count;
        public boolean         is_following;
        public String          location;
        public String          signature;
        public int             follower_count;
        public String          head_img;
        public int             id;
        public List<String>    posts_images;
        public List<UserTitle> user_title;

        @Override
        public String toString() {
            return "UsersBean{" +
                    "user_title=" + user_title +
                    ", username='" + username + '\'' +
                    ", following_count=" + following_count +
                    ", post_count=" + post_count +
                    ", ucenter_token='" + ucenter_token + '\'' +
                    ", membership_point=" + membership_point +
                    ", is_operation=" + is_operation +
                    ", sex=" + sex +
                    ", email='" + email + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", like_count=" + like_count +
                    ", is_following=" + is_following +
                    ", location='" + location + '\'' +
                    ", signature='" + signature + '\'' +
                    ", follower_count=" + follower_count +
                    ", head_img='" + head_img + '\'' +
                    ", id=" + id +
                    ", posts_images=" + posts_images +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetHotUserListResult{" +
                "users=" + users +
                '}';
    }
}

/*
{
		"users": [{
			"username": "\u6768\u4e00\u821f",
			"posts_images": [],
			"following_count": 0,
			"post_count": 0,
			"ucenter_token": "10575048f756e5f8.11661228",
			"membership_point": 1,
			"is_operation": 0,
			"sex": 0,
			"email": "zachary.y.yang@gmail.com",
			"nickname": "\u624b\u673a\u7528\u6237_2314",
			"like_count": 0,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 1,
			"head_img": "",
			"user_title": [],
			"id": 28398
		}, {
			"username": "\u67e0\u6aac\u8349",
			"posts_images": [],
			"following_count": 0,
			"post_count": 0,
			"ucenter_token": "857860a9287eb47.88251014",
			"membership_point": 1,
			"is_operation": 0,
			"sex": 0,
			"email": "linfb99@tom.com",
			"nickname": "\u624b\u673a\u7528\u6237_2848",
			"like_count": 0,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 3,
			"head_img": "",
			"user_title": [],
			"id": 5725
		}, {
			"username": "\u516b\u70b9\u4e8c\u5341\u7684\u82b1\u82b1",
			"posts_images": ["http://st-prod.b0.upaiyun.com/post/2016/10/19/9e786654a68d234676ac4d2553306d4f"],
			"following_count": 0,
			"post_count": 1,
			"ucenter_token": "05742c1393cd429.22447421",
			"membership_point": 109,
			"is_operation": 0,
			"sex": 1,
			"email": "2850583143@qq.com",
			"nickname": "\u624b\u673a",
			"like_count": 0,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 3,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/05/23/5ff7e59818e43c45c4f26af2a64314e6",
			"user_title": [],
			"id": 8018
		}, {
			"username": "leonard",
			"posts_images": [],
			"following_count": 0,
			"post_count": 0,
			"ucenter_token": "85783ffd4a33b66.03967397",
			"membership_point": 1,
			"is_operation": 0,
			"sex": 0,
			"email": "leonardwang8@gmail.com",
			"nickname": "\u624b\u673a\u7528\u6237_2805",
			"like_count": 0,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 1,
			"head_img": "",
			"user_title": [],
			"id": 17483
		}, {
			"username": "semi",
			"posts_images": ["http://st-prod.b0.upaiyun.com/post/2016/07/18/ef81af00bc507c2756f11aa1f8e4aadf", "http://st-prod.b0.upaiyun.com/post/2016/07/18/fb5e8b54412cf43737d4825b5528b092", "http://st-prod.b0.upaiyun.com/post/2016/07/17/9d50197f7a287117f75f29d0ee227188", "http://st-prod.b0.upaiyun.com/post/2016/07/17/ff98d679aed24af296763d9c1b61f5db"],
			"following_count": 0,
			"post_count": 13,
			"ucenter_token": "1057889ec0b94af6.61208491",
			"membership_point": 1380,
			"is_operation": 0,
			"sex": 2,
			"email": "852828113@qq.com",
			"nickname": "semi",
			"like_count": 42,
			"is_following": false,
			"location": "",
			"signature": "\u56e4\u8d27\u7656\uff0c\u7231\u6d77\u6dd8\uff0c\u7231\u65c5\u6e38",
			"follower_count": 21,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/07/15/66b04ede8876c3400b6e6cfed1928bae",
			"user_title": [],
			"id": 37400
		}, {
			"username": "Andrea",
			"posts_images": ["http://aaaaa", "http://ssss", "http://st-prod.b0.upaiyun.com/avatar/2016/09/14/7ddaqagxsiij4v8i3e117yw1pm3uenxx", "http://a.n.c"],
			"following_count": 1,
			"post_count": 16,
			"ucenter_token": "057583ca21a0aa7.06573460",
			"membership_point": 100400,
			"is_operation": 0,
			"sex": 2,
			"email": "692126487@qq.com",
			"nickname": "\u8427\u8427DeXiao",
			"like_count": 12,
			"is_following": false,
			"location": "\u4e2d\u56fd,\u91cd\u5e86\u5e02",
			"signature": "\u4e00\u8d77\u62d4\u8349\u5427\uff5e",
			"follower_count": 3,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/06/08/fdfb2e036637d2cbabef865d23b6efe3",
			"user_title": [],
			"id": 3260
		}, {
			"username": "\u7b71\u4e4b\u598d",
			"posts_images": ["http://st-prod.b0.upaiyun.com/post/2016/07/18/08f330bf0f3e9d0b586ff8389fb169ba", "http://st-prod.b0.upaiyun.com/post/2016/07/16/663b75f5eec7cd12d366dc3eafa1f477", "http://st-prod.b0.upaiyun.com/post/2016/07/13/4f877ae876391f492bfd50f97356d5c4"],
			"following_count": 2,
			"post_count": 5,
			"ucenter_token": "657846e53826016.31048493",
			"membership_point": 900,
			"is_operation": 0,
			"sex": 2,
			"email": "317867387@qq.com",
			"nickname": "\u7b71\u4e4b\u598d",
			"like_count": 38,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 30,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/07/13/0f76c2e40d6363226e82df3eb5c44a89",
			"user_title": [],
			"id": 329521
		}, {
			"username": "ht_11002100014",
			"posts_images": ["http://st-prod.b0.upaiyun.com/post/2016/06/02/f5037f630c00a0fd34b952aa555d47e9", "http://st-prod.b0.upaiyun.com/post/2016/06/02/aab40178eddff8f21e351cd792fb7317", "http://st-prod.b0.upaiyun.com/post/2016/05/26/057608c2246a43e2786a440fc2e2f0d7"],
			"following_count": 36,
			"post_count": 5,
			"ucenter_token": "157466fae07ef38.38655070",
			"membership_point": 900,
			"is_operation": 0,
			"sex": 0,
			"email": "11002100014@ent.baijie.in",
			"nickname": "\u8d85\u6a21\u5fc3\u5c0f\u4e2a",
			"like_count": 8,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 7,
			"head_img": "http://st-prod.b0.upaiyun.com/fake_account/a7692e1777577c85ac4ef746b1c68428.jpg",
			"user_title": [],
			"id": 5951389
		}, {
			"username": "anshaoriji",
			"posts_images": ["http://st-prod.b0.upaiyun.com/post/2016/07/19/7a6061ee42b39591bf22bc5c2410a441", "http://st-prod.b0.upaiyun.com/post/2016/07/12/36149a8a8a52368a4d95f3212c7763b0", "http://st-prod.b0.upaiyun.com/post/2016/07/09/05143b44a1b02a2ff846e6fa540bf05a", "http://st-prod.b0.upaiyun.com/post/2016/07/09/a4f3ec76536702f7bfab747c79425730"],
			"following_count": 31,
			"post_count": 9,
			"ucenter_token": "4576d3540062dc6.47492932",
			"membership_point": 1740,
			"is_operation": 0,
			"sex": 2,
			"email": "cjmmm@hotmail.com",
			"nickname": "\u58a8\u537f",
			"like_count": 129,
			"is_following": false,
			"location": "",
			"signature": "",
			"follower_count": 37,
			"head_img": "http://st-prod.b0.upaiyun.com/avatar/2016/06/27/b33d42f30635ca055bb0cbb2acf4396e",
			"user_title": [],
			"id": 42776
		}]
	}
*/