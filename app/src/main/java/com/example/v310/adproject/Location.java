package com.example.v310.adproject;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

import io.vov.vitamio.MediaScanner;


public  class Location   {
    public  final int SHOW_LOCATION = 0;
    private LocationManager locationManager;
    private String locationProvider;
    Context context;
    public String position;
    static final int LOCATION_SHOW =1;




    Location(Context context)
    {
        this.context = context;


        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if(providers.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else{
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return ;
        }
        //获取Location
        android.location.Location location = locationManager.getLastKnownLocation(locationProvider);

        if(location!=null){
            //不为空,显示地理位置经纬度

            showLocation(location);
        }else{
            Toast.makeText(context, "location为空", Toast.LENGTH_SHORT).show();
        }
        //监视地理位置变化
        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

    }
    public void showLocation(final android.location.Location location){
		/*String locationStr = "维度：" + location.getLatitude() +"\n"
				+ "经度：" + location.getLongitude();
		postionView.setText(locationStr);*/
        final  StringBuilder url = new StringBuilder();

        new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    //组装反向地理编码的接口位置
                    url.append("http://api.map.baidu.com/geocoder?output=json&location=");
                    url.append(location.getLatitude()).append(",");
                    url.append(location.getLongitude());
                    url.append("&ak=2239360806313d2293b4093a16982e2b");
                    HttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url.toString());
                    //httpGet.addHeader("Accept-Language","zh-CN");
                    HttpResponse response = client.execute(httpGet);
                    if(response.getStatusLine().getStatusCode() == 200){
                        HttpEntity entity = response.getEntity();
                        String res = EntityUtils.toString(entity);
                        //解析
                        JSONObject jsonObject = new JSONObject(res);

                        //获取results节点下的位置信息
                        //JSONArray resultArray = jsonObject.getJSONArray("result");
                        if(jsonObject.length() > 0){
                            JSONObject resultObject=jsonObject.getJSONObject("result");
                            JSONObject cityBoject=resultObject.getJSONObject("addressComponent");
                            //JSONObject addressJson=resultObject.getJSONObject("formatted_address");
                            // JSONObject obj = resultArray.getJSONObject(0);
                            //取出格式化后的位置数据
                            position=cityBoject.getString("city");
                            new Weather(context).execute(position);




                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    static LocationListener locationListener =  new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(android.location.Location location) {



        }
    };

}
