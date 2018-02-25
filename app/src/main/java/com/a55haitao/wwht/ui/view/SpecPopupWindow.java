package com.a55haitao.wwht.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ProductDetailBean;
import com.a55haitao.wwht.utils.DisplayUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.SpecTranslateUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;
import com.orhanobut.logger.Logger;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haotao_Fujie on 16/10/20.
 */

public class SpecPopupWindow extends PopupWindow {

    @BindView(R.id.buttonOK)        Button       buttonOK;
    @BindView(R.id.specMain)        LinearLayout specMain;
    @BindView(R.id.specZoneLinear)  LinearLayout specZoneLinear;
    @BindView(R.id.priceTxt)        TextView     priceTxt;
    @BindView(R.id.selectedTxt)     TextView     selectedTxt;
    @BindView(R.id.imageCover)      ImageView    imageCover;
    @BindView(R.id.clearZoneLinear) View         clearZoneLinear;
    @BindView(R.id.productRTImage)  ImageView    productRTImage;
    @BindView(R.id.productRTLin)    LinearLayout productRTLin;
    @BindView(R.id.singleLin)       LinearLayout singleLin;

    private Context                 mContext;
    private View                    contentView;
    private View                    parentView;
    private SpecPopuWindowInterface specListener;
    private LayoutInflater          mInflater;

    private ProductDetailBean mProductDetailBean;

    private int STATE_DISABLE    = -1;
    private int STATE_SELECTED   = 1;
    private int STATE_UNSELECTED = 0;

    private boolean fromBuyButton = false;
    private String coverCach;

    public SpecPopupWindow(final Activity context, int width, int height, ProductDetailBean mProductDetailBean) {

        mContext = context;
        this.mProductDetailBean = mProductDetailBean;

        // 初始化UI
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = mInflater.inflate(R.layout.spec_selection_view, null);
        ButterKnife.bind(this, contentView);

        this.setContentView(contentView);

        if (width < 1) {
            this.setWidth(DisplayUtils.getScreenWidth(context));
        } else {
            this.setWidth(width);
        }

        if (height < 1) {
            this.setHeight(DisplayUtils.getScreenHeight(context));
        } else {
            this.setHeight(height);
        }

        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.update();

        // 设置动画
        // this.setAnimationStyle(R.style.AnimationSpecPopuWindow);
        renderUI(context);
    }

    public void setSpecListener(SpecPopuWindowInterface specListener) {
        this.specListener = specListener;
    }

    public void setFromBuyButton(boolean fromBuyButton) {
        this.fromBuyButton = fromBuyButton;
    }

    public void showOrDismiss(View parent) {

        if (parent == null) return;
        parentView = parent;

        if (!this.isShowing()) {

            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        } else {

            this.dismiss();
        }
    }

