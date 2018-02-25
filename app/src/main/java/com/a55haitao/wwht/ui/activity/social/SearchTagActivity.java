package com.a55haitao.wwht.ui.activity.social;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.social.TagListAdapter;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.model.entity.TagBean;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.DividerItemDecoration;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.NameLengthFilter;
import com.a55haitao.wwht.utils.TxtUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索标签
 */
public class SearchTagActivity extends BaseNoFragmentActivity {

    @BindView(R.id.title)      DynamicHeaderView mTitle;    // 标题
    @BindView(R.id.et_search)  EditText          mEtSearch; // 搜索
    @BindView(R.id.rv_content) RecyclerView      mRvContent;// 内容

    @BindString(R.string.key_method)     String KEY_METHOD;
    @BindString(R.string.key_count)      String KEY_COUNT;
    @BindString(R.string.key_page)       String KEY_PAGE;
    @BindString(R.string.key_name)       String KEY_NAME;
    @BindString(R.string.key_is_hot)     String KEY_IS_HOT;
    @BindString(R.string.key_user_token) String KEY_USER_TOKEN;
    @BindString(R.string.key_tag_name)   String KEY_TAG_NAME;

    private InputMethodManager mImm;
    private String             mName;
    private int mCurrentPage = 1;
    private TextView       mTvTagName;
    //    private NetGetPostTagList mData;
    private View           mHeaderView;
    private List<TagBean>  mTagListData;
    private TagListAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);
        ButterKnife.bind(this);
        initVariables();
        initViews(savedInstanceState);
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    public void initVariables() {
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    public void initViews(Bundle savedInstanceState) {
        mEtSearch.setHint(R.string.hint_search_tag);
        mEtSearch.requestFocus();
        mEtSearch.setFilters(new InputFilter[]{TxtUtils.EMOJI_FILTER, new NameLengthFilter(mActivity, 20)});

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
                requestSearchTag();
            }
        });

        // 输入法回车键触发搜索
        mEtSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                mName = mEtSearch.getText().toString().trim();
                requestSearchTag();
            }
            return false;
        });

        // 触摸ListView时隐藏输入法
        mRvContent.setOnTouchListener((v, event) -> {
            mImm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
            return false;
        });

        // 添加新标签HeaderView
        mHeaderView = getLayoutInflater().inflate(R.layout.header_activity_search_tag, null);
        mTvTagName = (TextView) mHeaderView.findViewById(R.id.tv_tag_name);
        mHeaderView.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("tag_name", mTvTagName.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        });

        mRvContent.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRvContent.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new TagListAdapter(null);
        mRvContent.setAdapter(mAdapter);

        mRvContent.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mTagListData != null) {
                    Intent intent = new Intent();
                    intent.putExtra("tag_name", mTagListData.get(position).name);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 搜索tag请求
     */
    private void requestSearchTag() {
        SnsRepository.getInstance()
                .getPostTagList(mName)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<List<TagBean>>() {
                    @Override
                    public void onSuccess(List<TagBean> tags) {
                        mTagListData = tags;
                        setTagListView(mTagListData);
                    }

                    @Override
                    public void onFinish() {

                    }
                });


    }

    private void setTagListView(List<TagBean> data) {
        ArrayList<String> tagNames = new ArrayList<>();
        for (TagBean d : data) {
            tagNames.add(d.name);
        }

        if (!tagNames.contains(mEtSearch.getText().toString())) {
            mAdapter.removeAllHeaderView();
            mTvTagName.setText(mEtSearch.getText().toString());
            mAdapter.addHeaderView(mHeaderView);
        } else {
            mAdapter.removeAllHeaderView();
        }
        mAdapter.setNewData(tagNames);
    }

}
