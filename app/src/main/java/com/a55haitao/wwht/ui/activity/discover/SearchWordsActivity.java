package com.a55haitao.wwht.ui.activity.discover;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a55haitao.wwht.HaiApplication;
import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.data.model.entity.HotWordsBean;
import com.a55haitao.wwht.data.model.entity.RealmHistoryBean;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.base.BaseNoFragmentActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.TraceUtils;
import com.a55haitao.wwht.utils.HaiUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import tom.ybxtracelibrary.YbxTrace;

/**
 * 搜索_进入页
 */
public class SearchWordsActivity extends BaseNoFragmentActivity implements TagFlowLayout.OnTagClickListener {

    @BindView(R.id.searchEdt)              EditText      mEditText;                  // 搜索框
    @BindView(R.id.img_clear_input)        ImageView     ivClear;                    // 清除输入
    @BindView(R.id.llyt_content)           LinearLayout  llytContent;                // 标签部分
    @BindView(R.id.noHistoryTxt)           TextView      mNoHistoryTxt;              // Null History
    @BindView(R.id.historyTagFlowLayout)   TagFlowLayout mHistoryTagFlowLayout;      // 历史搜索
    @BindView(R.id.hotSearchTagFlowLayout) TagFlowLayout mHotSearchTagFlowLayout;    // 热门词汇
    @BindView(R.id.rycv_auto_fill)         RecyclerView  rycvAutoFill;               // 自动填充

    private static final int REQUEST_SEARCH = 100;

