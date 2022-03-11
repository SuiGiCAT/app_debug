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

//a class used to search for local code
public class LocalCode {
    String largeClassCode ="japan";
    String largeClassName ="日本";

    String detailClassCode, detailClassName = null;
    String smallClassCode, smallClassName = null;
    String middleClassCode, middleClassName = null;
    JSONArray middleClasses;
    URL url;
    int i,j,k = -1;
    public LocalCode(String middle, String small, String detail){
        this.middleClassName = middle;
        this.detailClassName = detail;
        this.smallClassName = small;
    }
    public LocalCode(String middle, String small)
    {
        this.middleClassName = middle;
        this.detailClassName = null;
        this.smallClassName = small;
    }
    public LocalCode(String middle)
    {
        this.middleClassName = middle;
        this.detailClassName = null;
        this.smallClassName = null;
    }
    public void getIndex()
    {
        try {
            this.url = new URL("https://app.rakuten.co.jp/services/api/Travel/GetAreaClass/20131024?format=json&formatVersion=1&applicationId=1090757631029247105");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) this.url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder localBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                localBuilder.append(line);
            }
            bufferedReader.close();
            urlConnection.disconnect();
            JSONObject jsonObject = new JSONObject(localBuilder.toString());
            JSONObject areaClasses = new JSONObject(jsonObject.getString("areaClasses"));
            JSONArray largeClasses = new JSONArray(areaClasses.getString("largeClasses"));
            JSONObject data = new JSONObject(largeClasses.get(0).toString());
            JSONArray largeClass = new JSONArray(data.getString("largeClass"));
            JSONObject middleData = new JSONObject(largeClass.get(1).toString());
            this.middleClasses = new JSONArray(middleData.getString("middleClasses"));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        JSONArray b = null;JSONObject c =null;JSONObject a =null;
        JSONObject e1 =null;JSONArray f =null;JSONObject g =null;
        JSONArray h =null;JSONObject l =null;
        for (i = 0;i< this.middleClasses.length();i++)
        {
            try {
                  a = new JSONObject(this.middleClasses.get(i).toString());
                  b = new JSONArray(a.getString("middleClass"));
                  c = new JSONObject(b.get(0).toString());
                if (c.getString("middleClassName").equals(this.middleClassName))
                {
                    this.middleClassCode = c.getString("middleClassCode");
                    this.i = i;
                    e1 = new JSONObject(b.get(1).toString());
                    f = new JSONArray(e1.getString("smallClasses"));
                    for(j = 0;j< f.length() ;j++)
                    {
                        g = new JSONObject(f.get(j).toString());
                        h = new JSONArray(g.getString("smallClass"));
                        l = new JSONObject(h.get(0).toString());
                        if(l.getString("smallClassName").equals(this.smallClassName))
                        {
                            this.smallClassCode = l.getString("smallClassCode");
                            this.j = j;
                            if (h.length()>=2){
                                JSONObject e2 = new JSONObject(h.get(1).toString());
                                JSONArray f2 = new JSONArray(e2.getString("detailClasses"));
                                for(k =0 ;k<f2.length();k++)
                                {
                                    JSONObject g1 = new JSONObject(f2.get(k).toString());
                                    JSONObject h1 = new JSONObject(g1.getString("detailClass"));
                                    if(h1.getString("detailClassName").equals(this.detailClassName)){
                                        this.detailClassCode = h1.getString("detailClassCode");
                                        this.k =k;
                                        break;
                                    }
                                }
                            }else {
                                this.k = -2;
                                break;
                            }//detail list is not existed
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public String getMiddleClassCode()
    {
        return this.middleClassCode;
    }
    public String getSmallClassCode()
    {
        return this.smallClassName;
    }
    public String getDetailClassCode()
    {
        return this.detailClassName;
    }
}
