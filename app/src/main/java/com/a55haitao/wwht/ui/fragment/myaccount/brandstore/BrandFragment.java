package com.a55haitao.wwht.ui.fragment.myaccount.brandstore;

import android.widget.CheckBox;

import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.model.result.GetFollowBrandStoreResult;
import com.a55haitao.wwht.data.model.annotation.BrandSellerFragmentType;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.utils.ToastUtils;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import rx.Observable;

/**
 * 商家Fragment
 * Created by 陶声 on 16/6/12.
 */
public class BrandFragment extends BrandSellerBaseFragment {
    @Override
    protected int getPageType() {
        return PageType.BRAND;
    }

    /**
     * 关注品牌
     *
     * @param chkFollowBrandStore checkbox
     * @param nameen              品牌英文名
     */
    @Override
    protected void requestFollowBrandStore(CheckBox chkFollowBrandStore, String nameen) {
        ProductRepository.getInstance()
                .followBrandSeller(PageType.BRAND, nameen)
                .compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<CommonDataBean>() {
                    @Override
                    public void onSuccess(CommonDataBean commonDataBean) {
                        if (chkFollowBrandStore.isChecked()) {
                            ToastUtils.showToast(mActivity, "关注成功");
                        }
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        chkFollowBrandStore.setChecked(!chkFollowBrandStore.isChecked());
                        return super.onFailed(e);
                    }

                    @Override
                    public void onFinish() {
                    }
                });
    }

    /**
     * 网络请求
     */
    protected void requestBrandStoreList() {
        Observable<GetFollowBrandStoreResult> followBrandStore;
        if (mUserId == 0) { // 个人主页
            followBrandStore = ProductRepository.getInstance()
                    .getFollowBrandStore(BrandSellerFragmentType.BRAND, mCurrentPage);
        } else { // 他人主页
            followBrandStore = ProductRepository.getInstance()
                    .getFollowBrandStore(BrandSellerFragmentType.BRAND, mCurrentPage, mUserId);
        }

        followBrandStore.compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<GetFollowBrandStoreResult>() {
                    @Override
                    public void onSuccess(GetFollowBrandStoreResult result) {
                        if (result.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = result.allpage;
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(result.datas);
                            } else {
                                mAdapter.addData(result.datas);
                                if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= result.count) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        }
                        mHasData = true;
                    }

                    @Override
                    public boolean onFailed(Throwable e) {
                        showFailView(mSv, e, mHasData);
                        return mHasData;
                    }

                    @Override
                    public void onFinish() {
                        mSwipe.setEnabled(true);
                        mSwipe.setRefreshing(false);
                        mAdapter.setEnableLoadMore(true);
                    }
                });
    }
}
