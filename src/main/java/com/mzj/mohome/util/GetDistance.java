package com.mzj.mohome.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 1.这个需要客户申请个高德API上面的key,也可以自己申请(免费的，很快)
 * */
public class GetDistance {
    public static void main(String[] args){
        String start = "北京天安门";
        String end = "上海市黄浦区西藏中路";

        String startLonLat = getLonLat(start);
        String endLonLat = getLonLat(end);

        System.out.println("起始地："+start+",经纬度："+startLonLat);
        System.out.println("终点："+end+",经纬度："+endLonLat);

        Long dis = getDistance(startLonLat,endLonLat);
        System.out.println("两点间距离："+dis+"米");
    }
    /**
     * 0.得到两个地址间距离
     * */
    public static long getDistanceByAdress(String start,String end){
        String startLonLat = getLonLat(start);
        String endLonLat = getLonLat(end);
        long dis = getDistance(startLonLat,endLonLat);
        return dis;
    }

    /**
     * 1.地址转换为经纬度
     * */
    private static String getLonLat(String address){
        //返回输入地址address的经纬度信息, 格式是 经度,纬度
        String queryUrl = "http://restapi.amap.com/v3/geocode/geo?key=9c4ac6befac0d9f329bdde7f3dbe5f35&address="+address;
        String queryResult = getResponse(queryUrl);  //高德接品返回的是JSON格式的字符串
        JSONObject job = JSONObject.parseObject(queryResult);
        JSONObject jobJSON = JSONObject.parseObject(job.get("geocodes").toString().substring(1, job.get("geocodes").toString().length()-1));
        String DZ = jobJSON.get("location").toString();
        System.out.println("经纬度："+DZ);
        return DZ;
    }

    /**
     * 2.经纬度算出两点间距离
     * */
    private static long getDistance(String startLonLat, String endLonLat){
        //返回起始地startAddr与目的地endAddr之间的距离，单位：米
        Long result = new Long(0);
        String queryUrl = "http://restapi.amap.com/v3/distance?key=9c4ac6befac0d9f329bdde7f3dbe5f35&origins="+startLonLat+"&destination="+endLonLat;
        String queryResult = getResponse(queryUrl);
        JSONObject job = JSONObject.parseObject(queryResult);
        JSONArray ja = job.getJSONArray("results");
        JSONObject jobO = JSONObject.parseObject(ja.getString(0));
        result = Long.parseLong(jobO.get("distance").toString());
        System.out.println("距离2："+result);
        return result;
    }

    /**
     * 3.发送请求
     * */
    private static String getResponse(String serverUrl){
        //用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
                result.append(line);
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
