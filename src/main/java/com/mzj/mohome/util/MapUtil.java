package com.mzj.mohome.util;


import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStreamReader;

import java.net.MalformedURLException;

import java.net.URL;

import java.net.URLConnection;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSON;
import net.sf.json.JSONArray;

import net.sf.json.JSONObject;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

@Slf4j
public  class MapUtil {
    private static String key = "efb831362ea4ab89625825771a29ad39";
    public static void main(String[] args){

       /* Long s = 100089L;
        System.out.println( ( (float)s/1000));
        String start = "浙江省杭州市西湖区";

        String end = "郑州市金水区";

        String startLonLat = getLonLat(start);

        String endLonLat = getLonLat(end);

        System.out.println(startLonLat);

        System.out.println(endLonLat);*/
        /*121.253758,31.117122==121.34921,31.155279*/
       /* Long dis = getDistance("116.339303,40.011160069003","116.452562,39.93640407532");

        System.out.println(dis);*/

/*
        System.out.println("经纬度距离计算结果：" + getDistance(121.253758,31.117122, 121.34921,31.155279) + "米");
*/


        GlobalCoordinates source = new GlobalCoordinates(116.339303,40.011160069003);
        GlobalCoordinates target = new GlobalCoordinates(116.452562,39.93640407532);

        Long meter1 = (long)getDistanceMeter(source, target, Ellipsoid.Sphere);
        double meter2 = getDistanceMeter(source, target, Ellipsoid.WGS84);
        System.out.println("Sphere坐标系计算结果："+meter1 + "米");
        System.out.println("WGS84坐标系计算结果："+meter2 + "米");


    }



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




        public static double getDistance(double longitudeFrom, double latitudeFrom, double longitudeTo, double latitudeTo) {
            GlobalCoordinates source = new GlobalCoordinates(latitudeFrom, longitudeFrom);
            GlobalCoordinates target = new GlobalCoordinates(latitudeTo, longitudeTo);

            return new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target).getEllipsoidalDistance();
        }

    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo, Ellipsoid ellipsoid){
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }

}
