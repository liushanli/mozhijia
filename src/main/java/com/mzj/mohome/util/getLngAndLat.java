package com.mzj.mohome.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.mzj.mohome.vo.AddressVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.springframework.util.StringUtils;
import net.sf.json.JSONObject;

import static org.apache.commons.math3.util.FastMath.toRadians;

/**
 * @Author admin
 * @ClassName getLngAndLat
 * @Description TODO
 * @Date 2023/10/23 15:20
 */
@Slf4j
public class getLngAndLat {
    /**
     * 根据地址获得经纬度，把代码中的ak值更改为你自己的ak值
     */

    public static LatitudeAndLongitude getLngAndLat(String address) {
        address = address.replaceAll(" ","");
        LatitudeAndLongitude latAndLng = new LatitudeAndLongitude();
        System.out.println(address);
        //GET请求
        String url = "http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak=zdv9i6SuZtRFCVfXey4u49xXRkBWl5FY";
        String json = loadJSON(url);
        System.out.println(json);
        if (StringUtils.isEmpty(json))
        {
            return latAndLng;
        }
        int len = json.length();
        // 如果不是合法的json格式
        if (json.indexOf("{") != 0 || json.lastIndexOf("}") != len - 1) {
            return latAndLng;
        }
        JSONObject obj = JSONObject.fromObject(json);
        if (obj.get("status").toString().equals("0")) {
            double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
            double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
            DecimalFormat df = new DecimalFormat("#.######");
            latAndLng.setLatitude(df.format(lat));
            latAndLng.setLongitude(df.format(lng));
        }
        return latAndLng;

    }


    public static Map<String,Object> getAddressInfo(String location){
        Map<String,Object> map = new HashMap<>();
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=Dw4VqR2Z5ygmDxfEVlaz0j2cI3wx9DGn&output=json&coordtype=wgs84ll&location=" +location;
        //GET请求
        /*String json = loadJSON(url);*/
        String json = HttpsService.httpGet(url);
        log.info("==getAddressInfo=="+json);
        if (StringUtils.isEmpty(json))
        {
            return map;
        }
        int len = json.length();
        // 如果不是合法的json格式
        if (json.indexOf("{") != 0 || json.lastIndexOf("}") != len - 1) {
            return map;
        }
        JSONObject obj = JSONObject.fromObject(json);
        if (obj.get("status").toString().equals("0")) {
            map.put("formatted_address",obj.getJSONObject("result").get("formatted_address"));
            map.put("province",obj.getJSONObject("result").getJSONObject("addressComponent").get("province"));
            map.put("city",obj.getJSONObject("result").getJSONObject("addressComponent").get("city"));
            map.put("district",obj.getJSONObject("result").getJSONObject("addressComponent").get("district"));
            map.put("lng",obj.getJSONObject("result").getJSONObject("location").get("lng"));
            map.put("lat",obj.getJSONObject("result").getJSONObject("location").get("lat"));
        }
        return map;

    }

    public static String loadJSON(String url) {

        StringBuilder json = new StringBuilder();
        try {
            URL urlObj = new URL(url);
            URLConnection uc = urlObj.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ((inputLine = br.readLine()) != null) {
                json.append(inputLine);
            }
            br.close();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException=="+e);
        } catch (IOException e) {
            System.out.println("IOException=="+e);
        }
        return json.toString();

    }



