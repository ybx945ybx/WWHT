package com.a55haitao.wwht.ui.fragment.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.entity.ProductDetailBean;
import com.a55haitao.wwht.ui.fragment.BaseFragment;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductQueryFragment extends BaseFragment {

    @BindView(R.id.quertTextLin) LinearLayout quertTextLin;

    public ArrayList<ProductDetailBean.ProductDetailQuestion> faq;

    public static ProductQueryFragment newInstance(String faq) {
        ProductQueryFragment fragment = new ProductQueryFragment();
        Bundle               bundle   = new Bundle();
        bundle.putString("faq", faq);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String faqJson = getArguments().getString("faq");
        faq = new Gson().fromJson(faqJson, new TypeToken<ArrayList<ProductDetailBean.ProductDetailQuestion>>() {
        }.getType());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_query, container, false);
        ButterKnife.bind(this, view);
        renderUI();
        return view;
    }

    private void renderUI() {
        for (ProductDetailBean.ProductDetailQuestion productDetailQuestion : faq) {
            LinearLayout itemLin = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.product_query_title_content, quertTextLin, false);

            HaiTextView titleTxt   = (HaiTextView) itemLin.getChildAt(0);
            HaiTextView contentTxt = (HaiTextView) itemLin.getChildAt(1);
            titleTxt.setText(productDetailQuestion.title);
            contentTxt.setText(productDetailQuestion.content);
            quertTextLin.addView(itemLin);

        }
    }
}
