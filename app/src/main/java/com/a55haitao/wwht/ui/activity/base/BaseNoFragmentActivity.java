package com.a55haitao.wwht.ui.activity.base;

import com.umeng.analytics.MobclickAgent;

/**
 * 不包含Fragment的Activity基类
 *
 * @author 陶声
 * @since 2016-10-10
 */
public abstract class BaseNoFragmentActivity extends BaseActivity {

    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
}
