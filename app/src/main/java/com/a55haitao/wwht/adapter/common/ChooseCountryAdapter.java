package com.a55haitao.wwht.adapter.common;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.enums.CountryCode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 选择国家 Adapter
 * <p>
 * Created by 陶声 on 16/7/18.
 */
public class ChooseCountryAdapter extends BaseAdapter {
    private       CountryCode[]  mCountries;
    private       Activity       mActivity;
    private final LayoutInflater mInflater;

    public ChooseCountryAdapter(Activity activity) {
        mCountries = CountryCode.values();
        mActivity = activity;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mCountries.length;
    }

    @Override
    public Object getItem(int position) {
        return mCountries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_country, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CountryCode country = (CountryCode) getItem(position);
        holder.mTvCountryName.setText(country.getName());
        holder.mTvCountryCode.setText("+" + country.getCode());

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent();
            //            intent.putExtra("location_desc", country.getName());
            intent.putExtra("region_id", country.getCode());
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
        });
        return convertView;
    }

    /**
     * LVHolder
     */
    static class ViewHolder {
        @BindView(R.id.tv_country_name) TextView mTvCountryName;
        @BindView(R.id.tv_country_code) TextView mTvCountryCode;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