    // 构建UI
    private void renderUI(Context context) {

        // 设置"OK"按钮监听
        buttonOK.setOnClickListener(v -> {
            if (specListener != null && mProductDetailBean.getInProductRTing() == false && !mProductDetailBean.isSoldOut()) {
                if (fromBuyButton) {
                    specListener.onClickPopuOKFromBuying(getSelectedSkuid());
                } else {
                    specListener.onClickPopuOKFromAddingCart(getSelectedSkuid());
                }
            }
        });

        // 设置"点击"Cover的监听
        imageCover.setOnClickListener(v -> {
            if (specListener != null) {
                String cover = currentCover();
                specListener.onClickCover(cover == null ? coverCach : cover);
            }
        });

        // 点击透明区域
        clearZoneLinear.setOnClickListener(v -> showOrDismiss(parentView));

        // Remove UI
        if (specZoneLinear.getChildCount() > 0) {
            specZoneLinear.removeAllViews();
        }

        // Add UI
        Iterator iter = mProductDetailBean.cache_allSpecs.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry    entry      = (Map.Entry) iter.next();
            String       specName   = (String) entry.getKey();
            List<String> specValues = (List<String>) entry.getValue();

            try {

                // 加载布局
                LinearLayout specSelectionZone = (LinearLayout) mInflater.inflate(R.layout.spec_selection_zone, null);
                if (isSpecNameEqualStyle(specName) == true) {
                    specZoneLinear.addView(specSelectionZone, 0);
                } else {
                    specZoneLinear.addView(specSelectionZone);
                }
                // 设置Spec Name
                LinearLayout specNameLinear = (LinearLayout) specSelectionZone.getChildAt(0);
                TextView     specNameTxt    = (TextView) specNameLinear.getChildAt(0);
                specNameTxt.setText(SpecTranslateUtils.translateToChinese(specName));

                // 是否显示"尺码对照"
                if (specName.toLowerCase().equals("size")) {
                    TextView specCompare = (TextView) specNameLinear.getChildAt(1);
                    specCompare.setVisibility(View.VISIBLE);
                    specCompare.setOnClickListener(v -> {
                        if (specListener != null) {
                            specListener.onClickSpecCompareInfo();
                        }
                    });
                }

                // 设置Spec Values
                TagFlowLayout specValueFlowlayout = (TagFlowLayout) specSelectionZone.getChildAt(1);
                specSelectionZone.setTag(specName);
                specValueFlowlayout.setTag(specName);
                specValueFlowlayout.setAdapter(new TagAdapter<String>(specValues) {
                    @Override
                    public View getView(FlowLayout parent, int position, String specValue) {

                        boolean isStyle = isStyle(specName);
                        if (isStyle) {
                            // 图文
                            LinearLayout lin      = (LinearLayout) mInflater.inflate(R.layout.spec_textview_w_image, specValueFlowlayout, false);
                            ImageView    image    = (ImageView) ((LinearLayout) lin.getChildAt(0)).getChildAt(0);
                            String       imageUrl = getStyleImg(specValue);
                            UPaiYunLoadManager.loadImage(mContext, imageUrl, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, image);

                            TextView tv = (TextView) lin.getChildAt(1);
                            tv.setText(specValue);
                            lin.setTag(STATE_UNSELECTED);
                            return lin;
                        } else {
                            // 文字
                            TextView txt = (TextView) mInflater.inflate(R.layout.spec_textview, specValueFlowlayout, false);
                            txt.setText(specValue);
                            txt.setTag(STATE_UNSELECTED);
                            return txt;
                        }
                    }
                });

                specValueFlowlayout.setOnTagClickListener((view, position, parent) -> {

                    String specName1 = (String) parent.getTag();
                    if (view instanceof LinearLayout) {
                        // 图文按钮
                        LinearLayout image_text = (LinearLayout) view;

                        int tag = (int) image_text.getTag();
                        if (tag == STATE_SELECTED) {
                            setTagViewState(context, view, STATE_SELECTED);
                            mProductDetailBean.cache_selectedSpecs.put(specName1, "");
                        } else if (tag == STATE_UNSELECTED) {
                            setTagViewState(context, view, STATE_SELECTED);
                            TextView txt   = (TextView) image_text.getChildAt(1);
                            String   value = txt.getText().toString();
                            mProductDetailBean.cache_selectedSpecs.put(specName1, value);
                        } else {
                            return false;
                        }

                    } else if (view instanceof TextView) {
                        // 文字按钮
                        TextView txt = (TextView) view;
                        int      tag = (int) txt.getTag();
                        if (tag == STATE_SELECTED) {
                            setTagViewState(context, view, STATE_SELECTED);
                            mProductDetailBean.cache_selectedSpecs.put(specName1, "");
                        } else if (tag == STATE_UNSELECTED) {
                            setTagViewState(context, view, STATE_SELECTED);
                            String value = txt.getText().toString();
                            mProductDetailBean.cache_selectedSpecs.put(specName1, value);
                        } else {
                            return false;
                        }
                    }
                    // 刷新规则状态
                    freshUI(context);
                    return true;
                });

            } catch (Exception e) {
                Logger.d("SpecPopupWindow", TextUtils.isEmpty(e.getMessage()) ? "" : e.getMessage());
            } finally {

            }
        }

        // 初始化规则状态
        freshUI(context);

