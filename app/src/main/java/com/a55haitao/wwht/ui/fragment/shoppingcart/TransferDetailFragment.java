package com.a55haitao.wwht.ui.fragment.shoppingcart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.adapter.category.TraceAdapter;
import com.a55haitao.wwht.data.model.result.TrackInfoResult;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.glide.UpyUrlManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by a55 on 2017/3/30.
 * 物流详情每个包裹页面
 */

public class TransferDetailFragment extends BaseFragment {

    private TrackInfoResult.PackageListBean packageListBean;

    private ScrollView   scrollViewContent;           //  物流信息部分
    private LinearLayout llytProducts;              //  展示商品图片

    private LinearLayout llytHome;                  //  国内快递公司及快递单号
    private LinearLayout llytExpressName;
    private TextView     tvExpressName;
    private LinearLayout llytExpressNum;
    private TextView     tvExpressNum;

    private ListView mListview;                     //  国内物流列表

    private LinearLayout llytExpressNameBottom;     //  国际物流部分
    private TextView     tvExpressNameBotto;
    private LinearLayout llytExpressNumBotto;
    private TextView     tvExpressNumBotto;
    private ImageView    ivStatus;
    private TextView     tvStatusContent;
    private TextView     tvStatusData;
    private boolean      isStart;                        //  是否刚刚国外发货

    private TextView tvNUllContent;           //  无物流信息提示

    public static TransferDetailFragment instance(TrackInfoResult trackInfoResult, int position) {
        TransferDetailFragment fragment = new TransferDetailFragment();
        Bundle                 bundle   = new Bundle();
        bundle.putString("package_info", new Gson().toJson(trackInfoResult));
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrackInfoResult trackInfoResult = new Gson().fromJson(getArguments().getString("package_info"), TrackInfoResult.class);
        packageListBean = trackInfoResult.package_list.get(getArguments().getInt("position"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_detail, container, false);

        scrollViewContent = (ScrollView) view.findViewById(R.id.scrollView_content);
        tvNUllContent = (TextView) view.findViewById(R.id.tv_null_content);

        if (packageListBean.track_info != null && packageListBean.track_info.size() > 0) {// 有物流信息

            scrollViewContent.setVisibility(View.VISIBLE);
            tvNUllContent.setVisibility(View.GONE);

            llytProducts = (LinearLayout) view.findViewById(R.id.llyt_products);
            for (int i = 0; i < packageListBean.product_list.size(); i++) {
                View      product   = LayoutInflater.from(getActivity()).inflate(R.layout.transfer_product_item, null);
                ImageView imageView = (ImageView) product.findViewById(R.id.iv_product);

                Glide.with(this)
                        .load(UpyUrlManager.getUrl(packageListBean.product_list.get(i).coverImgUrl, DisplayUtils.dp2px(getActivity(), 60)))
                        .dontAnimate()
                        .placeholder(R.mipmap.ic_default_square_small)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
                llytProducts.addView(product);
            }

            llytHome = (LinearLayout) view.findViewById(R.id.llyt_home);
            llytExpressName = (LinearLayout) view.findViewById(R.id.expressLayout_1);
            tvExpressName = (TextView) view.findViewById(R.id.expressNameTxt);
            llytExpressNum = (LinearLayout) view.findViewById(R.id.expressLayout_2);
            tvExpressNum = (TextView) view.findViewById(R.id.expressNoTxt);

            mListview = (ListView) view.findViewById(R.id.listView);

            llytExpressNameBottom = (LinearLayout) view.findViewById(R.id.expressLayout_1_bottom);
            tvExpressNameBotto = (TextView) view.findViewById(R.id.expressNameTxt_bottom);
            llytExpressNumBotto = (LinearLayout) view.findViewById(R.id.expressLayout_2_bottom);
            tvExpressNumBotto = (TextView) view.findViewById(R.id.expressNoTxt_bottom);

            ivStatus = (ImageView) view.findViewById(R.id.statusImg);
            tvStatusContent = (TextView) view.findViewById(R.id.statusContentTxt);
            tvStatusData = (TextView) view.findViewById(R.id.statusDataTxt);

            if (packageListBean.track_info.size() < 2) {
                isStart = true;
                TrackInfoResult.PackageListBean.TrackInfoBean trackInfoBean = packageListBean.track_info.get(packageListBean.track_info.size() - 1);
                setBottomView(trackInfoBean);

            } else {
                TrackInfoResult.PackageListBean.TrackInfoBean trackInfoBean = packageListBean.track_info.get(packageListBean.track_info.size() - 1);
                setBottomView(trackInfoBean);

                //                packageListBean.track_info.remove(packageListBean.track_info.size() - 1);
                ArrayList<TrackInfoResult.PackageListBean.TrackInfoBean> trackInfos = new ArrayList<>();
                for (int i = 0; i < packageListBean.track_info.size() - 1; i++) {
                    trackInfos.add(packageListBean.track_info.get(i));
                }

                llytHome.setVisibility(View.VISIBLE);
                llytExpressName.setVisibility(View.VISIBLE);
                llytExpressNum.setVisibility(View.VISIBLE);
                tvExpressName.setText(packageListBean.track_company);
                tvExpressNum.setText(packageListBean.track_number);
                mListview.setAdapter(new TraceAdapter(mActivity, trackInfos));

            }
        } else {
            scrollViewContent.setVisibility(View.GONE);
            tvNUllContent.setVisibility(View.VISIBLE);
        }

        return view;
    }

    // 底部国际物流板块
    private void setBottomView(TrackInfoResult.PackageListBean.TrackInfoBean trackInfoBean) {
        llytExpressNameBottom.setVisibility(View.VISIBLE);
        llytExpressNumBotto.setVisibility(View.VISIBLE);

        // 截取物流公司和快递单号
        String content = trackInfoBean.content;
        //        【物流公司:UPS,快递单号:1ZX5598X0305125124】    ,敬请留意物流更新"    截取快递公司和物流单号
        if (!TextUtils.isEmpty(content) && content.contains("【")) {
            String subContent = content.substring(content.indexOf("【"), content.indexOf("】") + 1);
            content = content.replace(subContent, "");

            String expressName = "";
            String expressNum  = "";
            if (!TextUtils.isEmpty(subContent)) {
                expressName = subContent.substring(6, subContent.indexOf(","));
                subContent = subContent.replace(expressName, "");
                expressNum = subContent.substring(12, subContent.length() - 1);
            }
            tvExpressNameBotto.setText(expressName);
            tvExpressNumBotto.setText(expressNum);
        }

        tvStatusContent.setText(content);
        tvStatusData.setText(trackInfoBean.date);

        tvStatusContent.setTextColor(isStart ? Color.parseColor("#FFAA00") : Color.parseColor("#666666"));
        tvStatusData.setTextColor(isStart ? Color.parseColor("#FFAA00") : Color.parseColor("#c8c8c8"));
        ivStatus.setImageResource(isStart ? R.mipmap.ic_trace_end_orange : R.mipmap.ic_trace_end);

    }

}
