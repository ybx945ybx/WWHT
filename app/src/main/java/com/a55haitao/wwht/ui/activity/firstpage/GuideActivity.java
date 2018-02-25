package com.a55haitao.wwht.ui.activity.firstpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.firstpage.GuideAdapter;
import com.a55haitao.wwht.data.constant.HaiConstants;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuideActivity extends BaseNoFragmentActivity {

    @BindView(R.id.vp_content)             ViewPager    mVpContent;              // 闪屏页图片
    @BindView(R.id.ll_indicator_container) LinearLayout mLlIndicatorContainer;   // 指示器容器
    @BindView(R.id.img_indicator_selected) ImageView    mImgIndicatorSelected;   // 选中的指示器
    @BindView(R.id.tv_skip)                TextView     mTvSkip;                 // 跳过
    @BindView(R.id.tv_go_home)             TextView     mTvGoHome;               // 去首页

    @BindString(R.string.key_is_first_enter) String KEY_IS_FIRST_ENTER;

    private List<ImageView> mImgList;                                                        // 图片列表
    private int[] mImgRes = {R.mipmap.bg_guide_a, R.mipmap.bg_guide_b, R.mipmap.bg_guide_c}; // 图片资源
    private int mPointDis;                                                                   // indicator圆点距离

    private int currentPosition;

    private boolean hasEnter = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        SPUtils.put(this, KEY_IS_FIRST_ENTER, false); // 修改sp配置
        initViews(savedInstanceState);
    }

    @Override
    protected String getActivityTAG() {
        return null;
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {

        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mImgList = new ArrayList<>(3);

        for (int i = 0; i < mImgRes.length; i++) {
            // 添加背景图
            ImageView img = new ImageView(this);
            img.setImageResource(mImgRes[i]);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImgList.add(img);

            // 添加小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_black_solid_circle_indicator_unselected);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ((int) (HaiConstants.CompoundSize.PX_10 * 1.6)),
                    ((int) (HaiConstants.CompoundSize.PX_10 * 1.6)));

            if (i > 0) {
                params.leftMargin = HaiConstants.CompoundSize.PX_10 * 2;
            }

            point.setLayoutParams(params);
            mLlIndicatorContainer.addView(point);
        }

        // 设置适配器
        mVpContent.setAdapter(new GuideAdapter(mImgList));

        mImgIndicatorSelected.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mImgIndicatorSelected.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointDis = mLlIndicatorContainer.getChildAt(1).getLeft() - mLlIndicatorContainer.getChildAt(0).getLeft();
            }
        });

        // ViewPager滑动监听
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean isScrolling = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int leftMargin = (int) (mPointDis * (positionOffset + position));

                if (position == mImgRes.length - 1 && isScrolling && !hasEnter) {
                    hasEnter = true;
                    startActivity(new Intent(mActivity, CenterActivity.class));
                    finish();
                    overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
                }

                RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) mImgIndicatorSelected.getLayoutParams();
                lp.leftMargin = leftMargin;
                mImgIndicatorSelected.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isScrolling = (state == 1);
            }
        });
    }

    @OnClick(R.id.tv_skip)
    public void skip() {
        hasEnter = true;
        startActivity(new Intent(mActivity, CenterActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
    }

    @OnClick(R.id.tv_go_home)
    public void goHome() {
        if (currentPosition == mImgRes.length - 1 && !hasEnter) {
            hasEnter = true;
            startActivity(new Intent(mActivity, CenterActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_next, R.anim.exit_next);
        }
    }

}
