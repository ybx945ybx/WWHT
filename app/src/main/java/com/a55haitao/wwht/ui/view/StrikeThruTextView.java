package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by 董猛 on 2016/10/17.
 */

public class StrikeThruTextView extends HaiTextView{
    public StrikeThruTextView(Context context) {
        super(context);
        init();
    }

    public StrikeThruTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        getPaint().setAntiAlias(true);
    }
}
