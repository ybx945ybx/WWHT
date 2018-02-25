package com.a55haitao.wwht.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

import com.a55haitao.wwht.R;
import com.a55haitao.wwht.data.event.ProductRTEvent;
import com.a55haitao.wwht.data.model.entity.ProductDetailBean;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Haotao_Fujie on 16/10/10.
 */

public class ProductMgr {

    // 产品规格处理单例
    private ProductMgr() {
    }

    private static ProductMgr singleton = null;

    public static ProductMgr getInstance() {

        if (singleton == null) {

            singleton = new ProductMgr();

        }
        return singleton;
    }

//    public static ProductMgr newInstance(String topic) {
//        ProductMgr productMgr = new ProductMgr();
//        repisity.mIsQuery = true;
//        repisity.mTopic = topic;
//        repisity.mCurrentSort = -1;
//        repisity.mFilterPositions = new int[]{-1, -1, -1, -1};
//        return productMgr;
//    }

    // Cache
    /**
     * {A:[a1,a2], B:[b1,b2,b3]}
     */
    public HashMap<String, ArrayList<String>> cache_allSpecs;

    /**
     * [A:"", B:b1] , ""代表没有选择
     */
    public HashMap<String, String> cache_selectedSpecs;

    /**
     * Product Data
     */
    public ProductDetailBean cache_productData;

    /**
     * 是否正在核价
     */
    private boolean inProductRTing;

    public void setInProductRTing(boolean inProductRTing) {
        this.inProductRTing = inProductRTing;
        EventBus.getDefault().post(new ProductRTEvent(inProductRTing));
    }

    public boolean getInProductRTing() {
        return this.inProductRTing;
    }

    /**
     * 是否是单一规格商品
     */
    private boolean _isSingleSpecAndValue;

    // 数据库
    private SqliteHelper   dbHelper;
    private SQLiteDatabase db;
    // 数据库版本
    private static int    DB_VERSION = 1;
    private static String DB_NAME    = "mydatabase.db";
    private Context context;


    // 初始化Context
    public void setContext(Context context) {
        this.context = context;
    }

    // 初始化PageProductBean
//    public void setCache_pageProductBean(PageProductBean cache_pageProductBean) {
//        this.cache_pageProductBean = cache_pageProductBean;
//
//        // FIX BITCH
//        // fixBitch(cache_pageProductBean.mProductDetailData);
//        this.cache_productData = cache_pageProductBean.mProductDetailData;
//    }

    // 初始化ProductData
    public void setCache_productData(ProductDetailBean cache_productData) {
        // FIX BITCH
        fixBitch(cache_productData);
        this.cache_productData = cache_productData;
    }

