package com.a55haitao.wwht.ui.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.EasyOptFavourCountChangeEvent;
import com.a55haitao.wwht.data.event.EasyOptLikeCountChangeEvent;
import com.a55haitao.wwht.data.event.EasyoptListChangeEvent;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.CommCountsBean;
import com.a55haitao.wwht.data.model.entity.EasyOptBean;
import com.a55haitao.wwht.data.model.entity.ShareModel;
import com.a55haitao.wwht.data.model.entity.UserBean;
import com.a55haitao.wwht.data.model.result.EasyOptListResult;
import com.a55haitao.wwht.data.model.result.EasyoptLikeResult;
import com.a55haitao.wwht.data.model.result.EasyoptStarResult;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.easyopt.EasyoptEditActivity;
import com.a55haitao.wwht.ui.activity.myaccount.OthersHomePageActivity;
import com.a55haitao.wwht.ui.activity.social.CommentListActivity;
import com.a55haitao.wwht.ui.view.AvatarView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.SimpleIntroducePopupWindow;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DeviceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.FastBlurUtil;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * 草单详情 - HeaderLayout
 * Created by drolmen on 2017/1/5.
 */

public class EasyOptHeaderLayout extends RelativeLayout {

    @BindView(R.id.nameTxt)         HaiTextView     mNameTxt;
    @BindView(R.id.headerImgView)   AvatarView      mHeaderImgView;
    @BindView(R.id.nickNameTxt)     TextView        mNickNameTxt;
    @BindView(R.id.starCountTxt)    TextView        mStarCountTxt;
    @BindView(R.id.likeCountTxt)    TextView        mLikeCountTxt;
    @BindView(R.id.editTxt)         TextView        mEditImagTxt;
    @BindView(R.id.commentCountTxt) TextView        mCommentCountTxt;
    @BindView(R.id.coverImg)        ImageView       mCoverImg;
    @BindView(R.id.descTxt)         HaiTextView     mDescTxt;
    @BindView(R.id.likeStatusTxt)   CheckedTextView mLikeStatusTxt;
    @BindView(R.id.starStatusTxt)   CheckedTextView mStarStatusTxt;
    @BindView(R.id.countTxt)        TextView        mCountTxt;
    @BindView(R.id.starLayout)      LinearLayout    mStarLayout;
    @BindView(R.id.infoLayout)      LinearLayout    mInfoLayout;

    @BindView(R.id.groundImgView)     ImageView mGroundImg;
    @BindView(R.id.backgroundImgView) ImageView mBgGroundImg;           // 遮罩

    private int owerId;         //创建者id
    private int mEasyoptId;     //草单id

    private SimpleIntroducePopupWindow mIntroducePW;

    private ShareModel  mShareModel;
    private EasyOptBean mEasyOptBean;

    public EasyOptHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIntroducePW = new SimpleIntroducePopupWindow(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @OnClick({R.id.starLayout, R.id.commentLayout, R.id.likeLayout, R.id.coverImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.starLayout: // 收藏草单
                actionStar();
                break;
            case R.id.commentLayout: // 评论草单
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_EasyComments, "", getContext(), TraceUtils.PageCode_EasyDetail, TraceUtils.PositionCode_ViewComments + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_EasyComments, TraceUtils.PageCode_EasyDetail, TraceUtils.PositionCode_ViewComments, "");

                CommentListActivity.toThisActivityEasyopt(getContext(), mEasyOptBean.easyopt_id, mEasyOptBean.img_cover, mEasyOptBean.name, mEasyOptBean.owner.getNickname(), mEasyOptBean.owner.getId() == HaiUtils.getUserId());
                break;
            case R.id.likeLayout: // 点赞草单
                actionLike();
                break;
        }
    }


