package com.a55haitao.wwht.ui.fragment.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.RVBaseAdapter;
import com.a55haitao.wwht.adapter.RVBaseHolder;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.RelateSiteBean;
import com.a55haitao.wwht.data.model.entity.SellerBean;
import com.a55haitao.wwht.data.model.result.RelateSiteResult;
import com.a55haitao.wwht.data.net.ActivityCollector;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.ProductLinearLayout;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 相关品牌/商家
 */
public class RelateSiteFragment extends BaseFragment {

    private int    mType;
    private String mName;

    @BindView(R.id.nullTxt)             TextView     mNullTxtView;
    @BindView(R.id.contentRecyclerView) RecyclerView mContentRecyclerView;

    private Unbinder mUnbinder;

    public static RelateSiteFragment newInstance(int type, String name) {
        RelateSiteFragment f      = new RelateSiteFragment();
        Bundle             bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("name", name);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt("type");
        mName = getArguments().getString("name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_releate_site, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mNullTxtView.setText(mType == PageType.BRAND ? "暂无相关品牌哦!" : "暂无相关商家哦！");
        requestData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void requestData() {
        showProgressDialog("正在加载", true);
        /*if (mType == PageType.BRAND) {
            PageRepository.getInstance()
                    .getRelateBrand(mName)
                    .subscribe(new TestSubscriber<>(this));
        } else if (mType == PageType.SELLER) {
            PageRepository.getInstance()
                    .getRelateSeller(mName)
                    .subscribe(new TestSubscriber<>(this));
        }*/

        ProductRepository.getInstance()
                .getRelateBrandSeller(mType, mName)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<RelateSiteResult>() {
                    @Override
                    public void onSuccess(RelateSiteResult result) {
                        boolean hasContent = (result != null) && (HaiUtils.getSize(result.brands) > 0 || HaiUtils.getSize(result.sellers) > 0);

                        if (hasContent) {
                            initRecyclerView(mType == PageType.BRAND ? result.brands : result.sellers);
                            mNullTxtView.setVisibility(View.GONE);
                            mContentRecyclerView.setVisibility(View.VISIBLE);
                        } else {     //show null testview
                            mNullTxtView.setVisibility(View.VISIBLE);
                            mContentRecyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }

    /*@Override
    public void onSuccess(RelateSiteResult relateSiteResult) {

        boolean hasContent = (relateSiteResult != null) && (HaiUtils.getSize(relateSiteResult.brands) > 0 || HaiUtils.getSize(relateSiteResult.sellers) > 0);

        if (hasContent) {
            initRecyclerView(mType == PageType.BRAND ? relateSiteResult.brands : relateSiteResult.sellers);
            mNullTxtView.setVisibility(View.GONE);
            mContentRecyclerView.setVisibility(View.VISIBLE);
        } else {     //show null testview
            mNullTxtView.setVisibility(View.VISIBLE);
            mContentRecyclerView.setVisibility(View.GONE);
        }
    }*/

    private void initRecyclerView(ArrayList<RelateSiteBean> sites) {
        RVBaseAdapter<RelateSiteBean> adapter =
                new RVBaseAdapter<RelateSiteBean>(getContext(), sites, R.layout.relate_site_item_layout) {
                    @Override
                    public void bindView(RVBaseHolder holder, RelateSiteBean relateSiteBean) {
                        SellerBean.SellerDescBaseBean bean =
                                mType == PageType.BRAND ? relateSiteBean.brand_info : relateSiteBean.seller_info;
                        UPaiYunLoadManager.loadImage(ActivityCollector.getTopActivity(),
                                bean.pdb_logo, UpaiPictureLevel.FOURTH,
                                R.id.u_pai_yun_null_holder_tag, holder.getView(R.id.smallCoverImg));
                        if (mType == PageType.BRAND) {
                            holder.setText(R.id.nameTxt, bean.nameen + "/" + bean.namecn);
                        } else {
                            holder.setText(R.id.nameTxt, bean.nameen);
                        }
                        holder.setText(R.id.countryTxt, bean.country);
                        if (HaiUtils.getSize(relateSiteBean.products) > 0) {
                            ((ProductLinearLayout) holder.getView(R.id.productLayout01)).setData(relateSiteBean.products.get(0));
                            ((ProductLinearLayout) holder.getView(R.id.productLayout02)).setData(relateSiteBean.products.get(1));
                            ((ProductLinearLayout) holder.getView(R.id.productLayout03)).setData(relateSiteBean.products.get(2));
                            holder.getView(R.id.productParentLayout).setVisibility(View.VISIBLE);
                        }
                        holder.getView(R.id.jumpImgView).setTag(R.id.tag_for_img, bean);
                    }
                };
        adapter.setOnClickListener(R.id.jumpImgView, view -> {
            String                        name = null;
            SellerBean.SellerDescBaseBean bean = (SellerBean.SellerDescBaseBean) view.getTag(R.id.tag_for_img);
            if (bean.name != null) {
                name = bean.name;
            } else if (bean.nameen != null) {
                name = bean.nameen;
            } else if (bean.namecn != null) {
                name = bean.namecn;
            }
            SiteActivity.toThisActivity(getActivity(), name, mType, bean.logo1, bean.logo3, bean.img_cover);
        });
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentRecyclerView.setAdapter(adapter);
    }

   /* @Override
    public void onFinish() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        showFailView(e);
    }*/
}
