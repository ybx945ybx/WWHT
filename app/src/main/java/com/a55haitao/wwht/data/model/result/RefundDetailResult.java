package com.a55haitao.wwht.data.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a55 on 2017/6/8.
 */

public class RefundDetailResult {

    /**
     * refund_no : 695
     * refund_status : 0
     * refund_status_code : REFUND_ACCEPTED
     * refund_status_name : 受理中
     * refund_status_note : 申请受理中，预计需要3个工作日反馈结果，请耐心等待。
     * goods_size : 1
     * cTime : 1496633406
     * note :
     * linkman_contacts : 张三
     * phone : 15036507119
     * voucher_url :
     * items : [{"product_name":"Ceramic Ladies Watch","cover_img_url":"http://cdn2.jomashop.com/media/catalog/product/a/k/akribos-xxiv-ceramic-ladies-watch-ak514bk.jpg","selected_sku":{"styleId":"","spuid":"dee5b05966775711bf589ca543a15c6f","stock":1,"realPriceOrg":53,"mallPrice":2498,"skuid":"dee5b05966775711bf589ca543a15c6f","inStock":1,"realPrice":385,"skuValues":[],"mallPriceOrg":345}}]
     */

    public int                  refund_no;
    public int                  refund_status;
    public String               refund_status_code;
    public String               refund_status_name;
    public String               refund_status_note;
    public int                  goods_size;
    public long                 cTime;
    public String               note;
    public String               linkman_contacts;
    public String               phone;
    public String               voucher_url;
    public ArrayList<ItemsBean> items;

    //    public int getRefund_no() {
    //        return refund_no;
    //    }
    //
    //    public void setRefund_no(int refund_no) {
    //        this.refund_no = refund_no;
    //    }
    //
    //    public int getRefund_status() {
    //        return refund_status;
    //    }
    //
    //    public void setRefund_status(int refund_status) {
    //        this.refund_status = refund_status;
    //    }
    //
    //    public String getRefund_status_code() {
    //        return refund_status_code;
    //    }
    //
    //    public void setRefund_status_code(String refund_status_code) {
    //        this.refund_status_code = refund_status_code;
    //    }
    //
    //    public String getRefund_status_name() {
    //        return refund_status_name;
    //    }
    //
    //    public void setRefund_status_name(String refund_status_name) {
    //        this.refund_status_name = refund_status_name;
    //    }
    //
    //    public String getRefund_status_note() {
    //        return refund_status_note;
    //    }
    //
    //    public void setRefund_status_note(String refund_status_note) {
    //        this.refund_status_note = refund_status_note;
    //    }
    //
    //    public int getGoods_size() {
    //        return goods_size;
    //    }
    //
    //    public void setGoods_size(int goods_size) {
    //        this.goods_size = goods_size;
    //    }
    //
    //    public int getCTime() {
    //        return cTime;
    //    }
    //
    //    public void setCTime(int cTime) {
    //        this.cTime = cTime;
    //    }
    //
    //    public String getNote() {
    //        return note;
    //    }
    //
    //    public void setNote(String note) {
    //        this.note = note;
    //    }
    //
    //    public String getLinkman_contacts() {
    //        return linkman_contacts;
    //    }
    //
    //    public void setLinkman_contacts(String linkman_contacts) {
    //        this.linkman_contacts = linkman_contacts;
    //    }
    //
    //    public String getPhone() {
    //        return phone;
    //    }
    //
    //    public void setPhone(String phone) {
    //        this.phone = phone;
    //    }
    //
    //    public String getVoucher_url() {
    //        return voucher_url;
    //    }
    //
    //    public void setVoucher_url(String voucher_url) {
    //        this.voucher_url = voucher_url;
    //    }
    //
    //    public List<ItemsBean> getItems() {
    //        return items;
    //    }
    //
    //    public void setItems(List<ItemsBean> items) {
    //        this.items = items;
    //    }

    public static class ItemsBean {
        /**
         * product_name : Ceramic Ladies Watch
         * cover_img_url : http://cdn2.jomashop.com/media/catalog/product/a/k/akribos-xxiv-ceramic-ladies-watch-ak514bk.jpg
         * selected_sku : {"styleId":"","spuid":"dee5b05966775711bf589ca543a15c6f","stock":1,"realPriceOrg":53,"mallPrice":2498,"skuid":"dee5b05966775711bf589ca543a15c6f","inStock":1,"realPrice":385,"skuValues":[],"mallPriceOrg":345}
         */

        public String          product_name;
        public String          cover_img_url;
        public SelectedSkuBean selected_sku;
        //
        //        public String getProduct_name() {
        //            return product_name;
        //        }
        //
        //        public void setProduct_name(String product_name) {
        //            this.product_name = product_name;
        //        }
        //
        //        public String getCover_img_url() {
        //            return cover_img_url;
        //        }
        //
        //        public void setCover_img_url(String cover_img_url) {
        //            this.cover_img_url = cover_img_url;
        //        }
        //
        //        public SelectedSkuBean getSelected_sku() {
        //            return selected_sku;
        //        }
        //
        //        public void setSelected_sku(SelectedSkuBean selected_sku) {
        //            this.selected_sku = selected_sku;
        //        }

        public static class SelectedSkuBean {
            /**
             * styleId :
             * spuid : dee5b05966775711bf589ca543a15c6f
             * stock : 1
             * realPriceOrg : 53
             * mallPrice : 2498
             * skuid : dee5b05966775711bf589ca543a15c6f
             * inStock : 1
             * realPrice : 385
             * skuValues : []
             * mallPriceOrg : 345
             */

            public String  styleId;
            public String  spuid;
            public int     stock;
            public int     realPriceOrg;
            public int     mallPrice;
            public String  skuid;
            public int     inStock;
            public int     realPrice;
            public int     mallPriceOrg;
            public List<?> skuValues;

            public String getStyleId() {
                return styleId;
            }

            public void setStyleId(String styleId) {
                this.styleId = styleId;
            }

            public String getSpuid() {
                return spuid;
            }

            public void setSpuid(String spuid) {
                this.spuid = spuid;
            }

            public int getStock() {
                return stock;
            }

            public void setStock(int stock) {
                this.stock = stock;
            }

            public int getRealPriceOrg() {
                return realPriceOrg;
            }

            public void setRealPriceOrg(int realPriceOrg) {
                this.realPriceOrg = realPriceOrg;
            }

            public int getMallPrice() {
                return mallPrice;
            }

            public void setMallPrice(int mallPrice) {
                this.mallPrice = mallPrice;
            }

            public String getSkuid() {
                return skuid;
            }

            public void setSkuid(String skuid) {
                this.skuid = skuid;
            }

            public int getInStock() {
                return inStock;
            }

            public void setInStock(int inStock) {
                this.inStock = inStock;
            }

            public int getRealPrice() {
                return realPrice;
            }

            public void setRealPrice(int realPrice) {
                this.realPrice = realPrice;
            }

            public int getMallPriceOrg() {
                return mallPriceOrg;
            }

            public void setMallPriceOrg(int mallPriceOrg) {
                this.mallPriceOrg = mallPriceOrg;
            }

            public List<?> getSkuValues() {
                return skuValues;
            }

            public void setSkuValues(List<?> skuValues) {
                this.skuValues = skuValues;
            }
        }
    }
}