    // FIX BITCH
    private void fixBitch(ProductDetailBean data) {
        if (data.skuInfo != null) {

            // 修复style中有数字开头的情况
            String styleName = "";
            if (data.skuInfo.style != null) {
                if (data.skuInfo.style.name.length() > 0) {
                    int num = data.skuInfo.style.name.charAt(0);
                    if (num >= 48 && num <= 57) {
                        data.skuInfo.style.name = "_" + data.skuInfo.style.name;
                    }
                    styleName = data.skuInfo.style.name;
                }
            }
            if (data.skuInfo.select != null && data.skuInfo.select.size() > 0) {

                List<String> fixArray = new ArrayList();

                for (ProductDetailBean.SelectData selectData : data.skuInfo.select) {
                    for (ProductDetailBean.SkuSelectListData skuSelectListData : selectData.skuselectlist) {

                        if (skuSelectListData.select.length() > 0) {

                            // 修复select中有数字开头的情况
                            int num = skuSelectListData.select.charAt(0);
                            if (num >= 48 && num <= 57) {
                                skuSelectListData.select = "_" + skuSelectListData.select;
                            }

                            if (!TextUtils.isEmpty(styleName)) {
                                // 同名(防止和style同名)
                                if (styleName.equals(skuSelectListData.select)) {
                                    data.skuInfo.style.name = data.skuInfo.style.name + "_0";
                                    styleName = "";
                                }
                            }

                            // 修复value,把双引号变成两个单引号 "->''
                            skuSelectListData.value = skuSelectListData.value.replace("\"", "\'\'");
                        }
                    }

                    // 修正style list的顺序
                    fixArray.add(selectData.style);
                }

                // 修正style list的顺序
                if (fixArray.size() > 1) {

                    // 去除重复
                    for (int i = 0; i < fixArray.size() - 1; i++) {
                        String a = (String) fixArray.get(i);
                        for (int j = fixArray.size() - 1; j > i; j--) {
                            String b = (String) fixArray.get(j);
                            if (a.equals(b)) {
                                fixArray.remove(j);
                            }
                        }
                    }

                    if (data.skuInfo.style != null && data.skuInfo.style.skustylelist != null && fixArray.size() > 1 && data.skuInfo.style.skustylelist.size() == fixArray.size()) {

                        // 按照FixArray的顺序
                        List<ProductDetailBean.StyleListData> skustylelist = new ArrayList();
                        skustylelist.addAll(data.skuInfo.style.skustylelist);
                        data.skuInfo.style.skustylelist.clear();
                        for (String s : fixArray) {
                            for (ProductDetailBean.StyleListData styleListData : skustylelist) {
                                if (styleListData.value.equals(s)) {
                                    data.skuInfo.style.skustylelist.add(styleListData);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 清空初始化
    public void destroyProductData() {

        cache_productData = null;

        prepareData();

    }

    // 内部：准备计算的数据模型
    private void prepareData() {

        if (cache_allSpecs == null) {
            cache_allSpecs = new HashMap<String, ArrayList<String>>();
        } else {
            cache_allSpecs.clear();
        }

        if (cache_selectedSpecs == null) {
            cache_selectedSpecs = new HashMap<String, String>();
        } else {
            cache_selectedSpecs.clear();
        }

        _isSingleSpecAndValue = false;
    }

    // 初始化产品
    public void initializeProductData() {

        // 初始化
        prepareData();

        // 提前设置好Context和PageProductBean
//        assert (context != null);
//        assert (cache_pageProductBean != null);

        // 初始化局部变量
        ArrayList<AssistSpecItem> specs                = new ArrayList<AssistSpecItem>();
        ArrayList<String>         defalutSelectedStyle = new ArrayList<String>();
        ArrayList<String>         defaultValues        = new ArrayList<String>();

        if (cache_productData.skuInfo.style == null) {

            if (cache_productData.skuInfo.select != null && cache_productData.skuInfo.select.size() > 0) {
                for (ProductDetailBean.SelectData selectData : cache_productData.skuInfo.select) {
                    AssistSpecItem item = new AssistSpecItem();
                    item.specs = new ArrayList<>();
                    item.values = new ArrayList<>();
                    for (ProductDetailBean.SkuSelectListData skuSelectListData : selectData.skuselectlist) {
                        item.specs.add(skuSelectListData.select);
                        item.values.add(skuSelectListData.value);
                    }
                    item.stock = selectData.inStock;
                    item.skuid = selectData.skuid;
                    specs.add(item);

                    if (cache_productData.defaultSkuid.equals(selectData.skuid)) {
                        // 默认选中项
                        defalutSelectedStyle.addAll(item.specs);
                        defaultValues.addAll(item.values);
                    }

                    // 设置cache_allSpecs的key
                    if (selectData.equals(cache_productData.skuInfo.select.get(0))) {
                        for (String spec : item.specs) {
                            cache_allSpecs.put(spec, new ArrayList<String>());
                        }
                    }

                }
            } else {
                // 单规格，单选项
                _isSingleSpecAndValue = true;
                return;

            }

        } else {

            if (cache_productData.skuInfo.select == null || cache_productData.skuInfo.select.size() == 0) {
                // 单个规格单个选项，且并只能选择一个默认
                _isSingleSpecAndValue = true;

            } else {
                // 多个规格，并且选中一个默认
                for (ProductDetailBean.SelectData selectData : cache_productData.skuInfo.select) {
                    AssistSpecItem item = new AssistSpecItem();
                    item.specs = new ArrayList<>();
                    item.values = new ArrayList<>();
                    item.specs.add(cache_productData.skuInfo.style.name);
                    item.values.add(selectData.style);
                    for (ProductDetailBean.SkuSelectListData skuSelectListData : selectData.skuselectlist) {
                        item.specs.add(skuSelectListData.select);
                        item.values.add(skuSelectListData.value);
                    }
                    item.stock = selectData.inStock;
                    item.skuid = selectData.skuid;
                    specs.add(item);

                    if (cache_productData.defaultSkuid.equals(selectData.skuid)) {
                        // 默认选中项
                        defalutSelectedStyle.addAll(item.specs);
                        defaultValues.addAll(item.values);
                    }

                    // 设置cache_allSpecs的key
                    if (selectData.equals(cache_productData.skuInfo.select.get(0))) {
                        for (String spec : item.specs) {
                            cache_allSpecs.put(spec, new ArrayList<String>());
                        }

                    }
                }

            }

        }

        try {
            // 初始化Specs
            setSpecs(specs, defalutSelectedStyle, defaultValues);

            // 设置cache_allSpecs
            setCache_AllSpecs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化产品 - 转义
     * <p>
     * eg: 传入参数specs: [{ "specs":["Color", "Size"], "values":["Black","5"], YES, "11111111"}, { "specs":["Color", "Size"], "values":["Black","6"], YES, "22222222"}]
     * styleName: "Color"，也可以为nil
     * value: "Black"，也可以为nil
     */
    private void setSpecs(ArrayList<AssistSpecItem> specs, ArrayList<String> styleNames, ArrayList<String> values) {

        if (specs == null || specs.size() == 0) {
            return;
        }

        AssistSpecItem item = specs.get(0);


        // create 新建表（根据AssistSpecItem结构里的spec名称+stock+skuid创建字段）
        String createSql = "create table tb(";
        for (String spec : item.specs) {
            createSql += this.addQuoatation(spec) + " VARCHAR(255),";
        }
        createSql += "Stock INT, Skuid VARCHAR(255));";

        if (dbHelper == null) {
            dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION, createSql);
            db = dbHelper.getWritableDatabase();
        }

        try {
            db.execSQL("DROP TABLE IF EXISTS tb");
            db.execSQL(createSql);
            // insert 填充表（根据AssistSpecItem结构里的spec名称+stock+skuid填充字段）
            for (AssistSpecItem specItem : specs) {
                String insertSql = "insert into tb values(";
                for (String value : specItem.values) {
                    insertSql += this.addQuoatation(value) + ",";
                }
                insertSql += specItem.stock + ",";
                insertSql += "\"" + specItem.skuid + "\")";
                db.execSQL(insertSql);
                insertSql = null;
            }
        }catch (SQLException ex ) {
            Logger.d(ex.getMessage());
        }


        // 清空本地的spec
        cache_selectedSpecs.clear();

        // 填充本地的specs缓存（根据AssistSpecItem结构里的spec名称，以KVO形式保存是否选中）
        for (String spec : item.specs) {
            // 空字符串代表未选中spec
            cache_selectedSpecs.put(spec, "");
        }

        // 将默认的选中规格设置为已选
        if (styleNames != null && values != null && styleNames.size() > 0 && values.size() > 0) {
            for (int i = 0; i < styleNames.size(); i++) {
                cache_selectedSpecs.put(styleNames.get(i), values.get(i));
            }
        }

    }

    // 初始化Specs
    private void setCache_AllSpecs() {
        if (db.isOpen()) {
            for (String key : cache_allSpecs.keySet()) {

                ArrayList<String> values = new ArrayList<String>();
                String[]          coloum = {key};
                Cursor            csr    = db.query(true, "tb", coloum, null, null, null, null, null, null);


                if (csr.moveToFirst()) {
                    do {
                        String value = csr.getString(0);
                        values.add(value);
                    }
                    while (csr.moveToNext());
                }
                cache_allSpecs.put(key, values);
            }
        }
    }

    // 返回可选的其它的spec名称和spec的值
    public ArrayList<AvailableSpectItemValue> getAvailableSpecs() {

        if (_isSingleSpecAndValue == true) {

            return null;
        }

        // 按spec名称作为sql条件查询其它未选的spec和spec value
        // 比如 select distinct[未选spec名称] where [已选字段] = [已选字段Value] AND [STOCK] == 1   // 1代表有库存，0代表没库存
        // 结果为 "5"、"6"， 那么把[未选spec名称]，["5","6"]添加入返回值队列里
        if (db.isOpen()) {

            ArrayList<AvailableSpectItemValue> ret = new ArrayList<AvailableSpectItemValue>();

            for (String eachKey : cache_selectedSpecs.keySet()) {

                // Compose where sentence
                String   wheresSentence = "where ";
                Iterator iter           = cache_selectedSpecs.entrySet().iterator();

                while (iter.hasNext()) {

                    Map.Entry entry = (Map.Entry) iter.next();
                    String    spec  = (String) entry.getKey();
                    String    value = (String) entry.getValue();

                    if (!spec.equals(eachKey)) {
                        if (!value.equals("") && !TextUtils.isEmpty(value)) {
                            wheresSentence += this.addQuoatation(spec) + " = " + this.addQuoatation(value) + " AND ";
                        }
                    }
                }
                wheresSentence += "Stock >= 1";

                AvailableSpectItemValue availableSpectItemValue = new AvailableSpectItemValue();
                availableSpectItemValue.spec = eachKey;

                //String[] colums = {eachKey, "Stock"};
                //Cursor csr = db.query(false, "tb", colums, null, null, null, null, null, null);

                String querySql = "select distinct(" + this.addQuoatation(eachKey) + ")" + " from tb " + wheresSentence;

                try {
                    Cursor csr = db.rawQuery(querySql, null);
                    if (csr.moveToFirst()) {

                        do {

                            String value = csr.getString(0);
                            availableSpectItemValue.availableValues.add(value);

                        } while (csr.moveToNext());
                    }
                    ret.add(availableSpectItemValue);

                }catch (SQLException ex) {
                    Logger.d(ex.getMessage());
                }

            }
            return ret;
        }

        return null;

    }


    // 获取商品的spuid
    public String getSpuid() {
        return cache_productData.spuid;
    }

    // 获取选规格的skuid
    public String getSelectedSkuid() {

        if (_isSingleSpecAndValue == true) {
            return TextUtils.isEmpty(cache_productData.skuid) ? cache_productData.defaultSkuid : cache_productData.skuid;
        }

        String whereSentence = "where ";

        // 判断cache_selectedSpecs是否全部选中spec
        Iterator iter = cache_selectedSpecs.entrySet().iterator();
        while (iter.hasNext()) {

            Map.Entry entry = (Map.Entry) iter.next();
            String    key   = (String) entry.getKey();
            String    value = (String) entry.getValue();
            if (TextUtils.isEmpty(value)) {

                // 未全选中规格
                return null;

            } else {
                // 拼装whereSql
                whereSentence += this.addQuoatation(key) + " = " + this.addQuoatation(value) + " and ";
            }
        }
        whereSentence += " Stock >= 1";


        // 从数据库查出spuid
        // 比如 select skuid from tb where spec1 = "xx" AND spec2 = "xx"


        if (db.isOpen()) {

            String querySql = "select skuid from tb " + whereSentence;
            try {
                Cursor csr      = db.rawQuery(querySql, null);
                if (csr.moveToFirst()) {
                    String skuid = csr.getString(0);
                    if (skuid != null && skuid.length() > 0) return skuid;
                }
            }catch (SQLException ex) {
                Logger.d(ex.getMessage());
            }

        }

        return null;

    }

    // 当前的商品图片
    public String currentCover() {

        if (_isSingleSpecAndValue == true) {

            return cache_productData.coverImgUrl;

        } else {

            String skuid = getSelectedSkuid();
            if (!TextUtils.isEmpty(skuid)) {

                if (cache_productData != null && cache_productData.skuInfo != null && cache_productData.skuInfo.select != null && cache_productData.skuInfo.select.size() > 0) {

                    for (ProductDetailBean.SelectData selectData : cache_productData.skuInfo.select) {

                        if (selectData.skuid.equals(skuid)) {

                            String style = selectData.style;
                            if (cache_productData.skuInfo.style != null && cache_productData.skuInfo.style.skustylelist != null && cache_productData.skuInfo.style.skustylelist.size() > 0) {

                                for (ProductDetailBean.StyleListData styleListData : cache_productData.skuInfo.style.skustylelist) {
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

            if (cache_productData != null) {

                if (cache_productData.skuInfo != null && cache_productData.skuInfo.style != null && cache_productData.skuInfo.style.skustylelist != null && cache_productData.skuInfo.style.skustylelist.size() > 0) {

                    String   coverImg = null;
                    Iterator iter     = cache_selectedSpecs.entrySet().iterator();
                    while (iter.hasNext()) {

                        Map.Entry entry = (Map.Entry) iter.next();
                        String    key   = (String) entry.getKey();
                        if (key.equals(cache_productData.skuInfo.style.name)) {

                            Object value = entry.getValue();
                            if (value != null && value instanceof String && !TextUtils.isEmpty((String) value)) {

                                for (ProductDetailBean.StyleListData styleListData : cache_productData.skuInfo.style.skustylelist) {
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
                    return cache_productData.coverImgUrl;
                }
            }

        }

        return null;

    }

    // 当前的商品Title
    public String currentTitle() {

        if (_isSingleSpecAndValue == true) {

            return cache_productData.name;

        } else {

            return cache_productData.name;

        }

    }

    // 当前的商品价格
    public String currentPrice() {

        if (_isSingleSpecAndValue == true) {

            return PriceUtils.toRMBFromFen(cache_productData.realPrice);

        } else {

            String skuid = getSelectedSkuid();
            if (TextUtils.isEmpty(skuid)) {

                return PriceUtils.toRMBFromFen(cache_productData.realPrice);

            } else {

                if (cache_productData != null && cache_productData.skuInfo != null && cache_productData.skuInfo.select != null && cache_productData.skuInfo.select.size() > 0) {

                    for (ProductDetailBean.SelectData selectData : cache_productData.skuInfo.select) {
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
        if (_isSingleSpecAndValue) {

            ret = "已选择:均码,";

        } else {

            ret = "已选择:";

            for (String specName : cache_allSpecs.keySet()) {
                boolean find = false;

                Iterator iter = cache_selectedSpecs.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry            = (Map.Entry) iter.next();
                    String    selectedSpecName = (String) entry.getKey();

                    if (selectedSpecName.equals(specName)) {
                        Object value = entry.getValue();
                        if (value instanceof String) {
                            // 已选择规格值
                            String specValue = (String) value;
                            if (TextUtils.isEmpty(specValue) == false) {
                                ret += "\"" + specValue + "\",";
                                find = true;
                                break;
                            }
                        }
                    }
                }

                if (find == false) {
                    ret += "\"" + SpecTranslateUtils.translateToChinese(specName) + "\",";
                }
            }
        }

        return ret.substring(0, ret.length() - 1);

    }

    // 当前已选择的规格
    public SpannableString currentSpannableSpecs() {
        String          ret             = currentSpecs();
        SpannableString spannableString = new SpannableString(ret);

        if (ret.contains("已选择:均码")) {
            spannableString.setSpan(new TextAppearanceSpan(context, R.style.SpecDescription333), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new TextAppearanceSpan(context, R.style.SpecDescription333), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new TextAppearanceSpan(context, R.style.SpecDescription999), 4, ret.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            for (String selectedValue : cache_selectedSpecs.values()) {

                if (TextUtils.isEmpty(selectedValue)) continue;

                selectedValue = "\"" + selectedValue + "\"";
                int start = ret.indexOf(selectedValue);
                int end   = start + selectedValue.length();
                if (start >= 0 && end <= ret.length()) {
                    spannableString.setSpan(new TextAppearanceSpan(context, R.style.SpecDescriptionRed), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        return spannableString;
    }

    // 是否单一Specs
    public boolean isSignleProduct() {
        return _isSingleSpecAndValue;
    }

    // 是否是Style
    public boolean isStyle(String specName) {

        if (cache_productData != null && cache_productData.skuInfo != null && cache_productData.skuInfo.style != null) {

            if (cache_productData.skuInfo.style.name.equals(specName)) {
                return true;
            }
        }
        return false;

    }

    // 获取Style Image
    public String getStyleImg(String specValue) {

        if (cache_productData != null && cache_productData.skuInfo != null && cache_productData.skuInfo.style != null && cache_productData.skuInfo.style.skustylelist != null && cache_productData.skuInfo.style.skustylelist.size() > 0) {
            for (ProductDetailBean.StyleListData styleListData : cache_productData.skuInfo.style.skustylelist) {
                if (styleListData.value.equals(specValue)) {

                    return styleListData.imgCover;

                }
            }
        }
        return null;

    }

    // 判断spec name是否是style name
    public boolean isSpecNameEqualStyle(String specName) {
        if (cache_productData != null && cache_productData.skuInfo != null && cache_productData.skuInfo.style != null && !TextUtils.isEmpty(cache_productData.skuInfo.style.name) && cache_productData.skuInfo.style.name.equals(specName)) {
            return true;
        }
        return false;
    }

    // Assistant Func 打印可选的选项
    public void printAvailable() {

        System.out.println("----------打印可选的选项(Begin)----------");
        for (AvailableSpectItemValue item : getAvailableSpecs()) {
            System.out.println(item.toString());
        }
        System.out.println("----------打印可选的选项(End)----------");

    }

    // 将Key和Value外面加Quote
    public String addQuoatation(String str) {
        if (!str.contains("\"")) {
            return "\"" + str + "\"";
        }else if (!str.contains("\'")) {
            return "\'" + str + "\'";
        }else {
            return "\'" + str.replace('\"', '\'') + "\'";
        }
    }


//    // 内部管理对象
    public class AssistSpecItem {
        public ArrayList<String> specs;
        public ArrayList<String> values;
        int    stock;
        String skuid;
    }

    public class AvailableSpectItemValue {
        public String          spec;
        public HashSet<String> availableValues;

        public AvailableSpectItemValue() {
            availableValues = new HashSet<String>();
        }

        @Override
        public String toString() {

            String ret = spec;

            for (String value : availableValues) {
                ret += "[" + value + "]";
            }

            return spec;

        }
    }

}


