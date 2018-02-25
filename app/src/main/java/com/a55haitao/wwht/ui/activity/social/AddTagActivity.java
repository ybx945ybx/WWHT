package com.a55haitao.wwht.ui.activity.social;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 编辑晒物 - 添加标签页面
 */
public class AddTagActivity extends BaseNoFragmentActivity {
    //    @BindView(R.id.et_tag_name) EditText mEtTagName;                    // 搜索框
    @BindView(R.id.title)              DynamicHeaderView mTitle;            // 标题
    @BindView(R.id.tags_already_added) TagFlowLayout     mTagsAdded; // 已添加标签
    @BindView(R.id.tags_hot)           TagFlowLayout     mTagsHot;          // 热门标签
    @BindView(R.id.ll_search)          LinearLayout      mLlSearch;         // 热门标签
    @BindView(R.id.tv_no_tag)          TextView          mTvNoTag;          // 热门标签

    @BindString(R.string.key_tags)     String KEY_TAGS;
    @BindString(R.string.key_tag_name) String KEY_TAG_NAME;
    @BindString(R.string.key_is_hot)   String KEY_IS_HOT;
    @BindString(R.string.key_method)   String KEY_METHOD;

    private static final int REQUEST_SEARCH_TAG = 1;

    private ArrayList<String> mValsAdded = new ArrayList<>();
    private ArrayList<String> mValsHot   = new ArrayList<>();
    private Set<Integer>      mPos       = new HashSet<>();

    private static final int TAG_NOT_HOT = 0;
    private static final int TAG_HOT     = 1;

    private LayoutInflater     mInflater;
    private TagAdapter<String> mTagAddedAdapter;
    private TagAdapter<String> mTagHotAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
        loadData();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    /**
     * 初始化变量
     */
    public void initVariables() {
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<String> tags = intent.getStringArrayListExtra(KEY_TAGS);
            if (tags != null && tags.size() != 0) {
                Logger.t(TAG).d(tags.toString());
                setTagsAlreadyAdded(tags);
            }
        }
        mTvNoTag.setVisibility(mValsAdded.size() == 0 ? View.VISIBLE : View.GONE);
        mInflater = LayoutInflater.from(this);

    }

    /**
     * 初始化布局
     */
    public void initViews(Bundle savedInstanceState) {
        // 点击确定
        mTitle.setHeadClickListener(this::submit);

        // 跳转到搜索tag页面
        mLlSearch.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, SearchTagActivity.class);
            startActivityForResult(intent, REQUEST_SEARCH_TAG);
        });
        // 已添加标签 Adapter
        mTagAddedAdapter = new TagAdapter<String>(mValsAdded) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_cancel,
                        mTagsAdded, false);
                tv.setText(s.length() > 15 ? s.substring(0, 15) + "..." : s);
                return tv;
            }
        };
        // 热门标签 Adapter
        mTagHotAdapter = new TagAdapter<String>(mValsHot) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag,
                        mTagsHot, false);
                tv.setText(s.length() > 15 ? s.substring(0, 15) + "..." : s);
                return tv;
            }
        };
        mTagsAdded.setAdapter(mTagAddedAdapter);
        // 已添加标签点击事件
        mTagsAdded.setOnTagClickListener((view, position, parent) -> {
            mValsAdded.remove(position);
            mTagAddedAdapter.notifyDataChanged();

            setHotSelectedTag();

            mTvNoTag.setVisibility(mValsAdded.size() == 0 ? View.VISIBLE : View.GONE);
            return true;
        });
        // 热门标签点击事件
        mTagsHot.setOnTagClickListener((view, position, parent) -> {
            /*if (mValsAdded.size() >= 10) {
                // // TODO: 2017/3/28 bug
                ToastUtils.showToast(mActivity, "最多只能选择10个标签");
                //                return true;
            }*/

            if (mValsAdded.contains(mValsHot.get(position))) {
                mValsAdded.remove(mValsHot.get(position));
            } else if (mValsAdded.size() < 10) {
                mValsAdded.add(mValsHot.get(position));
            } else {
                setHotSelectedTag();
                ToastUtils.showToast(mActivity, "最多只能选择10个标签");
                return true;
            }

            mTvNoTag.setVisibility(mValsAdded.size() == 0 ? View.VISIBLE : View.GONE);
            mTagAddedAdapter.notifyDataChanged();
            return true;
        });
    }

    /**
     * 设置已经选择的Tags
     */
    private void setTagsAlreadyAdded(ArrayList<String> tags) {
        mValsAdded = tags;
    }


    /**
     * 获取数据
     */
    public void loadData() {
        SnsRepository.getInstance()
                .getPostTagList()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<List<TagBean>>() {
                    @Override
                    public void onSuccess(List<TagBean> tags) {
                        for (TagBean tag : tags) {
                            mValsHot.add(tag.name);
                        }
                        mTagsHot.setAdapter(mTagHotAdapter);
                        setHotSelectedTag();
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 设置选中的tag
     */
    private void setHotSelectedTag() {
        mPos.clear();
        for (String v : mValsAdded) {
            int i = mValsHot.indexOf(v);
            mPos.add(i);
        }
        mTagHotAdapter.setSelectedList(mPos);
    }

    /**
     * 确定
     */
    private void submit() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(KEY_TAGS, mValsAdded);
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SEARCH_TAG && resultCode == RESULT_OK) {
            String tagName = data.getStringExtra(KEY_TAG_NAME);
            if (!mValsAdded.contains(tagName)) {
                if (mValsAdded.size() >= 10) {
                    ToastUtils.showToast(mActivity, "最多只能选择10个标签");
                } else {
                    mValsAdded.add(tagName);
                    mTagAddedAdapter.notifyDataChanged();
                    setHotSelectedTag();
                    mTvNoTag.setVisibility(mValsAdded.size() == 0 ? View.VISIBLE : View.GONE);
                }
            }
        }
    }
}
