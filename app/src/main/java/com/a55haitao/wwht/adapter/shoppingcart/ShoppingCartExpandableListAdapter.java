package com.a55haitao.wwht.adapter.shoppingcart;

import android.app.Activity;
import android.content.Intent;
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
import com.a55haitao.wwht.data.constant.ApiUrl;
import com.a55haitao.wwht.data.model.annotation.UpaiPictureLevel;
import com.a55haitao.wwht.data.model.entity.ShoppingCartBean;
import com.a55haitao.wwht.ui.activity.product.ProductsListActivity;
import com.a55haitao.wwht.ui.view.HaiTextView;
import com.a55haitao.wwht.utils.HaiUtils;
import com.a55haitao.wwht.utils.PriceUtils;
import com.a55haitao.wwht.utils.glide.UPaiYunLoadManager;

import java.util.ArrayList;

import static com.a55haitao.wwht.R.id.headerSellerTxt;

/**
 * 购物车适配器
 * Created by Haotao_Fujie on 2016/10/31.
 */

public class ShoppingCartExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity                     mActivity;
    private ShoppingCartBean             mShoppingData;
    private OnShoppingCartChangeListener mChangeListener;

    public ShoppingCartExpandableListAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setShoppingData(ShoppingCartBean data) {
        this.mShoppingData = data;
    }

    public void setOnShoppingCartChangeListener(OnShoppingCartChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    @Override
    public int getGroupCount() {
        if (mShoppingData == null || mShoppingData.data == null) {
            return 0;
        } else {
            return mShoppingData.data.size();
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mShoppingData == null || mShoppingData.data == null) {
            return 0;
        } else {
            ShoppingCartBean.CartListStoreData cartListStoreData = mShoppingData.data.get(groupPosition);
            if (cartListStoreData == null || cartListStoreData.cart_list == null) {
                return 0;
            } else {
                return cartListStoreData.cart_list.size();
            }
        }
    }

    @Override
    public Object getGroup(int groupPosition) {

        if (mShoppingData == null || mShoppingData.data == null) {
            return null;
        } else {
            return mShoppingData.data.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        if (mShoppingData == null || mShoppingData.data == null) {
            return null;
        } else {
            ShoppingCartBean.CartListStoreData cartListStoreData = mShoppingData.data.get(groupPosition);
            if (cartListStoreData == null || cartListStoreData.cart_list == null) {
                return null;
            } else {
                return cartListStoreData.cart_list.get(childPosition);
            }
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
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
            holder.cutoffTxt = (TextView) convertView.findViewById(R.id.cutoffTxt);
            holder.cutoffButton = (TextView) convertView.findViewById(R.id.cutoffButton);
            holder.tvcutoffMeet = (HaiTextView) convertView.findViewById(R.id.tvTransferFeeMeet);
            convertView.setTag(holder);

            // Header View事件
            GroupViewHolder finalHolder = holder;
            holder.headerSellerTxt.setOnClickListener(v -> {
                if (mChangeListener != null) {
                    GroupData data = finalHolder.data;
                    mChangeListener.onClicked(true, data.storeid, null, null);
                }
            });
            if (holder.cutoffLin != null) {
                holder.cutoffLin.setOnClickListener(view -> {
                    if (mChangeListener != null) {
                        GroupData data = finalHolder.data;
                        mChangeListener.onClicked(true, data.storeid, null, null);
                    }
                });
            }

            holder.headerCheckBox.setOnClickListener(v -> {
                if (v instanceof CompoundButton) {
                    CompoundButton compoundButton = (CompoundButton) v;
                    boolean        isChecked      = compoundButton.isChecked();
                    if (mChangeListener != null) {
                        GroupData data = finalHolder.data;
                        mChangeListener.onCheckBoxChanged(true, data.storeid, null, isChecked);
                    }
                }
            });
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        // render UI
        // HeaderView
        ShoppingCartBean.CartListStoreData cartListStoreData      = mShoppingData.data.get(groupPosition);
        boolean                            sellerProductsSelected = cartListStoreData.getSellerProductsSelected();
        holder.headerCheckBox.setChecked(sellerProductsSelected);
        int flagResourceId = HaiUtils.getFlagResourceId(cartListStoreData.country.regionName, false);
        if (flagResourceId != -1) {
            holder.headerFlagImage.setImageResource(flagResourceId);
            holder.headerFlagImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            UPaiYunLoadManager.loadImage(mActivity, cartListStoreData.country.regionImgUrl, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.headerFlagImage);
        }
        holder.headerSellerTxt.setText(cartListStoreData.getStoreName());

        // FullCutoff 满减运费
        if (cartListStoreData.getStoreFullCutoff() == true) {
            holder.cutoffLin.setVisibility(View.VISIBLE);
            holder.cutoffTxt.setText(cartListStoreData.getStoreFullCutoffTitle());
            if (cartListStoreData.getStoreFullCutoffRemaing() > 0) {
                holder.tvcutoffMeet.setVisibility(View.GONE);
                holder.cutoffButton.setVisibility(View.VISIBLE);
                holder.cutoffButton.setText("再买" + ApiUrl.RMB_UNICODE + cartListStoreData.getStoreFullCutoffRemaing() + "，去凑单");
            } else {
                holder.cutoffButton.setVisibility(View.GONE);
                holder.tvcutoffMeet.setVisibility(View.VISIBLE);
            }
        } else {
            holder.cutoffLin.setVisibility(View.GONE);
        }
        holder.data = new GroupData("" + cartListStoreData.store_id);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewItemHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewItemHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.shopping_cart_item_view, parent, false);
            holder.tvActivityType = (HaiTextView) convertView.findViewById(R.id.tv_activity_type);
            holder.llytActivityCutoff = (LinearLayout) convertView.findViewById(R.id.llyt_activity_cutoff);
            holder.cutoffTxt = (TextView) convertView.findViewById(R.id.cutoffTxt);
            holder.cutoffButton = (TextView) convertView.findViewById(R.id.cutoffButton);
            holder.itemCheckBox = (CheckBox) convertView.findViewById(R.id.itemCheckBox);
            holder.itemCheckBox.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorClear));
            holder.itemInvalid = (TextView) convertView.findViewById(R.id.itemInvalid);
            holder.itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
            holder.itemTitleTxt = (TextView) convertView.findViewById(R.id.itemTitleTxt);
            holder.itemPriceTxt = (TextView) convertView.findViewById(R.id.itemPriceTxt);
            holder.seperateLine = (View) convertView.findViewById(R.id.seperateLine);
            holder.seperateLineLong = (View) convertView.findViewById(R.id.seperateLineLong);
            holder.itemLin = (LinearLayout) convertView.findViewById(R.id.itemLin);
            convertView.setTag(holder);

            // Item View事件
            ChildViewItemHolder finalHolder = holder;

            holder.itemLin.setOnClickListener(view -> {
                if (mChangeListener != null) {
                    ChildData data = finalHolder.data;
                    mChangeListener.onClicked(false, null, data.spuid, data.name);
                }
            });

            holder.itemCheckBox.setOnClickListener(v -> {
                if (v instanceof CompoundButton) {
                    CompoundButton compoundButton = (CompoundButton) v;
                    boolean        isChecked      = compoundButton.isChecked();
                    if (mChangeListener != null) {
                        ChildData data = finalHolder.data;
                        mChangeListener.onCheckBoxChanged(false, null, data.cartid, isChecked);
                    }
                }
            });

        } else {
            holder = (ChildViewItemHolder) convertView.getTag();
        }

        // render UI
        ShoppingCartBean.Cartdata cartdata = mShoppingData.data.get(groupPosition).cart_list.get(childPosition);
        if (cartdata.product.hasGroundingAndStock()) {
            // 有效
            holder.itemInvalid.setVisibility(View.INVISIBLE);
            holder.itemCheckBox.setVisibility(View.VISIBLE);

        } else {
            // 无效
            holder.itemInvalid.setVisibility(View.VISIBLE);
            holder.itemCheckBox.setVisibility(View.INVISIBLE);
        }
        // 活动优惠
        if (cartdata.showfullMinusTitle) {
            holder.seperateLineLong.setVisibility(View.VISIBLE);
            holder.seperateLine.setVisibility(View.GONE);
            if (cartdata.fullMinus != null) {
                setActivityType(holder.tvActivityType, cartdata.fullMinus.rtype);
                holder.llytActivityCutoff.setVisibility(View.VISIBLE);
                holder.llytActivityCutoff.setOnClickListener(v -> {
                    Intent intent = new Intent(mActivity, ProductsListActivity.class);
                    intent.putExtra("title", cartdata.fullMinus.title);
                    intent.putExtra("fid", cartdata.fullMinus.fid);
                    mActivity.startActivity(intent);
                });

                boolean meet = false;

                float  minCutoff = cartdata.fullMinus.cut_list.get(0).full;
                String minTitle  = cartdata.fullMinus.cut_list.get(0).title;

                float  maxCutoff = 0;
                String maxTitle  = "";

                switch (cartdata.fullMinus.rtype) {  // 1,2 满钱满减  3,4 满件
                    case 1:
                    case 2:
                        float activitiesCountAmount = getActivitiesCountAmount(mShoppingData.data.get(groupPosition).cart_list, cartdata.fullMinus.fid);

                        for (ShoppingCartBean.CutData cutdata : cartdata.fullMinus.cut_list) {
                            if (minCutoff > cutdata.full) {
                                minCutoff = cutdata.full;
                                minTitle = cutdata.title;
                            }

                            if (activitiesCountAmount >= cutdata.full) {
                                if (cutdata.equals(cartdata.fullMinus.cut_list.get(0))) {
                                    maxCutoff = cutdata.full;
                                    maxTitle = cutdata.title;
                                } else {
                                    if (maxCutoff < cutdata.full) {
                                        maxCutoff = cutdata.full;
                                        maxTitle = cutdata.title;
                                    }
                                }

                                meet = true;
                            }

                        }

                        if (!meet) {
                            holder.cutoffTxt.setText(minTitle);
                            holder.cutoffButton.setText("再买￥" + (int) (minCutoff - activitiesCountAmount) + "，去凑单");
                        } else {
                            holder.cutoffTxt.setText(maxTitle);
                            holder.cutoffButton.setText("已满足，再逛逛");
                        }

                        break;
                    case 3:
                    case 4:
                        int activitiesProductCount = getActivitiesProductCount(mShoppingData.data.get(groupPosition).cart_list, cartdata.fullMinus.fid);

                        for (ShoppingCartBean.CutData cutdata : cartdata.fullMinus.cut_list) {
                            if (minCutoff > cutdata.full) {
                                minCutoff = cutdata.full;
                                minTitle = cutdata.title;
                            }

                            if (activitiesProductCount >= cutdata.full) {
                                if (cutdata.equals(cartdata.fullMinus.cut_list.get(0))) {
                                    maxCutoff = cutdata.full;
                                    maxTitle = cutdata.title;
                                } else {
                                    if (maxCutoff < cutdata.full) {
                                        maxCutoff = cutdata.full;
                                        maxTitle = cutdata.title;
                                    }
                                }
                                meet = true;
                            }

                        }

                        if (!meet) {
                            holder.cutoffTxt.setText(minTitle);
                            holder.cutoffButton.setText("再买" + (int) (minCutoff - activitiesProductCount) + "件，去凑单");
                        } else {
                            holder.cutoffTxt.setText(maxTitle);
                            holder.cutoffButton.setText("已满足，再逛逛");
                        }

                        break;
                }
            } else {
                holder.llytActivityCutoff.setVisibility(View.GONE);
            }
        } else {
            if (childPosition == 0) {
                holder.seperateLine.setVisibility(View.GONE);
                holder.seperateLineLong.setVisibility(View.VISIBLE);
            } else {
                holder.seperateLineLong.setVisibility(View.GONE);
                holder.seperateLine.setVisibility(View.VISIBLE);
            }

            holder.llytActivityCutoff.setVisibility(View.GONE);

        }
        boolean productSelected = cartdata.getProductSelected();
        holder.itemCheckBox.setChecked(productSelected);
        UPaiYunLoadManager.loadImage(mActivity, cartdata.product.coverImgUrl, UpaiPictureLevel.FOURTH, R.mipmap.ic_default_square_tiny, holder.itemImage);
        String itemTitle = cartdata.product.product_name + "\n\n" + cartdata.product.selectedSpecsDescription();
        holder.itemTitleTxt.setText(itemTitle);
        String itemPrice = PriceUtils.toRMBFromYuan(cartdata.product.helper_getRealPrice()) + "\nX" + cartdata.count;
        holder.itemPriceTxt.setText(itemPrice);

        holder.data = new ChildData(cartdata.product.spuid, cartdata.product.skuid, "" + cartdata.cart_id, cartdata.product.product_name);
        return convertView;
    }

    private void setActivityType(HaiTextView tvActivityType, int rtype) { // type 1/3满减  2/4 打折
        if (rtype == 1 || rtype == 3){
            tvActivityType.setText("满减");
        }else {
            tvActivityType.setText("满折");
        }
    }

    // 获取相关活动的已选商品的总金额
    private float getActivitiesCountAmount(ArrayList<ShoppingCartBean.Cartdata> cartdatas, String fid) {
        float activitiesCountAmount = 0;
        for (ShoppingCartBean.Cartdata cartdata : cartdatas) {
            if (cartdata.fullMinus != null && cartdata.fullMinus.fid.equals(fid) && cartdata.is_select) {
                activitiesCountAmount += cartdata.product.helper_getRealPrice() * cartdata.count;
            }
        }
        return activitiesCountAmount;
    }

    // 获取相关活动的已选商品的总件数
    private int getActivitiesProductCount(ArrayList<ShoppingCartBean.Cartdata> cartdatas, String fid) {
        int activitiesProductCount = 0;
        for (ShoppingCartBean.Cartdata cartdata : cartdatas) {
            if (cartdata.fullMinus != null && cartdata.fullMinus.fid.equals(fid) && cartdata.is_select) {
                activitiesProductCount += cartdata.count;
            }
        }
        return activitiesProductCount;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public interface OnShoppingCartChangeListener {
        // 长按
        void onLongClicked(String cartId);

        // 点击
        void onClicked(boolean headerView, String store_id, String spuid, String name);

        // 勾选Checkbox
        void onCheckBoxChanged(boolean headerView, String store_id, String cart_id, boolean checked);
    }

    class GroupViewHolder {
        CheckBox     headerCheckBox;
        ImageView    headerFlagImage;
        TextView     headerSellerTxt;
        LinearLayout cutoffLin;
        TextView     cutoffTxt;
        TextView     cutoffButton;
        HaiTextView  tvcutoffMeet;
        GroupData    data;
    }

    class ChildViewItemHolder {
        HaiTextView  tvActivityType;
        LinearLayout llytActivityCutoff;
        TextView     cutoffTxt;
        TextView     cutoffButton;

        CheckBox     itemCheckBox;
        TextView     itemInvalid;
        ImageView    itemImage;
        TextView     itemTitleTxt;
        TextView     itemPriceTxt;
        LinearLayout itemLin;
        View         seperateLine;
        View         seperateLineLong;
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
        String cartid;
        String name;

        protected ChildData(String spuid, String skuid, String cartid, String name) {
            this.spuid = spuid;
            this.skuid = skuid;
            this.cartid = cartid;
            this.name = name;
        }
    }
}