    public static void main(String[] args) {
        //LatitudeAndLongitude latAndLng = getLngAndLat.getLngAndLat("上海市 钦州路840号 钦州花苑|11");
        //System.out.println(JSON.toJSONString(latAndLng));

        JSON.toJSONString(getAddressInfo("39.900000,116.400000"));
        /*LatitudeAndLongitude latAndLng = getLngAndLat.getLngAndLat("广东省广州市");
        LatitudeAndLongitude latAndLng2 = getLngAndLat.getLngAndLat("广西省南宁市");


        double distance=getLngAndLat.getDistance
                (latAndLng.getLongitude(), latAndLng.getLatitude(), latAndLng2.getLongitude(), latAndLng2.getLatitude());
        log.info("两者的距离为："+distance);
        log.info("经度：" + latAndLng.getLongitude() + "---纬度：" + latAndLng.getLatitude());
        log.info("经度：" + latAndLng2.getLongitude() + "---纬度：" + latAndLng2.getLatitude());*/
//113.27143,23.135336
        //108.37345,22.822607

       /* String origin = "23.135336,113.27143";
        String destination = "22.822607,108.37345";*/

        /*String origin = "39.923423,116.368904";
        String destination = "39.922501,116.387271";
        log.info("======-------------：fsafd1");
        Integer f = getDistances(origin,destination);*/
//121.428794，31.234748
//
//121.424510850428，31.2479605850466
        //distanceSimplifyMore();
        double lat1 = 31.234748; // 纬度
        double lon1 = 121.428794; // 经度

        double lat2 = 31.247960; // 纬度
        double lon2 = 121.424510; // 经度

        /*double f1 = calculateDistance(lat1, lon1, lat2, lon2);
        System.out.println("两个坐标之间的距离为：" + f + "");
        System.out.println("两个坐标之间的距离为：" + f1 + "");*/
        /*(double lat1, double lng1, double lat2, double lng2, double[] a)*/
        /*log.info("=======-------"+GetDistance.getDistance(origin,destination));*/
        //log.info("======-------------：fsafd2=="+f);
        //纬度,经度 origin起点，destination终点
        /*lng：经度
        lat：纬度*/

        /*String url = "http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak=zdv9i6SuZtRFCVfXey4u49xXRkBWl5FY";*/
/*<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=4IU3oIAMpZhfWZsMu7xzqBBAf6vMHcoa">*/
       /* String URL1 = "https://api.map.baidu.com/directionlite/v1/driving?origin=23.135336,113.27143&destination=22.822607,108.37345&ak=zdv9i6SuZtRFCVfXey4u49xXRkBWl5FY";
        String json1 = loadJSON(URL1);*/

        //System.out.println("=============--------------"+json1);


       /* Map<String,String> map_1 = new HashMap<>();
        map_1.put("origin","23.135336,113.27143");
        map_1.put("destination","22.822607,108.37345");
        map_1.put("steps_info","0");
        map_1.put("ak","zdv9i6SuZtRFCVfXey4u49xXRkBWl5FY");

        String json=HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving",map_1);
        //System.out.println("====json==="+json);*/
        /*JSONObject jsonObject = JSONObject.fromObject(JSON.parseObject(json1));
        if(!jsonObject.getString("status").equals("0")){
            log.error("配送路线规划失败");
        }
        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance1 = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");//获取举例米
        log.info("距离为:{}",distance1);
        if (distance1>5000){
            //配送距离超过5000米
            log.info("超出配送范围");
        }*/


    }

    //113.27143,23.135336
    //108.37345,22.822607
    /**
     * 起点，终点
     * 格式为：纬度(lat)，经度（lng）
     * @param origin
     * @param destination
     * @return
     */
    public static Integer getDistances(String origin,String destination){

        String URL1 = "https://api.map.baidu.com/directionlite/v1/driving?origin="+origin+"&destination="+destination+"&ak=zdv9i6SuZtRFCVfXey4u49xXRkBWl5FY";
        String json1 = loadJSON(URL1);
        log.info("json1==="+json1);
        JSONObject jsonObject = JSONObject.fromObject(JSON.parseObject(json1));

        if(!jsonObject.getString("status").equals("0")){
            log.error("配送路线规划失败");
        }
        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance1 = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");//获取举例米
        log.info("距离为:{}",distance1);
        if (distance1>5000){
            //配送距离超过5000米
            log.info("超出配送范围");
        }
        return distance1;
    }


