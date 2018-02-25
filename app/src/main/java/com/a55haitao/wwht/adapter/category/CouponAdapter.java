package com.a55haitao.wwht.adapter.category;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.CouponBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 55haitao on 2016/11/6.
 */

public class CouponAdapter extends BaseAdapter {

    private ArrayList<CouponBean> mDatas;
    private Context               mContext;

    public CouponAdapter(Context context, ArrayList<CouponBean> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size() == 0 ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CouponBean   item   = mDatas.get(position);
        CouponHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_coupon_list, parent, false);
            holder = new CouponHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CouponHolder) convertView.getTag();
        }
        //先设置数据,后设置状态
        //1.设置数据
        holder.mSmallSizeTxt.setText(item.getShow_desc());
        //根据优惠券 TYPE,改变右侧显示状态
        holder.mBigSizeTxt.setText(item.getShow_title().replace("￥", "\u00A5"));
        holder.mCouponNameTxt.setText(item.getTitle());
        holder.mCouponNameTxt.setTextColor(ContextCompat.getColor(mContext,
                item.getStatus() == 1 ? R.color.colorTextYellow : R.color.colorGray666666));
        holder.mCouponConditionTxt.setText(item.getSubtitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        holder.mCouponDateTxt.setText(String.format("%s-%s",
                sdf.format(new Date(1000 * item.getBegintime())),
                sdf.format(new Date(1000 * item.getEndtime()))));
        //2.设置状态

        //内边框
        View insideLayout = holder.mCouponInsideLayout;
        //显示优惠券是否过期、被使用状态的ImageView
        ImageView imageView = holder.mCouponStatusImg;

        //选中状态
        holder.mChoiceImgView.setVisibility(item.isSelect() ? View.VISIBLE : View.INVISIBLE);

        //3.设置数据
        insideLayout.setTag(R.id.tag_coupon_code, item.getCode());

        switch (item.getStatus()) {
            case 1: //未使用
                //使用蓝色边框背景
                insideLayout.setBackgroundResource(R.mipmap.coupon_inside_usless_bg);
                imageView.setVisibility(View.INVISIBLE);
                holder.mBigSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextYellow));
                holder.mSmallSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextYellow));
                break;
            case 2: //已过期
                insideLayout.setBackgroundResource(R.mipmap.coupon_inside_gray_bg);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.mipmap.coupon_out_of_data_bg);
                holder.mBigSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
                holder.mSmallSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
                break;
            case 3: //已使用
                insideLayout.setBackgroundResource(R.mipmap.coupon_inside_gray_bg);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.mipmap.coupon_used_bg);
                holder.mBigSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
                holder.mSmallSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
                break;
            case 4: //不可用
                insideLayout.setBackgroundResource(R.mipmap.coupon_inside_gray_bg);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.mipmap.coupon_use_less);
                holder.mBigSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
                holder.mSmallSizeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray666666));
                break;
        }


        return convertView;
    }

    public class CouponHolder {

        @BindView(R.id.couponStatusImg)    ImageView    mCouponStatusImg;
        @BindView(R.id.bigSizeTxt)         TextView     mBigSizeTxt;
        @BindView(R.id.smallSizeTxt)       TextView     mSmallSizeTxt;
        @BindView(R.id.couponNameTxt)      TextView     mCouponNameTxt;
        @BindView(R.id.couponConditionTxt) TextView     mCouponConditionTxt;
        @BindView(R.id.couponDateTxt)      TextView     mCouponDateTxt;
        @BindView(R.id.couponInsideLayout) LinearLayout mCouponInsideLayout;
        @BindView(R.id.choiceImgView)      ImageView    mChoiceImgView;

        public CouponHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }
}
