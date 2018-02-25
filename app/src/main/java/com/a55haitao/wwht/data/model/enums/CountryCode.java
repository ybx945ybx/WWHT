package com.a55haitao.wwht.data.model.enums;

/**
 * 国家代码
 * <p/>
 * Created by 陶声 on 16/7/18.
 */
public enum CountryCode {
    CHINA("中国", 86),
    USA("美国", 1),
    JAPAN("日本", 81),
    KOREA("韩国", 82),
    CANADA("加拿大", 1),
    AUSTRALIA("澳大利亚", 61),
    UK("英国", 44),
    FRANCE("法国", 33),
    ITALY("意大利", 39),
    GERMANY("德国", 49),
    THAILAND("泰国", 66),
    SINGAPORE("新加坡", 65),
    RUSSIA("俄罗斯", 7),
    NEW_ZEALAND("新西兰", 64);

    private int code;
    private String name;

    CountryCode(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
