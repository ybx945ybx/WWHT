package com.a55haitao.wwht.adapter.category;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.LVBaseAdaptrer;
import com.a55haitao.wwht.data.model.annotation.PageType;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.CommonDataBean;
import com.a55haitao.wwht.data.model.result.AllBrandOrSellerBean;
import com.a55haitao.wwht.data.model.result.GetFollowBrandStoreResult;
import com.a55haitao.wwht.data.net.DefaultSubscriber;
import com.a55haitao.wwht.data.repository.ProductRepository;
import com.a55haitao.wwht.ui.activity.discover.SiteActivity;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.ToastUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.ArrayList;

/**
 * Created by 55haitao on 2016/11/9.
 */

public class AllBrandOrSellerAdapter extends SectionedBaseAdapter {

    private final boolean mIsBrand;

    private ArrayList<AllBrandOrSellerBean> datas;

    private Activity mActivity;

    private LayoutInflater inflater;

    private View.OnClickListener mItemListener;

    private View.OnClickListener mStarListener;

    public AllBrandOrSellerAdapter(Activity activity, ArrayList<AllBrandOrSellerBean> datas, boolean isBrand) {
        this.mActivity = activity;
        this.datas = datas;
        mIsBrand = isBrand;
        this.inflater = LayoutInflater.from(activity);
        initListener();
    }

    private void initListener() {

        mStarListener = v -> {

            if (!HaiUtils.needLogin(mActivity)) {
                GetFollowBrandStoreResult.DataBean item = (GetFollowBrandStoreResult.DataBean) v.getTag();

                ProductRepository.getInstance()
                        .followBrandSeller(mIsBrand ? PageType.BRAND : PageType.SELLER, item.nameen)
                        .subscribe(new DefaultSubscriber<CommonDataBean>() {
                            @Override
                            public void onSuccess(CommonDataBean commonDataBean) {
                                if (!HaiUtils.needLogin(mActivity)) {
                                    item.is_followed = !item.is_followed;
                                    if (item.is_followed)
                                        ToastUtils.showToast(mActivity, "关注成功");
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFinish() {

                            }
                        });
            }
        };

        mItemListener = v -> {
            GetFollowBrandStoreResult.DataBean item = (GetFollowBrandStoreResult.DataBean) v.getTag(R.id.tag_for_img);

            // 埋点
//            TraceUtils.addClick(TraceUtils.PageCode_BrandOrSiteDetail, item.name, mActivity, TraceUtils.PageCode_Category, mIsBrand ? TraceUtils.PositionCode_Brand + "" : TraceUtils.PositionCode_Store+ "", "");

//            TraceUtils.addAnalysAct(TraceUtils.PageCode_BrandOrSiteDetail, TraceUtils.PageCode_Category, mIsBrand ? TraceUtils.PositionCode_Brand : TraceUtils.PositionCode_Store, item.name);

            SiteActivity.toThisActivity(mActivity, item.name, mIsBrand ? PageType.BRAND : PageType.SELLER, item.logo1, item.logo3, item.img_cover);
        };

    }

    @Override
    public Object getItem(int section, int position) {
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        long result = 0;
        result += section;
        for (int i = 0; i < section; i++) {
            result += getCountForSection(i);
        }
        result += position;
        return result;
    }

    @Override
    public int getSectionCount() {
        return HaiUtils.getSize(datas);
    }

    @Override
    public int getCountForSection(int section) {
        return datas.get(section).values.size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        GetFollowBrandStoreResult.DataBean item = datas.get(section).values.get(position);

        LVBaseAdaptrer.LVHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_all_brand_or_seller_layout, parent, false);
            holder = new LVBaseAdaptrer.LVHolder(convertView);

            holder.getItemView().setOnClickListener(mItemListener);
            holder.getView(R.id.concernBtn).setOnClickListener(mStarListener);

            convertView.setTag(holder);
        } else {
            holder = (LVBaseAdaptrer.LVHolder) convertView.getTag();
        }

        holder.getItemView().setTag(R.id.tag_for_img, item);

        String title = item.nameen;
        if (mIsBrand && !TextUtils.equals(item.namecn, item.nameen)) {
            title = item.nameen + "/" + item.namecn;
        }

        holder.setText(R.id.brandNameTxt, title);
        CheckedTextView checkedTextView = holder.getView(R.id.concernBtn);
        checkedTextView.setTag(item);

        checkedTextView.setChecked(item.is_followed);

        if (TextUtils.isEmpty(item.logo1)) {
            holder.setVisibile(R.id.smallCoverTex, true);
            holder.setVisibile(R.id.smallCoverImg, false);

            String logoName = "";
            if (!TextUtils.isEmpty(item.nameen)) {
                logoName = item.nameen;
            } else if (!TextUtils.isEmpty(item.namecn)) {
                logoName = item.namecn;
            } else {
                logoName = item.name;
            }

            HaiUtils.setBrandOrSellerImg(holder.getView(R.id.smallCoverTex), logoName, 2);

        } else {
            holder.setVisibile(R.id.smallCoverTex, false);
            holder.setVisibile(R.id.smallCoverImg, true);

            UPaiYunLoadManager.loadImage(mActivity, item.logo1, UpaiPictureLevel.FOURTH, R.id.u_pai_yun_null_holder_tag, holder.getView(R.id.smallCoverImg));
        }
        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_hot_category_header, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.headerTxt)).setText(datas.get(section).index);

        return convertView;
    }

    public void changeData(ArrayList<AllBrandOrSellerBean> datas) {
        if (datas == null) {
            return;
        }
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

}
