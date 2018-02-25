package com.a55haitao.wwht.adapter.shoppingcart;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.result.CanRefundProductListResult;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import static com.a55haitao.wwht.R.id.headerSellerTxt;

/**
 * 申请退款可选择受理商品列表适配器
 * Created by a55 on 2017/6/8.
 */

public class CanRefundProductListAdapter extends BaseExpandableListAdapter {

    private Activity                   mActivity;
    private CanRefundProductListResult canRefundProductData;
    private OnSelectChangeListener     mChangeListener;


    public CanRefundProductListAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setCanRefundProductData(CanRefundProductListResult canRefundProductData) {
        this.canRefundProductData = canRefundProductData;
    }

    public void setmChangeListener(OnSelectChangeListener mChangeListener) {
        this.mChangeListener = mChangeListener;
    }

    @Override
    public int getGroupCount() {
        if (canRefundProductData == null || canRefundProductData.storelist == null) {
            return 0;
        } else {
            return canRefundProductData.storelist.size();
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (canRefundProductData == null || canRefundProductData.storelist == null) {
            return 0;
        } else {
            CanRefundProductListResult.StorelistBean storelistBean = canRefundProductData.storelist.get(groupPosition);
            if (storelistBean == null || storelistBean.productlist == null) {
                return 0;
            } else {
                return storelistBean.productlist.size();
            }
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (canRefundProductData == null || canRefundProductData.storelist == null) {
            return null;
        } else {
            return canRefundProductData.storelist.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (canRefundProductData == null || canRefundProductData.storelist == null) {
            return null;
        } else {
            CanRefundProductListResult.StorelistBean storelistBean = canRefundProductData.storelist.get(groupPosition);
            if (storelistBean == null || storelistBean.productlist == null) {
                return null;
            } else {
                return storelistBean.productlist.get(childPosition);
            }
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.shopping_cart_header_view, parent, false);
            holder.headerCheckBox = (CheckBox) convertView.findViewById(R.id.headerCheckBox);
            holder.headerCheckBox.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorClear));
            holder.headerFlagImage = (ImageView) convertView.findViewById(R.id.headerFlagImage);
            holder.headerSellerTxt = (TextView) convertView.findViewById(headerSellerTxt);
            holder.cutoffLin = (LinearLayout) convertView.findViewById(R.id.cutoffLin);
            convertView.setTag(holder);

            // Header View事件 点击全选该store下的全部商品
            GroupViewHolder finalHolder = holder;
            holder.headerCheckBox.setOnClickListener(v -> {
                if (v instanceof CompoundButton) {
                    CompoundButton compoundButton = (CompoundButton) v;
                    boolean        isChecked      = compoundButton.isChecked();
                    if (mChangeListener != null) {
                        GroupData data = finalHolder.data;
                        mChangeListener.onSelectedChanged(true, data.storeid, null, isChecked);
                    }
                }
            });
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        // render UI
        // HeaderView
        CanRefundProductListResult.StorelistBean storelistBean          = canRefundProductData.storelist.get(groupPosition);
        boolean                                  sellerProductsSelected = storelistBean.getSellerProductsSelected();
        holder.headerCheckBox.setChecked(sellerProductsSelected);
        // 国旗
        int flagResourceId = HaiUtils.getFlagResourceId(storelistBean.country.regionName, false);
        if (flagResourceId != -1) {
            holder.headerFlagImage.setImageResource(flagResourceId);
            holder.headerFlagImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            UPaiYunLoadManager.loadImage(mActivity, storelistBean.country.regionImgUrl, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.headerFlagImage);
        }
        holder.headerSellerTxt.setText(storelistBean.getStoreName());
        holder.cutoffLin.setVisibility(View.GONE);
        holder.data = new GroupData("" + storelistBean.store_id);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewItemHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewItemHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.shopping_cart_item_view, parent, false);
            holder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
            holder.itemCheckBox.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorClear));
            holder.itemInvalid = (TextView) convertView.findViewById(R.id.itemInvalid);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
            holder.itemTitleTxt = (TextView) convertView.findViewById(R.id.itemTitleTxt);
            holder.itemPriceTxt = (TextView) convertView.findViewById(R.id.itemPriceTxt);
            holder.seperateLine = (View) convertView.findViewById(R.id.seperateLine);
            holder.itemLin = (LinearLayout) convertView.findViewById(R.id.itemLin);
            convertView.setTag(holder);

            // Item View事件
            ChildViewItemHolder finalHolder = holder;
            holder.itemLin.setOnClickListener(view -> {
                if (mChangeListener != null) {
                    ChildData data = finalHolder.data;
                    mChangeListener.onSelectedChanged(false, data.storeid, data.skuid, !finalHolder.itemCheckBox.isChecked());

                }
            });
            holder.itemCheckBox.setOnClickListener(v -> {
                if (v instanceof CompoundButton) {
                    CompoundButton compoundButton = (CompoundButton) v;
                    boolean        isChecked      = compoundButton.isChecked();
                    if (mChangeListener != null) {
                        ChildData data = finalHolder.data;
                        mChangeListener.onSelectedChanged(false, data.storeid, data.skuid, isChecked);
                    }
                }
            });

        } else {
            holder = (ChildViewItemHolder) convertView.getTag();
        }

        // render UI
        CanRefundProductListResult.StorelistBean.ProductlistBean productlistBean = canRefundProductData.storelist.get(groupPosition).productlist.get(childPosition);
        holder.itemInvalid.setVisibility(View.INVISIBLE);
        holder.itemCheckBox.setVisibility(View.VISIBLE);
        boolean productSelected = productlistBean.isSelected;
        holder.itemCheckBox.setChecked(productSelected);
        UPaiYunLoadManager.loadImage(mActivity, productlistBean.coverImg_url, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.itemImage);
        String itemTitle = productlistBean.product_name + "\n\n" + productlistBean.selected_sku.selectedSpecsDescription();
        holder.itemTitleTxt.setText(itemTitle);
        String itemPrice = PriceUtils.toRMBFromYuan(productlistBean.selected_sku.realPrice) + "\nX" + productlistBean.count;
        holder.itemPriceTxt.setText(itemPrice);
        if (isLastChild) {
            holder.seperateLine.setVisibility(View.GONE);
        } else {
            holder.seperateLine.setVisibility(View.VISIBLE);
        }
        holder.data = new ChildData(productlistBean.selected_sku.spuid, productlistBean.selected_sku.skuid, "" + canRefundProductData.storelist.get(groupPosition).store_id, productlistBean.product_name);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public interface OnSelectChangeListener {
        // 勾选Checkbox 点击item 都是是选中的效果
        void onSelectedChanged(boolean headerView, String store_id, String skuid, boolean checked);
    }

    class GroupViewHolder {
        CheckBox     headerCheckBox;
        ImageView    headerFlagImage;
        TextView     headerSellerTxt;
        LinearLayout cutoffLin;
        GroupData    data;
    }

    class ChildViewItemHolder {
        CheckBox     itemCheckBox;
        TextView     itemInvalid;
        ImageView    itemImage;
        TextView     itemTitleTxt;
        TextView     itemPriceTxt;
        LinearLayout itemLin;
        View         seperateLine;
        ChildData    data;
    }

    class GroupData {
        String storeid;

        protected GroupData(String storeid) {
            this.storeid = storeid;
        }
    }

    class ChildData {
        String spuid;
        String skuid;
        String storeid;
        String name;

        protected ChildData(String spuid, String skuid, String storeid, String name) {
            this.spuid = spuid;
            this.skuid = skuid;
            this.storeid = storeid;
            this.name = name;
        }
    }
}
