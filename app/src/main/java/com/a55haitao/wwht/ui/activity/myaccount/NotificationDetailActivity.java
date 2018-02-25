package com.a55haitao.wwht.ui.activity.myaccount;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.HaiTimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通知详情页面（系统通知）
 */
public class NotificationDetailActivity extends BaseNoFragmentActivity {
    @BindView(R.id.title)                   DynamicHeaderView mTitle;                   // 标题
    @BindView(R.id.tv_notification_content) HaiTextView       mTvNotificationContent;   // 通知文本
    @BindView(R.id.tv_notification_time)    HaiTextView       mTvNotificationTime;      // 通知时间


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        ButterKnife.bind(this);
        initVars();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    /**
     * 初始化变量
     */
    public void initVars() {
        Intent intent = getIntent();
        if (intent != null) {
            String content = intent.getStringExtra("content");
            long   time    = intent.getLongExtra("time", 0);
            // 通知文本
            mTvNotificationContent.setText(content);
            // 通知时间戳
            if (time != 0) {
                mTvNotificationTime.setText(HaiTimeUtils.showPostTime(time));
            }
        }
    }

    /**
     * 跳转到本页面
     *
     * @param context context
     * @param content 文本内容
     */
    public static void toThisActivity(Context context, String content, long time, boolean newTask) {
        Intent intent = new Intent(context, NotificationDetailActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("time", time);
        if (newTask) {
            if (newTask) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        }
        context.startActivity(intent);
    }
}
