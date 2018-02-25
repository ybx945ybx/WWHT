package com.a55haitao.wwht.ui.activity.base;

import com.umeng.analytics.MobclickAgent;

/**
 * 包含Fragment的Activity基类
 *
 * @author 陶声
 * @since 2016-10-10
 */
public abstract class BaseHasFragmentActivity extends BaseActivity {

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
