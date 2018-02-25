package com.a55haitao.wwht.ui.activity.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.utils.SPUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by a55 on 2017/3/28.
 * 闪屏广告页
 */

public class AdActivity extends BaseNoFragmentActivity implements View.OnClickListener {

    private ImageView ivAd;
    private Intent    intent;
    boolean isnull = true;
    private Handler  handler;
    private Runnable runnable;
    private TextView tvSkip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ad);

        initView();
        initEvent();
        initData();

    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    private void initView() {
        handler = new Handler(getMainLooper());
        intent = new Intent(AdActivity.this, CenterActivity.class);
        tvSkip = (TextView) findViewById(R.id.tv_skip);
        ivAd = (ImageView) findViewById(R.id.iv_ad);

        if (HaiApplication.adImage != null) {
            isnull = false;
            Glide.with(mActivity)
                    .load(HaiApplication.adImage)
                    .dontAnimate()
                    .diskCacheStrategy(UPaiYunLoadManager.sDiskCacheStrategy)
                    .into(ivAd);
        } else {
            startActivity(intent);
        }

    }

    private void initEvent() {
        runnable = () -> startActivity(intent);
        if (!isnull) {
            handler.postDelayed(runnable, 3000);
        }

        tvSkip.setOnClickListener(this);
        ivAd.setOnClickListener(this);

    }

    private void initData() {

    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_skip) {
            startActivity(intent);
        } else if (v.getId() == R.id.iv_ad) {

            String uri = (String) SPUtils.get(AdActivity.this, "adUri", "");

            // GA 事件
            HaiApplication application = (HaiApplication) getApplication();
            Tracker        tracker     = application.getDefaultTracker();
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory("电商运营")
                    .setAction("开屏广告 Click")
                    .setLabel(uri)
                    .build());

            // 埋点
            //            TraceUtils.addAnalysActMatchUri(this, TraceUtils.PageCode_ADS, TraceUtils.PositionCode_Ads, uri);

            intent.putExtra("adUri", uri);
            startActivity(intent);
        }
    }
}
