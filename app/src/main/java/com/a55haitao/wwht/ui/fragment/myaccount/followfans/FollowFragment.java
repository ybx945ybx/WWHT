package com.a55haitao.wwht.ui.fragment.myaccount.followfans;

import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.model.result.UserListResultData;
import com.a55haitao.wwht.data.repository.SnsRepository;
import com.a55haitao.wwht.utils.HaiUtils;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;

import rx.Observable;

/**
 * 关注Fragment
 * "_mt":"55haitao_sns.SnsAPI/get_following_list"
 * Created by 陶声 on 16/6/12.
 */
public class FollowFragment extends FollowFansBaseFragment {
    /**
     * 网络请求
     */
    public void loadUserList() {
        Observable<UserListResultData> followingList;
        if (mUserId == 0) { // 个人主页
            followingList = SnsRepository.getInstance()
                    .getFollowingList(mCurrentPage);
        } else { // 他人主页
            followingList = SnsRepository.getInstance()
                    .getFollowingList(mCurrentPage, mUserId);
        }

        followingList.compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<UserListResultData>() {
                    @Override
                    public void onSuccess(UserListResultData userListResult) {
                        if (userListResult.count == 0) {
                            mSv.showEmpty();
                            //                            mAdapter.notifyDataSetChanged();
                            TextView tvDesc = (TextView) mSv.getEmptyView().findViewById(R.id.tv_desc);
                            tvDesc.setText(mUserId == HaiUtils.getUserId() ? "你目前没有关注用户哦" : "TA目前没有关注用户哦");
                            mAdapter.loadMoreFail();
                        } else {
                            mSv.showContent();
                            mAllPage = userListResult.allpage;
                            if (mSwipe.isRefreshing()) {
                                mAdapter.setNewData(userListResult.users);
                            } else {
                                mAdapter.addData(userListResult.users);
                                if (mAdapter.getData().size() > PAGE_SIZE && mAdapter.getData().size() >= userListResult.count) {
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