        // 判断是否在核价状态
        if (mProductDetailBean.getInProductRTing() == true) {
            Animation          circleAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_roation_orage_cirlce);
            LinearInterpolator linear          = new LinearInterpolator();
            circleAnimation.setInterpolator(linear);
            if (circleAnimation != null) {
                productRTImage.startAnimation(circleAnimation);
            }

            buttonOK.setText("更新价格中...");
            buttonOK.setBackgroundResource(R.drawable.shape_btn_gray);
            EventBus.getDefault().register(this);

        } else {
            productRTImage.clearAnimation();
            productRTLin.setVisibility(View.GONE);

            if (!mProductDetailBean.isSoldOut()) {
                buttonOK.setText("确定");
                buttonOK.setBackgroundResource(R.drawable.shape_btn_black);
            } else {
                buttonOK.setText("暂时缺货");
                buttonOK.setBackgroundResource(R.drawable.shape_btn_gray);
            }
        }
    }

    // 刷新规则状态
    private void freshUI(Context context) {

        // 商品名称、价格和已选信息
        // 获取可用的规格
        priceTxt.setText(currentPrice());
        selectedTxt.setText(currentSpannableSpecs(), TextView.BufferType.SPANNABLE);
        String cover = currentCover();
        if (cover != null) {
            coverCach = cover;
            UPaiYunLoadManager.loadImage(mContext, cover, UpaiPictureLevel.SINGLE, R.mipmap.ic_default_square_large, imageCover);
        }

        HashMap<String, ArrayList<String>> allSpecs      = getAvailableSpecs();
        HashMap<String, String>            selectedSpecs = mProductDetailBean.cache_selectedSpecs;

        for (int i = 0; i < specZoneLinear.getChildCount(); i++) {

            // 循环 spec zone
            LinearLayout specSelectionZone = (LinearLayout) specZoneLinear.getChildAt(i);
            // 当前spec zone的spec名称
            String selectedName = specSelectionZone.getTag().toString();
            // 当前spec zone的已选择的值
            String selectedValue = selectedSpecs.get(selectedName);

            ArrayList<String> availableValues = null;
            for (String eachKey : allSpecs.keySet()) {
                // 找到spec name
                if (eachKey.equals(selectedName)) {
                    // 当前可以点击的值的集合
                    availableValues = allSpecs.get(eachKey);
                    break;
                }
            }

            // 循环每个spec zone中的TagView
            TagFlowLayout specValueFlowlayout = (TagFlowLayout) specSelectionZone.getChildAt(1);
            for (int j = 0; j < specValueFlowlayout.getChildCount(); j++) {
                TagView tagView = (TagView) specValueFlowlayout.getChildAt(j);

                // 每个TagView的UI控件
                View view = tagView.getChildAt(0);

                // 每个控件的值（规格对应的每个值）
                String value = null;
                if (view instanceof LinearLayout) {
                    // 图文按钮
                    LinearLayout image_text = (LinearLayout) view;
                    TextView     txt        = (TextView) image_text.getChildAt(1);
                    value = txt.getText().toString();
                } else if (view instanceof TextView) {
                    // 文字按钮
                    TextView txt = (TextView) view;
                    value = txt.getText().toString();
                }

                // 设置TagView状态
                if (availableValues != null && availableValues.contains(value)) {
                    // 当前TagView在可点击值集合中
                    if (selectedValue.equals(value)) {
                        // 当前TagView是已选值
                        setTagViewState(context, view, STATE_SELECTED);
                    } else {
                        // 当前TagView是非选值
                        setTagViewState(context, view, STATE_UNSELECTED);
                    }
                } else {
                    // 当前TagView不在可点击值集合中
                    setTagViewState(context, view, STATE_DISABLE);
                }
            }
        }

    }

    // 设置TagView的状态
    private void setTagViewState(Context context, View view, int state) {

        if (state == STATE_DISABLE) {
            view.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_btn_spec_disabed));
            view.setTag(STATE_DISABLE);

            if (view instanceof LinearLayout) {
                LinearLayout image_text = (LinearLayout) view;
                TextView     tv         = (TextView) image_text.getChildAt(1);
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorGrayCCCCCC));
            } else if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorGrayCCCCCC));
            }

        } else {

            if (state == STATE_SELECTED) {
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_btn_spec_select));
                view.setTag(STATE_SELECTED);

            } else if (state == STATE_UNSELECTED) {
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_btn_spec_unselect));
                view.setTag(STATE_UNSELECTED);
            }

            if (view instanceof LinearLayout) {
                LinearLayout image_text = (LinearLayout) view;
                TextView     tv         = (TextView) image_text.getChildAt(1);
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorGray26241F));
            } else if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorGray26241F));
            }
        }
    }

    public interface SpecPopuWindowInterface {
        void onClickPopuOKFromBuying(String skuid);

        void onClickPopuOKFromAddingCart(String skuid);

        void onClickSpecCompareInfo();

        void onClickCover(String cover);
    }

    // 判断spec name是否是style name
    public boolean isSpecNameEqualStyle(String specName) {
        if (mProductDetailBean != null && mProductDetailBean.skuInfo != null && mProductDetailBean.skuInfo.style != null && !TextUtils.isEmpty(mProductDetailBean.skuInfo.style.name) && mProductDetailBean.skuInfo.style.name.equals(specName)) {
            return true;
        }
        return false;
    }

    // 是否是Style
    public boolean isStyle(String specName) {
        if (mProductDetailBean != null && mProductDetailBean.skuInfo != null && mProductDetailBean.skuInfo.style != null) {
            if (mProductDetailBean.skuInfo.style.name.equals(specName)) {
                return true;
            }
        }
        return false;
    }

    // 获取Style Image
    public String getStyleImg(String specValue) {
        if (mProductDetailBean != null && mProductDetailBean.skuInfo != null && mProductDetailBean.skuInfo.style != null && mProductDetailBean.skuInfo.style.skustylelist != null && mProductDetailBean.skuInfo.style.skustylelist.size() > 0) {
            for (ProductDetailBean.StyleListData styleListData : mProductDetailBean.skuInfo.style.skustylelist) {
                if (styleListData.value.equals(specValue)) {

                    return styleListData.imgCover;

                }
            }
        }
        return null;
    }

    // 返回可选的其它的spec名称和spec的值

    // 按spec名称作为sql条件查询其它未选的spec和spec value
    // 比如 select distinct[未选spec名称] where [已选字段] = [已选字段Value] AND [STOCK] == 1   // 1代表有库存，0代表没库存
    // 结果为 "5"、"6"， 那么把[未选spec名称]，["5","6"]添加入返回值队列里

    public HashMap<String, ArrayList<String>> getAvailableSpecs() {
        if (mProductDetailBean._isSingleSpecAndValue == true) {
            return null;
        }
        HashMap<String, ArrayList<String>> ret    = new HashMap<>();
        ArrayList<String>                  values = new ArrayList<>();

        int               maxSpecs          = mProductDetailBean.cache_allSpecs.size();  // 所有的spec
        int               selectedSpecs     = 0;
        ArrayList<String> selectedKeys      = new ArrayList<>();
        ArrayList<String> selectedValues    = new ArrayList<>();
        Set<String>       sets              = mProductDetailBean.cache_selectedSpecs.keySet();
        ArrayList<String> cacheSelectedKeys = new ArrayList<>(sets);
        for (String seleSpec : mProductDetailBean.cache_selectedSpecs.keySet()) {

            String specName = mProductDetailBean.cache_selectedSpecs.get(seleSpec);

            if (!TextUtils.isEmpty(specName)) {
                selectedSpecs++;
                selectedKeys.add(seleSpec);
                selectedValues.add(specName);
            }

        }

        // 没有已选项
        if (selectedSpecs == 0) {
            for (String spec : cacheSelectedKeys) {
                int position = 0;

                for (ProductDetailBean.AssistSpecItem assistSpecItem : mProductDetailBean.specs) {
                    boolean is = true;
                    if (assistSpecItem.stock < 0) {
                        is = false;
                    }
                    if (is) {
                        for (int j = 0; j < assistSpecItem.specs.size(); j++) {

                            if (spec.equals(assistSpecItem.specs.get(j))) {
                                position = j;
                            }
                        }
                        values.add(assistSpecItem.values.get(position));
                    }
                }

                ret.put(spec, values);

            }

        } else {

            for (String spec : mProductDetailBean.cache_selectedSpecs.keySet()) {//  所有的spec的key
                int position = 0;
                for (ProductDetailBean.AssistSpecItem assistSpecItem : mProductDetailBean.specs) {
                    // 要除开本身以外的已选条件
                    boolean is = true;
                    for (int i = 0; i < selectedSpecs; i++) {
                        if (!selectedKeys.get(i).equals(spec)) { // 不包含本身
                            for (int j = 0; j < assistSpecItem.specs.size(); j++) {
                                if (selectedKeys.get(i).equals(assistSpecItem.specs.get(j))) {
                                    if (!selectedValues.get(i).equals(assistSpecItem.values.get(j))) {
                                        is = false;
                                    } else if (assistSpecItem.stock <= 0) {
                                        is = false;
                                    }
                                }
                            }
                        }
                    }

                    if (is) {
                        for (int j = 0; j < assistSpecItem.specs.size(); j++) {

                            if (spec.equals(assistSpecItem.specs.get(j))) {
                                position = j;
                            }
                        }
                        values.add(assistSpecItem.values.get(position));
                    }

                }

                ret.put(spec, values);

            }
        }

        return ret;
    }

    // 当前的商品价格
    public String currentPrice() {
        if (mProductDetailBean._isSingleSpecAndValue == true) {
            return PriceUtils.toRMBFromFen(mProductDetailBean.realPrice);
        } else {
            String skuid = getSelectedSkuid();
            if (TextUtils.isEmpty(skuid)) {
                return PriceUtils.toRMBFromFen(mProductDetailBean.realPrice);
            } else {
                if (mProductDetailBean != null && mProductDetailBean.skuInfo != null && mProductDetailBean.skuInfo.select != null && mProductDetailBean.skuInfo.select.size() > 0) {
                    for (ProductDetailBean.SelectData selectData : mProductDetailBean.skuInfo.select) {
                        if (selectData.skuid.equals(skuid)) {
                            return PriceUtils.toRMBFromFen(selectData.realPrice);
                        }
                    }
                }
            }
        }
        return null;
    }

    // 当前已选择的规格
    public String currentSpecs() {
        String ret = null;
        if (mProductDetailBean._isSingleSpecAndValue) {
            ret = "已选择:均码,";
        } else {
            ret = "";
            Set<String>       keySet  = mProductDetailBean.cache_allSpecs.keySet();
            ArrayList<String> keyList = new ArrayList<>(keySet);
            for (String specName : keyList) {
                boolean  find = false;
                Iterator iter = mProductDetailBean.cache_selectedSpecs.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry            = (Map.Entry) iter.next();
                    String    selectedSpecName = (String) entry.getKey();

                    if (selectedSpecName.equals(specName)) {
                        Object value = entry.getValue();
                        if (value instanceof String) {
                            // 已选择规格值
                            String specValue = (String) value;
                            if (TextUtils.isEmpty(specValue) == false) {
                                if (isSpecNameEqualStyle(specName)) {
                                    ret = "\"" + specValue + "\"," + ret;
                                } else {
                                    ret += "\"" + specValue + "\",";
                                }
                                find = true;
                                break;
                            }
                        }
                    }
                }

                if (find == false) {
                    if (isSpecNameEqualStyle(specName)) {
                        ret = "\"" + SpecTranslateUtils.translateToChinese(specName) + "\"," + ret;
                    } else {
                        ret += "\"" + SpecTranslateUtils.translateToChinese(specName) + "\",";
                    }
                }
            }
        }
        ret = "已选择:" + ret;
        return ret.substring(0, ret.length() - 1);

    }

    // 当前已选择的规格
    public SpannableString currentSpannableSpecs() {
        String          ret             = currentSpecs();
        SpannableString spannableString = new SpannableString(ret);

        if (ret.contains("已选择:均码")) {
            spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.SpecDescription333), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.SpecDescription333), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.SpecDescription999), 4, ret.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            for (String selectedValue : mProductDetailBean.cache_selectedSpecs.values()) {

                if (TextUtils.isEmpty(selectedValue)) continue;

                selectedValue = "\"" + selectedValue + "\"";
                int start = ret.indexOf(selectedValue);
                int end   = start + selectedValue.length();
                if (start >= 0 && end <= ret.length()) {
                    spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.SpecDescriptionRed), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spannableString;
    }

    // 获取已选规格的skuid
    public String getSelectedSkuid() {

        if (mProductDetailBean._isSingleSpecAndValue == true) {
            return TextUtils.isEmpty(mProductDetailBean.skuid) ? mProductDetailBean.defaultSkuid : mProductDetailBean.skuid;
        }

        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> keys   = new ArrayList<>();
        // 判断cache_selectedSpecs是否全部选中spec
        for (String key : mProductDetailBean.cache_selectedSpecs.keySet()) {
            if (TextUtils.isEmpty(mProductDetailBean.cache_selectedSpecs.get(key))) {
                // 未全选中规格
                return null;
            } else {
                keys.add(key);
                values.add(mProductDetailBean.cache_selectedSpecs.get(key));
            }
        }
        for (ProductDetailBean.AssistSpecItem item : mProductDetailBean.specs) {
            boolean isSame = true;
            for (int i = 0; i < values.size(); i++) {

                for (int j = 0; j < item.values.size(); j++) {
                    if (keys.get(i).equals(item.specs.get(j))) {
                        if (!values.get(i).equals(item.values.get(j))) {
                            isSame = false;
                        }
                    }
                }
            }
            if (isSame)
                return item.skuid;

        }
        return null;
    }

    // 当前的商品图片
    public String currentCover() {
        if (mProductDetailBean._isSingleSpecAndValue == true) {
            return mProductDetailBean.coverImgUrl;
        } else {
            String skuid = getSelectedSkuid();
            if (!TextUtils.isEmpty(skuid)) {
                if (mProductDetailBean != null && mProductDetailBean.skuInfo != null && mProductDetailBean.skuInfo.select != null && mProductDetailBean.skuInfo.select.size() > 0) {
                    for (ProductDetailBean.SelectData selectData : mProductDetailBean.skuInfo.select) {
                        if (selectData.skuid.equals(skuid)) {
                            String style = selectData.style;
                            if (mProductDetailBean.skuInfo.style != null && mProductDetailBean.skuInfo.style.skustylelist != null && mProductDetailBean.skuInfo.style.skustylelist.size() > 0) {
                                for (ProductDetailBean.StyleListData styleListData : mProductDetailBean.skuInfo.style.skustylelist) {
                                    if (style.equals(styleListData.value)) {
                                        return styleListData.imgCover;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
            if (mProductDetailBean != null) {

                if (mProductDetailBean.skuInfo != null && mProductDetailBean.skuInfo.style != null && mProductDetailBean.skuInfo.style.skustylelist != null && mProductDetailBean.skuInfo.style.skustylelist.size() > 0) {

                    String   coverImg = null;
                    Iterator iter     = mProductDetailBean.cache_selectedSpecs.entrySet().iterator();
                    while (iter.hasNext()) {

                        Map.Entry entry = (Map.Entry) iter.next();
                        String    key   = (String) entry.getKey();
                        if (key.equals(mProductDetailBean.skuInfo.style.name)) {

                            Object value = entry.getValue();
                            if (value != null && value instanceof String && !TextUtils.isEmpty((String) value)) {

                                for (ProductDetailBean.StyleListData styleListData : mProductDetailBean.skuInfo.style.skustylelist) {
                                    if (styleListData.value.equals((String) value)) {

                                        coverImg = styleListData.imgCover;
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }

                    if (!TextUtils.isEmpty(coverImg)) {
                        return coverImg;
                    }

                } else {
                    return mProductDetailBean.coverImgUrl;
                }
            }

        }

        return null;

    }
}