    @OnClick({R.id.jumpImg, R.id.nickNameTxt, R.id.headerImgView, R.id.coverImg, R.id.editTxt})
    public void inClickHandle(View view) {
        switch (view.getId()) {
            case R.id.jumpImg:
            case R.id.nickNameTxt:
            case R.id.headerImgView:
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_HomePage, owerId + "", getContext(), TraceUtils.PageCode_EasyDetail, TraceUtils.PositionCode_User + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_HomePage, TraceUtils.PageCode_EasyDetail, TraceUtils.PositionCode_User, owerId + "");

                OthersHomePageActivity.actionStart(getContext(), owerId);
                break;
            case R.id.coverImg:
                View viewById = ActivityCollector.getTopActivity().findViewById(R.id.content_view);
                viewById.setDrawingCacheEnabled(true);
                viewById.buildDrawingCache(false);
                Bitmap drawingCache = viewById.getDrawingCache(false);

                int scaleRatio = 8;
                Bitmap scaledBit = Bitmap.createScaledBitmap(drawingCache,
                        drawingCache.getWidth() / scaleRatio,
                        drawingCache.getHeight() / scaleRatio,
                        false);

                Bitmap bitmap = FastBlurUtil.blurBitmap(scaledBit, getContext());

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, DeviceUtils.getScreenWidth() / 100, DeviceUtils.getScreenHeight() / 100, false);
                viewById.destroyDrawingCache();
                mIntroducePW.setBlurBackground(scaledBitmap);
                mIntroducePW.showAtLocation(mCoverImg, Gravity.CENTER, 0, 0);
                break;
            case R.id.editTxt:
                // 埋点
//                TraceUtils.addClick(TraceUtils.PageCode_EasyCreat, "", getContext(), TraceUtils.PageCode_EasyDetail, TraceUtils.PositionCode_AlbumCreate + "", "");

                //                TraceUtils.addAnalysAct(TraceUtils.PageCode_EasyCreat, TraceUtils.PageCode_EasyDetail, TraceUtils.PositionCode_AlbumCreate, "");