    private InputMethodManager      imm;
    private RVBaseAdapter<String[]> mAutoFillAdapter;  // 自动填充视图的适配器
    private Tracker                 mTracker;          // GA Tracker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        ivClear.setOnClickListener(v -> mEditText.setText(""));
        //初始化PopuWindow
        mAutoFillAdapter = new RVBaseAdapter<String[]>(this, new ArrayList<>(), R.layout.auto_fill_text_item) {
            @Override
            public void bindView(RVBaseHolder holder, String[] strings) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(strings[0]);

                // 字体颜色
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#26241F"));

                if (strings[0].toUpperCase().contains(mEditText.getText().toString().toUpperCase())) {
                    int start = strings[0].toUpperCase().indexOf(mEditText.getText().toString().toUpperCase());
                    int end   = start + HaiUtils.getSize(mEditText.getText().toString());
                    spannableStringBuilder.setSpan(foregroundColorSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                    ((HaiTextView) holder.getView(R.id.tv_word)).setText(spannableStringBuilder);
                } else {
                    holder.setText(R.id.tv_word, strings[0]);
                }
                holder.itemView.setTag(strings[0]);
            }
        };
        mAutoFillAdapter.setOnItemClickListener(v -> {
            toSearchResultAct((String) v.getTag());
            llytContent.setVisibility(View.VISIBLE);
            rycvAutoFill.setVisibility(View.GONE);
            if (HaiUtils.getSize(mAutoFillAdapter.getmDatas()) > 0) {
                mAutoFillAdapter.getmDatas().clear();
                mAutoFillAdapter.notifyDataSetChanged();
            }

        });
        rycvAutoFill.setLayoutManager(new LinearLayoutManager(this));
        rycvAutoFill.setAdapter(mAutoFillAdapter);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    llytContent.setVisibility(View.VISIBLE);
                    rycvAutoFill.setVisibility(View.GONE);
                    ivClear.setVisibility(View.GONE);
                } else {
                    searchWords(s.toString());
                    llytContent.setVisibility(View.GONE);
                    rycvAutoFill.setVisibility(View.VISIBLE);
                    ivClear.setVisibility(View.VISIBLE);
                }
            }
        });
        mEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                    Toast.makeText(SearchWordsActivity.this, "搜索词不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }
                toSearchResultAct(v.getText().toString());
                return true;
            }
            return false;

        });

        // GA Tracker
        HaiApplication application = (HaiApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("搜索_进入");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        loadHotWords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEditText.requestFocus();
        showSoftInput();
        loadHistory();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    /**
     * 取消按钮
     */
    @OnClick(R.id.cancleTxt)
    public void cancle() {
        onBackPressed();
    }

    /**
     * 加载以及更新历史
     */
    private void loadHistory() {
        Realm                          realm   = Realm.getDefaultInstance();
        RealmResults<RealmHistoryBean> results = realm.where(RealmHistoryBean.class).findAllSorted("time", Sort.DESCENDING);

        if (results.size() == 0) {
            mNoHistoryTxt.setVisibility(View.VISIBLE);
            mHistoryTagFlowLayout.setVisibility(View.GONE);
        } else {
            mNoHistoryTxt.setVisibility(View.GONE);
            mHistoryTagFlowLayout.setVisibility(View.VISIBLE);
            mHistoryTagFlowLayout.setOnTagClickListener(this);
            mHistoryTagFlowLayout.setAdapter(new TagAdapter<RealmHistoryBean>(results) {
                @Override
                public View getView(FlowLayout parent, int position, RealmHistoryBean o) {
                    TextView tv = (TextView) LayoutInflater.from(SearchWordsActivity.this).inflate(R.layout.simple_tag_txt_layout, parent, false);
                    tv.setText(o.topic);
                    return tv;
                }
            });
        }
    }

    /**
     * 清空历史
     */
    @OnClick(R.id.clearHistoryImgBtn)
    public void clearHistory() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(RealmHistoryBean.class);
        realm.commitTransaction();
        loadHistory();
    }

    /**
     * 加载热门搜索词
     */
    private void loadHotWords() {
        ProductRepository.getInstance()
                .getHotSearchWords()
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<HotWordsBean>() {
                    @Override
                    public void onSuccess(HotWordsBean bean) {
                        mHotSearchTagFlowLayout.setAdapter(new TagAdapter<String>(bean.hotquery) {
                            @Override
                            public View getView(FlowLayout parent, int position, String o) {
                                TextView tv = (TextView) LayoutInflater.from(SearchWordsActivity.this).inflate(R.layout.simple_tag_txt_layout, parent, false);
                                tv.setText(o);
                                return tv;
                            }
                        });
                        mHotSearchTagFlowLayout.setVisibility(View.VISIBLE);
                        mHotSearchTagFlowLayout.setOnTagClickListener((view, position, parent) -> {
                            toSearchResultAct(((TextView) view).getText().toString());
                            mEditText.setText(((TextView) view).getText().toString());
                            mEditText.setSelection(((TextView) view).getText().toString().length());
                            return false;
                        });
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    /**
     * 获取自动搜索
     *
     * @param string
     */
    private void searchWords(String string) {
        ProductRepository.getInstance()
                .getAutoFill(string)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<ArrayList<String[]>>() {
                    @Override
                    public void onSuccess(ArrayList<String[]> result) {
                        showAutoFillView(result);
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    // 自动匹配结果
    private void showAutoFillView(ArrayList<String[]> result) {
        mAutoFillAdapter.setmDatas(result);
        mAutoFillAdapter.notifyDataSetChanged();
    }

    //弹起软键盘
    private void showSoftInput() {
        mEditText.postDelayed(() -> imm.showSoftInput(mEditText, 0), 200);

    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        TextView tv   = (TextView) view;
        String   lect = String.valueOf(tv.getText());
        toSearchResultAct(lect);
        mEditText.setText(lect);
        mEditText.setSelection(lect.length());
        return false;
    }

    private void toSearchResultAct(String characteric) {

        RealmHistoryBean bean = new RealmHistoryBean();
        bean.topic = characteric;
        bean.time = System.currentTimeMillis();
        Realm defaultInstance = Realm.getDefaultInstance();
        defaultInstance.beginTransaction();
        defaultInstance.insertOrUpdate(bean);
        defaultInstance.commitTransaction();

        // 埋点
        YbxTrace.getInstance().event(mActivity, pref, prefh, getActivityTAG(), purlh, "", TraceUtils.PositionCode_SearchKey, TraceUtils.Event_Category_Click, "", null, "");

        SearchResultActivity.toThisActivityForResult(this, characteric, REQUEST_SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_SEARCH) {
            if (data != null) {
                String words = data.getStringExtra("searchWords");
                if (!TextUtils.isEmpty(words)) {
                    mEditText.setText(words);
                    mEditText.setSelection(words.length());

                }
            }
        }
    }


}
