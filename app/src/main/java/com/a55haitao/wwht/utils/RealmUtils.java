package com.a55haitao.wwht.utils;

import com.a55haitao.wwht.data.model.entity.RealmHistoryBean;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmSchema;

/**
 * Created by 55haitao on 2016/11/3.
 */

public class RealmUtils {

    private static RealmConfiguration sConfig;

    public static void addHistory(RealmHistoryBean bean) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.insertOrUpdate(bean);
        realm.commitTransaction();
    }

    public static Realm getInstance() {
        // 版本号从0开始
        if (sConfig == null) {
            sConfig = new RealmConfiguration.Builder()
                    .schemaVersion(1) // 版本号从0开始
                    .migration((realm, oldVersion, newVersion) -> {
                        if (oldVersion == 0) {
                            Logger.d("版本迁移");
                            RealmSchema schema = realm.getSchema();
                            if (!schema.contains("CommCountsBean")) {
                                // 添加CommCountsBean表
                                schema.create("CommCountsBean")
                                        .addField("ready_to_receive_counts", int.class)
                                        .addField("commission_counts", int.class)
                                        .addField("coupon_counts", int.class)
                                        .addField("ready_to_deliver_counts", int.class)
                                        .addField("star_post_counts", int.class)
                                        .addField("ready_to_pay_counts", int.class)
                                        .addField("star_product_counts", int.class)
                                        .addField("like_counts", int.class)
                                        .addField("easyopt_start_count", int.class)
                                        .addField("post_counts", int.class)
                                        .addField("easyopt_count", int.class)
                                        .addField("star_special_counts", int.class)
                                        .addField("membership_point", int.class)
                                        .addField("follower_counts", int.class)
                                        .addField("following_counts", int.class);
                                // 添加CommCountsBean字段至UserBean
                                schema.get("UserBean")
                                        .addRealmObjectField("commCounts", schema.get("CommCountsBean"));
                            }
                            oldVersion++;
                        }
                    })
                    .build();
        }
        return Realm.getInstance(sConfig);
    }
}
