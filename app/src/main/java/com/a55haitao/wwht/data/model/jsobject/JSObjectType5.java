package com.a55haitao.wwht.data.model.jsobject;

/**
 * Created by Haotao_Fujie on 2016/11/22.
 */

/**
 * Type 5
 * { type:5, alert_title, alert_desc ,title:,  icon:, desc:, small_icon:, amount:0.0 canEarnMemberShipPoint:1}
 */
public class JSObjectType5 extends BaseJSObject {
    public String  alert_title;
    public String  alert_desc;
    public String  title;
    public String  icon;
    public String  desc;
    public String  url;
    public double  amount;
    public boolean canEarnMemberShipPoint;

    @Override
    public String toString() {
        return "JSObjectType5{" +
                "alert_title='" + alert_title + '\'' +
                ", alert_desc='" + alert_desc + '\'' +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", amount=" + amount +
                ", canEarnMemberShipPoint=" + canEarnMemberShipPoint +
                '}';
    }
}
