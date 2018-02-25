package com.a55haitao.wwht.ui.activity.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.ui.activity.base.BaseActivity;
import com.a55haitao.wwht.ui.fragment.shoppingcart.CenterShoppingCartFragment;
import com.a55haitao.wwht.utils.TraceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingCartActivity extends BaseActivity {

    @BindView(R.id.shoppingCartFramelayout) FrameLayout  shoppingCartFramelayout;
    @BindView(R.id.activity_shopping_cart)  LinearLayout activityShoppingCart;

    private CenterShoppingCartFragment centerShoppingCartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);

        // 加载Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        centerShoppingCartFragment = new CenterShoppingCartFragment();
        ft.add(R.id.shoppingCartFramelayout, centerShoppingCartFragment);
        ft.commit();
    }

    @Override
    protected String getActivityTAG() {
        return TAG;
    }

    public static void toThisAcivity(Activity activity) {
        Intent intent = new Intent(activity, ShoppingCartActivity.class);
        activity.startActivity(intent);
    }
}
