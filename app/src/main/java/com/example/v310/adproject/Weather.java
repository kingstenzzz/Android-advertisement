package com.example.v310.adproject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Weather extends AsyncTask<String,Integer,String> {


    private Context context;
    public static String weaText;
    String location;
    private String weatherAPI = "https://free-api.heweather.com/s6/weather/now?key=14134781d3024bf3945b32caeebf29ae&location=";

    public Weather (Context context){
        this.context=context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream = null;
        StringBuffer stringBuffer = null;

        try {
            //创建URL对象，接收api
            this.location=strings[0];
            URL url = new URL(weatherAPI+strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //判断是否联网
            if(httpURLConnection.getResponseCode()==200){
                inputStream = httpURLConnection.getInputStream();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();
            String temp = null;
            while((temp=bufferedReader.readLine())!=null){
                stringBuffer.append(temp);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return stringBuffer.toString();
    }

    @Override

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            //创建jsonobject对象，接收字符串
            JSONObject jsonObject  = new JSONObject(s);
            //创建jsonarray对象，接收数组
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            //创建jsonobject对象，接收数组的第一个对象
            JSONObject jsonObjectindex = jsonArray.getJSONObject(0);
            //创建jsonobject对象，接收对象
            JSONObject jsonObjectnow = jsonObjectindex.getJSONObject("now");
            //读取数据
            String cond_txt = jsonObjectnow.getString("cond_txt");
            String tmp = jsonObjectnow.getString("tmp");
            String wind_dir = jsonObjectnow.getString("wind_dir");
            String wind_sc  = jsonObjectnow.getString("wind_sc");
            //设置值
            /*
            weatherTv.setText(cond_txt);
            tempTv.setText(tmp+"℃");
            windTv.setText(wind_dir+wind_sc+"级");
            */
            String string_value=context.getResources().getString(R.string.WeaText).toString();
            weaText=String.format(string_value,location,cond_txt,tmp,wind_dir+wind_sc+"级");
            MainActivity.mtextview.setText(Weather.weaText);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
