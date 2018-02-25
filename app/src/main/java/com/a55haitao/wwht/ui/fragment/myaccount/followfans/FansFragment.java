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
 * 粉丝Fragment
 * Created by skyper on 16/6/12.
 */
public class FansFragment extends FollowFansBaseFragment {
    /**
     * 网络请求
     */
    public void loadUserList() {
        Observable<UserListResultData> followerList;
        if (mUserId == 0) { // 个人主页
            followerList = SnsRepository.getInstance()
                    .getFollowerList(mCurrentPage);
        } else { // 他人主页
            followerList = SnsRepository.getInstance()
                    .getFollowerList(mCurrentPage, mUserId);
        }

        followerList.compose(RxLifecycleAndroid.bindFragment(lifecycle()))
                .subscribe(new DefaultSubscriber<UserListResultData>() {
                    @Override
                    public void onSuccess(UserListResultData userListResult) {
                        if (userListResult.count == 0) {
                            mSv.showEmpty();
                            mAdapter.notifyDataSetChanged();
                            TextView tvDesc = (TextView) mSv.getEmptyView().findViewById(R.id.tv_desc);
                            tvDesc.setText(mUserId == HaiUtils.getUserId() ? "你目前一个粉丝都没有哦" : "TA目前一个粉丝都没有哦");
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
