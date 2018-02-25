package com.a55haitao.wwht.ui.activity.discover;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.SearchSpecialBean;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.SpecialRepository;
import com.a55haitao.wwht.ui.activity.base.BaseHasFragmentActivity;
import com.a55haitao.wwht.ui.fragment.discover.TwiceProductFragment;
import com.a55haitao.wwht.ui.view.DynamicHeaderView;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.ui.view.MultipleStatusView;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by a55 on 2017/4/23.
 * 搜索专题主页
 */
public class CategorySpecialActivity extends BaseHasFragmentActivity {

    @BindView(R.id.title_view)   DynamicHeaderView  titleView;
    @BindView(R.id.msv)          MultipleStatusView msView;
    @BindView(R.id.appBarLayout) AppBarLayout       mAppBarLayout;
    @BindView(R.id.bigCoverImg)  ImageView          ivCover;
    @BindView(R.id.descTxt)      HaiTextView        tvDesc;

    private TwiceProductFragment fragment;
    private int                  type;
    public  int                  special_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        ButterKnife.bind(this);

        initIntent();
        initFragment();
        requestInfo();
    }

    @Override
    protected String getActivityTAG() {
        return TAG + "?" + "id = " + special_id;
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", -1);
            special_id = intent.getIntExtra("special_id", -1);
        }

    }

    private void initFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment = TwiceProductFragment.newInstance(getIntent().getExtras());
        ft.add(R.id.framLayout, fragment);
        ft.commit();

        msView.setOnRetryClickListener(v -> requestInfo());
    }

    /**
     * 请求搜素专题信息
     */
    private void requestInfo() {
        msView.showLoading();

        SpecialRepository.getInstance()
                .getSearchSpecialInfo(special_id)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(new DefaultSubscriber<SearchSpecialBean>() {
                    @Override
                    public void onSuccess(SearchSpecialBean searchSpecialBean) {
                        if (searchSpecialBean != null) {
                            msView.showContent();
                            titleView.setHeadTitle("搜索专题");
                            tvDesc.setText(searchSpecialBean.desc);
                            UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(),
                                    searchSpecialBean.img_cover,
                                    UpaiPictureLevel.SINGLE,
                                    R.id.u_pai_yun_null_holder_tag,
                                    ivCover);

                            fragment.setReposityInfo("搜索专题", type, searchSpecialBean.query);
                            fragment.setmTag(getActivityTAG());
                            fragment.requestData();
                        } else {
                            msView.showEmpty();
                        }
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(msView, e, mHasLoad);
                        return mHasLoad;
                    }

                    @Override
                    public void onFinish() {
                        //                        mHasLoad = true;
                    }
                });

    }

    /**
     * 跳转到本页面
     *
     * @param context    context
     * @param special_id 专题Id
     * @param pageType   专题类型
     */
    public static void toThisActivity(Context context, int special_id, int pageType) {
        toThisActivity(context, special_id, pageType, false);
    }

    /**
     * 跳转到本页面
     *
     * @param context    context
     * @param special_id 专题Id
     * @param pageType   专题类型
     * @param newTask    开启新栈
     */
    public static void toThisActivity(Context context, int special_id, int pageType, boolean newTask) {
        Intent intent = new Intent(context, CategorySpecialActivity.class);
        intent.putExtra("special_id", special_id);
        intent.putExtra("type", pageType);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

}
