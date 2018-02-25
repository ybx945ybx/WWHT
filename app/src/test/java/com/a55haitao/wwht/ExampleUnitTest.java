package com.a55haitao.wwht;

import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testExceptiom() {
        String json = "[{\n" +
                "            \"uri\": \"http:\\/\\/search.dev.55haitao.com\\/mallhome\\/VIPweekM\\/13\",\n" +
                "            \"name\": \"\",\n" +
                "            \"image\": \"http:\\/\\/st-prod.b0.upaiyun.com\\/zeus\\/2016\\/11\\/24\\/39abc1764de651d2c13e02d96b8128f2!\\/format\\/jpg\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"uri\": \"55haitao:\\/\\/Product\\/55haitao:\\/\\/Product\\/84a0a4e7e245e279ef562da1b0e47d6c\",\n" +
                "            \"name\": \"\",\n" +
                "            \"image\": \"http:\\/\\/st-prod.b0.upaiyun.com\\/zeus\\/2016\\/11\\/21\\/4a20597d9e02a3314e4a2eeef43837cc!\\/format\\/jpg\"\n" +
                "          }]";
        try {
            ExampleUnitTest test = new Gson().fromJson(json, ExampleUnitTest.class);
            JSONObject jsonObject = new JSONObject(json);
            String a = jsonObject.getString("a");
        } catch (JSONException e) {
            System.out.println("发生JSONException异常");
        } catch (JsonSyntaxException e) {
            System.out.println("发生JsonSyntaxException异常");
        }
        System.out.println("---执行结束");
    }

    @Test
    public void testArrayList() {
        ArrayList arrayList = new ArrayList(3);
        arrayList.add(0, "0");
        arrayList.add(1, "1");
        arrayList.add(2, "2");

        arrayList.set(1, "---");
        arrayList.set(2, "---");

        for (Object o : arrayList) {
            System.out.println(o);
        }

        System.out.println(arrayList.size());

    }

    @Test
    public void testGson() {
        String s = "{\n" +
                "    \"easyopts\": {\n" +
                "        \"deleted\": 5\n" +
                "    }\n" +
                "}";
        Te t = new Gson().fromJson(s, Te.class);
        System.out.println(t.deleted);
    }

    public static class Te {
        @SerializedName("deleted")
        public int deleted;
    }


}