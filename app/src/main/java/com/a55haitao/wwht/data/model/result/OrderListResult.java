package com.a55haitao.wwht.data.model.result;

import com.a55haitao.wwht.data.model.annotation.OrderListLayoutType;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 订单列表
 *
 * @author 陶声
 * @since 2016-10-14
 */

public class OrderListResult {
    public int                 count;
    public int                 totalCount;
    public int                 totalPage;
    public int                 page;
    public List<OrderListBean> order_list;

    public static class OrderListBean implements MultiItemEntity {
        public String           consumption_tax_amount;
        public String           tariff_amount;
        public String           added_tax_amount;
        public int              ctime;
        public String           order_id;
        public int              paied_time;
        public String           product_amount;
        public AddressBean      address;
        public String           shipping_amount;
        public String           order_status_name;
        public long             overtime;
        public String           ext;
        public String           need_pay_tariff_price;
        public String           shipping_store_amount;
        public int              tariff_has_paied;
        public String           total;
        public String           order_status;
        public long             now;
        public String           need_pay_tariff;
        public List<DetailBean> detail;
        public int              detailCount;

        @Override
        public int getItemType() {
            switch (order_status) {
                case "NEW":
                    return OrderListLayoutType.UNPAY;
                case "ARRIVED":
                case "SENT2":
                case "SENT1":
                    return OrderListLayoutType.SHIPPING;
                case "OK":
                    return OrderListLayoutType.COMPLETE;
                case "FAIL":
                    return OrderListLayoutType.CANCEL;
                case "BOUGHT":
                case "PAIED":
                case "ROB":
                    return OrderListLayoutType.NORMAL;
            }
            return -1;
        }

        public static class AddressBean {
            public String detail_address;
            public String phone;
            public String consignee;
            public String idt_number;
        }

        public static class DetailBean {
            public int                 count;
            public String              skuid;
            public String              coverImgUrl;
            public String              spuid;
            public String              tax;
            public String              product_name;
            public String              mallPrice;
            public String              realPrice;
            public double              shipping_fee;
            public String              istariff;
            public List<SkuValuesBean> skuValues;

            public static class SkuValuesBean {
                public String name;
                public String value;
            }
        }

