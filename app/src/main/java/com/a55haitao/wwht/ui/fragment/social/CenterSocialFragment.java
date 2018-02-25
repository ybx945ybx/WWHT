package com.a55haitao.wwht.ui.fragment.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.SocialPagerAdapter;
import com.a55haitao.wwht.data.model.entity.DraftPostBean;
import com.a55haitao.wwht.ui.activity.social.AddFriendsActivity;
import com.a55haitao.wwht.ui.activity.social.PostEditActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * 社区Fragment
 * {@link SocialPostFragment 精选Fragment}
 * {@link FollowFragment 关注Fragment}
 *
 * @author 陶声
 * @since 2016-10-12
 */
public class CenterSocialFragment extends BaseFragment {

    @BindView(R.id.ib_add_friend)   ImageButton mIbAddFriend;        // 添加好友
    @BindView(R.id.tab)             TabLayout   mTab;                // tab
    @BindView(R.id.ib_publish_post) ImageButton mIbTakePhoto;        // 拍照
    @BindView(R.id.vp_content)      ViewPager   mVpContent;          // 内容

    private FunctionConfig mFunctionConfig;
    private Tracker        mTracker; // GA Tracker

    private static final int REQUEST_CODE_GALLERY = 1001;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_center_social, container, false);
        ButterKnife.bind(this, v);
        initVariables();
        initViews();
        return v;
    }

    private void initVariables() {
        mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setMutiSelectMaxSize(9)
                .setEnablePreview(false)
                .build();

        // GA Tracker
        HaiApplication application = (HaiApplication) mActivity.getApplication();
        mTracker = application.getDefaultTracker();
    }

    protected void initViews() {
        mVpContent.setOffscreenPageLimit(0);
        mVpContent.setAdapter(new SocialPagerAdapter(getChildFragmentManager()));
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTracker.setScreenName(position == 0 ? "社区_精选" : "社区_关注");
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTab.setupWithViewPager(mVpContent);
    }

    /**
     * 图片选择回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
            Intent intent = new Intent(mActivity, PostEditActivity.class);
            intent.putParcelableArrayListExtra("img_path", resultList);
            mActivity.startActivity(intent);
            Logger.t(TAG).d(resultList.toString());
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };

    /**
     * 点击发帖
     */
    @OnClick(R.id.ib_publish_post)
    public void clickPublishPost() {
        if (HaiUtils.needLogin(mActivity))
            return;

        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_PostEdit, "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_SendPost + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_PostEdit, TraceUtils.PageCode_Social, TraceUtils.PositionCode_SendPost, "");

        String postDraft = (String) SPUtils.get(mActivity, "post_draft", "");
        Logger.d(postDraft);
        if (!TextUtils.isEmpty(postDraft)) {
            try {
                new AlertDialog.Builder(mActivity, R.style.Theme_AppCompat_Light_Dialog_Alert_Self)
                        .setMessage("是否打开上一次的草稿")
                        .setPositiveButton("恢复", (dialog, which) -> {
                            DraftPostBean draftPostBean = new Gson().fromJson(postDraft, DraftPostBean.class);
                            PostEditActivity.toThisActivity(mActivity, draftPostBean);
                        })
                        .setNegativeButton("新建", (dialog, which) -> {
                            SPUtils.remove(mActivity, "post_draft");
                            openGallery();
                        }).show();

            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                openGallery();
            }
        } else {
            openGallery();
        }
    }

    /**
     * 打开相册
     */
    private void openGallery() {
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, mFunctionConfig, mOnHanlderResultCallback);
    }

    /**
     * 点击添加好友
     */
    @OnClick(R.id.ib_add_friend)
    public void clickAddFriends() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_AddFriends, "", mActivity, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Friend + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_AddFriends, TraceUtils.PageCode_Social, TraceUtils.PositionCode_Friend, "");

        startActivity(new Intent(mActivity, AddFriendsActivity.class));
    }

    @Override
    public void onDestroyView() {
        SocialFragmentFactory.clearFragments();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(mVpContent.getCurrentItem() == 0 ? "社区_精选" : "社区_关注");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
