package com.a55haitao.wwht.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.PostImgListAdapter;
import com.a55haitao.wwht.data.constant.ServerUrl;
import com.a55haitao.wwht.data.constant.StringConstans;
import com.a55haitao.wwht.data.event.PostChangeEvent;
import com.a55haitao.wwht.data.event.PostDetailChangeEvent;
import com.a55haitao.wwht.data.model.entity.DraftPostBean;
import com.a55haitao.wwht.data.model.entity.ImagesBean;
import com.a55haitao.wwht.data.model.entity.PublishPostBean;
import com.a55haitao.wwht.data.model.result.EditPostResult;
import com.a55haitao.wwht.data.model.result.PublishPostResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DividerItemDecoration;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.SPUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.baoyz.actionsheet.ActionSheet;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadManager;
import com.upyun.library.listener.SignatureListener;
import com.upyun.library.utils.UpYunUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PhotoEditActivity;
import cn.finalteam.galleryfinal.model.BasePhotoInfo;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 社区 - 发布晒物 - 编辑晒物
 */
public class PostEditActivity extends BaseNoFragmentActivity {
    //    @BindView(R.id.ib_cancel)         ImageView            mIbTitleBack;        // 返回
    @BindView(R.id.tv_cancel)         TextView             mTvCancel;           // 取消
    @BindView(R.id.tv_publish_post)   TextView             mTvPublishPost;      // 发布晒物
    @BindView(R.id.et_content)        EditText             mEtContent;          // 晒物内容
    @BindView(R.id.ll_add_tag)        LinearLayout         mLlAddTag;           // 添加标签
    @BindView(R.id.tv_add_tags)       TextView             mTvAddTags;          // 添加tag
    @BindView(R.id.ll_tags_container) LinearLayout         mLlTagsContainer;    // tag容器
    @BindView(R.id.sv_tags_container) HorizontalScrollView mSvTagsContainer;    // tag容器
    @BindView(R.id.rv_img)            RecyclerView         mRvImg;              // 图片列表
    @BindView(R.id.et_title)          EditText             mEtTitle;

    @BindString(R.string.key_country)                          String KEY_COUNTRY;
    @BindString(R.string.key_tags)                             String KEY_TAGS;
    @BindString(R.string.key_img_path)                         String KEY_IMG_PATH;
    @BindString(R.string.key_tag_name)                         String KEY_TAG_NAME;
    @BindString(R.string.toast_publish_post_content_not_empty) String TOAST_PUBLISH_POST_CONTENT_NOT_EMPTY;
    @BindString(R.string.toast_publish_post_content_too_short) String TOAST_PUBLISH_POST_CONTENT_TOO_SHORT;
    @BindString(R.string.toast_publish_post_img_not_empty)     String TOAST_PUBLISH_POST_IMG_NOT_EMPTY;
    @BindString(R.string.key_post_id)                          String KEY_POST_ID;
    @BindString(R.string.key_is_my_post)                       String KEY_IS_MY_POST;
    @BindString(R.string.key_img_url)                          String KEY_IMG_URL;
    @BindString(R.string.toast_publish_post_tag_not_empty)     String TOAST_PUBLISH_POST_TAG_NOT_EMPTY;
    @BindString(R.string.toast_publish_post_country_not_empty) String TOAST_PUBLISH_POST_COUNTRY_NOT_EMPTY;

    @BindDimen(R.dimen.margin_large)   int      DIVIDER_WIDTH;
    @BindDrawable(R.drawable.bg_white) Drawable DIVIDER_BG;

    private static final int    REQUEST_PHOTO        = 0;
    private static final int    REQUEST_TAG          = 1;
    private static final int    REQUEST_COUNTRY      = 2;
    private static final int    REQUEST_CODE_GALLERY = 1001;
    private static final String KEY                  = "MJrxn7x0fP5c54QFfb0GlFiJ9pY=";
    private static final String SPACE                = "st-prod";
    private static final String savePath             = "/post/{year}/{mon}/{day}/{random32}";