    /**

     * 计算两点之间真实距离

     * @return 千米

     */

    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {

        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos
                (Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d ;
    }


        private static final double EARTH_RADIUS = 6378.137; // 地球半径（单位：公里）

        public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);

            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return EARTH_RADIUS * c;
        }




    private void checkOutOfRange(String address,String ak){
        Map map=new HashMap();
        map.put("address",address);
        map.put("output","json");
        map.put("ak",ak);
        //获取店铺的经纬度坐标
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        JSONObject jsonObject = JSONObject.fromObject(JSON.parseObject(shopCoordinate));
        if(!jsonObject.getString("status").equals("0")){//0代表着地址解析成功
            log.error("店铺地址解析失败");
        }
//        { 返回的结果集举例
//            "status": 0,
//                "result": {
//            "location": {
//                "lng": 113.923201,
//                "lat": 23.105796
//            }
//        }
//        }
        //查看经纬度
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");

        String lat = location.getString("lat");
        String lng = location.getString("lng");

        //获取店铺的经纬度
        String shopLngLat=lat+","+lng;
        log.info("获取店铺的经纬度{}",shopLngLat);

        map.put("address",address);
        //获取用户收货地址的经纬度坐标
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        jsonObject = JSONObject.fromObject(JSON.parseObject(userCoordinate));
        if(!jsonObject.getString("status").equals("0")){//0代表着地址解析成功
           log.error("收获地址解析失败");
        }
        //数据解析
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat=location.getString("lat");
        lng=location.getString("lng");
        //用户收货地址经纬度坐标
        String userLngLat=lat+","+lng;

        map.put("origin",shopLngLat);
        map.put("destination",userLngLat);
        map.put("steps_info","0");

        String json=HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving",map);

        jsonObject = JSONObject.fromObject(JSON.parseObject(json));
        if(!jsonObject.getString("status").equals("0")){
            log.error("配送路线规划失败");
        }
        /*
        {
  "status": 0,
  "result": {
    "routes": [
      {
        "distance": "1449",
        "duration": "245",
        "real_time_traffic": {
          "road_id": "11607050001",
          "speed": "50",
          "delay": "0",
          "jing_jie": "100",
          "traffic_state": "normal"
        },
       .....
       */
        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");//获取举例米
        log.info("距离为:{}",distance);
        if (distance>5000){
            //配送距离超过5000米
           log.info("超出配送范围");
        }
    }



    /*public static double[] trainPolyFit(int degree, int Length){
        PolynomialCurveFitter polynomialCurveFitter = PolynomialCurveFitter.create(degree);
        double minLat = 10.0; //中国最低纬度
        double maxLat = 60.0; //中国最高纬度
        double interv = (maxLat - minLat) / (double)Length;
        List<WeightedObservedPoint> weightedObservedPoints = new ArrayList<WeightedObservedPoint>();
        for(int i = 0; i < Length; i++) {
            WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(1,  minLat + (double)i*interv, Math.cos(toRadians(x[i])));
            weightedObservedPoints.add(weightedObservedPoint);
        }
        return polynomialCurveFitter.fit(weightedObservedPoints);
    }*/


    public static double distanceSimplifyMore(double lat1, double lng1, double lat2, double lng2, double[] a) {
        //1) 计算三个参数
        double dx = lng1 - lng2; // 经度差值
        double dy = lat1 - lat2; // 纬度差值
        double b = (lat1 + lat2) / 2.0; // 平均纬度
        //2) 计算东西方向距离和南北方向距离(单位：米)，东西距离采用三阶多项式
        double Lx = (a[3] * b*b*b  + a[2]* b*b  +a[1] * b + a[0] ) * toRadians(dx) * 6367000.0; // 东西距离
        double Ly = 6367000.0 * toRadians(dy); // 南北距离
        //3) 用平面的矩形对角距离公式计算总距离
        return Math.sqrt(Lx * Lx + Ly * Ly);
    }


}

