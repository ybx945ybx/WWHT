package com.a55haitao.wwht.data.interfaces;

import android.os.Parcel;
import android.os.Parcelable;

import com.a55haitao.wwht.data.model.entity.AddressItemBean;
import com.a55haitao.wwht.data.model.entity.CouponBean;

import java.util.ArrayList;

/**
 * Created by 董猛 on 16/7/21.
 */
public class OrderModel {

    /**
     * 佣金
     */

    private double commission;

    /**
     * 商品信息.类型
     */
    private CartType type;

    /**
     * 收货地址
     */
    private AddressItemBean addressBean;

    public ArrayList<String> cart_list;

    /**
     * 使用优惠券ID
     */
    private String couponId;

    private ArrayList<CouponBean> couponList;

    public ArrayList<CouponBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(ArrayList<CouponBean> couponList) {
        this.couponList = couponList;
    }

    public String getCouponId() {
        return couponId == null ? "" : couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public AddressItemBean getAddressBean() {
        return addressBean;
    }

    public void setAddressBean(AddressItemBean addressBean) {
        this.addressBean = addressBean;
    }

    public void setCart_list(ArrayList<String> cart_list) {
        this.cart_list = cart_list;
    }

    public CartType getType() {
        return type;
    }

    public void setType(CartType type) {
        this.type = type;
    }

    /**
     * 用于{@link com.a55haitao.wwht.ui.activity.shoppingcart.ShoppingCartActivity}
     * 和{@link com.a55haitao.wwht.ui.activity.product.ProductMainActivity}
     * 跳转到商品详情页传参时使用
     * {@link com.a55haitao.wwht.ui.activity.shoppingcart.ShoppingCartActivity} 调用{@link #CartType(int[])}构造方法
     * {@link com.a55haitao.wwht.ui.activity.product.ProductMainActivity} 调用{@link #CartType(String, String, String)}构造方法
     */
    public static class CartType implements Parcelable {

        /**
         * product详情
         */
        private String spuid;
        private String skuid;
        private String count;

        /**
         * 购物车id集合
         */
        private int[] cart_list;

        /**
         * 商品集合
         */
        private int[] product_list;

        private String carttype;

        /**
         * 构造方法
         *
         * @param spuid 商品spuid
         * @param skuid 商品skuid
         * @param count 商品count
         */
        public CartType(String spuid, String skuid, String count) {
            this.spuid = spuid;
            this.skuid = skuid;
            this.count = count;
            carttype = "product";
        }

        /**
         * 构造方法
         *
         * @param cart_list 购物车id集合
         */
        public CartType(int[] cart_list) {
            this.cart_list = cart_list;
            carttype = "cart";
        }

        public String getCarttype() {
            return carttype;
        }

        public boolean isShoppingCartProduct() {
            return "cart".equals(carttype);
        }

        public String getSpuid() {
            return spuid == null ? "" : spuid;
        }

        public String getSkuid() {
            return skuid == null ? "" : skuid;
        }

        public String getCount() {
            return count == null ? "" : count;
        }

        /**
         * @return 返回购物车Item格式化之后的字符窜
         */
        public String getCart_list() {
            if (cart_list == null || cart_list.length == 0)
                return "";

            int           length = cart_list.length - 1;
            StringBuilder sb     = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < length; i++) {
                sb.append(cart_list[i]);
                sb.append(",");
            }
            sb.append(cart_list[cart_list.length - 1]);
            sb.append("]");
            return sb.toString();
        }

        /**
         * @return 返回商品信息格式化之后的字符窜
         */
        public String getProduct_list() {

            if (product_list == null || product_list.length == 0)
                return "";

            int           length = product_list.length - 1;
            StringBuilder sb     = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < length; i++) {
                sb.append(product_list[i]);
                sb.append(",");
            }
            sb.append(product_list[product_list.length - 1]);
            sb.append("]");
            return sb.toString();
        }

        /**
         * 获取商品数量
         *
         * @return 商品数量
         */
        public int getProductCount() {
            if (cart_list != null && cart_list.length != 0) {
                return cart_list.length;
            } else if (product_list != null && product_list.length != 0) {
                return product_list.length;
            } else {
                return Integer.valueOf(count);
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.spuid);
            dest.writeString(this.skuid);
            dest.writeString(this.count);
            dest.writeIntArray(this.cart_list);
            dest.writeIntArray(this.product_list);
            dest.writeString(this.carttype);
        }

        protected CartType(Parcel in) {
            this.spuid = in.readString();
            this.skuid = in.readString();
            this.count = in.readString();
            this.cart_list = in.createIntArray();
            this.product_list = in.createIntArray();
            this.carttype = in.readString();
        }

        public static final Parcelable.Creator<CartType> CREATOR = new Parcelable.Creator<CartType>() {
            @Override
            public CartType createFromParcel(Parcel source) {
                return new CartType(source);
            }

            @Override
            public CartType[] newArray(int size) {
                return new CartType[size];
            }
        };
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }
}