    private FunctionConfig           mFunctionConfig;
    private ArrayList<PhotoInfo>     mLocalImgPaths;
    private ArrayList<ImagesBean>    mUpYunImgs;
    private PostImgListAdapter       mAdapter;
    private View                     mImgAdd;
    private Tracker                  mTracker; // GA Tracker
    private int                      mPostId;
    private PublishPostBean          mPublishPostData;
    private ArrayList<BasePhotoInfo> mBasePhotoInfos;
    private boolean                  mIsEditPost; // 编辑帖子
    private ArrayList<Integer>       mIndexList;
    private ActionSheet              mAsSavePost; // 保存草稿
    private DraftPostBean            mDraftPost;
    private String                   mTagName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id=" + mPostId;
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            // 笔记草稿
            mDraftPost = intent.getParcelableExtra("draft_post");
            // 创建笔记
            mLocalImgPaths = intent.getParcelableArrayListExtra("img_path");
            // 编辑笔记 所需字段
            mPostId = intent.getIntExtra("post_id", 0);
            mPublishPostData = intent.getParcelableExtra("post_bean");

            mTagName = intent.getStringExtra("tag_name");
        }

        mUpYunImgs = new ArrayList<>(9);
        mBasePhotoInfos = new ArrayList<>(9);
        mIndexList = new ArrayList<>();
        // 笔记数据
        if (mPublishPostData == null) {
            mPublishPostData = new PublishPostBean();
        }
        // 编辑帖子/发布帖子
        if (mPostId != 0) {
            mIsEditPost = true;
        }
        // 有笔记草稿
        if (mDraftPost != null) {
            //            Logger.d(mDraftPost.toString());
            // 本地图片
            mLocalImgPaths = mDraftPost.localImagePaths;
            // 标题
            mPublishPostData.one_word = mDraftPost.one_word;
            // 文本
            mPublishPostData.content = mDraftPost.content;
            // 标签
            mPublishPostData.tags = mDraftPost.tags;
        }
        // 笔记标签
        if (mPublishPostData.tags == null) {
            mPublishPostData.tags = new ArrayList<>();
            if (!TextUtils.isEmpty(mTagName)){
                mPublishPostData.tags.add(mTagName);
            }
        }
        // 编辑笔记图片（网络图）
        if (mPublishPostData.images == null) {
            mPublishPostData.images = new ArrayList<>();
        } else {
            mBasePhotoInfos.addAll(mPublishPostData.images);
        }
        // 本地图片
        if (mLocalImgPaths == null) {
            mLocalImgPaths = new ArrayList<>();
        } else if (mLocalImgPaths.size() != 0) {
            mBasePhotoInfos.addAll(mLocalImgPaths);
        }

        // galleryfinal配置
        /*mFunctionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setSelected(mLocalImgPaths)
                .setMutiSelectMaxSize(9)
                .setEnablePreview(false)
                .build();*/
        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName(mIsEditPost ? "笔记_编辑笔记" : "笔记_创建笔记");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    /**
     * 恢复草稿
     *
     * @param postData 晒物Data的json字符串
     */
    private void restorePostData(String postData) {
        /*PublishPostDataBean data = new Gson().fromJson(postData, PublishPostDataBean.class);

        Logger.t(TAG).d(data.toString());

        mEtContent.setText(data.content); // 晒物内容
        mEtContent.setSelection(mEtContent.getText().toString().length()); // 设置光标位置

        // 晒物图片
        //        mLocalImgPath = data.localImagePaths.get(0);
        normalizeLoadFile(data.localImagePaths.get(0));
        GlideUtils.loadImg(mActivity, data.localImagePaths.get(0), mImgPic, R.mipmap.ic_default_square_small);

        // 晒物标签
        mTags = data.tags;
        setTagList(data.tags);

        // 购买地
        mCountryName = data.location.location_desc;
        mCountryCode = data.location.region_id;
        if (!TextUtils.isEmpty(mCountryName))
            mTvProductCountry.setText(data.location.location_desc); // 购买地*/
    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        // 标题
        mEtTitle.setText(mPublishPostData.one_word);
        // 内容
        mEtContent.setText(mPublishPostData.content);
        // 标签
        if (mPublishPostData.tags != null) {
            setTagList(mPublishPostData.tags);
        }
        mEtTitle.requestFocus();
        // 分割线
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.HORIZONTAL_LIST, DIVIDER_BG);
        itemDecoration.setWidth(DIVIDER_WIDTH);
        mRvImg.addItemDecoration(itemDecoration);
        // layoutManager
        mRvImg.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new PostImgListAdapter(mBasePhotoInfos);

        mImgAdd = getLayoutInflater().inflate(R.layout.layout_activity_editpost_add_img, null);
        //        setImgFull(mLocalImgPaths.size() == 9);
        setImgFull(mBasePhotoInfos.size() == 9);

        // 增加照片
        mImgAdd.setOnClickListener(view -> {
            mFunctionConfig = new FunctionConfig.Builder()
                    .setEnableCamera(true)
                    .setEnableEdit(true)
                    .setEnableCrop(true)
                    .setSelected(mLocalImgPaths)
                    .setMutiSelectMaxSize(9 - (mBasePhotoInfos.size() - mLocalImgPaths.size()))
                    .setEnablePreview(false)
                    .build();
            Logger.d(mLocalImgPaths);
            GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, mFunctionConfig, mOnHanlderResultCallback);
        });
        mAdapter.addFooterView(mImgAdd, -1, LinearLayout.HORIZONTAL);

        mRvImg.setAdapter(mAdapter);
        mRvImg.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mBasePhotoInfos.get(position) instanceof PhotoInfo) {
                    ActionSheet.createBuilder(mActivity, getSupportFragmentManager())
                            .setCancelButtonTitle("取消")
                            .setOtherButtonTitles("设为封面", "编辑图片", "删除图片")
                            .setCancelableOnTouchOutside(true)
                            .setListener(new ActionSheet.ActionSheetListener() {
                                @Override
                                public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                                }

                                @Override
                                public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                    switch (index) {
                                        case 0: // 设为封面
                                            Collections.swap(mBasePhotoInfos, position, 0);
                                            if (!mIsEditPost) {
                                                Collections.swap(mLocalImgPaths, position, 0);
                                            }
                                            mAdapter.notifyItemMoved(position, 0);
                                            break;
                                        case 1: // 编辑图片
                                            Intent intent = new Intent(mActivity, PhotoEditActivity.class);
                                            intent.putExtra(PhotoEditActivity.SELECT_MAP, mLocalImgPaths);

                                            int selectedIndex = mLocalImgPaths.indexOf(mBasePhotoInfos.get(position));
                                            intent.putExtra(PhotoEditActivity.SELECTED_INDEX, selectedIndex);
                                            startActivity(intent);
                                            break;
                                        case 2: // 删除图片
                                            BasePhotoInfo basePhoto = mBasePhotoInfos.get(position);
                                            if (basePhoto instanceof PhotoInfo) {
                                                mLocalImgPaths.remove(basePhoto);
                                            }
                                            mBasePhotoInfos.remove(basePhoto);
                                            mAdapter.notifyItemRemoved(position);
                                            setImgFull(false);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }).show();
                } else {
                    ActionSheet.createBuilder(mActivity, getSupportFragmentManager())
                            .setCancelButtonTitle("取消")
                            .setOtherButtonTitles("设为封面", "删除图片")
                            .setCancelableOnTouchOutside(true)
                            .setListener(new ActionSheet.ActionSheetListener() {
                                @Override
                                public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                                }

                                @Override
                                public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                    switch (index) {
                                        case 0: // 设为封面
                                            Collections.swap(mBasePhotoInfos, position, 0);
                                            if (!mIsEditPost) {
                                                Collections.swap(mLocalImgPaths, position, 0);
                                            }
                                            mAdapter.notifyItemMoved(position, 0);
                                            break;
                                        case 1: // 删除图片
                                            BasePhotoInfo basePhoto = mBasePhotoInfos.get(position);
                                            if (basePhoto instanceof PhotoInfo) {
                                                mLocalImgPaths.remove(basePhoto);
                                            }
                                            mBasePhotoInfos.remove(basePhoto);
                                            mAdapter.notifyItemRemoved(position);
                                            setImgFull(false);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }).show();
                }
            }
        });
    }

    /**
     * 填充tagList
     */
    private void setTagList(List<String> tags) {
        mLlTagsContainer.removeAllViews();
        if (tags == null || tags.size() == 0) {
            mSvTagsContainer.setVisibility(View.GONE);
            mTvAddTags.setVisibility(View.VISIBLE);
            return;
        }

        for (String tag : tags) {
            TextView tvTag = (TextView) getLayoutInflater().inflate(R.layout.tag_normal, null);
            tvTag.setText(tag.length() > 15 ? tag.substring(0, 15) + "..." : tag);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = DisplayUtils.dp2px(mActivity, 8);
            tvTag.setLayoutParams(layoutParams);
            mLlTagsContainer.addView(tvTag);

            mSvTagsContainer.setVisibility(View.VISIBLE);
            mTvAddTags.setVisibility(View.GONE);
        }
    }

    /**
     * 图片选择回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, ArrayList<PhotoInfo> resultList) {
            Logger.t(TAG).d(resultList.toString());

            mLocalImgPaths.clear();
            mLocalImgPaths.addAll(resultList);

            Logger.d(mLocalImgPaths);

            mBasePhotoInfos.removeAll(resultList);

            Iterator<BasePhotoInfo> iter = mBasePhotoInfos.iterator();
            while (iter.hasNext()) {
                BasePhotoInfo basePhotoInfo = iter.next();
                if (basePhotoInfo instanceof PhotoInfo) {
                    iter.remove();
                }
            }

            mBasePhotoInfos.addAll(resultList);
            mAdapter.notifyDataSetChanged();
            // 隐藏图片添加
            //            setImgFull(mLocalImgPaths.size() == 9);
            setImgFull(mBasePhotoInfos.size() == 9);
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtils.showToast(mActivity, errorMsg);
        }
    };

    SignatureListener signatureListener = raw -> UpYunUtils.md5(raw + KEY);


    /**
     * 发布
     * 创建笔记 && 编辑笔记
     */
    @OnClick(R.id.tv_publish_post)
    public void publishPost() {
        if (mBasePhotoInfos.size() == 0) {
            ToastUtils.showToast(mActivity, "请选择照片");
        } else if (mLocalImgPaths.size() == 0) {
            requestEditPost();
        } else {
            if (mIsEditPost) {
                compressEdit();
            } else {
                compress(mLocalImgPaths);
            }
        }
    }

    /**
     * 编辑笔记 压缩操作
     */
    private void compressEdit() {
        ArrayList<PhotoInfo> photoList = new ArrayList<>();
        for (int i = 0; i < mBasePhotoInfos.size(); i++) {
            if (mBasePhotoInfos.get(i) instanceof PhotoInfo) {
                photoList.add((PhotoInfo) mBasePhotoInfos.get(i));
                mIndexList.add(i);
            }
        }
        compress(photoList);
    }

    /**
     * 压缩操作
     */
    private void compress(ArrayList<PhotoInfo> images) {
        Observable.from(images)
                .map(photoInfo -> TextUtils.isEmpty(photoInfo.getCropPhotoPath()) ? photoInfo.getPhotoPath() : photoInfo.getCropPhotoPath())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<File>>() {
                    @Override
                    public Observable<File> call(String fileName) {
                        File file        = new File(fileName);
                        long fileContent = file.length();
                        if (fileContent > 200 * 1024) {
                            return Luban.get(PostEditActivity.this)
                                    .load(file)
                                    .putGear(Luban.THIRD_GEAR)
                                    .asObservable();
                        } else {
                            return Observable.just(file);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    ArrayList<PhotoInfo> datas = new ArrayList<PhotoInfo>();

                    @Override
                    public void onStart() {
                        showProgressDialog(StringConstans.TOAST_DEAL_FILE, false);
                    }

                    @Override
                    public void onCompleted() {
                        dismissProgressDialog();
                        requestUpyunUpload(datas);
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onNext(File s) {
                        PhotoInfo photoInfo = new PhotoInfo();
                        photoInfo.setPhotoPath(s.getAbsolutePath());
                        datas.add(photoInfo);
                    }
                });
    }

    /**
     * 又拍云上传图片
     *
     * @param mLocalImgPaths 本地图片
     */
    private void requestUpyunUpload(ArrayList<PhotoInfo> mLocalImgPaths) {
        showProgressDialog("发布中", true);
        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, SPACE);
        paramsMap.put(Params.SAVE_KEY, savePath);

        final int[] count = {0};

        mUpYunImgs.clear();

        ImagesBean imgUrls[] = new ImagesBean[mLocalImgPaths.size()];
        for (int i = 0, len = mLocalImgPaths.size(); i < len; i++) {
            File file   = new File(mLocalImgPaths.get(i).getPhotoPath());
            int  finalI = i;
            UploadManager.getInstance()
                    .formUpload(file, paramsMap, signatureListener,
                            (isSuccess, result) -> {
                                Logger.t(TAG).d(result);
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.optInt("code") == 200) { // 成功
                                        ImagesBean imagesBean = new ImagesBean(ServerUrl.UPYUN_BASE + jsonObject.optString("url"),
                                                jsonObject.optInt("image-width") * 1.0f / jsonObject.optInt("image-height"));
                                        imgUrls[finalI] = imagesBean;
                                        //查询
                                        count[0]++;
                                        if (count[0] == imgUrls.length) {
                                            mUpYunImgs.addAll(Arrays.asList(imgUrls));

                                            if (mIsEditPost) {
                                                requestEditPost(); // 编辑帖子
                                            } else {
                                                requestPublishPost(); // 发布帖子
                                            }

                                        }
                                    } else {
                                        dismissProgressDialog();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    dismissProgressDialog();
                                    ToastUtils.showToast("网络请求失败，请检查您的网络设置");
                                }
                            },
                            (bytesWrite, contentLength) -> {
                            });
        }
    }

    /**
     * 发布帖子请求
     */
    private void requestPublishPost() {
        mPublishPostData.one_word = mEtTitle.getText().toString();
        mPublishPostData.content = mEtContent.getText().toString();
        mPublishPostData.images = mUpYunImgs;

        SnsRepository.getInstance()
                .publishPost(new Gson().toJson(mPublishPostData))
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<PublishPostResult>() {
                    @Override
                    public void onSuccess(PublishPostResult result) {
                        if (result.membership_point <= 0) {
                            ToastUtils.showToast(mActivity, "发布成功");
                        }
                        EventBus.getDefault().post(new PostChangeEvent());
                        // 清除笔记草稿
                        SPUtils.remove(mActivity, "post_draft");

                        Intent intent = new Intent(mActivity, PostDetailActivity.class);
                        intent.putExtra(KEY_POST_ID, result.post_id);
                        intent.putExtra("membership_point", result.membership_point);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /**
     * 编辑帖子请求
     */
    private void requestEditPost() {
        showProgressDialog("发布中", true);
        mPublishPostData.one_word = mEtTitle.getText().toString();
        mPublishPostData.content = mEtContent.getText().toString();

        for (int i = 0; i < mUpYunImgs.size(); i++) {
            int index = mIndexList.get(i);
            mBasePhotoInfos.remove(index);
            mBasePhotoInfos.add(index, mUpYunImgs.get(i));
        }

        ArrayList<ImagesBean> upImages = new ArrayList<>(mBasePhotoInfos.size());

        for (BasePhotoInfo basePhotoInfo : mBasePhotoInfos) {
            upImages.add((ImagesBean) basePhotoInfo);
        }
        mPublishPostData.images = upImages;
        mPublishPostData.location = new PublishPostBean.LocationBean();

        SnsRepository.getInstance()
                .editPost(mPostId, new Gson().toJson(mPublishPostData))
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<EditPostResult>() {
                    @Override
                    public void onSuccess(EditPostResult result) {
                        ToastUtils.showToast(mActivity, "修改成功");
                        EventBus.getDefault().post(new PostDetailChangeEvent());
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });

    }

    /**
     * 显示隐藏添加图片
     */
    public void setImgFull(boolean flag) {
        mImgAdd.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    /**
     * 跳转到其他Activity
     */
    /*@OnClick(R.id.ll_add_tag)
    public void jumpActivity(View view) {
        Intent intent = null;
        intent = new Intent(this, AddTagActivity.class);
        intent.putStringArrayListExtra(KEY_TAGS, mPublishPostData.tags);
        startActivityForResult(intent, REQUEST_TAG);
    }*/
    @OnClick({R.id.ll_tags_container, R.id.tv_add_tags, R.id.img_add_tag})
    public void jumpActivity() {
        // 埋点
//        TraceUtils.addClick(TraceUtils.PageCode_PostEditAddTags, "", this, TraceUtils.PageCode_PostEdit, TraceUtils.PositionCode_AddUserTag + "", "");

        //        TraceUtils.addAnalysAct(TraceUtils.PageCode_PostEditAddTags, TraceUtils.PageCode_PostEdit, TraceUtils.PositionCode_AddUserTag, "");

        Intent intent = null;
        intent = new Intent(this, AddTagActivity.class);
        intent.putStringArrayListExtra(KEY_TAGS, mPublishPostData.tags);
        startActivityForResult(intent, REQUEST_TAG);
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAG:       // 标签
                    mPublishPostData.tags = data.getStringArrayListExtra(KEY_TAGS);
                    //                    ToastUtils.showToast(mActivity, mTags.toString());
                    setTagList(mPublishPostData.tags);
                    break;
            }
        }
    }

    /**
     * 返回
     */
    @OnClick(R.id.tv_cancel)
    public void clickCancel() {
        savePostConfirm();
    }

    @Override
    public void onBackPressed() {
        savePostConfirm();
    }

    /**
     * 保存草稿确认
     */
    private void savePostConfirm() {
        // 编辑帖子不保存草稿
        if (mIsEditPost) {
            finish();
            return;
        }
        if (!TextUtils.isEmpty(mEtContent.getText().toString())
                || !TextUtils.isEmpty(mEtTitle.getText().toString())
                || mPublishPostData.tags != null && mPublishPostData.tags.size() != 0
                || mBasePhotoInfos != null && mBasePhotoInfos.size() != 0) {
            if (mAsSavePost == null || mAsSavePost.isDismissed()) {
                mAsSavePost = ActionSheet.createBuilder(mActivity, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("保存草稿", "不保存")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                switch (index) {
                                    case 0: // 保存草稿
                                        savePostData();
                                        break;
                                    case 1: // 不保存
                                        SPUtils.remove(mActivity, "post_draft");
                                        finish();
                                        break;

                                }
                            }


                        }).show();
            }
        } else {
            finish();
        }
    }

    /**
     * 保存草稿数据
     */
    private void savePostData() {
        DraftPostBean draftPostTmp = new DraftPostBean();
        draftPostTmp.content = mEtContent.getText().toString();
        draftPostTmp.one_word = mEtTitle.getText().toString();
        draftPostTmp.localImagePaths = mLocalImgPaths;
        draftPostTmp.tags = mPublishPostData.tags;
        String strDraft = new Gson().toJson(draftPostTmp, DraftPostBean.class);
        Logger.d(strDraft);
        SPUtils.put(mActivity, "post_draft", strDraft);
        finish();
    }

    /**
     * 跳转到本页面
     *
     * @param context  context
     * @param postId   帖子Id
     * @param postBean 帖子内容
     */
    public static void toThisActivity(Context context, int postId, PublishPostBean postBean) {
        Intent intent = new Intent(context, PostEditActivity.class);
        intent.putExtra("post_id", postId);
        intent.putExtra("post_bean", postBean);
        context.startActivity(intent);
    }

    /**
     * 跳转到本页面(包含帖子草稿)
     *
     * @param context       context
     * @param draftPostBean 帖子草稿
     */
    public static void toThisActivity(Context context, DraftPostBean draftPostBean) {
        Intent intent = new Intent(context, PostEditActivity.class);
        intent.putExtra("draft_post", draftPostBean);
        context.startActivity(intent);
    }
}
