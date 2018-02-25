package com.a55haitao.wwht.data.net;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by 董猛 on 16/7/19.
 */
public class PullParser {

    private ArrayList<String> princeList ;
    private ArrayList<ArrayList<String>> cityList ;
    private ArrayList<ArrayList<ArrayList<String>>> districtList ;


    /**
     * 解析XML
     * @param context
     * @throws XmlPullParserException
     * @throws IOException
     */
    public void parseFromXml(Context context) throws XmlPullParserException, IOException {


        ArrayList<String> temp1 = new ArrayList<>() ;
        ArrayList<ArrayList<String>> temp2 = new ArrayList<>();
        ArrayList<String> temp3 = new ArrayList<>();

        //创建一个pull解析对象
        XmlPullParser parse = XmlPullParserFactory.newInstance().newPullParser();

        //指定输入流
        parse.setInput(context.getAssets().open("province_data.xml"),"UTF-8");

        //的到解析的第一个编号
        int event = parse.getEventType() ;
        while (event != XmlPullParser.END_DOCUMENT){
            switch (event) {
                case XmlPullParser.START_DOCUMENT :
                    princeList = new ArrayList<>();
                    cityList = new ArrayList<>();
                    districtList = new ArrayList<>();

                    break;
                case XmlPullParser.START_TAG :
                    if ("province".equals(parse.getName())){
                        princeList.add(parse.getAttributeValue(0));
                        temp1 = new ArrayList<>();
                        temp2 = new ArrayList<>();
                    } else if ("city".equals(parse.getName())){
                        temp1.add(parse.getAttributeValue(0));
                        temp3 = new ArrayList<>();
                    } else if ("district".equals(parse.getName())){
                        temp3.add(parse.getAttributeValue(0));
                    }
                    break;
                case XmlPullParser.END_TAG :
                    if ("province".equals(parse.getName())){
                        cityList.add(temp1);
                        districtList.add(temp2);
                    } else if ("city".equals(parse.getName())){
                        temp2.add(temp3);
                    }
                    break;
            }
            event = parse.next() ;
        }
    }


    /**
     * 解析JSON文件
     */
    public void parseFromJson(Context context) throws IOException, JSONException {

        String str = readFile(context);

        if (TextUtils.isEmpty(str)){
            return;
        }

        ArrayList<String> temp1  ;
        ArrayList<ArrayList<String>> temp2 ;
        ArrayList<String> temp3 ;

        princeList = new ArrayList<>();
        cityList = new ArrayList<>();
        districtList = new ArrayList<>();

        JSONArray provinceJsonArray = new JSONArray(str) ;

        for (int i = 0; i < provinceJsonArray.length(); i++) {
            JSONObject provinceItemJSONObject = provinceJsonArray.getJSONObject(i);

            //省的名字
            String provinceName = provinceItemJSONObject.getString("name");
            princeList.add(provinceName);
            temp1 = new ArrayList<>();
            temp2 = new ArrayList<>();

            JSONArray cityJSONArray  = provinceItemJSONObject.getJSONArray("list");

            for (int j = 0; j < cityJSONArray.length(); j++) {

                JSONObject cityItemJSONObject = cityJSONArray.getJSONObject(j);

                String cityName = cityItemJSONObject.getString("name");
                temp1.add(cityName);
                temp3 = new ArrayList<>();

                JSONArray distinctJSONArray  = cityItemJSONObject.getJSONArray("list");
                for (int k = 0; k < distinctJSONArray.length() ; k++) {
                    temp3.add(distinctJSONArray.getString(k)) ;
                }
                temp2.add(temp3);

            }
            cityList.add(temp1);
            districtList.add(temp2) ;
        }

    }

    @NonNull
    private String readFile(Context context) throws IOException {
        InputStream is = context.getAssets().open("province_data.json");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] bytes = new byte[1024 *1024] ;

        int len = -1 ;
        while ((len = is.read(bytes)) != -1){
            bos.write(bytes,0,len);
        }
        is.close();

        String json = new String(bos.toString()) ;
        bos.close();

        return json;
    }

    public ArrayList<String> getPrinceList() {
        return princeList;
    }

    public ArrayList<ArrayList<String>> getCityList() {
        return cityList;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getDistrictList() {
        return districtList;
    }
}
