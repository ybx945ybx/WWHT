package com.a55haitao.wwht.adapter.product;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.ui.view.ProductBrandRelatedView;
import com.a55haitao.wwht.ui.view.StrikeThruTextView;

import java.util.ArrayList;

/**
 * Created by Haotao_Fujie on 16/10/28.
 */

public class ProductBrandAdpater extends PagerAdapter {

    private ArrayList<ArrayList<ProductBaseBean>> mProducts;

    private SparseArray<ProductBrandRelatedView> mCacheViews;

    private Context mContext;

    private ProductBrandRelatedView.ProductBrandRelatedViewInterface mOnClickListener;

    public ProductBrandAdpater(Context context, ArrayList<ProductBaseBean> products) {
        mContext = context;
        setProducts(products);
        mCacheViews = new SparseArray<>();
    }

    public void setClickListener(ProductBrandRelatedView.ProductBrandRelatedViewInterface listener) {
        mOnClickListener = listener;
    }

    private void setProducts(ArrayList<ProductBaseBean> products) {

        if (products == null || products.size() == 0) return;

        if (mProducts == null) {
            mProducts = new ArrayList<>();
        }

        for (ProductBaseBean product : products) {

            ArrayList<ProductBaseBean> lastOne = mProducts.size() == 0 ? null : mProducts.get(mProducts.size() - 1);
            if (lastOne == null || lastOne.size() == 2) {
                ArrayList<ProductBaseBean> newOne = new ArrayList<ProductBaseBean>();
                newOne.add(product);
                mProducts.add(newOne);
            } else {
                lastOne.add(product);
                if (mProducts.size() == 4) {
                    return;
                }
            }
        }
    }

    @Override
    public int getCount() {
        return mProducts == null ? 0 : mProducts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ProductBrandRelatedView view = mCacheViews.get(position);
        if (view == null) {
            view = new ProductBrandRelatedView(mContext);
            view.setProductBrandRelatedViewInterface(new ProductBrandRelatedView.ProductBrandRelatedViewInterface() {
                @Override
                public void tapOnProduct(String spuid, String name) {
                    if (mOnClickListener != null) {
                        mOnClickListener.tapOnProduct(spuid, name);
                    }
                }
            });

            ArrayList<ProductBaseBean> pairs = mProducts.get(position);
            view.updateUI(pairs);
            mCacheViews.put(position, view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
