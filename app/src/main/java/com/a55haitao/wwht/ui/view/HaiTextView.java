package com.a55haitao.wwht.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.a55haitao.wwht.R;

/**
 * Created by 董猛 on 2016/10/18.
 */

public class HaiTextView extends android.support.v7.widget.AppCompatTextView {

    private static Typeface sRegular;
    private static Typeface sLight;
    private static Typeface sGotham;

    private final static String Regular = "0";
    private final static String Light   = "1";
    private final static String Gotham  = "2";

    public HaiTextView(Context context) {
        super(context);
        useTypeFace(context, null);
    }

    public HaiTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        useTypeFace(context, attrs);
    }

    private void useTypeFace(Context context, AttributeSet attrs) {
        TypedArray t = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HaiTextView, R.attr.expandTypeface, 0);

        String value = t.getString(R.styleable.HaiTextView_expandTypeface);
        if (TextUtils.isEmpty(value)) {
            setTypeface(sRegular);
        } else if (value.equals(Regular)) {
            setTypeface(sRegular);
        } else if (value.equals(Light)) {
            setTypeface(sLight);
        } else if (value.equals(Gotham)) {
            setTypeface(sGotham);
        }
        t.recycle();
    }

    public static void init(Context context) {
        if (sRegular == null) {
            sRegular = Typeface.createFromAsset(context.getAssets(), "fonts/FZLTH_R_GB.TTF");
        }
        if (sLight == null) {
            sLight = Typeface.createFromAsset(context.getAssets(), "fonts/FZLTH_L_GB.TTF");
        }
        if (sGotham == null) {
            sGotham = Typeface.createFromAsset(context.getAssets(), "fonts/gotham_book.ttf");
        }
    }
}
