package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.a55haitao.wwht.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drolmen on 2017/2/7.
 */

public class ExchangeCouponLayout extends LinearLayout {

    @BindView(R.id.titleTxt)                HaiTextView    titleTxt;
    @BindView(R.id.exchangeCouponTxt)       EditText       exchangeCouponTxt;
    @BindView(R.id.exchangeCouponTitle)     HaiTextView    exchangeCouponTitle;
    @BindView(R.id.exchangeCouponSubTitle)  HaiTextView    exchangeCouponSubTitle;
    @BindView(R.id.exchangeCouponDate)      HaiTextView    exchangeCouponDate;
    @BindView(R.id.exchangeCouponLayout)    RelativeLayout exchangeCouponLayout;
    @BindView(R.id.exchangeCouponBtn)       Button         exchangeCouponBtn;
    @BindView(R.id.exchangeCouponErrMsgTxt) HaiTextView    exchangeCouponErrMsgTxt;
    @BindView(R.id.centerView)              LinearLayout   centerView;

    private ExchangeCouponPopuWindowInterface mInterface;
    private boolean                                                    continueExchange;

    public ExchangeCouponLayout(Context context) {
        super(context);
    }

    public ExchangeCouponLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        renderUI();
    }

    private void renderUI() {

        exchangeCouponBtn.setOnClickListener(v -> {

            if (continueExchange) {
                titleTxt.setText("输入兑换码领取优惠券");
                continueExchange = false;
                exchangeCouponBtn.setText("确认兑换");
                exchangeCouponLayout.setVisibility(View.GONE);
                exchangeCouponTxt.setVisibility(View.VISIBLE);
                exchangeCouponTxt.setText("");
                return;
            }

            if (mInterface != null) {
                mInterface.tapExchange(exchangeCouponTxt.getText().toString());
            }
        });
        exchangeCouponTxt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public void setError(boolean show, String msg) {
        if (show) {
            exchangeCouponErrMsgTxt.setVisibility(View.VISIBLE);
            exchangeCouponErrMsgTxt.setText(msg);
        } else {
            exchangeCouponErrMsgTxt.setVisibility(View.GONE);
        }
    }

    public void setExchangeSuccess(String title, String subTitle, String date) {

        titleTxt.setText("兑换成功");
        exchangeCouponTxt.setVisibility(View.GONE);
        exchangeCouponLayout.setVisibility(View.VISIBLE);
        exchangeCouponTitle.setText(title);
        exchangeCouponSubTitle.setText(subTitle);
        exchangeCouponDate.setText(date);
        exchangeCouponBtn.setText("继续兑换");
        continueExchange = true;
    }

    public void setContinue() {
        exchangeCouponLayout.setVisibility(View.GONE);
        exchangeCouponTxt.setVisibility(View.VISIBLE);
    }

    public void setInterface(ExchangeCouponPopuWindowInterface anInterface) {
        mInterface = anInterface;
    }

    public interface ExchangeCouponPopuWindowInterface {
        void tapExchange(String code);
    }
}