                EasyoptEditActivity.toThisActivity(getContext(), mEasyOptBean.easyopt_id, mEasyOptBean.img_cover, mEasyOptBean.name, mEasyOptBean.content, mEasyOptBean.is_visible);
                break;
        }
    }

    public void switchEditStatus(boolean editActived, int count) {
        mEditImagTxt.setVisibility(editActived ? VISIBLE : GONE);

        if (editActived) {
            mCountTxt.setText(String.format("共%s商品(长按商品可调整顺序)", count));
        } else {
            mCountTxt.setText(String.format("共%s商品", count));
        }
    }

    /**
     * 点赞草单
     */
    private void actionLike() {
        if (HaiUtils.needLogin(getContext())) {
            return;
        }

        SnsRepository.getInstance()
                .easyoptPoint(mEasyoptId)
                .compose(ActivityCollector.getTopActivity().bindToLifecycle())
                .subscribe(new DefaultSubscriber<EasyoptLikeResult>() {
                    @Override
                    public void onSuccess(EasyoptLikeResult result) {
                        mLikeCountTxt.setText(String.format("赞(%s)", result.point_count));

                        mLikeStatusTxt.toggle();

                        if (mLikeStatusTxt.isChecked()) {         //成功
                            ToastUtils.showToast("点赞成功");
                        }
                        mEasyOptBean.pointit = result.ispoint;
                        mEasyOptBean.point_count = result.point_count;

                        EventBus.getDefault().post(new EasyOptFavourCountChangeEvent(result.point_count, mEasyoptId));
                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }

    /**
     * 收藏草单
     */
    private void actionStar() {
        if (HaiUtils.needLogin(getContext())) {
            return;
        }

        SnsRepository.getInstance()
                .easyoptLike(mEasyoptId)
                .compose(ActivityCollector.getTopActivity().bindToLifecycle())
                .subscribe(new DefaultSubscriber<EasyoptStarResult>() {
                    @Override
                    public void onSuccess(EasyoptStarResult result) {
                        mStarCountTxt.setText(String.format("收藏(%s)", result.like_count));

                        mStarStatusTxt.toggle();

                        if (mStarStatusTxt.isChecked()) {         //收藏成功
                            ToastUtils.showToast("收藏成功");
                        }
                        mEasyOptBean.likeit = result.islike;
                        mEasyOptBean.like_count = result.like_count;

                        EventBus.getDefault().post(new EasyoptListChangeEvent());
                        //                        EventBus.getDefault().post(new EasyoptLikeEvent());
                        EventBus.getDefault().post(mEasyOptBean);
                        EventBus.getDefault().post(new EasyOptLikeCountChangeEvent(result.like_count, mEasyoptId, result.islike));

                        Realm.getDefaultInstance()
                                .executeTransactionAsync(realm -> {
                                    UserBean       user       = realm.where(UserBean.class).findFirst();
                                    CommCountsBean commCounts = user.getCommCounts();
                                    if (commCounts != null) {
                                        commCounts.setEasyopt_start_count(result.user_like_count);
                                    }
                                });
                    }

                    @Override
                    public void onFinish() {

                    }
                });

    }

    public void canclePopuWindow() {
        if (mIntroducePW.isShowing()) {
            mIntroducePW.dismiss();
        }
    }

    public void setData(EasyOptListResult result, EasyOptBean bean, ShareModel shareModel, Bitmap blurBitmap) {
        mEasyOptBean = bean;
        // 草单名
        mNameTxt.setText(bean.name);
        // 封面图片
        UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(), bean.img_cover, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_small, mCoverImg, 4);
        // 草单描述
        mDescTxt.setText(TextUtils.isEmpty(bean.content) ? "该草单还木有编写描述哦~" : bean.content);
        // 发布者信息
        /*UPaiYunLoadManager.load(ActivityCollector.getTopActivity(), bean.owner.getHeadImg(), UpaiPictureLevel.FOURTH,
                R.id.u_pai_yun_null_holder_tag, mHeaderImgView, true);*/
        // 头像
        String cornerUrl = null;
        if (bean.owner.getUserTitleList().size() != 0) {
            cornerUrl = bean.owner.getUserTitleList().get(0).getIconUrl();
        }
        mHeaderImgView.loadImg(bean.owner.getHeadImg(), cornerUrl);
        // 昵称
        mNickNameTxt.setText(bean.owner.getNickname());
        //info
        mLikeStatusTxt.setChecked(bean.pointit == 1);
        mStarStatusTxt.setChecked(bean.likeit == 1);
        mLikeCountTxt.setText(String.format("赞(%d)", bean.point_count));
        mStarCountTxt.setText(String.format("收藏(%d)", bean.like_count));
        mCommentCountTxt.setText(String.format("评论(%d)", bean.reply_count));

        //商品数量
        mCountTxt.setText(String.format("共%s商品", HaiUtils.getSize(bean.products)));

        owerId = bean.owner.getId();
        mEasyoptId = bean.easyopt_id;

        mShareModel = shareModel;

        mIntroducePW.setData(bean);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getPartHeight();

                ViewGroup.LayoutParams lp1 = mBgGroundImg.getLayoutParams();
                lp1.height = height;
                mBgGroundImg.setLayoutParams(lp1);

                ViewGroup.LayoutParams lp2 = mGroundImg.getLayoutParams();
                lp2.height = height;
                mGroundImg.setLayoutParams(lp2);

                mGroundImg.setImageBitmap(blurBitmap);

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


    }

    public int getPartHeight() {
        return mInfoLayout.getTop();
    }

    public int getMyHeight() {
        return mInfoLayout.getTop() + DisplayUtils.dp2px(getContext(), 82);
    }

    public void changeCommentCount(int count) {
        mEasyOptBean.reply_count = count;
        mCommentCountTxt.setText(String.format("评论(%s)", count));
    }

}
