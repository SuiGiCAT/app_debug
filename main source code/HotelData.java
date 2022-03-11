package com.example.myapplication1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//1.获取大阪府所有的酒店坐标和 middle
//2.获取高规所有的酒店坐标和信息 small
//3.如果需要detail酒店信息需要判断code不为null才进行

//a class used to search for hotel info
public class HotelData {
    //default
    String aUrl = "https://app.rakuten.co.jp/services/api/Travel/SimpleHotelSearch/20170426?format=" +
            "json&largeClassCode=japan&datumType=1&middleClassCode=osaka&smallClassCode=hokubu&applicationId=" +
            "1090757631029247105";
    //needed
    String largeClassCode ="japan";
    String detailClassCode = null;
    String smallClassCode = null;
    String middleClassCode = null;
    List<Hotel> hotelList = new ArrayList<>();
    int test =0;
    public HotelData(LocalCode lc){

        this.middleClassCode = lc.middleClassCode;
        this.smallClassCode =lc.smallClassCode;
        this.detailClassCode =lc.detailClassCode;
    }
    public void getDetailData()
    {
        if(this.detailClassCode!=null)
        {
            //all hotels in detail scale
            this.aUrl = "https://app.rakuten.co.jp/services/api/Travel/SimpleHotelSearch/20170426?format=json" +
                    "&datumType=1&largeClassCode=japan&middleClassCode=" +
                    this.middleClassCode +
                    "&smallClassCode=" +
                    this.smallClassCode +
                    "&detailClassCode=" +
                    this.detailClassCode + "&applicationId=1090757631029247105";
        }
        try {
            URL url =new URL(aUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            bufferedReader.close();
            urlConnection.disconnect();
            JSONObject info = new JSONObject(stringBuilder.toString());
            JSONArray hotels = new JSONArray(info.getString("hotels"));
            // get all hotels info
            int i=0;
            for(i = 0 ;i < hotels.length() ;i++)
            {
                JSONObject cnm = new JSONObject(hotels.get(i).toString());
                JSONArray hotelInfo = new JSONArray(cnm.getString("hotel"));
                JSONObject hotelBasicInfos = new JSONObject(hotelInfo.get(0).toString());
                JSONObject hotelBasicInfo =new JSONObject(hotelBasicInfos.getString("hotelBasicInfo"));
                Hotel hotel = new Hotel();
                hotel.access = hotelBasicInfo.getString("access");
                hotel.address2 = hotelBasicInfo.getString("address2");
                hotel.hotelName = hotelBasicInfo.getString("hotelName");
                hotel.telephoneNo = hotelBasicInfo.getString("telephoneNo");
                hotel.latitude = hotelBasicInfo.getDouble("latitude");
                hotel.longitude = hotelBasicInfo.getDouble("longitude");
                hotelList.add(hotel);

            }
            test = i;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    public void getSmallData()
    {
        //all hotels in small scale 工事中
        this.aUrl = "https://app.rakuten.co.jp/services/api/Travel/SimpleHotelSearch/20170426?format=json" +
                "&datumType=1&largeClassCode=japan&middleClassCode=" +
                this.middleClassCode +
                "&smallClassCode=" +
                this.smallClassCode +
                "&applicationId=1090757631029247105";
        this.getDetailData();
    }
    public void getMiddleData()
    {
        //all hotels in middle scale 工事中
        aUrl = "https://app.rakuten.co.jp/services/api/Travel/SimpleHotelSearch/20170426?format=json" +
                "&datumType=1&largeClassCode=japan&middleClassCode=" +
                this.middleClassCode +
                "&applicationId=1090757631029247105";
    }
}
