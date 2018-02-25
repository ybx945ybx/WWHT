package com.a55haitao.wwht.adapter.product;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.a55haitao.wwht.data.model.entity.ProductBaseBean;
import com.a55haitao.wwht.ui.view.ProductBrandRelatedView;
import com.a55haitao.wwht.ui.view.ProductEveryoneBuyingView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haotao_Fujie on 2016/10/28.
 */

public class ProductEveryoneBuyingAdapter extends PagerAdapter {

    private ArrayList<ArrayList<ProductBaseBean>>                    mProducts;
    private HashMap<Integer, ProductEveryoneBuyingView>              mCacheViews;
    private Activity                                                 mActivity;
    private ProductBrandRelatedView.ProductBrandRelatedViewInterface mOnClickListener;

    public ProductEveryoneBuyingAdapter(Activity activity, ArrayList<ProductBaseBean> products) {
        mActivity = activity;
        setProducts(products);
        mCacheViews = new HashMap<>();
    }

    private void setProducts(ArrayList<ProductBaseBean> products) {
        if (products == null || products.size() == 0) return;

        if (mProducts == null) {
            mProducts = new ArrayList<>();
        }

        for (ProductBaseBean product : products) {

            ArrayList<ProductBaseBean> lastOne = mProducts.size() == 0 ? null : mProducts.get(mProducts.size() - 1);
            if (lastOne == null || lastOne.size() == 4) {
                ArrayList<ProductBaseBean> newOne = new ArrayList<ProductBaseBean>();
                newOne.add(product);
                mProducts.add(newOne);

            } else {
                lastOne.add(product);
                if (mProducts.size() == 4 && lastOne.size() == 4) {
                    return;
                }
            }
        }
    }

    public void setClickListener(ProductBrandRelatedView.ProductBrandRelatedViewInterface listener) {
        mOnClickListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ProductEveryoneBuyingView view = mCacheViews.get(position);
        if (view == null) {
            view = new ProductEveryoneBuyingView(mActivity);
            view.setProductBrandRelatedViewInterface(new ProductBrandRelatedView.ProductBrandRelatedViewInterface() {
                @Override
                public void tapOnProduct(String spuid, String  name) {
                    if (mOnClickListener != null) {
                        mOnClickListener.tapOnProduct(spuid, name);
                    }
                }
            });
            mCacheViews.put(position, view);
        }
        ArrayList<ProductBaseBean> pairs = mProducts.get(position);
        view.updateUI(pairs);
        container.addView(view);
        return view;
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
