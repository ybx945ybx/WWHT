package com.a55haitao.wwht.data.model.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 55haitao on 2016/11/2.
 */

public class RealmHistoryBean extends RealmObject {
    @PrimaryKey
    public String topic ;

    public long time ;
}
