package com.a55haitao.wwht.data.model.result;

/**
 * Created by a55 on 2017/3/28.
 */

public class AdResult {
    public Advent advert;
    public int is_available;

    public static class Advent{
        public int id;
        public String img;
        public String uri;
    }
}