        @Override
        public String toString() {
            return "OrderListBean{" +
                    "consumption_tax_amount='" + consumption_tax_amount + '\'' +
                    ", tariff_amount='" + tariff_amount + '\'' +
                    ", added_tax_amount='" + added_tax_amount + '\'' +
                    ", ctime=" + ctime +
                    ", order_id='" + order_id + '\'' +
                    ", paied_time=" + paied_time +
                    ", product_amount='" + product_amount + '\'' +
                    ", address=" + address +
                    ", shipping_amount='" + shipping_amount + '\'' +
                    ", order_status_name='" + order_status_name + '\'' +
                    ", overtime=" + overtime +
                    ", ext='" + ext + '\'' +
                    ", need_pay_tariff_price='" + need_pay_tariff_price + '\'' +
                    ", shipping_store_amount='" + shipping_store_amount + '\'' +
                    ", tariff_has_paied=" + tariff_has_paied +
                    ", total='" + total + '\'' +
                    ", order_status='" + order_status + '\'' +
                    ", now=" + now +
                    ", need_pay_tariff='" + need_pay_tariff + '\'' +
                    ", detail=" + detail +
                    ", detailCount=" + detailCount +
                    '}';
        }
    }
}
/*
{
        "count":10,
        "totalCount":96,
        "totalPage":10,
        "page":1,
        "order_list":[
            {
                "consumption_tax_amount":"437.00",
                "tariff_amount":"1749.00",
                "added_tax_amount":"0.00",
                "ctime":1476344725,
                "order_id":"16101315452598713622",
                "paied_time":0,
                "product_amount":"11643.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"deb6deb27f6e58197e148d05de208927",
                        "coverImgUrl":"http://images.bloomingdales.com/is/image/BLM/products/9/optimized/8793639_fpx.tif?wid=1200",
                        "spuid":"948a1eb501301e288c29f4ade5720a27",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"Black"
                            }
                        ],
                        "product_name":"Khaki Pilot Pioneer Quartz Watch, 41mm",
                        "mallPrice":"4852",
                        "realPrice":"4852",
                        "shipping_fee":62.14,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"65dd5acf4dbc15d864908762c4be3797",
                        "coverImgUrl":"http://www.thewatchery.com/images/H/TISSOT-T0554171601700.jpg",
                        "spuid":"65dd5acf4dbc15d864908762c4be3797",
                        "tax":"0.30",
                        "product_name":"Women's PRC 200 White Leather and Dial",
                        "mallPrice":2080,
                        "realPrice":1594,
                        "shipping_fee":62.14,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"5df525d47776ee50354316d3dde34968",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/6/6/5/0/6/3665065-p-2x.jpg",
                        "spuid":"1dbf20a9a528808d6e8931b9a29467fa",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Black Cherry"
                            }
                        ],
                        "product_name":"Gathered Leather Crosstown Crossbody",
                        "mallPrice":"1939",
                        "realPrice":"1023",
                        "shipping_fee":93.22,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"150fed8741de2710998205121799d35a",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/7/9/0/3/0/3790302-p-2x.jpg",
                        "spuid":"261c659bfde6a853d23951cfa69377af",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Pink Pearl"
                            }
                        ],
                        "product_name":"Madison Pinnacle Pebbled Leather Eva",
                        "mallPrice":"5626",
                        "realPrice":"2820",
                        "shipping_fee":93.22,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"6b3f8411e2dde433ad8fe54adcb226e4",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/3/0/3/6/0/3303604-p-2x.jpg",
                        "spuid":"4b7b9126c45e0d23a124b2f04efbc92c",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Midnight Combo"
                            },
                            {
                                "name":"size",
                                "value":"MD (Women's 8-10)"
                            }
                        ],
                        "product_name":"Tied To You Dress",
                        "mallPrice":"903",
                        "realPrice":"677",
                        "shipping_fee":62.14,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"2244c982782f2b3c9e9b88841df3b054",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/3/0/3/6/0/3303604-p-2x.jpg",
                        "spuid":"4b7b9126c45e0d23a124b2f04efbc92c",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Midnight Combo"
                            },
                            {
                                "name":"size",
                                "value":"SM (Women's 4-6)"
                            }
                        ],
                        "product_name":"Tied To You Dress",
                        "mallPrice":"903",
                        "realPrice":"677",
                        "shipping_fee":62.14,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"435.00",
                "order_status_name":"订单取消",
                "overtime":1476346525,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"0.00",
                "tariff_has_paied":0,
                "total":"13764.00",
                "order_status":"FAIL",
                "now":1476776401,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"173.00",
                "tariff_amount":"1041.00",
                "added_tax_amount":"0.00",
                "ctime":1476344419,
                "order_id":"16101315401947493168",
                "paied_time":0,
                "product_amount":"6913.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"48c72fafaea9cf4525bac81ebe5e5b36",
                        "coverImgUrl":"http://macys-o.scene7.com/is/image/MCY/products/1/optimized/3885181_fpx.tif?wid=800&hei=600&fit=fit,1&$filtersm$",
                        "spuid":"9a694fe5d9c89e6c32b2ea2b3a7f238a",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"Luggage"
                            },
                            {
                                "name":"Size",
                                "value":"One Size"
                            }
                        ],
                        "product_name":"MICHAEL Michael Kors Jet Set Travel Medium East West Crossbody",
                        "mallPrice":"1044",
                        "realPrice":"1044",
                        "shipping_fee":93.94,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"39f268df985c2a7d0b668e3c2b5f2c56",
                        "coverImgUrl":"http://macys-o.scene7.com/is/image/MCY/products/9/optimized/3649989_fpx.tif?wid=800&hei=600&fit=fit,1&$filtersm$",
                        "spuid":"7db6b5873ccfb1d2a3f1c6ff0cca8a53",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"Black"
                            },
                            {
                                "name":"Size",
                                "value":"One Size"
                            }
                        ],
                        "product_name":"MICHAEL Michael Kors Sloan Large Gusset Crossbody",
                        "mallPrice":"1749",
                        "realPrice":"875",
                        "shipping_fee":93.94,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"ff147ad342b1329f691c03c2443fb7d7",
                        "coverImgUrl":"http://www.zappos.com/images/z/2/9/9/0/6/3/2990637-p-2x.jpg",
                        "spuid":"9d3552081557febd8e40238c9f572de9",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Heather Dark Taupe"
                            },
                            {
                                "name":"size",
                                "value":"XS (US 0-2)"
                            }
                        ],
                        "product_name":"Faux Fur Sweater Vest",
                        "mallPrice":"1125",
                        "realPrice":"621",
                        "shipping_fee":93.94,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"1c83be3cb71258aaed53677efb69b10d",
                        "coverImgUrl":"http://content.backcountry.com/images/items/large/ADD/ADD00C0/UNPUPUPU.jpg",
                        "spuid":"3cf9e78f855ee308df67d9eb6ddca8bf",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Unity Purple/Shock Purple/Collegiate Purple"
                            },
                            {
                                "name":"size",
                                "value":"8.0"
                            }
                        ],
                        "product_name":"Adidas Ultra Boost ST Running Shoe - Women's",
                        "mallPrice":"1269",
                        "realPrice":"1269",
                        "shipping_fee":93.94,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"51c8b1d92068a4189b2107b094298d80",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/5/0/9/3/5/3509353-p-2x.jpg",
                        "spuid":"dadecbe44b3f6e47c47c8accd4bfee02",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Coral Blush/Coral Blush/Moon"
                            },
                            {
                                "name":"size",
                                "value":"36 (US Women's 5-5.5)"
                            },
                            {
                                "name":"width",
                                "value":"B - Medium"
                            }
                        ],
                        "product_name":"Intrinsic Karma Tie",
                        "mallPrice":"1058",
                        "realPrice":"1058",
                        "shipping_fee":62.63,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"9006f7c0d73a3538cf9891619aafa200",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/3/2/1/9/2/3321921-p-2x.jpg",
                        "spuid":"066dbbd764d883749dce02e87d070037",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Black"
                            },
                            {
                                "name":"size",
                                "value":"XL (US 14)"
                            }
                        ],
                        "product_name":"Double Breasted Belted Wool Trench Coat",
                        "mallPrice":"988",
                        "realPrice":"988",
                        "shipping_fee":93.94,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"cb556e6583ffd428df21cd418b4c7aae",
                        "coverImgUrl":"http://s1.onlineshoes.com/images/br011/mens-ecco-exceed-low-dried-tobacco-507481_450_45.jpg",
                        "spuid":"99d07b30191449d8462847a7e7c1ac95",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"Dried Tobacco"
                            },
                            {
                                "name":"Size",
                                "value":"40, Men's US 6-6.5 (Euro, Medium)"
                            }
                        ],
                        "product_name":"Men's Exceed Low",
                        "mallPrice":"1058",
                        "realPrice":"1058",
                        "shipping_fee":62.63,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"595.00",
                "order_status_name":"订单取消",
                "overtime":1476346219,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"0.00",
                "tariff_has_paied":0,
                "total":"8522.00",
                "order_status":"FAIL",
                "now":1476776404,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"0.00",
                "tariff_amount":"652.00",
                "added_tax_amount":"0.00",
                "ctime":1476333539,
                "order_id":"16101312385978693923",
                "paied_time":0,
                "product_amount":"4331.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"82159ffb7f04d86a29d57a7d44b4cd23",
                        "coverImgUrl":"http://s001.osstatic.net/s/jctr/store/productimages/details/5215_rio_blue_l.jpg",
                        "spuid":"7e79ee95d54930308aae32e94ea9d195",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"Rio Blue"
                            },
                            {
                                "name":"Size",
                                "value":"L"
                            }
                        ],
                        "product_name":"CRINKLE NYLON RUNNING SHORT",
                        "mallPrice":"832",
                        "realPrice":"564",
                        "shipping_fee":31.43,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"93d6c2151462b4571a60990a940c21b5",
                        "coverImgUrl":"http://media.kohlsimg.com/is/image/kohls/2438572?wid=1000&hei=1000&op_sharpen=1",
                        "spuid":"943beaaccba146575830bce03255c749",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"Lodi Strpe"
                            },
                            {
                                "name":"Size",
                                "value":"X SMALL"
                            }
                        ],
                        "product_name":"Women's Juicy Couture Tie-Dye Striped Soft Shorts",
                        "mallPrice":"283",
                        "realPrice":"113",
                        "shipping_fee":31.43,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"419fc9869a8fafa2e79508fb1672fcd6",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/4/8/9/3/9/3489399-p-2x.jpg",
                        "spuid":"bc881e1763647a15bb547fe363634c2f",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Mid Grey Heather"
                            },
                            {
                                "name":"size",
                                "value":"2XL"
                            }
                        ],
                        "product_name":"Venture Jacket Tall",
                        "mallPrice":"839",
                        "realPrice":"501",
                        "shipping_fee":94.29,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"ab5acedac2960b7f0545126418ca5021",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/6/2/0/8/3/3620837-p-2x.jpg",
                        "spuid":"3aa9f70e8b49fe4620805da2c5c6fef1",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Duck Green/Climbing Ivy Green"
                            },
                            {
                                "name":"size",
                                "value":"LG"
                            }
                        ],
                        "product_name":"Apex Bionic 2 Jacket",
                        "mallPrice":"1051",
                        "realPrice":"1051",
                        "shipping_fee":94.29,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"3490cc989e7e2ac24bf12385525a402b",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/6/2/0/8/3/3620839-p-2x.jpg",
                        "spuid":"3aa9f70e8b49fe4620805da2c5c6fef1",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Sequoia Red Heather/Sequoia Red Heather"
                            },
                            {
                                "name":"size",
                                "value":"LG"
                            }
                        ],
                        "product_name":"Apex Bionic 2 Jacket",
                        "mallPrice":"1051",
                        "realPrice":"1051",
                        "shipping_fee":94.29,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"d8d3c029fb4b55bf6998b238d288f239",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/7/0/8/4/5/3708456-p-2x.jpg",
                        "spuid":"3aa9f70e8b49fe4620805da2c5c6fef1",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Root Brown Heather/Root Brown Heather"
                            },
                            {
                                "name":"size",
                                "value":"LG"
                            }
                        ],
                        "product_name":"Apex Bionic 2 Jacket",
                        "mallPrice":"1051",
                        "realPrice":"1051",
                        "shipping_fee":94.29,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"440.00",
                "order_status_name":"订单取消",
                "overtime":1476335339,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"97.00",
                "tariff_has_paied":0,
                "total":"5520.00",
                "order_status":"FAIL",
                "now":1476776407,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"124.00",
                "tariff_amount":"880.00",
                "added_tax_amount":"0.00",
                "ctime":1476331543,
                "order_id":"16101312054391221906",
                "paied_time":0,
                "product_amount":"5489.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"743c876f846653b5f467fbedf4f31614",
                        "coverImgUrl":"http://res.cloudinary.com/ssenseweb/image/upload/v550/162148F110010_1.jpg",
                        "spuid":"c876e8d731c051fb2ba0aa7b7038215e",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"size",
                                "value":"XS"
                            }
                        ],
                        "product_name":"Black 'Twins' T-Shirt",
                        "mallPrice":"1340",
                        "realPrice":"1340",
                        "shipping_fee":31.43,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"a3e50f4065cab7839cfe59f01afa907f",
                        "coverImgUrl":"http://res.cloudinary.com/ssenseweb/image/upload/v550/162751M237035_1.jpg",
                        "spuid":"bb616cf719029b64c94ac4dc6c97e8db",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"size",
                                "value":"IT 43.5"
                            }
                        ],
                        "product_name":"White & Green Stan Smith Sneakers 'Stan Smith'绿尾鞋",
                        "mallPrice":"529",
                        "realPrice":"529",
                        "shipping_fee":62.86,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"7c195cf534219f3588d20b5469078ce0",
                        "coverImgUrl":"http://www.sephora.com/productimages/sku/s1795087-main-zoom.jpg",
                        "spuid":"fdbedc099e6c0b37da13a2a08e068b02",
                        "tax":"0.60",
                        "skuValues":[
                            {
                                "name":"Size",
                                "value":"1 oz"
                            }
                        ],
                        "product_name":"Meteorites Base Perfecting Pearls",
                        "mallPrice":"522",
                        "realPrice":"522",
                        "shipping_fee":31.43,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"9006f7c0d73a3538cf9891619aafa200",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/3/2/1/9/2/3321921-p-2x.jpg",
                        "spuid":"066dbbd764d883749dce02e87d070037",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Black"
                            },
                            {
                                "name":"size",
                                "value":"XL (US 14)"
                            }
                        ],
                        "product_name":"Double Breasted Belted Wool Trench Coat",
                        "mallPrice":"988",
                        "realPrice":"988",
                        "shipping_fee":94.29,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"648de5a7ee2e3b48bc2a6ded11fd332f",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/5/4/1/4/5/3541452-p-2x.jpg",
                        "spuid":"f9a5de8df039fd84f84ffd74448cbb3a",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Navy"
                            },
                            {
                                "name":"size",
                                "value":"16"
                            }
                        ],
                        "product_name":"Pencil Skirt",
                        "mallPrice":"487",
                        "realPrice":"487",
                        "shipping_fee":62.86,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"2cc36e6042d721ce319af4132c9cfc9a",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/3/2/1/7/1/3321719-p-2x.jpg",
                        "spuid":"5e522eefe54172f0ec2590a2510e40aa",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Navy"
                            },
                            {
                                "name":"size",
                                "value":"XL (US 14)"
                            }
                        ],
                        "product_name":"Packable Down Walker Coat with Velvet Collar",
                        "mallPrice":"1848",
                        "realPrice":"776",
                        "shipping_fee":94.29,
                        "istariff":"true"
                    },
                    {
                        "count":1,
                        "skuid":"41ec5542c5dc4b1875983d9ca30f2295",
                        "coverImgUrl":"http://macys-o.scene7.com/is/image/MCY/products/5/optimized/3866845_fpx.tif?wid=800&hei=600&fit=fit,1&$filtersm$",
                        "spuid":"9fa5f22f9a4ec5c845c8adb9a329e1ba",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"Core Black/Ray Red"
                            },
                            {
                                "name":"Size",
                                "value":"8.5"
                            }
                        ],
                        "product_name":"adidas Women's Pure Boost X Print Running Sneakers from Finish Line",
                        "mallPrice":"847",
                        "realPrice":"847",
                        "shipping_fee":62.86,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"440.00",
                "order_status_name":"订单取消",
                "overtime":1476333343,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"0.00",
                "tariff_has_paied":0,
                "total":"6933.00",
                "order_status":"FAIL",
                "now":1476776409,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"0.00",
                "tariff_amount":"48.00",
                "added_tax_amount":"0.00",
                "ctime":1476327496,
                "order_id":"16101310581632560476",
                "paied_time":0,
                "product_amount":"191.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"1c303bedb83bda08046c2c9677ac06b6",
                        "coverImgUrl":"http://s7d9.scene7.com/is/image/LordandTaylor/027131476368_main?$PDPLARGE$",
                        "spuid":"53ec2397e0318edcff71e419ba5ff708",
                        "tax":"0.60",
                        "skuValues":[
                            {
                                "name":"Color",
                                "value":"1W Light"
                            },
                            {
                                "name":"Size",
                                "value":"One Size"
                            }
                        ],
                        "product_name":"Double Wear Stay-in-Place High Cover Concealer",
                        "mallPrice":"191",
                        "realPrice":"191",
                        "shipping_fee":35,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"35.00",
                "order_status_name":"订单取消",
                "overtime":1476329296,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"76.00",
                "tariff_has_paied":0,
                "total":"350.00",
                "order_status":"FAIL",
                "now":1476776413,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"0.00",
                "tariff_amount":"828.00",
                "added_tax_amount":"0.00",
                "ctime":1476324925,
                "order_id":"16101310152509859731",
                "paied_time":0,
                "product_amount":"5515.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"c14d29783e67017d248a7774838fb286",
                        "coverImgUrl":"http://thewebster.us/shop/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/n/1/n1485735731287410960-2.jpg",
                        "spuid":"b7d074d40224cdfb444fab0d4a640a71",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"Style",
                                "value":"One Color"
                            },
                            {
                                "name":"Size",
                                "value":"37"
                            }
                        ],
                        "product_name":"'Fetish' sandals",
                        "mallPrice":"11022",
                        "realPrice":"5515",
                        "shipping_fee":65,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"65.00",
                "order_status_name":"订单取消",
                "overtime":1476326725,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"0.00",
                "tariff_has_paied":0,
                "total":"6408.00",
                "order_status":"FAIL",
                "now":1476776414,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"114.00",
                "tariff_amount":"190.00",
                "added_tax_amount":"0.00",
                "ctime":1474377449,
                "order_id":"16092021172987015446",
                "paied_time":0,
                "product_amount":"1261.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"998630713be2dc38e34fb0b82cd82f22",
                        "coverImgUrl":"http://s7d4.scene7.com/is/image/KateSpade/WKRU2097_218_5?$s7fullsize$",
                        "spuid":"998630713be2dc38e34fb0b82cd82f22",
                        "tax":"0.30",
                        "product_name":"varick street abbie",
                        "mallPrice":2997,
                        "realPrice":1261,
                        "shipping_fee":65,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"65.00",
                "order_status_name":"订单取消",
                "overtime":1474379249,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"34.00",
                "tariff_has_paied":0,
                "total":"1664.00",
                "order_status":"FAIL",
                "now":1476776416,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"114.00",
                "tariff_amount":"190.00",
                "added_tax_amount":"0.00",
                "ctime":1474377097,
                "order_id":"16092021113742509761",
                "paied_time":0,
                "product_amount":"1261.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"998630713be2dc38e34fb0b82cd82f22",
                        "coverImgUrl":"http://s7d4.scene7.com/is/image/KateSpade/WKRU2097_218_5?$s7fullsize$",
                        "spuid":"998630713be2dc38e34fb0b82cd82f22",
                        "tax":"0.30",
                        "product_name":"varick street abbie",
                        "mallPrice":2997,
                        "realPrice":1261,
                        "shipping_fee":65,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"65.00",
                "order_status_name":"订单取消",
                "overtime":1474378897,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"34.00",
                "tariff_has_paied":0,
                "total":"1664.00",
                "order_status":"FAIL",
                "now":1476776416,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"0.00",
                "tariff_amount":"99.00",
                "added_tax_amount":"0.00",
                "ctime":1474373427,
                "order_id":"16092020102777776423",
                "paied_time":0,
                "product_amount":"658.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"89faa909bae3571f2f2ae8c04c91baf2",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/6/7/6/8/6/3676864-p-2x.jpg",
                        "spuid":"80a220fb5f3fb154b35b3d81cf4dcdf1",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Violet Purple"
                            },
                            {
                                "name":"size",
                                "value":"One Size"
                            }
                        ],
                        "product_name":"Challenger II Backpack",
                        "mallPrice":"658",
                        "realPrice":"658",
                        "shipping_fee":95,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"95.00",
                "order_status_name":"订单取消",
                "overtime":1474375227,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"0.00",
                "tariff_has_paied":0,
                "total":"852.00",
                "order_status":"FAIL",
                "now":1476776417,
                "need_pay_tariff":"0"
            },
            {
                "consumption_tax_amount":"0.00",
                "tariff_amount":"99.00",
                "added_tax_amount":"0.00",
                "ctime":1474373296,
                "order_id":"16092020081653021874",
                "paied_time":0,
                "product_amount":"658.00",
                "address":{
                    "detail_address":"北京市北京市东城区你心惊胆寒",
                    "phone":"18650335127",
                    "consignee":"测试",
                    "idt_number":"352203198956555555"
                },
                "detail":[
                    {
                        "count":1,
                        "skuid":"89faa909bae3571f2f2ae8c04c91baf2",
                        "coverImgUrl":"http://www.zappos.com/images/z/3/6/7/6/8/6/3676864-p-2x.jpg",
                        "spuid":"80a220fb5f3fb154b35b3d81cf4dcdf1",
                        "tax":"0.30",
                        "skuValues":[
                            {
                                "name":"color",
                                "value":"Violet Purple"
                            },
                            {
                                "name":"size",
                                "value":"One Size"
                            }
                        ],
                        "product_name":"Challenger II Backpack",
                        "mallPrice":"658",
                        "realPrice":"658",
                        "shipping_fee":95,
                        "istariff":"true"
                    }
                ],
                "shipping_amount":"95.00",
                "order_status_name":"订单取消",
                "overtime":1474375096,
                "ext":"",
                "need_pay_tariff_price":"0",
                "shipping_store_amount":"0.00",
                "tariff_has_paied":0,
                "total":"852.00",
                "order_status":"FAIL",
                "now":1476776418,
                "need_pay_tariff":"0"
            }
        ]
    }
*/